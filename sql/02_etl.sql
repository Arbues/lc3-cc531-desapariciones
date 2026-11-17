-- ============================================================================
-- 02_etl.sql : proceso ETL (Extract -> Transform -> Load) en PostgreSQL 16
-- Ejecutar:  psql -h /tmp -p 5433 -U macrodatos -d denuncias -f sql/02_etl.sql
--
-- Requiere que el CSV del dataset este accesible al servidor. Como el cluster
-- corre en el mismo equipo, se usa la ruta absoluta del archivo.
-- ============================================================================
\set ruta_csv '/home/arbues/macrodatos/PC3/data/denuncias_desaparicion_2019_2024.csv'

BEGIN;

-- ---------------------------------------------------------------------------
-- EXTRACT: carga cruda del CSV en la tabla de staging (todas las columnas TEXT)
-- Se usa \copy (client-side) porque el rol de la aplicacion no tiene el
-- privilegio pg_read_server_files que exige COPY ... FROM 'archivo'.
-- ---------------------------------------------------------------------------
TRUNCATE stg_desaparicion;

\copy stg_desaparicion (anio, mes, ubigeo_hecho, dpto_hecho, prov_hecho, dist_hecho, sexo, rango_edad, est_civil, nacionalidad, nivel_educativo, ocupacion, cantidad) FROM '/home/arbues/macrodatos/PC3/data/denuncias_desaparicion_2019_2024.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',', ENCODING 'UTF8', NULL '')

-- ---------------------------------------------------------------------------
-- TRANSFORM + LOAD de las dimensiones
-- Regla 1: categoricas vacias -> 'NO ESPECIFICA'
-- Regla 2: ubigeo vacio -> NULL (el registro existe pero sin geografia)
-- ---------------------------------------------------------------------------
INSERT INTO dim_tiempo (anio, periodo, es_pandemia) VALUES
    (2019, 'PRE-PANDEMIA',  FALSE),
    (2020, 'PANDEMIA',      TRUE),
    (2021, 'PANDEMIA',      TRUE),
    (2022, 'POST-PANDEMIA', FALSE),
    (2023, 'POST-PANDEMIA', FALSE),
    (2024, 'POST-PANDEMIA', FALSE);

INSERT INTO dim_geografia (ubigeo, departamento, provincia, distrito, region_natural,
                           cod_departamento, cod_provincia)
SELECT ubigeo,
       MAX(departamento)   AS departamento,
       MAX(provincia)      AS provincia,
       MAX(distrito)       AS distrito,
       CASE
         WHEN MAX(departamento) IN ('TUMBES','PIURA','LAMBAYEQUE','LA LIBERTAD','ÁNCASH',
              'LIMA METROPOLITANA','REGIÓN LIMA','CALLAO','ICA','AREQUIPA','MOQUEGUA','TACNA')
              THEN 'COSTA'
         WHEN MAX(departamento) IN ('CAJAMARCA','HUÁNUCO','PASCO','JUNÍN','HUANCAVELICA',
              'AYACUCHO','APURIMAC','CUSCO','PUNO','AMAZONAS')
              THEN 'SIERRA'
         ELSE 'SELVA'
       END                                        AS region_natural,
       SUBSTRING(ubigeo, 1, 2)                    AS cod_departamento,
       SUBSTRING(ubigeo, 1, 4)                    AS cod_provincia
FROM (
  SELECT LPAD(TRIM(ubigeo_hecho), 6, '0')        AS ubigeo,
         UPPER(TRIM(dpto_hecho))                 AS departamento,
         UPPER(TRIM(prov_hecho))                 AS provincia,
         UPPER(TRIM(dist_hecho))                 AS distrito
  FROM stg_desaparicion
  WHERE COALESCE(TRIM(ubigeo_hecho), '') <> ''
    AND COALESCE(TRIM(dpto_hecho), '')   <> ''
) t
GROUP BY ubigeo;

INSERT INTO dim_rango_edad (rango_edad, grupo_etario, orden, edad_min, edad_max) VALUES
    ('0-5 AÑOS',      'PRIMERA INFANCIA', 0,  0,   5),
    ('6-11 AÑOS',     'NINEZ',            1,  6,  11),
    ('12-17 AÑOS',    'ADOLESCENTE',      2, 12,  17),
    ('18-29 AÑOS',    'ADULTO JOVEN',     3, 18,  29),
    ('30-59 AÑOS',    'ADULTO',           4, 30,  59),
    ('60 A MAS AÑOS', 'ADULTO MAYOR',     5, 60, 120),
    ('NO ESPECIFICA', 'NO ESPECIFICA',    9, -1,  -1);

