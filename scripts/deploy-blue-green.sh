#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 1 || ! -f "$1" ]]; then
  echo "Uso: $0 <archivo.jar>" >&2
  exit 2
fi

artifact="$1"
mkdir -p deploy/blue deploy/green

active="none"
if [[ -L deploy/current ]]; then
  active="$(basename "$(readlink deploy/current)")"
fi

if [[ "$active" == "blue" ]]; then
  target="green"
else
  target="blue"
fi

cp "$artifact" "deploy/$target/app.jar"
health_output="$(java -jar "deploy/$target/app.jar" health)"
if [[ "$health_output" != "HEALTHY" ]]; then
  echo "El health check fallo en $target" >&2
  exit 1
fi

if [[ "$active" != "none" ]]; then
  printf '%s\n' "$active" > deploy/previous-slot.txt
fi

ln -sfn "$target" deploy/current
printf '%s\n' "$target" > deploy/active-slot.txt
printf 'DEPLOY_OK strategy=blue-green previous=%s active=%s health=%s\n' "$active" "$target" "$health_output"

