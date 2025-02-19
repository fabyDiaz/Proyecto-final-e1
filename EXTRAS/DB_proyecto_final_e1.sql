-- ============================================================================
-- CREACIÓN DE TABLAS PARA PROYECTO FINAL JAVA
-- ============================================================================

-- 1. Tabla admin
CREATE TABLE IF NOT EXISTS admin (
    username        VARCHAR(100) NOT NULL PRIMARY KEY,
    password        VARCHAR(200) NOT NULL
);

-- 2. Tabla company
CREATE TABLE IF NOT EXISTS company (
    id              SERIAL       PRIMARY KEY,
    company_name    VARCHAR(200) NOT NULL,
    company_api_key VARCHAR(255) NOT NULL UNIQUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 3. Tabla location
CREATE TABLE IF NOT EXISTS location (
    id               SERIAL       PRIMARY KEY,
    company_id       INT          NOT NULL,
    location_name    VARCHAR(200) NOT NULL,
    location_country VARCHAR(100),
    location_city    VARCHAR(100),
    location_meta    TEXT,
    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP    NOT NULL DEFAULT NOW(),

    -- Clave foránea: indica que company_id está relacionado con la tabla company
    CONSTRAINT fk_company
      FOREIGN KEY (company_id)
      REFERENCES company(id)
      ON DELETE CASCADE
);

-- 4. Tabla sensor
CREATE TABLE IF NOT EXISTS sensor (
    id              SERIAL       PRIMARY KEY,
    location_id     INT          NOT NULL,
    sensor_name     VARCHAR(200) NOT NULL,
    sensor_category VARCHAR(100),
    sensor_meta     TEXT,
    sensor_api_key  VARCHAR(255) NOT NULL UNIQUE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW(),

    -- Clave foránea: indica que location_id se relaciona con la tabla location
    CONSTRAINT fk_location
      FOREIGN KEY (location_id)
      REFERENCES location(id)
      ON DELETE CASCADE
);

-- 5. Tabla sensor_data

-- Se utiliza JSONB para almacenar lecturas variables de los sensores. TESTAR 
CREATE TABLE IF NOT EXISTS sensor_data (
    id          SERIAL       PRIMARY KEY,
    sensor_id   INT          NOT NULL,
    reading     JSONB        NOT NULL,               -- Datos crudos en formato JSON
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(), -- Momento de recepción

    -- Clave foránea: sensor_id se relaciona con la tabla sensor
    CONSTRAINT fk_sensor
      FOREIGN KEY (sensor_id)
      REFERENCES sensor(id)
      ON DELETE CASCADE
);

-- Crear índices útiles para búsquedas
CREATE INDEX IF NOT EXISTS idx_sensor_data_sensor_id ON sensor_data(sensor_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_created_at ON sensor_data(created_at);


