package com.example.demo;

import java.util.stream.IntStream;

public class Fizz {
    public static void main(String ... a) {
        IntStream.range(1,101).forEach(l -> System.out.println(l%(3*5)==0 ? "FizzBuzz": l%3==0 ? "Fizz": l%5==0?"Buzz": String.valueOf(l) ));
    }

    static long factorial(long i){
        if(i == 0 ) return 1;
        return i * factorial(i-1);
    }
    static long fibonacci(long i){
        if(i<=2) return 1;
        System.out.println(i);
        return fibonacci(i-1) + fibonacci( i-2);
    }
}
