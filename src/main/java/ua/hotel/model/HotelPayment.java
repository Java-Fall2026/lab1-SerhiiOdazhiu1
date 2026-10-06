package ua.hotel.model;
import ua.common.BaseEntity;
import ua.hotel.util.HotelUtils;
import java.time.LocalDate;
import java.util.Objects;


public class HotelPayment extends BaseEntity{
    private final String paymentId;
    private final Booking booking;
    private final double amount;
    private final LocalDate paymentDate;

    private HotelPayment(String paymentId, Booking booking, double amount, LocalDate paymentDate) {
        super();
        this.paymentId = HotelUtils.requireNotBlank(paymentId, "Payment ID");
        this.booking = HotelUtils.requireNonNull(booking, "Booking");
        this.amount = HotelUtils.requirePositive(amount, "Amount");
        HotelUtils.validatePaymentDate(paymentDate, this.booking.getCheckInDate());
        this.paymentDate = paymentDate;
    }

    public static HotelPayment of(String paymentId, Booking booking, double amount, LocalDate paymentDate) {
        return new HotelPayment(paymentId, booking, amount, paymentDate);
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelPayment that = (HotelPayment) o;
        return Objects.equals(paymentId, that.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }

    @Override
    public String toString() {
        return "HotelPayment{" + "paymentId='" + paymentId + '\'' + ", booking=" + booking + ", amount=" + amount + ", paymentDate=" + paymentDate + ", createdAt=" + createdAt + '}';
    }
}