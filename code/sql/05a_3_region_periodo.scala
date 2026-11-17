// ============================================================================
// 5-a.3) groupBy + filter con 2 columnas
// Pregunta: "Como cambia el volumen de desapariciones entre regiones naturales
// y periodos (pre-pandemia, pandemia, post-pandemia), y en que combinaciones
// se superan las 10,000 personas?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-a.3) REGION NATURAL x PERIODO CON MAS DE 10,000 PERSONAS")

val consulta = {
  denuncias
    .groupBy("region", "periodo")
    .agg(sum("cantidad").as("personas"), count("*").as("registros"))
    .filter(col("personas") > 10000)
    .orderBy(desc("personas"))
}

mostrar("5-a.3", consulta, 20)
