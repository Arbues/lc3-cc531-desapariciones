// ============================================================================
// 5-e.1) VISTAS TEMPORALES + spark.sql, con JOIN (self-join temporal)
// Pregunta: "Cuantos meses tuvieron mas desapariciones que el mismo mes del ano
// anterior, y cual fue la variacion porcentual?"
//
// Nota tecnica: las columnas derivadas se calculan con CAST(... AS DOUBLE)
// porque ROUND sobre aritmetica decimal (DecimalType) rompe la compilacion en
// spark-shell con Scala 2.12.15 sobre JDK 11.
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-e.1) VARIACION INTERANUAL POR MES (self-join sobre vista temporal)")

val consulta = {
  spark.sql("""
    SELECT a.anio,
           a.mes,
           a.personas AS personas_anio,
           b.personas AS personas_anio_anterior,
           ROUND(CAST(a.personas - b.personas AS DOUBLE) * 100.0 / b.personas, 2) AS variacion_pct
    FROM (SELECT anio, mes, SUM(cantidad) AS personas FROM denuncias GROUP BY anio, mes) a
    JOIN (SELECT anio, mes, SUM(cantidad) AS personas FROM denuncias GROUP BY anio, mes) b
      ON a.mes = b.mes AND a.anio = b.anio + 1
    ORDER BY a.anio, a.mes
  """)
}

mostrar("5-e.1", consulta, 60)

println("")
println("[lectura] meses con subida y con bajada respecto al ano anterior:")
val tendencia = {
  consulta
    .withColumn("tendencia", when(col("variacion_pct") > 0, "SUBE").otherwise("BAJA"))
    .groupBy("tendencia")
    .agg(count("*").as("meses"), round(avg("variacion_pct"), 2).as("variacion_media"))
    .orderBy("tendencia")
}
tendencia.show(false)
