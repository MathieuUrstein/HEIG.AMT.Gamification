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
        point_scale_testing = "testing_goat_scale"
        event_rule_count_tests_done = "count_tests_done"
        event_rule_count_test_fixed = "count_test_fixed"
        event_test_created = "new_test"
        event_test_fixed = "test_fixed"
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
            self.request("post", self.url + "/pointScales/", json=dict(name=point_scale_testing)),
            requests.codes.created,
            "Could not create our pointScale."
        )

        # create an event rule
        data = dict(
            pointScale=point_scale_testing, name=event_rule_count_tests_done, event=event_test_created, pointsGiven=1
        )
        self.check_answer(
            self.request("post", self.url + "/rules/events/", json=data),
            requests.codes.created,
            "Could not create an event rule."
        )

        # create a second event rule
        data = dict(
            pointScale=point_scale_testing, name=event_rule_count_test_fixed, event=event_test_fixed, pointsGiven=1
        )
        self.check_answer(
            self.request("post", self.url + "/rules/events/", json=data),
            requests.codes.created,
            "Could not create an event rule."
        )

        # create a trigger rule
        data = dict(
            name=trigger_name, badgeAwarded=agile_tester_badge, pointScale=point_scale_testing, limit=5, aboveLimit=True
        )
        self.check_answer(
            self.request("post", self.url + "/rules/triggers/", json=data),
            requests.codes.created,
            "Could not create {} trigger rule".format(trigger_name)
        )

        # create an event
        data = dict(type=event_test_created, username=user)
        self.check_answer(
            self.request("post", self.url + "/events/", json=data),
            requests.codes.created,
            "Could not create an event"
        )

        # check that user was indeed created and given the points
        users = self.request("get", self.url + "/users/").json()
        self.assertEqual(len(users), 1, "More user are present than what we prepared")
        user = users[0]

        self.assertEqual(
            len(user["points"]), 1, "User received points in more than one pointScale, which we did not create"
        )

        self.assertEqual(user["points"][0]["points"], 1, "User did not receive the one point it was awarded")

        # give more points to get the award
        for _ in range(2):
            data = dict(type=event_test_fixed, username=user)
            self.check_answer(
                self.request("post", self.url + "/events/", json=data),
                requests.codes.created,
                "Could not create an additional event"
            )

        # check that user gained award and points
