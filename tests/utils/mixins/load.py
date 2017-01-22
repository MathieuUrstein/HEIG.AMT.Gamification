from multiprocessing.pool import Pool

import requests


def request(data):
    x = requests.request(data["method"], data["url"], **data["kwargs"])
    return x


class ConcurrentTesterMixin:
    def only_one_with_status(self, status, results):
        g = iter(result.status_code == status for result in results)
        return any(g) and not any(g)

    def all_but_one_with_status(self, status, results):
        g = iter(result.status_code != status for result in results)
        return any(g) and not any(g)

    def none_with_status(self, status, results):
        g = iter(result.status_code != status for result in results)
        return all(g)

    def check_only_one_created(self, results):
        self.assertTrue(
            self.only_one_with_status(requests.codes.created, results),
            msg="More than one client received a 201 created with conflicts."
        )

        self.assertTrue(
            self.none_with_status(requests.codes.server_error, results),
            msg="At least one client received a 500 with concurrent requests."
        )

        self.assertTrue(
            self.all_but_one_with_status(requests.codes.conflict, results),
            msg="Some clients didn't get a 409 when doing an operation with conflicts."
        )

    def request_concurrently(self, method, url, number, **kwargs):
        with Pool(number) as p:
            return p.map(request, [dict(method=method, url=url, kwargs=kwargs) for _ in range(number)])
