// ============================================================================
// 3-c.3) Consulta con DECIMALES y TRES mapReduce encadenados.
//
// Pregunta de negocio: "Existe estacionalidad en las desapariciones? Cual es el
// indice de cada mes respecto al promedio mensual esperado (8.33%) y cuanto se
// desvia en puntos porcentuales?"
//
// Cadena:  MR1 personas por mes
//          MR2 total nacional (una sola clave)
//          MR3 participacion decimal + desviacion respecto a 1/12
//
// Ejecutar:  ./bin/ejecutar.sh code/rdd/03c_3_estacionalidad_mensual.scala
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("3-c.3) ESTACIONALIDAD MENSUAL DE LAS DESAPARICIONES  (3 mapReduce encadenados)")

// --- MR1: personas por mes -------------------------------------------------
val mr1PorMes = {
  hechos.map(h => (h.mes, h.cantidad)).reduceByKey(_ + _)
}
println("")
println("[MR1] meses con datos: " + mr1PorMes.count())

// --- MR2: total nacional en un solo reduce ---------------------------------
val mr2Total = {
  hechos.map(h => ("TOTAL", h.cantidad)).reduceByKey(_ + _)
}
val totalNacional = mr2Total.first()._2
println("[MR2] total nacional de personas: " + totalNacional)

// --- MR3: indice de estacionalidad ----------------------------------------
val esperado = 100.0 / 12.0   // 8.3333 %
val mr3Indice = {
  mr1PorMes
    .map { caso =>
      val (mes, personas) = caso
      val participacion = personas * 100.0 / totalNacional
      (mes, personas, dosDec(participacion), dosDec(participacion - esperado),
        dosDec(participacion / esperado))
    }
    .sortBy(caso => caso._1)
    .collect()
}

println("")
println("[MR3] mes | personas | % del total | desviacion vs 8.33% (pp) | indice (1.00 = normal)")
mr3Indice.foreach { caso =>
  val mes = if (caso._1 < 10) "0" + caso._1 else "" + caso._1
  println("  " + mes + "  personas=" + caso._2.toString.padTo(7, ' ') +
    " %=" + caso._3.toString.padTo(7, ' ') +
    " desv=" + caso._4.toString.padTo(7, ' ') +
    " indice=" + caso._5)
}

// --- Apoyo: comportamiento por trimestre (interpretacion) -----------------
val porTrimestre = {
  hechos
    .map(h => (h.anio, h.mes, h.cantidad))
    .map { caso =>
      val trimestre = ((caso._2 - 1) / 3) + 1
      (trimestre, caso._3)
    }
    .reduceByKey(_ + _)
    .sortByKey()
    .collect()
}

println("")
println("[apoyo] personas por trimestre (todo el periodo)")
porTrimestre.foreach { caso =>
  println("  T" + caso._1 + " -> " + caso._2 + "  (" + dosDec(caso._2 * 100.0 / totalNacional) + "%)")
}

println("")
println("[3-c.3] FIN")
