# Demo de LC3: qué abrir, qué ejecutar y qué decir

La ruta recomendada dura aproximadamente tres minutos **con Spark ya abierto y los datos cargados**. La demo es adicional a la exposición de diez minutos; úsala si el docente la solicita. El guion hablado está al final de `guion-exposicion.tex`.

Los comandos de terminal de esta guía parten de esta carpeta:

```bash
cd /home/sergi/Documents/semesters/nine/bigdata/labs/lc3-cc531-desapariciones
```

Si usan otra computadora, cambia solamente este `cd` por la ubicación real de la carpeta del proyecto.

## 1. Preparar antes de entrar a clase

Ten abiertas tres ventanas: presentación, editor con los scripts y terminal. Deja también disponible la carpeta `excel/`.

En la terminal configura el usuario del contenedor y una contraseña para la base descartable de ensayo. El valor del ejemplo es exclusivamente para esta demo local:

```bash
export LC3_UID="$(id -u)"
export LC3_GID="$(id -g)"
export LC3_PG_PASSWORD='lc3-demo-local'
docker compose -f presentacion/demo/compose.yaml config --quiet
```

Estos `export` se repiten en cada terminal nueva que utilice la configuración. `config --quiet` valida la configuración; no ejecuta consultas.

La configuración usa la misma imagen Spark 3.3.2 que el entorno Docker del proyecto. Monta la carpeta real en `/home/arbues/macrodatos/PC3` **dentro del contenedor**, que es la ruta que ya usan los scripts. El proyecto se monta en modo de solo lectura. No hay que crear esa ruta en el equipo ni reemplazar las rutas de los `.scala`.

Antes de clase, con conexión disponible, puedes descargar las imágenes necesarias:

```bash
docker compose -f presentacion/demo/compose.yaml pull spark
docker compose -f presentacion/demo/compose.yaml --profile postgres pull postgres
```

El segundo comando solo hace falta si vas a mostrar PostgreSQL. No esperes a la exposición para descargar imágenes.

Abre Spark:

```bash
docker compose -f presentacion/demo/compose.yaml run --rm --service-ports spark
```

Cuando aparezca `scala>`, ya estás dentro de Spark. **Los siguientes comandos son de Scala, no de Bash**:

```scala
spark.version
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala
```

La versión esperada es `3.3.2`. Al terminar la carga, los controles del esquema deben mostrar:

| Control | Resultado esperado según las evidencias |
|---|---:|
| Registros del DataFrame | 93 831 |
| Registros del RDD | 93 831 |
| Ubigeos del catálogo | 1 506 |

Ensaya una vez los comandos de las secciones siguientes y deja la consola abierta. El arranque y la carga inicial quedan fuera de la demo de tres minutos. Si no aparece la consola o hay errores de Scala, resuélvelos antes de presentar.

## 2. Demo principal: una pregunta RDD y una pregunta SQL

### Paso A: comprobar entrada y medida — unos 30 segundos

En `scala>`:

```scala
denuncias.count()
denuncias.agg(sum("cantidad").as("personas")).show(false)
```

Esperado: `93831` y una tabla con `121427` personas. No copies estos números sobre una salida diferente: si difiere, revisa la carga.

Di:

> Primero verifico la entrada. El conteo da las filas y la suma de CANTIDAD da las personas contabilizadas. Son medidas distintas porque cada fila representa un cruce agregado.

### Paso B: consulta 3c.1 — unos 60 segundos

Deja visible en el editor `code/rdd/03c_1_menores_por_departamento.scala`. Señala `map`, `reduceByKey`, `join` y el control nacional. En la consola:

```scala
:load /home/arbues/macrodatos/PC3/code/rdd/03c_1_menores_por_departamento.scala
```

El script vuelve a cargar el esquema por su cuenta. Es parte del archivo original, no un segundo dataset que se apile al primero.

Busca estas filas y el control final:

| Salida | Total | Menores | Porcentaje |
|---|---:|---:|---:|
| Madre de Dios | 655 | 473 | 72.21 |
| Lima Metropolitana | 39 490 | 19 751 | 50.02 |
| Control nacional | 121 427 | 70 178 | 57.79 |

Di:

> Esta consulta responde qué porcentaje corresponde a menores en cada departamento. Una agregación calcula el total y otra calcula menores. Después se unen para obtener el porcentaje. Las reducciones del control nacional permiten contrastar los parciales con el total del país.

Si pide ver los dos agregados antes de la división:

```scala
mr1Total.take(5).foreach(println)
mr2Menores.take(5).foreach(println)
```

Esas cinco filas no tienen un orden de ranking garantizado: `take(5)` sirve para inspeccionar la estructura de los pares. La tabla ordenada ya la imprime la consulta.

