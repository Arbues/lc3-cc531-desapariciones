#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# empaquetar.sh — genera los dos zips de entrega y copia los resultados
# importantes a OneDrive.
#   ./bin/empaquetar.sh            solo zips
#   ./bin/empaquetar.sh --onedrive zips + copia a la carpeta del curso
#
# Se ejecuta desde cualquier ubicación: la raíz del repositorio se deduce de la
# ruta del script. La carpeta destino se puede cambiar con
#   LC3_ONEDRIVE="/ruta/a/pc3" ./bin/empaquetar.sh --onedrive
# ---------------------------------------------------------------------------
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

# zip no viene instalado por defecto en Ubuntu.
command -v zip > /dev/null || { echo "Falta el paquete zip: sudo apt install zip"; exit 1; }

ONEDRIVE="${LC3_ONEDRIVE:-/mnt/c/Users/harus/OneDrive/Documentos/Arbués Enrique PV/CIENCIAS DE LA COMPUTACION 2022/9 CICLO/MACRODATOS/pc3}"

mkdir -p entrega
rm -f entrega/*.zip

# presentacion/ e informe/ van completos: las láminas y el guion se arman con
# \input de subcarpetas (estilo.tex, consultas/, secciones/, guion/), así que
# copiar solo los .tex de entrada deja fuentes que no compilan.
zip -r -q entrega/LC3_Arbues_Perez.zip \
  README.md AGENTS.md docs code sql data/denuncias_desaparicion_2019_2024.csv \
  excel evidencias informe presentacion bin lib/postgresql-42.7.6.jar \
  -x '*/plantilla/*' '*.aux' '*.log' '*.out' '*.toc' '*.fls' '*.fdb_latexmk' \
     '*.nav' '*.snm' '*.vrb' '*.synctex.gz' '*.bak'

cp entrega/LC3_Arbues_Perez.zip entrega/LC3_Sergio_Pezo.zip

echo "zips generados:"
ls -lh entrega/*.zip | awk '{print "  " $5 "  " $9}'
echo "  archivos dentro: $(unzip -l entrega/LC3_Arbues_Perez.zip | tail -1 | awk '{print $2}')"
unzip -t entrega/LC3_Arbues_Perez.zip > /dev/null && echo "  integridad: OK"

if [ "${1:-}" = "--onedrive" ]; then
  mkdir -p "$ONEDRIVE"
  cp entrega/LC3_Arbues_Perez.zip "$ONEDRIVE/" 2>/dev/null
  cp entrega/LC3_Sergio_Pezo.zip  "$ONEDRIVE/" 2>/dev/null
  cp presentacion/presentacion.pdf "$ONEDRIVE/LC3_presentacion.pdf" 2>/dev/null
  cp presentacion/guion-exposicion.pdf "$ONEDRIVE/LC3_guion-exposicion.pdf" 2>/dev/null
  cp informe/informe.pdf "$ONEDRIVE/LC3_informe.pdf" 2>/dev/null
  mkdir -p "$ONEDRIVE/excel" && cp excel/*.xlsx "$ONEDRIVE/excel/" 2>/dev/null
  echo ""
  echo "copias en OneDrive:"
  ls -lh "$ONEDRIVE" | awk 'NR>1 {print "  " $5 "  " $9}'
fi
