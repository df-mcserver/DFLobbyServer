package uk.co.nikodem.Utils;

public class StringHelper {
    public static String sanitiseString(String input) {
        return input.replaceAll("\\p{C}", "");
    }
}
