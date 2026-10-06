package ua.hotel.util;

class FormatHelper {

    private FormatHelper() {}

    static String trim(String value) {
        return value == null ? null : value.trim();
    }

    static String normalizeUpper(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}