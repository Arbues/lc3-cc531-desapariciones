// ============================================================================
// 5-f.1) spark.sql con ORDER BY combinado con filtros
// Pregunta: "Cuales fueron los 10 distritos con mas mujeres desaparecidas en el
// ultimo ano registrado (2024), excluyendo el sexo no especificado?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-f.1) TOP 10 DISTRITOS - MUJERES 2024 (ORDER BY + WHERE)")

val consulta = {
  spark.sql("""
    SELECT departamento, provincia, distrito,
           SUM(cantidad) AS personas,
           COUNT(*)      AS registros
    FROM denuncias
    WHERE anio = 2024
      AND sexo = 'F'
      AND rango_edad <> 'NO ESPECIFICA'
    GROUP BY departamento, provincia, distrito
    HAVING SUM(cantidad) >= 20
    ORDER BY personas DESC
    LIMIT 10
  """)
}

mostrar("5-f.1", consulta, 10)
