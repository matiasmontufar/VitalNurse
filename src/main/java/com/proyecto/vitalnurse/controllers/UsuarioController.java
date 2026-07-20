package com.proyecto.vitalnurse.controllers;

import com.proyecto.vitalnurse.models.Usuario;
import com.proyecto.vitalnurse.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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
        model.addAttribute("usuario", new Usuario());
        return "registrar-usuario";
    }

    @PostMapping("/usuarios/nuevo")
    public String registrarUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registrar-usuario";
        }
        usuarioService.guardarUsuario(usuario);
        model.addAttribute("mensajeExito", "Usuario creado y credenciales encriptadas correctamente.");
        model.addAttribute("usuario", new Usuario());
        return "registrar-usuario";
    }
}
