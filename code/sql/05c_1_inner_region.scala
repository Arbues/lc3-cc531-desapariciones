// ============================================================================
// 5-c.1) JOIN tipo INNER
// Pregunta: "Como se reparten las desapariciones entre las tres regiones
// naturales del pais, cruzando el hecho con el catalogo geografico?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-c.1) INNER JOIN: HECHOS x DIM_GEOGRAFIA -> TOTAL POR REGION NATURAL")

val hechosUbigeo = {
  denuncias.groupBy("ubigeo").agg(sum("cantidad").as("personas"), count("*").as("registros"))
}

val consulta = {
  hechosUbigeo
    .join(dimGeografia.select("ubigeo", "region"), Seq("ubigeo"), "inner")
    .groupBy("region")
    .agg(sum("personas").as("personas"), sum("registros").as("registros"), count("*").as("ubigeos"))
    .orderBy(desc("personas"))
}

mostrar("5-c.1 (inner)", consulta, 10)

println("")
println("[nota] los registros con ubigeo nulo quedan fuera del INNER JOIN: " +
  denuncias.filter(col("ubigeo").isNull).count() + " registro(s)")
