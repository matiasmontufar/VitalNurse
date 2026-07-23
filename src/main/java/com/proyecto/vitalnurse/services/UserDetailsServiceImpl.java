package com.proyecto.vitalnurse.services;

import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import com.proyecto.vitalnurse.repositories.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        System.out.println(">>> INTENTO DE LOGIN. Buscando cédula: " + cedula);

        Usuario usuario = usuarioRepository.findByUsernameWithRoles(cedula).orElse(null);

        if (usuario == null) {
            System.out.println(">>> ERROR: No se encontró el usuario en la base de datos.");
            throw new UsernameNotFoundException("Usuario no encontrado: " + cedula);
        }

        List<SimpleGrantedAuthority> authorities = usuario.getUsuarioRoles().stream()
                .map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRol().getCodigo().toUpperCase()))
                .collect(Collectors.toList());

        System.out.println(">>> ÉXITO: Usuario encontrado: " + usuario.getPersona().getNombres());
        System.out.println(">>> Roles: " + authorities);

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getContrasenaHash())
                .authorities(authorities)
                .build();
    }
}
