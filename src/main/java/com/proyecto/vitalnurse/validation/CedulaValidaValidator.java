package com.proyecto.vitalnurse.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CedulaValidaValidator implements ConstraintValidator<CedulaValida, String> {

    @Override
    public boolean isValid(String cedula, ConstraintValidatorContext context) {
        if (cedula == null) return false;

        cedula = cedula.trim();

        if (!cedula.matches("\\d{10}")) return false;

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) return false;

        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito >= 6) return false;

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int producto = digito * coeficientes[i];
            suma += (producto >= 10) ? producto - 9 : producto;
        }

        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int residuo = suma % 10;
        int digitoCalculado = (residuo == 0) ? 0 : 10 - residuo;

        return digitoCalculado == digitoVerificador;
    }
}
