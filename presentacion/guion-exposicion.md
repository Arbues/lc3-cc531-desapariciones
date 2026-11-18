# Guion literal LC3: versión explicada

Arbués Enrique Pérez Villanueva y Sergio Pezo · noviembre de 2026.

Esta copia permite leer el guion actualizado sin compilar LaTeX. La fuente editable es [guion-exposicion.tex](guion-exposicion.tex), que incluye los archivos de [guion/detallado](guion/detallado/).

**Reparto con un solo relevo:** Arbués presenta las láminas 1–10 (objetivo de la ruta breve: 4:40). Sergio continúa con las láminas 11–19 (4:50), incluido el cierre, y presenta los anexos si se abren después. Ambos pueden responder preguntas. Se conserva el orden de las diapositivas.

**Cómo usarlo:** leer en voz alta únicamente los bloques «Texto para decir». Las indicaciones orientan el puntero y las pausas. Las transiciones entre láminas y expositores están incluidas.

**Duración:** la versión explicada tiene unas 11 000 palabras habladas, aproximadamente 75–95 minutos sin pausas ni demo. Sirve para estudiar y defender el trabajo. Para diez minutos, utilizar la ruta breve al final y ensayar con cronómetro; no leer ambas versiones consecutivamente.

**PDF:** el PDF de 33 páginas incorporado desde el remoto contiene el guion ampliado, pero conserva el reparto anterior de expositores. El nuevo reparto por bloques está en esta copia y en las fuentes TEX; no se recompiló este ajuste.

**Demo:** [DEMO.md](DEMO.md) contiene los comandos y salidas esperadas. La configuración Docker está preparada; no se ejecutó durante esta revisión.

---

## Lámina 1: Qué estudiamos y qué vamos a responder

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Señalar el título, el periodo y las dos cantidades inferiores.

**Texto para decir:**

Buenos días. Somos Arbués Enrique Pérez Villanueva y Sergio Pezo. Presentamos el análisis de denuncias por desaparición de personas en el Perú, entre 2019 y 2024. Nuestra exposición sigue una misma idea: primero formulamos una pregunta que los datos pueden responder; luego mostramos las operaciones utilizadas y, finalmente, contrastamos la respuesta con la salida del programa.

**Texto para decir:**

En la portada aparecen noventa y tres mil ochocientos treinta y un registros agregados y ciento veintiún mil cuatrocientas veintisiete personas contabilizadas. Son cantidades diferentes: un registro puede contener un conteo de varias personas. Por eso distinguimos entre contar filas y sumar CANTIDAD. Las veinticinco preguntas se trabajan con Scala, el lenguaje del código; Spark, el motor que ejecuta las operaciones; y PostgreSQL, que conserva una copia estructurada en una base relacional. A continuación ubicaremos cada parte dentro de la práctica.

## Lámina 2: Cómo se relaciona cada pregunta con la práctica

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Recorrer todas las filas del mapa. Los números de la derecha son enlaces a fichas, no resultados numéricos.

**Texto para decir:**

Este cuadro permite reconocer qué parte de la práctica resuelve cada grupo. Los ítems uno y dos presentan la fuente, los campos y las reglas de lectura. Los ítems tres a y tres b trabajan extremos y estadísticos mediante Scala y RDD. RDD significa colección distribuida resiliente: una colección que Spark divide en particiones y cuyas transformaciones puede reconstruir a partir de su origen.

**Texto para decir:**

El punto tres c solicita tres consultas con resultados decimales y al menos tres MapReduce por consulta. En sus fichas identificaremos las agregaciones principales, las reducciones adicionales y el cálculo decimal. Así podremos señalar las operaciones que existen en los scripts y distinguirlas de una simple división. El punto cuatro corresponde al almacenamiento en PostgreSQL y al proceso ETL: extracción, transformación y carga.

**Texto para decir:**

Después viene el punto cinco. En cinco a agrupamos y filtramos por dos o más columnas; en cinco b calculamos un promedio y un orden descendente. En cinco c mostramos cuatro formas de unir tablas: INNER, LEFT, RIGHT y FULL OUTER. En cinco d utilizamos funciones de Spark, disponibles mediante la importación functions punto guion bajo. En cinco e hacemos tres consultas con JOIN sobre vistas temporales; en cinco f ordenamos resultados combinando filtros; y en cinco g exportamos esas seis consultas a Excel.

**Texto para decir:**

Los puntos seis y siete motivan la forma de presentar la solución: cada ficha contiene pregunta, entrada, operaciones, código y respuesta. Los enlaces permiten abrir las veinticinco consultas. El recorrido principal selecciona ejemplos; las demás también tienen su explicación en anexos. Antes de leer resultados, necesitamos precisar qué representa el dataset.

## Lámina 3: Fuente, alcance y unidad de análisis

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Leer ambos bloques, la fórmula y el recuadro inferior.

**Texto para decir:**

En el primer bloque leemos «Fuente y alcance». Los datos corresponden al Ministerio del Interior, abreviado MININTER, y se publican en la Plataforma Nacional de Datos Abiertos, o PNDA, dentro del rubro Seguridad y Justicia. Utilizamos el archivo local de enero de 2019 a diciembre de 2024: setenta y dos meses, noventa y tres mil ochocientas treinta y una filas, trece campos y mil quinientos seis ubigeos observados. Ubigeo es el código que identifica una ubicación administrativa. Las veintiséis unidades departamentales incluyen Lima Metropolitana y Región Lima por separado.

**Texto para decir:**

El segundo bloque dice «Unidad de análisis». Una fila contiene un conteo para una combinación de año, mes, territorio y perfil. Dos filas del mismo distrito y mes pueden diferenciarse por sexo o rango de edad. No estamos leyendo una lista de personas con identificador individual. Por eso la fórmula dice: personas contabilizadas igual a la suma de CANTIDAD. Esa suma produce ciento veintiún mil cuatrocientas veintisiete. En cambio, count asterisco cuenta las filas: noventa y tres mil ochocientas treinta y una.

**Texto para decir:**

Finalmente, el recuadro «Qué podemos interpretar» fija el alcance de las respuestas. Podemos describir cómo se distribuyen y cambian los conteos reportados. No podemos deduplicar personas, porque no hay identificadores individuales; tampoco establecer que desaparecieron juntas, explicar causas o calcular riesgo poblacional. Para una tasa de riesgo necesitaríamos, además, la población expuesta de cada grupo. Esta distinción acompañará todas las consultas. Ahora revisaremos cómo se leen las columnas.

## Lámina 4: Los trece campos y las reglas de lectura

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Seguir todas las filas de la tabla y terminar en Limpieza y Derivadas.

**Texto para decir:**

Para transformar esas preguntas en operaciones, revisamos los campos disponibles. AÑO y MES son enteros, números sin parte decimal; permiten agrupar por año, mes o trimestre. Cuando necesitamos una fecha, añadimos el día uno como convención: no es el día real de una desaparición. UBIGEO HECHO es texto, aunque contenga dígitos, para conservar los ceros iniciales de su código de seis posiciones. DPTO HECHO, PROV HECHO y DIST HECHO identifican departamento, provincia y distrito del hecho reportado.

**Texto para decir:**

SEXO contiene F, M o NO ESPECIFICA. RANGO EDAD permite reconocer menores de dieciocho años mediante los intervalos de cero a cinco, seis a once y doce a diecisiete. EST CIVIL describe el estado civil; NACIONALIDAD permite separar las nacionalidades declaradas. Extranjero significa nacionalidad distinta de PERÚ y distinta de NO ESPECIFICA: un dato desconocido no se clasifica automáticamente como extranjero. NIVEL EDUCATIVO y OCUPACION describen otras características del perfil. Finalmente, CANTIDAD es un entero y una medida aditiva: podemos sumar sus valores. En la evidencia varía entre uno y dieciocho.

**Texto para decir:**

La limpieza conserva los vacíos categóricos bajo la etiqueta NO ESPECIFICA. No imputamos esos atributos; imputar significaría asignarles un valor estimado. También generamos dos columnas derivadas. Región utiliza la clasificación por departamento definida en el código; no mide ruralidad. Periodo separa 2019, 2020 a 2021 y 2022 a 2024, con las etiquetas pre pandemia, pandemia y post pandemia. Son agrupaciones temporales de uno, dos y tres años.

**Texto para decir:**

Al pie aparecen los nombres normalizados de Scala: anio, mes, ubigeo, departamento, provincia, distrito, sexo, rango edad, estado civil, nacionalidad, nivel educativo, ocupacion y cantidad. Los nombres con guiones bajos representan los mismos campos que acabamos de definir. El archivo esquema punto Scala centraliza su lectura. Veamos cómo ese mismo origen alimenta distintas formas de consulta.

## Lámina 5: Leer todas las ramas del flujo

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Comenzar en CSV; seguir RDD, DataFrame y PostgreSQL. Las dos flechas que salen de DataFrame llegan a API y SQL.

**Texto para decir:**

El cuadro inicial dice «CSV MININTER: trece campos, noventa y tres mil ochocientas treinta y una filas». CSV significa archivo de valores separados por comas. Desde él se abren tres ramas. Las flechas indican de dónde obtiene sus datos cada representación; no son pasos que deban ejecutarse todos en una sola cadena.

**Texto para decir:**

La rama superior llega a «RDD: colección distribuida; parseo en Lc3 punto parsear». Parsear significa convertir una línea de texto en campos utilizables, como año entero y cantidad entera. La flecha siguiente conduce a «tres a a tres c: Scala y MapReduce; map, reduceByKey, reduce». Map transforma cada elemento; reduceByKey combina valores que comparten una clave; reduce combina todos los elementos de una colección en un resultado. MapReduce resume este patrón de transformar y combinar.

**Texto para decir:**

En el centro leemos «DataFrame: tabla con esquema; limpieza y dimensiones». El esquema define nombres y tipos de columnas. Las dimensiones son catálogos descriptivos, como el que relaciona ubigeo y ubicación. La flecha horizontal llega a «cinco a a cinco d: API de DataFrame; groupBy, filter, join, funciones». API significa interfaz de programación: los métodos utilizados desde Scala. GroupBy agrupa, filter selecciona y join une por una condición.

**Texto para decir:**

Desde DataFrame sale otra flecha hacia «cinco e a cinco f: vistas y spark punto sql; registrar vistas y consultar en SQL; cinco g: escribir resultados en Excel». Una vista temporal da un nombre consultable a un DataFrame dentro de la sesión de Spark. SQL es el lenguaje con el que expresamos qué datos queremos seleccionar, agrupar o unir. Esta rama utiliza Spark SQL; no consulta PostgreSQL.

**Texto para decir:**

La rama inferior del CSV llega a «PostgreSQL dieciséis: extracción, transformación y carga, ETL». Esa flecha representa una carga independiente a la base. No hay una flecha desde PostgreSQL hacia las consultas Spark. Finalmente, el bloque inferior recuerda cómo leer los diagramas siguientes: Map crea claves y valores; Reduce los combina; join reúne agregados por clave y un map posterior calcula el porcentaje. A continuación veremos ese patrón con máximos y mínimos.

## Lámina 6: 3a: de una fila a los extremos por sexo

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Recorrer los cuatro cuadros de izquierda a derecha; después, relacionar cada fragmento de código con su cuadro.

**Texto para decir:**

Esta pregunta nace de dos campos: SEXO permite formar categorías y CANTIDAD permite comparar sus conteos. El punto tres a pide agrupar una categoría y obtener los valores mayores y menores de un campo numérico. Por eso preguntamos: ¿cuál es el mayor y el menor conteo por registro para cada sexo, y dónde se observa un máximo? Buscamos extremos de las filas agregadas.

**Texto para decir:**

El primer cuadro dice «Entrada: sexo y cantidad». Partimos de las noventa y tres mil ochocientas treinta y una filas; conservamos también territorio, año y mes para localizar extremos. La primera flecha conduce a «uno, Map: sexo, par c y c». Aquí c significa cantidad. El Map emite una clave, el sexo, y una tupla, es decir, un conjunto ordenado de valores. Esa tupla repite la cantidad porque inicia dos cálculos: máximo y mínimo.

**Texto para decir:**

La siguiente flecha llega a «dos, Reduce: máximo y mínimo por sexo». ReduceByKey combina solamente valores de la misma clave. En el código, h representa una fila y la flecha escrita como igual y mayor que indica qué transformación se aplica a esa fila. En la reducción, a y b son dos tuplas que se están combinando; a punto guion bajo uno y b punto guion bajo uno acceden a sus primeras posiciones, y guion bajo dos, a las segundas. Math punto max conserva el mayor de los primeros valores y math punto min conserva el menor de los segundos. Al repetir la combinación obtenemos un par de extremos por sexo. No sumamos cantidades: elegimos los extremos.

