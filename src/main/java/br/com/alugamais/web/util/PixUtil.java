package br.com.alugamais.web.util;

import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PixUtil {

    public Response getPixKey(BigDecimal valorAluguel, LocalDateTime expirationDate) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"allowsMultiplePayments\":false,\"expirationDate\":\"" + expirationDate + "\",\"format\":\"ALL\",\"value\":" + valorAluguel + ",\"description\":\"ALUGUEL\"}");
        Request request = new Request.Builder()
                .url("https://sandbox.asaas.com/api/v3/pix/qrCodes/static")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("access_token", "$aact_MzkwODA2MWY2OGM3MWRlMDU2NWM3MzJlNzZmNGZhZGY6OjFjZjZkZDU2LTkxMGUtNDVmOC04NjUzLWUxZjIzNDFlMWQ1Mzo6JGFhY2hfMTAxZDdlNGMtYTFkYS00NDYyLWIzMjYtNjhkMGZhYzNkNmUw")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }
}
