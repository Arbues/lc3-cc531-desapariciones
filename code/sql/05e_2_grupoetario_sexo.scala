// ============================================================================
// 5-e.2) VISTAS TEMPORALES + spark.sql, con dos JOIN (dim_edad y dim_sexo)
// Pregunta: "Que grupo etario y sexo concentran mas personas desaparecidas, y
// como se ordenan las categorias dentro de cada grupo?"
//
// Nota tecnica: CAST(... AS DOUBLE) evita la aritmetica decimal (DecimalType),
// que rompe la compilacion del REPL en spark-shell con Scala 2.12.15 / JDK 11.
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-e.2) GRUPO ETARIO x SEXO (dos joins con vistas temporales)")

val consulta = {
  spark.sql("""
    SELECT e.grupo_etario,
           s.sexo,
           SUM(d.cantidad)                                            AS personas,
           COUNT(*)                                                   AS registros,
           ROUND(CAST(AVG(d.cantidad) AS DOUBLE), 3)                  AS promedio_por_registro,
           ROUND(CAST(SUM(d.cantidad) AS DOUBLE) * 100.0 /
                 SUM(SUM(d.cantidad)) OVER (), 2)                     AS pct_nacional
    FROM denuncias d
    JOIN dim_edad e ON d.rango_edad = e.rango_edad
    JOIN dim_sexo s ON d.sexo = s.sexo
    GROUP BY e.grupo_etario, s.sexo
    ORDER BY personas DESC
  """)
}

mostrar("5-e.2", consulta, 30)
