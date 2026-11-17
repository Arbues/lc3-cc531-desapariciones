// ============================================================================
// 5-c.4) JOIN tipo FULL OUTER
// Pregunta: "Todos los departamentos tienen tanto victimas menores de edad como
// adultas? El FULL OUTER revela departamentos que solo aparecen en uno de los
// dos agregados."
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-c.4) FULL OUTER JOIN: MENORES vs ADULTOS POR DEPARTAMENTO")

val menores = {
  denuncias
    .filter(col("rango_edad") === "0-5 AÑOS" || col("rango_edad") === "6-11 AÑOS" || col("rango_edad") === "12-17 AÑOS")
    .groupBy("departamento")
    .agg(sum("cantidad").as("menores_18"))
}

val adultos = {
  denuncias
    .filter(col("rango_edad") === "18-29 AÑOS" || col("rango_edad") === "30-59 AÑOS" || col("rango_edad") === "60 A MAS AÑOS")
    .groupBy("departamento")
    .agg(sum("cantidad").as("adultos"))
}

val consulta = {
  menores
    .join(adultos, Seq("departamento"), "full_outer")
    .withColumn("menores_18", coalesce(col("menores_18"), lit(0)))
    .withColumn("adultos",    coalesce(col("adultos"),    lit(0)))
    .withColumn("total",      col("menores_18") + col("adultos"))
    .withColumn("pct_menores", round(col("menores_18") * 100.0 / col("total"), 2))
    .orderBy(desc("total"))
}

mostrar("5-c.4 (full_outer) menores vs adultos por departamento", consulta, 30)

println("")
println("[nota] departamentos presentes solo en un agregado: " +
  consulta.filter(col("menores_18") === 0 || col("adultos") === 0).count())
