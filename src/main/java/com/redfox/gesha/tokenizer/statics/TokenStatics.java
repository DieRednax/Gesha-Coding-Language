package com.redfox.gesha.tokenizer.statics;

import java.util.List;

public class TokenStatics {
    public static List<String> getKeywords() {
        return List.of(
                "public",
                "private",
                "f",
                "void",
                "int",
                "long",
                "float",
                "double"
        );
    }

    public static List<Character> getHexCharacters() {
        return List.of(
                'a',
                'b',
                'c',
                'd',
                'e',
                'f'
        );
    }

    public static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int parseAnyStringToInteger(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }

        // Clean up whitespace and normalize to lowercase for easier prefix checking
        String cleanInput = input.trim().toLowerCase();
        if (cleanInput.isEmpty()) {
            throw new NumberFormatException("Input string is empty");
        }

        // Track the sign of the number
        boolean isNegative = false;
        if (cleanInput.startsWith("-")) {
            isNegative = true;
            cleanInput = cleanInput.substring(1);
        } else if (cleanInput.startsWith("+")) {
            cleanInput = cleanInput.substring(1);
        }

        int result;

        // Detect format and parse accordingly
        if (cleanInput.startsWith("0x")) {
            // Hexadecimal (Base 16)
            result = Integer.parseInt(cleanInput.substring(2), 16);
        } else if (cleanInput.startsWith("0b")) {
            // Binary (Base 2)
            result = Integer.parseInt(cleanInput.substring(2), 2);
        } else {
            // Decimal (Base 10)
            result = Integer.parseInt(cleanInput, 10);
        }

        // Apply the sign back
        return isNegative ? -result : result;
    }
}
