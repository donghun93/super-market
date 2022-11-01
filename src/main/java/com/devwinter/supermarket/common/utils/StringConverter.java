package com.devwinter.supermarket.common.utils;

import javax.persistence.Convert;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class StringConverter {
    public static String localDateTimeToString(LocalDateTime localDateTime) {
        if(localDateTime == null) {
            return "";
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
