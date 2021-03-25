package com.example.demo.payments;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WiseTransfer {

    private final String TOKEN = "2bf4b3fd-1714-4a92-a757-1ba692c50ac2";

    private static RestTemplate restTemplate = new RestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();


    public WiseTransfer(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    private String getProfileId() {

        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Object> request = new HttpEntity<>(headers);
        String profiles = "https://api.sandbox.transferwise.tech/v1/profiles";
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

    private String createQuote(double amount, String targetCurr) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, Object> map = new HashMap<>();
        map.put("sourceCurrency", "EUR");
        map.put("targetCurrency", targetCurr);
        map.put("sourceAmount", amount);
        map.put("targetAmount", null);
        map.put("profile", this.getProfileId());

        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        String quotes = "https://api.sandbox.transferwise.tech/v2/quotes";
        ResponseEntity<Object> response = restTemplate.postForEntity(quotes, entity, Object.class);

        return getId(response);
    }

    private String createRecipient(String iban, String name, String targetCurr) {

        Map<String, Object> detailsMap = new HashMap<>();

        detailsMap.put("iban",iban);

        Map<String, Object> map = new HashMap<>();
        map.put("currency", targetCurr);
        map.put("type", "iban");
        map.put("profile", getProfileId());
        map.put("accountHolderName", name.replaceAll("_"," "));
        map.put("legalType", "PRIVATE");
        map.put("details", detailsMap);

        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        String accounts = "https://api.sandbox.transferwise.tech/v1/accounts";
        ResponseEntity<Object> response = restTemplate.postForEntity(accounts, entity, Object.class);

        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(checkHttpStatus(response)).toString().replaceAll("=", ":"));
        return jsonObject.get("id").toString();
    }

    private String createTransfer(double amount, String targetCurr, String name, String iban) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);


        Map<String, Object> map = new HashMap<>();

        map.put("targetAccount", createRecipient(iban, name, targetCurr));
        map.put("quoteUuid", Objects.requireNonNull(createQuote(amount, targetCurr)));
        map.put("customerTransactionId", UUID.randomUUID());


        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        String transfers = "https://api.sandbox.transferwise.tech/v1/transfers";
        ResponseEntity<Object> response = restTemplate.postForEntity(transfers, entity, Object.class);

        return getId(response);

    }

    private String getId(ResponseEntity<Object> response) {
        String responseBody = Objects.requireNonNull(checkHttpStatus(response)).toString();
        String[] series = responseBody.split(",");
        for (String s : series) {
            if (s.contains("id=")) {
                return s.substring(4);
            }
        }
        return null;
    }

    private Object checkHttpStatus(ResponseEntity<Object> response) {
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else return null;
    }

    public String fundTransfer(double amount, String targetCurr, String name, String iban) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, Object> map = new HashMap<>();
        map.put("type", "BALANCE");

        HttpEntity<Object> entity = new HttpEntity<>(map, headers);

        String fund = "https://api.sandbox.transferwise.tech/v3/profiles/{profileId}/transfers/{transferId}/payments";
        ResponseEntity<Object> response = restTemplate.postForEntity(fund, entity, Object.class, getProfileId(), createTransfer(amount, targetCurr, name, iban));

        String [] stat = Objects.requireNonNull(checkHttpStatus(response)).toString().split(",");
        for(String s: stat){
            if(s.contains("status=")){
                log.info("Payment has been finished");
                return s.substring(8);
            }
        }
        log.error("error during making payment");
        return null;
    }
}



