# Alternativas de dataset — barrido completo del rubro SEGURIDAD Y JUSTICIA (PNDA)

Verificado el **16-nov-2026** contra la API CKAN de la PNDA y **midiendo los archivos reales**
(no la descripción del portal). Método:

```
GET /api/3/action/package_list?x=1        -> 3,669 datasets (lista completa, 282 KB)
GET /api/3/action/package_show?x=1&id=<slug>  -> metadatos + recursos (38 datasets inspeccionados)
descarga + pandas: filas, columnas, nulos, cardinalidad, min/max, años
```

> Pitfall de la API: `package_search` y `status_show` responden **404/418** (WAF) en esta
> instancia; `package_list` y `package_show` **sí** funcionan, pero solo si se agrega un
> parámetro basura (`?x=1`) antes de `q`/`id`. Sin eso el WAF devuelve HTML chino (error 418).

Fuente de verdad de lo tomado: `docs/datasets-tomados.csv` (grupos 1-7).

## 1. Descartados por regla del enunciado (derivados de un dataset tomado)

| Dataset | Por qué se descarta |
|---|---|
| **Violencia contra la mujer y los integrantes del grupo familiar registrada por la PNP** | Tiene **exactamente la misma estructura** que *Denuncias policiales registradas* (tomado #4): `UBIGEO_HECHO, DPTO_HECHO, PROV_HECHO, DIST_HECHO, AÑO, MES, CANTIDAD`, y en el archivo 2026 de denuncias la columna `P_MODALIDADES` incluye el valor `"Violencia contra la mujer"`. Es un **filtro del mismo dataset** → prohibido. Medido: consolidado Ene-2018→Jul-2026 = 94,941 filas, pero **solo 1 categórica** además de la geografía (más pobre que desaparición). |
| Incidencias/incidentes por cámaras de videovigilancia (6 slugs distintos) | Tomado #2. |
| Trata de personas (`información-sobre-trata…`, `casos-intervenidos-por-el-delito-de-trata…`) | Tomado #5. |
| `delitos-denunciados-ante-el-ministerio-publico-de-lambayeque-2019-2023` | Recorte regional del dataset tomado #7. |
| ENAPRES – Seguridad ciudadana (2014-2017, 21 MB/año) | Es la **fuente** de *Indicadores de seguridad ciudadana y tendencias territoriales* (tomado #3) y solo llega a 2017. |

## 2. Descartados por volumen (medidos, no sirven para 25 consultas)

| Dataset | Filas medidas | Motivo |
|---|---|---|
| Casos fiscales de flagrancia delictiva [MPFN] | 34 (2026) / 7.6 KB (2025) | agregado anual, 17 columnas, 34 filas |
| Casos fiscales de violencia contra la mujer [MPFN] | 56 (2026) | 16 columnas, una fila por distrito fiscal |
| Delitos informáticos [MPFN] | 238 (2026), 60-90 KB/año | sin volumen |
| Servicios de Medicina Legal [MPFN] | 947 fila-año (2025) | 8 columnas |
| Fiscalías activas [MPFN] | ~1,000/año | es una **dimensión**, no un hecho |
| Registro de denuncias de violencia de género Tacna 2025 | 905 **bytes** | inservible |
| Unidades policiales especializadas e infraestructura | 389 filas | solo infraestructura (materiales de pared/piso) → dimensión |
| Inhabilitaciones judiciales (OECE), extradiciones (RREE), armas SUCAMEC, abogados sancionados (MINJUSDH) | 1 recurso xlsx/docx cada uno, 2017-2022 | sin serie temporal |
| MDSI San Isidro (seguridad/tránsito) | xls municipales, 40 KB-5 MB | municipal, sin jerarquía nacional |
| CENACOM 2016/2017 [INEI] | ZIP externo, 2016/17 | dato de hace 9 años |

Nota de catálogo: el INPE y la Defensoría del Pueblo **figuran como organizaciones registradas**
(406 en `group_list`), pero **no publican datasets**: ninguno de los 3,669 slugs corresponde a
esos organismos (barrido por `penitenciar`, `inpe`, `carcel`, `reclus`, `poblacion`,
`hacinam`, `condenad`, `defensor`) y el buscador del portal devuelve 0 datasets para
"penitenciario". La oferta real del rubro es MININTER, MPFN, PJ, MIMP, DEVIDA, SUTRAN,
SUCAMEC, RREE, MINJUSDH, OECE y municipios.

## 3. Ranking de las alternativas viables (medido archivo por archivo)

| # | Dataset (nombre exacto) | Organización | CSV medido | Filas | Cols | Años | Numéricos | Categóricas (cardinalidad) | Geo | Dicc. | Encaje |
|---|---|---|---|---|---|---|---|---|---|---|---|
| **1** | **Personal policial de la Policía Nacional del Perú [MININTER]** | MININTER | `DataSet - Personal Policial - Enero 2025 a Septiembre 2026.csv` (129 MB) | **495,311** | 19 | 2025-01 → 2026-09 (21 meses) | `CANTIDAD` (0-5,088) | SEXO 2, CATEGORIA_OFI_SUB 3, CATEGORIA_ARM_SERV 3, FUNCION 3, CLASIFICACION 4, ORIGINARIOS 4, ESTUDIOS 7, APT.PSICOSOMATICA 11, EXTRANJERO 21, PROFESION 26, DEPENDENCIAS 60, UNIDADES 251 | **Sí, 3 niveles** (DPTO 27 / PROV 197 / DIST 1,053) | Sí (xlsx + docx) | **5/5** |
| 2 | Resultados de las operaciones policiales a nivel nacional [MININTER] | MININTER | `DATASET Produccion Policial Octubre 2022 a Julio 2026.csv` (29.9 MB) | **555,515** | 6 | 2022-10 → 2026-07 (el corte de 2.15 MB llega a 2026-09, pero solo 40,716 filas) | `VALOR` (0 – 513,820,390; 10,082 filas con decimales) | VARIABLE 18, DEPENDENCIAS 53, DIR_REG_FRE 6 | No (solo región policial) | Sí | 4/5 |
| 3 | Parque automotor de la Policía Nacional del Perú [MININTER] | MININTER | `DATASET_Parque_automotor_de_la_PNP_{2024,2025,2026}.csv` | **13,322** (3 archivos) | 13 | 2024-12, 2025-12, 2026-09 | `CANTIDAD`, `AÑOFABRICACION` (1970-2026) | TIPO_VEHICULO 25, TIPO_UNIDAD 6, TIPO_PROCEDENCIA 9, ESTADO 1 | **Sí + UBIGEO** (26/193/884) | Sí | 3/5 |
| 4 | Ingreso por delitos año 2024 y 2025 de la Corte Superior de Justicia de Piura | CSJ Piura (PJ) | `registro_delitos2.csv` (15.9 MB) | **42,093** | 25 | 2024-01 → 2025-06 | `EDAD` (0-97) | DELITO 297, ARTICULO 394, GENERICO 44, MOTIVO_INGRESO 39, TIPO_PARTE 21, PROCESO 18, SEDE 15 | Solo Piura (1 dpto, 11 prov) | Sí | 3/5 |
| 5 | Procesos ingresados y resueltos de los módulos penales de violencia contra las mujeres… | Poder Judicial | `04.res_dashboard-modulos_penales_utf8.csv` (1.5 MB) | **6,584** | 13 | 2019 → 2024 | `INGRESOS`, `RESUELTOS` (dos numéricas) | ETAPA 2, INSTANCIAS 4, NORMATIVIDAD 2, DEPENDENCIA 46, CORTE 8 | Sí (6 dptos / 15 prov / 19 dist) | No | 3/5 |
| 6 | Casos atendidos por los Centros Emergencia Mujer, según sexo y departamento [MIMP] | MIMP | `2.1.1 BdD_CEM_Casos_9.csv` (1.9 MB, sep=`;`) | **4,699** | **54** (≈40 numéricas) | 2012 → 2025 | total, hombres, mujeres, 4 tipos de violencia, 6 bandas de edad ×3 | NOMBRE CENTRO 749, DISTRITO 374, CODIGO CENTRO 435 | **Sí + UBIGEO** (33/229/374) | Parcial (solo CHAT-100) | 3/5 |
| 7 | Casos agrupados mensualmente de violencia contra la mujer… Región Piura | GORE Piura | `dataset_violencia_0825.csv` (4.1 MB) | **25,057** | 25 | 2021 → 2025 | 13 numéricas (F_VIO, M_VIO, 6 bandas de edad, 3 tipos) | IPRESS 474, CLAS 111, EJECUTORA 12, CATEGORIA 9 | Sí + UBIGEO (solo Piura) | Sí | 2/5 |
| 8 | Ingreso por delitos en flagrancia y conducción en estado de ebriedad - CSJ Piura | CSJ Piura (PJ) | `delitos_flagrancia_0.csv` (5.2 MB) | **13,774** | 25 | 2016 → 2025 | `EDAD` | DELITO 147, ARTICULO 197, GENERICO 33, TIPO_PARTE 12, PROCESO 2 | Solo Piura | Sí | 2/5 |

## 4. Recomendación

**#1 — Personal policial de la Policía Nacional del Perú [MININTER]**
`https://www.datosabiertos.gob.pe/dataset/personal-policial-de-la-policía-nacional-del-perú-mininter`
Recurso principal:
`https://www.datosabiertos.gob.pe/sites/default/files/DataSet%20-%20Personal%20Policial%20-%20Enero%202025%20a%20Septiembre%202026.csv`

Por qué encaja mejor que *Denuncias por desaparición* (93,831 × 13, 2019-2024):

- **495,311 filas** (5× las 93,831 de desaparición) con **12 categóricas** de cardinalidad media,
  frente a 6 en desaparición, donde `NIVEL_EDUCATIVO` y `OCUPACION` vienen **65% nulas** y
  `OCUPACION` tiene 288 valores (medido sobre `data/denuncias_desaparicion_2019_2024.csv`).
- Tres niveles geográficos comparables (`DEPARTAMENTO`/`PROVINCIA`/`DISTRITO`, 27/197/1,053
  valores) → sirve para los **4 joins de tipos distintos** (5c) construyendo las dimensiones
  dpto/prov/dist desde el propio dataset, y para las **3 vistas con join** (5e).
- Jerarquía administrativa adicional: `DEPENDENCIAS_GENERALES` (10) → `DEPENDENCIAS` (60) →
  `UNIDADES` (251), y `CLASIFICACION` (comisaría básica / especializada / otras).
- `SEXO`, `CATEGORIA_OFI_SUB`, `FUNCION`, `ESTUDIOS_ADICIONALES`, `ORIGINARIOS`
  (castellano/quechua/aymara) dan 5a (groupBy + filtro con 2+ columnas) y 3a/3b sin repetir.
- Datos **2025-01 a 2026-09** (lo más reciente del portal) y diccionario + metadato publicados.
- 129 MB: procesa bien con `spark-shell` local (no hace falta YARN).

Riesgos reales (hay que documentarlos en el informe):

1. **`MARZO 2025` viene duplicado**: 134,368 filas de las cuales **117,227 son duplicados
   exactos** (el resto de meses ~17-19 mil). Sin `dropDuplicates()` el total se infla 31%.
   Verificado: `marzo.duplicated().sum() = 117,227`. Filas útiles = **378,084**.
2. **No hay UBIGEO** (solo nombres) → los joins geográficos van por nombre normalizado o por
   una tabla de ubigeos externa; conviene documentar la normalización (tildes, "LIMA
   METROPOLITANA" vs "LIMA").
3. **El único numérico es `CANTIDAD` (entero)** → los decimales de 3c salen de promedios,
   ratios y porcentajes (permitido), no de un campo decimal nativo.
4. `PROFESION` tiene **34.6% de nulos** y `ESTUDIOS_ADICIONALES` mezcla "TÍTULO" con "TITULO"
   (dos valores por el mismo concepto) → hay que limpiar y decirlo.
5. No cubre 2024 (empieza 2025). Si el profe exige 2024, usar el #2 en paralelo.

**#2 — Producción policial** es la alternativa si se prefiere **decimales nativos y 4 años**
(2022-10 → 2026-09, 555,515 filas, `VALOR` hasta 513 millones): su debilidad es que **no tiene
geografía distrital** (solo 6 `DIR_REG_FRE` y 53 dependencias/regiones policiales), así que los
joins de 5c quedan más artificiales y hay outliers que hay que tratar explícitamente.

**#3 — Parque automotor** no aguanta como dataset único (13,322 filas, snapshots anuales), pero
es la **mejor tabla de dimensión** del rubro: tiene `UBIGEO` más `TIPO_VEHICULO` (25) y
`AÑOFABRICACION`. Si el grupo quiere un dataset con ubigeo real y no le importa el volumen,
es la opción "limpia".

## 5. Archivos de validación (borrables si no se elige)

```
data/_sondeo/pnda_package_list.json            3669 datasets
data/_sondeo/meta/*.json                       38 metadatos package_show
data/_sondeo/personal_policial_consolidado.csv 129 MB 495,311 filas
data/_sondeo/ppolicial_2022_2026_jul.csv       29.9 MB 555,515 filas
data/_sondeo/parque_automotor_{2024,2025,2026}.csv  13,322 filas
data/_sondeo/csjpiura_delitos.csv              15.9 MB 42,093 filas
data/_sondeo/viol_mujer_pnp_2018_2026.csv      4.1 MB 94,941 filas (derivado, descartado)
... (resto de sondeos de MPFN/MIMP/PJ)
```
