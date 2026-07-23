package com.proyecto.vitalnurse.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.proyecto.vitalnurse.entity.clinical.EvaluacionCabecera;
import com.proyecto.vitalnurse.entity.persona.Paciente;
import com.proyecto.vitalnurse.entity.persona.Persona;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfService {

    private final PacienteService pacienteService;

    public PdfService(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public void exportarFichaPdf(HttpServletResponse response, Paciente paciente) throws IOException {
        Persona per = paciente.getPersona();
        List<EvaluacionCabecera> historial = pacienteService.obtenerEvaluacionesPorPaciente(paciente.getIdPaciente());

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(78, 115, 223));
        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.DARK_GRAY);
        Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK);
        Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);

        Paragraph titulo = new Paragraph("Reporte Clínico - VitalNurse", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        document.add(new Paragraph("Información del Paciente", fontSubtitulo));
        document.add(new Paragraph("Nombre: " + per.getNombres() + " " + per.getApellidos(), fontCuerpo));
        document.add(new Paragraph("Cédula: " + per.getCedula() + " | Edad: " + per.getEdad() + " | Sexo: " + per.getSexo(), fontCuerpo));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Evaluaciones Clínicas (Triaje)", fontSubtitulo));
        document.add(new Paragraph(" ", fontCuerpo));

        PdfPTable tablaEval = new PdfPTable(4);
        tablaEval.setWidthPercentage(100);
        String[] cabecerasEval = {"Fecha", "Evaluación", "Resultado", "Diagnóstico"};
        for (String cabecera : cabecerasEval) {
            PdfPCell cell = new PdfPCell(new Phrase(cabecera, fontCabecera));
            cell.setBackgroundColor(new Color(234, 236, 244));
            tablaEval.addCell(cell);
        }

        if (historial.isEmpty()) {
            PdfPCell emptyCell = new PdfPCell(new Phrase("Sin evaluaciones.", fontCelda));
            emptyCell.setColspan(4);
            tablaEval.addCell(emptyCell);
        } else {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (EvaluacionCabecera eval : historial) {
                tablaEval.addCell(new Phrase(eval.getFechaHora().format(fmt), fontCelda));
                tablaEval.addCell(new Phrase(eval.getTipoEvaluacion() != null ? eval.getTipoEvaluacion().getNombre() : "", fontCelda));
                tablaEval.addCell(new Phrase(eval.getResultado() != null ? eval.getResultado().getResultadoTexto() : "", fontCelda));
                tablaEval.addCell(new Phrase(eval.getResultado() != null ? eval.getResultado().getDiagnostico() : "", fontCelda));
            }
        }
        document.add(tablaEval);

        document.close();
    }
}
