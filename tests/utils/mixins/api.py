from json import JSONDecodeError
from pprint import pformat

import requests
from abc import abstractmethod, ABCMeta

from utils import BASE_URL, HTTP_METHODS, API_ERROR_MESSAGES
from utils.mixins import PreconditionFail


class RestAPITestMixin:
    """
    This is a mixin for testing an API.

    It adds automated checking for invalid HTTP methods and checks automatically which fields are required in
    the payload sent to the server.

    It also adds convenience methods to make requests and various other helpers.
    """

    @property
    @abstractmethod
    def url(self):
        """URL to which to make the requests"""

    @staticmethod
    def prepare_message(base_message, response=None):
        """
        Prepare the error message to be displayed.

        :param base_message: the base message to explain the error
        :param response: the response got from the server to be displayed nicely
        :return: a nicely formatted error message with the content of the server's response
        """
        if response is None:
            return base_message

        try:
            res = pformat(response.json()) \
                .replace("{", "{\n ") \
                .replace("}", "\n}") \
                .replace("[", "[\n ") \
                .replace("]", "\n]") \
                .split("\n")

            count_tabs = 0
            for index, entry in enumerate(res):
                if entry.startswith("}") or entry.startswith("]"):
                    count_tabs -= 1

                res[index] = "\t" * count_tabs + entry.strip()

                if entry.endswith("{") or entry.endswith("["):
                    count_tabs += 1

            result = "\n".join(res)

        except JSONDecodeError:
            result = response.text

        return "{}\n\nStatus code: {}\nResponse:\n{}\n".format(base_message, response.status_code, result)

    @property
    def required_fields(self):
        """List of all required fields the request must give."""
        return []

    @property
    def invalid_methods(self):
        """List of all methods that are not valid by the endpoint."""
        return HTTP_METHODS

    def request(self, method, url, json=None, headers=None):
        """
        Make an http request.

        This is a proxy for the `request.request` call, that allows subclasses to add logic handling
        in the request, for example adding authentication.

        :param method: HTTP method to make
        :param url: url to which to make the request
        :param json: data to send with the request
        :param headers: headers to send with the request
        :return: the response from the server
        """
        return requests.request(method, url, json=json, headers=headers)

    def check_message(self, entry):
        """
        Check that the error message is good.

        If this is the first time we get the error message, we register its code and message in a dictionary.
        Otherwise, we fail if the code and message are not exactly the same as the previous registered.

        This is to make sure that we don't mix up error codes and send twice the same for two different errors.

        :param entry: entry containing the error code and message
        """
        if entry.get("code") is None or entry.get("message") is None:
            self.fail("Error message is missing code or message : \n\n{}\n".format(entry))
        elif API_ERROR_MESSAGES.get(entry["code"]) is None:
            # first time we get the message, we add it to the global list
            API_ERROR_MESSAGES[entry["code"]] = entry["message"]
        else:
            # we already got the same code, let's check that the same message it received
            self.assertEqual(API_ERROR_MESSAGES[entry["code"]], entry["message"])

    def check_precondition(self, response, expected_code, msg):
        if response.status_code != expected_code:
            raise PreconditionFail(self.prepare_message(
                "{}\n\nExpected {}, but got {} \n".format(msg, expected_code, response.status_code),
                response
            ))

    def test_invalid_methods(self):
        for method in self.invalid_methods:
            with self.subTest(method=method):
                self.assertEqual(self.request(method, self.url).status_code, requests.codes.not_allowed)

    def test_required_fields(self):
        # FIXME skip if "post" is not acceptable
        response = self.request("post", self.url, json=dict())
        response_keys = set(response.json().keys())

        self.assertEqual(
            response.status_code,
            requests.codes.bad_request,
            msg=self.prepare_message(
                "A call with an empty json didn't return a bad request, while some fields are required", response
            )
        )

        for field in self.required_fields:
            with self.subTest(field=field):
                self.assertIn(field, response_keys, self.prepare_message(
                    "Expected an error on field {} as this field is required, but didn't got any".format(field),
                    response
                ))
                self.check_message(response.json()[field])

        tested_fields = self.required_fields & response_keys

        self.assertEqual(
            set(response.json().keys()),
            tested_fields,
            msg="A field was marked as required by the response, but test is not requiring it"
        )


class RegisterApplicationMixin(metaclass=ABCMeta):
    default_application = dict(name="goatsy", password="goat")
    token = None

    def setUp(self):
        super().setUp()
        self.token = self.register_application(**self.default_application)

    def tearDown(self):
        super().tearDown()
        self.token = None

    def register_application(self, name, password="default"):
        """
        Register a new application with the given name and password

        :param name: name of the application to register
        :param password: password of the application
        :return: an authentication token for registering
        """
        response = requests.post(BASE_URL + "/register", json=dict(name=name, password=password))

        if response.status_code != requests.codes.created:
            raise Exception("Could not register application, aborting test")

        return response.headers["Authorization"]


class AuthenticatedRestAPIMixin(RestAPITestMixin, RegisterApplicationMixin, metaclass=ABCMeta):
    def request(self, method, url, json=None, headers=None):
        """Extend `RestAPITestMixin.request` to add the authentication header."""
        if headers is None:
            headers = dict()
        headers["Authorization"] = "Bearer {}".format(self.token)

        return super().request(method, url, json=json, headers=headers)
