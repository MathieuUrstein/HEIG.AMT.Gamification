import unittest

import requests
from sqlalchemy import select

from utils import BASE_URL, HTTP_METHODS
from utils.mixins.database import DatabaseWiperTestMixin
from utils.mixins.api import AuthenticatedRestAPIMixin
from utils.models import User


class TestEvents(DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, unittest.TestCase):
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

    def test_two_applications_can_have_same_badge_name(self):
        self.check_precondition(
            self.request("post", self.url, json=self.event),
            requests.codes.created,
            "Could not create badge, aborting test"
        )

        new_token = "Bearer {}".format(self.register_application("goat"))
        r = requests.post(self.url, json=self.event, headers=dict(Authorization=new_token))
        self.assertEqual(r.status_code, requests.codes.created)


if __name__ == '__main__':
    unittest.main()
