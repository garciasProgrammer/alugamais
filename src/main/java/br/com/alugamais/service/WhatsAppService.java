package br.com.alugamais.service;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WhatsAppService {

    private static final Logger logger = LogManager.getLogger(WhatsAppService.class);
    OkHttpClient client;

    public WhatsAppService() {
        this.client = new OkHttpClient();
    }

    public void enviarMensagem(String numero, String mensagem) throws IOException {

        String codigoPais = "55";
        String phone = codigoPais + numero.replace("(", "").replace(")", "");
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"phone\": \"" + phone + "\", \"message\": \"" + mensagem + "\"}");
        Request request = new Request.Builder()
                .url("https://api.z-api.io/instances/3AD7EE2AB129E0AF85798E7A33DE9B77/token/43AC1C4A70D13F1A2E310DAB/send-text")
                .post(body)
                .addHeader("client-token", "F678499cec29748c898d7bdbb2fa3f812S")
                .build();

        Response response = client.newCall(request).execute();
       logger.info("Enviando mensagem via whatsApp-api-AlugaMais");
        response.body().close();
    }

    public void enviarDocumento(String numero, String pathDoc, String idPgamanto) throws IOException {
        String codigoPais = "55";
        String phone = codigoPais + numero.replace("(", "").replace(")", "").replace("-","");
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"phone\": \"" + phone + "\", \"document\": \"data:application/pdf;base64," + pathDoc + "\" , \"fileName\": \"RECIBO DE LOCAÇÃO-" + idPgamanto + "\"}");
        Request request = new Request.Builder()
                .url("https://api.z-api.io/instances/3AD7EE2AB129E0AF85798E7A33DE9B77/token/43AC1C4A70D13F1A2E310DAB/send-document/pdf")
                .post(body)
                .addHeader("client-token", "F678499cec29748c898d7bdbb2fa3f812S")
                .build();

        Response response = client.newCall(request).execute();
        logger.info("Enviando documento via whatsApp-api-AlugaMais");
        response.body().close();
    }
}
