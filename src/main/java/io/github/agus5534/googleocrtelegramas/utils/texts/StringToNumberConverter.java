package io.github.agus5534.googleocrtelegramas.utils.texts;

public class StringToNumberConverter {

    public static int convert(String value) {
        if ("-1".equals(value)) {
            return -1;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return customStringToNumber(value);
        }
    }

    private static int customStringToNumber(String value) {
        String lowerCaseValue = value.toLowerCase();
        StringBuilder result = new StringBuilder();

        for (char character : lowerCaseValue.toCharArray()) {
            switch (character) {
                case 'o', 'c':
                    result.append('0');
                    break;
                case 'l', 'n', 'i', '|', '/':
                    result.append('1');
                    break;
                case 'z':
                    result.append('2');
                    break;
                case 'e', 'b':
                    result.append('6');
                    break;
                case 'a', 'y', 'v', 'h', 'u', 'k', '\u0446', '\u0438':
                    result.append('4');
                    break;
                case 's':
                    result.append('5');
                    break;
                case 'g', 'q', 'p':
                    result.append('9');
                    break;
                case 'B':
                    result.append("13");
                    break;
                case '?':
                    result.append('7');
                    break;
                default:
                    result.append("-1");
                    break;
            }
        }

        return Integer.parseInt(result.toString());
    }
}