**Texto para decir:**

La última flecha lleva a «Salida: extremos y ubicación». Las otras reducciones del script conservan una tupla más amplia con cantidad, lugar y fecha. La tabla muestra F: máximo dieciocho y mínimo uno; M: máximo catorce y mínimo uno; y NO ESPECIFICA: máximo tres y mínimo uno. Un máximo F está en Villa María del Triunfo, noviembre de 2019; uno M está en Santiago, Cusco, octubre de 2019. Si hay empate, se conserva un registro extremo; no enumeramos todos los empatados. Ya sabemos los límites: ahora explicaremos qué ocurre entre ellos.

## Lámina 7: 3b: cómo se construyen los estadísticos

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Leer los cuatro cuadros y las tres filas Map/Reduce; señalar cada símbolo de las fórmulas.

**Texto para decir:**

Conocer un máximo no nos dice cuál es el valor típico. Del punto tres b y de la columna CANTIDAD nace esta pregunta: ¿cuál es el promedio, la mediana, la desviación, el máximo y el mínimo por registro? La entrada sigue siendo toda la tabla, sin filtrar personas ni departamentos.

**Texto para decir:**

El primer cuadro dice «Entrada: columna CANTIDAD». La flecha llega a «uno, Map: par c y uno, c al cuadrado y c». Es un resumen de varias ramas del código. En la primera, cada fila emite su cantidad junto con un uno; ese uno contará la fila. En la segunda emitimos la cantidad al cuadrado. En las ramas de extremos emitimos directamente la cantidad.

**Texto para decir:**

La segunda flecha conduce a «dos, Reduce: sumas, conteo y extremos». La tabla inferior precisa qué combinamos. El par c y uno se reduce sumando componente por componente: S es la suma de cantidades y n es la suma de unos, o número de registros. El símbolo sigma, parecido a una E mayúscula, indica sumatoria. Los cuadrados se suman para obtener Q. Finalmente, las cantidades se combinan con máximo y con mínimo en reducciones separadas. En el fragmento, h representa una fila; toDouble convierte la cantidad a número con decimales y uno L indica un entero de tipo largo.

**Texto para decir:**

La última flecha llega a «Salida: estadísticos». La media, escrita equis barra, es S dividido entre n. La desviación poblacional, escrita sigma, es la raíz cuadrada de Q dividido entre n menos la media al cuadrado. Resume la variación de los conteos alrededor de la media. La mediana requiere otro procedimiento: ordenar las cantidades, asignarles índices y tomar el centro. Con un número impar de filas, ese centro corresponde a una sola posición. No llamamos reducción al ordenamiento.

**Texto para decir:**

Leamos la salida: noventa y tres mil ochocientas treinta y una filas; suma de ciento veintiún mil cuatrocientas veintisiete; promedio de uno coma dos nueve cuatro uno; mediana uno; desviación poblacional cero coma ocho nueve cero seis; mínimo uno y máximo dieciocho. La mediana uno y la media superior a uno muestran que los conteos altos elevan el promedio. Media y promedio son sinónimos; mediana es otro estadístico. Con esta base pasaremos de cantidades a proporciones.

## Lámina 8: 3c.1: menores, denominador territorial y control nacional

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Leer A y su flecha; B y su flecha; C y su flecha. Después señalar el cálculo que une A con B y la tabla.

**Texto para decir:**

El punto tres c solicita resultados decimales. Aquí usamos edad, departamento y cantidad para preguntar: ¿qué porcentaje del total de cada departamento corresponde a menores de dieciocho años y cuál es el control nacional? El porcentaje permite comparar composición, porque un departamento pequeño puede tener menos menores en número absoluto y una proporción mayor. En el diagrama, d significa departamento y c, cantidad. Menores incluye cero a cinco, seis a once y doce a diecisiete años.

**Texto para decir:**

En la primera fila leemos «uno, Map, A: total; par d y c; todas las filas». Cada fila emite su departamento como clave y su cantidad como valor. La flecha conduce a «dos, Reduce, A: total; reduceByKey, suma; par d y T sub d, total por departamento». El guion bajo más guion bajo del código significa sumar dos valores que se están combinando. T sub d es el total de personas contabilizadas para ese departamento.

**Texto para decir:**

La segunda fila dice «uno, Map, B: menores; par d y c; filtrar menores antes del Map». Primero comprobamos el rango de edad; solo las filas de menores emiten ese par. Su flecha llega a «dos, Reduce, B: menores; reduceByKey, suma; par d y M sub d, menores por departamento». M sub d es el numerador. A y B parten del mismo RDD con filtros diferentes: la segunda fila no recibe como entrada el resultado de la primera.

**Texto para decir:**

La tercera fila dice «uno, Proyección, C: control; values de A y B; tomar los totales parciales». Values extrae los valores y deja de lado las claves departamentales. Su flecha conduce a «dos, Reduce, C: control; reduce, suma; total y menores nacionales». Se reduce la colección de totales de A y, por separado, la de menores de B. Este control sí utiliza resultados previos. Se llama proyección porque el script usa values; no mostramos un Map explícito que no existe en esa línea.

**Texto para decir:**

Después de reducir, join une A y B por departamento. Para cada clave obtenemos total y menores; entonces un map calcula p sub d igual a cien por M sub d dividido entre T sub d. P es porcentaje, M es numerador y T es denominador. La multiplicación por cien lo expresa por cada cien personas contabilizadas; el cálculo usa decimales y se redondea a dos posiciones. El fragmento inferior corresponde al Map y Reduce de A.

**Texto para decir:**

En la salida, Madre de Dios tiene cuatrocientos setenta y tres menores de seiscientas cincuenta y cinco personas: setenta y dos coma veintiuno por ciento. Ucayali tiene mil quinientos cuatro de dos mil ciento veintiséis: setenta coma setenta y cuatro por ciento. Lima Metropolitana registra diecinueve mil setecientos cincuenta y uno de treinta y nueve mil cuatrocientos noventa: cincuenta coma cero dos por ciento. El control nacional divide setenta mil ciento setenta y ocho entre ciento veintiún mil cuatrocientas veintisiete y obtiene cincuenta y siete coma setenta y nueve por ciento. El denominador incluye edades no especificadas; no es la población del departamento. Aplicaremos ahora el mismo principio a nacionalidad y año.

## Lámina 9: 3c.2: extranjeros por año y apoyo por nacionalidad

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Seguir horizontalmente los tres pares; distinguir la clave año de A/B y la clave nacionalidad de C.

**Texto para decir:**

La nacionalidad y el año permiten plantear otra consulta decimal: ¿qué porcentaje anual corresponde a nacionalidades extranjeras y cuáles aportan más al acumulado? Queremos separar el tamaño del conteo de su peso relativo. En la entrada, a significa año; n, nacionalidad; y c, cantidad. Excluimos PERÚ y NO ESPECIFICA para definir el subconjunto extranjero.

**Texto para decir:**

La fila A dice «uno, Map, A: total; par a y c; todas las filas». Emitimos año y cantidad. La flecha lleva a «dos, Reduce, A: total; reduceByKey, suma; par a y T sub a, total anual». Así obtenemos el denominador de cada año. En la fila B leemos «uno, Map, B: extranjeros; par a y c; solo extranjeros». Antes de emitir filtramos la nacionalidad. Su flecha conduce a «dos, Reduce, B: extranjeros; reduceByKey, suma; par a y E sub a, extranjeros por año». E sub a es el numerador anual.

**Texto para decir:**

En la fila C leemos «uno, Map, C: apoyo; par n y c; solo extranjeros». Ahora cambia la clave: agrupamos por nacionalidad, no por año. Su flecha llega a «dos, Reduce, C: apoyo; reduceByKey, suma; total por nacionalidad». Esa rama permite describir qué nacionalidades reúnen más personas en todo el periodo. No es una tercera reducción consecutiva del porcentaje anual. El fragmento de código visible muestra la construcción del total anual de A.

**Texto para decir:**

Una vez reducidos A y B, los unimos por año y calculamos p sub a igual a cien por E sub a dividido entre T sub a. La salida de 2019 muestra cuatrocientas treinta y cuatro personas extranjeras de veintiséis mil trescientas treinta y una: uno coma sesenta y cinco por ciento. En 2023 son quinientas veinticinco de dieciocho mil ciento noventa y dos: dos coma ochenta y nueve por ciento. En 2024 son quinientas veintiocho de diecinueve mil cuatrocientas veinte: dos coma setenta y dos por ciento.

**Texto para decir:**

El apoyo C registra dos mil ciento cincuenta y una personas de nacionalidad venezolana y ciento veinticuatro de nacionalidad colombiana. Finalmente, dos coma setenta y dos menos dos coma ochenta y nueve da menos cero coma diecisiete puntos porcentuales. Aunque el conteo extranjero crece de quinientos veinticinco a quinientos veintiocho, su participación baja porque también cambia el total anual. Puntos porcentuales es la resta entre porcentajes; no una tasa de crecimiento del conteo. A continuación trasladaremos esta lógica a los meses.

## Lámina 10: 3c.3: participación mensual, referencia e índice

**Expone:** Arbués. Versión explicada.

> **Indicación, no leer:** Leer A, B y C con sus flechas; después las dos fórmulas. Aclarar que la agrupación por mes reúne los seis años.

**Texto para decir:**

A partir de MES y CANTIDAD preguntamos: ¿qué meses superan una distribución uniforme y cómo se concentra el total por trimestre? Esta consulta responde al punto tres c con porcentajes e índices. Reunimos los seis años: enero significa todos los eneros del periodo. La referencia uniforme divide cien entre doce y obtiene ocho coma treinta y tres por ciento aproximadamente para cada mes. Es una referencia descriptiva, no una prueba de estacionalidad.

**Texto para decir:**

La fila A dice «uno, Map, A: mes; par m y c; clave mes». M representa el número del mes y c, cantidad. La flecha llega a «dos, Reduce, A: mes; reduceByKey, suma; par m y T sub m, total mensual». Combinamos todas las cantidades del mismo mes. El fragmento de Scala muestra precisamente esa emisión y esa suma por clave.

**Texto para decir:**

En B leemos «uno, Map, B: nacional; par TOTAL y c; la misma clave para todas las filas». TOTAL es una etiqueta constante. Como todas las filas comparten esa clave, la flecha lleva a «dos, Reduce, B: nacional; reduceByKey, suma; T igual a ciento veintiún mil cuatrocientas veintisiete». Obtenemos un único denominador para el periodo completo.

**Texto para decir:**

La fila C dice «uno, Map, C: apoyo; par t y c; calcular trimestre desde el mes». T minúscula representa trimestre: enero a marzo es el primero, abril a junio el segundo, julio a septiembre el tercero y octubre a diciembre el cuarto. Su flecha llega a «dos, Reduce, C: apoyo; reduceByKey, suma; par t y S sub t, total trimestral». Esta agrupación reúne el mismo trimestre de los seis años; no produce los veinticuatro trimestres cronológicos de otra consulta.

**Texto para decir:**

Después calculamos participación p sub m igual a cien por T sub m dividido entre T. El índice I sub m es doce por T sub m dividido entre T, equivalente a dividir la participación observada entre la uniforme. Un índice uno coincide con la referencia; mayor que uno la supera y menor que uno queda por debajo. No multiplicamos el porcentaje por doce sin ajustar su escala.

**Texto para decir:**

La salida muestra diciembre: once mil quinientas veintidós personas, nueve coma cuarenta y nueve por ciento e índice uno coma catorce. Febrero: once mil ciento dieciocho, nueve coma dieciséis por ciento e índice uno coma diez. Abril: ocho mil ochocientas cinco, siete coma veinticinco por ciento e índice cero coma ochenta y siete. El cuarto trimestre acumula treinta y dos mil seiscientas cincuenta, veintiséis coma ochenta y nueve por ciento. Diciembre queda aproximadamente catorce por ciento por encima de la referencia uniforme; eso no significa que su participación sea catorce por ciento. Tampoco ajustamos por días del mes ni demostramos causas. Concluido el bloque RDD, doy paso a Sergio, quien continuará con las consultas sobre tablas con esquema.

## Lámina 11: 5a.1: por qué agrupamos antes de filtrar

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir los cuatro cuadros y señalar la posición de filter después de agg.

**Texto para decir:**

El punto cinco a pide agrupar y filtrar utilizando dos o más columnas. Departamento y rango de edad nos permiten preguntar: ¿qué combinaciones reúnen al menos mil personas reportadas? Buscamos acumulados por perfil territorial, no filas individuales con una cantidad alta. El umbral de mil es una condición de selección de esta consulta, no un criterio oficial de riesgo.

**Texto para decir:**

