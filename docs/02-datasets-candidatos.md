# Datasets candidatos (PNDA, rubro SEGURIDAD Y JUSTICIA)

Verificado el 16-nov-2026 contra la API CKAN de la PNDA
(`/api/3/action/package_list`, `package_show`) y contra el cuadro de temas del curso
(`docs/datasets-tomados.csv`). Ninguno de estos aparece tomado.

## Ya tomados por otros grupos (prohibido usarlos)

1. Procesos judiciales principales a nivel nacional desde 2023 [PODER JUDICIAL] — grupo 1
2. Incidencias observadas mediante cámaras de videovigilancia — grupo 2
3. Indicadores de seguridad ciudadana y tendencias territoriales [MININTER] — grupo 3
4. Denuncias policiales registradas a nivel nacional [MININTER] — grupo 4
5. Trata de personas — grupo 5
6. Casos fiscales ingresados y atendidos a nivel nacional [MPFN] — grupo 6
7. Delitos denunciados ante el Ministerio Público [MPFN] — grupo 7

## Opción 1 (recomendada) — Denuncias por desaparición de personas [MININTER]

Página: https://www.datosabiertos.gob.pe/dataset/denuncias-por-desaparici%C3%B3n-de-personas-mininter
Última actualización del recurso: 18-sep-2026.

CSV verificado (8.1 MB, "Enero 2019 a Diciembre 2024"): **93,831 filas, 13 columnas**
En el mismo dataset hay un recurso más nuevo ("Enero 2019 a Diciembre 2025", 5.07 MB,
Latin-1): **77,331 filas, 10 columnas** — pierde EST_CIVIL, NIVEL_EDUCATIVO y OCUPACION
pero llega a dic-2025. Los totales por año son idénticos en ambos (Δ 0.00%), así que
combinarlos sin regla duplica los casos: ver `docs/03-evaluacion-desaparicion-vs-operaciones.md`.

```
AÑO, MES, UBIGEO_HECHO, DPTO_HECHO, PROV_HECHO, DIST_HECHO, SEXO, RANGO_EDAD,
EST_CIVIL, NACIONALIDAD, NIVEL_EDUCATIVO, OCUPACION, CANTIDAD
```

Ejemplo real: `2019,12,150110,LIMA METROPOLITANA,LIMA,COMAS,M,30-59 AÑOS,SOLTERO(A),PERÚ,,,3`

Por qué encaja con las 7 exigencias:

- **Numérico** `CANTIDAD` (conteo de personas) + **categóricos** ricos: SEXO, RANGO_EDAD,
  EST_CIVIL, NACIONALIDAD, NIVEL_EDUCATIVO, OCUPACION → 3a y 5a salen sin inventar nada.
- **Jerarquía geográfica** UBIGEO → DIST → PROV → DPTO: da una **dimensión geográfica real**
  para los 4 tipos de join de 5c y para las 3 selecciones con join de 5e.
- **Decimales** naturales: tasa por 100 mil habitantes y porcentajes por grupo de edad →
  3c (3 consultas con decimales y 3 MapReduce encadenados).
- Dos recursos más del mismo dataset (diccionario `.xlsx` y metadato `.docx`) que alimentan
  directamente el punto 2 del enunciado (campos, reglas de los datos, organización).
- Tamaño sano: 8 MB, procesa en segundos en `local[*]`; no hace falta YARN.

Riesgo: `NIVEL_EDUCATIVO` y `OCUPACION` vienen vacíos en muchas filas → hay que documentar
la regla de nulos en el ETL (eso suma en el informe, no resta).

## Opción 2 — Resultados de las operaciones policiales a nivel nacional [MININTER]

Página: https://www.datosabiertos.gob.pe/dataset/resultados-de-las-operaciones-policiales-nivel-nacional-mininter

Recursos: CSV por año (2023, 2024, 2025, 2026), un **consolidado Oct-2022→Jul-2026 de
29.9 MB** y diccionario `.xlsx` + metadato `.docx`. Verificado el 16-nov-2026:

```
consolidado Oct-2022→Sep-2026:  40,716 filas, 6 columnas  (LIMPIO)
consolidado Oct-2022→Jul-2026: 555,515 filas, 6 columnas  (78.2% duplicados exactos)
18 VARIABLE distintas | 53 DEPENDENCIAS | 6 DIR_REG_FRE (Región, Frente, DIRNIC, DIRNOS, RP ICA, RP TUMBES)
```

⚠ El archivo de 555,515 filas (`data/_sondeo/produccion_policial_consolidado.csv`) **no
sirve**: 434,478 filas son duplicados exactos y una misma clave AÑO-MES-VARIABLE-DEPENDENCIAS
se repite 28 a 62 veces. Si se suma tal cual, los totales salen ~30× inflados. El único
consolidado confiable es el de Sep-2026 (40,716 filas).

- Formato largo: `VARIABLE` es categórico, `VALOR` es numérico **con muchos decimales**
  (kilos de PBC/CC/marihuana, valorización de contrabando, soles incautados).
- 18 variables = 18 preguntas distintas para repartir entre RDD y SQL sin repetir (punto 6).
- Para joins necesita una dimensión derivada (dependencia → región policial → macrorregión);
  se puede construir del propio dataset y de las 6 `DIR_REG_FRE`.
- Riesgo real: outliers severos (máximo medido 513,820,390 en CONTRABANDO de RP PUNO,
  feb-2024; mediana global 3) y geografía gruesa: 38.9% de las filas pertenecen a
  direcciones nacionales sin departamento/provincia. El informe debe explicar el
  tratamiento de outliers.

## Opción 3 — Delitos informáticos denunciados ante el Ministerio Público [MPFN]

Página: https://www.datosabiertos.gob.pe/dataset/delitos-inform%C3%A1ticos-denunciados-ante-el-ministerio-p%C3%BAblico-mpfn
CSV por año 2019-2025 (60-90 KB cada uno, 2020 pesa 1.67 MB) + diccionario + metadatos.
Ventaja: tema actual y limpio. Desventaja: poco volumen y sin jerarquía geográfica, los
joins habría que inventarlos con tablas derivadas → más débil para 5c/5e.

## Opción 4 — Servicios del Instituto de Medicina Legal y Ciencias Forenses [MPFN]

Página: https://www.datosabiertos.gob.pe/dataset/servicios-del-instituto-de-medicina-legal-y-ciencias-forenses-mpfn
Dos CSV (2024: 101 KB, 2025: 93 KB) + diccionario. Necropsias, exámenes, pericias por sede.
Buena granularidad categórica, poco volumen.

## Opción 5 — Censo Nacional de Comisarías (CENACOM 2017) [INEI]

Página: https://www.datosabiertos.gob.pe/dataset/censo-nacional-de-comisarias-cenacom-2017-instituto-nacional-de-estad%C3%ADstica-e-inform%C3%A1tica
Data completa en ZIP (`inei.gob.pe/.../2017.zip`) + CSV de muestra + ficha técnica.
Excelente como **tabla de dimensión** de infraestructura policial si se cruza con otra base,
pero como dataset único es de 2017 y el profe premia bases recientes.

## Descargas ya hechas para validar (borrar si no se elige)

```
data/_sondeo/personas_desaparecidas.csv            8.1 MB   93,831 filas
data/_sondeo/produccion_policial_consolidado.csv   29.9 MB 555,515 filas
data/_sondeo/produccion_policial_2026.csv          478 KB    8,640 filas
data/_sondeo/diccionario_produccion_policial.xlsx   11 KB   diccionario de campos
```
