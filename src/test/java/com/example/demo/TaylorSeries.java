package com.example.demo;

import java.math.BigDecimal;
import java.util.Scanner;

public class TaylorSeries {

    public static double calculateFactorial(double x) {
        if (x == 0) return 1;
        return x * calculateFactorial(x - 1);
    }

    public static double sin(double x) {
        double mySin = 0;
        double sinVal = Math.sin(x);

        int s = 1;
        for (int i = 0; i < 10; i++) {
            double d = i * 2 + 1;

            mySin += s * Math.pow(x, d) / calculateFactorial(d);

            System.out.println("series  " + (i + 1) +
                    " calculate sin: " + mySin +
                    " ==> absolute val of diff == " + BigDecimal.valueOf(mySin - sinVal)
                                                                .abs()
                                                                .toPlainString()
            );

            s *= -1;
        }
        return mySin;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("You wanna enter celsius or radians? ");
        System.out.println("Enter c for celsius" + '\n' + "Enter r for radians");
        String choose = sc.next();

        System.out.println("Enter angle value");
        double num = sc.nextDouble();

        if (choose.equals("c")) {
            num *= 2 * Math.PI / 360;
        }
        System.out.println('\n'+"Calculated sin: " + sin(num));
    }
}



