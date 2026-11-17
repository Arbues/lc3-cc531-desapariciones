// ============================================================================
// 5-e.3) VISTAS TEMPORALES + spark.sql, con JOIN triple
// Pregunta: "Cual es la evolucion por region natural y periodo pandemico, y que
// region gana o pierde participacion respecto al periodo anterior?"
//
// Nota tecnica: CAST(... AS DOUBLE) para evitar DecimalType en ROUND
// (bug de compilacion del REPL de spark-shell: Scala 2.12.15 + JDK 11).
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-e.3) REGION x PERIODO CON JOIN TRIPLE (hechos + geografia + tiempo)")

val consulta = {
  spark.sql("""
    SELECT g.region,
           t.periodo,
           SUM(d.cantidad)                                              AS personas,
           ROUND(CAST(AVG(d.cantidad) AS DOUBLE), 3)                    AS promedio_registro,
           ROUND(CAST(SUM(d.cantidad) AS DOUBLE) * 100.0 /
                 SUM(SUM(d.cantidad)) OVER (PARTITION BY t.periodo), 2) AS pct_del_periodo
    FROM denuncias d
    JOIN dim_geografia g ON d.ubigeo = g.ubigeo
    JOIN dim_tiempo    t ON d.anio   = t.anio
    GROUP BY g.region, t.periodo
    ORDER BY t.periodo, personas DESC
  """)
}

mostrar("5-e.3", consulta, 20)

println("")
println("[control] total de personas en la consulta (debe ser 121,427): " +
  consulta.agg(sum("personas")).first().get(0))
