import unittest

import requests

from utils import BASE_URL, HTTP_METHODS
from utils.mixins.database import DatabaseWiperTestMixin
from utils.mixins.api import RestAPITestMixin, RegisterApplicationMixin


class TestAuth(DatabaseWiperTestMixin, RestAPITestMixin, RegisterApplicationMixin, unittest.TestCase):
    url = BASE_URL + "/auth/"
    invalid_methods = HTTP_METHODS - {"post"}
    required_fields = {"name", "password"}

    def _check_bad_authentication_answer(self, application):
        r = requests.post(self.url, json=application)
        self.assertEqual(
            r.status_code,
            requests.codes.unauthorized,
            msg=self.prepare_message("authentication didn't get the correct response code", r)
        )
        self.assertNotIn("Authorization", r.headers.keys(), msg="Got an authorization token with a bad authorization")

        for key in ["name", "password"]:
            self.assertNotIn(key, r.json().keys(), msg=self.prepare_message(
                "Error message should not give information on what is wrong for authentication", r
            ))

        self.assertEqual(len(r.json().keys()), 2, msg=self.prepare_message("No error message", r))
        self.check_message(r.json())

    def test_can_authenticate(self):
        r = requests.post(self.url, json=self.default_application)
        self.assertEqual(r.status_code, requests.codes.ok)
        self.assertIn("Authorization", r.headers.keys())

    def test_cannot_authenticate_without_registered_application_name(self):
        application = self.default_application
        application["name"] = "goat_invader"
        self._check_bad_authentication_answer(application)

    def test_cannot_authenticate_with_wrong_password(self):
        application = self.default_application
        application["password"] = "bad_password"
        self._check_bad_authentication_answer(application)

    def test_cannot_authenticate_when_authenticated(self):
        r = self.request(
            "post", self.url, json=self.default_application, headers={"Authorization": "Bearer {}".format(self.token)}
        )
        self.assertEqual(r.status_code, requests.codes.bad_request)
        self.check_message(r.json())
