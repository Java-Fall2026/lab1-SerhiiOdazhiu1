package ua.hotel.util;

import ua.hotel.model.Booking;
import ua.hotel.model.BookingStatus;
import ua.hotel.model.RoomType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class HotelUtils {

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

    public static String capitalize(String value) {
        return FormatHelper.capitalize(value);
    }

    public static String formatMoney(double amount) {
        return FormatHelper.formatMoney(amount);
    }

    // Guest
    public static LocalDate validateBirthDate(LocalDate birthDate) {
        ValidationHelper.requireAtLeast18YearsOld(birthDate);
        return birthDate;
    }

    // Booking
    public static void validateBookingDates(LocalDate checkInDate, LocalDate checkOutDate) {
        ValidationHelper.requireNotNull(checkInDate, "Check-in date");
        ValidationHelper.requireNotNull(checkOutDate, "Check-out date");
        ValidationHelper.requireAfter(checkOutDate, checkInDate, "Check-out date", "check-in date");
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

    public static String roomTypeLabel(RoomType type) {
        return switch (type) {
            case SINGLE -> "Odnomisnyi";
            case DOUBLE -> "Dvomisnyi";
            case SUITE -> "Lux";
            case DELUXE -> "Delux";
        };
    }

    public static String availableAction(BookingStatus status) {
        return switch (status) {
            case CONFIRMED -> "Zaselyty abo skasuvaty";
            case CHECKED_IN -> "Vyselyty";
            case CHECKED_OUT, CANCELLED -> "Dii nemaie";
        };
    }

}