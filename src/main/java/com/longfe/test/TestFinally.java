package com.longfe.test;

public class TestFinally {

    public static void main(String[] args) {
        String name = getName();
        System.out.println(name);
    }

    static String getName() {
        try{
            return returnName();

        } finally {
            System.out.println("finally");
        }
    }

    static String returnName() {
        System.out.println(".............");
        return "lonfe";
    }

}
