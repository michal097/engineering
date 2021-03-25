package com.example.demo;


import lombok.var;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws Exception{
        var list = Files.lines(Paths.get("C:\\Users\\slusa\\OneDrive\\Pulpit\\ \\Bisia\\test.txt")).collect(Collectors.toList());
        prepareReadData(list);
    }

    public static void prepareReadData(List<String> list) {

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
                if(findUserData.get("vat id") == null) {
                    findUserData.put("vat id", list.get(i).replaceAll("VAT ID ", ""));
                }
            } else if (list.get(i).contains("Total gross price")) {

                findUserData.put("costs", list.get(i+1)
                        .replaceAll("EUR ", "")
                        .replaceAll(" ", "")
                        .replaceAll(",", "."));
            } else if (list.get(i).contains("Invoice No. ")) {
                findUserData.put("fvNr", list.get(i).split(" ")[2]);
            }
        }

        for(Map.Entry<String, String> map: findUserData.entrySet()){
            System.out.println(map.getKey() + " == " + map.getValue());
        }

    }
}
