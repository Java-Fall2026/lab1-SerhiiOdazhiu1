package ua.hotel.util;
import java.util.Locale;

class FormatHelper {

    private FormatHelper() {}

    static String trim(String value) {
        return value == null ? null : value.trim();
    }

    static String normalizeUpper(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }

    static String capitalize(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        if (trimmed.isEmpty()) return trimmed;
        return trimmed.substring(0, 1).toUpperCase(Locale.ROOT)
                + trimmed.substring(1).toLowerCase(Locale.ROOT);
    }

    static String formatMoney(double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }
}