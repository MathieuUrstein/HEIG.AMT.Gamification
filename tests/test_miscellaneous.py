import unittest

import requests
from sqlalchemy import select

from utils import BASE_URL
from utils.mixins.database import DatabaseWiperTestMixin
from utils.models import Application


class TestMiscellaneous(DatabaseWiperTestMixin, unittest.TestCase):
    def check_server_accepts_weird_chars(self, name):

        application = dict(name=name, password="goat")
        r = requests.post(BASE_URL + "/register/", json=application)
        self.assertEqual(r.status_code, requests.codes.created)

        data = self.database_connection.execute(select([Application])).first()
        self.assertEqual(name, data["name"], msg="Saving object in UTF-8 doesn't keep the correct charset")

    def test_server_accepts_emojis(self):
        self.check_server_accepts_weird_chars("😁")

    def test_server_accepts_japanese(self):
        self.check_server_accepts_weird_chars("あ")
