import sys
from configparser import ConfigParser
from itertools import chain, repeat

import os

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


if CONFIG.get(DATABASE_SECTION, PASSWORD) is not None:
    user = "{}:{}".format(
        CONFIG.get(DATABASE_SECTION, USER),
        CONFIG.get(DATABASE_SECTION, PASSWORD)
    )
else:
    user = CONFIG.get(DATABASE_SECTION, USER)

DATABASE_CONNECTION_URL = "{protocol}://{user}@{host}/{name}".format(
    protocol=CONFIG.get(DATABASE_SECTION, PROTOCOL),
    user=user,
    host=CONFIG.get(DATABASE_SECTION, HOST),
    name=CONFIG.get(DATABASE_SECTION, NAME)
)
