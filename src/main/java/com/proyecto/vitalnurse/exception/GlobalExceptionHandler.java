package com.proyecto.vitalnurse.exception;

import com.proyecto.vitalnurse.entity.persona.Persona;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PacienteDuplicadoException.class)
    public String manejarPacienteDuplicado(PacienteDuplicadoException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("persona", new Persona());
        return "registro-paciente";
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarRecursoNoEncontrado(RecursoNoEncontradoException ex, RedirectAttributes attrs) {
        attrs.addFlashAttribute("error", ex.getMessage());
        return "redirect:/pacientes";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String manejarIntegridadDatos(DataIntegrityViolationException ex, RedirectAttributes attrs) {
        attrs.addFlashAttribute("error", "Error de integridad: el registro ya existe o tiene datos relacionados.");
        return "redirect:/pacientes";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String manejarArgumentoInvalido(IllegalArgumentException ex, RedirectAttributes attrs) {
        attrs.addFlashAttribute("error", ex.getMessage());
        return "redirect:/pacientes";
    }

    @ExceptionHandler(Exception.class)
    public String manejarErrorGeneral(Exception ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("error", "Ocurrió un error inesperado. Intente nuevamente.");
        return "dashboard";
    }
}
