package com.proyecto.vitalnurse.services;

import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.entity.seguridad.Rol;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import com.proyecto.vitalnurse.entity.seguridad.UsuarioRol;
import com.proyecto.vitalnurse.exception.UsuarioDuplicadoException;
import com.proyecto.vitalnurse.repositories.PersonaRepository;
import com.proyecto.vitalnurse.repositories.RolRepository;
import com.proyecto.vitalnurse.repositories.UsuarioRepository;
import com.proyecto.vitalnurse.repositories.UsuarioRolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository,
                          RolRepository rolRepository, UsuarioRolRepository usuarioRolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAllActiveWithPersona();
    }

    @Transactional
    public Usuario crearUsuario(Persona persona, String username, String password, String codigoRol) {
        if (usuarioRepository.findByUsernameAndActivoTrue(username).isPresent()) {
            throw new UsuarioDuplicadoException(username);
        }
        Persona savedPersona = personaRepository.save(persona);

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setContrasenaHash(passwordEncoder.encode(password));
        usuario.setPersona(savedPersona);
        usuario.setActivo(true);
        usuario = usuarioRepository.save(usuario);

        Rol rol = rolRepository.findByCodigoAndIsDeletedFalse(codigoRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + codigoRol));

        UsuarioRol usuarioRol = new UsuarioRol(usuario, rol);
        usuarioRolRepository.save(usuarioRol);

        return usuario;
    }

    @Transactional
    public void guardarUsuario(Usuario usuario, String rawPassword, String codigoRol) {
        if (usuarioRepository.findByUsernameAndActivoTrue(usuario.getUsername()).isPresent()) {
            throw new UsuarioDuplicadoException(usuario.getUsername());
        }
        usuario.setContrasenaHash(passwordEncoder.encode(rawPassword));
        usuario = usuarioRepository.save(usuario);

        Rol rol = rolRepository.findByCodigoAndIsDeletedFalse(codigoRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + codigoRol));
        usuarioRolRepository.save(new UsuarioRol(usuario, rol));
    }
}
