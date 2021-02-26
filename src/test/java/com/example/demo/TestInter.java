package com.example.demo;

public interface TestInter {

    TestInter makeSomething();
    void some();

    default String s(){
        return "nnn";
    };
}
