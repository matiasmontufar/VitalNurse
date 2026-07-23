package com.proyecto.vitalnurse;

import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.entity.seguridad.Rol;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import com.proyecto.vitalnurse.entity.seguridad.UsuarioRol;
import com.proyecto.vitalnurse.exception.UsuarioDuplicadoException;
import com.proyecto.vitalnurse.models.UsuarioRegistroDTO;
import com.proyecto.vitalnurse.repositories.PersonaRepository;
import com.proyecto.vitalnurse.repositories.RolRepository;
import com.proyecto.vitalnurse.repositories.UsuarioRepository;
import com.proyecto.vitalnurse.repositories.UsuarioRolRepository;
import com.proyecto.vitalnurse.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private UsuarioRolRepository usuarioRolRepository;

    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioService = new UsuarioService(usuarioRepository, personaRepository, rolRepository, usuarioRolRepository, passwordEncoder);
    }

    @Test
    void guardarUsuario_conCedulaNueva_guardaExitosamente() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("1234567890", "Admin User", "password123");
        dto.setRol("ENFERMERO");

        when(personaRepository.existsByCedulaAndIsDeletedFalse("1234567890")).thenReturn(false);
        when(personaRepository.save(any(Persona.class))).thenAnswer(i -> i.getArgument(0));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));
        when(rolRepository.findByCodigoAndIsDeletedFalse("ENFERMERO")).thenReturn(Optional.of(new Rol(2L)));
        when(usuarioRolRepository.save(any(UsuarioRol.class))).thenReturn(null);

        usuarioService.guardarUsuario(dto);

        verify(personaRepository).save(any(Persona.class));
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioRolRepository).save(any(UsuarioRol.class));
    }

    @Test
    void guardarUsuario_conCedulaDuplicada_lanzaExcepcion() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO("1234567890", "Admin", "password123");

        when(personaRepository.existsByCedulaAndIsDeletedFalse("1234567890")).thenReturn(true);

        assertThrows(UsuarioDuplicadoException.class,
                () -> usuarioService.guardarUsuario(dto));
        verify(personaRepository, never()).save(any());
        verify(usuarioRepository, never()).save(any());
    }
}
