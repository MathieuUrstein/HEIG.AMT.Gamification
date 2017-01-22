import unittest

from utils import BASE_URL
from utils.mixins.crud import CRUDTestWithDependencyOnPointScaleMixin
from utils.models import PointScale


class TestEventRules(CRUDTestWithDependencyOnPointScaleMixin, unittest.TestCase):
    point_scale_name = "agile_goat"

    model = PointScale
    object = dict(name="reward_new_tests_rule", event="test_added", pointScale=point_scale_name, pointsGiven=1)
    required_fields = {"event", "name", "pointsGiven", "pointScale"}
    url = BASE_URL + "/rules/events/"


if __name__ == '__main__':
    unittest.main()
