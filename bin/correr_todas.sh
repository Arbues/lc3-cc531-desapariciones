#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# correr_todas.sh — ejecuta todas las consultas .scala y guarda su salida
#   ./bin/correr_todas.sh            (rdd + sql)
#   ./bin/correr_todas.sh code/rdd   (solo una carpeta)
# Resultado: evidencias/salidas/<nombre>.txt  y  logs/<nombre>.log
# ---------------------------------------------------------------------------
set -uo pipefail
cd /home/arbues/macrodatos/PC3 || exit 1

CARPETA="${1:-}"
if [ -z "$CARPETA" ]; then ARCHIVOS=$(ls code/rdd/*.scala code/sql/*.scala 2>/dev/null); else ARCHIVOS=$(ls "$CARPETA"/*.scala 2>/dev/null); fi

mkdir -p evidencias/salidas logs

for f in $ARCHIVOS; do
  base=$(basename "$f" .scala)
  echo ">>> $base"
  JARS=""
  case "$base" in
    05g*) JARS="lib/spark-excel_2.12-3.3.1_0.18.7.jar,lib/postgresql-42.7.6.jar" ;;
  esac
  ./bin/ejecutar.sh "$f" "$JARS" > "logs/$base.log" 2>&1
  tr -d '\r' < "logs/$base.log" \
    | grep -v -E '^\[Stage |^scala> |^WARNING|^Using Spark|^Setting default|Web UI available|Spark context available|Spark session available|^Welcome|^ *\/|^ *\\\\|^Type in expressions|^Type :help|^For more information' \
    | sed '/^$/N;/^\n$/D' > "evidencias/salidas/$base.txt"
  errs=$(grep -c 'error:' "logs/$base.log" 2>/dev/null || true)
  echo "    errores: $errs  | salida: evidencias/salidas/$base.txt"
done

echo "LISTO"
