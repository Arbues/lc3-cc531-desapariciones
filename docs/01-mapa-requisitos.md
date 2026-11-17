# Mapa de requisitos — LC3 CC531 · estado real

Cada fila del enunciado → qué artefacto lo cumple, con qué herramienta y en qué estado está.
Estados: `listo` (ejecutado y verificado), `falta`, `por hacer`.
Responsables: **A** = Arbués, **S** = Sergio.
`listo` = existe el artefacto y su salida real está en `evidencias/salidas/`.

## A. Datos y registro

| # | Exigencia | Artefacto | Estado |
|---|---|---|---|
| 1 | Dataset PNDA rubro SEGURIDAD Y JUSTICIA, no repetido | URL en pestaña PC3 del cuadro de temas | `falta` — requiere la sesión de Google de Arbués |
| 1b | Verificar que no esté tomado | `docs/datasets-tomados.csv` (foto 16-nov) | `listo` |
| 1c | Dataset crudo en el repo | `data/denuncias_desaparicion_2019_2024.csv` (8.1 MB, 93,831 filas) | `listo` |
| 2 | Ficha y descripción del dataset | `informe/secciones/02-dataset.tex` (§2 del PDF) | `listo` |

## B. Consultas en Scala con MapReduce (RDD)

| # | Exigencia | Artefacto | Estado |
|---|---|---|---|
| 3a | Agrupar categórico + max/min numérico | `code/rdd/03a_max_min_por_sexo.scala` | `listo` |
| 3b | promedio + mediana + desviación + max + min | `code/rdd/03b_estadisticos_cantidad.scala` | `listo` |
| 3c | 3 consultas con decimales y 3 MapReduce encadenados | `code/rdd/03c_1_menores_por_departamento.scala`, `03c_2_extranjeros_por_anio.scala`, `03c_3_estacionalidad_mensual.scala` | `listo` |
| — | Entorno Scala | `sparkctl doctor` → entorno utilizable (Spark 3.3.2, Scala 2.12.15, JVM 11) | `listo` |

## C. PostgreSQL

| # | Exigencia | Artefacto | Estado |
|---|---|---|---|
| 4a | Arquitectura de tablas definida por el grupo | `sql/01_ddl.sql` + diagrama E-R (figura 1 del informe) | `listo` |
| 4b | Proceso ETL documentado | `sql/02_etl.sql` + §4 del informe | `listo` |
| 4c | Llenado de la BD | 93,831 hechos, 1,506 ubigeos, 121,427 personas | `listo` |
| 4d | Capturas de la BD funcionando | `evidencias/pg-version.png`, `pg-conteos.png`, `pg-consulta.png` | `listo` |
| 4e | Dump de exportación `.sql` | `sql/dump-denuncias.sql` (16 MB) | `listo` |
| — | Servidor PostgreSQL 16 | `~/pg16` + `~/pg16data`, puerto 5433, `sql/pg16_arranque.sh` | `listo` |
| — | Driver JDBC exigido | `lib/postgresql-42.7.6.jar` | `listo` |

## D. Spark SQL en Scala (DataFrame y vistas temporales)

| # | Exigencia | Artefacto | Estado |
|---|---|---|---|
| 5a | 4 `groupBy` + `filter` con 2+ columnas | `code/sql/05a_1..4_*.scala` | `listo` |
| 5b | 1 promedio + 1 orden descendente | `code/sql/05b_1_promedio_por_departamento.scala`, `05b_2_top_distritos_desc.scala` | `listo` |
| 5c | 4 joins de tipos distintos | `code/sql/05c_1_inner_region.scala`, `05c_2_left_ubigeos_sin_registros.scala`, `05c_3_right_calendario.scala`, `05c_4_full_menores_adultos.scala` | `listo` |
| 5d | 4 consultas con `functions._` | `code/sql/05d_1..4_*.scala` | `listo` |
| 5e | 3 vistas temporales + `spark.sql` con join | `code/sql/05e_1..3_*.scala` | `listo` |
| 5f | 3 `orderBy` combinados con filtros | `code/sql/05f_1..3_*.scala` | `listo` |
| 5g | Guardar e) y f) en Excel con Spark | `code/sql/05g_excel.scala` → 6 `.xlsx` en `excel/` | `listo` |
| 6 | 25 consultas con objetivos distintos | `docs/mapa-consultas.md` | `listo` |

## E. Informe y presentación

| # | Exigencia | Artefacto | Estado |
|---|---|---|---|
| 7 | Por consulta: tipo, pregunta, solución, interpretación, columnas, código | `informe/secciones/06-consultas.tex` (§6, 25 subsecciones) | `listo` |
| 8 | Informe PDF | `informe/informe.pdf` (19 páginas, plantilla UNI) | `listo` |
| 8b | Presentación 10 min | `presentacion/presentacion.pdf` (12 láminas beamer) | `listo` |
| 8c | Códigos `.scala` con sus salidas | `code/**` + `evidencias/salidas/` | `listo` |
| 8d | Dataset en el zip | `data/*.csv` | `listo` |
| 9 | Un zip por integrante | `entrega/LC3_<integrante>.zip` | `por hacer`: subir al aula antes de 17-nov 18:00 |

## F. Pendientes reales (dos)

1. **Registrar la URL del dataset** en la pestaña PC3 del cuadro de temas (sesión de Google de
   Arbués; riesgo de anulación si otro grupo lo toma).
2. **Subir los dos zips** al aula virtual (un zip por integrante).

## G. Notas técnicas para reproducir

- Ejecutor: `./bin/ejecutar.sh code/rdd/03a_max_min_por_sexo.scala`.
- Todas: `./bin/correr_todas.sh` (genera `logs/` y `evidencias/salidas/`).
- El codegen de Spark se desactiva en el arranque (`--conf spark.sql.codegen.wholeStage=false`):
  con JDK 11 + Scala 2.12 el compilador dinámico rompe en consultas con `ROUND` sobre decimales.
  Las consultas 5e y 5g usan además `CAST(... AS DOUBLE)` por la misma razón.
- El ubigeo `150121` (PUEBLO LIBRE / MAGDALENA VIEJA) obliga a deduplicar la dimensión
  geográfica; sin eso los joins duplican 82 personas.
