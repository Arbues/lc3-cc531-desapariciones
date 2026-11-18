# AGENTS.md — LC3 CC531 (grupo 8)

Instrucciones para cualquiera que abra este repositorio (humano o agente). El trabajo está
completo y verificado; lo que sigue es cómo mantenerlo y qué falta cerrar.

## Contexto en dos líneas

25 consultas en Scala/Spark sobre las denuncias por desaparición de personas [MININTER] 2019–2024
(93,831 registros = 121,427 personas), más un modelo estrella cargado en PostgreSQL 16 con ETL,
informe en LaTeX y presentación Beamer de 37 láminas (19 principales = ruta de 9 min 30 s,
el resto anexos). Todo reproducible con dos scripts.

## Cómo correr cualquier cosa

```bash
sparkctl doctor                                   # comprobar Spark 3.3.2
./bin/ejecutar.sh code/rdd/03a_max_min_por_sexo.scala
./bin/correr_todas.sh                             # las 26 salidas -> evidencias/salidas/
./sql/pg16_arranque.sh                            # PostgreSQL 16 en el puerto 5433
psql -h /tmp -p 5433 -U macrodatos -d denuncias -f sql/01_ddl.sql
psql -h /tmp -p 5433 -U macrodatos -d denuncias -f sql/02_etl.sql
cd informe && latexmk -pdf informe.tex
cd presentacion && latexmk -pdf presentacion.tex guion-exposicion.tex
```

El export a Excel necesita el jar: `./bin/ejecutar.sh code/sql/05g_excel.scala lib/spark-excel_2.12-3.3.1_0.18.7.jar`
(no está en el repo por peso; se descarga de Maven Central, coordenada
`com.crealytics:spark-excel_2.12:3.3.1_0.18.7`).

## Reglas que no se rompen

1. **Toda cifra del informe sale de `evidencias/salidas/`.** Si se cambia una consulta, se vuelve
   a correr y se regenera la evidencia; no se edita el número a mano.
2. **La carga del CSV vive solo en `code/comun/esquema.scala`.** Ninguna consulta repite lectura,
   limpieza ni creación de vistas.
3. **Los totales deben cuadrar en 121,427** entre RDD, DataFrame y PostgreSQL. Si un join infla el
   total, es una dimensión con la clave repetida: colapsar con `GROUP BY clave` + `MAX(atributo)`.
4. **El codegen de Spark se apaga en el arranque** (`--conf spark.sql.codegen.wholeStage=false`) y
   las consultas con porcentajes usan `CAST(... AS DOUBLE)`. Con JDK 11 + Scala 2.12, `ROUND`
   sobre decimales rompe el compilador del REPL. No «arreglar» eso quitando los CAST.
5. **Cada consulta es una sola sentencia entre llaves.** El REPL de `spark-shell` no lee
   expresiones multilínea sin llaves.

## Qué falta (y a quién le toca)

| Tarea | Quién | Detalle |
|---|---|---|
| Registrar la URL del dataset en la pestaña PC3 del cuadro de temas | Arbués | Requiere la sesión de Google; sin eso el trabajo se puede anular |
| Subir el zip con su nombre al aula virtual | cada uno | `entrega/LC3_<integrante>.zip`; el contenido es idéntico |
| Ensayar la exposición cronometrada | ambos | ruta breve de 19 láminas = 570 s objetivo (9:30) en `presentacion/guion/01-recorrido.tex`; el guion largo es para preguntas |
| Probar `presentacion/demo/compose.yaml` | ambos | nunca se ejecutó; la imagen `spark:3.3.2` no está descargada en esta laptop |

## Pendientes técnicos (menores, opcionales)

- Si se quiere incluir 2025: el portal publica un recurso 2019–2025 de 10 columnas que **suma
  exactamente lo mismo** por año en el tramo 2019–2024. No apilar los dos: duplica todos los casos.
  Regla si se usa: base 2019–2024 (13 columnas) + solo las filas de 2025.
- El ubigeo `150121` viene con dos nombres (PUEBLO LIBRE y MAGDALENA VIEJA). La dimensión
  geográfica lo colapsa a una fila; si se toca el ETL, mantener esa regla.

## Estructura

```
code/comun/esquema.scala   lectura, limpieza, dimensiones y vistas temporales
code/rdd/*.scala           consultas 3a, 3b, 3c x3 (mapReduce)
code/sql/*.scala           consultas 5a-5g (DataFrame, vistas temporales, Excel)
sql/01_ddl.sql             modelo estrella
sql/02_etl.sql             ETL completo
sql/dump-denuncias.sql     dump de la base cargada
evidencias/salidas/*.txt   salida real de cada consulta
evidencias/pg-*.png        capturas de la base funcionando
informe/                   informe.tex + informe.pdf (20 páginas)
presentacion/              presentacion.pdf (37 láminas; ER del informe en la 17, compilado) + guion-exposicion.pdf (33 páginas, reparto por bloques)
presentacion/DEMO.md       qué abrir, qué ejecutar y qué decir en la demo de 3 min
docs/mapa-consultas.md     las 25 preguntas y por qué no se repiten
```
