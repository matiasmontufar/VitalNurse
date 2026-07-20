package com.proyecto.vitalnurse.controllers;

import com.proyecto.vitalnurse.models.Evaluacion;
import com.proyecto.vitalnurse.models.Paciente;
import com.proyecto.vitalnurse.models.SignoVital;
import com.proyecto.vitalnurse.services.PacienteService;
import com.proyecto.vitalnurse.services.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/pacientes/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "registro-paciente";
    }

    @PostMapping("/pacientes/nuevo")
    public String guardarPaciente(@Valid @ModelAttribute Paciente paciente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registro-paciente";
        }
        if (pacienteService.existsByCedula(paciente.getCedula())) {
            model.addAttribute("error", "Ya existe un paciente registrado con la cédula: " + paciente.getCedula());
            model.addAttribute("paciente", paciente);
            return "registro-paciente";
        }
        pacienteService.guardar(paciente);
        model.addAttribute("mensajeExito", "Expediente del paciente creado exitosamente.");
        model.addAttribute("paciente", new Paciente());
        return "registro-paciente";
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        List<Paciente> listaPacientes = pacienteService.obtenerTodos();
        model.addAttribute("pacientes", listaPacientes);
        return "listar-pacientes";
    }

    @GetMapping("/pacientes/perfil/{id}")
    @Transactional(readOnly = true)
    public String verPerfilPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.obtenerPorId(id);
        List<Evaluacion> historial = pacienteService.obtenerEvaluacionesPorPaciente(id);
        List<SignoVital> historialSignos = pacienteService.obtenerSignosPorPaciente(id);

        model.addAttribute("paciente", paciente);
        model.addAttribute("historial", historial);
        model.addAttribute("historialSignos", historialSignos);

        return "perfil-paciente";
    }

    @GetMapping("/calculadoras/imc")
    public String mostrarCalculadoraIMC(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "calculadora-imc";
    }

    @PostMapping("/calculadoras/imc")
    public String calcularIMC(
            @RequestParam Long idPaciente,
            @RequestParam double peso,
            @RequestParam double estatura,
            Model model) {

        model.addAttribute("pacientes", pacienteService.obtenerTodos());

        if (estatura <= 0) {
            model.addAttribute("error", "La estatura debe ser mayor a cero");
            return "calculadora-imc";
        }

        double imc = pacienteService.calcularIMC(peso, estatura);
        imc = Math.round(imc * 100.0) / 100.0;
        String diagnostico = pacienteService.clasificarIMC(imc);

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTipo("Triaje Nutricional (IMC)");
        evaluacion.setResultado(String.valueOf(imc));
        evaluacion.setDiagnostico(diagnostico);
        pacienteService.guardarEvaluacion(idPaciente, evaluacion);

        model.addAttribute("mensajeExito", "IMC de " + imc + " guardado exitosamente.");
        model.addAttribute("imcCalculado", imc);
        model.addAttribute("diagnostico", diagnostico);
        model.addAttribute("colorAlerta", imc < 18.5 ? "warning" : imc >= 25 ? "danger" : "success");

        return "calculadora-imc";
    }

    @GetMapping("/calculadoras/glasgow")
    public String mostrarCalculadoraGlasgow(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "calculadora-glasgow";
    }

    @PostMapping("/calculadoras/glasgow")
    public String calcularGlasgow(
            @RequestParam Long idPaciente,
            @RequestParam int ocular,
            @RequestParam int verbal,
            @RequestParam int motora,
            Model model) {

        model.addAttribute("pacientes", pacienteService.obtenerTodos());

        try {
            pacienteService.calcularGlasgow(ocular, verbal, motora);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "calculadora-glasgow";
        }

        int puntajeTotal = ocular + verbal + motora;
        String nivelTrauma;
        if (puntajeTotal >= 13) {
            nivelTrauma = "Traumatismo Leve";
        } else if (puntajeTotal >= 9) {
            nivelTrauma = "Traumatismo Moderado";
        } else {
            nivelTrauma = "Traumatismo Severo (Riesgo)";
        }

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTipo("Triaje Neurológico (Glasgow)");
        evaluacion.setResultado(puntajeTotal + " / 15");
        evaluacion.setDiagnostico(nivelTrauma);
        pacienteService.guardarEvaluacion(idPaciente, evaluacion);

        model.addAttribute("mensajeExito", "Evaluación de Glasgow guardada exitosamente.");
        model.addAttribute("puntajeTotal", puntajeTotal);
        model.addAttribute("nivelTrauma", nivelTrauma);
        model.addAttribute("colorAlerta", puntajeTotal >= 13 ? "success" : puntajeTotal >= 9 ? "warning" : "danger");

        return "calculadora-glasgow";
    }

    @GetMapping("/pacientes/editar/{id}")
    public String mostrarEditarPaciente(@PathVariable Long id, Model model) {
        model.addAttribute("paciente", pacienteService.obtenerPorId(id));
        return "editar-paciente";
    }

    @PostMapping("/pacientes/editar/{id}")
    public String actualizarPaciente(@PathVariable Long id, @Valid @ModelAttribute Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            return "editar-paciente";
        }
        pacienteService.actualizarPaciente(id, paciente);
        return "redirect:/pacientes";
    }

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

        pdfService.exportarFichaPdf(response, paciente, historial, historialSignos);
    }

    @GetMapping("/pacientes/signos")
    public String mostrarFormularioSignos(Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        return "registro-signos";
    }

    @PostMapping("/pacientes/signos")
    public String registrarSignos(@RequestParam Long idPaciente, @Valid @ModelAttribute SignoVital signoVital, BindingResult result, Model model) {
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        if (result.hasErrors()) {
            return "registro-signos";
        }
        pacienteService.guardarSignosVitales(idPaciente, signoVital);
        model.addAttribute("mensajeExito", "Signos vitales registrados correctamente.");
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

        if (dosisIndicada <= 0 || concentracion <= 0 || volumen <= 0) {
            model.addAttribute("error", "Dosis, concentración y volumen deben ser valores positivos");
            return "calculadora-dosis";
        }

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