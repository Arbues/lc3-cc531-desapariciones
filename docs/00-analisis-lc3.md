# Análisis del LC3 (publicado 16-nov-2026)

Fuente: aula virtual CC531 A (curso 8476) → tarea `assign 80714`
"Enunciado y entrega Laboratorio calificado 3 26-2", adjunto `L3-cc531-26-2.pdf`
(PDF generado 14-nov-2026 01:30, 2 páginas).

## 1. Datos duros de la entrega

| Dato | Valor |
|---|---|
| Sección del aula | 16 de noviembre - 22 de noviembre |
| Apertura de entregas | 14-nov-2026 00:00 |
| **Fecha límite** | **17-nov-2026 18:00** |
| Cierre duro (cutoff) | 17-nov-2026 18:30 |
| Nota máxima | 20 |
| Intentos | 1 |
| Entrega por | archivo(s) subidos al aula, **un zip por cada integrante** |
| Estado hoy (16-nov 17:20) | sin entregar, 0 archivos |
| Exposición | 10 minutos por grupo, con descuento si se pasa o hay desorden |

Margen real desde este análisis: ~24 horas.

## 2. Lo que pide el enunciado, punto por punto

1. **Dataset PNDA del rubro SEGURIDAD Y JUSTICIA**, no usado en evaluaciones anteriores ni
   por otro grupo. Prohibido el mismo dataset "incluso si fuera otro periodo de tiempo o
   subconjuntos diferentes". Si dos grupos coinciden, **no se evalúa a ninguno**.
2. **Descripción del dataset**: organización dueña, rubro, teoría que implican los datos,
   características, campos, reglas de los datos, qué información se puede obtener.
3. **Scala + Spark con mapreduce (RDD, no DataFrame)**:
   - a) agrupar un campo categórico por tipos y, sobre otro campo numérico, mayores y menores;
   - b) promedio, "media", desviación, mayor y menor de un campo numérico;
   - c) **3 consultas con decimales, cada una con al menos 3 MapReduce** (3 jobs encadenados).
4. **PostgreSQL**: arquitectura de tablas definida por el grupo + sección de informe
   "Proceso ETL para la base de datos" (extracción, modelo E-R, llenado) + **capturas de la
   BD funcionando** + dump `.sql`.
5. **Spark SQL** (API Scala):
   - a) 4 consultas con `groupBy` + `filter` que usen 2 o más columnas;
   - b) 1 con promedio de una columna + 1 con ordenamiento decreciente;
   - c) 4 consultas con `join` "en sus diversos tipos" (≈ inner, left, right, full);
   - d) 4 consultas con `org.apache.spark.sql.functions._`;
   - e) con **vistas temporales + `spark.sql`**: 3 selecciones con join (distintas a las de c);
   - f) 3 `orderBy` combinados con otros operadores de filtro;
   - g) guardar los resultados de e) y f) **en Excel usando Spark**.
6. Las consultas de RDD, de Spark SQL y de vistas temporales deben ser **distintas entre sí**
   (objetivos diferentes). Mismas versiones que en clase: **Spark 3.3.2, PostgreSQL 15.13
   (máximo 16)**. Entregar informe + presentación.
7. **Por cada consulta, el informe y la presentación deben indicar** (esto es donde se cae
   puntaje): tipo/grupo de pregunta, la pregunta en lenguaje natural a la que responde,
   explicación de la solución, **interpretación del resultado**, y qué columnas/filas/
   características se usaron y las partes importantes del código.
8. **Entregables obligatorios**: códigos `.scala` con sus salidas; SQL de creación y llenado
   de Postgres; dump `.sql`; informe; presentación. Además: el dataset, los Excel generados.
   Un zip por integrante, subido al aula.
9. Si falta un obligatorio, el trabajo no se evalúa.

**Consecuencia práctica:** lo obligatorio son 6 piezas (`.scala` + salidas, SQL, dump,
informe, pptx, dataset). Presentación y Excel también puntúan fuerte por el punto 7.

## 3. Ambigüedades que hay que resolver hoy

| # | Ambigüedad | Lectura propuesta |
|---|---|---|
| 1 | "promedio, la media, la desviación" (3-b): promedio y media son sinónimos en castellano | interpretar como **promedio, mediana y desviación estándar** y decirlo explícitamente en el informe; si el profe pregunta, ya está cubierto |
| 2 | "join en sus diversos tipos" (5-c) y "3 selección utilizando join en sus diversos tipos" (5-e) | 5-c: inner, left_outer, right_outer, full_outer (4 tipos). 5-e: los mismos 4 tipos pero con **otras tablas y otro objetivo** (p.ej. self-join y join con tabla derivada) |
| 3 | "Excel usando Spark" (5-g) | Spark 3.3.2 no escribe `.xlsx` nativo → usar `com.crealytics:spark-excel_2.12:0.18.7` (compatible con Spark 3.3/Hadoop 3.3.2) y declararlo en el informe; alternativa: CSV + conversión, pero eso **no** es "usando Spark" |
| 4 | "3 MapReduce encadenados" (3-c) | 3 jobs donde la salida de uno es la entrada del siguiente (`first()` sobre RDD derivado), con RDD/caché en medio |
| 5 | "mismas versiones de PostgreSQL usados en clases" | clase usa 15.13 (máx 16) → instalar **PostgreSQL 16** (o 15) local; el WSL hoy solo tiene cliente 18, sin servidor |

