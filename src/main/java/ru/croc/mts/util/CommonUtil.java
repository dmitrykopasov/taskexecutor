package ru.croc.mts.util;

import java.util.UUID;

public class CommonUtil {

    public static String createUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
