#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAR_PATH="${SCRIPT_DIR}/../target/BulkMaster-1.0-jar-with-dependencies.jar"

usage() {
  echo "Usage: $(basename "$0") <sf-alias> [BulkMaster args...]"
  echo "Example: $(basename "$0") my-org -i -o Account -f ./input.csv"
  exit 1
}

if [ $# -lt 1 ]; then
  usage
fi

if ! command -v sf >/dev/null 2>&1; then
  echo "ERROR: 'sf' (Salesforce CLI) not found on PATH."
  exit 1
fi

if ! command -v jq >/dev/null 2>&1; then
  echo "ERROR: 'jq' not found on PATH."
  exit 1
fi

if ! command -v java >/dev/null 2>&1; then
  echo "ERROR: 'java' not found on PATH."
  exit 1
fi

if [ ! -f "$JAR_PATH" ]; then
  echo "ERROR: BulkMaster jar not found: $JAR_PATH"
  echo "Build first with: mvn clean package"
  exit 1
fi

ORG_ALIAS="$1"
shift

ORG_JSON="$(sf org display --target-org "$ORG_ALIAS" --json 2>/dev/null)" || {
  echo "ERROR: Failed to resolve Salesforce org alias '$ORG_ALIAS'."
  echo "Check authenticated orgs with: sf org list"
  exit 2
}

AUTH_TOKEN="$(echo "$ORG_JSON" | jq -r '.result.accessToken')"
INSTANCE_URL="$(echo "$ORG_JSON" | jq -r '.result.instanceUrl')"

if [ -z "$AUTH_TOKEN" ] || [ "$AUTH_TOKEN" = "null" ]; then
  echo "ERROR: Could not read access token for alias '$ORG_ALIAS'."
  exit 2
fi

if [ -z "$INSTANCE_URL" ] || [ "$INSTANCE_URL" = "null" ]; then
  echo "ERROR: Could not read instance URL for alias '$ORG_ALIAS'."
  exit 2
fi

java -jar "$JAR_PATH" \
  -authtoken "$AUTH_TOKEN" \
  -instanceurl "$INSTANCE_URL" \
  "$@"
