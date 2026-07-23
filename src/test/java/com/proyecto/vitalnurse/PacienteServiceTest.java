package com.proyecto.vitalnurse;

import com.proyecto.vitalnurse.entity.catalogo.CatSignoVital;
import com.proyecto.vitalnurse.entity.catalogo.TipoEvaluacion;
import com.proyecto.vitalnurse.entity.catalogo.UnidadMedida;
import com.proyecto.vitalnurse.entity.clinical.EvaluacionCabecera;
import com.proyecto.vitalnurse.entity.clinical.EvaluacionResultado;
import com.proyecto.vitalnurse.entity.persona.Paciente;
import com.proyecto.vitalnurse.entity.persona.Persona;
import com.proyecto.vitalnurse.exception.PacienteDuplicadoException;
import com.proyecto.vitalnurse.exception.RecursoNoEncontradoException;
import com.proyecto.vitalnurse.models.PacienteRegistroDTO;
import com.proyecto.vitalnurse.models.PacienteVista;
import com.proyecto.vitalnurse.repositories.CatSignoVitalRepository;
import com.proyecto.vitalnurse.repositories.EvaluacionCabeceraRepository;
import com.proyecto.vitalnurse.repositories.EvaluacionResultadoRepository;
import com.proyecto.vitalnurse.repositories.PacienteRepository;
import com.proyecto.vitalnurse.repositories.PersonaRepository;
import com.proyecto.vitalnurse.repositories.TipoEvaluacionRepository;
import com.proyecto.vitalnurse.repositories.UnidadMedidaRepository;
import com.proyecto.vitalnurse.services.CalculadoraClinicaService;
import com.proyecto.vitalnurse.services.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private EvaluacionCabeceraRepository evaluacionCabeceraRepository;
    @Mock
    private EvaluacionResultadoRepository evaluacionResultadoRepository;
    @Mock
    private CatSignoVitalRepository catSignoVitalRepository;
    @Mock
    private TipoEvaluacionRepository tipoEvaluacionRepository;
    @Mock
    private UnidadMedidaRepository unidadMedidaRepository;

    private PacienteService pacienteService;
    private CalculadoraClinicaService calculadoraClinicaService;

    @BeforeEach
    void setUp() {
        calculadoraClinicaService = new CalculadoraClinicaService();
        pacienteService = new PacienteService(personaRepository, pacienteRepository,
                evaluacionCabeceraRepository, evaluacionResultadoRepository,
                catSignoVitalRepository, tipoEvaluacionRepository, unidadMedidaRepository,
                calculadoraClinicaService);
    }

    // --- IMC Tests ---

    @Test
    void calcularIMC_conParametrosValidos_retornaIMCCorrecto() {
        CalculadoraClinicaService.ResultadoIMC resultado = pacienteService.calcularIMC(70, 1.75);
        assertEquals(22.86, resultado.valor(), 0.01);
        assertTrue(resultado.clasificacion().contains("Peso normal"));
    }

    @Test
    void calcularIMC_conTallaCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> pacienteService.calcularIMC(70, 0));
    }

    @Test
    void calcularIMC_conTallaNegativa_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> pacienteService.calcularIMC(70, -1));
    }

    @Test
    void clasificarIMC_bajoPeso_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoIMC resultado = calculadoraClinicaService.calcularIMC(50, 1.70);
        assertTrue(resultado.clasificacion().contains("Bajo peso"));
    }

    @Test
    void clasificarIMC_pesoNormal_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoIMC resultado = calculadoraClinicaService.calcularIMC(65, 1.70);
        assertTrue(resultado.clasificacion().contains("Peso normal"));
    }

    @Test
    void clasificarIMC_sobrepeso_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoIMC resultado = calculadoraClinicaService.calcularIMC(80, 1.70);
        assertTrue(resultado.clasificacion().contains("Sobrepeso"));
    }

    @Test
    void clasificarIMC_obesidad_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoIMC resultado = calculadoraClinicaService.calcularIMC(100, 1.70);
        assertTrue(resultado.clasificacion().contains("Obesidad"));
    }

    // --- Glasgow Tests ---

    @Test
    void calcularGlasgow_valoresValidos_retornaSumaCorrecta() {
        CalculadoraClinicaService.ResultadoGlasgow resultado = pacienteService.calcularGlasgow(4, 5, 6);
        assertEquals(15, resultado.puntaje());
    }

    @Test
    void calcularGlasgow_valorOcularInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> pacienteService.calcularGlasgow(0, 5, 6));
    }

    @Test
    void calcularGlasgow_valorOcularExcedido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> pacienteService.calcularGlasgow(5, 5, 6));
    }

    @Test
    void calcularGlasgow_valorVerbalInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> pacienteService.calcularGlasgow(4, 0, 6));
    }

    @Test
    void calcularGlasgow_valorMotorInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> pacienteService.calcularGlasgow(4, 5, 0));
    }

    @Test
    void clasificarGlasgow_tceLeve_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoGlasgow resultado = calculadoraClinicaService.calcularGlasgow(4, 5, 6);
        assertTrue(resultado.clasificacion().contains("TCE Leve"));
    }

    @Test
    void clasificarGlasgow_tceModerado_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoGlasgow resultado = calculadoraClinicaService.calcularGlasgow(4, 3, 3);
        assertTrue(resultado.clasificacion().contains("TCE Moderado"));
    }

    @Test
    void clasificarGlasgow_tceGrave_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoGlasgow resultado = calculadoraClinicaService.calcularGlasgow(2, 2, 3);
        assertTrue(resultado.clasificacion().contains("TCE Grave"));
    }

    @Test
    void clasificarGlasgow_puntajeGrave_retornaMensajeCorrecto() {
        CalculadoraClinicaService.ResultadoGlasgow resultado = calculadoraClinicaService.calcularGlasgow(1, 1, 1);
        assertEquals(3, resultado.puntaje());
        assertTrue(resultado.clasificacion().contains("TCE Grave"));
    }

    // --- Patient CRUD Tests ---

    @Test
    void registrarPaciente_conCedulaNueva_guardaExitosamente() {
        PacienteRegistroDTO dto = new PacienteRegistroDTO("1234567890", "Juan", "Perez", 30, "Masculino", "");

        when(personaRepository.existsByCedulaAndIsDeletedFalse("1234567890")).thenReturn(false);

        Persona persona = new Persona();
        persona.setIdPersona(1L);
        persona.setCedula("1234567890");
        when(personaRepository.save(any(Persona.class))).thenReturn(persona);

        Paciente paciente = new Paciente();
        paciente.setIdPaciente(1L);
        paciente.setPersona(persona);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteVista resultado = pacienteService.registrarPaciente(dto);

        assertNotNull(resultado);
        assertEquals("1234567890", resultado.getCedula());
        verify(personaRepository).save(any(Persona.class));
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void registrarPaciente_conCedulaDuplicada_lanzaExcepcion() {
        PacienteRegistroDTO dto = new PacienteRegistroDTO("1234567890", "Juan", "Perez", 30, "Masculino", "");

        when(personaRepository.existsByCedulaAndIsDeletedFalse("1234567890")).thenReturn(true);

        assertThrows(PacienteDuplicadoException.class,
                () -> pacienteService.registrarPaciente(dto));
        verify(personaRepository, never()).save(any());
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_conIdExistente_retornaPacienteVista() {
        Persona persona = new Persona();
        persona.setCedula("1234567890");

        Paciente paciente = new Paciente();
        paciente.setIdPaciente(1L);
        paciente.setPersona(persona);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        PacienteVista resultado = pacienteService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPaciente());
    }

    @Test
    void obtenerPorId_conIdInexistente_lanzaExcepcion() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> pacienteService.obtenerPorId(99L));
    }
}
