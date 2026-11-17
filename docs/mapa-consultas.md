# Mapa de las 25 consultas — LC3 CC531

Dataset: **Denuncias por desaparición de personas [MININTER]**, 2019–2024 (93,831 registros,
121,427 personas). Requisito 6 del enunciado: ninguna consulta repite el objetivo de otra.

Leyenda de grupos: **RDD** = punto 3 (Scala + mapReduce), **DF** = punto 5a–5d (DataFrame API),
**VT** = punto 5e–5f (vistas temporales + `spark.sql`).

| # | Archivo | Grupo | Objetivo de la consulta (pregunta en lenguaje natural) |
|---|---|---|---|
| 1 | `code/rdd/03a_max_min_por_sexo.scala` | 3a | ¿Cuál es el mayor y el menor número de personas desaparecidas en un solo hecho, por sexo, y dónde ocurrió? |
| 2 | `code/rdd/03b_estadisticos_cantidad.scala` | 3b | ¿Cuántas personas desaparecen en promedio en un hecho? ¿Cuál es el valor típico (mediana) y cuánto se dispersa (desviación)? |
| 3 | `code/rdd/03c_1_menores_por_departamento.scala` | 3c | ¿Qué porcentaje de las desapariciones de cada departamento corresponde a menores de 18 años? |
| 4 | `code/rdd/03c_2_extranjeros_por_anio.scala` | 3c | ¿Qué porcentaje de las denuncias por año corresponde a personas extranjeras y cómo varía año a año? |
| 5 | `code/rdd/03c_3_estacionalidad_mensual.scala` | 3c | ¿Existe estacionalidad mensual? Índice de cada mes frente al 8.33 % esperado. |
| 6 | `code/sql/05a_1_departamento_rangoedad.scala` | 5a | ¿Qué combinaciones departamento + rango de edad superan las 1,000 personas? |
| 7 | `code/sql/05a_2_anio_sexo_mujeres.scala` | 5a | ¿En qué años las desapariciones de mujeres superaron las 5,000 personas? |
| 8 | `code/sql/05a_3_region_periodo.scala` | 5a | ¿Cómo cambia el volumen por región natural entre pre-pandemia, pandemia y post-pandemia? |
| 9 | `code/sql/05a_4_nacionalidad_rangoedad.scala` | 5a | ¿Qué nacionalidades extranjeras y rangos de edad superan las 30 personas? |
| 10 | `code/sql/05b_1_promedio_por_departamento.scala` | 5b | ¿Cuál es el promedio de personas por registro en cada departamento? |
| 11 | `code/sql/05b_2_top_distritos_desc.scala` | 5b | ¿Cuáles son los 15 distritos con más personas desaparecidas (orden descendente)? |
| 12 | `code/sql/05c_1_inner_region.scala` | 5c | **INNER**: ¿cómo se reparten las desapariciones entre costa, sierra y selva? |
| 13 | `code/sql/05c_2_left_ubigeos_sin_registros.scala` | 5c | **LEFT**: ¿existen ubigeos del catálogo sin ningún registro? (cobertura territorial) |
| 14 | `code/sql/05c_3_right_calendario.scala` | 5c | **RIGHT**: ¿existe algún mes del calendario 2019–2024 sin registros? (cobertura temporal) |
| 15 | `code/sql/05c_4_full_menores_adultos.scala` | 5c | **FULL OUTER**: ¿todos los departamentos tienen víctimas menores y adultas? |
| 16 | `code/sql/05d_1_severidad_when.scala` | 5d | ¿Cómo se clasifican los hechos por severidad (`when`) y cuántas personas aporta cada nivel? |
| 17 | `code/sql/05d_2_participacion_periodo.scala` | 5d | ¿Qué peso porcentual tiene cada periodo pandémico sobre el total nacional? |
| 18 | `code/sql/05d_3_dispersion_por_sexo.scala` | 5d | ¿La dispersión del número de personas por registro es igual en hombres y mujeres? |
| 19 | `code/sql/05d_4_fecha_trimestre.scala` | 5d | ¿En qué trimestre calendario se concentran más desapariciones (fecha construida)? |
| 20 | `code/sql/05e_1_variacion_interanual.scala` | 5e | **JOIN (self)**: ¿cuántos meses subieron o bajaron respecto al mismo mes del año anterior? |
| 21 | `code/sql/05e_2_grupoetario_sexo.scala` | 5e | **JOIN ×2**: ¿qué grupo etario y sexo concentran más personas y qué % del total representan? |
| 22 | `code/sql/05e_3_region_periodo_join.scala` | 5e | **JOIN ×2**: evolución por región y periodo, con % dentro de cada periodo. |
| 23 | `code/sql/05f_1_top_distritos_mujeres_2024.scala` | 5f | **ORDER BY + WHERE**: top 10 distritos de mujeres desaparecidas en 2024. |
| 24 | `code/sql/05f_2_nacionalidades_2022.scala` | 5f | **ORDER BY + filtros**: nacionalidades extranjeras con más casos desde 2022. |
| 25 | `code/sql/05f_3_meses_estudiantes.scala` | 5f | **ORDER BY + filtros**: meses con más estudiantes desaparecidos. |

## Por qué no se repiten

- **3a** mide extremos por sexo (max/min de un registro); **5b.2** mide acumulados por distrito.
- **3b** describe la distribución global de `CANTIDAD`; **5d.3** compara esa dispersión entre sexos.
- **3c.1** (menores por departamento) es porcentaje territorial; **5c.4** (menores vs adultos) es
  comparación de dos agregados por departamento con FULL OUTER.
- **3c.2** (extranjeros por año) mide participación anual; **5a.4** y **5f.2** miran el perfil y la
  evolución de las nacionalidades, no la participación nacional.
- **3c.3** (estacionalidad mensual) usa índice frente al esperado; **5f.3** filtra solo
  estudiantes; **5e.1** compara cada mes con el mismo mes del año anterior.
- **5c.1/5c.2/5c.3/5c.4** son cuatro controles distintos: reparto por región (inner), cobertura
  territorial (left), cobertura temporal (right) y simetría de agregados (full outer).
- **5e** usa las mismas dimensiones con otros objetivos: variación interanual, perfil etario-sexo
  y participación regional por periodo. Ninguna es un `join` de validación como las 5c.
- **5f.1**, **5f.2** y **5f.3** filtran universos distintos (mujeres 2024, extranjeros 2022+,
  estudiantes) y siempre con `ORDER BY` + `WHERE`/`HAVING`.

## Cobertura de los requisitos

| Requisito | Consultas |
|---|---|
| 3a — un mapReduce | 1 |
| 3b — un mapReduce | 2 |
| 3c — 3 consultas × 3 mapReduce | 3, 4, 5 |
| 5a — 4 `groupBy` + `filter` con 2+ columnas | 6, 7, 8, 9 |
| 5b — promedio + orden descendente | 10 (promedio), 11 (desc) |
| 5c — 4 joins, uno de cada tipo | 12, 13, 14, 15 |
| 5d — 4 con `functions._` | 16, 17, 18, 19 |
| 5e — 3 con vistas temporales + join | 20, 21, 22 |
| 5f — 3 `orderBy` + filtros | 23, 24, 25 |
| 5g — Excel con Spark | `code/sql/05g_excel.scala` → `excel/` |
