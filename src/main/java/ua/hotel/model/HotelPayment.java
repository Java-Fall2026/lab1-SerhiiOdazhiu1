package ua.hotel.model;
import ua.common.BaseEntity;
import ua.hotel.util.HotelUtils;
import java.time.LocalDate;
import java.util.Objects;


public record HotelPayment(String paymentId, Booking booking, double amount, LocalDate paymentDate) {
    public HotelPayment {
        paymentId = HotelUtils.requireNotBlank(paymentId, "Payment ID");
        booking = HotelUtils.requireNonNull(booking, "Booking");
        amount = HotelUtils.requirePositive(amount, "Amount");
        HotelUtils.validatePaymentDate(paymentDate, booking.getCheckInDate());
    }
}