package br.com.alugamais.web.util;

import java.util.Random;

public class GeradorDeSecretKeyJWT {

    public static String secretKey() {

        Random random = new Random();
        int passwordInitial = random.nextInt(1900000) + 100000;
        return String.valueOf(passwordInitial);
    }
}
