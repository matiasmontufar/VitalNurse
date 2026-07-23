package com.proyecto.vitalnurse.services;

import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DosisService {

    private static final double ML_POR_LITRO = 1000.0;
    private static final double ML_POR_CC = 1.0;

    private static final double MCG_POR_MG = 1000.0;
    private static final double MG_POR_G = 1000.0;

    private static final int GOTAS_POR_ML = 20;
    private static final int MICROGOTAS_POR_GOTA = 3;
    private static final int MICROGOTAS_POR_ML = 60;

    private static final int MINUTOS_POR_HORA = 60;

    private static final Map<String, Double> FACTORES_MASA = new LinkedHashMap<>();
    static {
        FACTORES_MASA.put("mcg", 0.001);
        FACTORES_MASA.put("mg", 1.0);
        FACTORES_MASA.put("g", 1000.0);
    }

    private static final Map<String, String> UNIDADES_BASE = new LinkedHashMap<>();
    static {
        UNIDADES_BASE.put("mcg", "mg");
        UNIDADES_BASE.put("mg", "mg");
        UNIDADES_BASE.put("g", "mg");
    }

    public double convertirAMg(double valor, String unidad) {
        if (!FACTORES_MASA.containsKey(unidad)) {
            throw new IllegalArgumentException("Unidad de medida no válida: " + unidad);
        }
        return valor * FACTORES_MASA.get(unidad);
    }

    public double convertirDesdeMg(double valorMg, String unidadDestino) {
        if (!FACTORES_MASA.containsKey(unidadDestino)) {
            throw new IllegalArgumentException("Unidad de medida no válida: " + unidadDestino);
        }
        return valorMg / FACTORES_MASA.get(unidadDestino);
    }

    public String convertirPresentacionASistemaDosis(double presentacion, String unidadPresentacion, String unidadDosis) {
        if (unidadPresentacion.equals(unidadDosis)) {
            return presentacion + "|" + unidadDosis;
        }
        double presentacionEnMg = convertirAMg(presentacion, unidadPresentacion);
        double presentacionConvertida = convertirDesdeMg(presentacionEnMg, unidadDosis);
        return presentacionConvertida + "|" + unidadDosis;
    }

    public double calcularVolumenAdministrar(double dosisIndicada, String unidadDosis,
                                              double presentacion, String unidadPresentacion,
                                              double diluyenteMl) {
        if (dosisIndicada <= 0) throw new IllegalArgumentException("La dosis indicada debe ser mayor a cero");
        if (presentacion <= 0) throw new IllegalArgumentException("La concentración debe ser mayor a cero");
        if (diluyenteMl <= 0) throw new IllegalArgumentException("El diluyente debe ser mayor a cero");

        double dosisEnMg = convertirAMg(dosisIndicada, unidadDosis);
        double presentacionEnMg = convertirAMg(presentacion, unidadPresentacion);

        return Math.round((dosisEnMg * diluyenteMl / presentacionEnMg) * 100.0) / 100.0;
    }

    public double calcularMlPorHora(double volumenMl, double horas) {
        if (horas <= 0) throw new IllegalArgumentException("Las horas totales deben ser mayores a cero");
        if (volumenMl <= 0) throw new IllegalArgumentException("El volumen debe ser mayor a cero");
        return Math.round((volumenMl / horas) * 100.0) / 100.0;
    }

    public double calcularGotasPorMinuto(double volumenMl, double horas) {
        if (horas <= 0) throw new IllegalArgumentException("Las horas totales deben ser mayores a cero");
        if (volumenMl <= 0) throw new IllegalArgumentException("El volumen debe ser mayor a cero");
        double resultado = (volumenMl * GOTAS_POR_ML) / (horas * MINUTOS_POR_HORA);
        return Math.round(resultado * 100.0) / 100.0;
    }

    public double calcularMicrogotasPorMinuto(double volumenMl, double horas) {
        if (horas <= 0) throw new IllegalArgumentException("Las horas totales deben ser mayores a cero");
        if (volumenMl <= 0) throw new IllegalArgumentException("El volumen debe ser mayor a cero");
        double resultado = (volumenMl * MICROGOTAS_POR_ML) / (horas * MINUTOS_POR_HORA);
        return Math.round(resultado * 100.0) / 100.0;
    }

    public Map<String, Object> calcularDosisCompleto(double dosisIndicada, String unidadDosis,
                                                      double presentacion, String unidadPresentacion,
                                                      double diluyenteMl, double horas) {
        Map<String, Object> resultados = new LinkedHashMap<>();

        double dosisEnMg = convertirAMg(dosisIndicada, unidadDosis);
        double presentacionEnMg = convertirAMg(presentacion, unidadPresentacion);

        double volumenMl = (dosisEnMg * diluyenteMl) / presentacionEnMg;
        volumenMl = Math.round(volumenMl * 100.0) / 100.0;
        resultados.put("volumenMl", volumenMl);

        if (horas > 0) {
            double mlPorHora = calcularMlPorHora(volumenMl, horas);
            double gotasPorMin = calcularGotasPorMinuto(volumenMl, horas);
            double microgotasPorMin = calcularMicrogotasPorMinuto(volumenMl, horas);
            resultados.put("mlPorHora", mlPorHora);
            resultados.put("gotasPorMinuto", gotasPorMin);
            resultados.put("microgotasPorMinuto", microgotasPorMin);
        }

        String presentacionConvertida = presentacion + " " + unidadPresentacion;
        if (!unidadPresentacion.equals(unidadDosis)) {
            double pConv = convertirDesdeMg(presentacionEnMg, unidadDosis);
            presentacionConvertida = pConv + " " + unidadDosis + " (original: " + presentacion + " " + unidadPresentacion + ")";
        }
        resultados.put("presentacionConvertida", presentacionConvertida);
        resultados.put("dosisEnMg", dosisEnMg);

        return resultados;
    }

    public double getGOTAS_POR_ML() { return GOTAS_POR_ML; }
    public double getMICROGOTAS_POR_ML() { return MICROGOTAS_POR_ML; }
    public double getMINUTOS_POR_HORA() { return MINUTOS_POR_HORA; }
}
