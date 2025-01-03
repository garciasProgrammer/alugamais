package br.com.alugamais.web.util;

public class NumeroPorExtenso {

    public static String numeroPorExtenso(int numero) {
        if (numero == 0) {
            return "zero";
        }

        String[] unidades = {"", "um", "dois", "três", "quatro", "cinco", "seis",
                "sete", "oito", "nove", "dez", "onze", "doze", "treze",
                "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"};

        String[] dezenas = {"", "", "vinte", "trinta", "quarenta", "cinquenta",
                "sessenta", "setenta", "oitenta", "noventa"};

        String[] centenas = {"", "cem", "duzentos", "trezentos", "quatrocentos",
                "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"};

        if (numero < 0) {
            return "menos " + numeroPorExtenso(-numero);
        }

        if (numero < 20) {
            return unidades[numero];
        }

        if (numero < 100) {
            int resto = numero % 10;
            if (resto == 0) {
                return dezenas[numero / 10];
            } else {
                return dezenas[numero / 10] + " e " + unidades[resto];
            }
        }

        if (numero < 1000) {
            int resto = numero % 100;
            if (resto == 0) {
                return centenas[numero / 100];
            } else {
                return centenas[numero / 100] + " e " + numeroPorExtenso(resto);
            }
        }

        if (numero < 1000000) {
            int resto = numero % 1000;
            if (resto == 0) {
                return numeroPorExtenso(numero / 1000) + " mil";
            } else {
                return numeroPorExtenso(numero / 1000) + " mil e " + numeroPorExtenso(resto);
            }
        }

        return "Número muito grande!";
    }
}