El primer cuadro dice «noventa y tres mil ochocientos treinta y un registros». La flecha conduce a «groupBy: departamento y edad». GroupBy forma un grupo por cada pareja de valores. Por ejemplo, Lima Metropolitana y doce a diecisiete años es un grupo distinto de Lima Metropolitana y dieciocho a veintinueve. La siguiente flecha lleva a «SUM de cantidad; filter mayor o igual a mil». Sum suma cantidades dentro de cada grupo; filter conserva los grupos que cumplen el umbral. La última flecha llega a «treinta combinaciones; orden descendente»: primero aparecen los totales mayores.

**Texto para decir:**

El código expresa esa misma secuencia. Denuncias es el DataFrame limpio. Agg aplica la agregación; as personas da un nombre a la suma. Col personas referencia esa nueva columna. OrderBy con desc ordena de mayor a menor. Val consulta guarda la definición del resultado en una variable de Scala; los puntos encadenan operaciones y los paréntesis contienen sus argumentos.

**Texto para decir:**

En la tabla, Lima Metropolitana de doce a diecisiete años suma diecisiete mil novecientas una personas; de dieciocho a veintinueve, siete mil ochocientas setenta y cinco; y Junín de doce a diecisiete, cuatro mil ciento sesenta y nueve. Es una muestra de las treinta combinaciones devueltas. El filtro se aplica al acumulado ya calculado. Una fila con cantidad uno puede aportar a un grupo que supere mil. Ahora mantendremos ese principio y cambiaremos la pregunta hacia los distritos.

## Lámina 12: 5b.2: del acumulado al ranking

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Mostrar que orderBy precede a limit y que la clave territorial tiene tres componentes.

**Texto para decir:**

El punto cinco b solicita un orden decreciente. La disponibilidad de departamento, provincia y distrito permite preguntar: ¿cuáles son los quince distritos con más personas reportadas entre 2019 y 2024? Las tres columnas evitan mezclar distritos que comparten nombre en ubicaciones distintas.

**Texto para decir:**

El primer cuadro dice «Todos los registros». Su flecha llega a «groupBy: departamento, provincia, distrito». La siguiente conduce a «SUM de cantidad; orderBy descendente». Primero calculamos el acumulado por ubicación y luego lo ordenamos de mayor a menor. La última flecha llega a «limit quince; quince distritos». Limit restringe la salida a los primeros quince resultados; debe ir después del orden para que sean los de mayor acumulado.

**Texto para decir:**

El fragmento Scala utiliza agg para sumar, as para nombrar personas, desc para indicar orden descendente y limit para seleccionar los quince. En la salida, San Juan de Lurigancho suma cuatro mil setecientas setenta y cinco personas; Lima, tres mil ciento cincuenta y tres; San Martín de Porres, dos mil seiscientas cuarenta y seis; y Callao, dos mil seiscientas cuarenta y dos. Ordenamos personas contabilizadas, no número de filas. El ranking describe volumen reportado y no riesgo por habitante. Para agrupar territorios de otra forma necesitamos unirlos con un catálogo; ahora explicaré esa unión.

## Lámina 13: 5c.1: INNER JOIN y conservación del total

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas y detenerse en la clave ubigeo antes de leer las regiones.

**Texto para decir:**

El punto cinco c pide distintos JOIN. En este primero preguntamos: ¿cómo se distribuye el total entre las regiones definidas por el catálogo geográfico del proyecto? La tabla de denuncias aporta las cantidades y el catálogo aporta la región asociada al ubigeo. Un JOIN relaciona ambas tablas mediante una clave común.

**Texto para decir:**

El primer cuadro dice «groupBy ubigeo; suma de personas». Reducimos las filas a un acumulado por código geográfico y conservamos también el conteo de registros. La flecha conduce a «INNER JOIN; clave ubigeo». Inner significa que se conservan las claves que encuentran coincidencia en ambos lados. En el código, join recibe dimGeografia, Seq ubigeo y la palabra inner. Seq indica aquí la lista de columnas utilizadas como clave.

**Texto para decir:**

La siguiente flecha llega a «groupBy región; sumar personas y registros». Una vez incorporada la región, reunimos los ubigeos de cada región. La última flecha conduce a «tres regiones; total ciento veintiún mil cuatrocientas veintisiete». La tabla muestra Costa con setenta y nueve mil seiscientas sesenta y seis personas y quinientos sesenta y siete ubigeos; Sierra con treinta y cuatro mil trescientas una y ochocientos cuatro; Selva con siete mil cuatrocientas sesenta y ciento treinta y cinco. Ubigeos es número de claves geográficas observadas, no número de personas.

**Texto para decir:**

Para que el resultado sea válido, el catálogo tiene una fila por ubigeo. Si una clave encontrara dos filas de dimensión, su cantidad se repetiría al unir. Además, la salida guardada no presenta claves nulas, es decir, ausentes, en el control geográfico. Verificamos la suma final para comprobar que no perdimos ni multiplicamos el total. La clasificación regional es la del código por departamento; no mide ruralidad. Continuaremos con funciones estadísticas sobre esos registros.

## Lámina 14: 5d.3: promedio, desviación y varianza

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer los cuatro cuadros y las funciones de los dos fragmentos. Distinguir desviación poblacional y varianza muestral.

**Texto para decir:**

El promedio global de tres b puede ocultar diferencias entre categorías. Por eso, usando SEXO y CANTIDAD, preguntamos: ¿cómo varía la dispersión de CANTIDAD por registro entre mujeres y hombres? Dispersión significa cuánto se separan los valores alrededor de su promedio. Esta consulta ejemplifica el punto cinco d, funciones de Spark, y conserva también NO ESPECIFICA.

**Texto para decir:**

El primer cuadro dice «Registros por sexo». La flecha lleva a «groupBy sexo»: formamos los tres grupos. La segunda flecha conduce a «avg, stddev pop, variance, min, max». Avg obtiene la media; stddev pop obtiene la desviación estándar poblacional; variance calcula la varianza muestral; min y max encuentran los extremos. El código añade count para filas, sum para personas y round para redondear, en este caso a cuatro decimales. As asigna nombres a las columnas calculadas.

**Texto para decir:**

La última flecha llega a «tres grupos; medias y dispersiones». En la salida F presenta promedio uno coma tres siete uno cinco y desviación poblacional uno coma cero cuatro cinco cuatro. M presenta uno coma uno ocho seis seis y cero coma cinco nueve siete uno. NO ESPECIFICA presenta uno coma cero seis siete seis y cero coma tres cero cero cero. Una desviación menor indica conteos más cercanos entre sí, en la escala de CANTIDAD.

**Texto para decir:**

Conviene distinguir los denominadores: la desviación poblacional se obtiene a partir de una varianza dividida entre n, el número de filas del grupo. La varianza muestral divide entre n menos uno. Además, la varianza está en unidades al cuadrado y la desviación vuelve a la escala original mediante la raíz. Por eso no debemos comparar directamente variance con stddev pop ni elevar al cuadrado la cifra mostrada esperando igualdad exacta. La mayor dispersión de F describe conteos agregados; no demuestra desapariciones en grupo. Ahora explicaré cómo esas mismas tablas pueden consultarse mediante vistas SQL.

## Lámina 15: 5e.2: dos JOIN, perfil y porcentaje nacional

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer las cuatro cajas; marcar ambos JOIN y después el OVER vacío del porcentaje.

**Texto para decir:**

Al conocer los rangos de edad y el sexo surge la pregunta: ¿qué combinación de grupo etario y sexo concentra más personas y qué porcentaje del total representa? Grupo etario significa categoría de edad, como adolescente. Esta consulta resuelve cinco e mediante vistas temporales y JOIN. Usamos las vistas denuncias, dim edad y dim sexo; las claves son rango edad y sexo.

**Texto para decir:**

El primer cuadro dice «Vistas registradas en esquema punto Scala». Registrar significa dar a cada DataFrame un nombre que spark punto sql puede consultar. CreateOrReplaceTempView crea esa vista o sustituye su definición en la sesión; no genera una tabla permanente de PostgreSQL. La flecha conduce a «JOIN dim edad; JOIN dim sexo». La primera unión incorpora el nombre del grupo etario mediante rango edad. La segunda relaciona la categoría de sexo. Son tres tablas y dos operaciones JOIN.

**Texto para decir:**

La siguiente flecha llega a «GROUP BY grupo y sexo; suma y porcentaje nacional». Group By forma las combinaciones y SUM calcula su cantidad. El código utiliza d, e y s como alias, nombres cortos de denuncias, edad y sexo; por eso d punto cantidad indica de qué tabla viene la medida. ON expresa la condición de igualdad entre claves. SELECT elige las columnas de salida y AS les asigna nombre.

**Texto para decir:**

El porcentaje divide cada suma entre SUM de SUM de cantidad, OVER paréntesis vacíos. La suma interior corresponde a cada grupo; la suma exterior es una función de ventana que reúne esos totales sin eliminar las filas del resultado. OVER vacío toma todos los grupos, por eso el denominador es nacional. CAST AS DOUBLE convierte a un tipo numérico con decimales y ROUND redondea a dos posiciones. El código incluye también COUNT asterisco para contar registros y AVG para obtener el promedio de CANTIDAD por registro.

**Texto para decir:**

La última flecha lleva a «dieciocho combinaciones; orden por personas». Adolescentes mujeres reúne cincuenta y un mil veintiuna personas, cuarenta y dos coma cero dos por ciento del total nacional. Adolescentes hombres reúne trece mil trescientas nueve, diez coma noventa y seis por ciento. Adultos hombres reúne doce mil trescientas ochenta y nueve, diez coma veinte por ciento. La fórmula visible multiplica cincuenta y un mil veintiuna por cien y divide entre ciento veintiún mil cuatrocientas veintisiete. Es participación dentro del dataset, no probabilidad individual. Usaremos ahora filtros para delimitar otro universo de comparación.

## Lámina 16: 5f.1: el orden de WHERE, GROUP BY, HAVING y LIMIT

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir los cuatro cuadros; señalar dónde se filtran filas y dónde se filtran grupos.

**Texto para decir:**

Después del ranking general podemos hacer una pregunta más específica: ¿qué diez distritos reúnen más mujeres reportadas en 2024 con rango de edad especificado? El punto cinco f pide ORDER BY combinado con filtros. Aquí fijamos año, sexo y calidad de la información de edad, y exigimos al menos veinte personas por distrito.

**Texto para decir:**

El primer cuadro dice «WHERE: año, sexo y edad». Where selecciona las filas que cumplen año igual a 2024, sexo igual a F y rango de edad distinto de NO ESPECIFICA. El signo menor que seguido de mayor que en SQL significa distinto. La primera flecha llega a «GROUP BY: departamento, provincia, distrito». Con las filas ya filtradas formamos cada ubicación y sumamos CANTIDAD.

**Texto para decir:**

La siguiente flecha conduce a «HAVING SUM mayor o igual a veinte; ORDER BY personas descendente». Having filtra grupos ya sumados; Order By los ordena de mayor a menor. La última flecha lleva a «LIMIT diez; diez distritos». Seleccionamos las diez primeras ubicaciones del ranking después de filtrar y ordenar. En el código, COUNT asterisco añade el número de registros, mientras SUM aporta personas.

**Texto para decir:**

La salida muestra San Juan de Lurigancho con trescientas setenta y nueve; Lima con doscientas ochenta y cuatro; Ate con doscientas setenta y una; y Callao con doscientas sesenta y cinco. No deben compararse directamente con los cuatro mil setecientos setenta y cinco del ranking general: este universo comprende solo mujeres de 2024 con edad especificada. Tampoco sería equivalente aplicar cantidad mayor o igual a veinte en WHERE: eso buscaría filas individuales de ese tamaño y no distritos con veinte personas acumuladas. Ya tenemos las respuestas analíticas; ahora mostraré cómo se conserva su base de datos.

## Lámina 17: Ítem 4: cada dimensión del modelo estrella y cada paso ETL

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Comenzar en el hecho central; recorrer las cuatro dimensiones izquierdas y las cuatro derechas. Aclarar que las flechas son relaciones, no una secuencia de carga.

**Texto para decir:**

El punto cuatro pide llevar los datos a PostgreSQL. De allí nace esta pregunta: ¿cómo almacenamos el dataset conservando el grano y los totales? Grano significa qué representa exactamente una fila. Conservamos el cruce original de tiempo, territorio y perfil; no lo convertimos en una fila por persona. Elegimos un modelo estrella, llamado así porque una tabla central de hechos se relaciona con dimensiones alrededor.

**Texto para decir:**

El cuadro central dice «hecho desaparicion; PK, id; ocho claves foráneas; mes, cantidad; noventa y tres mil ochocientas treinta y una filas». La tabla de hechos conserva la medida CANTIDAD y las referencias a los catálogos. PK significa clave primaria: id identifica de forma única cada registro almacenado. Ese id es generado en la base; no identifica a una persona del dataset. Una clave foránea referencia una clave válida en otra tabla.

