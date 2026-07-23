package com.proyecto.vitalnurse.exception;

public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException(String cedula) {
        super("Ya existe un usuario registrado con la cedula: " + cedula);
    }
}
