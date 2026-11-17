// ============================================================================
// 5-b.2) Consulta con ORDENAMIENTO DECRECIENTE
// Pregunta: "Cuales son los 15 distritos con mas personas desaparecidas en todo
// el periodo, ordenados de mayor a menor?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-b.2) TOP 15 DISTRITOS POR PERSONAS DESAPARECIDAS (orden descendente)")

val consulta = {
  denuncias
    .groupBy("departamento", "provincia", "distrito")
    .agg(sum("cantidad").as("personas"), count("*").as("registros"))
    .orderBy(desc("personas"))
    .limit(15)
}

mostrar("5-b.2", consulta, 15)
