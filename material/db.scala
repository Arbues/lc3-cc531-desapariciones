// Leer empleados
val empleadosDF = spark.read
  .format("jdbc")
  .option("url", "jdbc:postgresql:postgres") 
  .option("dbtable", "empleados")
  .option("user", "postgres")
  .option("password", "87654321")
  .load()
//
//.option("url", "jdbc:postgresql://localhost:5433/postgres")
// Leer departamentos
val departamentosDF = spark.read
  .format("jdbc")
  .option("url", "jdbc:postgresql:postgres")
  .option("dbtable", "departamentos")
  .option("user", "postgres")
  .option("password", "87654321")
  .load()

// Mostrar datos
empleadosDF.show()
departamentosDF.show()

// Hacer JOIN entre empleados y departamentos
val resultado = empleadosDF.join(departamentosDF,
  empleadosDF("departamento_id") === departamentosDF("id")
)
val resultado2 = empleadosDF.join(departamentosDF,Seq("id"),"inner")

// Mostrar resultado
resultado.select("nombre", "salario", "nombre").show()

//mostrar empleados con salario mayor a 3000
val filtroSalario = empleadosDF.filter("salario > 3000")
filtroSalario.show()

//selecionar campos especificos: nombre y salario
val seleccion = empleadosDF.select("nombre", "salario")
seleccion.show()

//ordenar
val ordenados = empleadosDF.orderBy(empleadosDF("salario").desc)
ordenados.show()

//agrupar por departamentos y contar
val conteo = empleadosDF.groupBy("departamento_id").count()
conteo.show()

//promedio
val promedio = empleadosDF.groupBy("departamento_id").
agg(org.apache.spark.sql.functions.avg("salario").alias("promedio_salario"))
promedio.show()

//operacion join
val empleadosConDepto = empleadosDF
  .join(departamentosDF, empleadosDF("departamento_id") === departamentosDF("id"))
  .select(empleadosDF("nombre").alias("empleado"), $"salario", departamentosDF("nombre").alias("departamento"))

empleadosConDepto.show()

//contar empleados por departamento
val empleadosPorDepto = empleadosDF
  .join(departamentosDF, empleadosDF("departamento_id") === departamentosDF("id"))
  .groupBy(departamentosDF("nombre"))
  .count()

empleadosPorDepto.show()

//maximo y minimo 
import org.apache.spark.sql.functions._

val salarioStats = empleadosDF.groupBy("departamento_id")
  .agg(
    max("salario").alias("salario_max"),
    min("salario").alias("salario_min")
  )

salarioStats.show()

//usando vistas temporales y SQL
empleadosDF.createOrReplaceTempView("empleados")
departamentosDF.createOrReplaceTempView("departamentos")

val resultado = spark.sql("""
  SELECT e.nombre, e.salario, d.nombre AS departamento
  FROM empleados e
  JOIN departamentos d ON e.departamento_id = d.id
  WHERE e.salario > 3000
""")
resultado.show()