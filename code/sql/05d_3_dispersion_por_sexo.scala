// ============================================================================
// 5-d.3) Uso de functions._ (stddev_pop / variance / min / max / round)
// Pregunta: "La dispersion del numero de personas por registro es igual en
// hombres y mujeres? Cual grupo tiene registros mas homogeneos?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-d.3) DISPERSION DE LA CANTIDAD POR REGISTRO SEGUN SEXO")

val consulta = {
  denuncias
    .groupBy("sexo")
    .agg(
      count("*").as("registros"),
      sum("cantidad").as("personas"),
      round(avg("cantidad"), 4).as("promedio"),
      round(stddev_pop("cantidad"), 4).as("desv_poblacional"),
      round(variance("cantidad"), 4).as("varianza_muestral"),
      min("cantidad").as("minimo"),
      max("cantidad").as("maximo")
    )
    .orderBy(desc("personas"))
}

mostrar("5-d.3", consulta, 10)
