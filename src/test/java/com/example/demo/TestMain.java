package com.example.demo;

public class TestMain {
    public enum Some{
        ASSIGN
    }
    public static void main(String[] args) {
        TestInter testInter = new TestClass("asd");
        testInter.makeSomething().s();
        testInter.some();
        System.out.println(testInter.makeSomething().s());

        TestInter t = TestClass.print().makeSomething();

        TestInter ttt = testInter.makeSomething();
        System.out.println(ttt);
        System.out.println(t);
        TestInter tt = new TestInter() {
            @Override
            public TestInter makeSomething() {
                return null;
            }

            @Override
            public void some() {

            }
        };

        System.out.println("ASSIGN".equals(Some.ASSIGN.toString()));

        Interek i = (a,b) -> 1+2;
        System.out.println(i.doSomeThingCool(1,2));


    }
}
