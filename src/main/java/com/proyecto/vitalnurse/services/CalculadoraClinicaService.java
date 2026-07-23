package com.proyecto.vitalnurse.services;

import org.springframework.stereotype.Service;

@Service
public class CalculadoraClinicaService {

    public ResultadoIMC calcularIMC(double pesoKg, double tallaMetros) {
        if (tallaMetros <= 0) {
            throw new IllegalArgumentException("La estatura debe ser mayor a cero");
        }
        double imc = Math.round((pesoKg / (tallaMetros * tallaMetros)) * 100.0) / 100.0;
        return new ResultadoIMC(imc, clasificarIMC(imc));
    }

    private String clasificarIMC(double imc) {
        if (imc < 18.5) return "Bajo peso (Alerta nutricional)";
        if (imc < 25.0) return "Peso normal (Saludable)";
        if (imc < 30.0) return "Sobrepeso (Precaucion)";
        return "Obesidad (Alerta medica de alto riesgo)";
    }

    public ResultadoGlasgow calcularGlasgow(int ocular, int verbal, int motor) {
        if (ocular < 1 || ocular > 4) throw new IllegalArgumentException("Respuesta ocular debe ser 1-4");
        if (verbal < 1 || verbal > 5) throw new IllegalArgumentException("Respuesta verbal debe ser 1-5");
        if (motor < 1 || motor > 6) throw new IllegalArgumentException("Respuesta motora debe ser 1-6");
        int puntaje = ocular + verbal + motor;
        return new ResultadoGlasgow(puntaje, clasificarGlasgow(puntaje));
    }

    private String clasificarGlasgow(int puntaje) {
        if (puntaje >= 13 && puntaje <= 15) {
            return "TCE Leve (Paciente orientado/estable)";
        } else if (puntaje >= 9 && puntaje <= 12) {
            return "TCE Moderado (Alerta: Requiere monitorizacion estricta)";
        } else if (puntaje >= 3 && puntaje <= 8) {
            return "TCE Grave (Emergencia Medica: Requiere intubacion/soporte inmediato)";
        } else {
            return "Puntaje invalido";
        }
    }

    public record ResultadoIMC(double valor, String clasificacion) {}
    public record ResultadoGlasgow(int puntaje, String clasificacion) {}
}
