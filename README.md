# LC3 — Laboratorio Calificado 03 (CC531 A, 2026-2)

Grupo 8 · Arbués Enrique Pérez Villanueva · Sergio Pezo
Entrega: **martes 17-nov-2026, 18:00** (cutoff 18:30, sin prórroga). Un zip por integrante.

Dataset elegido: **Denuncias por desaparición de personas [MININTER]**, 2019–2024
(PNDA, rubro Seguridad y Justicia) — 93,831 registros, 121,427 personas.
Enunciado: `material/L3-cc531-26-2.pdf` · texto: `material/L3-cc531-26-2.txt`

---

## Estructura

```
data/       denuncias_desaparicion_2019_2024.csv   (dataset crudo)
code/comun/ esquema.scala        carga, limpieza, dimensiones y vistas temporales
code/rdd/   03a, 03b, 03c_1..3   consultas con mapReduce (RDD)
code/sql/   05a..05g             consultas DataFrame, vistas temporales y Excel
sql/        01_ddl.sql           arquitectura de tablas (modelo estrella)
            02_etl.sql           ETL completo (staging -> dimensiones -> hechos)
            dump-denuncias.sql   dump de la base cargada (pg_dump)
            pg16_arranque.sh     levanta el PostgreSQL 16 local (puerto 5433)
            pg16_parada.sh       lo detiene
excel/      *.xlsx               resultados de 5e y 5f exportados con Spark
evidencias/ salidas/*.txt        salida real de cada consulta
            pg-*.png             capturas de la base de datos funcionando
informe/    informe.tex + pdf    informe del laboratorio
presentacion/                    exposición de 10 minutos
docs/       00-analisis-lc3.md   qué pide el profe, ambigüedades y riesgos
            01-mapa-requisitos.md matriz requisito -> artefacto -> estado
            02-datasets-candidatos.md shortlist evaluada
            mapa-consultas.md    las 25 preguntas y por qué no se repiten
bin/        ejecutar.sh          corre una consulta con el esquema común
            correr_todas.sh      corre todas y guarda las salidas
            limpiar_salidas.sh   limpia los logs y regenera evidencias/salidas
lib/        postgresql-42.7.6.jar, spark-excel_2.12-3.3.1_0.18.7.jar
```

## Cómo reproducirlo

```bash
# 1. Spark 3.3.2 (local[*], Scala 2.12.15, JVM 11)
sparkctl doctor

# 2. Base de datos: PostgreSQL 16 en el puerto 5433
./sql/pg16_arranque.sh
psql -h /tmp -p 5433 -U macrodatos -d denuncias -f sql/01_ddl.sql
psql -h /tmp -p 5433 -U macrodatos -d denuncias -f sql/02_etl.sql

# 3. Una consulta cualquiera
./bin/ejecutar.sh code/rdd/03a_max_min_por_sexo.scala

# 4. Exportación a Excel (requiere el jar de spark-excel)
./bin/ejecutar.sh code/sql/05g_excel.scala lib/spark-excel_2.12-3.3.1_0.18.7.jar

# 5. Todas las consultas + evidencias
./bin/correr_todas.sh
```

## Versiones

| Pieza | Versión |
|---|---|
| Spark | 3.3.2 (Scala 2.12.15, OpenJDK 11) |
| PostgreSQL | 16.16 |
| Driver JDBC | postgresql-42.7.6.jar |
| spark-excel | com.crealytics:spark-excel_2.12:3.3.1_0.18.7 |

## Pendiente operativo

- Registrar la URL del dataset en la pestaña PC3 del cuadro de temas del aula (riesgo de anulación
  si otro grupo toma el mismo dataset).
- Al terminar la exposición se desinstala el PostgreSQL 16 local y se borra `~/pg16data`.
