from json import JSONDecodeError
from pprint import pformat

import requests
import sys

from abc import abstractmethod, ABCMeta
from sqlalchemy import MetaData
from sqlalchemy import create_engine
from sqlalchemy.exc import OperationalError

from tests.utils import DATABASE_CONNECTION_URL, print_customization_and_exit, BASE_URL, HTTP_METHODS, \
    API_ERROR_MESSAGES


class DatabaseAccessMixin:
    database_connection = None
    database_engine = None
    database_meta = None

    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        cls.database_engine = create_engine(DATABASE_CONNECTION_URL)
        cls.database_meta = MetaData()

        try:
            cls.database_meta.reflect(bind=cls.database_engine)
            cls.database_connection = cls.database_engine.connect()
        except OperationalError as e:
            print("Error establishing database connection :", e.orig, file=sys.stderr)
            print_customization_and_exit()

        try:
            requests.get(BASE_URL)
        except requests.exceptions.ConnectionError:
            print("Error connection to {}".format(BASE_URL), file=sys.stderr)
            print("Is the app up and running and the url correct ?", file=sys.stderr)
            print_customization_and_exit()

    @classmethod
    def tearDownClass(cls):
        super().tearDownClass()
        cls.database_connection.close()
        cls.database_engine.dispose()


class DatabaseWiperTestMixin(DatabaseAccessMixin):
    def tearDown(self):
        super().tearDown()

        for table in reversed(self.database_meta.sorted_tables):
            self.database_connection.execute(table.delete())


class RestAPITestMixin:
    @property
    @abstractmethod
    def url(self):
        """URL to which to make the requests"""

    @property
    def required_fields(self):
        """List of all required fields the request must give."""
        return []

    @property
    def invalid_methods(self):
        """List of all methods that are not valid by the endpoint."""
        return HTTP_METHODS

    def check_message(self, entry):
        if API_ERROR_MESSAGES.get(entry["code"]) is None:
            # first time we get the message, we add it to the global list
            API_ERROR_MESSAGES[entry["code"]] = entry["message"]
        else:
            # we already got the same code, let's check that the same message it received
            self.assertEqual(API_ERROR_MESSAGES[entry["code"]], entry["message"])

    def check_field_required(self, field_name, response):
        self.assertEqual(response.status_code, requests.codes.bad_request)
        self.assertIn(field_name, response.json().keys())
        self.check_message(response.json()[field_name])

    def test_invalid_methods(self):
        for method in self.invalid_methods:
            with self.subTest(method=method):
                self.assertEqual(requests.request(method, self.url).status_code, requests.codes.not_allowed)

    def test_required_fields(self):
        request = requests.post(self.url, json=dict())

        for field in self.required_fields:
            with self.subTest(field=field):
                self.check_field_required("name", request)

        self.assertEqual(
            set(request.json().keys()),
            self.required_fields,
            msg="A field was marked as required by the response, but test is not requiring it"
        )

    @staticmethod
    def prepare_message(base_message, response=None):
        if response is None:
            return base_message

        try:
            result = pformat(response.json(), indent=4).replace("{", "{\n ").replace("}", "\n}").replace("[", "[\n ").replace("]", "\n]")
        except JSONDecodeError:
            result = response.text

        return "{}\n\nStatus code: {}\nResponse:\n{}\n".format(base_message, response.status_code, result)


class AuthenticatedRestAPIMixin(RestAPITestMixin, metaclass=ABCMeta):
    default_application = dict(name="goatsy", password="goat")
    token = None

    def setUp(self):
        super().setUp()

        response = requests.post(BASE_URL + "/register", json=self.default_application)

        if response.status_code != requests.codes.created:
            raise Exception("Could not register application, aborting test")

        self.token = response.headers["Authorization"]

    def tearDown(self):
        super().tearDown()
        self.token = None
