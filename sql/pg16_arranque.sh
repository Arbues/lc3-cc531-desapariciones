#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# pg16_arranque.sh — Arranca (o reinicia) el PostgreSQL 16.16 de usuario
#                    del LC3 (CC531), SIN sudo.
#
#   Binarios : /home/arbues/pg16/bin        (PostgreSQL 16.16, debs pgdg26.04)
#   Cluster   : /home/arbues/pg16data
#   Puerto    : 5433 (TCP en 127.0.0.1, socket unix en /tmp)
#   Superuser : postgres   (auth local = trust)
#   App       : macrodatos / macrodatos   (auth TCP = scram-sha-256)
#   Base      : denuncias  (owner macrodatos)
#
# Uso:
#   ./pg16_arranque.sh              # arranca si está parado; si ya corre, no hace nada
#   ./pg16_arranque.sh --restart    # reinicia siempre (recarga postgresql.conf)
#   ./pg16_arranque.sh --status     # sólo informa el estado
#
# Idempotente: se puede ejecutar tantas veces como sea necesario.
# ---------------------------------------------------------------------------
set -uo pipefail

PGHOME=/home/arbues/pg16
PGBIN="$PGHOME/bin"
PGDATA=/home/arbues/pg16data
PGPORT=5433
PGSOCK=/tmp
PGLOG="$PGDATA/log.txt"

export PATH="$PGBIN:$PATH"

die() { echo "ERROR: $*" >&2; exit 1; }

[ -x "$PGBIN/pg_ctl" ] || die "no existe $PGBIN/pg_ctl (¿se perdió la instalación en $PGHOME?)"
[ -d "$PGDATA" ]       || die "no existe el cluster $PGDATA (habría que hacer initdb otra vez)"

is_running() { pg_ctl -D "$PGDATA" status >/dev/null 2>&1; }

wait_ready() {
  for _ in $(seq 1 30); do
    if pg_isready -h "$PGSOCK" -p "$PGPORT" >/dev/null 2>&1; then return 0; fi
    sleep 1
  done
  return 1
}

bootstrap_objects() {
  # Crea rol y base si faltan (idempotente, no falla si ya existen).
  psql -h "$PGSOCK" -p "$PGPORT" -U postgres -d postgres -tAc \
       "select 1 from pg_roles where rolname='macrodatos'" 2>/dev/null | grep -q 1 \
    || psql -h "$PGSOCK" -p "$PGPORT" -U postgres -d postgres \
            -c "CREATE USER macrodatos WITH PASSWORD 'macrodatos';" >/dev/null
  psql -h "$PGSOCK" -p "$PGPORT" -U postgres -d postgres -tAc \
       "select 1 from pg_database where datname='denuncias'" 2>/dev/null | grep -q 1 \
    || psql -h "$PGSOCK" -p "$PGPORT" -U postgres -d postgres \
            -c "CREATE DATABASE denuncias OWNER macrodatos ENCODING 'UTF8';" >/dev/null
}

case "${1:-}" in
  --status)
    if is_running; then
      pg_ctl -D "$PGDATA" status
      pg_isready -h "$PGSOCK" -p "$PGPORT"
    else
      echo "PostgreSQL 16 ($PGDATA) está PARADO"
      exit 1
    fi
    exit 0
    ;;
esac

if is_running; then
  if [ "${1:-}" = "--restart" ]; then
    echo "== reiniciando el cluster en puerto $PGPORT =="
    pg_ctl -D "$PGDATA" -m fast -w stop >/dev/null || die "no se pudo detener"
    sleep 1
  else
    echo "== ya está corriendo (PID $(head -1 "$PGDATA/postmaster.pid" 2>/dev/null)) =="
    pg_isready -h "$PGSOCK" -p "$PGPORT" || die "corre pero no acepta conexiones; probá --restart"
    bootstrap_objects
    echo "== listo: psql -h $PGSOCK -p $PGPORT -U macrodatos -d denuncias =="
    exit 0
  fi
else
  # Limpia un postmaster.pid huérfano de un cierre sucio.
  if [ -f "$PGDATA/postmaster.pid" ]; then
    echo "== postmaster.pid huérfano, se elimina =="
    rm -f "$PGDATA/postmaster.pid"
  fi
fi

echo "== arrancando PostgreSQL 16 en $PGDATA (puerto $PGPORT, socket $PGSOCK) =="
pg_ctl -D "$PGDATA" -o "-p $PGPORT -k $PGSOCK" -l "$PGLOG" -w start \
  || { echo "---- últimas líneas de $PGLOG ----" >&2; tail -30 "$PGLOG" >&2; exit 1; }

wait_ready || { echo "---- últimas líneas de $PGLOG ----" >&2; tail -30 "$PGLOG" >&2; die "arrancó pero no responde"; }

bootstrap_objects

echo
echo "== OK =="
pg_isready -h "$PGSOCK" -p "$PGPORT"
psql -h "$PGSOCK" -p "$PGPORT" -U postgres -d denuncias -tAc "select version();"
echo "Conexión app: PGPASSWORD=macrodatos psql -h 127.0.0.1 -p $PGPORT -U macrodatos -d denuncias"
echo "Sin PATH:     export PATH=$PGBIN:\$PATH"