INSERT INTO dim_sexo (sexo, descripcion) VALUES
    ('F', 'FEMENINO'), ('M', 'MASCULINO'), ('NO ESPECIFICA', 'NO ESPECIFICADO');

INSERT INTO dim_nivel_educativo (nivel_educativo, orden) VALUES
    ('SIN INSTRUCCION', 0), ('PRIMARIA', 1), ('SECUNDARIA', 2),
    ('TECNICA', 3), ('SUPERIOR', 4), ('NO ESPECIFICA', 9);

INSERT INTO dim_estado_civil (estado_civil)
SELECT DISTINCT COALESCE(NULLIF(TRIM(est_civil), ''), 'NO ESPECIFICA')
FROM stg_desaparicion
ON CONFLICT DO NOTHING;

INSERT INTO dim_nacionalidad (nacionalidad, es_extranjera)
SELECT DISTINCT COALESCE(NULLIF(TRIM(nacionalidad), ''), 'NO ESPECIFICA'),
       COALESCE(NULLIF(TRIM(nacionalidad), ''), 'NO ESPECIFICA') NOT IN ('PERÚ', 'NO ESPECIFICA')
FROM stg_desaparicion
ON CONFLICT DO NOTHING;

INSERT INTO dim_ocupacion (ocupacion)
SELECT DISTINCT COALESCE(NULLIF(TRIM(ocupacion), ''), 'NO ESPECIFICA')
FROM stg_desaparicion
ON CONFLICT DO NOTHING;

-- ---------------------------------------------------------------------------
-- TRANSFORM + LOAD de la tabla de hechos
-- ---------------------------------------------------------------------------
TRUNCATE hecho_desaparicion RESTART IDENTITY;

INSERT INTO hecho_desaparicion (anio, mes, ubigeo, sexo, rango_edad, estado_civil,
                                nacionalidad, nivel_educativo, ocupacion, cantidad)
SELECT s.anio,
       s.mes,
       CASE WHEN COALESCE(TRIM(s.ubigeo_hecho), '') = '' THEN NULL
            ELSE LPAD(TRIM(s.ubigeo_hecho), 6, '0') END                   AS ubigeo,
       COALESCE(NULLIF(TRIM(s.sexo), ''), 'NO ESPECIFICA')                AS sexo,
       COALESCE(NULLIF(TRIM(s.rango_edad), ''), 'NO ESPECIFICA')          AS rango_edad,
       COALESCE(NULLIF(TRIM(s.est_civil), ''), 'NO ESPECIFICA')           AS estado_civil,
       COALESCE(NULLIF(TRIM(s.nacionalidad), ''), 'NO ESPECIFICA')        AS nacionalidad,
       COALESCE(NULLIF(TRIM(s.nivel_educativo), ''), 'NO ESPECIFICA')     AS nivel_educativo,
       COALESCE(NULLIF(TRIM(s.ocupacion), ''), 'NO ESPECIFICA')           AS ocupacion,
       s.cantidad
FROM stg_desaparicion s
WHERE s.cantidad IS NOT NULL AND s.cantidad > 0;

COMMIT;

-- ---------------------------------------------------------------------------
-- VERIFICACION del ETL
-- ---------------------------------------------------------------------------
\echo '--- conteos por tabla ---'
SELECT 'stg_desaparicion' AS tabla, COUNT(*) FROM stg_desaparicion
UNION ALL SELECT 'hecho_desaparicion', COUNT(*) FROM hecho_desaparicion
UNION ALL SELECT 'dim_geografia',     COUNT(*) FROM dim_geografia
UNION ALL SELECT 'dim_nacionalidad',  COUNT(*) FROM dim_nacionalidad
UNION ALL SELECT 'dim_ocupacion',     COUNT(*) FROM dim_ocupacion;

\echo '--- control de cuadre: personas totales (debe ser 121,427) ---'
SELECT SUM(cantidad) AS personas_totales FROM hecho_desaparicion;

\echo '--- top 10 departamentos ---'
SELECT departamento, SUM(cantidad) AS personas
FROM v_desaparicion
GROUP BY departamento
ORDER BY personas DESC
LIMIT 10;
