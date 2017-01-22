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
        agile_tester_badge = "Agile Tester"
        point_scale_name = "testing_goat_scale"
        event_rule_name = "count_tests_done"
        event_test_created = "new_test"
        trigger_name = "award_agile_tester_badge"
        user = "Goat1"

        # registration
        self.check_answer(
            requests.post(self.url + "/register/", json=dict(name=app_name, password=app_password)),
            requests.codes.created,
            "Could not register."
        )

        # login with bad password
        self.check_answer(
            requests.post(self.url + "/auth/", json=dict(name=app_name, password="goat_invader")),
            requests.codes.unauthorized,
            "Error when trying to login with bad password."
        )

        # login with good password
        answer = requests.post(self.url + "/auth/", json=dict(name=app_name, password=app_password))
        self.check_answer(answer, requests.codes.ok, "Error when authenticating")
        self.assertIn("Authorization", answer.headers.keys(), "Didn't get an Authorization token.")
        self.token = "Bearer " + answer.headers["Authorization"]

        # create a badge
        self.check_answer(
            self.request("post", self.url + "/badges/", json=dict(name=agile_tester_badge)),
            requests.codes.created,
            "Could not create a badge for the application."
        )

        # create a pointScale
        self.check_answer(
            self.request("post", self.url + "/pointScales/", json=dict(name=point_scale_name)),
            requests.codes.created,
            "Could not create our pointScale."
        )

        # create an event rule
        data = dict(pointScale=point_scale_name, name=event_rule_name, event=event_test_created)
        self.check_answer(
            self.request("post", self.url + "/rules/events/", json=data),
            requests.codes.created,
            "Could not create an event rule."
        )

        # create a trigger rule
        data = dict(
            name=trigger_name, badgeAwarded=agile_tester_badge, pointScale=point_scale_name, limit=30, aboveLimit=True
        )
        self.check_answer(
            self.request("post", self.url + "/rules/triggers/", json=data),
            requests.codes.created,
            "Could not create {} trigger rule".format(trigger_name)
        )

        # create an event
        self.check_answer(
            self.request("post", self.url + "/events/", json={"type": event_test_created, "username": user}),
            requests.codes.created,
            "Could not create an event"
        )

        # check that user was indeed created and given the points

        # give more points to get the award

        # check that user gained award and points
