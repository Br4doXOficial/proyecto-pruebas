#!/usr/bin/env bash
set -euo pipefail

if [[ ! -f deploy/current/app.jar ]]; then
  echo "SMOKE_FAILED: no hay una version activa" >&2
  exit 1
fi

health_output="$(java -jar deploy/current/app.jar health)"
if [[ "$health_output" != "HEALTHY" ]]; then
  echo "SMOKE_FAILED: respuesta=$health_output" >&2
  exit 1
fi

active="$(basename "$(readlink deploy/current)")"
printf 'SMOKE_OK active=%s health=%s\n' "$active" "$health_output"

