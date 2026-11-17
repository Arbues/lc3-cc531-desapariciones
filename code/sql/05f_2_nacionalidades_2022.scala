// ============================================================================
// 5-f.2) spark.sql con ORDER BY combinado con filtros
// Pregunta: "Que nacionalidades extranjeras crecieron mas desde 2022, y en que
// anos se concentran sus registros?"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-f.2) NACIONALIDADES EXTRANJERAS 2022-2024 (ORDER BY + filtros)")

val consulta = {
  spark.sql("""
    SELECT nacionalidad, anio,
           SUM(cantidad)            AS personas,
           COUNT(*)                 AS registros,
           ROUND(AVG(cantidad), 3)  AS promedio
    FROM denuncias
    WHERE nacionalidad NOT IN ('PERÚ', 'NO ESPECIFICA')
      AND anio >= 2022
    GROUP BY nacionalidad, anio
    HAVING SUM(cantidad) >= 10
    ORDER BY personas DESC, anio ASC
  """)
}

mostrar("5-f.2", consulta, 30)
