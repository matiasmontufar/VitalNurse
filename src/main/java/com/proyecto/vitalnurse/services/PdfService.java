package com.proyecto.vitalnurse.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.proyecto.vitalnurse.models.Evaluacion;
import com.proyecto.vitalnurse.models.Paciente;
import com.proyecto.vitalnurse.models.SignoVital;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class PdfService {

    public void exportarFichaPdf(HttpServletResponse response, Paciente paciente, List<Evaluacion> historial, List<SignoVital> historialSignos) throws IOException {
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
        document.add(new Paragraph("Nombre: " + paciente.getNombres() + " " + paciente.getApellidos(), fontCuerpo));
        document.add(new Paragraph("Cédula: " + paciente.getCedula() + " | Edad: " + paciente.getEdad() + " | Sexo: " + paciente.getSexo(), fontCuerpo));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Monitoreo de Signos Vitales", fontSubtitulo));
        document.add(new Paragraph(" ", fontCuerpo));

        PdfPTable tablaSignos = new PdfPTable(7);
        tablaSignos.setWidthPercentage(100);
        String[] cabecerasSignos = {"Fecha", "P. Arterial", "FC", "FR", "Temp.", "O2", "Glicemia"};
        for (String cabecera : cabecerasSignos) {
            PdfPCell cell = new PdfPCell(new Phrase(cabecera, fontCabecera));
            cell.setBackgroundColor(new Color(234, 236, 244));
            tablaSignos.addCell(cell);
        }

        if (historialSignos.isEmpty()) {
            PdfPCell emptyCell = new PdfPCell(new Phrase("Sin registros.", fontCelda));
            emptyCell.setColspan(7);
            tablaSignos.addCell(emptyCell);
        } else {
            for (SignoVital sv : historialSignos) {
                tablaSignos.addCell(new Phrase(sv.getFechaFormateada(), fontCelda));
                tablaSignos.addCell(new Phrase(sv.getPresionArterial(), fontCelda));
                tablaSignos.addCell(new Phrase(sv.getFrecuenciaCardiaca() + " lpm", fontCelda));
                tablaSignos.addCell(new Phrase(sv.getFrecuenciaRespiratoria() + " rpm", fontCelda));
                tablaSignos.addCell(new Phrase(sv.getTemperatura() + " C", fontCelda));
                tablaSignos.addCell(new Phrase(sv.getSaturacionOxigeno() + " %", fontCelda));
                tablaSignos.addCell(new Phrase(sv.getGlicemia() + " mg/dl", fontCelda));
            }
        }
        document.add(tablaSignos);
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
            for (Evaluacion eval : historial) {
                tablaEval.addCell(new Phrase(eval.getFechaFormateada(), fontCelda));
                tablaEval.addCell(new Phrase(eval.getTipo(), fontCelda));
                tablaEval.addCell(new Phrase(eval.getResultado(), fontCelda));
                tablaEval.addCell(new Phrase(eval.getDiagnostico(), fontCelda));
            }
        }
        document.add(tablaEval);

        document.close();
    }
}
