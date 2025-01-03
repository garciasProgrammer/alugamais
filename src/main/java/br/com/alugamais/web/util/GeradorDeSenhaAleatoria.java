package br.com.alugamais.web.util;

import java.util.Random;

public class GeradorDeSenhaAleatoria {

    public static String senhaInicial() {

        Random random = new Random();
        int passwordInitial = random.nextInt(1900000) + 100000;
        return String.valueOf(passwordInitial);
    }
}
