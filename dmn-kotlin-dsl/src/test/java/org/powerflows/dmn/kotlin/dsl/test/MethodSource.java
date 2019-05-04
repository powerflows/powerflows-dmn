package org.powerflows.dmn.kotlin.dsl.test;

public class MethodSource {

    private int counter = 0;

    public static String staticMethod(final Integer paramOne, final String paramTwo) {
        return "Result " + paramOne + " : " + paramTwo;
    }

    public String instanceMethod(final String paramOne) {
        return paramOne + " - " + counter++;
    }

}