**Texto para decir:**

A la izquierda, el primer cuadro dice «dim tiempo; PK, anio». Guarda el año y su periodo; su clave es anual, mientras mes queda en el hecho. El segundo dice «dim rango edad; PK, rango edad»: organiza las categorías de edad. El tercero, «dim sexo; PK, sexo», contiene las categorías de sexo. El cuarto, «dim nivel educativo; PK, nivel educativo», describe la categoría educativa. En todos, dim significa dimensión.

**Texto para decir:**

A la derecha, el primero dice «dim geografia; PK, ubigeo; mil quinientos seis». Relaciona cada código observado con su departamento, provincia, distrito y región. El siguiente dice «dim estado civil; PK, estado civil». Después leemos «dim nacionalidad; PK, nacionalidad». Finalmente, «dim ocupacion; PK, ocupacion». Esos tres catálogos describen estado civil, nacionalidad y ocupación. Ninguna de estas tablas cuenta personas por sí sola: describen las claves utilizadas por el hecho.

**Texto para decir:**

Cada flecha conecta una dimensión con la tabla central. La relación uno a muchos, escrita uno a N, significa que una fila de catálogo puede ser referenciada por muchos hechos; cada hecho utiliza una clave de ese catálogo. Las flechas no indican que una dimensión se transforme en otra ni el orden de un algoritmo. Hay ocho dimensiones y una tabla de hechos: nueve tablas del modelo, más una tabla intermedia de carga.

**Texto para decir:**

A la derecha del diagrama aparece el proceso ETL. Paso uno, «Extraer»: el comando barra inversa copy de psql lee el CSV del cliente y carga las noventa y tres mil ochocientas treinta y una filas en stg desaparicion. Stg abrevia staging, una zona intermedia antes del modelo definitivo. Paso dos, «Transformar»: los vacíos categóricos se etiquetan NO ESPECIFICA, los ubigeos se completan a seis dígitos y se obtiene una fila de catálogo por clave. Paso tres, «Cargar»: se insertan primero dimensiones y después hechos con cantidad mayor que cero, dentro de una transacción. La transacción permite confirmar el conjunto de cambios o revertirlo si falla la carga.

**Texto para decir:**

El control del ubigeo quince cero uno veintiuno es especialmente importante: aparece con dos nombres. Si conserváramos dos filas de catálogo para esa clave, un JOIN podría multiplicar sus cantidades. Agrupar por ubigeo antes de unir evita esa repetición. Finalmente, SUM de cantidad mantiene ciento veintiún mil cuatrocientas veintisiete tanto en la base cargada como en la evidencia de restauración del dump. Un dump es un archivo de respaldo que permite reconstruir la base. La captura está enlazada como evidencia guardada. A continuación explicaré cómo entregamos las tablas de resultados.

## Lámina 18: 5g: de Spark SQL a los seis libros Excel

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas; luego explicar cada opción visible en el fragmento y todas las filas de la tabla.

**Texto para decir:**

El punto cinco g pide exportar las consultas cinco e y cinco f utilizando Spark. Por eso preguntamos: ¿cómo entregamos en Excel los resultados de las seis consultas de vistas temporales? Partimos de los resultados tabulares, para que puedan revisarse sus columnas y valores.

**Texto para decir:**

El primer cuadro dice «spark punto sql; consultas cinco e y cinco f». Ese método ejecuta el texto SQL dentro de Spark. La flecha llega a «DataFrame de resultado; coalesce uno». Cada consulta devuelve un DataFrame. Aquí coalesce uno reduce sus particiones a una para la escritura. Esta llamada afecta la distribución del resultado; no reemplaza nulos como la función SQL de igual nombre. Es adecuada para estas tablas pequeñas, aunque concentrar una tabla grande en una sola partición puede limitar el rendimiento.

**Texto para decir:**

La siguiente flecha conduce a «write punto format; proveedor spark excel». Write inicia la escritura; format selecciona el proveedor com punto crealytics punto spark punto excel, una biblioteca que permite generar archivos Excel desde Spark. La última flecha lleva a «seis libros punto xlsx; una hoja por consulta». Un libro es el archivo Excel completo y la hoja es la pestaña interna con la tabla. No son seis pestañas dentro de un único libro.

**Texto para decir:**

En el fragmento, df es el DataFrame que recibe la función. La opción dataAddress construye el nombre de la hoja y la celda A uno como inicio de la tabla. Header true indica escribir los nombres de columna. InferSchema false es una opción del proveedor; en esta escritura el DataFrame ya tiene esquema y no necesita inferir tipos al leer. DateFormat indica el formato para fechas y timestampFormat añade hora, minuto y segundo para marcas de tiempo. Mode overwrite sustituye un archivo previo en el destino y save ruta escribe el resultado.

**Texto para decir:**

La tabla de salida muestra cinco e uno, hoja cinco e uno variacion, con sesenta filas; cinco e dos, hoja cinco e dos perfil, con dieciocho; cinco e tres, hoja cinco e tres region, con nueve. Para cinco f uno, hoja cinco f uno distritos, hay diez; para cinco f dos, hoja cinco f dos nacionalidad, doce; y para cinco f tres, hoja cinco f tres meses, doce. Se guardan en la carpeta excel. Estos son nombres de hojas, no variables nuevas del análisis.

**Texto para decir:**

El registro de ejecución documenta los seis libros escritos desde DataFrames. Hay una diferencia que debemos conocer: el export de cinco e dos conserva totales y promedio, pero no incluye la columna de porcentaje nacional de la consulta individual. Por eso, el cuarenta y dos coma cero dos por ciento se verifica en esa salida individual. Así cerramos la entrega de resultados y podemos volver a las preguntas que respondimos.

## Lámina 19: Conectar cada conclusión con su pregunta y evidencia

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Sergio lee todas las filas y realiza el cierre. Terminar aquí si solo se realiza el recorrido principal.

**Texto para decir:**

Para cerrar, volvemos a las preguntas. En tres b preguntamos cuántas personas contabilizadas hay por fila: el promedio es uno coma dos nueve cuatro uno y la mediana es uno. En tres c uno preguntamos qué peso tienen los menores: cincuenta y siete coma setenta y nueve por ciento nacional y setenta y dos coma veintiuno por ciento en Madre de Dios. En tres c dos preguntamos qué peso tienen los extranjeros: dos coma setenta y dos por ciento en 2024. Cada proporción tiene un denominador explícito.

**Texto para decir:**

En tres c tres preguntamos qué meses superan la referencia uniforme: diciembre aporta nueve coma cuarenta y nueve por ciento, con índice uno coma catorce. En cinco e dos preguntamos qué perfil concentra más personas reportadas: adolescentes mujeres, con cincuenta y un mil veintiuna y cuarenta y dos coma cero dos por ciento nacional. Finalmente, en cuatro y cinco g preguntamos cómo conservar y entregar los resultados: la base y su respaldo mantienen ciento veintiún mil cuatrocientas veintisiete personas contabilizadas y el registro documenta seis libros Excel.

**Texto para decir:**

El bloque inferior explica cómo se sostienen esas respuestas: cada ficha conecta el ítem, las columnas, las operaciones, el código original y la salida. Las veinticinco consultas pueden abrirse desde el mapa. Las conclusiones describen lo reportado en este dataset; las proporciones no estiman riesgo poblacional y los cambios temporales no demuestran causalidad. Con esto terminamos el recorrido principal. Muchas gracias; podemos abrir el detalle de cualquier consulta o realizar la demostración si disponemos de tiempo adicional.

## Lámina 20: Cómo navegar los anexos

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** No leer todos los enlaces durante una respuesta puntual. Escoger el solicitado y usar el retorno al mapa o a conclusiones.

**Texto para decir:**

Esta lámina organiza el detalle complementario. En cinco a están año y sexo, región y periodo, nacionalidad y edad. En cinco b está el promedio por departamento. En cinco c encontramos LEFT para el catálogo observado, RIGHT para el calendario y FULL para edades conocidas. En cinco d están la clasificación con when y otherwise, la participación por periodo y la construcción de fecha y trimestre. En cinco e están la variación interanual y región con periodo mediante JOIN. En cinco f están nacionalidades desde 2022 y meses de estudiantes.

**Texto para decir:**

La fila de respaldo abre las operaciones reales de tres c, la captura de PostgreSQL y las fuentes con los comandos de reproducción. Cada enlace lleva a una ficha completa, y los botones inferiores vuelven al mapa o a las conclusiones. Abriremos ahora la consulta que se desee revisar y seguiremos el mismo orden: pregunta, entrada, operaciones y salida.

## Lámina 21: 5a.2: año, sexo y filtro agregado

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer las cuatro cajas. Distinguir sexo F del umbral sobre personas.

**Texto para decir:**

Este segundo ejemplo de cinco a nace de AÑO, SEXO y CANTIDAD. Preguntamos: ¿en qué años el total reportado de mujeres supera cinco mil personas? Queremos comprobar el umbral en cada año, manteniendo separadas las categorías de sexo antes de seleccionar F.

**Texto para decir:**

El primer cuadro dice «Todos los registros». La flecha llega a «groupBy año, sexo»: cada pareja año y categoría de sexo forma un grupo. La siguiente conduce a «SUM de cantidad; sexo F y total mayor que cinco mil». Calculamos el acumulado y conservamos los grupos F cuyo total supera el umbral. La última flecha llega a «seis años; orden ascendente». Ascendente significa del año menor al mayor, de 2019 a 2024.

**Texto para decir:**

En el código, agg reúne sum y count: personas es la suma de CANTIDAD y registros es el número de filas. Filter combina dos condiciones mediante el operador y: sexo igual a F y personas mayor que cinco mil. La igualdad de columnas en esta API se escribe con tres signos iguales. OrderBy anio aplica el orden cronológico.

**Texto para decir:**

La salida muestra 2019 con dieciséis mil cuatrocientas cincuenta y una mujeres y once mil treinta y siete registros; 2020 con once mil ochocientas veintiocho y ocho mil setecientos veintitrés; 2024 con once mil doscientas setenta y cinco y ocho mil trescientos noventa y ocho. Los seis años cumplen el umbral, aunque la tabla visible muestra tres. El criterio se aplica al grupo, no a una fila aislada. Cambiaremos ahora las dos claves para observar región y periodo.

## Lámina 22: 5a.3: dos variables derivadas y periodos desiguales

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Señalar que región y periodo se crean en el esquema común. Leer las cuatro cajas y las cuatro filas visibles.

**Texto para decir:**

La tercera agrupación de cinco a pregunta: ¿qué combinaciones de región y periodo superan diez mil personas reportadas? Nace de las columnas derivadas región y periodo, junto con la medida CANTIDAD. Buscamos acumulados grandes según esa clasificación territorial y temporal.

**Texto para decir:**

El primer cuadro dice «Región y periodo: derivados del código». La flecha conduce a «groupBy: dos columnas». Formamos una combinación por región y periodo. La siguiente llega a «SUM de cantidad; total mayor que diez mil». Sumamos dentro de cada combinación y luego filtramos el acumulado. La última flecha termina en «cinco combinaciones; orden descendente». El código usa agg y as para producir personas, filter para el umbral y orderBy con desc para ubicar los mayores al inicio.

**Texto para decir:**

En la salida, Costa de 2022 a 2024 suma treinta y siete mil doscientas cuatro personas; Costa de 2020 a 2021, veinticuatro mil seiscientas tres; Costa de 2019, diecisiete mil ochocientas cincuenta y nueve. Sierra de 2022 a 2024 suma quince mil novecientas ochenta y una. Son cuatro filas mostradas de cinco resultados. El mayor acumulado es Costa en el periodo final. Sin embargo, los periodos duran tres, dos y un año: su comparación no equivale a comparar promedios anuales. La siguiente consulta utilizará categorías originales de nacionalidad y edad.

## Lámina 23: 5a.4: delimitar extranjeros antes de agrupar

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer las cuatro cajas y contrastar el filtro inicial con el umbral posterior.

**Texto para decir:**

Del cruce entre NACIONALIDAD y RANGO EDAD surge esta pregunta: ¿qué combinaciones de nacionalidad extranjera y rango de edad reúnen al menos treinta personas? Es el cuarto ejemplo de cinco a. Queremos identificar los grupos más numerosos del subconjunto extranjero declarado.

**Texto para decir:**

El primer cuadro dice «Excluir PERÚ y NO ESPECIFICA». Este filtro actúa sobre las filas de entrada. La flecha lleva a «groupBy: nacionalidad y edad»: agrupamos únicamente las filas conservadas. La siguiente llega a «SUM de cantidad; total mayor o igual a treinta». Ahora el filtro actúa sobre grupos ya sumados. La última flecha conduce a «nueve combinaciones; orden descendente». Primero se ubican las de mayor número de personas.

