package com.example.demo.admin.service;

import com.example.demo.model.Client;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.InvoiceRepo;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.UserRoleRepository;
import com.example.demo.security.service.UserCreateService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCreateService userCreateService;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    InvoiceRepo invoiceRepo;

    public String makeUserName(String name, String surname) {
        String username = name.substring(0, 1).concat(surname.substring(1));

        try {
            Optional<Client> cl = clientRepository.findAllByUsername(username);
            if(cl.isPresent()) {
                username = surname.substring(0, 1).concat(name.substring(1));
            }
        }
        catch (Exception e){
            System.out.println("wyjabalo siem");
        }
        return username;
    }

    public Invoice prepareReadedData(List<String> list) {


        Map<String, String> findUserData = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toLowerCase().contains("nabywca")) {
                var nab = list.get(i + 1);

                String[] userData = nab.split(" ");
                if (userData.length > 2) {
                    findUserData.put("name", userData[0] + userData[1]);
                } else {
                    findUserData.put("name", userData[0]);
                }
                findUserData.put("surname", userData[userData.length - 1]);
            } else if (list.get(i).toLowerCase().contains("nip")) {
                findUserData.put("NIP", list.get(i).replaceAll("NIP ", ""));
                findUserData.put("bank", list.get(i + 2));
            } else if (list.get(i).contains("zaplaty")) {

                findUserData.put("costs", list.get(i).replaceAll("Do zaplaty: ", "")
                        .replaceAll(" PLN", "")
                        .replaceAll(" ", "")
                        .replaceAll(",", "."));
            } else if (list.get(i).contains("Faktura numer")) {
                findUserData.put("fvNr", list.get(i).split(" ")[2]);
            }
        }
        for (Map.Entry<String, String> map : findUserData.entrySet()) {
            System.out.println(map.getKey() + " ::: " + map.getValue());

        }

        return Invoice.builder().invName(findUserData.get("name"))
        .invSurname(findUserData.get("surname"))
        .NIP(findUserData.get("NIP"))
        .costs(Double.parseDouble(findUserData.get("costs")))
        .bankAccNumber(findUserData.get("bank"))
        .fvNumber(findUserData.get("fvNr"))
        .build();

    }
}
