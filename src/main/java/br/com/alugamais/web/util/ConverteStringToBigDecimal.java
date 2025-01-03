package br.com.alugamais.web.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ConverteStringToBigDecimal {

    public static BigDecimal parse(String valor) {
        BigDecimal bd = convertToBigDecimal(valor);
        return bd;
    }

    private static BigDecimal convertToBigDecimal(String number) {
        String normalizedNumber;

        // Verifica se contém ambos separadores (, e .)
        if (number.contains(",") && number.contains(".")) {
            // Remove separador de milhar e substitui separador decimal por ponto
            normalizedNumber = number.replaceAll("\\.", "").replace(',', '.');
        } else {
            // Verifica se o separador é decimal ou de milhar baseado na quantidade de dígitos após ele
            int lastIndexOfComma = number.lastIndexOf(',');
            int lastIndexOfDot = number.lastIndexOf('.');
            if ((lastIndexOfComma > -1 && number.length() - lastIndexOfComma - 1 == 2) ||
                    (lastIndexOfDot > -1 && number.length() - lastIndexOfDot - 1 == 2)) {
                // É um separador decimal
                normalizedNumber = number.replace(',', '.');
            } else {
                // É um separador de milhar ou não tem separador
                normalizedNumber = number.replaceAll("[,\\.]", "");
            }
        }

        return new BigDecimal(normalizedNumber);
    }

    public static String formatToBrazilianReal(BigDecimal value) {
        Locale brazil = new Locale("pt", "BR");
        NumberFormat realFormat = NumberFormat.getCurrencyInstance(brazil);
        return realFormat.format(value);
    }
}
