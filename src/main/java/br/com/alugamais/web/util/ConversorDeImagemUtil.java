package br.com.alugamais.web.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ConversorDeImagemUtil {

    public static byte[] conversor(byte[] imagem){

        try {
            // Lendo a imagem original
            BufferedImage imagemOriginal = ImageIO.read(new ByteArrayInputStream(imagem));

            // Criando um ByteArrayOutputStream para escrever a imagem convertida
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if(imagemOriginal!=null) {
                // Convertendo a imagem para PNG e escrevendo no ByteArrayOutputStream
                ImageIO.write(imagemOriginal, "png", baos);
            }
            // Obtendo o array de bytes da imagem convertida
            byte[] imagemPng = baos.toByteArray();

            return imagemPng;

        }catch (IOException ex){
            return null;
        }
    }
}
