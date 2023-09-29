package com.stepbook.infrastructure.util;

import java.util.regex.Pattern;

public class NumberUtil {

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
