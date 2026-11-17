// ============================================================================
// 5-b.1) Consulta con PROMEDIO de una columna
// Pregunta: "En promedio, cuantas personas se reportan por cada hecho delictivo
// registrado en cada departamento? (intensidad promedio del registro)"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-b.1) PROMEDIO DE PERSONAS POR REGISTRO, POR DEPARTAMENTO")

val consulta = {
  denuncias
    .groupBy("departamento")
    .agg(
      count("*").as("registros"),
      sum("cantidad").as("personas"),
      round(avg("cantidad"), 3).as("promedio_personas_por_registro")
    )
    .orderBy(desc("promedio_personas_por_registro"))
}

mostrar("5-b.1", consulta, 30)
