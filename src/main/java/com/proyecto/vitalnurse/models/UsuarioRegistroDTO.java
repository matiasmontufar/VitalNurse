package com.proyecto.vitalnurse.models;

import jakarta.validation.constraints.*;

public class UsuarioRegistroDTO {

    @NotBlank(message = "La cedula es obligatoria")
    @Size(min = 10, max = 10, message = "La cedula debe tener 10 digitos")
    private String cedula;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String contrasena;

    private String rol;

    public UsuarioRegistroDTO() {}

    public UsuarioRegistroDTO(String cedula, String nombre, String contrasena) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
