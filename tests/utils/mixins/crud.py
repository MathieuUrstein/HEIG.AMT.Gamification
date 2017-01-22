import unittest

import os
import requests
from abc import ABCMeta, abstractmethod
from copy import deepcopy
from sqlalchemy import select

from utils import HTTP_METHODS, BASE_URL
from utils.mixins import PreconditionFail
from utils.mixins.database import DatabaseWiperTestMixin
from utils.mixins.api import AuthenticatedRestAPIMixin
from utils.mixins.concurrency import ConcurrentTesterMixin, skip_concurrency_env_variable


class CRUDTestMixin(DatabaseWiperTestMixin, AuthenticatedRestAPIMixin, ConcurrentTesterMixin, metaclass=ABCMeta):
    invalid_methods = HTTP_METHODS - {"get", "post"}

    @property
    @abstractmethod
    def model(self):
        """Get the model for the test."""

    @property
    @abstractmethod
    def object(self):
        """Get the base object to try to create."""

    @property
    def updated_object(self):
        """Get the updated object for update tests."""
        o = deepcopy(self.object)
        o["name"] += "-copy"
        return o

    def _create_base_object(self):
        r = self.request("post", self.url, json=self.object)

        self.check_precondition(
            r,
            requests.codes.created,
            self.prepare_message("Could not create {}".format(self.model.__table__), r)
        )

    def test_cannot_create_object_when_not_authenticated(self):
        self.assertEqual(requests.post(self.url, json=self.object).status_code, requests.codes.unauthorized)

    def test_can_create_object(self):
        r = self.request("post", self.url, json=self.object)
        self.assertEqual(r.status_code, requests.codes.created, msg=self.prepare_message("Could not create object", r))
        self.assertIn("Location", r.headers.keys(), msg=self.prepare_message("Didn't get location of new resource", r))
        self.assertEqual(self.database_connection.execute(select([self.model])).rowcount, 1)

    def test_cannot_create_object_twice(self):
        self._create_base_object()
        r = self.request("post", self.url, json=self.object)
        self.assertEqual(r.status_code, requests.codes.conflict)
        self.assertIn(
            "name",
            r.json().keys(),
            msg=self.prepare_message("No error on the name, which threw a conflict", r)
        )
        self.check_message(r.json()["name"])

    def test_can_modify_object_with_put(self):
        self._create_base_object()
        # modify object
        r = self.request("put", self.url + self.object["name"] + "/", json=self.updated_object)
        self.assertEqual(
            r.status_code,
            requests.codes.ok,
            self.prepare_message("Could not update {}".format(self.model.__table__), r)
        )

        # try fetching it from new road
        r = self.request("get", self.url + self.updated_object["name"] + "/")
        self.assertEqual(
            r.json(),
            self.updated_object,
            self.prepare_message("Updated {} is not the same as the one got".format(self.model.__table__), r)
        )

        # try fetching it from old road
        r = self.request("get", self.url + self.object["name"] + "/")
        self.assertEqual(
            r.status_code,
            requests.codes.not_found,
            self.prepare_message("Old {} is still accessible even though it should not.".format(self.model.__table__))
        )

    def test_cannot_modify_with_patch(self):
        self._create_base_object()
        r = self.request("patch", self.url + self.object["name"] + "/", json=self.updated_object)
        self.assertEqual(
            r.status_code,
            requests.codes.method_not_allowed,
            self.prepare_message("PATCH method was not flagged as not supported", r)
        )

    def test_can_delete_object(self):
        self._create_base_object()
        self.assertEqual(
            self.request("delete", self.url + self.object["name"]).status_code,
            requests.codes.ok,
            "Could not delete {}".format(self.model.__table__)
        )

    def test_cannot_delete_non_existing_object(self):
        self.assertEqual(
            self.request("delete", self.url + "test").status_code,
            requests.codes.not_found,
            "Could delete {} that was not created".format(self.model.__table__)
        )

    def test_cannot_delete_object_when_not_authenticated(self):
        self._create_base_object()
        self.assertEqual(
            requests.delete(self.url + self.object["name"]).status_code,
            requests.codes.unauthorized,
            "Wrong answer when trying to delete a {} without authentication".format(self.model.__table__)
        )

    def test_cannot_delete_object_from_another_application(self):
        self._create_base_object()
        new_token = "Bearer {}".format(self.register_application("goat"))
        self.assertEqual(
            requests.delete(self.url + self.object["name"], headers=dict(Authorization=new_token)).status_code,
            requests.codes.not_found,
            "Wrong answer when trying to delete a {} from another application".format(self.model.__table__)
        )

    def test_two_applications_can_have_same_object_name(self):
        self._create_base_object()
        new_token = "Bearer {}".format(self.register_application("goat"))
        r = requests.post(self.url, json=self.object, headers=dict(Authorization=new_token))
        self.assertEqual(
            r.status_code,
            requests.codes.created,
            msg=self.prepare_message("Two different application could not have the same object", r)
        )

    def test_can_retrieve_object(self):
        r = self.request("post", self.url, json=self.object)
        self.check_precondition(
            r,
            requests.codes.created,
            "Could not create {}, aborting test".format(self.model.__table__)
        )

        r = self.request("get", r.headers["Location"])
        self.assertEqual(
            r.status_code,
            requests.codes.ok,
            msg=self.prepare_message("Could not retrieve {}".format(self.model.__table__), r)
        )
        self.assertEqual(r.json()["name"], self.object["name"])

    @unittest.skipIf(
        os.environ.get(skip_concurrency_env_variable, False),
        "Skipping concurrency test because {} is set".format(skip_concurrency_env_variable)
    )
    def test_can_only_create_one_object_with_a_given_name(self):
        self._create_base_object()
        headers = {"Authorization": "Bearer {}".format(self.token)}

        for i in range(self.concurrency_tests):
            o = deepcopy(self.object)
            o["name"] = "{}-conc-{}".format(o["name"], i)
            res = self.request_concurrently(
                "post", self.url, self.request_per_concurrent_test, json=o, headers=headers
            )
            self.check_only_one_created(res)


class CRUDTestWithDependencyOnPointScaleMixin(CRUDTestMixin):
    @property
    @abstractmethod
    def point_scale_name(self):
        """Name of the base point scale to create."""

    def create_prerequisites(self, token):
        r = requests.post(
            BASE_URL + "/pointScales/",
            json=dict(name=self.point_scale_name),
            headers=dict(Authorization=token)
        )

        if r.status_code != requests.codes.created:
            raise PreconditionFail(self.prepare_message("Could not create pointscale", r))

    def setUp(self):
        super().setUp()
        self.create_prerequisites("Bearer " + self.token)

    def test_cannot_create_object_if_pointscale_does_not_exist(self):
        obj = deepcopy(self.object)
        obj["pointScale"] += "-non-existing"

        r = self.request("post", self.url, obj)
        self.assertEqual(r.status_code, requests.codes.bad_request)
        self.assertIn(
            "point scale",
            r.text,
            msg=self.prepare_message("Did not get an error about point scale when one is missing", r)
        )
        self.check_message(r.json())

    def test_two_applications_can_have_same_object_name(self):
        self._create_base_object()
        new_token = "Bearer {}".format(self.register_application("goat"))

        self.create_prerequisites(new_token)

        r = requests.post(self.url, json=self.object, headers=dict(Authorization=new_token))
        self.assertEqual(
            r.status_code,
            requests.codes.created,
            msg=self.prepare_message("Two different application could not have the same object", r)
        )


if __name__ == '__main__':
    unittest.main()
