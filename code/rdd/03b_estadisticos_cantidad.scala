// ============================================================================
// 3-b) Promedio, mediana ("media"), desviacion estandar, mayor y menor de un
//      campo numerico -- todo con mapReduce (map + reduce, sin groupBy).
//
// Pregunta de negocio: "Cuantas personas desaparecen en promedio en un hecho
// registrado? Cual es el valor tipico (mediana) y cuan dispersa es la serie?"
//
// Ejecutar:  ./bin/ejecutar.sh code/rdd/03b_estadisticos_cantidad.scala
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("3-b) PROMEDIO, MEDIANA, DESVIACION, MAYOR Y MENOR DE CANTIDAD  (mapReduce)")

// --- JOB 1: (suma, conteo) en un solo reduce -------------------------------
val sumaConteo = {
  hechos.map(h => (h.cantidad.toDouble, 1L)).reduce((a, b) => (a._1 + b._1, a._2 + b._2))
}
val n        = sumaConteo._2
val suma     = sumaConteo._1
val promedio = suma / n

// --- JOB 2: suma de cuadrados para la desviacion ---------------------------
val sumaCuadrados = {
  hechos.map(h => h.cantidad.toDouble * h.cantidad.toDouble).reduce(_ + _)
}

val varianzaPoblacional = (sumaCuadrados / n) - (promedio * promedio)
val varianzaMuestral    = (sumaCuadrados - n * promedio * promedio) / (n - 1)
val desvPoblacional     = math.sqrt(varianzaPoblacional)
val desvMuestral        = math.sqrt(varianzaMuestral)

// --- JOB 3: mediana por ordenamiento + indice (dos reduces encadenados) -----
val ordenados = {
  hechos.map(_.cantidad.toDouble).sortBy(x => x).zipWithIndex().cache()
}
val medios = {
  ordenados
    .filter { par => par._2 == n / 2 || par._2 == (n - 1) / 2 }
    .map(_._1)
    .collect()
}
val mediana = medios.sum / medios.length

// --- JOB 4: mayor y menor --------------------------------------------------
val mayor = { hechos.map(_.cantidad).reduce(math.max) }
val menor = { hechos.map(_.cantidad).reduce(math.min) }

println("")
println("[resultado global sobre CANTIDAD]")
println("  registros (n)           : " + n)
println("  suma total de personas  : " + suma.toLong)
println("  promedio                : " + cuatroDec(promedio))
println("  mediana (media)         : " + cuatroDec(mediana))
println("  desviacion poblacional  : " + cuatroDec(desvPoblacional))
println("  desviacion muestral     : " + cuatroDec(desvMuestral))
println("  varianza poblacional    : " + cuatroDec(varianzaPoblacional))
println("  mayor                   : " + mayor)
println("  menor                   : " + menor)

// --- Apoyo: los mismos estadisticos por sexo (segundo nivel de analisis) ----
val porSexo = {
  hechos
    .map(h => (h.sexo, (h.cantidad.toDouble, 1L, h.cantidad.toDouble * h.cantidad.toDouble)))
    .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2, a._3 + b._3))
    .sortByKey()
    .collect()
}

println("")
println("[apoyo] por sexo: n | promedio | desviacion poblacional")
porSexo.foreach { par =>
  val (s, (suma, cnt, sc)) = par
  val prom = suma / cnt
  val desv = math.sqrt((sc / cnt) - prom * prom)
  println("  " + s.padTo(14, ' ') + " n=" + cnt + "  promedio=" + cuatroDec(prom) + "  desv=" + cuatroDec(desv))
}

println("")
println("[3-b] FIN")
