package com.example.demo.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import net.minidev.json.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.mongodb.util.BsonUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class WiseTransfer implements Serializable {
    final static String TOKEN = "2bf4b3fd-1714-4a92-a757-1ba692c50ac2";
    final static String profiles = "https://api.sandbox.transferwise.tech/v1/profiles";
    final static String quotes = "https://api.sandbox.transferwise.tech/v2/quotes";
    final static String accounts = "https://api.sandbox.transferwise.tech/v1/accounts";
    private static RestTemplate restTemplate = new RestTemplate();

    public WiseTransfer(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }
    public static String getProfileId() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<Object> reponse = restTemplate.exchange(profiles, HttpMethod.GET, request, Object.class);

        String resp =  reponse.getStatusCode() == HttpStatus.OK ? Objects.requireNonNull(reponse.getBody()).toString() : null;
        JSONArray json = new JSONArray(Objects.requireNonNull(resp).replaceAll("=",":"));

        for(int i=0; i<json.length(); i++){
            JSONObject o = json.getJSONObject(i);
            if(o.get("id") != null){
                return o.get("id").toString();
            }
        }
        return null;
    }

    public static String createQuote() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, Object> map = new HashMap<>();
        map.put("sourceCurrency", "GBP");
        map.put("targetCurrency", "USD");
        map.put("sourceAmount", 100);
        map.put("targetAmount", null);
        map.put("profile", getProfileId());

        HttpEntity entity = new HttpEntity<>(map,headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(quotes, entity, Object.class);
        System.out.println(response.getBody().toString().replaceAll("=",":"));
        JSONObject jsonObject = new JSONObject(response.getBody().toString().replaceAll("=",":"));
        return null;
    }

    public static String createRecipient(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put("sortCode", "231470");
        detailsMap.put("accountNumber", "28821822");


        Map<String, Object> map = new HashMap<>();
        map.put("currency","GBP");
        map.put("type", "sort_code");
        map.put("profile", getProfileId());
        map.put("accountHolderName", "Ann Johnson");
        map.put("legalType", "PRIVATE");
        map.put("details", detailsMap);

        HttpEntity entity = new HttpEntity<>(map,headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(accounts, entity, Object.class);

        System.out.println(response.getBody());
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.getBody()).toString().replaceAll("=",":"));
        return jsonObject.get("id").toString();
    }

    public static void createTransfer(){

    }



    public static void main(String[] args) {
        System.out.println("profile id : " + getProfileId());
        System.out.println("quote uuid:: " + createQuote());
        System.out.println("recipient id "+ createRecipient());
    }


}



