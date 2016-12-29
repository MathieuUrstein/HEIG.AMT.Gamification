import sys
from configparser import ConfigParser
from itertools import chain, repeat

import os
import requests
import sqlalchemy
from abc import abstractmethod
from sqlalchemy import MetaData
from sqlalchemy import create_engine

HOME_DIR = os.path.dirname(os.path.dirname(__file__))
ENV_VARIABLES_PREFIX = "gamification"

DATABASE_SECTION = "database"
API_SECTION = "api"

HOST = "host"
NAME = "name"
USER = "user"
PASSWORD = "password"
PROTOCOL = "protocol"
URL = "url"


DEFAULT_CONF_PATH = os.path.join(HOME_DIR, "test_defaults.conf")
CONF_PATH = os.path.join(HOME_DIR, "test.conf")

RECOGNIZED_ENV_VARIABLES = {
    (section, option): explanation for section, option, explanation in chain(
        zip(
            repeat(DATABASE_SECTION),
            [NAME, USER, PASSWORD, PROTOCOL, HOST],
            [
                "the name database used for the run",
                "the user owning the database",
                "the password of the user owning the database",
                "the protocol used for connection to the database",
                "the address to which to contact the database",
            ]
        ),
        zip(
            repeat(API_SECTION),
            [URL, HOST],
            [
                "the url used to access the application's api",
                "the host url on which to access the application",
            ]
        )
    )
}


CONFIG = ConfigParser()
CONFIG.read([DEFAULT_CONF_PATH, CONF_PATH])

for section, option in RECOGNIZED_ENV_VARIABLES.keys():
    env_var = os.environ.get("{}_{}_{}".format(ENV_VARIABLES_PREFIX, section, option).upper())

    if env_var is not None:
        CONFIG[section][option] = env_var


API_ERROR_MESSAGES = dict()

HTTP_METHODS = {
    "delete",
    "get",
    "patch",
    "post",
    "put",
}

BASE_URL = "{}://{}/{}".format(
    CONFIG.get(API_SECTION, PROTOCOL),
    CONFIG.get(API_SECTION, HOST),
    CONFIG.get(API_SECTION, URL)
)


def print_customization_and_exit():
    env_variables = "\n".join("{}: {}".format("{}_{}_{}".format(
        ENV_VARIABLES_PREFIX, *_key,
    ).upper(), RECOGNIZED_ENV_VARIABLES[_key]) for _key in RECOGNIZED_ENV_VARIABLES.keys())

    print(
        "If you need to customize the connection parameters, please create a `test.conf` file "
        "next to `test_defaults.conf` in `{}` and following the same schema.\n"
        "You can also override every value with environment variables. The recognised environment variables are :\n\n"
        "{}".format(HOME_DIR, env_variables),
        file=sys.stderr
    )
    exit(1)


class DatabaseAccessMixin:
    database_connection = None
    database_engine = None
    database_meta = None

    if CONFIG.get(DATABASE_SECTION, PASSWORD) is not None:
        user = "{}:{}".format(
            CONFIG.get(DATABASE_SECTION, USER),
            CONFIG.get(DATABASE_SECTION, PASSWORD)
        )
    else:
        user = CONFIG.get(DATABASE_SECTION, USER)

    database_connection_url = "{protocol}://{user}@{host}/{name}".format(
        protocol=CONFIG.get(DATABASE_SECTION, PROTOCOL),
        user=user,
        host=CONFIG.get(DATABASE_SECTION, HOST),
        name=CONFIG.get(DATABASE_SECTION, NAME)
    )

    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        cls.database_engine = create_engine(cls.database_connection_url)
        cls.database_meta = MetaData()

        try:
            cls.database_meta.reflect(bind=cls.database_engine)
            cls.database_connection = cls.database_engine.connect()
        except sqlalchemy.exc.OperationalError as e:
            print("ErrorJSONFieldsContent establishing database connection :", e.orig, file=sys.stderr)
            print_customization_and_exit()

        try:
            requests.get(BASE_URL)
        except requests.exceptions.ConnectionError:
            print("ErrorJSONFieldsContent connection to {}".format(BASE_URL), file=sys.stderr)
            print("Is the app up and running and the url correct ?", file=sys.stderr)
            print_customization_and_exit()

    @classmethod
    def tearDownClass(cls):
        super().tearDownClass()
        cls.database_connection.close()
        cls.database_engine.dispose()


class DatabaseWiperTestMixin(DatabaseAccessMixin):
    def tearDown(self):
        super().tearDown()

        for table in reversed(self.database_meta.sorted_tables):
            self.database_connection.execute(table.delete())


class RestAPITestUtilities:
    @property
    @abstractmethod
    def required_fields(self):
        """List of all required fields the request must give."""

    @property
    @abstractmethod
    def invalid_methods(self):
        """List of all methods that are not valid by the endpoint."""

    @property
    @abstractmethod
    def url(self):
        """URL to which to make the requests"""

    def check_message(self, entry):
        if API_ERROR_MESSAGES.get(entry["code"]) is None:
            # first time we get the message, we add it to the global list
            API_ERROR_MESSAGES[entry["code"]] = entry["message"]
        else:
            # we already got the same code, let's check that the same message it received
            self.assertEqual(API_ERROR_MESSAGES[entry["code"]], entry["message"])

    def check_field_required(self, field_name, response):
        self.assertEqual(response.status_code, requests.codes.bad_request)
        self.assertIn(field_name, response.json().keys())
        self.check_message(response.json()[field_name])

    def test_invalid_methods(self):
        for method in self.invalid_methods:
            with self.subTest(method=method):
                self.assertEqual(requests.request(method, self.url).status_code, requests.codes.not_allowed)

    def test_required_fields(self):
        request = requests.post(self.url, json=dict())

        for field in self.required_fields:
            with self.subTest(field=field):
                self.check_field_required("name", request)

        self.assertEqual(
            set(request.json().keys()),
            self.required_fields,
            msg="A field was marked as required by the response, but test is not requiring it"
        )
