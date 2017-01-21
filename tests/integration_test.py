from unittest import TestCase

import requests

from utils import BASE_URL
from utils.mixins.api import APITestMixin
from utils.mixins.database import DatabaseWiperTestMixin


class IntegrationTest(DatabaseWiperTestMixin, APITestMixin, TestCase):
    url = BASE_URL
    token = None

    def request(self, method, url, json=None):
        headers = {"Authorization": self.token}
        return requests.request(method, url, json=json, headers=headers)

    def check_answer(self, answer, expected, msg=None):
        msg += "\n\nRequest was done on {}.\nExpected {}, but got {}\n".format(answer.url, expected, answer.status_code)
        self.assertEqual(answer.status_code, expected, msg=self.prepare_message(msg, answer))

    def test_full_scenario(self):
        app_name = "goatsy"
        app_password = "verysecure"
        badge_name = "BestGoat"

        # registration
        self.check_answer(
            requests.post(self.url + "/register", json=dict(name=app_name, password=app_password)),
            requests.codes.created,
            "Could not register"
        )

        # login with bad password
        self.check_answer(
            requests.post(self.url + "/auth", json=dict(name=app_name, password="goat_invader")),
            requests.codes.unauthorized,
            "Error when trying to login with bad password"
        )

        # login with good password
        answer = requests.post(self.url + "/auth", json=dict(name=app_name, password=app_password))
        self.check_answer(answer, requests.codes.ok, "Error when authenticating")
        self.assertIn("Authorization", answer.headers.keys(), "Didn't get an Authorization token.")
        self.token = "Bearer " + answer.headers["Authorization"]

        # create a badge
        self.check_answer(
            self.request("post", self.url + "/badges", json=dict(name=badge_name)),
            requests.codes.created,
            "Could not create a badge for the application"
        )

        # create a pointScale

        # create an event rule

        # create a trigger rule

        # create an event

        # check that user was indeed created and given the points

        # give more points to get the award

        # check that user gained award and points