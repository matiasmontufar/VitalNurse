# Auditoría Técnica y Refactorización - VitalNurse

A continuación, se detalla el análisis exhaustivo del código fuente basado en los 8 pilares críticos de negocio y arquitectura. Se clasifica cada hallazgo por su gravedad y se aporta el código refactorizado listo para su integración.

---

## 1. Unificación de Interfaz y Menús (UI/UX)
**Gravedad: [MEJORA ESTÉTICA/UI]** 

**Hallazgo:** Actualmente existen múltiples vistas HTML dispersas (`calculadora-imc.html`, `calculadora-glasgow.html`, `calculadora-dosis.html`). Esto genera una mala experiencia de usuario (UX) al obligarlos a cambiar de página constantemente.
**Solución:** Consolidar todas las calculadoras y registros en un **único Dashboard Clínico (`dashboard-clinico.html`)** que utilice pestañas (Tabs) de Bootstrap/Tailwind.

**Archivo a crear/reemplazar:** `src/main/resources/templates/evaluacion-completa.html`

```html
<!-- Ejemplo de unificación con Bootstrap 5 (Pestañas) -->
<div class="container mt-4">
    <h2 class="mb-4"><i class="fas fa-stethoscope"></i> Dashboard Clínico Centralizado</h2>
    <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active" data-bs-toggle="pill" data-bs-target="#imc" type="button" role="tab">IMC</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" data-bs-toggle="pill" data-bs-target="#glasgow" type="button" role="tab">Glasgow</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" data-bs-toggle="pill" data-bs-target="#dosis" type="button" role="tab">Dosis</button>
        </li>
    </ul>
    
    <div class="tab-content" id="pills-tabContent">
        <!-- Tab IMC -->
        <div class="tab-pane fade show active" id="imc" role="tabpanel">
            <!-- Formulario IMC aquí (AJAX recomendado para no recargar) -->
        </div>
        <!-- Tab Glasgow -->
        <div class="tab-pane fade" id="glasgow" role="tabpanel">
            <!-- Formulario Glasgow aquí -->
        </div>
        <!-- Tab Dosis -->
        <div class="tab-pane fade" id="dosis" role="tabpanel">
            <!-- Formulario Dosis aquí -->
        </div>
    </div>
</div>
```

---

## 2. Validaciones Estrictas y Prevención de Datos Irreales
**Gravedad: [CRÍTICO]**

**Hallazgo:** En `Paciente.java` y `SignoVitalDTO.java`, algunas validaciones son laxas. Se puede aceptar presión arterial inválida y temperaturas no compatibles con la vida.
**Solución:** Endurecer las validaciones de Jakarta en el DTO (nunca validar en la Entidad directamente).

**Archivo a modificar:** `src/main/java/com/proyecto/vitalnurse/models/SignoVitalDTO.java`
```java
package com.proyecto.vitalnurse.models;

import jakarta.validation.constraints.*;

public class SignoVitalDTO {

    @NotNull(message = "El paciente es obligatorio")
    private Long idPaciente;

    @NotNull(message = "La temperatura es obligatoria")
    @DecimalMin(value = "30.0", message = "La temperatura debe ser mayor a 30.0°C (Incompatible con la vida)")
    @DecimalMax(value = "43.0", message = "La temperatura debe ser menor a 43.0°C (Incompatible con la vida)")
    private Double temperatura;

    @NotBlank(message = "La presión arterial es obligatoria")
    @Pattern(regexp = "^\\d{2,3}/\\d{2,3}$", message = "Formato de Presión Arterial inválido. Use el formato Sistólica/Diastólica (ej. 120/80)")
    private String presionArterial;

    @NotNull(message = "La frecuencia cardíaca es obligatoria")
    @Min(value = 30, message = "Frecuencia Cardíaca inferior al límite humano")
    @Max(value = 250, message = "Frecuencia Cardíaca superior al límite humano")
    private Integer frecuenciaCardiaca;

    // ... (Mantener las demás, agregando mensajes amigables)
}
```

---

## 3. Lógica Clínica y Rangos Médicos
**Gravedad: [CRÍTICO]**

**Hallazgo:** Falta automatizar la extracción de diagnósticos y alertas clínicas por presión, temperatura y ritmo cardíaco. No hay lógica para interpretar signos vitales.
**Solución:** Ampliar el servicio clínico para interpretar cada signo vital.

