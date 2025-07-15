/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PayPalClient {

    private final String CLIENT_ID = "AbbF_BAyrn_Cs4imD-joRWRZKKuuYk0gVIbzsecnQptYnED-DIhibqcWNaEg92QVzHMK0ihkyED5_RJx";
    private final String CLIENT_SECRET = "EJsZdngs3eq4WXpyY7z_abPRss7glV0PQQEYhXu963-7cZSMl2mZK-gU_clZLyNB1zN5I6O3JlHPxv5J";
    private final String BASE_URL = "https://api-m.sandbox.paypal.com";
    private final ObjectMapper mapper = new ObjectMapper();

    public String obtenerToken() throws Exception {
        HttpPost post = new HttpPost(BASE_URL + "/v1/oauth2/token");
        String auth = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        post.setHeader("Authorization", "Basic " + encodedAuth);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new StringEntity("grant_type=client_credentials"));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            var response = client.execute(post);
            JsonNode json = mapper.readTree(response.getEntity().getContent());
            return json.get("access_token").asText();
        }
    }

    public String[] crearOrdenPago(String montoPEN) throws Exception {
        // Paso 1: Conversión de moneda PEN → USD (tipo cambio fijo)
        BigDecimal montoSoles = new BigDecimal(montoPEN);
        BigDecimal tipoCambio = new BigDecimal("3.80"); // Puedes usar un servicio real luego
        BigDecimal montoUSD = montoSoles.divide(tipoCambio, 2, RoundingMode.HALF_UP);

        String montoUSDStr = montoUSD.toPlainString();

        // Paso 2: Construcción del JSON para PayPal
        String cuerpo = """
        {
            "intent": "CAPTURE",
            "purchase_units": [{
                "amount": {
                    "currency_code": "USD",
                    "value": "%s"
                }
            }],
            "application_context": {
                "return_url": "https://www.ejemplo.com/success",
                "cancel_url": "https://www.ejemplo.com/cancel"
            }
        }
        """.formatted(montoUSDStr);

        // Paso 3: Enviar petición a PayPal
        String token = obtenerToken();
        HttpPost post = new HttpPost(BASE_URL + "/v2/checkout/orders");
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(cuerpo, StandardCharsets.UTF_8));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            var response = client.execute(post);
            JsonNode json = mapper.readTree(response.getEntity().getContent());

            System.out.println("Respuesta JSON de PayPal:\n" + json.toPrettyString());

            String orderId = json.get("id").asText();
            String approvalUrl = "";

            for (JsonNode link : json.get("links")) {
                if ("approve".equals(link.get("rel").asText())) {
                    approvalUrl = link.get("href").asText();
                    break;
                }
            }

            if (approvalUrl.isEmpty()) {
                throw new RuntimeException("No se encontró URL de aprobación.");
            }

            return new String[]{approvalUrl, orderId};
        }
    }

    public String capturarOrden(String orderId) throws Exception {
        String token = obtenerToken();
        HttpPost post = new HttpPost(BASE_URL + "/v2/checkout/orders/" + orderId + "/capture");
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            var response = client.execute(post);
            JsonNode json = mapper.readTree(response.getEntity().getContent());

            String status = json.get("status").asText();
            String id = json.get("id").asText();
            String monto = json.get("purchase_units").get(0).get("payments")
                    .get("captures").get(0).get("amount").get("value").asText();
            String moneda = json.get("purchase_units").get(0).get("payments")
                    .get("captures").get(0).get("amount").get("currency_code").asText();
            String fecha = json.get("purchase_units").get(0).get("payments")
                    .get("captures").get(0).get("create_time").asText();

            return """
                ID de Transacción: %s
                Estado: %s
                Monto: %s %s
                Fecha: %s
                """.formatted(id, status, monto, moneda, fecha);
        }
    }
}
