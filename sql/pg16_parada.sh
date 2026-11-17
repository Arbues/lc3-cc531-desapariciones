#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# pg16_parada.sh — Detiene el PostgreSQL 16.16 de usuario del LC3 (CC531).
#
#   Cluster : /home/arbues/pg16data     Puerto: 5433     Socket: /tmp
#
# Uso:
#   ./pg16_parada.sh            # parada "fast" (por defecto)
#   ./pg16_parada.sh --smart    # espera a que terminen las conexiones
#   ./pg16_parada.sh --immediate# corta sin cerrar limpio (sólo si se colgó)
#   ./pg16_parada.sh --status   # sólo informa
#
# Idempotente: si el servidor ya está parado, termina con código 0.
# ---------------------------------------------------------------------------
set -uo pipefail

PGHOME=/home/arbues/pg16
PGBIN="$PGHOME/bin"
PGDATA=/home/arbues/pg16data
PGPORT=5433
PGSOCK=/tmp
PGLOG="$PGDATA/log.txt"

export PATH="$PGBIN:$PATH"

[ -x "$PGBIN/pg_ctl" ] || { echo "ERROR: no existe $PGBIN/pg_ctl" >&2; exit 1; }
[ -d "$PGDATA" ]       || { echo "ERROR: no existe el cluster $PGDATA" >&2; exit 1; }

if [ "${1:-}" = "--status" ]; then
  if pg_ctl -D "$PGDATA" status >/dev/null 2>&1; then
    pg_ctl -D "$PGDATA" status
    exit 0
  fi
  echo "PostgreSQL 16 ($PGDATA) está PARADO"
  exit 0
fi

if ! pg_ctl -D "$PGDATA" status >/dev/null 2>&1; then
  echo "== ya estaba parado (nada que hacer) =="
  exit 0
fi

MODE=fast
case "${1:-}" in
  --smart)     MODE=smart ;;
  --immediate) MODE=immediate ;;
  "")          MODE=fast ;;
  *)           echo "opción desconocida: $1 (usá --smart|--immediate|--status)" >&2; exit 2 ;;
esac

echo "== deteniendo PostgreSQL 16 (modo $MODE) en $PGDATA =="
pg_ctl -D "$PGDATA" -m "$MODE" -w stop || {
  echo "---- últimas líneas de $PGLOG ----" >&2
  tail -20 "$PGLOG" >&2
  exit 1
}

pg_isready -h "$PGSOCK" -p "$PGPORT" >/dev/null 2>&1 \
  && { echo "ADVERTENCIA: pg_isready todavía responde en $PGSOCK:$PGPORT" >&2; exit 1; }

echo "== OK: servidor detenido (el cluster $PGDATA queda intacto) =="