**Archivo a modificar:** `src/main/java/com/proyecto/vitalnurse/services/CalculadoraClinicaService.java`
```java
package com.proyecto.vitalnurse.services;

import org.springframework.stereotype.Service;

@Service
public class CalculadoraClinicaService {

    // ... (Mantener IMC y Glasgow) ...

    public String evaluarTemperatura(double temperatura) {
        if (temperatura < 35.0) return "Hipotermia (Alerta Médica)";
        if (temperatura <= 37.5) return "Normotermia (Normal)";
        if (temperatura <= 38.3) return "Febrícula (Observación)";
        if (temperatura <= 39.5) return "Fiebre (Atención Requerida)";
        return "Hiperpirexia (Emergencia)";
    }

    public String evaluarPresionArterial(String pa) {
        String[] partes = pa.split("/");
        int sistolica = Integer.parseInt(partes[0]);
        int diastolica = Integer.parseInt(partes[1]);

        if (sistolica < 90 && diastolica < 60) return "Hipotensión";
        if (sistolica <= 120 && diastolica <= 80) return "Presión Normal";
        if (sistolica <= 139 && diastolica <= 89) return "Prehipertensión";
        if (sistolica <= 159 && diastolica <= 99) return "Hipertensión Grado 1";
        return "Hipertensión Grado 2 (Crisis)";
    }

    public String evaluarFrecuenciaCardiaca(int fc) {
        if (fc < 60) return "Bradicardia";
        if (fc <= 100) return "Ritmo Normal";
        return "Taquicardia";
    }
}
```

---

## 4, 5 y 6. Arquitectura Spring Boot, Inyección y Persistencia (N+1, LazyInit)
**Gravedad: [CRÍTICO]**

**Hallazgos:** 
1. Los controladores usan `@Autowired` (no recomendado). Se debe usar inyección por constructor.
2. Se exponen y reciben Entidades JPA (`Paciente`) directamente en los Controllers, violando la separación de capas.
3. El problema N+1 y `LazyInitializationException` puede ocurrir al leer perfiles completos con sus historiales.

**Archivo a modificar:** `src/main/java/com/proyecto/vitalnurse/repositories/PacienteRepository.java`
```java
package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByCedula(String cedula);

    // FETCH JOIN PARA EVITAR EL PROBLEMA N+1 Y LAZY INITIALIZATION EXCEPTION
    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.evaluaciones LEFT JOIN FETCH p.signosVitales WHERE p.idPaciente = :id")
    Optional<Paciente> findByIdConHistorial(@Param("id") Long id);
}
```
*(Nota: Si usabas repositorios de Signos y Evaluaciones separados, puedes seguir usándolos, pero asegúrate de anotar los métodos de servicio con `@Transactional`)*.

**Archivo a modificar:** `src/main/java/com/proyecto/vitalnurse/controllers/PacienteController.java` (Fragmento)
```java
// ...
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
public class PacienteController {

    private final PacienteService pacienteService;
    private final PdfService pdfService;

    // 1. INYECCIÓN POR CONSTRUCTOR (Evitamos @Autowired)
    public PacienteController(PacienteService pacienteService, PdfService pdfService) {
        this.pacienteService = pacienteService;
        this.pdfService = pdfService;
    }

    @PostMapping("/pacientes/nuevo")
    public String guardarPaciente(@Valid @ModelAttribute("paciente") PacienteRegistroDTO pacienteDTO, BindingResult result, Model model) {
        // 2. USO DE DTO
        if (result.hasErrors()) {
            return "registro-paciente";
        }
        try {
            pacienteService.registrarDesdeDTO(pacienteDTO);
            model.addAttribute("mensajeExito", "Expediente del paciente creado exitosamente.");
            model.addAttribute("paciente", new PacienteRegistroDTO());
        } catch (PacienteDuplicadoException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("paciente", pacienteDTO);
        }
        return "registro-paciente";
    }
}
```

---

## 7. Manejo Global de Errores (@ControllerAdvice)
**Gravedad: [MODERADO]**

**Hallazgo:** El manejador global existe pero no intercepta errores de tipo `ConstraintViolationException`.
**Solución:** Añadir interceptores específicos.

**Archivo a modificar:** `src/main/java/com/proyecto/vitalnurse/exception/GlobalExceptionHandler.java` (Añadir este método)
```java
import jakarta.validation.ConstraintViolationException;

// ... dentro de la clase GlobalExceptionHandler ...

    @ExceptionHandler(ConstraintViolationException.class)
    public String manejarViolacionRestricciones(ConstraintViolationException ex, RedirectAttributes attrs) {
        StringBuilder errores = new StringBuilder("Error de validación médica: ");
        ex.getConstraintViolations().forEach(violacion -> 
            errores.append(violacion.getMessage()).append(". ")
        );
        attrs.addFlashAttribute("error", errores.toString());
        return "redirect:/pacientes"; 
    }
```