### Paso C: consulta 5e.2 — unos 60 segundos

En el editor abre `code/sql/05e_2_grupoetario_sexo.scala`. Señala las dos condiciones `JOIN`. En la consola:

```scala
:load /home/arbues/macrodatos/PC3/code/sql/05e_2_grupoetario_sexo.scala
consulta.show(5, false)
```

La primera fila esperada es `ADOLESCENTE`, `F`, `51021` personas y `42.02` en `pct_nacional`. La consulta devuelve 18 combinaciones.

Di:

> Ahora usamos vistas temporales y Spark SQL. Los JOIN relacionan las denuncias con edad y sexo. El agrupamiento obtiene el perfil más numeroso y el porcentaje usa el total nacional como denominador. Así se reproduce la respuesta de la presentación.

Si pregunta cómo se registraron las vistas, abre la sección 4 de `code/comun/esquema.scala`, donde aparecen las llamadas `createOrReplaceTempView`. Para listar las vistas en esa sesión:

```scala
spark.sql("SHOW TABLES").show(false)
```

Opcional, si solicita el plan:

```scala
consulta.explain(true)
```

Muestra las operaciones de unión y agregación del plan. El plan exacto puede cambiar según configuración y optimización. No lo presentes como prueba de tres MapReduce consecutivos.

### Paso D: cerrar — unos 30 segundos

Di:

> Hemos mostrado la entrada, una consulta RDD y una consulta sobre vistas temporales. Las cifras de la presentación se pueden relacionar con el código y la salida. También tenemos la base y los Excel para revisar el almacenamiento y la entrega.

Si pide otras consultas, utiliza el mapa de la presentación para elegir su archivo. En Scala puedes cargarlo con `:load` y la misma ruta base. Las consultas 5a–5f leen el proyecto sin escribir los Excel.

## 3. Si pide ver Spark trabajando

