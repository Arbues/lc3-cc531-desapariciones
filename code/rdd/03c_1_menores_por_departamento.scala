// ============================================================================
// 3-c.1) Consulta con DECIMALES y TRES mapReduce encadenados.
//
// Pregunta de negocio: "Que porcentaje de las desapariciones de cada
// departamento corresponde a menores de 18 anos, y que departamentos
// concentran el mayor peso relativo de poblacion infantil y adolescente?"
//
// Cadena:  MR1 total por departamento
//          MR2 menores de edad por departamento
//          MR3 join de los dos agregados -> porcentaje con decimales
//
// Ejecutar:  ./bin/ejecutar.sh code/rdd/03c_1_menores_por_departamento.scala
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("3-c.1) % DE MENORES DE 18 AÑOS POR DEPARTAMENTO  (3 mapReduce encadenados)")

// --- MR1: total de personas desaparecidas por departamento -----------------
val mr1Total = {
  hechos.map(h => (h.departamento, h.cantidad)).reduceByKey(_ + _)
}
println("")
println("[MR1] departamentos con total calculado: " + mr1Total.count())

// --- MR2: total de menores de 18 anos por departamento ---------------------
val mr2Menores = {
  hechos
    .filter(h => Lc3.MENORES.contains(h.rangoEdad))
    .map(h => (h.departamento, h.cantidad))
    .reduceByKey(_ + _)
}
println("[MR2] departamentos con menores calculados: " + mr2Menores.count())

// --- MR3: join de MR1 con MR2 -> porcentaje con 2 decimales ----------------
val mr3Porcentaje = {
  mr1Total
    .join(mr2Menores)
    .map { caso =>
      val (dpto, (total, menores)) = caso
      (dpto, total, menores, dosDec(menores * 100.0 / total))
    }
    .sortBy(caso => -caso._4)
    .collect()
}

println("")
println("[MR3] departamento | total | menores de 18 | % menores")
mr3Porcentaje.foreach { caso =>
  println("  " + caso._1.padTo(22, ' ') + " total=" + caso._2.toString.padTo(7, ' ') +
    " menores=" + caso._3.toString.padTo(7, ' ') + " %=" + caso._4)
}

// --- Total nacional (control de cuadre) -----------------------------------
val totalNacional = mr1Total.values.reduce(_ + _)
val menoresNacional = mr2Menores.values.reduce(_ + _)
println("")
println("[control] total nacional=" + totalNacional +
  "  menores=" + menoresNacional +
  "  % nacional=" + dosDec(menoresNacional * 100.0 / totalNacional))

println("")
println("[3-c.1] FIN")
