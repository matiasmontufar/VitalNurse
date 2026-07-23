package com.proyecto.vitalnurse.controllers;

import com.proyecto.vitalnurse.entity.clinical.EvaluacionCabecera;
import com.proyecto.vitalnurse.entity.persona.Paciente;
import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.models.EvaluacionVista;
import com.proyecto.vitalnurse.models.PacienteVista;
import com.proyecto.vitalnurse.models.SignoVitalVista;
import com.proyecto.vitalnurse.services.PacienteService;
import com.proyecto.vitalnurse.services.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PacienteController {

    private final PacienteService pacienteService;
    private final PdfService pdfService;

    public PacienteController(PacienteService pacienteService, PdfService pdfService) {
        this.pacienteService = pacienteService;
        this.pdfService = pdfService;
    }

    @GetMapping("/pacientes/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("persona", new Persona());
        return "registro-paciente";
    }

    @PostMapping("/pacientes/nuevo")
    public String guardarPaciente(@ModelAttribute Persona persona, Model model) {
        if (pacienteService.existsByCedula(persona.getCedula())) {
            model.addAttribute("error", "Ya existe un paciente registrado con la cédula: " + persona.getCedula());
            model.addAttribute("persona", persona);
            return "registro-paciente";
        }
        pacienteService.registrarPaciente(persona);
        model.addAttribute("mensajeExito", "Expediente del paciente creado exitosamente.");
        model.addAttribute("persona", new Persona());
        return "registro-paciente";
    }

    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        List<Paciente> lista = pacienteService.obtenerTodos();
        List<PacienteVista> vistas = lista.stream().map(p -> {
            Persona per = p.getPersona();
            return new PacienteVista(p.getIdPaciente(), per.getCedula(), per.getNombres(),
                    per.getApellidos(), per.getEdad(), per.getSexo(), p.getEnfermedades() != null ?
                    p.getEnfermedades().stream().map(Object::toString).collect(Collectors.joining(", ")) : "");
        }).collect(Collectors.toList());
        model.addAttribute("pacientes", vistas);
        return "listar-pacientes";
    }

    @GetMapping("/pacientes/perfil/{id}")
    public String verPerfilPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.obtenerPorId(id);
        Persona per = paciente.getPersona();

        List<EvaluacionCabecera> historial = pacienteService.obtenerEvaluacionesPorPaciente(id);
        List<EvaluacionVista> evalVistas = historial.stream().map(e -> {
            EvaluacionVista ev = new EvaluacionVista();
            ev.setFechaFormateada(e.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            ev.setTipo(e.getTipoEvaluacion() != null ? e.getTipoEvaluacion().getNombre() : "");
            ev.setResultado(e.getResultado() != null ? e.getResultado().getResultadoTexto() : "");
            ev.setDiagnostico(e.getResultado() != null ? e.getResultado().getDiagnostico() : "");
            return ev;
        }).collect(Collectors.toList());

        model.addAttribute("paciente", per);
        model.addAttribute("pacienteId", id);
        model.addAttribute("historial", evalVistas);

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

        model.addAttribute("mensajeExito", "IMC de " + imc + " calculado: " + diagnostico);
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

        model.addAttribute("mensajeExito", "Evaluación de Glasgow calculada: " + puntajeTotal + "/15");
        model.addAttribute("puntajeTotal", puntajeTotal);
        model.addAttribute("nivelTrauma", nivelTrauma);
        model.addAttribute("colorAlerta", puntajeTotal >= 13 ? "success" : puntajeTotal >= 9 ? "warning" : "danger");

        return "calculadora-glasgow";
    }

    @GetMapping("/pacientes/editar/{id}")
    public String mostrarEditarPaciente(@PathVariable Long id, Model model) {
        Paciente paciente = pacienteService.obtenerPorId(id);
        model.addAttribute("paciente", paciente);
        model.addAttribute("persona", paciente.getPersona());
        return "editar-paciente";
    }

    @PostMapping("/pacientes/editar/{id}")
    public String actualizarPaciente(@PathVariable Long id, @ModelAttribute Persona persona) {
        pacienteService.actualizarPersonaPaciente(id, persona);
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

        pdfService.exportarFichaPdf(response, paciente);
    }

    @GetMapping("/pacientes/signos")
    public String mostrarFormularioSignos(Model model) {
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