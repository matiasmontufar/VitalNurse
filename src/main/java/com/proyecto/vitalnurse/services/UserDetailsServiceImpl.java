package com.proyecto.vitalnurse.services;

import com.proyecto.vitalnurse.models.Usuario;
import com.proyecto.vitalnurse.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {

        System.out.println(">>> INTENTO DE LOGIN. Buscando cédula: " + cedula);

        Usuario usuario = usuarioRepository.findByCedula(cedula).orElse(null);

        if (usuario == null) {
            System.out.println(">>> ERROR: No se encontró la cédula en la base de datos.");
            throw new UsernameNotFoundException("Usuario o cédula no encontrada");
        }

        System.out.println(">>> ÉXITO: Usuario encontrado: " + usuario.getNombre());
        System.out.println(">>> Hash en base de datos: " + usuario.getContrasena());

        return User.builder()
                .username(usuario.getCedula())
                .password(usuario.getContrasena())
                .roles(usuario.getRol())
                .build();
    }
}
