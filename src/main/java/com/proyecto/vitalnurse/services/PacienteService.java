package com.proyecto.vitalnurse.services;

import com.proyecto.vitalnurse.entity.catalogo.CatSignoVital;
import com.proyecto.vitalnurse.entity.catalogo.TipoEvaluacion;
import com.proyecto.vitalnurse.entity.catalogo.UnidadMedida;
import com.proyecto.vitalnurse.entity.clinical.EvaluacionCabecera;
import com.proyecto.vitalnurse.entity.clinical.EvaluacionDetalleSigno;
import com.proyecto.vitalnurse.entity.clinical.EvaluacionResultado;
import com.proyecto.vitalnurse.entity.persona.Paciente;
import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import com.proyecto.vitalnurse.exception.PacienteDuplicadoException;
import com.proyecto.vitalnurse.exception.RecursoNoEncontradoException;
import com.proyecto.vitalnurse.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PersonaRepository personaRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final SignoVitalRepository signoVitalRepository;
    private final TipoEvaluacionRepository tipoEvaluacionRepository;
    private final CatSignoVitalRepository catSignoVitalRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;

    public PacienteService(PacienteRepository pacienteRepository, PersonaRepository personaRepository,
                           EvaluacionRepository evaluacionRepository, SignoVitalRepository signoVitalRepository,
                           TipoEvaluacionRepository tipoEvaluacionRepository,
                           CatSignoVitalRepository catSignoVitalRepository,
                           UnidadMedidaRepository unidadMedidaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.personaRepository = personaRepository;
        this.evaluacionRepository = evaluacionRepository;
        this.signoVitalRepository = signoVitalRepository;
        this.tipoEvaluacionRepository = tipoEvaluacionRepository;
        this.catSignoVitalRepository = catSignoVitalRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @Transactional
    public Paciente registrarPaciente(Persona persona) {
        if (personaRepository.existsByCedulaAndIsDeletedFalse(persona.getCedula())) {
            throw new PacienteDuplicadoException(persona.getCedula());
        }
        Persona savedPersona = personaRepository.save(persona);
        Paciente paciente = new Paciente();
        paciente.setPersona(savedPersona);
        return pacienteRepository.save(paciente);
    }

    public boolean existsByCedula(String cedula) {
        return personaRepository.existsByCedulaAndIsDeletedFalse(cedula);
    }

    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAllActiveWithPersona();
    }

    public double calcularIMC(double pesoKg, double tallaMetros) {
        if (tallaMetros <= 0) {
            throw new IllegalArgumentException("La estatura debe ser mayor a cero");
        }
        return pesoKg / (tallaMetros * tallaMetros);
    }

    public String clasificarIMC(double imc) {
        if (imc < 18.5) return "Bajo peso (Alerta nutricional)";
        else if (imc < 25.0) return "Peso normal (Saludable)";
        else if (imc < 30.0) return "Sobrepeso (Precaución)";
        else return "Obesidad (Alerta médica de alto riesgo)";
    }

    public int calcularGlasgow(int ocular, int verbal, int motor) {
        if (ocular < 1 || ocular > 4) throw new IllegalArgumentException("Respuesta ocular debe ser 1-4");
        if (verbal < 1 || verbal > 5) throw new IllegalArgumentException("Respuesta verbal debe ser 1-5");
        if (motor < 1 || motor > 6) throw new IllegalArgumentException("Respuesta motora debe ser 1-6");
        return ocular + verbal + motor;
    }

    public String clasificarGlasgow(int puntaje) {
        if (puntaje >= 13 && puntaje <= 15) return "TCE Leve (Paciente orientado/estable)";
        else if (puntaje >= 9 && puntaje <= 12) return "TCE Moderado (Alerta: Requiere monitorización estricta)";
        else if (puntaje >= 3 && puntaje <= 8) return "TCE Grave (Emergencia Médica: Requiere intubación/soporte inmediato)";
        else return "Puntaje inválido";
    }

    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
    }

    public Persona obtenerPersonaPorId(Long id) {
        Paciente pac = obtenerPorId(id);
        return pac.getPersona();
    }

    @Transactional
    public void guardarEvaluacionClinica(Paciente paciente, TipoEvaluacion tipo, String resultadoTexto, String diagnostico, Usuario usuario) {
        EvaluacionCabecera cabecera = new EvaluacionCabecera();
        cabecera.setPaciente(paciente);
        cabecera.setUsuario(usuario);
        cabecera.setTipoEvaluacion(tipo);
        cabecera.setNotas(diagnostico);
        cabecera = evaluacionRepository.save(cabecera);

        EvaluacionResultado resultado = new EvaluacionResultado();
        resultado.setCabecera(cabecera);
        resultado.setResultadoTexto(resultadoTexto);
        resultado.setDiagnostico(diagnostico);
        resultado.setCreatedAt(java.time.LocalDateTime.now());
        resultado = com.proyecto.vitalnurse.repositories.BeanResolver.evaluacionResultadoRepo.save(resultado);
        cabecera.setResultado(resultado);
        evaluacionRepository.save(cabecera);
    }

    @Transactional
    public EvaluacionCabecera crearEvaluacionConSignos(Long idPaciente, Long idUsuario, Long idTipoEval,
                                                        String notas, List<SignoVitalInput> signos) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", idPaciente));
        Usuario usuario = com.proyecto.vitalnurse.repositories.BeanResolver.usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", idUsuario));
        TipoEvaluacion tipo = tipoEvaluacionRepository.findById(idTipoEval)
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoEvaluacion", idTipoEval));

        EvaluacionCabecera cabecera = new EvaluacionCabecera();
        cabecera.setPaciente(paciente);
        cabecera.setUsuario(usuario);
        cabecera.setTipoEvaluacion(tipo);
        cabecera.setNotas(notas);
        cabecera = evaluacionRepository.save(cabecera);

        for (SignoVitalInput s : signos) {
            CatSignoVital cat = catSignoVitalRepository.findByCodigo(s.codigoSigno)
                    .orElseThrow(() -> new IllegalArgumentException("Signo vital no encontrado: " + s.codigoSigno));
            UnidadMedida uni = unidadMedidaRepository.findById(s.idUnidad)
                    .orElseThrow(() -> new IllegalArgumentException("Unidad no encontrada: " + s.idUnidad));

            EvaluacionDetalleSigno detalle = new EvaluacionDetalleSigno();
            detalle.setCabecera(cabecera);
            detalle.setSignoVital(cat);
            detalle.setValorMedido(BigDecimal.valueOf(s.valor));
            detalle.setUnidad(uni);
            signoVitalRepository.save(detalle);
        }
        return cabecera;
    }

    public List<EvaluacionCabecera> obtenerEvaluacionesPorPaciente(Long idPaciente) {
        return evaluacionRepository.findByPacienteIdPacienteAndIsDeletedFalseOrderByFechaHoraDesc(idPaciente);
    }

    @Transactional
    public void guardar(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    @Transactional
    public void eliminarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
        paciente.setIsDeleted(true);
        paciente.getPersona().setIsDeleted(true);
        pacienteRepository.save(paciente);
    }

    @Transactional
    public void actualizarPersonaPaciente(Long idPaciente, Persona personaActualizada) {
        Paciente paciente = obtenerPorId(idPaciente);
        Persona persona = paciente.getPersona();
        persona.setNombres(personaActualizada.getNombres());
        persona.setApellidos(personaActualizada.getApellidos());
        persona.setEdad(personaActualizada.getEdad());
        persona.setSexo(personaActualizada.getSexo());
        persona.setFechaNacimiento(personaActualizada.getFechaNacimiento());
        personaRepository.save(persona);
    }

    public record SignoVitalInput(String codigoSigno, double valor, Long idUnidad) {}
}