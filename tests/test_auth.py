import unittest

import requests

from tests.utils import BASE_URL, HTTP_METHODS
from tests.utils.mixins.database import DatabaseWiperTestMixin
from tests.utils.mixins.api import AuthenticatedRestAPIMixin


class TestAuth(DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, unittest.TestCase):
    url = BASE_URL + "/auth"
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

        self.assertEqual(len(r.json().keys()), 1, msg=self.prepare_message("No error message", r))
        self.assertIn("error", r.json().keys(), msg=self.prepare_message("No error key in response", r))
        self.check_message(r.json()["error"])

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
        self.assertEqual(
            self.request("post", self.url, json=self.default_application).status_code,
            requests.codes.bad_request
        )
