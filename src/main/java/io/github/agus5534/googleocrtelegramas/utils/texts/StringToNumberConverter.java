package io.github.agus5534.googleocrtelegramas.utils.texts;

public class StringToNumberConverter {

    public static int convert(String value) {
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
                case 'o':
                    result.append('0');
                    break;
                case 'l':
                    result.append('1');
                    break;
                case 'z':
                    result.append('2');
                    break;
                case 'e':
                    result.append('6');
                    break;
                case 'a':
                    result.append('4');
                    break;
                case 's':
                    result.append('5');
                    break;
                case 'g':
                    result.append('9');
                    break;
                case 'b':
                    result.append('6');
                    break;
                case '|':
                case '/':
                    result.append('1');
                    break;
                case 'u':
                    result.append('4');
                    break;
                case 'h':
                    result.append('4');
                    break;
                case 'v':
                    result.append('4');
                    break;
                case 'k':
                    result.append('4');
                    break;
                case 'p':
                    result.append('9');
                    break;
                case 'i':
                    result.append('1');
                    break;
                case 'c':
                    result.append('0');
                    break;
                case 'n':
                    result.append('1');
                    break;
                case 'y':
                    result.append('4');
                    break;
                case 'q':
                    result.append('9');
                    break;
                case 'B':
                    result.append("13");
                    break;
                default:
                    result.append("-1");
                    break;
            }
        }

        return Integer.parseInt(result.toString());
    }
}