## 4. Riesgos ordenados por impacto

1. **Dataset sin registrar y 24 h de plazo.** El cuadro de temas ya tiene 7 datasets tomados
   y el grupo 8 está vacío. Si otro grupo registra el mismo dataset, se anula el trabajo.
   Acción inmediata: elegir y escribir la URL en la pestaña PC3.
2. **Dataset que no soporte las 4 formas de join.** Si el CSV es una sola tabla plana, hay
   que construir tablas derivadas/dimensiones; es legítimo, pero hay que planearlo antes de
   escribir consultas (ver `02-datasets-candidatos.md`, la opción 1 ya trae jerarquía
   geográfica UBIGEO/DEPTO/PROV/DIST).
3. **Mezclar RDD y SQL con las mismas preguntas** (punto 6): obliga a mantener un mapa de
   25 consultas con objetivos distintos (5 de RDD, 14 de DataFrame, 6 de vistas temporales).
   Sin ese mapa se repiten preguntas y se descuenta.
4. **Postgres ausente en la máquina.** No hay servidor instalado (solo `psql` 18 cliente).
   Instalar PG 16 + crear BD + cargar + capturas: ~2 h si se hace en paralelo con el código.
5. **Exportación a Excel** sin `spark-excel` → se pierde 5-g entera.
6. **Se olvida la interpretación de resultados** por consulta (punto 7). Es el rubro que más
   puntos mueve en el informe y no se puede recuperar después.

## 5. Entorno local: qué está listo y qué no

| Pieza | Estado real (verificado hoy) |
|---|---|
| Spark 3.3.2 + `spark-shell` (Scala 2.12.15, JVM 11) | OK en `~/macrodatos/spark` (`sparkctl doctor` = "entorno utilizable") |
| Hadoop nativo (si se quiere `--master yarn` o HDFS) | instalado; daemons **detenidos** ahora |
| Driver JDBC `postgresql-42.7.6.jar` | descargado en `lib/` |
| Servidor PostgreSQL | **FALTA**: solo cliente 18.6, sin cluster (`pg_isready` sin respuesta) |
| Driver `spark-excel` | **FALTA**: descargar jar y pasarlo con `--jars`/`--packages` |
| Docker / clúster multinodo | no disponible en esta distro ahora mismo (Desktop apagado) — no hace falta para LC3 |
| Jupyter / notebook | no hace falta: el entregable es `.scala` |
| Plantilla de informe | `material/Plantilla201doc (1).docx` y `material/Plantilla201latex (1).zip` (las mismas del LC1) |

## 6. Plan sugerido de 24 horas

| Bloque | Quién | Salida |
|---|---|---|
| 0. Elegir dataset y registrarlo en la pestaña PC3 (30 min) | ambos | URL en el cuadro de temas |
| 1. Ficha del dataset + descripción (punto 2) + ETL/modelo E-R (punto 4) | A | sección de informe |
| 2. Instalar PG 16, crear esquema, cargar, capturas | A | `sql/`, `evidencias/` |
| 3. 3 consultas RDD con mapreduce (3-a/b/c) en `.scala` + salidas | S | `code/rdd/*.scala` |
| 4. 12 consultas Spark SQL DataFrame (5-a..d) + 6 de vistas (5-e/f) | S | `code/sql/*.scala` |
| 5. Export a Excel (5-g) con spark-excel | A | `excel/` |
| 6. Informe (interpretación por consulta) + pptx de 10 min | A pptx / S revisa | `informe/`, `presentacion/` |
| 7. Empaquetar zip por integrante y subir antes de las 18:00 del 17 | ambos | 2 zips subidos |

## 7. Decisiones que necesito de ti (antes de escribir código)

1. **Dataset**: ¿vamos con *Denuncias por desaparición de personas [MININTER]* (93,831 filas,
   jerarquía geográfica completa, ya validada) o prefieres otra de la shortlist? Hay que
   registrarlo en el sheet hoy mismo.
2. **Reparto**: ¿mantenemos A = informe/Postgres/presentación y S = todo el código Scala?
3. **Postgres**: ¿instalo PG 16 local en el WSL (recomendado, es la versión que exige el
   enunciado) o cargas el dataset en el PG del servidor t440?
4. **"Media"**: ¿la interpretamos como mediana y lo dejamos escrito, o le preguntas al profe
   por el chat del curso?
