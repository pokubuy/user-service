package org.pokubuy.userservice.domain.utils;

public final class MaskDataUtil {

    public static String maskData(String data) {
        return data.charAt(0) + "***" + data.charAt(data.length() - 1);
    }
}
