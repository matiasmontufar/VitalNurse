package com.proyecto.vitalnurse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La cédula debe tener 10 dígitos")
    @Column(nullable = false, unique = true, length = 10)
    private String cedula;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(nullable = false)
    private String apellidos;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 150, message = "La edad no puede superar 150")
    @Column(nullable = false)
    private Integer edad;

    @NotBlank(message = "El sexo es obligatorio")
    @Column(nullable = false, length = 15)
    private String sexo;

    @Column(columnDefinition = "TEXT")
    private String enfermedadesPreexistentes;

    public Paciente() {
    }

    // --- Getters y Setters Corregidos ---

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

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