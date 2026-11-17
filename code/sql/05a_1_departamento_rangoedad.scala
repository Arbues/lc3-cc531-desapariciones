// ============================================================================
// 5-a.1) groupBy + filter con 2 columnas
// Pregunta: "Que combinaciones departamento + rango de edad concentran mas de
// 1,000 personas desaparecidas en el periodo 2019-2024?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-a.1) DEPARTAMENTO x RANGO DE EDAD CON MAS DE 1,000 PERSONAS")

val consulta = {
  denuncias
    .groupBy("departamento", "rango_edad")
    .agg(sum("cantidad").as("personas"))
    .filter(col("personas") >= 1000)
    .orderBy(desc("personas"))
}

mostrar("5-a.1", consulta, 40)
