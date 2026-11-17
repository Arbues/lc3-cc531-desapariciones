-- ============================================================================
-- LC3 CC531 - Denuncias por desaparicion de personas [MININTER] 2019-2024
-- 01_ddl.sql : creacion del modelo estrella en PostgreSQL 16
-- Ejecutar:  psql -h /tmp -p 5433 -U macrodatos -d denuncias -f sql/01_ddl.sql
-- ============================================================================

DROP TABLE IF EXISTS hecho_desaparicion CASCADE;
DROP TABLE IF EXISTS dim_tiempo CASCADE;
DROP TABLE IF EXISTS dim_geografia CASCADE;
DROP TABLE IF EXISTS dim_rango_edad CASCADE;
DROP TABLE IF EXISTS dim_sexo CASCADE;
DROP TABLE IF EXISTS dim_estado_civil CASCADE;
DROP TABLE IF EXISTS dim_nacionalidad CASCADE;
DROP TABLE IF EXISTS dim_nivel_educativo CASCADE;
DROP TABLE IF EXISTS dim_ocupacion CASCADE;
DROP TABLE IF EXISTS stg_desaparicion CASCADE;

-- ---------------------------------------------------------------------------
-- DIMENSIONES
-- ---------------------------------------------------------------------------
CREATE TABLE dim_tiempo (
    anio         INTEGER      PRIMARY KEY,
    periodo      VARCHAR(20)  NOT NULL,
    es_pandemia  BOOLEAN      NOT NULL
);

CREATE TABLE dim_geografia (
    ubigeo            CHAR(6)      PRIMARY KEY,
    departamento      VARCHAR(60)  NOT NULL,
    provincia         VARCHAR(60)  NOT NULL,
    distrito          VARCHAR(80)  NOT NULL,
    region_natural    VARCHAR(10)  NOT NULL,
    cod_departamento  CHAR(2)      NOT NULL,
    cod_provincia     CHAR(4)      NOT NULL,
    CONSTRAINT ck_region CHECK (region_natural IN ('COSTA', 'SIERRA', 'SELVA'))
);

CREATE TABLE dim_rango_edad (
    rango_edad     VARCHAR(20)  PRIMARY KEY,
    grupo_etario   VARCHAR(25)  NOT NULL,
    orden          SMALLINT     NOT NULL,
    edad_min       SMALLINT     NOT NULL,
    edad_max       SMALLINT     NOT NULL
);

CREATE TABLE dim_sexo (
    sexo      VARCHAR(15)  PRIMARY KEY,
    descripcion VARCHAR(25) NOT NULL
);

CREATE TABLE dim_estado_civil (
    estado_civil  VARCHAR(25)  PRIMARY KEY
);

CREATE TABLE dim_nacionalidad (
    nacionalidad  VARCHAR(60)  PRIMARY KEY,
    es_extranjera BOOLEAN      NOT NULL
);

CREATE TABLE dim_nivel_educativo (
    nivel_educativo  VARCHAR(25)  PRIMARY KEY,
    orden            SMALLINT     NOT NULL
);

CREATE TABLE dim_ocupacion (
    ocupacion  VARCHAR(80)  PRIMARY KEY
);

-- ---------------------------------------------------------------------------
-- TABLA DE HECHOS (grano: anio x mes x ubigeo x perfil de la victima)
-- ---------------------------------------------------------------------------
CREATE TABLE hecho_desaparicion (
    id              BIGSERIAL    PRIMARY KEY,
    anio            INTEGER      NOT NULL REFERENCES dim_tiempo(anio),
    mes             SMALLINT     NOT NULL CHECK (mes BETWEEN 1 AND 12),
    ubigeo          CHAR(6)      REFERENCES dim_geografia(ubigeo),
    sexo            VARCHAR(15)  NOT NULL REFERENCES dim_sexo(sexo),
    rango_edad      VARCHAR(20)  NOT NULL REFERENCES dim_rango_edad(rango_edad),
    estado_civil    VARCHAR(25)  NOT NULL REFERENCES dim_estado_civil(estado_civil),
    nacionalidad    VARCHAR(60)  NOT NULL REFERENCES dim_nacionalidad(nacionalidad),
    nivel_educativo VARCHAR(25)  NOT NULL REFERENCES dim_nivel_educativo(nivel_educativo),
    ocupacion       VARCHAR(80)  NOT NULL REFERENCES dim_ocupacion(ocupacion),
    cantidad        SMALLINT     NOT NULL CHECK (cantidad > 0)
);

CREATE INDEX ix_hecho_anio      ON hecho_desaparicion (anio);
CREATE INDEX ix_hecho_ubigeo    ON hecho_desaparicion (ubigeo);
CREATE INDEX ix_hecho_perfil    ON hecho_desaparicion (sexo, rango_edad);
CREATE INDEX ix_hecho_nacional  ON hecho_desaparicion (nacionalidad);

-- ---------------------------------------------------------------------------
-- TABLA DE STAGING (paso de extraccion del ETL: COPY del CSV tal cual)
-- ---------------------------------------------------------------------------
CREATE TABLE stg_desaparicion (
    anio            INTEGER,
    mes             INTEGER,
    ubigeo_hecho    VARCHAR(10),
    dpto_hecho      VARCHAR(60),
    prov_hecho      VARCHAR(60),
    dist_hecho      VARCHAR(80),
    sexo            VARCHAR(15),
    rango_edad      VARCHAR(20),
    est_civil       VARCHAR(25),
    nacionalidad    VARCHAR(60),
    nivel_educativo VARCHAR(25),
    ocupacion       VARCHAR(80),
    cantidad        INTEGER
);

-- ---------------------------------------------------------------------------
-- Vista de consulta usada en las capturas del informe
-- ---------------------------------------------------------------------------
CREATE OR REPLACE VIEW v_desaparicion AS
SELECT h.anio, t.periodo, h.mes,
       g.departamento, g.provincia, g.distrito, g.region_natural,
       h.sexo, e.grupo_etario, h.rango_edad,
       h.nacionalidad, n.es_extranjera,
       h.nivel_educativo, h.estado_civil, h.ocupacion,
       h.cantidad
FROM hecho_desaparicion h
JOIN dim_tiempo        t ON h.anio = t.anio
LEFT JOIN dim_geografia g ON h.ubigeo = g.ubigeo
JOIN dim_rango_edad    e ON h.rango_edad = e.rango_edad
JOIN dim_nacionalidad  n ON h.nacionalidad = n.nacionalidad;
