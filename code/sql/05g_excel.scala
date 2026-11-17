// ============================================================================
// 5-g) Guardar en Excel los resultados de las consultas 5e (a,b,c) y 5f (a,b,c)
//      usando Spark + com.crealytics:spark-excel (no pandas, no openpyxl).
//
// Ejecutar:
//   ./bin/ejecutar.sh code/sql/05g_excel.scala lib/spark-excel_2.12-3.3.1_0.18.7.jar
//
// Genera un .xlsx por consulta en excel/ con una hoja por resultado.
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

import org.apache.spark.sql.SaveMode

titulo("5-g) EXPORTACION A EXCEL CON SPARK-EXCEL")

val carpetaExcel = RAIZ + "/excel"

// ---------------------------------------------------------------------------
// Resultados de 5-e (vistas temporales con join)
// ---------------------------------------------------------------------------
val res5e1 = {
  spark.sql("""
    SELECT a.anio, a.mes, a.personas AS personas_anio,
           b.personas AS personas_anio_anterior,
           ROUND(CAST(a.personas - b.personas AS DOUBLE) * 100.0 / b.personas, 2) AS variacion_pct
    FROM (SELECT anio, mes, SUM(cantidad) AS personas FROM denuncias GROUP BY anio, mes) a
    JOIN (SELECT anio, mes, SUM(cantidad) AS personas FROM denuncias GROUP BY anio, mes) b
      ON a.mes = b.mes AND a.anio = b.anio + 1
    ORDER BY a.anio, a.mes
  """)
}

val res5e2 = {
  spark.sql("""
    SELECT e.grupo_etario, s.sexo, SUM(d.cantidad) AS personas, COUNT(*) AS registros,
           ROUND(CAST(AVG(d.cantidad) AS DOUBLE), 3) AS promedio_por_registro
    FROM denuncias d
    JOIN dim_edad e ON d.rango_edad = e.rango_edad
    JOIN dim_sexo s ON d.sexo = s.sexo
    GROUP BY e.grupo_etario, s.sexo
    ORDER BY personas DESC
  """)
}

val res5e3 = {
  spark.sql("""
    SELECT g.region, t.periodo, SUM(d.cantidad) AS personas,
           ROUND(CAST(AVG(d.cantidad) AS DOUBLE), 3) AS promedio_registro,
           ROUND(CAST(SUM(d.cantidad) AS DOUBLE) * 100.0 /
                 SUM(SUM(d.cantidad)) OVER (PARTITION BY t.periodo), 2) AS pct_del_periodo
    FROM denuncias d
    JOIN dim_geografia g ON d.ubigeo = g.ubigeo
    JOIN dim_tiempo    t ON d.anio   = t.anio
    GROUP BY g.region, t.periodo
    ORDER BY t.periodo, personas DESC
  """)
}

// ---------------------------------------------------------------------------
// Resultados de 5-f (orderBy + filtros)
// ---------------------------------------------------------------------------
val res5f1 = {
  spark.sql("""
    SELECT departamento, provincia, distrito, SUM(cantidad) AS personas, COUNT(*) AS registros
    FROM denuncias
    WHERE anio = 2024 AND sexo = 'F' AND rango_edad <> 'NO ESPECIFICA'
    GROUP BY departamento, provincia, distrito
    HAVING SUM(cantidad) >= 20
    ORDER BY personas DESC
    LIMIT 10
  """)
}

val res5f2 = {
  spark.sql("""
    SELECT nacionalidad, anio, SUM(cantidad) AS personas, COUNT(*) AS registros,
           ROUND(CAST(AVG(cantidad) AS DOUBLE), 3) AS promedio
    FROM denuncias
    WHERE nacionalidad NOT IN ('PERÚ', 'NO ESPECIFICA') AND anio >= 2022
    GROUP BY nacionalidad, anio
    HAVING SUM(cantidad) >= 10
    ORDER BY personas DESC, anio ASC
  """)
}

val res5f3 = {
  spark.sql("""
    SELECT mes, SUM(cantidad) AS personas, COUNT(*) AS registros
    FROM denuncias
    WHERE ocupacion = 'ESTUDIANTE' AND rango_edad IN ('6-11 AÑOS','12-17 AÑOS','18-29 AÑOS')
    GROUP BY mes
    HAVING SUM(cantidad) > 500
    ORDER BY personas DESC, mes ASC
  """)
}

// ---------------------------------------------------------------------------
// Escritura con spark-excel (0.18.7) — un libro por consulta
// ---------------------------------------------------------------------------
def guardarExcel(df: org.apache.spark.sql.DataFrame, archivo: String, hoja: String): Unit = {
  val ruta = carpetaExcel + "/" + archivo
  df.coalesce(1).write
    .format("com.crealytics.spark.excel")
    .option("dataAddress", "'" + hoja + "'!A1")
    .option("header", "true")
    .option("inferSchema", "false")
    .option("dateFormat", "yyyy-mm-dd")
    .option("timestampFormat", "yyyy-mm-dd hh:mm:ss")
    .mode("overwrite")
    .save(ruta)
  println("  guardado: " + ruta + "  (hoja '" + hoja + "', " + df.count() + " filas)")
}

println("")
println("[5-g] escribiendo libros Excel...")
guardarExcel(res5e1, "05e_1_variacion_interanual.xlsx", "5e1_variacion")
guardarExcel(res5e2, "05e_2_grupoetario_sexo.xlsx",   "5e2_perfil")
guardarExcel(res5e3, "05e_3_region_periodo.xlsx",     "5e3_region")
guardarExcel(res5f1, "05f_1_distritos_mujeres2024.xlsx", "5f1_distritos")
guardarExcel(res5f2, "05f_2_nacionalidades.xlsx",     "5f2_nacionalidad")
guardarExcel(res5f3, "05f_3_meses_estudiantes.xlsx",  "5f3_meses")

println("")
println("[5-g] FIN: revisar la carpeta " + carpetaExcel)
