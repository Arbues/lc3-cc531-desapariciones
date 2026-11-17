// ============================================================================
// 3-c.2) Consulta con DECIMALES y TRES mapReduce encadenados.
//
// Pregunta de negocio: "Que porcentaje de las denuncias por desaparicion
// corresponde a personas de nacionalidad extranjera en cada ano, y como varia
// ese porcentaje de un ano a otro (puntos porcentuales)?"
//
// Cadena:  MR1 total de personas por ano
//          MR2 personas extranjeras por ano
//          MR3 join -> porcentaje decimal + variacion interanual
//
// Ejecutar:  ./bin/ejecutar.sh code/rdd/03c_2_extranjeros_por_anio.scala
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("3-c.2) % DE PERSONAS EXTRANJERAS POR AÑO  (3 mapReduce encadenados)")

// --- MR1: total por ano ----------------------------------------------------
val mr1Total = {
  hechos.map(h => (h.anio, h.cantidad)).reduceByKey(_ + _)
}
println("")
println("[MR1] anios procesados: " + mr1Total.count())

// --- MR2: extranjeros por ano ---------------------------------------------
val mr2Extranjeros = {
  hechos
    .filter(h => h.nacionalidad != Lc3.EXTRANJERO && h.nacionalidad != "NO ESPECIFICA")
    .map(h => (h.anio, h.cantidad))
    .reduceByKey(_ + _)
}
println("[MR2] anios con extranjeros: " + mr2Extranjeros.count())

// --- MR3: join de ambos agregados -> porcentaje decimal --------------------
val mr3Porcentaje = {
  mr1Total
    .join(mr2Extranjeros)
    .map { caso =>
      val (anio, (total, extranjeros)) = caso
      (anio, total, extranjeros, dosDec(extranjeros * 100.0 / total))
    }
    .sortBy(caso => caso._1)
    .collect()
}

println("")
println("[MR3] anio | total | extranjeros | % extranjeros | variacion (puntos porcentuales)")
var anterior: Option[Double] = None
mr3Porcentaje.foreach { caso =>
  val variacion = anterior match {
    case Some(p) => dosDec(caso._4 - p).toString
    case None    => "-"
  }
  println("  " + caso._1 + "  total=" + caso._2.toString.padTo(7, ' ') +
    " extranjeros=" + caso._3.toString.padTo(6, ' ') +
    " %=" + caso._4.toString.padTo(7, ' ') + " var=" + variacion)
  anterior = Some(caso._4)
}

// --- Apoyo: que nacionalidades explican el fenomeno -----------------------
val topNacionalidades = {
  hechos
    .filter(h => h.nacionalidad != Lc3.EXTRANJERO && h.nacionalidad != "NO ESPECIFICA")
    .map(h => (h.nacionalidad, h.cantidad))
    .reduceByKey(_ + _)
    .sortBy(caso => -caso._2)
    .take(8)
}

println("")
println("[apoyo] top nacionalidades extranjeras (personas)")
topNacionalidades.foreach { caso => println("  " + caso._1.padTo(20, ' ') + caso._2) }

println("")
println("[3-c.2] FIN")
