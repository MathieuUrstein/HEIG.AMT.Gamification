import unittest

from utils import BASE_URL
from utils.mixins.crud import CRUDTestMixin
from utils.models import PointScale


class TestPointScales(CRUDTestMixin, unittest.TestCase):
    model = PointScale
    object = dict(name="testing_pointscale")
    url = BASE_URL + "/pointScales/"
    required_fields = {"name"}


if __name__ == '__main__':
    unittest.main()
