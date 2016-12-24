import unittest

import requests

from tests.mixins import DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, BASE_URL, HTTP_METHODS


class TestAuth(DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, unittest.TestCase):
    url = BASE_URL + "/auth"
    invalid_methods = HTTP_METHODS - {"post"}
    required_fields = {"name", "password"}

    def _check_bad_authentication_answer(self, application):
        r = requests.post(self.url, json=application)
        self.assertEqual(
            r.status_code,
            requests.codes.forbidden,
            msg=self.prepare_message("authentication didn't get the correct response code", r)
        )
        self.assertNotIn("Authorization", r.headers.keys(), msg="Got an authorization token with a bad authorization")

        for key in ["name", "password"]:
            self.assertNotIn(r.json().keys(), key, msg=self.prepare_message(
                "Error message should not give information on what is wrong for authentication", r
            ))

        self.assertEqual(len(r.json().keys()), 1, msg=self.prepare_message("No error message", r))
        self.assertIn(r.json().keys(), "error", msg=self.prepare_message("No error key in response", r))
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
