package br.com.alugamais.web.util;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CriptpgrafarSenhas {

    public static String encodeSenha(String senha){

        String encodeSenha = new BCryptPasswordEncoder().encode(senha);
        return encodeSenha;
    }

}
