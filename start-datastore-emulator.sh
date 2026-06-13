#!/bin/bash
# Start Google Cloud Datastore emulator for local development.
# Run this in a separate terminal BEFORE starting the Spring Boot app from IntelliJ.
#
# Uses ./db_data if present (existing local order data); otherwise starts empty.

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

PROJECT_ID="gujrati-tailors-backend"
HOST_PORT="localhost:8081"
DATA_DIR="${SCRIPT_DIR}/db_data"

ARGS=(
  beta emulators datastore start
  --project="${PROJECT_ID}"
  --host-port="${HOST_PORT}"
)

if [[ -d "${DATA_DIR}" ]]; then
  echo "Using persisted emulator data: ${DATA_DIR}"
  ARGS+=(--data-dir="${DATA_DIR}")
else
  echo "No ${DATA_DIR} found; starting with empty datastore."
fi

echo "Starting Datastore emulator on ${HOST_PORT} (project: ${PROJECT_ID})"
echo "Leave this terminal running. Then run GujratiTailorsApplication from IntelliJ."
echo ""

exec gcloud "${ARGS[@]}"
