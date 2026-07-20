package com.proyecto.vitalnurse.controllers;

import com.proyecto.vitalnurse.models.Evaluacion;
import com.proyecto.vitalnurse.models.Paciente;
import com.proyecto.vitalnurse.models.SignoVital;
import com.proyecto.vitalnurse.services.PacienteService;
import com.proyecto.vitalnurse.services.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller // Cambiado para poder gestionar las vistas de las páginas HTML
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PdfService pdfService;

    // Ruta para MOSTRAR el formulario de Admisión de un Nuevo Paciente
    @GetMapping("/pacientes/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "registro-paciente";
    }

    // Ruta para GUARDAR al nuevo paciente en la base de datos
    @PostMapping("/pacientes/nuevo")
    public String guardarPaciente(@ModelAttribute Paciente paciente, Model model) {
        pacienteService.guardar(paciente);
        model.addAttribute("mensajeExito", "Expediente del paciente creado exitosamente.");
        model.addAttribute("paciente", new Paciente());
        return "registro-paciente";
    }

    // Ruta para MOSTRAR la lista de pacientes
    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        List<Paciente> listaPacientes = pacienteService.obtenerTodos();
        model.addAttribute("pacientes", listaPacientes);
        return "listar-pacientes"; // Llamará al nuevo archivo HTML
    }

    // Ruta para MOSTRAR el perfil, evaluaciones y SIGNOS VITALES
    @GetMapping("/pacientes/perfil/{id}")
    @Transactional(readOnly = true)
    public String verPerfilPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.obtenerPorId(id);
        if (paciente == null) return "redirect:/pacientes";

        List<Evaluacion> historial = pacienteService.obtenerEvaluacionesPorPaciente(id);
        List<SignoVital> historialSignos = pacienteService.obtenerSignosPorPaciente(id);

        model.addAttribute("paciente", paciente);
        model.addAttribute("historial", historial);
        model.addAttribute("historialSignos", historialSignos);

        return "perfil-paciente";
    }

    // Ruta para MOSTRAR la calculadora de IMC con estética de registro
    @GetMapping("/calculadoras/imc")
    public String mostrarCalculadoraIMC(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "calculadora-imc";
    }

    // Ruta para PROCESAR y GUARDAR el IMC en la ficha del paciente
    @PostMapping("/calculadoras/imc")
    public String calcularIMC(
            @RequestParam Long idPaciente,
            @RequestParam double peso,
            @RequestParam double estatura,
            Model model) {

        // 1. Cálculo matemático del IMC
        double imc = peso / (estatura * estatura);
        imc = Math.round(imc * 100.0) / 100.0; // Redondear a 2 decimales

        // 2. Diagnóstico médico
        String diagnostico = "";
        if (imc < 18.5) {
            diagnostico = "Bajo Peso (Riesgo Nutricional)";
        } else if (imc >= 18.5 && imc <= 24.9) {
            diagnostico = "Peso Normal (Saludable)";
        } else if (imc >= 25 && imc <= 29.9) {
            diagnostico = "Sobrepeso (Precaución)";
        } else {
            diagnostico = "Obesidad (Riesgo Cardiovascular)";
        }

        // 3. Crear y guardar la evaluación
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTipo("Triaje Nutricional (IMC)");
        evaluacion.setResultado(String.valueOf(imc));
        evaluacion.setDiagnostico(diagnostico);
        
        pacienteService.guardarEvaluacion(idPaciente, evaluacion);

        // 4. Recargar la pantalla
        model.addAttribute("mensajeExito", "IMC de " + imc + " guardado exitosamente en el expediente del paciente.");
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("imcCalculado", imc);
        model.addAttribute("diagnostico", diagnostico);
        model.addAttribute("colorAlerta", imc < 18.5 ? "warning" : imc >= 25 ? "danger" : "success");

        return "calculadora-imc";
    }

    // Ruta para MOSTRAR la calculadora de Glasgow con estética de registro
    @GetMapping("/calculadoras/glasgow")
    public String mostrarCalculadoraGlasgow(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "calculadora-glasgow";
    }

    // Ruta para PROCESAR y GUARDAR el puntaje en la ficha del paciente
    @PostMapping("/calculadoras/glasgow")
    public String calcularGlasgow(
            @RequestParam Long idPaciente,
            @RequestParam int ocular,
            @RequestParam int verbal,
            @RequestParam int motora,
            Model model) {

        int puntajeTotal = ocular + verbal + motora;
        String nivelTrauma = "";
        
        if (puntajeTotal >= 13) {
            nivelTrauma = "Traumatismo Leve";
        } else if (puntajeTotal >= 9) {
            nivelTrauma = "Traumatismo Moderado";
        } else {
            nivelTrauma = "Traumatismo Severo (Riesgo)";
        }

        // Creamos el objeto Evaluacion para guardarlo en la base de datos
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTipo("Triaje Neurológico (Glasgow)");
        evaluacion.setResultado(puntajeTotal + " / 15");
        evaluacion.setDiagnostico(nivelTrauma);
        
        pacienteService.guardarEvaluacion(idPaciente, evaluacion);

        // Recargamos la pantalla con el mensaje de éxito y la lista de pacientes
        model.addAttribute("mensajeExito", "Evaluación de Glasgow guardada exitosamente en el expediente del paciente.");
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("puntajeTotal", puntajeTotal);
        model.addAttribute("nivelTrauma", nivelTrauma);
        model.addAttribute("colorAlerta", puntajeTotal >= 13 ? "success" : puntajeTotal >= 9 ? "warning" : "danger");

        return "calculadora-glasgow";
    }

    // Mostrar pantalla de edición
    @GetMapping("/pacientes/editar/{id}")
    public String mostrarEditarPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.obtenerPorId(id);
        if (paciente == null) return "redirect:/pacientes";
        model.addAttribute("paciente", paciente);
        return "editar-paciente";
    }

    // Procesar la actualización y devolver a la tabla
    @PostMapping("/pacientes/editar/{id}")
    public String actualizarPaciente(@PathVariable Long id, @ModelAttribute Paciente paciente) {
        pacienteService.actualizarPaciente(id, paciente);
        return "redirect:/pacientes";
    }

    // Eliminar paciente (solo supervisores)
    @PostMapping("/pacientes/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        pacienteService.eliminarPaciente(id);
        redirectAttrs.addFlashAttribute("mensajeExito", "Expediente del paciente eliminado correctamente.");
        return "redirect:/pacientes";
    }

    @GetMapping("/pacientes/perfil/{id}/pdf")
    public void descargarFichaPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Ficha_Clinica_Paciente_" + id + ".pdf");

        Paciente paciente = pacienteService.obtenerPorId(id);
        List<Evaluacion> historial = pacienteService.obtenerEvaluacionesPorPaciente(id);
        List<SignoVital> historialSignos = pacienteService.obtenerSignosPorPaciente(id);

        if (paciente != null) {
            pdfService.exportarFichaPdf(response, paciente, historial, historialSignos);
        }
    }

    @GetMapping("/pacientes/signos")
    public String mostrarFormularioSignos(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "registro-signos";
    }

    @PostMapping("/pacientes/signos")
    public String registrarSignos(@RequestParam Long idPaciente, @ModelAttribute SignoVital signoVital, Model model) {

        pacienteService.guardarSignosVitales(idPaciente, signoVital);

        model.addAttribute("mensajeExito", "Signos vitales registrados correctamente en el historial del paciente.");
        model.addAttribute("pacientes", pacienteService.obtenerTodos());

        return "registro-signos";
    }

    @GetMapping("/calculadoras/dosis")
    public String mostrarCalculadoraDosis() {
        return "calculadora-dosis";
    }

    @PostMapping("/calculadoras/dosis")
    public String calcularDosis(
            @RequestParam double dosisIndicada,
            @RequestParam String unidadDosis,
            @RequestParam double concentracion,
            @RequestParam String unidadConcentracion,
            @RequestParam double volumen,
            @RequestParam(required = false, defaultValue = "0") int tiempoMinutos,
            Model model) {

        double dosisEnMg = unidadDosis.equals("g") ? dosisIndicada * 1000 : dosisIndicada;
        double concentracionEnMg = unidadConcentracion.equals("g") ? concentracion * 1000 : concentracion;

        double volumenAdministrar = (dosisEnMg * volumen) / concentracionEnMg;

        double gotasPorMinuto = 0;
        double microgotasPorMinuto = 0;

        if (tiempoMinutos > 0) {
            gotasPorMinuto = (volumenAdministrar * 20) / tiempoMinutos;
            microgotasPorMinuto = (volumenAdministrar * 60) / tiempoMinutos;
        }

        model.addAttribute("resultadoMl", Math.round(volumenAdministrar * 100.0) / 100.0);
        model.addAttribute("resultadoGotas", Math.round(gotasPorMinuto));
        model.addAttribute("resultadoMicrogotas", Math.round(microgotasPorMinuto));

        model.addAttribute("dosisIndicada", dosisIndicada);
        model.addAttribute("concentracion", concentracion);
        model.addAttribute("volumen", volumen);
        model.addAttribute("tiempo", tiempoMinutos);

        return "calculadora-dosis";
    }
}