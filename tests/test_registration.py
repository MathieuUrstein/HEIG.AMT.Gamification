import unittest

import requests

from tests.utils import DatabaseWiperTestMixin, RestAPITestUtilities, HTTP_METHODS, BASE_URL


class TestRegistration(DatabaseWiperTestMixin, RestAPITestUtilities, unittest.TestCase):
    url = BASE_URL + "/register"
    invalid_methods = HTTP_METHODS - {"post"}
    required_fields = {"name", "password"}

    user = dict(name="goatsy", password="goat")

    def test_can_register(self):
        r = requests.post(self.url, json=self.user)
        self.assertEqual(r.status_code, requests.codes.created)
        self.assertIn("Authorization", r.headers.keys())

    def test_cannot_register_twice(self):
        requests.post(self.url, json=self.user)
        r = requests.post(self.url, json=self.user)

        self.assertEqual(r.status_code, requests.codes.conflict)
        self.assertIsNotNone(r.json().get("name"), msg="No error message on field name, when there is a duplicate.")
        self.assertListEqual(r.json()["name"].keys(), ["message", "code"])


if __name__ == '__main__':
    unittest.main()
