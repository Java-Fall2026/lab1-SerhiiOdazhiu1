package ua.hotel.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;

class ValidationHelper {

    private ValidationHelper() {}

    static void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }
    }

    static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank, got: " + value);
        }
    }

    static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0, got: " + value);
        }
    }

    static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0, got: " + value);
        }
    }

    static void requireAtLeast18YearsOld(LocalDate birthDate) {
        requireNotNull(birthDate, "Birth date");
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException("Guest must be at least 18 years old, got birth date: " + birthDate + " (age: " + age + ")");
        }
    }

    static void requireAfter(LocalDate date, LocalDate baseDate, String fieldName, String baseFieldName) {
        requireNotNull(date, fieldName);
        requireNotNull(baseDate, baseFieldName);
        if (!date.isAfter(baseDate)) {
            throw new IllegalArgumentException(fieldName + " must be strictly after " + baseFieldName + " (" + baseDate + "), got: " + date);
        }
    }

    static void requireNotBefore(LocalDate date, LocalDate baseDate, String fieldName, String baseFieldName) {
        requireNotNull(date, fieldName);
        requireNotNull(baseDate, baseFieldName);
        if (date.isBefore(baseDate)) {
            throw new IllegalArgumentException(fieldName + " must not be before " + baseFieldName + " (" + baseDate + "), got: " + date);
        }
    }

    static void requireInAllowed(String value, Collection<String> allowedValues, String fieldName) {
        requireNotNull(value, fieldName);
        if (!allowedValues.contains(value)) {
            throw new IllegalArgumentException(fieldName + " must be one of " + allowedValues + ", got: " + value);
        }
    }
}