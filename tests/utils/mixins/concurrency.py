from multiprocessing.pool import Pool

import os
import requests
from collections import defaultdict


skip_concurrency_env_variable = "GAMIFICATION_SKIP_CONCURRENCY_TESTS"


def request(data):
    x = requests.request(data["method"], data["url"], **data["kwargs"])
    return x


class ConcurrentTesterMixin:
    concurrency_tests = int(os.environ.get("GAMIFICATION_CONCURRENCY_TESTS", 100))
    request_per_concurrent_test = int(os.environ.get("GAMIFICATION_REQUEST_PER_CONCURRENT_TEST", 10))

    def only_one_with_status(self, status, results):
        g = iter(result.status_code == status for result in results)
        return any(g) and not any(g)

    def all_but_one_with_status(self, status, results):
        g = iter(result.status_code != status for result in results)
        return any(g) and not any(g)

    def none_with_status(self, status, results):
        g = iter(result.status_code != status for result in results)
        return all(g)

    def all_with_status(self, status, results):
        g = iter(result.status_code == status for result in results)
        return all(g)

    def _parse_responses(self, responses):
        data = dict(messages=defaultdict(lambda: 0), codes=defaultdict(lambda: 0))

        for r in responses:
            data["codes"][r.status_code] += 1
            data["messages"][(r.status_code, r.text)] += 1

        return data

    def _format_responses(self, results):
        res_string = "Result code received:\n\t" + "\n\t".join(
            "{}: {}".format(entry, results["codes"][entry]) for entry in sorted(results["codes"].keys())
        )

        res_string += "\n\nMessages received:"

        for status_code in sorted(results["codes"].keys()):
            res_string += "\n\tFor {}:".format(status_code)
            for messages in results["messages"]:
                if messages[0] == status_code and messages[1] != "":
                    res_string += "\n\t\t{}".format(messages[1])
            res_string += "\n"

        return res_string

    def _format_msg(self, base_message, formatted_response):
        return "{}\n\n{}".format(base_message, formatted_response)

    def check_only_one_created(self, results):
        err_str = self._format_responses(self._parse_responses(results))
        self.assertTrue(
            self.only_one_with_status(requests.codes.created, results),
            msg=self._format_msg("More than one client received a 201 created with conflicts.", err_str)
        )

        self.assertTrue(
            self.none_with_status(requests.codes.server_error, results),
            msg=self._format_msg("At least one client received a 500 with concurrent requests.", err_str)
        )

        self.assertTrue(
            self.all_but_one_with_status(requests.codes.conflict, results),
            msg=self._format_msg("Some clients didn't get a 409 when doing an operation with conflicts.", err_str)
        )

    def request_concurrently(self, method, url, number, **kwargs):
        with Pool(number) as p:
            return p.map(request, [dict(method=method, url=url, kwargs=kwargs) for _ in range(number)])
