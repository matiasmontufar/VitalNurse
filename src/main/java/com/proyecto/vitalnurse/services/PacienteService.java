package com.proyecto.vitalnurse.services;

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

    // Lógica para registrar un paciente nuevo validando que no exista (Flujo Alterno 5.1)
    public Paciente registrarPaciente(Paciente nuevoPaciente) throws Exception {
        // Consultamos al repositorio si la cédula ya existe
        if (pacienteRepository.existsByCedula(nuevoPaciente.getCedula())) {
            // Fiel a tu documento, lanzamos un error si está duplicado
            throw new Exception("El paciente ya se encuentra registrado");
        }
        // Si no existe, lo guardamos en la base de datos
        return pacienteRepository.save(nuevoPaciente);
    }

    // Método para obtener la lista de todos los pacientes registrados
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    // Lógica para calcular el Índice de Masa Corporal (RF006)
    public double calcularIMC(double pesoKg, double tallaMetros) {
        if (tallaMetros <= 0) {
            return 0.0; // Validación de seguridad para evitar división por cero
        }
        // Aplicamos la fórmula matemática: Peso / Talla al cuadrado
        return pesoKg / (tallaMetros * tallaMetros);
    }
    // Lógica para clasificar el estado nutricional según el valor del IMC (RF006)
    public String clasificarIMC(double imc) {
        if (imc < 18.5) {
            return "Bajo peso (Alerta nutricional)";
        } else if (imc >= 18.5 && imc < 24.9) {
            return "Peso normal (Saludable)";
        } else if (imc >= 24.9 && imc < 29.9) {
            return "Sobrepeso (Preaución)";
        } else {
            return "Obesidad (Alerta médica de alto riesgo)";
        }
    }

    // Lógica para calcular la puntuación total de Glasgow (RF007)
    public int calcularGlasgow(int ocular, int verbal, int motor) {
        return ocular + verbal + motor;
    }

    // Lógica para clasificar el estado de conciencia según el puntaje total
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

    // Método para buscar un paciente específico por su ID para ver su historial
    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    // Método para crear y guardar una evaluación en el historial del paciente
    public void guardarEvaluacionClinica(Paciente paciente, String tipo, String resultado, String diagnostico) {
        Evaluacion nuevaEvaluacion = new Evaluacion();
        nuevaEvaluacion.setPaciente(paciente);
        nuevaEvaluacion.setTipo(tipo);
        nuevaEvaluacion.setResultado(resultado);
        nuevaEvaluacion.setDiagnostico(diagnostico);
        
        evaluacionRepository.save(nuevaEvaluacion);
    }

    // Método para obtener el historial clínico completo de un paciente ordenado por fecha
    public List<Evaluacion> obtenerEvaluacionesPorPaciente(Long idPaciente) {
        return evaluacionRepository.findByPacienteIdPacienteOrderByFechaDesc(idPaciente);
    }

    // Método para guardar un nuevo paciente (sin validación de duplicado)
    public void guardar(Paciente paciente) {
        pacienteRepository.save(paciente);
    }

    // Método para eliminar un paciente por su ID
    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }

    // Método para actualizar los datos de un paciente (RF3)
    public void actualizarPaciente(Long id, Paciente pacienteActualizado) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente != null) {
            paciente.setNombres(pacienteActualizado.getNombres());
            paciente.setApellidos(pacienteActualizado.getApellidos());
            paciente.setEdad(pacienteActualizado.getEdad());
            paciente.setSexo(pacienteActualizado.getSexo());
            paciente.setEnfermedadesPreexistentes(pacienteActualizado.getEnfermedadesPreexistentes());
            pacienteRepository.save(paciente);
        }
    }

    // Método para guardar un nuevo registro de signos vitales (RF4)
    public void guardarSignosVitales(Long idPaciente, SignoVital signos) {
        Paciente paciente = obtenerPorId(idPaciente);
        if (paciente != null) {
            signos.setPaciente(paciente);
            signoVitalRepository.save(signos);
        }
    }

    // Método para guardar una nueva evaluación clínica (Ej: Glasgow, IMC)
    public void guardarEvaluacion(Long idPaciente, Evaluacion evaluacion) {
        Paciente paciente = obtenerPorId(idPaciente);
        if (paciente != null) {
            evaluacion.setPaciente(paciente);
            evaluacionRepository.save(evaluacion);
        }
    }

    // Método para obtener el historial de signos vitales (RF5)
    public List<SignoVital> obtenerSignosPorPaciente(Long idPaciente) {
        return signoVitalRepository.findByPacienteIdPacienteOrderByFechaDesc(idPaciente);
    }
}