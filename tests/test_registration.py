import unittest

import requests
from sqlalchemy import select

from tests.models import Application
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
        self.assertIsNotNone(
            r.json().get("name"),
            msg=self.prepare_message("No error message on field name, when there is a duplicate.", r)
        )
        self.assertListEqual(r.json()["name"].keys(), ["message", "code"])

    def test_password_is_hashed(self):
        r = requests.post(self.url, json=self.user)
        self.assertEqual(
            r.status_code,
            requests.codes.created,
            msg=self.prepare_message("User couldn't be created, aborting test", r)
        )

        result = self.database_connection.execute(
            select([Application]).where(Application.name == self.user["name"])
        ).first()
        self.assertNotEqual(self.user["password"], result["password"], msg="Password is not hashed")

if __name__ == '__main__':
    unittest.main()
