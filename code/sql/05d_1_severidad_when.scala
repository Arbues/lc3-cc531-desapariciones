// ============================================================================
// 5-d.1) Uso de org.apache.spark.sql.functions._ (when / otherwise)
// Pregunta: "Como se clasifican los hechos segun su severidad (numero de
// personas por registro) y cuantas personas aporta cada nivel?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-d.1) CLASIFICACION DE SEVERIDAD CON when/otherwise")

val consulta = {
  denuncias
    .withColumn("severidad",
      when(col("cantidad") >= 5, "FOCO (5 o mas)")
        .when(col("cantidad") >= 3, "ALTO (3-4)")
        .otherwise("INDIVIDUAL (1-2)"))
    .groupBy("severidad")
    .agg(count("*").as("registros"), sum("cantidad").as("personas"),
         round(avg("cantidad"), 2).as("promedio"))
    .orderBy(desc("personas"))
}

mostrar("5-d.1", consulta, 10)
