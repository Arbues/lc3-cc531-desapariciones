// ============================================================================
// 5-d.4) Uso de functions._ (to_date / concat_ws / lpad / quarter / year)
// Pregunta: "En que trimestre calendario se concentran mas desapariciones,
// construyendo la fecha completa a partir de los campos anio y mes?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-d.4) CONSTRUCCION DE FECHA Y AGREGACION POR TRIMESTRE")

val conFecha = {
  denuncias
    .withColumn("fecha", to_date(concat_ws("-", col("anio"), lpad(col("mes"), 2, "0"), lit("01"))))
    .withColumn("anio_trimestre", concat(col("anio"), lit("-T"), quarter(col("fecha"))))
}

val consulta = {
  conFecha
    .groupBy("anio_trimestre")
    .agg(sum("cantidad").as("personas"), count("*").as("registros"))
    .orderBy("anio_trimestre")
}

println("")
println("[control] fecha minima y maxima construida: " +
  conFecha.agg(min("fecha"), max("fecha")).first())

mostrar("5-d.4", consulta, 30)
