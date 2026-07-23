package com.proyecto.vitalnurse.models;

import java.util.List;

public class PacienteVista {
    private Long idPaciente;
    private String cedula;
    private String nombres;
    private String apellidos;
    private Integer edad;
    private String sexo;
    private String enfermedadesPreexistentes;

    public PacienteVista() {}

    public PacienteVista(Long idPaciente, String cedula, String nombres, String apellidos, Integer edad, String sexo, String enfermedadesPreexistentes) {
        this.idPaciente = idPaciente;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.sexo = sexo;
        this.enfermedadesPreexistentes = enfermedadesPreexistentes;
    }

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
