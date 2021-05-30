package com.example.demo.admin.service;

import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class AdminService {

    private final ClientRepository clientRepository;

    @Autowired
    public AdminService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private static String deleteSpecialChars(String str) {
        return str.replaceAll("ą", "a")
                .replaceAll("ę", "e")
                .replaceAll("ć", "c")
                .replaceAll("ł", "l")
                .replaceAll("ó", "o");


    }

    public boolean searchExistingUsername(String username) {
        return clientRepository.findAllByUsername(username).isPresent();
    }

    public String makeUserName(String name, String surname) {
        StringBuilder username = new StringBuilder(deleteSpecialChars(name).substring(0, 1).concat(deleteSpecialChars(surname)));
        boolean isPres = searchExistingUsername(username.toString());
        int i = 0;
        while (isPres) {
            username.append(i);
            i++;
            isPres = searchExistingUsername(username.toString());
        }

        return username.toString();
    }

    public Invoice prepareReadData(List<String> list) {

        Map<String, String> findUserData = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toLowerCase().contains("buyer")) {
                var nab = list.get(i + 1);

                String[] userData = nab.split(" ");
                if (userData.length > 2) {
                    findUserData.put("name", userData[0] + userData[1]);
                } else {
                    findUserData.put("name", userData[0]);
                }
                findUserData.put("surname", userData[userData.length - 1]);
            } else if (list.get(i).toLowerCase().contains("vat id")) {
                findUserData.put("bank", list.get(i + 2));
                int finalI = i;
                findUserData.computeIfAbsent("vat id", k -> list.get(finalI).replaceAll("VAT ID ", ""));
            } else if (list.get(i).contains("Total gross price")) {

                findUserData.put("costs", list.get(i + 1)
                        .replaceAll("EUR ", "")
                        .replaceAll(" ", "")
                        .replaceAll(",", ""));
            } else if (list.get(i).contains("Invoice No. ")) {
                findUserData.put("fvNr", list.get(i).split(" ")[2]);
            }
        }
        log.info("retrieving data from invoice using ocr");

        return Invoice.builder().invName(findUserData.get("name"))
                .invSurname(findUserData.get("surname"))
                .NIP(findUserData.get("vat id"))
                .costs(Double.parseDouble(findUserData.get("costs")))
                .bankAccNumber(findUserData.get("bank"))
                .fvNumber(findUserData.get("fvNr"))
                .build();

    }
}
