package com.proyecto.vitalnurse;

import com.proyecto.vitalnurse.models.Usuario;
import com.proyecto.vitalnurse.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class VitalNurseApplication {

    public static void main(String[] args) {
        SpringApplication.run(VitalNurseApplication.class, args);
    }

    @Bean
    public CommandLineRunner encriptarClavePorDefecto(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Usuario admin = usuarioRepository.findByCedula("1753402161").orElse(null);

            if (admin != null) {
                String claveSegura = passwordEncoder.encode("12345");
                admin.setContrasena(claveSegura);
                usuarioRepository.save(admin);
                System.out.println(">>> ÉXITO: Contraseña re-encriptada y guardada correctamente.");
            }
        };
    }
}
