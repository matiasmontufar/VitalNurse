package com.proyecto.vitalnurse.services;

import com.proyecto.vitalnurse.models.Usuario;
import com.proyecto.vitalnurse.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public void guardarUsuario(Usuario usuario) {
        String claveSegura = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(claveSegura);
        usuarioRepository.save(usuario);
    }
}
