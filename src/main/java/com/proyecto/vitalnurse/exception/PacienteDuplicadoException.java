package com.proyecto.vitalnurse.exception;

public class PacienteDuplicadoException extends RuntimeException {
    public PacienteDuplicadoException(String cedula) {
        super("Ya existe un paciente registrado con la cédula: " + cedula);
    }
}
