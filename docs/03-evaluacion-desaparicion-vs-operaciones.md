# Evaluación con datos reales: Desaparición de personas vs Operaciones policiales

Medición hecha el 16-nov-2026 descargando los CSV de la PNDA con `curl` y midiendo con
pandas. Nada de cifras estimadas. Reproducible con los comandos del final.

Veredicto: **MANTENER Opción 1 (Denuncias por desaparición de personas [MININTER]).**
Puntaje criterios A–H: desaparición **15.5/16**, operaciones **11.0/16**.

---

## 1. Ficha técnica de las dos bases

| | Opción 1 · Desaparición | Opción 2 · Operaciones policiales |
|---|---|---|
| Slug CKAN | `denuncias-por-desaparición-de-personas-mininter` | `resultados-de-las-operaciones-policiales-nivel-nacional-mininter` |
| Filas / columnas (recurso consolidado) | 77,331 × 10 (Ene-2019→Dic-2025) | 40,716 × 6 (Oct-2022→Sep-2026) |
| Recurso histórico | 93,831 × 13 (Ene-2019→Dic-2024) | 555,515 × 6 (Oct-2022→Jul-2026) — **78% duplicados** |
| Peso | 5.07 MB / 8.12 MB | 2.26 MB / 29.9 MB |
| Encoding | Latin-1 (nuevo) vs UTF-8-BOM (viejo) | UTF-8-BOM en ambos |
| Último dato | Dic-2025 (publicado mar-2026) | Sep-2026 (publicado 13-nov-2026) |
| Diccionario / metadato | `.xlsx` + `.docx` (sí) | `.xlsx` + `.docx` (sí) |
| Duplicados exactos | 0 | 0 en el consolidado Sep-2026 · **434,478 en el de Jul-2026** |

## 2. Criterio A — campo numérico para max/min/media/mediana/desviación

Desaparición (`CANTIDAD`, unidad única = personas):

```
n=77,331 · min=1 · max=53 · media=1.84 · mediana=1 · desv=2.07 · suma=142,374 · nulos=0
distribución: 1→52,263 (67.6%) · 2→12,340 · 3→5,116 · 4→2,730 · 5→1,465 ...
```

Operaciones (`VALOR`) **mezcla unidades en la misma columna**: kilos, soles, envoltorios y
conteos. Por eso el agregado global no significa nada:

```
min=0 · max=513,820,390 · media=56,462 · mediana=3 · desv=2,744,044
max/mediana = 171,273,463× · 40.0% de filas son 0
```

Solo es interpretable filtrando por `VARIABLE` (18 subseries). Ej.: `DETENIDOS DIVERSOS
DELITOS (PERUANOS)` → n=2,262, min=0, max=6,843, media=460.88, mediana=205, desv=841.14.
Eso obliga a 18 ETL distintos; en desaparición las 25 consultas comparten una sola métrica.

**A: desaparición 2/2 · operaciones 1/2.**

## 3. Criterio B — categóricas con cardinalidad útil (5–60)

| Desaparición | distintos | | Operaciones | distintos |
|---|---|---|---|---|
| DPTO_HECHO | 26 | | VARIABLE | 18 |
| NACIONALIDAD | 60 | | DEPENDENCIAS | 53 |
| MES | 12 | | DIR_REG_FRE | 6 |
| RANGO_EDAD | 6 | | MES | 12 |
| EST_CIVIL (recurso 13 col.) | 6 | | AÑO | 5 |
| NIVEL_EDUCATIVO (13 col.) | 5 | | | |
| PROV_HECHO | 195 (agregable) | | | |
| DIST_HECHO | 1,435 (llave, no agrupador) | | | |

Ambas cumplen de sobra (≥5 categóricas en rango). **B: empate 2/2.**

## 4. Criterio C — decimales reales

- **Desaparición:** `CANTIDAD` es entero puro (0 valores con decimales en crudo). Los
  decimales salen de derivar: tasa por 100 mil habitantes, % del total, índice de
  estacionalidad (mes/media), promedio por distrito (1.84 → decimal).
  Denominador verificado y disponible: RENIEC «Población identificada con DNI» 2026-II
  (53 MB, columnas `UBIGEO_INEI`, `Sexo`, `Edad`, `Cantidad`) → tasa real por ubigeo.
- **Operaciones:** 4,080 valores (10.02%) traen decimales nativos, incluidos kilos con 3
  decimales (`4.295`, `8.495`). Más fácil, pero solo dentro de cada `VARIABLE`.

**C: empate 2/2** (desaparición exige un join extra con población, que además suma para 5c).

## 5. Criterio D — jerarquía geográfica y 4 tipos de join (aquí se decide todo)

