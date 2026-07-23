-- Migración: separar presion_arterial (VARCHAR) en presion_arterial_sistolica / diastolica (INT)
-- Ejecutar contra la BD mysql: mysql -u root -p vitalnurse < migration-signos-vitales.sql
-- O desde MySQL Workbench: File > Run SQL Script

-- 1. Agregar columnas como NULLable primero
ALTER TABLE signos_vitales ADD COLUMN presion_arterial_sistolica INT NULL;
ALTER TABLE signos_vitales ADD COLUMN presion_arterial_diastolica INT NULL;

-- 2. Migrar datos existentes (asume formato "120/80")
UPDATE signos_vitales
SET presion_arterial_sistolica = CAST(SUBSTRING_INDEX(presion_arterial, '/', 1) AS UNSIGNED),
    presion_arterial_diastolica = CAST(SUBSTRING_INDEX(presion_arterial, '/', -1) AS UNSIGNED)
WHERE presion_arterial IS NOT NULL
  AND presion_arterial LIKE '%/%';

-- 3. Establecer default para registros nulos (si los hay)
UPDATE signos_vitales SET presion_arterial_sistolica = 120 WHERE presion_arterial_sistolica IS NULL;
UPDATE signos_vitales SET presion_arterial_diastolica = 80 WHERE presion_arterial_diastolica IS NULL;

-- 4. Agregar NOT NULL
ALTER TABLE signos_vitales MODIFY COLUMN presion_arterial_sistolica INT NOT NULL;
ALTER TABLE signos_vitales MODIFY COLUMN presion_arterial_diastolica INT NOT NULL;

-- 5. Eliminar columna vieja
ALTER TABLE signos_vitales DROP COLUMN presion_arterial;

-- 6. Agregar check constraints
ALTER TABLE signos_vitales ADD CONSTRAINT chk_pas CHECK (presion_arterial_sistolica >= 40 AND presion_arterial_sistolica <= 300);
ALTER TABLE signos_vitales ADD CONSTRAINT chk_pad CHECK (presion_arterial_diastolica >= 20 AND presion_arterial_diastolica <= 200);
