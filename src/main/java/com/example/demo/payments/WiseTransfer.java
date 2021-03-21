package com.example.demo.payments;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class WiseTransfer {

    final static String TOKEN = "2bf4b3fd-1714-4a92-a757-1ba692c50ac2";
    final static String profiles = "https://api.sandbox.transferwise.tech/v1/profiles";
    final static String quotes = "https://api.sandbox.transferwise.tech/v2/quotes";
    final static String accounts = "https://api.sandbox.transferwise.tech/v1/accounts";
    final static String transfers = "https://api.sandbox.transferwise.tech/v1/transfers";
    final static String fund = "https://api.sandbox.transferwise.tech/v3/profiles/{profileId}/transfers/{transferId}/payments";

    private static RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();


    public WiseTransfer(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    private static String getProfileId() {

        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<Object> response = restTemplate.exchange(profiles, HttpMethod.GET, request, Object.class);

        String resp = Objects.requireNonNull(checkHttpStatus(response)).toString();

        JSONArray json = new JSONArray(Objects.requireNonNull(resp).replaceAll("=", ":"));

        for (int i = 0; i < json.length(); i++) {
            JSONObject o = json.getJSONObject(i);
            if (o.get("id") != null) {
                return o.get("id").toString();
            }
        }
        return null;
    }

    private static String createQuote() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, Object> map = new HashMap<>();
        map.put("sourceCurrency", "GBP");
        map.put("targetCurrency", "GBP");
        map.put("sourceAmount", 100);
        map.put("targetAmount", null);
        map.put("profile", getProfileId());

        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(quotes, entity, Object.class);

        return getId(response);
    }

    private static String createRecipient() {

        Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put("sortCode", "231470");
        detailsMap.put("accountNumber", "28821822");


        Map<String, Object> map = new HashMap<>();
        map.put("currency", "GBP");
        map.put("type", "sort_code");
        map.put("profile", getProfileId());
        map.put("accountHolderName", "Ann Johnson");
        map.put("legalType", "PRIVATE");
        map.put("details", detailsMap);

        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(accounts, entity, Object.class);

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(checkHttpStatus(response)).toString().replaceAll("=", ":"));
        return jsonObject.get("id").toString();
    }

    private static String createTransfer() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);


        Map<String, Object> map = new HashMap<>();

        map.put("targetAccount", createRecipient());
        map.put("quoteUuid", Objects.requireNonNull(createQuote()));
        map.put("customerTransactionId", UUID.randomUUID());


        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(transfers, entity, Object.class);

        return getId(response);

    }

    private static String getId(ResponseEntity<Object> response) {
        String responseBody = Objects.requireNonNull(checkHttpStatus(response)).toString();
        String[] series = responseBody.split(",");
        for (String s : series) {
            if (s.contains("id=")) {
                return s.substring(4);
            }
        }
        return null;
    }

    private static Object checkHttpStatus(ResponseEntity<Object> response) {
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else return null;
    }

    public static void fundTransfer() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, Object> map = new HashMap<>();
        map.put("type", "BALANCE");

        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(fund, entity, Object.class, getProfileId(), createTransfer());
        System.out.println("final transfer : " + checkHttpStatus(response));
    }


    public static void main(String[] args) {
        fundTransfer();
    }
}



