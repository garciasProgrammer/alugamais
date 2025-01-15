package br.com.alugamais.web.util;

import java.math.BigDecimal;

public class ConversorDeMoedaParaBigDecimal {

    public static BigDecimal converterParaBigDecimal(String valor) {
        if (valor == null || valor.isEmpty()) {
            return null; // ou pode lançar uma exceção, dependendo da lógica
        }

        // Remover o prefixo "R$", espaços em branco e separadores de milhares
        String valorLimpo = valor.replace("R$", "").replace(".", "").replace(",", ".").trim();

        // Converter para BigDecimal
        return new BigDecimal(valorLimpo);
    }
}
