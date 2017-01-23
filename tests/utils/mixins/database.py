import sys

import requests
from sqlalchemy import create_engine, MetaData
from sqlalchemy.exc import OperationalError

from .. import DATABASE_CONNECTION_URL, print_customization_and_exit, BASE_URL


class DatabaseAccessMixin:
    """
    Mixin to add a connection to handle a connection to the database.

    This will automatically create a connection before running all tests in the class and
    will cleanup the connection at the end of the tests.
    """
    database_connection = None
    database_engine = None
    database_meta = None

    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        cls.database_engine = create_engine(DATABASE_CONNECTION_URL, convert_unicode=True)
        cls.database_meta = MetaData()

        try:
            cls.database_meta.reflect(bind=cls.database_engine)
            cls.database_connection = cls.database_engine.connect()
        except OperationalError as e:
            print("Error establishing database connection :", e.orig, file=sys.stderr)
            print_customization_and_exit()

        try:
            requests.get(BASE_URL)
        except requests.exceptions.ConnectionError:
            print("Error connection to {}".format(BASE_URL), file=sys.stderr)
            print("Is the app up and running and the url correct ?", file=sys.stderr)
            print_customization_and_exit()

    @classmethod
    def tearDownClass(cls):
        super().tearDownClass()
        cls.database_connection.close()
        cls.database_engine.dispose()


class DatabaseWiperTestMixin(DatabaseAccessMixin):
    """Mixin to clear the database between each test."""

    def setUp(self):
        for table in reversed(self.database_meta.sorted_tables):
            self.database_connection.execute(table.delete())
        super().setUp()

    def tearDown(self):
        super().tearDown()

        for table in reversed(self.database_meta.sorted_tables):
            self.database_connection.execute(table.delete())

    @classmethod
    def tearDownClass(cls):
        for table in reversed(cls.database_meta.sorted_tables):
            cls.database_connection.execute(table.delete())

        super().tearDownClass()
