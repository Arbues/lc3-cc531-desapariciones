// ============================================================================
// 5-a.2) groupBy + filter con 2 columnas
// Pregunta: "En que anos las desapariciones de mujeres superaron las 5,000
// personas, y cual fue el total de esa categoria?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-a.2) AÑOS CON MAS DE 5,000 MUJERES DESAPARECIDAS")

val consulta = {
  denuncias
    .groupBy("anio", "sexo")
    .agg(sum("cantidad").as("personas"), count("*").as("registros"))
    .filter(col("sexo") === "F" && col("personas") > 5000)
    .orderBy("anio")
}

mostrar("5-a.2", consulta, 20)
