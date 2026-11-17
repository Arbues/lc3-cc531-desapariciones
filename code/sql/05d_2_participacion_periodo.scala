// ============================================================================
// 5-d.2) Uso de functions._ (sum / col / lit / round / desc)
// Pregunta: "Que peso porcentual tiene cada periodo pandemico sobre el total
// nacional de personas desaparecidas?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-d.2) PARTICIPACION PORCENTUAL DE CADA PERIODO SOBRE EL TOTAL NACIONAL")

val totalNacional = { denuncias.agg(sum("cantidad")).first().getLong(0) }
println("")
println("[base] total nacional de personas: " + totalNacional)

val consulta = {
  denuncias
    .groupBy("periodo")
    .agg(sum("cantidad").as("personas"), count("*").as("registros"))
    .withColumn("pct_nacional", round(col("personas") * 100.0 / lit(totalNacional), 2))
    .withColumn("personas_por_registro", round(col("personas") / col("registros"), 3))
    .orderBy(desc("pct_nacional"))
}

mostrar("5-d.2", consulta, 10)
