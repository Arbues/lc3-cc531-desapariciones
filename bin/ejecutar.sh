#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# ejecutar.sh — corre una consulta .scala del LC3 junto al esquema comun.
#
#   ./bin/ejecutar.sh code/rdd/03a_max_min_por_sexo.scala
#   ./bin/ejecutar.sh code/sql/05e_1_variacion_mensual.scala lib/spark-excel_2.12-3.3.1_0.18.7.jar
#
# El segundo argumento (opcional) agrega jars al classpath (JDBC, spark-excel).
# El script usa sparkctl shell (perfil local, master local[*], fs file://).
# ---------------------------------------------------------------------------
set -uo pipefail

if [ $# -lt 1 ]; then echo "uso: $0 <archivo.scala> [jar,jar]"; exit 1; fi

RAIZ="/home/arbues/macrodatos/PC3"
CONSULTA="$1"
[ -f "$CONSULTA" ] || { echo "no existe: $CONSULTA"; exit 1; }

JARS=""
if [ $# -ge 2 ] && [ -n "$2" ]; then JARS="--jars $2"; fi

sparkctl="${HOME}/macrodatos/spark/bin/sparkctl"

printf ':load %s/code/comun/esquema.scala\n:load %s\n:quit\n' "$RAIZ" "$(realpath "$CONSULTA")" \
  | "$sparkctl" shell --conf spark.sql.codegen.wholeStage=false --conf spark.sql.codegen.factoryMode=NO_CODEGEN $JARS 2>&1
