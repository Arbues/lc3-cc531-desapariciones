// ============================================================================
// 5-c.2) JOIN tipo LEFT OUTER
// Pregunta: "Existen ubigeos del catalogo geografico sin ningun registro de
// desaparicion? (control de cobertura territorial del dataset)"
// ============================================================================
:load /home/arbues/macrodatos/PC3/code/comun/esquema.scala

titulo("5-c.2) LEFT JOIN: DIM_GEOGRAFIA x HECHOS -> UBIGEOS SIN REGISTROS")

val hechosUbigeo = {
  denuncias.filter(col("ubigeo").isNotNull).groupBy("ubigeo").agg(sum("cantidad").as("personas"))
}

val consulta = {
  dimGeografia
    .select("ubigeo", "departamento", "provincia", "distrito")
    .join(hechosUbigeo, Seq("ubigeo"), "left_outer")
}

val sinRegistros = {
  consulta.filter(col("personas").isNull).orderBy("departamento", "provincia", "distrito")
}

println("")
println("[control] ubigeos del catalogo          : " + consulta.count())
println("[control] ubigeos SIN ningun registro   : " + sinRegistros.count())
println("[control] ubigeos CON registros         : " + consulta.filter(col("personas").isNotNull).count())

mostrar("5-c.2 (left_outer) ubigeos sin registros", sinRegistros, 20)
