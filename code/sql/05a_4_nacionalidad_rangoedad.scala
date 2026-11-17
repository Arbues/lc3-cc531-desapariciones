// ============================================================================
// 5-a.4) groupBy + filter con 2 columnas
// Pregunta: "Que nacionalidades extranjeras y rangos de edad superan las 30
// personas desaparecidas, y en que perfil etario se concentran?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-a.4) NACIONALIDAD EXTRANJERA x RANGO DE EDAD (>= 30 PERSONAS)")

val consulta = {
  denuncias
    .filter(col("nacionalidad") =!= "PERÚ" && col("nacionalidad") =!= "NO ESPECIFICA")
    .groupBy("nacionalidad", "rango_edad")
    .agg(sum("cantidad").as("personas"))
    .filter(col("personas") >= 30)
    .orderBy(desc("personas"))
}

mostrar("5-a.4", consulta, 25)
