package com.proyecto.vitalnurse.services;

import com.proyecto.vitalnurse.exception.PacienteDuplicadoException;
import com.proyecto.vitalnurse.exception.RecursoNoEncontradoException;
import com.proyecto.vitalnurse.models.Evaluacion;
import com.proyecto.vitalnurse.models.Paciente;
import com.proyecto.vitalnurse.models.SignoVital;
import com.proyecto.vitalnurse.repositories.EvaluacionRepository;
import com.proyecto.vitalnurse.repositories.PacienteRepository;
import com.proyecto.vitalnurse.repositories.SignoVitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private SignoVitalRepository signoVitalRepository;

    public Paciente registrarPaciente(Paciente nuevoPaciente) {
        if (pacienteRepository.existsByCedula(nuevoPaciente.getCedula())) {
            throw new PacienteDuplicadoException(nuevoPaciente.getCedula());
        }
        return pacienteRepository.save(nuevoPaciente);
    }

    public boolean existsByCedula(String cedula) {
        return pacienteRepository.existsByCedula(cedula);
    }

    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    public double calcularIMC(double pesoKg, double tallaMetros) {
        if (tallaMetros <= 0) {
            throw new IllegalArgumentException("La estatura debe ser mayor a cero");
        }
        return pesoKg / (tallaMetros * tallaMetros);
    }

    public String clasificarIMC(double imc) {
        if (imc < 18.5) {
            return "Bajo peso (Alerta nutricional)";
        } else if (imc < 25.0) {
            return "Peso normal (Saludable)";
        } else if (imc < 30.0) {
            return "Sobrepeso (Precaución)";
        } else {
            return "Obesidad (Alerta médica de alto riesgo)";
        }
    }

    public int calcularGlasgow(int ocular, int verbal, int motor) {
        if (ocular < 1 || ocular > 4) throw new IllegalArgumentException("Respuesta ocular debe ser 1-4");
        if (verbal < 1 || verbal > 5) throw new IllegalArgumentException("Respuesta verbal debe ser 1-5");
        if (motor < 1 || motor > 6) throw new IllegalArgumentException("Respuesta motora debe ser 1-6");
        return ocular + verbal + motor;
    }

    public String clasificarGlasgow(int puntaje) {
        if (puntaje >= 13 && puntaje <= 15) {
            return "TCE Leve (Paciente orientado/estable)";
        } else if (puntaje >= 9 && puntaje <= 12) {
            return "TCE Moderado (Alerta: Requiere monitorización estricta)";
        } else if (puntaje >= 3 && puntaje <= 8) {
            return "TCE Grave (Emergencia Médica: Requiere intubación/soporte inmediato)";
        } else {
            return "Puntaje inválido";
        }
    }

    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
    }

    public void guardarEvaluacionClinica(Paciente paciente, String tipo, String resultado, String diagnostico) {
        Evaluacion nuevaEvaluacion = new Evaluacion();
        nuevaEvaluacion.setFecha(java.time.LocalDateTime.now());
        nuevaEvaluacion.setPaciente(paciente);
        nuevaEvaluacion.setTipo(tipo);
        nuevaEvaluacion.setResultado(resultado);
        nuevaEvaluacion.setDiagnostico(diagnostico);
        evaluacionRepository.save(nuevaEvaluacion);
    }

    public List<Evaluacion> obtenerEvaluacionesPorPaciente(Long idPaciente) {
        return evaluacionRepository.findByPacienteIdPacienteOrderByFechaDesc(idPaciente);
    }

    public void guardar(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    public void eliminarPaciente(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Paciente", id);
        }
        pacienteRepository.deleteById(id);
    }

    public void actualizarPaciente(Long id, Paciente pacienteActualizado) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
        paciente.setNombres(pacienteActualizado.getNombres());
        paciente.setApellidos(pacienteActualizado.getApellidos());
        paciente.setEdad(pacienteActualizado.getEdad());
        paciente.setSexo(pacienteActualizado.getSexo());
        paciente.setEnfermedadesPreexistentes(pacienteActualizado.getEnfermedadesPreexistentes());
        pacienteRepository.save(paciente);
    }

    public void guardarSignosVitales(Long idPaciente, SignoVital signos) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", idPaciente));
        signos.setFecha(java.time.LocalDateTime.now());
        signos.setPaciente(paciente);
        signoVitalRepository.save(signos);
    }

    public void guardarEvaluacion(Long idPaciente, Evaluacion evaluacion) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", idPaciente));
        evaluacion.setFecha(java.time.LocalDateTime.now());
        evaluacion.setPaciente(paciente);
        evaluacionRepository.save(evaluacion);
    }

    public List<SignoVital> obtenerSignosPorPaciente(Long idPaciente) {
        return signoVitalRepository.findByPacienteIdPacienteOrderByFechaDesc(idPaciente);
    }
}