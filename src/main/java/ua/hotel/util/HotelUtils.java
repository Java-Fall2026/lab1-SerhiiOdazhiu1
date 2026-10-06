package ua.hotel.util;

import ua.hotel.model.Booking;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class HotelUtils {

    public static final List<String> ALLOWED_ROOM_TYPES = List.of("SINGLE", "DOUBLE", "SUITE", "DELUXE");
    public static final List<String> ALLOWED_BOOKING_STATUSES = List.of("CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED");

    private HotelUtils() {}

    // Загальні валідатори
    public static String requireNotBlank(String value, String fieldName) {
        ValidationHelper.requireNotBlank(value, fieldName);
        return FormatHelper.trim(value);
    }

    public static <T> T requireNonNull(T object, String fieldName) {
        ValidationHelper.requireNotNull(object, fieldName);
        return object;
    }

    public static int requirePositive(int value, String fieldName) {
        ValidationHelper.requirePositive(value, fieldName);
        return value;
    }

    public static double requirePositive(double value, String fieldName) {
        ValidationHelper.requirePositive(value, fieldName);
        return value;
    }

    // Guest
    public static LocalDate validateBirthDate(LocalDate birthDate) {
        ValidationHelper.requireAtLeast18YearsOld(birthDate);
        return birthDate;
    }

    // Room
    public static String normalizeRoomType(String roomType) {
        ValidationHelper.requireNotBlank(roomType, "Room type");
        String normalized = FormatHelper.normalizeUpper(roomType);
        ValidationHelper.requireInAllowed(normalized, ALLOWED_ROOM_TYPES, "Room type");
        return normalized;
    }

    // Booking
    public static void validateBookingDates(LocalDate checkInDate, LocalDate checkOutDate) {
        ValidationHelper.requireNotNull(checkInDate, "Check-in date");
        ValidationHelper.requireNotNull(checkOutDate, "Check-out date");
        ValidationHelper.requireAfter(checkOutDate, checkInDate, "Check-out date", "check-in date");
    }

    public static String normalizeBookingStatus(String status) {
        ValidationHelper.requireNotBlank(status, "Booking status");
        String normalized = FormatHelper.normalizeUpper(status);
        ValidationHelper.requireInAllowed(normalized, ALLOWED_BOOKING_STATUSES, "Booking status");
        return normalized;
    }

    // HotelPayment
    public static void validatePaymentDate(LocalDate paymentDate, LocalDate checkInDate) {
        ValidationHelper.requireNotNull(paymentDate, "Payment date");
        ValidationHelper.requireNotNull(checkInDate, "Booking check-in date");
        ValidationHelper.requireNotBefore(paymentDate, checkInDate, "Payment date", "check-in date");
    }

    // Обчислювані методи
    public static long nights(Booking booking) {
        ValidationHelper.requireNotNull(booking, "Booking");
        return ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
    }

    public static double totalPrice(Booking booking) {
        ValidationHelper.requireNotNull(booking, "Booking");
        ValidationHelper.requireNotNull(booking.getRoom(), "Booking room");
        return nights(booking) * booking.getRoom().getPricePerNight();
    }
}