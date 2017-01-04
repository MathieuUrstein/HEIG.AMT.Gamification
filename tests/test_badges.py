import unittest

import requests
from sqlalchemy import select

from tests.utils import BASE_URL, HTTP_METHODS
from tests.utils.mixins.database import DatabaseWiperTestMixin
from tests.utils.mixins.api import AuthenticatedRestAPIMixin
from tests.utils.models import Badge


class TestBadges(DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, unittest.TestCase):
    url = BASE_URL + "/badges"
    invalid_methods = HTTP_METHODS - {"get", "post"}
    required_fields = {"name"}

    badge = dict(name="unbelievable achievement")

    def test_cannot_create_badge_when_not_authenticated(self):
        self.assertEqual(requests.post(self.url, json=self.badge).status_code, requests.codes.unauthorized)

    def test_can_create_badge(self):
        r = self.request("post", self.url, json=self.badge)
        self.assertEqual(r.status_code, requests.codes.created)
        self.assertIn("Location", r.headers.keys(), msg=self.prepare_message("Didn't get location of new resource", r))
        self.assertEqual(self.database_connection.execute(select([Badge])).rowcount, 1)

    def test_cannot_create_badge_twice(self):
        self.assertEqual(self.request("post", self.url, json=self.badge).status_code, requests.codes.created)

        r = self.request("post", self.url, json=self.badge)
        self.assertEqual(r.status_code, requests.codes.conflict)
        self.assertIn(
            "name",
            r.json().keys(),
            msg=self.prepare_message("No error on the name, which threw a conflict", r)
        )
        self.check_message(r.json()["name"])

    def test_two_applications_can_have_same_badge_name(self):
        self.assertEqual(self.request("post", self.url, json=self.badge).status_code, requests.codes.created)

        new_token = "Bearer {}".format(self.register_application("goat"))
        r = requests.post(self.url, json=self.badge, headers=dict(Authorization=new_token))
        self.assertEqual(r.status_code, requests.codes.created)

    def test_can_retrieve_badge(self):
        r = self.request("post", self.url, json=self.badge)
        self.assertEqual(
            r.status_code, requests.codes.created,
            msg=self.prepare_message("Could not create badge, aborting test", r)
        )

        r = self.request("get", r.headers["Location"])
        self.assertEqual(r.status_code, requests.codes.ok, msg=self.prepare_message("Could not retrieve badge", r))
        self.assertEqual(r.json()["name"], self.badge["name"])

    @unittest.skip("Image upload is not yet implemented")
    def test_can_add_image_to_badge(self):
        raise NotImplementedError()


if __name__ == '__main__':
    unittest.main()
