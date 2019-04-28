package org.powerflows.dmn.kotlin.dsl.test;

public class MethodSource {

    private int counter = 0;

    public static String staticMethod(Integer paramOne, String paramTwo) {
        return "Result " + paramOne + " : " + paramTwo;
    }

    public String instanceMethod(String paramOne) {
        return paramOne + " - " + counter++;
    }

}
