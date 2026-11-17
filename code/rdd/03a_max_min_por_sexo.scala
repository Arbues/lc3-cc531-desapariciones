// ============================================================================
// 3-a) Agrupar un campo por tipos y, sobre un campo numerico, obtener los
//      mayores y menores valores -- con mapReduce (RDD).
//
// Pregunta de negocio: "Para cada sexo, cual es el mayor y el menor numero de
// personas desaparecidas registrado en un solo hecho, y donde ocurrio?"
//
// Ejecutar:  ./bin/ejecutar.sh code/rdd/03a_max_min_por_sexo.scala
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("3-a) MAXIMO Y MINIMO DE CANTIDAD POR SEXO  (mapReduce / RDD)")

// --- JOB 1: map -> (sexo, (cantidad, cantidad)) ; reduce -> (max, min) ------
val extremos = {
  hechos
    .map(h => (h.sexo, (h.cantidad, h.cantidad)))
    .reduceByKey((a, b) => (math.max(a._1, b._1), math.min(a._2, b._2)))
    .sortByKey()
    .collect()
}

println("")
println("[job 1] sexo | maximo | minimo")
extremos.foreach { par =>
  println("  " + par._1.padTo(14, ' ') + " max=" + par._2._1 + "  min=" + par._2._2)
}

// --- JOB 2: map -> (sexo, tupla de primitivos) ; reduce -> registro MAYOR ---
// (se usa una tupla de tipos primitivos: las clases de caso declaradas en el
//  REPL no son serializables al pasar por un shuffle)
val registroMax = {
  hechos
    .map(h => (h.sexo, (h.cantidad, h.departamento, h.provincia, h.distrito, h.anio, h.mes)))
    .reduceByKey((a, b) => if (a._1 >= b._1) a else b)
    .sortByKey()
    .collect()
}

println("")
println("[job 2] registro con MAYOR cantidad por sexo:")
registroMax.foreach { par =>
  val h = par._2
  println("  " + par._1.padTo(4, ' ') + " cantidad=" + h._1 + "  -> " +
    h._2 + " / " + h._3 + " / " + h._4 + "  (" + h._5 + "-" + h._6 + ")")
}

// --- JOB 3: map -> (sexo, tupla de primitivos) ; reduce -> registro MENOR ---
val registroMin = {
  hechos
    .map(h => (h.sexo, (h.cantidad, h.departamento, h.provincia, h.distrito, h.anio, h.mes)))
    .reduceByKey((a, b) => if (a._1 <= b._1) a else b)
    .sortByKey()
    .collect()
}

println("")
println("[job 3] registro con MENOR cantidad por sexo:")
registroMin.foreach { par =>
  val h = par._2
  println("  " + par._1.padTo(4, ' ') + " cantidad=" + h._1 + "  -> " +
    h._2 + " / " + h._3 + " / " + h._4 + "  (" + h._5 + "-" + h._6 + ")")
}

// --- Apoyo para la interpretacion: distribucion del numero de personas ------
val distribucion = {
  hechos
    .map(h => (h.cantidad, 1))
    .reduceByKey(_ + _)
    .sortByKey()
    .take(8)
}

println("")
println("[apoyo] personas por registro -> cuantos registros existen")
distribucion.foreach { par => println("  cantidad=" + par._1 + " -> registros=" + par._2) }

println("")
println("[3-a] FIN")
