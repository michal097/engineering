package com.example.demo;


import lombok.var;

import java.util.Arrays;

class DemoApplicationTests {

    public static void main(String[] args) {
        String str = "Detected: Faktura F/016/02/2021\n" +
                "\n" +
                "Detected: numer\n" +
                "\n" +
                "Detected: Miejsce wystawienia: Warszawa\n" +
                "\n" +
                "Detected: Data wystawieria: 2021-02-01\n" +
                "\n" +
                "Detected: Data sprzedazy: 2021-02-01\n" +
                "\n" +
                "Detected: Platnosc: Przelew\n" +
                "\n" +
                "Detected: Termin platnosci: 2021-02-10\n" +
                "\n" +
                "Detected: Sprzedawca\n" +
                "\n" +
                "Detected: Nabywca\n" +
                "\n" +
                "Detected: Michal Krzysztof Slusarczyk\n" +
                "\n" +
                "Detected: Maciei Syguta\n" +
                "\n" +
                "Detected: Gwiazdzista 7B/22\n" +
                "\n" +
                "Detected: Chtodna 89/1\n" +
                "\n" +
                "Detected: 01-651 Warszawa, Polska\n" +
                "\n" +
                "Detected: 26-600 Radom\n" +
                "\n" +
                "Detected: NIP 5832500402\n" +
                "\n" +
                "Detected: mBank S.A\n" +
                "\n" +
                "Detected: 37 1140 2017 0000 4802 0940 6168\n" +
                "\n" +
                "Detected: Wartosc VAT\n" +
                "\n" +
                "Detected: LP Nazwa towaru ustugi\n" +
                "\n" +
                "Detected: Faktura\n" +
                "\n" +
                "Detected: numer\n" +
                "\n" +
                "Detected: F/016/02/2021\n" +
                "\n" +
                "Detected: Miejsce\n" +
                "\n" +
                "Detected: wystawienia:\n" +
                "\n" +
                "Detected: Warszawa\n" +
                "\n" +
                "Detected: Data\n" +
                "\n" +
                "Detected: wystawieria:\n" +
                "\n" +
                "Detected: 2021-02-01\n" +
                "\n" +
                "Detected: Data\n" +
                "\n" +
                "Detected: sprzedazy:\n" +
                "\n" +
                "Detected: 2021-02-01\n" +
                "\n" +
                "Detected: Platnosc:\n" +
                "\n" +
                "Detected: Przelew\n" +
                "\n" +
                "Detected: Termin\n" +
                "\n" +
                "Detected: platnosci:\n" +
                "\n" +
                "Detected: 2021-02-10\n" +
                "\n" +
                "Detected: Sprzedawca\n" +
                "\n" +
                "Detected: Nabywca\n" +
                "\n" +
                "Detected: Maciei\n" +
                "\n" +
                "Detected: Syguta\n" +
                "\n" +
                "Detected: Michal\n" +
                "\n" +
                "Detected: Krzysztof\n" +
                "\n" +
                "Detected: Slusarczyk\n" +
                "\n" +
                "Detected: Gwiazdzista\n" +
                "\n" +
                "Detected: 7B/22\n" +
                "\n" +
                "Detected: Chtodna\n" +
                "\n" +
                "Detected: 89/1\n" +
                "\n" +
                "Detected: 01-651\n" +
                "\n" +
                "Detected: Warszawa,\n" +
                "\n" +
                "Detected: Polska\n" +
                "\n" +
                "Detected: 26-600\n" +
                "\n" +
                "Detected: Radom\n" +
                "\n" +
                "Detected: NIP\n" +
                "\n" +
                "Detected: 5832500402\n" +
                "\n" +
                "Detected: mBank\n" +
                "\n" +
                "Detected: S.A\n" +
                "\n" +
                "Detected: 37\n" +
                "\n" +
                "Detected: 1140\n" +
                "\n" +
                "Detected: 2017\n" +
                "\n" +
                "Detected: 0000\n" +
                "\n" +
                "Detected: 4802\n" +
                "\n" +
                "Detected: 0940\n" +
                "\n" +
                "Detected: 6168\n" +
                "\n" +
                "Detected: Wartosc\n" +
                "\n" +
                "Detected: VAT\n" +
                "\n" +
                "Detected: LP\n" +
                "\n" +
                "Detected: Nazwa\n" +
                "\n" +
                "Detected: towaru\n" +
                "\n" +
                "Detected: ustugi\n" +
                "\n" +
                "Detected lines and words for 1.png\n" +
                "Detected: Faktura\n" +
                "\n" +
                "Detected: Nr: FVMK\n" +
                "\n" +
                "Detected: 1/12/2017\n" +
                "\n" +
                "Detected: Miejsce\n" +
                "\n" +
                "Detected: wystawienia: Poznan\n" +
                "\n" +
                "Detected: Data wystawienia: 06-12-2017\n" +
                "\n" +
                "Detected: Data sprzedazy: 06-12-2017\n" +
                "\n" +
                "Detected: Sprzedawca\n" +
                "\n" +
                "Detected: Nabywca\n" +
                "\n" +
                "Detected: P.H.U. Jan Kowalski\n" +
                "\n" +
                "Detected: Andrzej Nowak\n" +
                "\n" +
                "Detected: Pogodna 100/2\n" +
                "\n" +
                "Detected: Albanska 100/2\n" +
                "\n" +
                "Detected: 60-135 Poznan\n" +
                "\n" +
                "Detected: 134 Poznan\n" +
                "\n" +
                "Detected: NIP 4211123842\n" +
                "\n" +
                "Detected: NIP 9693835169\n" +
                "\n" +
                "Detected: Lp. Nazwa towaru uslugi J.m. llose Cena netto VAT Wartosc netto Wartosc Wartosc brutto\n" +
                "\n" +
                "Detected: towar szt. 1 100.00 23% 100,00\n" +
                "\n" +
                "Detected: Faktura\n" +
                "\n" +
                "Detected: Nr:\n" +
                "\n" +
                "Detected: FVMK\n" +
                "\n" +
                "Detected: 1/12/2017\n" +
                "\n" +
                "Detected: Miejsce\n" +
                "\n" +
                "Detected: wystawienia:\n" +
                "\n" +
                "Detected: Poznan\n" +
                "\n" +
                "Detected: Data\n" +
                "\n" +
                "Detected: wystawienia:\n" +
                "\n" +
                "Detected: 06-12-2017\n" +
                "\n" +
                "Detected: Data\n" +
                "\n" +
                "Detected: sprzedazy:\n" +
                "\n" +
                "Detected: 06-12-2017\n" +
                "\n" +
                "Detected: Sprzedawca\n" +
                "\n" +
                "Detected: Nabywca\n" +
                "\n" +
                "Detected: P.H.U.\n" +
                "\n" +
                "Detected: Jan Kowalski\n" +
                "\n" +
                "Detected: Andrzej\n" +
                "\n" +
                "Detected: Nowak\n" +
                "\n" +
                "Detected: Pogodna\n" +
                "\n" +
                "Detected: 100/2\n" +
                "\n" +
                "Detected: Albanska\n" +
                "\n" +
                "Detected: 100/2\n" +
                "\n" +
                "Detected: 60-135\n" +
                "\n" +
                "Detected: Poznan\n" +
                "\n" +
                "Detected: 134\n" +
                "\n" +
                "Detected: Poznan\n" +
                "\n" +
                "Detected: NIP\n" +
                "\n" +
                "Detected: 4211123842\n" +
                "\n" +
                "Detected: NIP\n" +
                "\n" +
                "Detected: 9693835169\n" +
                "\n" +
                "Detected: Lp.\n" +
                "\n" +
                "Detected: Nazwa\n" +
                "\n" +
                "Detected: towaru\n" +
                "\n" +
                "Detected: uslugi\n" +
                "\n" +
                "Detected: J.m.\n" +
                "\n" +
                "Detected: llose\n" +
                "\n" +
                "Detected: Cena\n" +
                "\n" +
                "Detected: netto\n" +
                "\n" +
                "Detected: VAT\n" +
                "\n" +
                "Detected: Wartosc\n" +
                "\n" +
                "Detected: netto\n" +
                "\n" +
                "Detected: Wartosc\n" +
                "\n" +
                "Detected: Wartosc brutto\n" +
                "\n" +
                "Detected: towar\n" +
                "\n" +
                "Detected: szt.\n" +
                "\n" +
                "Detected: 1\n" +
                "\n" +
                "Detected: 100.00\n" +
                "\n" +
                "Detected: 23%\n" +
                "\n" +
                "Detected: 100,00\n" +
                "\n" +
                "1.png\n";

        var splitter = str.replaceAll("Detected: ", "");
        var splitter2 = splitter.split("\n");
        System.out.println(splitter2.length);
        for(String spli: splitter2){
            System.out.println(spli);
        }
    }



}