**Texto para decir:**

En el código, col referencia una columna; el operador de distinto descarta las dos etiquetas indicadas y el operador y exige ambas condiciones. Agg calcula sum, as nombra personas y filter aplica el mínimo de treinta. La salida muestra Venezuela de doce a diecisiete años con mil nueve personas; Venezuela de dieciocho a veintinueve con quinientas noventa y cinco; y Colombia de dieciocho a veintinueve con cincuenta y dos. El mayor volumen corresponde al primer grupo. No es una tasa respecto de cuántas personas de cada nacionalidad viven en el país. Después del volumen revisaremos una pregunta sobre promedios.

## Lámina 24: 5b.1: promedio por registro y departamento

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas y explicar la diferencia entre promedio y total de la tabla.

**Texto para decir:**

El punto cinco b también solicita un promedio. A partir de DEPARTAMENTO y CANTIDAD preguntamos: ¿cuál es el promedio de personas contabilizadas por registro en cada departamento? Esta pregunta es diferente de cuál departamento suma más personas. El promedio divide la suma de cantidades entre el número de filas del departamento.

**Texto para decir:**

El primer cuadro dice «Registros por departamento». Su flecha llega a «groupBy: departamento». La siguiente conduce a «avg de cantidad; redondeo a tres decimales». Avg calcula la media y round fija las posiciones decimales de presentación. La última flecha lleva a «veintiséis departamentos; promedio descendente». Ordenamos por esa media, de mayor a menor. El código añade count para registros y sum para personas; as asigna nombres a las columnas calculadas.

**Texto para decir:**

La salida muestra Callao con promedio uno coma cinco cero cinco y cuatro mil quinientas sesenta y cinco personas; Lima Metropolitana con uno coma cuatro ocho cinco y treinta y nueve mil cuatrocientas noventa; y Lambayeque con uno coma tres uno nueve y seis mil doscientas treinta y una. Callao encabeza la media, aunque Lima Metropolitana tiene mayor acumulado. La media describe la cantidad por cruce agregado, no cuántas personas participan de un mismo incidente. Veamos ahora cómo se detecta una falta de correspondencia entre tablas.

## Lámina 25: 5c.2: qué conserva LEFT y qué significa un nulo

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Identificar el catálogo como izquierda y los acumulados como derecha antes de seguir las flechas.

**Texto para decir:**

El segundo JOIN de cinco c pregunta: ¿qué ubigeos del catálogo construido con el dataset no tienen registros asociados? Queremos mostrar cómo conservar una lista de referencia y detectar sus elementos sin coincidencia. La referencia es dimGeografia, a la izquierda, y los acumulados por ubigeo quedan a la derecha.

**Texto para decir:**

El primer cuadro dice «Catálogo derivado del mismo dataset». La flecha llega a «LEFT OUTER JOIN: conservar todos sus ubigeos». Left conserva cada clave de la tabla izquierda, incluso si no encuentra coincidencia en la derecha; outer indica esa conservación de filas sin pareja. La siguiente flecha conduce a «personas punto isNull: buscar faltantes». Un null representa ausencia de valor; aquí aparecería cuando un ubigeo del catálogo no encuentra su acumulado. IsNull comprueba esa ausencia. La última flecha termina en «cero sin registros; mil quinientos seis con registros».

**Texto para decir:**

En el fragmento se ve join con la clave ubigeo y el tipo left, seguido del filtro de personas nulas. La salida repite los tres controles: mil quinientos seis ubigeos de catálogo, todos con registros, y cero sin registros. Este cero verifica correspondencia interna. Como el catálogo procede de las propias denuncias, no prueba que estén todos los distritos del país. Para detectar ausencias respecto de un universo independiente necesitaremos una referencia construida fuera de las filas observadas, como el calendario de la siguiente consulta.

## Lámina 26: 5c.3: RIGHT con un calendario independiente

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Señalar el calendario a la derecha y distinguir coalesce como función de valores.

**Texto para decir:**

La tercera unión pregunta: ¿hay meses sin registros entre enero de 2019 y diciembre de 2024? Aquí sí podemos construir la referencia completa sin depender de las denuncias: seis años por doce meses son setenta y dos combinaciones. Los acumulados están a la izquierda y ese calendario a la derecha.

**Texto para decir:**

El primer cuadro dice «groupBy año, mes». Sumamos las cantidades de cada combinación observada. La flecha conduce a «RIGHT OUTER JOIN: conservar calendario». Right conserva todas las filas de la derecha, aunque un mes no tenga coincidencia en los acumulados. La siguiente llega a «coalesce a cero; filtrar total igual a cero». En esta expresión, coalesce elige el primer valor no nulo: mantiene el acumulado si existe y utiliza cero si falta. Después buscamos esos ceros. La última flecha termina en «setenta y dos meses presentes; cero meses sin registros».

**Texto para decir:**

El código utiliza join de tipo right con las claves año y mes. WithColumn crea o sustituye la columna ajustada, y lit cero aporta el valor constante. Esta función coalesce sobre columnas no es el método coalesce uno de la exportación, que modifica particiones. La tabla confirma setenta y dos meses conservados, ninguno sin registros y una suma de ciento veintiún mil cuatrocientas veintisiete personas. La consulta controla presencia mensual; no garantiza que cada denuncia posible se haya registrado. Pasaremos ahora a conservar claves de ambos lados de una unión.

## Lámina 27: 5c.4: FULL OUTER y el universo de edades conocidas

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer las cuatro cajas; señalar por qué el denominador difiere del de 3c.1.

**Texto para decir:**

El cuarto JOIN pregunta: ¿qué departamentos aparecen solo entre menores o solo entre adultos con edad conocida? RANGO EDAD permite separar dos subconjuntos; DEPARTAMENTO permite compararlos. Excluimos NO ESPECIFICA, por lo que esta consulta tiene un universo diferente del porcentaje de tres c uno.

**Texto para decir:**

El primer cuadro dice «Filtrar edades conocidas; dos agregados». Sumamos menores por departamento y, por separado, adultos por departamento. La flecha conduce a «FULL OUTER JOIN: clave departamento». Full outer conserva todas las claves de ambos lados: si un departamento aparece solo en uno, no desaparece del resultado. La siguiente flecha llega a «coalesce a cero; sumar y calcular porcentaje». Reemplazamos cantidades ausentes por cero, sumamos menores y adultos y calculamos la participación de menores sobre ese total conocido. La última flecha conduce a «veintiséis departamentos; ambos grupos presentes».

**Texto para decir:**

En el código, full guion bajo outer selecciona ese comportamiento; withColumn crea los totales y round redondea el porcentaje. La salida muestra Lima Metropolitana con diecinueve mil setecientos cincuenta y un menores y diecinueve mil doscientos ochenta y un adultos; Junín con cuatro mil quinientos veinticinco y dos mil seiscientos ochenta y ocho; Cusco con cuatro mil doscientos noventa y nueve y dos mil setecientos sesenta y dos.

**Texto para decir:**

Los veintiséis departamentos tienen ambos grupos. Para Lima, menores dividido entre menores más adultos da cincuenta coma sesenta por ciento. En tres c uno, el mismo numerador se divide entre el total que incluye edad no especificada y da cincuenta coma cero dos. Cambiar el denominador cambia la pregunta; esa diferencia no es una contradicción. La siguiente ficha mostrará cómo crear categorías mediante condiciones.

## Lámina 28: 5d.1: interpretar las etiquetas de when y otherwise

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas. Leer las etiquetas del código y explicar sus límites.

**Texto para decir:**

Esta consulta nace de la variación de CANTIDAD y ejemplifica las funciones del punto cinco d. Preguntamos: ¿cuántas filas y personas corresponden a cada tramo de CANTIDAD definido por el código? Los tramos sirven para describir la distribución de conteos. No son categorías oficiales de gravedad.

**Texto para decir:**

El primer cuadro dice «cantidad por fila». La flecha llega a «when y otherwise: uno a dos, tres a cuatro, cinco o más». When significa cuando: si cantidad es al menos cinco se asigna FOCO; si no, pero es al menos tres, se asigna ALTO; otherwise, en los demás casos de estos datos positivos, se asigna INDIVIDUAL. Importa comprobar primero el umbral más alto. WithColumn crea la columna llamada severidad con esa etiqueta.

**Texto para decir:**

La siguiente flecha conduce a «groupBy severidad; count, sum y avg». Agrupamos por la nueva etiqueta, contamos filas, sumamos personas y calculamos el promedio por fila. La última flecha llega a «tres categorías; noventa y ocho mil doscientas treinta y seis, catorce mil tres y nueve mil ciento ochenta y ocho». Son sus sumas de personas, no sus conteos de filas.

**Texto para decir:**

La tabla detalla INDIVIDUAL, uno a dos, con ochenta y ocho mil ciento ochenta y dos filas y noventa y ocho mil doscientas treinta y seis personas; ALTO, tres a cuatro, con cuatro mil doscientas cuarenta y nueve filas y catorce mil tres personas; FOCO, cinco o más, con mil cuatrocientas filas y nueve mil ciento ochenta y ocho personas. Aunque la columna se llame severidad y una etiqueta diga INDIVIDUAL, los datos no permiten afirmar gravedad ni incidentes individuales o grupales. Los nombres son decisiones del código sobre conteos agregados. Ahora calcularemos participaciones por periodo.

## Lámina 29: 5d.2: numerador por periodo y denominador constante

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer las cuatro cajas; señalar que lit introduce un valor fijo y round solo redondea.

**Texto para decir:**

De la columna periodo nace la pregunta: ¿qué participación tiene cada periodo sobre el total de personas reportadas? Es otra aplicación de funciones del punto cinco d. Necesitamos un numerador que cambia por periodo y un denominador nacional común.

**Texto para decir:**

El primer cuadro dice «Obtener total nacional: agg y sum». La agregación de todo el DataFrame obtiene ciento veintiún mil cuatrocientas veintisiete. La flecha conduce a «groupBy periodo: suma por periodo». Cada periodo recibe su propio acumulado. La siguiente llega a «lit total nacional; round de cien por parte dividido entre total». Lit convierte el número fijo en una expresión de columna utilizable en la fórmula. Round redondea el porcentaje; no cambia su denominador. La última flecha llega a «tres participaciones; suman cien por ciento».

**Texto para decir:**

En el código, col personas referencia el numerador y withColumn añade pct nacional. Se añade también personas por registro dividiendo la suma de personas entre count de registros, con tres decimales. La salida de 2019 es veintiséis mil trescientas treinta y una personas, veintiuno coma sesenta y ocho por ciento; 2020 a 2021 suma treinta y ocho mil seiscientas veintidós, treinta y uno coma ochenta y uno por ciento; 2022 a 2024 suma cincuenta y seis mil cuatrocientas setenta y cuatro, cuarenta y seis coma cincuenta y uno por ciento.

**Texto para decir:**

El periodo final pesa más dentro del acumulado, pero dura tres años frente a dos y uno de los demás. Esa participación no demuestra mayor intensidad anual ni un efecto causal de la pandemia. Si quisiéramos intensidad comparable, tendríamos que definir otra medida que tenga en cuenta la duración. La siguiente consulta construye una unidad temporal más detallada: el trimestre de cada año.

## Lámina 30: 5d.4: construir una fecha sin inventar un día observado

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas y relacionar concat, lpad y to date con la fecha convencional.

**Texto para decir:**

AÑO y MES permiten preguntar: ¿cuántas personas se reportan por trimestre al construir una fecha con año y mes? Esta consulta del punto cinco d muestra funciones temporales. Necesitamos una fecha utilizable por Spark, aunque el archivo no informe el día. Por eso fijamos convencionalmente el día uno.

**Texto para decir:**

El primer cuadro dice «Año y mes originales». La flecha lleva a «concat ws más lpad; to date». Concat ws une textos con un separador, aquí el guion; lpad completa el mes por la izquierda con cero hasta dos posiciones, por ejemplo cero uno para enero. Lit aporta el texto cero uno como día. To date convierte esa cadena en una fecha.

**Texto para decir:**

La siguiente flecha conduce a «quarter de fecha; agrupar y sumar». Quarter devuelve el trimestre de uno a cuatro. Concat forma una etiqueta como 2020 guion T dos, uniendo el año, la letra T y el trimestre. Agrupamos por esa etiqueta y sumamos cantidades. La última flecha llega a «veinticuatro trimestres; orden cronológico»: seis años por cuatro trimestres. A diferencia del apoyo de tres c tres, aquí cada año conserva sus cuatro trimestres separados.

**Texto para decir:**

