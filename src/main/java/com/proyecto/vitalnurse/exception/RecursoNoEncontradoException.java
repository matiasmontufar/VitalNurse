package com.proyecto.vitalnurse.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " no encontrado con ID: " + id);
    }
    public RecursoNoEncontradoException(String recurso, String criterio) {
        super(recurso + " no encontrado: " + criterio);
    }
}
