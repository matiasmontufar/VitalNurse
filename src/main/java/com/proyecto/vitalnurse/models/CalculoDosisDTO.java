package com.proyecto.vitalnurse.models;
    
    import jakarta.validation.constraints.*;
    
    public class CalculoDosisDTO {
    
        @Positive(message = "La dosis indicada debe ser positiva")
        private double dosisIndicada;
    
        @NotBlank(message = "La unidad de dosis es obligatoria")
        private String unidadDosis;
    
        @Positive(message = "La concentracion debe ser positiva")
        private double concentracion;
    
        @NotBlank(message = "La unidad de concentracion es obligatoria")
        private String unidadConcentracion;
    
        @Positive(message = "El volumen debe ser positivo")
        private double volumen;
    
        @PositiveOrZero(message = "El tiempo debe ser cero o positivo")
        private int tiempoMinutos;
    
        public CalculoDosisDTO() {}
    
        public double getDosisIndicada() { return dosisIndicada; }
        public void setDosisIndicada(double dosisIndicada) { this.dosisIndicada = dosisIndicada; }
    
        public String getUnidadDosis() { return unidadDosis; }
        public void setUnidadDosis(String unidadDosis) { this.unidadDosis = unidadDosis; }
    
        public double getConcentracion() { return concentracion; }
        public void setConcentracion(double concentracion) { this.concentracion = concentracion; }
    
        public String getUnidadConcentracion() { return unidadConcentracion; }
        public void setUnidadConcentracion(String unidadConcentracion) { this.unidadConcentracion = unidadConcentracion; }
    
        public double getVolumen() { return volumen; }
        public void setVolumen(double volumen) { this.volumen = volumen; }
    
        public int getTiempoMinutos() { return tiempoMinutos; }
        public void setTiempoMinutos(int tiempoMinutos) { this.tiempoMinutos = tiempoMinutos; }
    }