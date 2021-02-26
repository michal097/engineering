package com.example.demo;

public class TestClass implements TestInter{

    private String something;

    public String getSomething(){
        return something;
    }
    public TestClass(String something){
        this.something=something;
    }
    @Override
    public TestInter makeSomething() {
        return new TestClass("aaa");
    }

    public static TestInter print(){
        return new TestClass("bbbbb");
    }



    @Override
    public void some(){
        System.out.println("asdasdasd");
    }
    @Override
    public String toString(){
        return TestClass.class.toString() + " " + this.something;
    }
}
