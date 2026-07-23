package com.proyecto.vitalnurse.controllers;

import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute("mensaje", "Panel de Control Clínico Activo");
        return "dashboard";
    }

    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        return "registrar-usuario";
    }

    @PostMapping("/usuarios/nuevo")
    public String registrarUsuario(
            @RequestParam String cedula,
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam int edad,
            @RequestParam String sexo,
            @RequestParam String username,
            @RequestParam String contrasena,
            @RequestParam(defaultValue = "ENFERMERO") String rol,
            Model model) {

        try {
            Persona persona = new Persona();
            persona.setCedula(cedula);
            persona.setNombres(nombres);
            persona.setApellidos(apellidos);
            persona.setEdad(edad);
            persona.setSexo(sexo);
            usuarioService.crearUsuario(persona, username, contrasena, rol);
            model.addAttribute("mensajeExito", "Usuario creado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear usuario: " + e.getMessage());
        }
        return "registrar-usuario";
    }
}
