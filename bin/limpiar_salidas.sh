#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# limpiar_salidas.sh — regenera evidencias/salidas/*.txt desde logs/*.log
# quitando el banner de Spark y el eco del REPL al cargar el esquema.
#   ./bin/limpiar_salidas.sh
# ---------------------------------------------------------------------------
set -uo pipefail
cd /home/arbues/macrodatos/PC3 || exit 1
mkdir -p evidencias/salidas

for log in logs/*.log; do
  [ -f "$log" ] || continue
  base=$(basename "$log" .log)
  tr -d '\r' < "$log" \
    | sed -n '/^=\{90,\}/,$p' \
    | grep -v -E '^scala> |^\[Stage |^WARNING|^\s*$' \
    | sed 's/^=\{100\}$//' > "evidencias/salidas/$base.txt"
  echo "  $base.txt  ($(wc -l < "evidencias/salidas/$base.txt") lineas)"
done
