package com.proyecto.vitalnurse.models;
    
    import jakarta.validation.constraints.*;
    
    public class SignoVitalDTO {
    
        @NotNull(message = "El paciente es obligatorio")
        private Long idPaciente;
    
        @NotNull(message = "La temperatura es obligatoria")
        @DecimalMin(value = "30.0", message = "Temperatura fuera de rango")
        @DecimalMax(value = "45.0", message = "Temperatura fuera de rango")
        private Double temperatura;
    
        @NotBlank(message = "La presion arterial es obligatoria")
        private String presionArterial;
    
        @NotNull(message = "La frecuencia cardiaca es obligatoria")
        @Min(value = 20, message = "FC fuera de rango")
        @Max(value = 300, message = "FC fuera de rango")
        private Integer frecuenciaCardiaca;
    
        @NotNull(message = "La frecuencia respiratoria es obligatoria")
        @Min(value = 4, message = "FR fuera de rango")
        @Max(value = 100, message = "FR fuera de rango")
        private Integer frecuenciaRespiratoria;
    
        @NotNull(message = "La saturacion de oxigeno es obligatoria")
        @Min(value = 30, message = "SpO2 fuera de rango")
        @Max(value = 100, message = "SpO2 fuera de rango")
        private Integer saturacionOxigeno;
    
        @NotNull(message = "La glicemia es obligatoria")
        @DecimalMin(value = "10.0", message = "Glicemia fuera de rango")
        @DecimalMax(value = "600.0", message = "Glicemia fuera de rango")
        private Double glicemia;
    
        public SignoVitalDTO() {}
    
        public Long getIdPaciente() { return idPaciente; }
        public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
    
        public Double getTemperatura() { return temperatura; }
        public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }
    
        public String getPresionArterial() { return presionArterial; }
        public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }
    
        public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
        public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
    
        public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
        public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }
    
        public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
        public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }
    
        public Double getGlicemia() { return glicemia; }
        public void setGlicemia(Double glicemia) { this.glicemia = glicemia; }
    }