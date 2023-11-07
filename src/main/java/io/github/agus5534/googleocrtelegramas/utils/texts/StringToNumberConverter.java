package io.github.agus5534.googleocrtelegramas.utils.texts;

import java.util.HashMap;
import java.util.Map;

public class StringToNumberConverter {

    public static int convert(String value) {
        if ("-1".equals(value)) {
            return -1;
        }

        if (isNumeric(value)) {
            return Integer.parseInt(value);
        } else {
            return customStringToNumber(value);
        }
    }
    private static int customStringToNumber(String value) {
        String lowerCaseValue = value.toLowerCase();
        StringBuilder result = new StringBuilder();
        boolean foundMinusOne = false;

        for (char character : lowerCaseValue.toCharArray()) {
            if (foundMinusOne) {
                return -1;
            }

            if (Character.isDigit(character)) {
                result.append(character);
            } else {
                Integer mappedValue = charToNumberMap.get(character);
                if (mappedValue != null) {
                    result.append(mappedValue);
                } else {
                    result.append("-1");
                    foundMinusOne = true;
                }
            }
        }
        return Integer.parseInt(result.toString());
    }


    private static boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static final Map<Character, Integer> charToNumberMap = new HashMap<>();
    static {
        charToNumberMap.put('o', 0);
        charToNumberMap.put('c', 0);
        charToNumberMap.put('l', 1);
        charToNumberMap.put('n', 1);
        charToNumberMap.put('i', 1);
        charToNumberMap.put('|', 1);
        charToNumberMap.put('(', 1);
        charToNumberMap.put('z', 2);
        charToNumberMap.put('e', 6);
        charToNumberMap.put('b', 6);
        charToNumberMap.put('G', 6);
        charToNumberMap.put('a', 4);
        charToNumberMap.put('y', 4);
        charToNumberMap.put('v', 4);
        charToNumberMap.put('h', 4);
        charToNumberMap.put('u', 4);
        charToNumberMap.put('k', 4);
        charToNumberMap.put('\u0446', 4);
        charToNumberMap.put('\u0438', 4);
        charToNumberMap.put('s', 5);
        charToNumberMap.put('g', 9);
        charToNumberMap.put('q', 9);
        charToNumberMap.put('p', 9);
        charToNumberMap.put('B', 13);
        charToNumberMap.put('?', 7);
        charToNumberMap.put('\u0E32', 7);
        charToNumberMap.put('\u3131', 7);
        charToNumberMap.put('+', 7);
        charToNumberMap.put('&', 8);
    }

}
