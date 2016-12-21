#!/bin/sh

set -u

. "$(dirname "$0")/env.sh"

if [ -f "${RESULT_FILE}" ]; then
    rm "${RESULT_FILE}"
fi

echo "running integration tests."

python3 -m unittest discover ./tests -v 1>${REPORT_FILE} 2>&1
RESULT=$?

echo "${RESULT}" > "${RESULT_FILE}"

if [ "${RESULT}" -ne 0 ]; then
    echo "[ERROR] The tests failed. Please see ${REPORT_FILE} for results." 1>&2
else
    echo "[INFO] Tests ran successfully"
fi

exit 0