Con esa consola todavía abierta, entra a [la interfaz local de Spark](http://localhost:4041). La configuración publica el puerto 4040 del contenedor en el puerto 4041 del equipo.

Muestra las pestañas de trabajos y etapas disponibles después de ejecutar una consulta. Relaciona la actividad con la consulta recién lanzada. Esta demo usa `local[2]`: dos hilos en un equipo. El número de trabajos y etapas observado en la interfaz no tiene por qué coincidir con los rótulos MR-A, MR-B y MR-C del diagrama lógico.

Si el puerto 4041 está ocupado, antes de presentar cambia solo el puerto del equipo en `demo/compose.yaml`, por ejemplo `127.0.0.1:4042:4040`, y utiliza `http://localhost:4042`.

## 4. Si pide ver PostgreSQL

Prepara esta parte **antes** de clase. La configuración crea una base separada llamada `denuncias_demo`, dentro de un contenedor PostgreSQL 16. No utiliza ni modifica una instalación PostgreSQL del equipo. Su contenido está en memoria y se pierde al detener el contenedor.

En otra terminal, desde la raíz del laboratorio y con los tres `export` de preparación:

```bash
docker compose -f presentacion/demo/compose.yaml --profile postgres up -d --wait postgres
docker compose -f presentacion/demo/compose.yaml --profile postgres exec -T postgres \
  psql -v ON_ERROR_STOP=1 -U macrodatos -d denuncias_demo < sql/dump-denuncias.sql
```

La importación se hace **una vez sobre la base vacía**. Si ya importaste el dump, pasa directamente a abrir `psql`; repetirlo sobre las mismas tablas produce errores de objetos existentes.

Abre la consola de esa copia:

```bash
docker compose -f presentacion/demo/compose.yaml --profile postgres exec postgres \
  psql -U macrodatos -d denuncias_demo
```

Dentro de `psql`, no en Scala:

```sql
\pset pager off
SELECT version();
\dt
\d hecho_desaparicion
SELECT COUNT(*) AS registros, SUM(cantidad) AS personas
FROM hecho_desaparicion;
SELECT COUNT(*) AS ubigeos FROM dim_geografia;
```

Esperado: PostgreSQL de la serie 16, 93 831 registros, 121 427 personas y 1 506 ubigeos. La revisión exacta 16.x de la imagen descargada se consulta con `version()`.

Después puedes relacionar el modelo con el perfil de la presentación:

```sql
SELECT e.grupo_etario, h.sexo, SUM(h.cantidad) AS personas
FROM hecho_desaparicion h
JOIN dim_rango_edad e ON h.rango_edad = e.rango_edad
GROUP BY e.grupo_etario, h.sexo
ORDER BY personas DESC
LIMIT 5;
```

La primera combinación debe ser ADOLESCENTE y F, con 51 021 personas.

Di:

> Esta es una copia restaurada del dump entregado. Aquí se ve la tabla de hechos, sus relaciones y el control de filas y personas. El JOIN con la dimensión de edad reproduce el perfil principal.

Esta parte demuestra **restauración y consulta de la base**. Para explicar el ETL original, abre `sql/01_ddl.sql` y `sql/02_etl.sql` en el editor y señala arquitectura, `\copy`, transformación e inserción. No describas la restauración del dump como una ejecución en vivo del ETL. El DDL contiene `DROP TABLE`; no hace falta ejecutarlo durante la demostración.

Para salir de `psql`:

```sql
\q
```

## 5. Si pide ver los Excel

Abre desde el explorador de archivos uno de estos libros existentes:

- `excel/05e_2_grupoetario_sexo.xlsx`: perfil por edad y sexo, 18 filas de datos.
- `excel/05f_1_distritos_mujeres2024.xlsx`: diez distritos del subconjunto femenino de 2024.

Di:

> Este libro es un resultado generado previamente con Spark. El script de exportación especifica el proveedor spark-excel, la hoja, la cabecera y la ruta. Aquí podemos revisar las categorías y totales entregados.

Para mostrar el exportador, abre `code/sql/05g_excel.scala`, especialmente `guardarExcel`. El libro 5e.2 **no contiene** la columna `pct_nacional` de la consulta individual: conserva categorías, total de personas, registros y promedio.

La demo Docker monta los archivos como solo lectura y no trae el JAR de spark-excel en `lib/`. Por ello esta guía muestra los libros ya generados y el código escritor; no incluye una regeneración en vivo. Regenerarlos necesita preparar primero el JAR compatible y un destino escribible. La demo RDD y SQL no necesita ese JAR.

## 6. Plan alternativo si falla el entorno

Desde Bash, muestra una salida guardada sin iniciar servicios:

```bash
less -S evidencias/salidas/03c_1_menores_por_departamento.txt
less -S evidencias/salidas/05e_2_grupoetario_sexo.txt
less -S evidencias/pg-conteos.txt
```

Pulsa `q` para salir de cada archivo. Abre también el `.scala` correspondiente y la captura `evidencias/pg-conteos.png`.

Di:

> El entorno no está disponible en este momento. Esta es la salida guardada de una ejecución anterior. Voy a relacionarla con el código para mostrar cómo se obtiene la respuesta.

No presentes `less`, una captura ni los Excel existentes como una ejecución nueva.

## 7. Problemas habituales y salida concreta

| Mensaje o situación | Qué revisar |
|---|---|
| Variable `LC3_UID`, `LC3_GID` o `LC3_PG_PASSWORD` ausente | Repetir los tres `export` en esa terminal. |
| Ruta `/home/arbues/...` inexistente en el equipo | Esa ruta es interna al contenedor. Usar esta configuración Docker en vez del ejecutor original del equipo. |
| `not found: value denuncias` o `hechos` | El esquema no terminó de cargar. Revisar el primer error y cargar `code/comun/esquema.scala` dentro de Spark. |
| `Table or view not found` | Cargar el esquema en la misma sesión, ya que las vistas son temporales. |
| Error relacionado con `ROUND` o generación de código | Usar el arranque de esta configuración y conservar los `CAST(... AS DOUBLE)` originales. |
| Puerto 4041 ocupado | Cambiar el puerto del equipo como indica la sección de interfaz Spark. |
| PostgreSQL tarda en estar listo | Revisar `docker compose -f presentacion/demo/compose.yaml --profile postgres logs postgres` antes de restaurar. |
| El dump indica tabla existente | La importación ya se hizo; consultar la base en lugar de volver a restaurar. |
| `\restrict` no reconocido al restaurar | Usar el cliente `psql` del contenedor PostgreSQL 16 actualizado, como en esta guía; no un cliente antiguo del equipo. |

No uses `bin/correr_todas.sh` durante la exposición: vuelve a ejecutar todas las consultas y modifica los archivos de salida. Para esta demo basta con las dos consultas seleccionadas.

## 8. Al terminar

En Spark:

```scala
:quit
```

Si abriste PostgreSQL, al terminar de usar esa copia:

```bash
docker compose -f presentacion/demo/compose.yaml --profile postgres down
```

Este cierre descarta la base temporal de la demo; no borra el CSV, el dump, las evidencias ni los Excel del proyecto.

## Alcance de esta preparación

Se revisaron las rutas, el esquema, los scripts, las cifras guardadas y la sintaxis de configuración. **No se arrancaron los servicios ni se ejecutaron las consultas durante esta edición.** Haz un ensayo completo antes de clase para comprobar resultados y duración reales. Tampoco se recompilaron los PDF del Beamer ni del guion.
