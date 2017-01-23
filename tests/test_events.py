import unittest

import os
import requests
from copy import deepcopy
from sqlalchemy import select

from utils import BASE_URL, HTTP_METHODS
from utils.mixins.concurrency import ConcurrentTesterMixin, skip_concurrency_env_variable
from utils.mixins.database import DatabaseWiperTestMixin
from utils.mixins.api import AuthenticatedRestAPIMixin
from utils.models import User


class TestEvents(DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, ConcurrentTesterMixin, unittest.TestCase):
    url = BASE_URL + "/events/"
    invalid_methods = HTTP_METHODS - {"post"}
    required_fields = {"type", "username"}

    event = dict(type="death", username="goatsy")

    def test_cannot_create_event_when_not_authenticated(self):
        self.assertEqual(requests.post(self.url, json=self.event).status_code, requests.codes.unauthorized)

    def test_can_create_event(self):
        r = self.request("post", self.url, json=self.event)

        self.assertEqual(r.status_code, requests.codes.created, msg=self.prepare_message(
            "Could not create new event", r
        ))

        self.assertNotIn("Location", r.headers.keys(), msg=self.prepare_message(
            "When creating events, no location header should be set", r
        ))

        self.assertEqual(self.database_connection.execute(select([User])).rowcount, 1)

    def test_can_create_event_with_username_as_integer(self):
        self.assertEqual(
            self.request("post", self.url, json=dict(type="death", username=2)).status_code,
            requests.codes.created
        )

    @unittest.skipIf(
        os.environ.get(skip_concurrency_env_variable, False),
        "Skipping concurrency test because {} is set".format(skip_concurrency_env_variable)
    )
    def test_can_send_events_concurrently(self):
        self.check_precondition(
            self.request("post", BASE_URL + "/badges/", json=dict(name="badge")),
            requests.codes.created,
            "Could not create a badge for the application."
        )
        self.check_precondition(
            self.request("post", BASE_URL + "/pointScales/", json=dict(name="pointscale")),
            requests.codes.created,
            "Could not create our pointScale."
        )
        data = dict(
            pointScale="pointscale", name="rule-event", event="event", pointsGiven=1
        )
        self.check_precondition(
            self.request("post", BASE_URL + "/rules/events/", json=data),
            requests.codes.created,
            "Could not create an event rule."
        )
        data = dict(
            name="trigger", badgeAwarded="badge", pointScale="pointscale",
            limit=self.request_per_concurrent_test, aboveLimit=True
        )
        self.check_precondition(
            self.request("post", BASE_URL + "/rules/triggers/", json=data),
            requests.codes.created,
            "Could not create trigger rule"
        )

        headers = {"Authorization": "Bearer {}".format(self.token)}

        for i in range(self.concurrency_tests):
            event = deepcopy(self.event)
            event["username"] = event["username"] + "-" + str(i)
            res = self.request_concurrently(
                "post", self.url, self.request_per_concurrent_test, json=self.event, headers=headers
            )

            if not self.all_with_status(requests.codes.created, res):
                self.fail(
                    self._format_msg("Not all events were accepted", self._format_responses(self._parse_responses(res)))
                )

            r = self.request("get", BASE_URL + "/users/")
            self.assertEqual(r.status_code, requests.codes.ok, self.prepare_message("Couldn't get list of users", r))

            for u in r.json():
                if u["name"] != event["username"]:
                    continue

                self.assertEqual(len(u["badges"]), 1, msg=self.prepare_message("User did not receive his badge", r))
                break


if __name__ == '__main__':
    unittest.main()
