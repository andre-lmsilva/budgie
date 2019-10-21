package io.geekmind.budgie.ui;

public class TextHelper {

    public static String formatOrdinal(Integer value) {
        String suffix = "th";
        if (value == 1) {
            suffix = "st";
        } else if (value == 2) {
            suffix = "nd";
        } else if (value == 3) {
            suffix = "rd";
        }
        return String.format("%s%s", value, suffix);
    }

}
