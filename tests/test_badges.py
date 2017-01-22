import unittest

from utils import BASE_URL
from utils.mixins.crud import CRUDTestMixin
from utils.models import Badge


class TestBadges(CRUDTestMixin, unittest.TestCase):
    model = Badge
    object = dict(name="unbelievable achievement")
    required_fields = {"name"}
    url = BASE_URL + "/badges/"


if __name__ == '__main__':
    unittest.main()
