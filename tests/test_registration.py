import unittest

import requests
from sqlalchemy import select

from utils import HTTP_METHODS, BASE_URL
from utils.mixins.database import DatabaseWiperTestMixin
from utils.mixins.api import RestAPITestMixin
from utils.mixins.load import ConcurrentTesterMixin
from utils.models import Application


class TestRegistration(DatabaseWiperTestMixin, RestAPITestMixin, ConcurrentTesterMixin, unittest.TestCase):
    url = BASE_URL + "/register"
    invalid_methods = HTTP_METHODS - {"post"}
    required_fields = {"name", "password"}

    application = dict(name="goatsy", password="goat")
    concurrency_tests = 1000
    request_per_concurrent_test = 10

    def test_can_register(self):
        r = requests.post(self.url, json=self.application)
        self.assertEqual(r.status_code, requests.codes.created)
        self.assertIn("Authorization", r.headers.keys())

    def test_cannot_register_twice(self):
        requests.post(self.url, json=self.application)
        r = requests.post(self.url, json=self.application)

        self.assertEqual(r.status_code, requests.codes.conflict)
        self.assertIsNotNone(
            r.json().get("name"),
            msg=self.prepare_message("No error message on field name, when there is a duplicate.", r)
        )
        self.assertCountEqual(list(r.json()["name"].keys()), ["code", "message"])

    def test_password_is_hashed(self):
        self.check_precondition(
            requests.post(self.url, json=self.application),
            requests.codes.created,
            "User couldn't be created, aborting test"
        )

        result = self.database_connection.execute(
            select([Application]).where(Application.name == self.application["name"])
        ).first()
        self.assertNotEqual(self.application["password"], result["password"], msg="Password is not hashed")

    def test_can_only_create_one_application_with_a_given_name(self):
        for i in range(self.concurrency_tests):
            app = dict(name="conc-{}".format(i), password="pass")
            res = self.request_concurrently("post", self.url, self.request_per_concurrent_test, json=app)
            self.check_only_one_created(res)


if __name__ == '__main__':
    unittest.main()