La salida muestra 2019 T cuatro con siete mil ciento sesenta y cuatro; 2020 T uno con seis mil dieciocho; T dos con dos mil cuatrocientas ochenta y cinco; T tres con cuatro mil quinientas sesenta y siete. Entre esas filas, el segundo trimestre de 2020 tiene el menor acumulado. La fecha construida permite agrupar y ordenar; no representa el día real de cada desaparición ni explica la causa de la caída. Las siguientes fichas utilizarán SQL sobre vistas para otras comparaciones temporales.

## Lámina 31: 5e.1: comparar cada mes con el mismo mes del año anterior

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer las cuatro cajas; señalar la doble condición del JOIN y la fórmula de variación.

**Texto para decir:**

La serie mensual permite preguntar cómo cambia cada mes frente al mismo mes del año anterior y cuántos meses suben o bajan. Es la primera consulta de cinco e sobre vistas temporales. Comparar enero con el enero anterior mantiene el mes de referencia; comparar enero con diciembre respondería otra pregunta.

**Texto para decir:**

El primer cuadro dice «SUM por año y mes; dos copias del agregado». Las dos subconsultas calculan el mismo acumulado mensual y reciben los alias a y b. La flecha conduce a «JOIN consigo misma; mismo mes, año anterior». Esto se llama self join, o unión de una tabla consigo misma. La condición ON exige a punto mes igual a b punto mes y a punto año igual a b punto año más uno. A representa el periodo actual y b el anterior.

**Texto para decir:**

La siguiente flecha llega a «cien por actual menos previo, dividido entre previo». Primero restamos los conteos, luego dividimos entre el anterior y multiplicamos por cien. CAST AS DOUBLE permite el cálculo decimal y ROUND lo redondea a dos posiciones. La última flecha conduce a «sesenta comparaciones; veintiuna suben, treinta y nueve bajan». Hay cinco años comparables, de 2020 a 2024, por doce meses. El año 2019 sirve de base inicial y no tiene un 2018 en el archivo para compararse.

**Texto para decir:**

En la salida, enero de 2020 tiene mil ciento sesenta y cinco frente a dos mil ochenta y seis: menos cuarenta y cuatro coma quince por ciento. Febrero tiene dos mil setecientas veinticinco frente a dos mil trescientas veinticuatro: más diecisiete coma veinticinco por ciento. Abril tiene seiscientas cuarenta y tres frente a mil novecientas noventa y seis: menos sesenta y siete coma setenta y nueve por ciento. Son cambios de conteos frente al año anterior, no restas de participaciones. La consulta describe la variación; no identifica su causa. Pasaremos ahora a participaciones territoriales dentro de cada periodo.

## Lámina 32: 5e.3: tres tablas, dos JOIN y una ventana por periodo

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas y marcar el PARTITION BY periodo. La tabla de salida se lee por columnas temporales.

**Texto para decir:**

Esta consulta de cinco e pregunta: ¿cuál es el peso de cada región dentro de cada periodo tras unir geografía y tiempo? Nace de la posibilidad de relacionar las denuncias con dos catálogos: ubicación por ubigeo y periodo por año. Queremos comparar la composición regional de cada periodo, además de sus acumulados.

**Texto para decir:**

El primer cuadro dice «Denuncias y dimensiones». La flecha conduce a «Dos JOIN: ubigeo y año». Denuncias se llama d; geografía, g; tiempo, t. Unimos d punto ubigeo con g punto ubigeo y d punto anio con t punto anio. Son tres tablas relacionadas mediante dos JOIN, aunque el comentario original del script diga unión triple. Cada alias identifica de qué tabla procede una columna.

**Texto para decir:**

La siguiente flecha lleva a «GROUP BY región, periodo; ventana por periodo». Primero sumamos cantidad para cada combinación. Después, SUM de SUM de cantidad OVER PARTITION BY periodo obtiene el denominador de ese periodo. Partition By separa los grupos de la ventana según el periodo; no cambia aquí el grano de la fila de salida ni describe particiones físicas de un archivo. CAST AS DOUBLE permite decimales, ROUND redondea y AVG calcula el promedio por registro que también incluye el código.

**Texto para decir:**

La última flecha conduce a «nueve combinaciones; total ciento veintiún mil cuatrocientas veintisiete». Son tres regiones por tres periodos. La tabla muestra Costa con sesenta y siete coma ochenta y dos por ciento en 2019, sesenta y tres coma setenta en 2020 a 2021 y sesenta y cinco coma ochenta y ocho en 2022 a 2024. Sierra presenta veintiséis coma quince, veintinueve coma sesenta y veintiocho coma treinta. Selva presenta seis coma cero dos, seis coma sesenta y nueve y cinco coma ochenta y dos.

**Texto para decir:**

Cada columna temporal se interpreta respecto de su propio total; las participaciones suman aproximadamente cien por ciento por el redondeo. Costa conserva el mayor peso. A diferencia de cinco e dos, donde OVER vacío usa todos los grupos del país, aquí la ventana se restringe al periodo. La suma de las nueve combinaciones conserva el total original. Revisaremos ahora filtros y ordenamientos sobre nacionalidades.

## Lámina 33: 5f.2: ordenar acumulados no equivale a medir crecimiento

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer los cuatro cuadros y la doble regla de ORDER BY.

**Texto para decir:**

El segundo ejemplo de cinco f pregunta: ¿qué pares de nacionalidad extranjera y año acumulan más personas desde 2022, con al menos diez por grupo? La pregunta se construye con NACIONALIDAD, AÑO y CANTIDAD. Buscamos ordenar volúmenes anuales de nacionalidades declaradas.

**Texto para decir:**

El primer cuadro dice «WHERE: extranjeros desde 2022». NOT IN excluye PERÚ y NO ESPECIFICA; la condición año mayor o igual a 2022 conserva el periodo final. La flecha conduce a «GROUP BY: nacionalidad y año». Cada pareja recibe una suma de cantidad, un count de registros y un promedio con AVG. ROUND presenta ese promedio con tres decimales.

**Texto para decir:**

La siguiente flecha llega a «HAVING SUM mayor o igual a diez». Filtramos grupos, no filas originales. La última conduce a «doce pares; personas descendente, año ascendente». El primer criterio ordena de mayor a menor acumulado. Si dos grupos empatan en personas, el segundo coloca primero el año menor. ASC significa ascendente y DESC, descendente. No estamos ordenando principalmente por año.

**Texto para decir:**

La salida muestra Venezuela en 2023 con cuatrocientas treinta personas; en 2024 con cuatrocientas veintiocho; y en 2022 con cuatrocientas cuatro. Colombia en 2023 aparece con veinticuatro. Venezuela ocupa los tres primeros puestos. Aunque el comentario original del script mencione crecimiento, la consulta ejecutada ordena acumulados y no calcula una tasa de crecimiento: no resta un año anterior ni divide entre su valor. La pregunta de esta ficha corresponde a lo que efectivamente calcula el código. La última consulta filtrada se centrará en estudiantes.

## Lámina 34: 5f.3: qué incluye el subconjunto de estudiantes

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Seguir las cuatro cajas; señalar los tres rangos de edad y que GROUP BY mes reúne los años.

**Texto para decir:**

La ocupación y la edad permiten preguntar: ¿en qué meses se acumulan más estudiantes reportados de seis a veintinueve años? Esta consulta completa cinco f. Su universo incluye solo ocupación declarada ESTUDIANTE y los rangos seis a once, doce a diecisiete y dieciocho a veintinueve.

**Texto para decir:**

El primer cuadro dice «WHERE: estudiante y edad». El operador IN comprueba si rango edad pertenece a esa lista; AND exige además que la ocupación coincida. La flecha lleva a «GROUP BY mes; SUM de cantidad». Agrupamos por número de mes, reuniendo todos los años del archivo, y sumamos personas. Count asterisco cuenta las filas que aportan al grupo.

**Texto para decir:**

La siguiente flecha conduce a «HAVING SUM mayor que quinientos». Conservamos los meses cuyo acumulado supera el umbral. La última llega a «doce meses; personas descendente, mes ascendente». Primero ordenamos por personas; en un empate, por el número de mes de menor a mayor.

**Texto para decir:**

La salida muestra diciembre con dos mil seiscientas setenta y tres personas y dos mil diecinueve registros; noviembre con dos mil quinientas treinta y ocho y mil novecientos sesenta; septiembre con dos mil cuatrocientas diecinueve y mil novecientos tres; abril con mil setecientas setenta y cuatro y mil trescientos setenta y ocho. Diciembre encabeza ese subconjunto. No incluye edades de cero a cinco ni edades desconocidas, y tampoco podemos recuperar estudiantes cuya ocupación no está informada. Por ello describimos estudiantes declarados en esos rangos, sin generalizar a todo el alumnado del país. Terminamos las veinticinco consultas; las siguientes láminas permiten revisar operaciones y evidencias.

## Lámina 35: Qué reducciones existen realmente en 3c

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Leer cada fila de izquierda a derecha. No presentar el cuadro como un conteo de jobs físicos ni como tres reducciones consecutivas del indicador.

**Texto para decir:**

Esta lámina responde dónde están las reducciones que acompañan a cada consulta decimal del punto tres c. Las columnas separan dos reducciones principales, una reducción adicional real y el cálculo decimal. Esa separación permite explicar el código con precisión.

**Texto para decir:**

En tres c uno, las dos principales son total y menores por departamento mediante map y reduceByKey. El control nacional usa values y reduce sobre los totales departamentales y, por separado, sobre los menores. Values proyecta los valores: extrae las cantidades y descarta las claves. El cálculo decimal usa join y map para obtener cien por menores dividido entre total. Por tanto, la división posterior no es otra reducción.

**Texto para decir:**

En tres c dos, las principales agregan total y extranjeros por año. La adicional utiliza map y reduceByKey en topNacionalidades, que suma extranjeros por nacionalidad. Luego join y map calculan el porcentaje anual. En tres c tres, las principales suman por mes y obtienen el total nacional bajo una clave constante. La adicional, porTrimestre, utiliza map y reduceByKey para los totales trimestrales. Un map posterior obtiene participación, diferencia respecto de la referencia e índice.

**Texto para decir:**

El bloque «Lectura del flujo» resume que cada consulta combina ramas principales y de control o apoyo. Los diagramas describen las operaciones del programa. No representan tres reducciones consecutivas del indicador, ni un conteo medido de jobs físicos de Spark. Un job es una ejecución que Spark activa al solicitar un resultado; sus etapas y tareas dependen del plan y las particiones. Si se exigiera una dependencia estricta de tres reducciones consecutivas para el mismo indicador, el flujo actual necesitaría una adaptación. Podemos abrir los scripts para señalar exactamente las operaciones que acabamos de describir.

## Lámina 36: Qué demuestra la captura PostgreSQL

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Abrir la captura y señalar la consulta ejecutada y su tabla de respuesta. No afirmar que es una ejecución actual.

**Texto para decir:**

Esta es una captura guardada de PostgreSQL funcionando. Sirve como evidencia visual de la base utilizada en el proyecto. La contrastamos con los archivos de conteos y de restauración, que permiten leer los resultados en texto. No la presentamos como una ejecución en vivo.

**Texto para decir:**

Arriba se ve el comando psql: es el cliente de terminal de PostgreSQL. Menos h indica la ubicación de conexión, aquí barra tmp para el socket local; menos p indica el puerto, cinco mil cuatrocientos treinta y tres; menos U indica el usuario macrodatos y menos d selecciona la base denuncias. La tabla de salida muestra dos columnas, tabla y count, donde count es el conteo de filas. Dim ocupacion tiene doscientas ochenta y ocho; dim nacionalidad, cincuenta y siete; dim geografia, mil quinientas seis; hecho desaparicion, noventa y tres mil ochocientas treinta y una; y stg desaparicion, también noventa y tres mil ochocientas treinta y una. Stg es la tabla intermedia de carga. La indicación cinco rows significa que se mostraron cinco filas de este control.

**Texto para decir:**

Los controles distinguen filas de dimensiones, filas de hechos y suma de cantidad. Cada dimensión contiene su catálogo; la tabla hecho desaparicion conserva noventa y tres mil ochocientas treinta y una filas. SUM de cantidad conserva ciento veintiún mil cuatrocientas veintisiete personas contabilizadas. El conteo de hechos y la suma no tienen que coincidir, porque responden a medidas distintas. El respaldo restaurado mantiene el mismo total. El enlace inferior permite volver al modelo estrella para relacionar cada nombre de tabla con su función.

## Lámina 37: Fuentes, fragmentos de código y reproducción

**Expone:** Sergio. Versión explicada.

> **Indicación, no leer:** Recorrer los seis grupos de fuentes y ambos comandos. Explicar los pies y botones comunes de las fichas.

