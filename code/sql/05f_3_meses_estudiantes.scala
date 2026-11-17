// ============================================================================
// 5-f.3) spark.sql con ORDER BY combinado con filtros
// Pregunta: "En que meses del ano desaparecen mas estudiantes menores de 30
// anos? (estacionalidad del grupo escolarizado)"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-f.3) MESES CON MAS ESTUDIANTES DESAPARECIDOS (ORDER BY + filtros)")

val consulta = {
  spark.sql("""
    SELECT mes,
           SUM(cantidad)  AS personas,
           COUNT(*)       AS registros
    FROM denuncias
    WHERE ocupacion = 'ESTUDIANTE'
      AND rango_edad IN ('6-11 AÑOS', '12-17 AÑOS', '18-29 AÑOS')
    GROUP BY mes
    HAVING SUM(cantidad) > 500
    ORDER BY personas DESC, mes ASC
  """)
}

mostrar("5-f.3", consulta, 15)
