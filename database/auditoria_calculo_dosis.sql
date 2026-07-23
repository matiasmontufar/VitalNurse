CREATE TABLE auditoria_calculo_dosis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    usuario_nombre VARCHAR(100) NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    dosis_indicada DOUBLE NOT NULL,
    unidad_dosis VARCHAR(10) NOT NULL,
    presentacion DOUBLE NOT NULL,
    unidad_presentacion VARCHAR(10) NOT NULL,
    diluyente_ml DOUBLE NOT NULL,
    horas_totales DOUBLE NOT NULL DEFAULT 0,

    volumen_ml DOUBLE,
    ml_por_hora DOUBLE,
    gotas_por_minuto DOUBLE,
    microgotas_por_minuto DOUBLE,

    CONSTRAINT chk_dosis_positiva CHECK (dosis_indicada > 0),
    CONSTRAINT chk_presentacion_positiva CHECK (presentacion > 0),
    CONSTRAINT chk_diluyente_positivo CHECK (diluyente_ml > 0),
    CONSTRAINT chk_horas_no_negativas CHECK (horas_totales >= 0),
    CONSTRAINT chk_unidad_dosis CHECK (unidad_dosis IN ('mcg', 'mg', 'g')),
    CONSTRAINT chk_unidad_presentacion CHECK (unidad_presentacion IN ('mcg', 'mg', 'g'))
);