**Texto para decir:**

Esta última lámina permite localizar el soporte de cada afirmación. Primero está el enunciado, guardado en material, cuyos puntos uno a nueve orientan la entrega; el punto siete determina la estructura de las fichas. Después está la fuente del dataset: MININTER, Denuncias por desaparición de personas, en la PNDA. El enlace identifica su publicación; el CSV utilizado está en la carpeta data y corresponde al periodo 2019 a 2024.

**Texto para decir:**

La carpeta code común contiene lectura, limpieza y catálogos; code RDD contiene las consultas de tres a a tres c; code SQL contiene las consultas DataFrame, vistas y exportación. Cada ficha muestra un fragmento de esos archivos originales, no una implementación diferente escrita solo para la lámina. Los resultados se localizan en evidencias, salidas; los libros, en excel; y los controles de base, en los archivos cuyo nombre empieza por pg.

**Texto para decir:**

En la parte de base de datos, cero uno DDL define las tablas: DDL significa lenguaje de definición de datos. Cero dos ETL realiza la carga. Dump denuncias es el respaldo SQL de la base. Las extensiones indican el tipo de archivo: punto Scala contiene el programa, punto SQL contiene instrucciones de base, punto TXT contiene salida de texto y punto XLSX es un libro Excel.

**Texto para decir:**

En los comandos, punto barra bin barra ejecutar punto sh recibe la ruta de una consulta: el ejemplo ejecuta tres a, máximos y mínimos por sexo. La extensión sh indica un script de shell. Punto barra bin barra correr todas punto sh lanza el conjunto de consultas. Las rutas absolutas de la instalación original deben ajustarse si se ejecutan directamente en otra máquina; la guía DEMO explica además una alternativa con Docker que conserva esas rutas dentro del contenedor. Docker ejecuta un entorno aislado con las dependencias preparadas.

**Texto para decir:**

En las fichas, el encabezado identifica el ítem y la pregunta; Entrada indica qué columnas y filtros se utilizan; Salida muestra valores de la ejecución guardada; y Respuesta interpreta esos valores. Los pies remiten a fuentes o archivos de soporte. Los botones Fuentes y Mapa permiten navegar y el número de lámina ubica la ficha. La presentación se revisó con resultados ya guardados: editar este material no implica haber vuelto a ejecutar Spark o PostgreSQL. Así queda disponible el recorrido desde la pregunta hasta su evidencia.

# Ruta breve para diez minutos: texto para decir

Esta es una alternativa a la versión explicada, no su continuación oral.

Los tiempos son objetivos de ensayo y deben comprobarse con cronómetro.

### Lámina 1: Portada

**Expone:** Arbués. **Tiempo objetivo:** 15 s.

> **Indicación, no leer:** El título y las cifras de registros y personas.

**Texto para decir:**

Buenos días. Somos Arbués Pérez y Sergio Pezo. Presentaremos el análisis de las denuncias por desaparición de personas en el Perú entre 2019 y 2024. Trabajamos con datos del Ministerio del Interior y consultas en Scala, Spark SQL y PostgreSQL.

### Lámina 2: Mapa de preguntas

**Expone:** Arbués. **Tiempo objetivo:** 20 s.

> **Indicación, no leer:** Las filas 3, 4 y 5 del mapa. No abrir los anexos durante este recorrido.

**Texto para decir:**

Organizamos el trabajo siguiendo los números del enunciado. Primero explicaremos el dataset, después las operaciones MapReduce y luego las consultas de Spark SQL. Cada ficha indica qué pregunta responde, qué columnas usa y cómo se interpreta su resultado. Los enlaces permiten revisar las veinticinco consultas.

### Lámina 3: Dataset y unidad de análisis

**Expone:** Arbués. **Tiempo objetivo:** 30 s.

> **Indicación, no leer:** Primero 93 831 filas y después la suma 121 427.

**Texto para decir:**

El dataset pertenece al rubro Seguridad y Justicia de la Plataforma Nacional de Datos Abiertos. Contiene noventa y tres mil ochocientos treinta y un registros y trece columnas. Cada fila combina año, mes, ubicación y perfil, con una cantidad de personas reportadas. Por eso, contar filas no equivale a contar personas. Para obtener el total sumamos CANTIDAD: ciento veintiún mil cuatrocientos veintisiete.

### Lámina 4: Campos y reglas

**Expone:** Arbués. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** Tiempo, ubicación, perfil y CANTIDAD en la tabla.

**Texto para decir:**

Los campos se organizan en cuatro grupos: tiempo, ubicación, perfil y cantidad. El ubigeo se conserva como texto para mantener sus ceros iniciales. Consideramos menores a los rangos de cero a cinco, seis a once y doce a diecisiete años. Los campos vacíos se conservan como NO ESPECIFICA. Región y periodo se obtienen mediante reglas explícitas del código.

### Lámina 5: Flujo de procesamiento

**Expone:** Arbués. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** Las ramas del CSV a RDD, DataFrame y PostgreSQL.

**Texto para decir:**

Este diagrama muestra cómo obtuvimos las respuestas. El mismo CSV alimenta una colección distribuida RDD y una tabla DataFrame. Con el RDD aplicamos map y reduce. Con el DataFrame usamos agrupamientos, filtros, uniones y funciones. Después registramos vistas para consultar en SQL. PostgreSQL recibe los datos mediante un proceso propio de extracción, transformación y carga.

### Lámina 6: 3a: extremos por sexo

**Expone:** Arbués. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** La clave sexo en map, el reducer y la tabla de extremos.

**Texto para decir:**

La pregunta del punto tres a es cuál es el mayor y el menor conteo por registro para cada sexo. Map emite el sexo como clave y las cantidades como valores. Reduce conserva el máximo y el mínimo. Obtenemos máximos de dieciocho para mujeres y catorce para hombres. Otras reducciones conservan la ubicación de un registro extremo.

### Lámina 7: 3b: estadísticos

**Expone:** Arbués. **Tiempo objetivo:** 30 s.

> **Indicación, no leer:** Suma y conteo, fórmula de desviación y tabla.

**Texto para decir:**

En el punto tres b describimos la columna CANTIDAD. Una reducción obtiene la suma y el número de filas para calcular el promedio. Otra suma los cuadrados para obtener la desviación. El promedio es aproximadamente uno coma veintinueve y la mediana es uno. La mediana requiere ordenar e identificar la posición central. También obtenemos mínimo uno y máximo dieciocho, como muestra la salida.

### Lámina 8: 3c.1: menores por departamento

**Expone:** Arbués. **Tiempo objetivo:** 40 s.

> **Indicación, no leer:** Las ramas A y B, el porcentaje y el control C.

**Texto para decir:**

La primera consulta decimal pregunta qué proporción de cada departamento corresponde a menores. Primero, Map emite departamento y cantidad. Después, Reduce suma por departamento. Repetimos el par Map y Reduce filtrando menores. Unimos ambos resultados y dividimos menores entre total. Madre de Dios registra cuatrocientos setenta y tres menores de seiscientas cincuenta y cinco personas: setenta y dos coma veintiuno por ciento. Además, reducimos los parciales para el control nacional. Ese control da setenta mil ciento setenta y ocho menores sobre ciento veintiún mil cuatrocientos veintisiete: cincuenta y siete coma setenta y nueve por ciento.

### Lámina 9: 3c.2: extranjeros por año

**Expone:** Arbués. **Tiempo objetivo:** 35 s.

> **Indicación, no leer:** Total anual, extranjeros por año y agregación por nacionalidad.

**Texto para decir:**

La segunda consulta decimal calcula la participación anual de personas extranjeras. Primero, Map emite año y cantidad; después, Reduce suma por año. Repetimos esas operaciones para extranjeros, excluyendo PERÚ y NO ESPECIFICA. Al unir ambas ramas obtenemos el porcentaje. En 2024 son quinientas veintiocho personas extranjeras: dos coma setenta y dos por ciento del total. Como tercera agregación por clave, el programa suma por nacionalidad para explicar ese resultado. Venezuela encabeza el acumulado con dos mil ciento cincuenta y una personas.

### Lámina 10: 3c.3: distribución mensual

**Expone:** Arbués. **Tiempo objetivo:** 35 s.

> **Indicación, no leer:** Suma mensual, total nacional e índice. Después, el apoyo trimestral.

**Texto para decir:**

La tercera consulta decimal compara los meses con una distribución uniforme. Primero, Map emite mes y cantidad; después, Reduce suma por mes. Otro par Map y Reduce obtiene el total nacional. Con esos agregados calculamos la participación. La referencia es cien dividido entre doce: aproximadamente ocho coma treinta y tres por ciento. Diciembre representa nueve coma cuarenta y nueve por ciento y obtiene un índice de uno coma catorce. Una tercera agregación por clave resume los trimestres: el cuarto concentra treinta y dos mil seiscientas cincuenta personas.

> **Indicación, no leer:** Único cambio de expositor. Arbués dice la transición siguiente antes de pasar a la lámina 11.

**Texto para decir:**

Con esto termino el bloque de datos y consultas RDD. Ahora Sergio continuará con Spark SQL, el almacenamiento en PostgreSQL y la entrega de resultados.

### Lámina 11: 5a.1: groupBy y filter

**Expone:** Sergio. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** Las dos columnas de groupBy y el filtro posterior a la suma.

**Texto para decir:**

Para el punto cinco a mostramos un agrupamiento por dos columnas: departamento y rango de edad. La pregunta es qué combinaciones reúnen al menos mil personas. Sumamos CANTIDAD, filtramos los grupos y ordenamos. Obtenemos treinta combinaciones. La primera es Lima Metropolitana y doce a diecisiete años, con diecisiete mil novecientas una personas.

### Lámina 12: 5b.2: orden descendente

**Expone:** Sergio. **Tiempo objetivo:** 20 s.

> **Indicación, no leer:** orderBy descendente, limit y la primera fila.

**Texto para decir:**

Esta consulta responde cuáles son los quince distritos con mayor total reportado. Agrupamos por departamento, provincia y distrito, sumamos personas y ordenamos de mayor a menor. Finalmente aplicamos el límite. San Juan de Lurigancho encabeza el resultado con cuatro mil setecientas setenta y cinco personas.

### Lámina 13: 5c.1: INNER JOIN

**Expone:** Sergio. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** La clave ubigeo del JOIN y la suma por región.

**Texto para decir:**

En el punto cinco c usamos los cuatro tipos de unión. Aquí mostramos INNER JOIN. Unimos el agregado por ubigeo con la dimensión geográfica y después sumamos por región. Costa concentra setenta y nueve mil seiscientas sesenta y seis personas. La dimensión tiene una fila por clave, de modo que la unión conserva el total nacional sin multiplicar registros.

### Lámina 14: 5d.3: funciones estadísticas

**Expone:** Sergio. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** stddev_pop, variance, min y max en el código.

**Texto para decir:**

El punto cinco d aplica funciones de Spark. Esta consulta compara la dispersión de CANTIDAD por sexo. Calcula promedio, desviación, varianza y extremos. La desviación poblacional es aproximadamente uno coma cero cinco para mujeres y cero coma sesenta para hombres. Esto indica mayor variación entre los conteos femeninos por fila; no permite reconstruir incidentes individuales.

### Lámina 15: 5e.2: vistas temporales

**Expone:** Sergio. **Tiempo objetivo:** 30 s.

> **Indicación, no leer:** JOIN con edad, JOIN con sexo y el porcentaje nacional.

**Texto para decir:**

Para el punto cinco e consultamos vistas temporales. Unimos las denuncias con las dimensiones de edad y sexo y agrupamos por ambas categorías. La combinación más numerosa es adolescentes mujeres: cincuenta y un mil veintiuna personas. Al dividir entre el total nacional obtenemos cuarenta y dos coma cero dos por ciento. Esta respuesta muestra cómo los JOIN incorporan categorías útiles para interpretar la medida.

### Lámina 16: 5f.1: ordenar con filtros

**Expone:** Sergio. **Tiempo objetivo:** 25 s.

> **Indicación, no leer:** WHERE, HAVING y ORDER BY, en ese orden.

**Texto para decir:**

El punto cinco f combina ordenamiento y filtros. WHERE selecciona mujeres de 2024 con edad especificada. Después agrupamos por ubicación y HAVING exige al menos veinte personas por distrito. ORDER BY ordena el resultado y LIMIT conserva los diez primeros. San Juan de Lurigancho vuelve a encabezar, ahora con trescientas setenta y nueve personas dentro de este subconjunto.

### Lámina 17: Modelo estrella y ETL

**Expone:** Sergio. **Tiempo objetivo:** 60 s.

> **Indicación, no leer:** Tabla central, dimensiones, clave ubigeo y tres pasos del ETL.

**Texto para decir:**

