package com.proyecto.vitalnurse.models;
    
    import jakarta.validation.constraints.*;
    
    public class PacienteRegistroDTO {
    
        @NotBlank(message = "La cedula es obligatoria")
        @Size(min = 10, max = 10, message = "La cedula debe tener 10 digitos")
        private String cedula;
    
        @NotBlank(message = "El nombre es obligatorio")
        private String nombres;
    
        @NotBlank(message = "Los apellidos son obligatorios")
        private String apellidos;
    
        @NotNull(message = "La edad es obligatoria")
        @Min(value = 0, message = "La edad no puede ser negativa")
        @Max(value = 150, message = "La edad no puede superar 150")
        private Integer edad;
    
        @NotBlank(message = "El sexo es obligatorio")
        private String sexo;
    
        private String enfermedadesPreexistentes;
    
        public PacienteRegistroDTO() {}
    
        public PacienteRegistroDTO(String cedula, String nombres, String apellidos, Integer edad, String sexo, String enfermedadesPreexistentes) {
            this.cedula = cedula;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.edad = edad;
            this.sexo = sexo;
            this.enfermedadesPreexistentes = enfermedadesPreexistentes;
        }
    
        public String getCedula() { return cedula; }
        public void setCedula(String cedula) { this.cedula = cedula; }
    
        public String getNombres() { return nombres; }
        public void setNombres(String nombres) { this.nombres = nombres; }
    
        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }
    
        public String getSexo() { return sexo; }
        public void setSexo(String sexo) { this.sexo = sexo; }
    
        public String getEnfermedadesPreexistentes() { return enfermedadesPreexistentes; }
        public void setEnfermedadesPreexistentes(String enfermedadesPreexistentes) { this.enfermedadesPreexistentes = enfermedadesPreexistentes; }
    }