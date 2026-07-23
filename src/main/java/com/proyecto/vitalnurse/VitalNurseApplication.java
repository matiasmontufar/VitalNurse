package com.proyecto.vitalnurse;

import com.proyecto.vitalnurse.entity.catalogo.*;
import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.entity.seguridad.Rol;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import com.proyecto.vitalnurse.entity.seguridad.UsuarioRol;
import com.proyecto.vitalnurse.repositories.*;
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
    public CommandLineRunner inicializarDatos(UsuarioRepository usuarioRepository,
                                              PersonaRepository personaRepository,
                                              RolRepository rolRepository,
                                              UsuarioRolRepository usuarioRolRepository,
                                              UnidadMedidaRepository unidadMedidaRepository,
                                              CatSignoVitalRepository catSignoVitalRepository,
                                              TipoEvaluacionRepository tipoEvaluacionRepository,
                                              GeneroRepository generoRepository,
                                              PasswordEncoder passwordEncoder) {
        return args -> {

            if (rolRepository.count() == 0) {
                Rol admin = new Rol(); admin.setCodigo("ADMIN"); admin.setNombre("Administrador"); admin.setDescripcion("Acceso total al sistema"); rolRepository.save(admin);
                Rol sup = new Rol(); sup.setCodigo("SUPERVISOR"); sup.setNombre("Supervisor"); sup.setDescripcion("Gestion de usuarios y pacientes"); rolRepository.save(sup);
                Rol enf = new Rol(); enf.setCodigo("ENFERMERO"); enf.setNombre("Enfermero"); enf.setDescripcion("Acceso a pacientes y evaluaciones"); rolRepository.save(enf);
                System.out.println(">>> Roles creados: ADMIN, SUPERVISOR, ENFERMERO");
            }

            if (generoRepository.count() == 0) {
                Genero m = new Genero(); m.setCodigo("M"); m.setNombre("Masculino"); generoRepository.save(m);
                Genero f = new Genero(); f.setCodigo("F"); f.setNombre("Femenino"); generoRepository.save(f);
                System.out.println(">>> Generos creados");
            }

            if (unidadMedidaRepository.count() == 0) {
                UnidadMedida temp = new UnidadMedida(); temp.setCodigo("C"); temp.setNombre("Grado Celsius"); temp.setSimbolo("°C"); temp.setCategoria("temperatura"); unidadMedidaRepository.save(temp);
                UnidadMedida lpm = new UnidadMedida(); lpm.setCodigo("LPM"); lpm.setNombre("Latidos por minuto"); lpm.setSimbolo("lpm"); lpm.setCategoria("frecuencia"); unidadMedidaRepository.save(lpm);
                UnidadMedida rpm = new UnidadMedida(); rpm.setCodigo("RPM"); rpm.setNombre("Respiraciones por minuto"); rpm.setSimbolo("rpm"); rpm.setCategoria("frecuencia"); unidadMedidaRepository.save(rpm);
                UnidadMedida pct = new UnidadMedida(); pct.setCodigo("%"); pct.setNombre("Porcentaje"); pct.setSimbolo("%"); pct.setCategoria("porcentaje"); unidadMedidaRepository.save(pct);
                UnidadMedida mgdl = new UnidadMedida(); mgdl.setCodigo("MGDL"); mgdl.setNombre("mg/dL"); mgdl.setSimbolo("mg/dL"); mgdl.setCategoria("concentracion"); unidadMedidaRepository.save(mgdl);
                UnidadMedida mmhg = new UnidadMedida(); mmhg.setCodigo("MMHG"); mmhg.setNombre("mm Hg"); mmhg.setSimbolo("mm Hg"); mmhg.setCategoria("presion"); unidadMedidaRepository.save(mmhg);
                UnidadMedida kg = new UnidadMedida(); kg.setCodigo("KG"); kg.setNombre("Kilogramo"); kg.setSimbolo("kg"); kg.setCategoria("masa"); unidadMedidaRepository.save(kg);
                UnidadMedida mcg = new UnidadMedida(); mcg.setCodigo("MCG"); mcg.setNombre("Microgramo"); mcg.setSimbolo("mcg"); mcg.setCategoria("masa"); unidadMedidaRepository.save(mcg);
                UnidadMedida mg = new UnidadMedida(); mg.setCodigo("MG"); mg.setNombre("Miligramo"); mg.setSimbolo("mg"); mg.setCategoria("masa"); unidadMedidaRepository.save(mg);
                UnidadMedida g = new UnidadMedida(); g.setCodigo("G"); g.setNombre("Gramo"); g.setSimbolo("g"); g.setCategoria("masa"); unidadMedidaRepository.save(g);
                UnidadMedida ml = new UnidadMedida(); ml.setCodigo("ML"); ml.setNombre("Mililitro"); ml.setSimbolo("ml"); ml.setCategoria("volumen"); unidadMedidaRepository.save(ml);
                System.out.println(">>> Unidades de medida creadas");
            }

            if (catSignoVitalRepository.count() == 0) {
                UnidadMedida uCel = unidadMedidaRepository.findByCodigo("C").get();
                UnidadMedida uLpm = unidadMedidaRepository.findByCodigo("LPM").get();
                UnidadMedida uRpm = unidadMedidaRepository.findByCodigo("RPM").get();
                UnidadMedida uPct = unidadMedidaRepository.findByCodigo("%").get();
                UnidadMedida uMgdl = unidadMedidaRepository.findByCodigo("MGDL").get();
                UnidadMedida uMmhg = unidadMedidaRepository.findByCodigo("MMHG").get();

                CatSignoVital t = new CatSignoVital(); t.setCodigo("TEMP"); t.setNombre("Temperatura"); t.setUnidad(uCel); t.setRangoMin(30.0); t.setRangoMax(45.0); t.setOrden(1); catSignoVitalRepository.save(t);
                CatSignoVital fc = new CatSignoVital(); fc.setCodigo("FC"); fc.setNombre("Frecuencia Cardiaca"); fc.setUnidad(uLpm); fc.setRangoMin(20.0); fc.setRangoMax(300.0); fc.setOrden(2); catSignoVitalRepository.save(fc);
                CatSignoVital fr = new CatSignoVital(); fr.setCodigo("FR"); fr.setNombre("Frecuencia Respiratoria"); fr.setUnidad(uRpm); fr.setRangoMin(4.0); fr.setRangoMax(100.0); fr.setOrden(3); catSignoVitalRepository.save(fr);
                CatSignoVital spo2 = new CatSignoVital(); spo2.setCodigo("SPO2"); spo2.setNombre("Saturacion de Oxigeno"); spo2.setUnidad(uPct); spo2.setRangoMin(30.0); spo2.setRangoMax(100.0); spo2.setOrden(4); catSignoVitalRepository.save(spo2);
                CatSignoVital gli = new CatSignoVital(); gli.setCodigo("GLI"); gli.setNombre("Glicemia"); gli.setUnidad(uMgdl); gli.setRangoMin(10.0); gli.setRangoMax(600.0); gli.setOrden(5); catSignoVitalRepository.save(gli);
                CatSignoVital pas = new CatSignoVital(); pas.setCodigo("PAS"); pas.setNombre("Presion Arterial Sistolica"); pas.setUnidad(uMmhg); pas.setRangoMin(40.0); pas.setRangoMax(300.0); pas.setOrden(6); catSignoVitalRepository.save(pas);
                CatSignoVital pad = new CatSignoVital(); pad.setCodigo("PAD"); pad.setNombre("Presion Arterial Diastolica"); pad.setUnidad(uMmhg); pad.setRangoMin(20.0); pad.setRangoMax(200.0); pad.setOrden(7); catSignoVitalRepository.save(pad);
                System.out.println(">>> Signos vitales del catalogo creados");
            }

            if (tipoEvaluacionRepository.count() == 0) {
                TipoEvaluacion imc = new TipoEvaluacion(); imc.setCodigo("IMC"); imc.setNombre("Triaje Nutricional (IMC)"); tipoEvaluacionRepository.save(imc);
                TipoEvaluacion glasgow = new TipoEvaluacion(); glasgow.setCodigo("GLASGOW"); glasgow.setNombre("Triaje Neurologico (Glasgow)"); tipoEvaluacionRepository.save(glasgow);
                TipoEvaluacion signos = new TipoEvaluacion(); signos.setCodigo("SIGNOS"); signos.setNombre("Evaluacion de Signos Vitales"); tipoEvaluacionRepository.save(signos);
                System.out.println(">>> Tipos de evaluacion creados");
            }

            if (!personaRepository.findByCedulaAndIsDeletedFalse("1753402161").isPresent()) {
                Persona p = new Persona();
                p.setCedula("1753402161");
                p.setNombres("Matias Jair");
                p.setApellidos("Montufar Ninahualpa");
                p.setEdad(30);
                p.setSexo("Masculino");
                p = personaRepository.save(p);

                Usuario u = new Usuario();
                u.setUsername("1753402161");
                u.setContrasenaHash(passwordEncoder.encode("12345"));
                u.setPersona(p);
                u.setActivo(true);
                u = usuarioRepository.save(u);

                Rol rolAdmin = rolRepository.findByCodigoAndIsDeletedFalse("ADMIN").get();
                usuarioRolRepository.save(new UsuarioRol(u, rolAdmin));

                Rol rolSupervisor = rolRepository.findByCodigoAndIsDeletedFalse("SUPERVISOR").get();
                usuarioRolRepository.save(new UsuarioRol(u, rolSupervisor));

                System.out.println(">>> Usuario admin creado: 1753402161 / 12345 (roles: ADMIN, SUPERVISOR)");
            } else {
                System.out.println(">>> Usuario admin ya existe");
            }
        };
    }
}