En el punto cuatro almacenamos el dataset en PostgreSQL mediante un modelo estrella. La tabla central conserva cada registro y su cantidad. Ocho dimensiones describen tiempo, ubicación y perfil. Una clave de dimensión aparece una sola vez en su catálogo, pero puede relacionarse con muchos registros de la tabla central. El proceso ETL tiene tres pasos. Primero extraemos el CSV hacia una tabla de carga. Después transformamos vacíos y normalizamos claves. Finalmente cargamos dimensiones y hechos dentro de una transacción. Un caso importante es el ubigeo ciento cincuenta mil ciento veintiuno, que aparece con dos nombres. Lo agrupamos antes de unir para evitar duplicar datos. Las evidencias de carga y restauración conservan noventa y tres mil ochocientas treinta y una filas y ciento veintiún mil cuatrocientas veintisiete personas.

### Lámina 18: Exportación con Spark

**Expone:** Sergio. **Tiempo objetivo:** 40 s.

> **Indicación, no leer:** Proveedor spark-excel, cabecera, destino y tabla de seis hojas.

**Texto para decir:**

El punto cinco g pide guardar en Excel los resultados de cinco e y cinco f usando Spark. El script calcula las seis consultas y escribe un libro por consulta mediante el proveedor spark-excel. Configuramos la hoja, la cabecera y la ruta de salida. Los libros contienen sesenta, dieciocho, nueve, diez, doce y doce filas, respectivamente. La exportación de cinco e dos conserva las categorías, los totales y el promedio. La columna adicional de porcentaje nacional se muestra en la consulta individual de la presentación.

### Lámina 19: Cierre

**Expone:** Sergio. **Tiempo objetivo:** 40 s.

> **Indicación, no leer:** Las preguntas de la tabla y sus respuestas.

**Texto para decir:**

Como conclusión, cada resultado responde a una pregunta concreta. Identificamos la distribución territorial, la participación de menores y extranjeros, la distribución mensual y el perfil de edad y sexo. Estas respuestas se obtienen mediante operaciones que se pueden seguir desde las columnas originales hasta la salida. La base conserva el total contabilizado y los seis resultados de vistas temporales tienen su entrega en Excel. Las veinticinco consultas están documentadas y enlazadas para revisar cualquier detalle. Muchas gracias.

# Ampliaciones literales para ensayar o responder

Estos textos son reserva. Añadirlos al recorrido principal aumenta el tiempo de exposición.

### Dataset: por qué una fila no equivale a una persona

**Texto para decir:**

Podemos pensarlo como una tabla de frecuencias. Dos registros pueden compartir mes y distrito, pero tener distinto perfil. A su vez, una fila puede contener una cantidad mayor que uno. Por eso utilizamos la suma de CANTIDAD para hablar de personas contabilizadas y el conteo de filas para describir registros. No contamos con identificadores que permitan saber si una misma persona aparece en denuncias distintas.

### Qué hace map y qué hace reduce

**Texto para decir:**

Map transforma cada elemento en una representación útil. Por ejemplo, de una fila extraemos el departamento y la cantidad. Reduce combina valores; si la clave es departamento, reduceByKey suma las cantidades que comparten esa clave. Antes de combinar los valores, Spark puede redistribuir datos entre particiones. El diagrama explica las operaciones del programa; no fija cuántas tareas físicas o etapas ejecutará Spark.

### 3a: cómo se conserva el extremo

**Texto para decir:**

Para obtener máximo y mínimo por sexo emitimos dos copias del conteo en una tupla. Al combinar dos tuplas conservamos el mayor de sus máximos y el menor de sus mínimos. Así reducimos todas las filas de una categoría a un par de valores. Para conservar la ubicación utilizamos otra tupla que incluye territorio y fecha. Si hay empate, el código conserva un registro, no todos los registros empatados.

### 3b: media, mediana y desviación

**Texto para decir:**

La media usa toda la suma y por eso cambia cuando aparecen cantidades altas. La mediana ocupa el centro de la lista ordenada y representa mejor el valor típico cuando predominan unos pocos conteos. La desviación resume cuánto varían los valores alrededor de la media. El código calcula tanto desviación poblacional como muestral. Además aclaremos la terminología: media y promedio son sinónimos; la mediana es un estadístico distinto que añadimos a la descripción.

### 3c: cómo explicar las tres agregaciones existentes

**Texto para decir:**

En estas consultas mostramos tanto el indicador decimal como sus agregaciones de apoyo. En menores tenemos total por departamento, menores por departamento y control nacional. En extranjeros tenemos total anual, extranjeros por año y total por nacionalidad. En distribución mensual tenemos total por mes, total nacional y total trimestral. Esas operaciones aparecen en el código. El porcentaje se calcula después de agregar; no llamamos reducer a una división.

### 3c.1: el denominador cambia la respuesta

**Texto para decir:**

La consulta tres c uno divide menores entre el total reportado, incluyendo los registros cuya edad no está especificada. En cambio, cinco c cuatro compara únicamente menores y adultos con edad conocida. Por eso Lima Metropolitana aparece con cincuenta coma cero dos por ciento en la primera y cincuenta coma sesenta por ciento en la segunda. La diferencia proviene del denominador, no de una contradicción entre resultados.

### 3c.2: porcentaje y puntos porcentuales

**Texto para decir:**

El porcentaje anual responde cuánto representan los extranjeros dentro del total de ese año. Los puntos porcentuales comparan dos participaciones: dos coma setenta y dos menos dos coma ochenta y nueve da menos cero coma diecisiete puntos. Esto puede ocurrir aunque el conteo extranjero aumente de quinientas veinticinco a quinientas veintiocho personas, porque el total de personas reportadas también cambia.

### 3c.3: lectura del índice

**Texto para decir:**

Un índice igual a uno significa que la participación del mes coincide con la referencia uniforme. Un índice de uno coma catorce indica una participación alrededor de catorce por ciento superior a esa referencia. No significa catorce por ciento de todas las desapariciones: esa participación es nueve coma cuarenta y nueve por ciento. Es una comparación descriptiva del acumulado de seis años, sin ajuste por días del mes ni una prueba estadística de estacionalidad.

### DataFrame y SQL: dos formas de expresar operaciones

**Texto para decir:**

Con DataFrame expresamos operaciones mediante métodos de Scala, como groupBy, filter y join. Con las vistas temporales damos un nombre consultable a las tablas y escribimos SELECT, JOIN y GROUP BY dentro de spark.sql. Las dos formas trabajan dentro de Spark. No convertimos la consulta Spark SQL en una consulta de PostgreSQL: PostgreSQL es otra capa de almacenamiento y verificación del proyecto.

### Los cuatro JOIN del punto 5c

**Texto para decir:**

INNER conserva coincidencias. LEFT conserva todas las claves de la izquierda. RIGHT conserva todas las de la derecha. FULL OUTER conserva las de ambos lados, aunque falte una coincidencia. Elegimos esos comportamientos para responder preguntas diferentes: distribución regional, claves sin registros, meses faltantes y departamentos con menores o adultos. Unir no es suficiente: también debemos comprobar que la clave de una dimensión no esté repetida.

### WHERE, HAVING y ORDER BY

**Texto para decir:**

WHERE decide qué filas entran al análisis. GROUP BY forma los grupos y SUM calcula su medida. HAVING filtra usando esa medida ya calculada. ORDER BY determina en qué orden se presentan los grupos y LIMIT restringe la cantidad mostrada. Si filtráramos CANTIDAD mayor o igual a veinte antes de sumar, estaríamos respondiendo otra pregunta: buscaríamos filas grandes y no distritos con acumulados grandes.

### Modelo estrella y carga

**Texto para decir:**

El modelo estrella separa la medida de sus descripciones. La tabla de hechos mantiene la cantidad y las claves de las dimensiones. Los catálogos evitan repetir descripciones y permiten agrupar de forma consistente. La clave primaria impide repetir una clave de dimensión; las claves foráneas relacionan los hechos con esos catálogos. La transacción agrupa la carga, de forma que una falla no deje confirmada solamente una parte de las inserciones.

### Excel y las dos variantes de 5e.2

**Texto para decir:**

Los libros son resultados tabulares producidos por Spark, no capturas pegadas en un Excel. La consulta individual cinco e dos incluye un porcentaje nacional calculado con una ventana. El script de exportación utiliza una selección más corta y no incluye esa columna. Por eso, si abrimos el libro veremos los totales y promedios, mientras que el porcentaje de cuarenta y dos coma cero dos aparece en la salida de la consulta individual.

# Preguntas del docente: respuestas literales

### ¿Por qué no usan COUNT para hablar de personas?

**Texto para decir:**

Porque el dataset ya está agregado. COUNT cuenta combinaciones de tiempo, lugar y perfil; SUM de CANTIDAD agrega personas reportadas dentro de esas combinaciones. Para mostrar la estructura usamos COUNT y para medir el volumen reportado usamos SUM.

### ¿Dónde están las tres operaciones de 3c?

**Texto para decir:**

En la ficha distinguimos las agregaciones principales de la reducción de control o apoyo. Por ejemplo, en tres c dos agregamos total anual, extranjeros por año y extranjeros por nacionalidad. El porcentaje se calcula después con los agregados. Podemos abrir el script y señalar cada operación.

> **Indicación, no leer:** Si pide expresamente tres MapReduce consecutivos del indicador:

**Texto para decir:**

El código actual tiene esas agregaciones en ramas principales y de apoyo; no tres reducciones consecutivas del porcentaje. Para cumplir esa dependencia estricta habría que adaptar el flujo.

### ¿El dataset demuestra que las personas desaparecen juntas?

**Texto para decir:**

No. La fila agrega personas que comparten fecha mensual, territorio y perfil. Sin un identificador de incidente no podemos establecer que desaparecieron juntas. La cantidad mayor que uno describe el conteo del cruce agregado.

### ¿Cómo verifican que un JOIN no duplicó personas?

**Texto para decir:**

Comprobamos que el catálogo tenga una fila por clave y contrastamos la suma antes y después. El ubigeo con dos nombres se colapsa antes de unir. La salida del INNER JOIN y los controles de PostgreSQL conservan el total nacional.

### ¿Por qué la región no implica ruralidad?

**Texto para decir:**

Porque la región es una clasificación por departamento definida en el código. El dataset no incorpora una medida de ruralidad ni un denominador poblacional. Nuestro resultado describe la distribución según esa agrupación, sin convertirla en una medida de riesgo.

### ¿Por qué la base tiene 93831 hechos y no 121427?

**Texto para decir:**

Cada fila original se conserva como un registro de la tabla de hechos. La columna cantidad mantiene el número de personas de ese registro. Por eso el conteo de hechos reproduce las filas del CSV y la suma de cantidad reproduce las personas contabilizadas.

### ¿Esto demuestra procesamiento en varios equipos?

**Texto para decir:**

La demo utiliza Spark en modo local con dos hilos. Permite mostrar el programa y sus operaciones dentro del motor, pero no demuestra escalamiento entre varios equipos. Los diagramas describen el flujo lógico de los datos.

# Guion hablado para una demo de tres minutos

> **Indicación, no leer:** Preparar Spark y cargar el esquema antes. Los comandos están en DEMO.md. La demo se realiza si se concede tiempo adicional.

### Inicio y control de datos

**Texto para decir:**

Voy a mostrar cómo una pregunta de la presentación se obtiene desde los datos. Este entorno utiliza Spark tres punto tres punto dos. Primero comprobamos el número de filas y la suma de CANTIDAD. Así distinguimos los registros del total de personas contabilizadas.

### Una consulta RDD

**Texto para decir:**

Ahora cargo la consulta de menores por departamento. Aquí se suma el total territorial, aquí se filtran y suman los menores, y aquí se unen ambos resultados para calcular el porcentaje. La salida permite leer Madre de Dios y contrastarlo con la cifra de la presentación. Al final aparecen los controles nacionales.

### Una consulta Spark SQL

**Texto para decir:**

Después cargo la consulta de perfil por edad y sexo. Estas vistas permiten unir las denuncias con las categorías de edad y sexo. El resultado vuelve a mostrar adolescentes mujeres con cincuenta y un mil veintiuna personas y cuarenta y dos coma cero dos por ciento.

### Cierre de la demo

**Texto para decir:**

Con esto mostramos la entrada, las operaciones y la respuesta dentro del motor. Si desea revisar almacenamiento, podemos abrir la copia de PostgreSQL y contrastar el conteo y la suma. Los Excel existentes permiten revisar también la entrega tabular.

### Si hay un problema de entorno

**Texto para decir:**

En este momento el entorno de ejecución no está disponible. Puedo mostrar el script y la salida que quedó guardada de la ejecución anterior. Esta salida es evidencia previa; no la estoy presentando como una ejecución en vivo.
