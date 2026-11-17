// ============================================================================
// 5-c.3) JOIN tipo RIGHT OUTER
// Pregunta: "Existe algun mes del periodo 2019-2024 sin registros de
// desaparicion? Se contrasta el calendario completo (72 meses) contra los datos."
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-c.3) RIGHT JOIN: HECHOS x CALENDARIO (72 meses) -> COBERTURA TEMPORAL")

val hechosMes = {
  denuncias.groupBy("anio", "mes").agg(sum("cantidad").as("personas"))
}

val consulta = {
  hechosMes
    .join(calendario, Seq("anio", "mes"), "right_outer")
    .withColumn("personas", coalesce(col("personas"), lit(0)))
    .orderBy("anio", "mes")
}

val sinDatos = { consulta.filter(col("personas") === 0) }

println("")
println("[control] meses en el calendario : " + consulta.count())
println("[control] meses sin registros    : " + sinDatos.count())
println("[control] personas en el periodo : " + consulta.agg(sum("personas")).first().getLong(0))

mostrar("5-c.3 (right_outer) meses del calendario sin registros", sinDatos, 20)
