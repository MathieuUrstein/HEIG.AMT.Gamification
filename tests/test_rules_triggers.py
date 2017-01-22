import unittest

import requests

from utils import BASE_URL
from utils.mixins import PreconditionFail
from utils.mixins.crud import CRUDTestWithDependencyOnPointScaleMixin
from utils.models import Badge


class TestTriggerRules(CRUDTestWithDependencyOnPointScaleMixin, unittest.TestCase):
    badge_name = "agile_tester"
    point_scale_name = "agile_goat"

    model = Badge
    object = dict(
        name="award_agile_tester_badge",
        aboveLimit=True,
        badgeAwarded=badge_name,
        limit=10,
        pointScale=point_scale_name
    )

    required_fields = {"aboveLimit", "badgeAwarded", "limit", "name", "pointScale"}
    url = BASE_URL + "/rules/triggers/"

    def create_prerequisites(self, token):
        super().create_prerequisites(token)

        r = requests.post(
            BASE_URL + "/badges/",
            json=dict(name=self.badge_name),
            headers=dict(Authorization=token)
        )

        if r.status_code != requests.codes.created:
            raise PreconditionFail(self.prepare_message("Could not create pointscale", r))


if __name__ == '__main__':
    unittest.main()
