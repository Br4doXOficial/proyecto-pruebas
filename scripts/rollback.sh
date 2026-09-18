#!/usr/bin/env bash
set -euo pipefail

if [[ ! -f deploy/previous-slot.txt ]]; then
  echo "ROLLBACK_NOT_AVAILABLE: no existe un slot anterior" >&2
  exit 1
fi

previous="$(tr -d '\r\n' < deploy/previous-slot.txt)"
if [[ ! -f "deploy/$previous/app.jar" ]]; then
  echo "ROLLBACK_FAILED: no existe el artefacto del slot $previous" >&2
  exit 1
fi

current="none"
if [[ -L deploy/current ]]; then
  current="$(basename "$(readlink deploy/current)")"
fi

ln -sfn "$previous" deploy/current
printf '%s\n' "$previous" > deploy/active-slot.txt
printf '%s\n' "$current" > deploy/previous-slot.txt
health_output="$(java -jar deploy/current/app.jar health)"
printf 'ROLLBACK_OK from=%s active=%s health=%s\n' "$current" "$previous" "$health_output"