Desaparición: `UBIGEO_HECHO` (6 dígitos, 1,549 distintos) → DIST (1,435) → PROV (195) → DPTO (26).

```
ubigeo con longitud ≠ 6 ......... 0
ubigeo no numérico .............. 0
ubigeo que apunta a 2 dptos ..... 0
ubigeo que apunta a 2 provs ..... 0
```

Cruce real contra la tabla INEI de ubigeos (1,891 distritos, `UBIGEO 2022_1891 distritos.xlsx`):

| Join | Resultado medido |
|---|---|
| INNER (denuncias ∩ ubigeo INEI) | 77,152 filas (99.77%); descarta 179 filas / 3 ubigeos huérfanos (`010207`, `150144`, `160109`) |
| LEFT (denuncias → ubigeo) | 77,331 filas, 179 con nombres INEI en NULL |
| RIGHT (ubigeo → denuncias) | 1,891 filas, **345 distritos sin ninguna denuncia** (NULL en CANTIDAD) |
| FULL OUTER | 77,676 filas = 77,331 + 345 |

Los cuatro joins tienen resultado distinto y cada uno responde una pregunta real
(cobertura del registro, distritos sin denuncia, ubigeos inválidos). Eso es exactamente 5c.

Operaciones: **no hay ubigeo ni distrito/provincia**. La única geografía es
`DEPENDENCIAS` (53 unidades):

```
RP (región policial) ......... 21,384 filas (52.5%) · 27 unidades (≠ 25 dptos + Callao)
FP (frente) ..................  3,510 filas ( 8.6%) · 6 unidades
DIR (dirección nacional) ..... 13,230 filas (32.5%) · 17 unidades
DIV/TCIU .....................  2,592 filas ( 6.4%) · 3 unidades
→ 38.9% de las filas no tienen ubicación geográfica recuperable
```

Además `DIR_REG_FRE` está contaminado: 756 filas traen `RP ICA` / `RP TUMBES` en vez de
`Región`. Sin jerarquía no hay right ni full outer join con sentido; solo self-joins.

**D: desaparición 2/2 · operaciones 0/2.** Es el criterio que decide.

## 6. Criterio E — volumen sano (10 mil–1 millón, `local[*]`)

| | filas consolidadas | si se apila histórico | veredicto |
|---|---|---|---|
| Desaparición | 77,331 | 105,289 (93,831 + 2025) | ideal |
| Operaciones | 40,716 | 555,515 pero con 434,478 duplicados | sano solo si se descarta el consolidado Jul-2026 |

**E: desaparición 2/2 · operaciones 1.5/2.**

## 7. Criterio F — actualidad (2024–2026)

Desaparición llega a **Dic-2025** (12/12 meses en cada año 2019–2025, sin huecos).
Operaciones llega a **Sep-2026** (2026 con 9 meses; 2022 solo 3 meses por inicio de serie).

**F: desaparición 2/2 (cumple) · operaciones 2/2 (mejor, pero no compensa D).**

## 8. Criterio G — diccionario y metadatos

Ambos publican `.xlsx` con las 10/6 variables documentadas (nombre, descripción, tipo,
tamaño) y `.docx` de metadatos. Defectos a citar en el informe:

- Diccionario de desaparición: dice `AÑO` «Formato: aaaammdd» (falso, es un año de 4
  dígitos), y escribe «Distristo», «Nacinalidad», «ocurrio». Documenta `RANGO_EDAD`
  «Contiene valos vacios» (coincide con el 1.55% medido: buena señal de honestidad).
- Diccionario de operaciones: documenta que los ceros son legítimos («Contiene valores
  cero porque no se registró la intervención»), pero declara `VALOR` tamaño 9 y el máximo
  real es 513,820,390 (9 dígitos → 1,000× el rango declarado).

**G: empate 2/2** (desaparición con errores tipográficos; operaciones con metadato de rango
incorrecto).

## 9. Criterio H — limpieza: nulos y outliers

Desaparición, recurso 10 columnas:

```
SEXO ............. 83 nulos (0.11%)
RANGO_EDAD ..... 1,197 nulos (1.55%)
resto ............ 0 nulos
duplicados exactos ... 0
outliers IQR (Q1=1, Q3=2, bigote=3.5) ... 7,612 filas (9.84%), todas por cola derecha
máximo 53 casos = 1 fila (SJL, nov-2019, F, 12-17 años) → plausible, no error
```

Desaparición, recurso 13 columnas (el histórico):

```
EST_CIVIL ........ 13,480 nulos (14.37%) · 6 valores + "NO INDICA" (8,286)
NIVEL_EDUCATIVO .. 60,731 nulos (64.72%)  ← >50%, no usable como eje principal
OCUPACION ........ 60,790 nulos (64.79%)  ← idem (288 valores distintos)
```

Operaciones:

```
nulos ............ 0
ceros ............ 16,282 (40.0%) — estructurales, documentados
outliers IQR ..... 7,991 filas (19.63%)
VALOR > 1e6 ...... 209 filas (0.51%)
peor caso ........ CONTRABANDO (VALORIZACION) RP PUNO Feb-2024 = 513,820,390
consolidado Jul-2026 ... 434,478 filas duplicadas de 555,515 (78.2%); una misma
                         clave AÑO-MES-VARIABLE-DEPENDENCIAS aparece 28 a 62 veces
```

**H: desaparición 1.5/2 · operaciones 0.5/2.**

## 10. Tablero final

| Criterio | Desaparición | Operaciones | Comentario |
|---|---|---|---|
| A numérico homogéneo | 2.0 | 1.0 | operaciones mezcla kilos/soles/conteos |
| B categóricas 5–60 | 2.0 | 2.0 | empate |
| C decimales | 2.0 | 2.0 | empate (desaparición vía tasa + RENIEC) |
| D jerarquía + 4 joins | 2.0 | 0.0 | **decisivo**: ubigeo vs 38.9% sin geografía |
| E volumen | 2.0 | 1.5 | archivo de operaciones con 78% duplicados |
| F actualidad | 2.0 | 2.0 | empate (2025 vs 2026) |
| G diccionario/metadatos | 2.0 | 2.0 | empate |
| H limpieza | 1.5 | 0.5 | 1.55% nulos vs 40% ceros + 19.6% outliers |
| **Total** | **15.5/16** | **11.0/16** | |

## 11. Los 3 riesgos que el informe debe mitigar (con cifras)

1. **Nulos que superan el 50% en dos campos del recurso histórico.**
   `NIVEL_EDUCATIVO` 60,731/93,831 = 64.72% y `OCUPACION` 60,790/93,831 = 64.79%
   (y `EST_CIVIL` 14.37%). Regla: no imputar; cargar `'SIN DATO'`, excluir esas columnas
   de los groupBy obligatorios y explicar en el informe que los nulos son del sistema de
   registro, no del ETL. En el recurso de 2025 esos campos no existen: si se combinan
   ambos, el 100% de las filas 2025 queda en NULL en esas 3 columnas.

2. **Outliers de cola larga en `CANTIDAD`: 7,612 filas (9.84%) sobre el bigote IQR.**
   Mediana 1, media 1.84, máximo 53 (una sola fila: San Juan de Lurigancho, nov-2019,
   sexo F, 12-17 años). No se eliminan: se reporta media **y** mediana, y en las consultas
   con decimales se muestra el promedio con y sin el 1% superior para evidenciar la
   sensibilidad (53 vs 1 mueve el promedio del distrito más de 500%).

3. **Riesgo de duplicar el 100% de los casos al apilar los dos recursos.**
   El CSV 2019-2024 (13 col., 93,831 filas, UTF-8-BOM) y el 2019-2025 (10 col., 77,331
   filas, Latin-1) **suman exactamente lo mismo por año** (Δ suma = 0.00% en los 6 años
   comparables: 26,331 / 18,481 / 20,141 / 18,862 / 18,192 / 19,420). Apilarlos sin regla
   produce 171,162 filas y 284,748 casos, el doble de la realidad. Regla adoptada: base =
   2019-2024 (13 col.) + solo las 11,458 filas de 2025 del recurso nuevo → 105,289 filas,
   142,374 casos, 13 columnas.

Riesgo administrativo (no técnico, pero anula el trabajo): el dataset deriva del mismo
SIDPOL que «Denuncias policiales registradas a nivel nacional» (grupo 4). Es un dataset
distinto con diccionario propio, pero **hay que registrar la URL en la pestaña PC3 del
Google Sheet antes de escribir código** y confirmar con el profesor que no se considera
repetido.

## 12. Reproducir

```bash
curl -sG "https://www.datosabiertos.gob.pe/api/3/action/package_show" \
  --data-urlencode "id=denuncias-por-desaparición-de-personas-mininter"
curl -sG "https://www.datosabiertos.gob.pe/api/3/action/package_show" \
  --data-urlencode "id=resultados-de-las-operaciones-policiales-nivel-nacional-mininter"
# ojo: package_search devuelve 404/301 en este portal; usar package_list (3,669 datasets)
curl -s -o desap.csv "https://www.datosabiertos.gob.pe/sites/default/files/\
DATASET_Personas%20Desaparecidas%20-%20Enero%202019%20a%20Diciembre%202025.csv"
curl -s -o oper.csv "https://www.datosabiertos.gob.pe/sites/default/files/\
DATASET%20Produccion%20Policial%20Octubre%202022%20a%20Setiembre%202026.csv"
```
