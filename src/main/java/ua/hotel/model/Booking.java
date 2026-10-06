package ua.hotel.model;
import ua.common.BaseEntity;
import ua.hotel.util.HotelUtils;
import java.time.LocalDate;
import java.util.Objects;

public class Booking extends BaseEntity{
    private final Room room;
    private final Guest guest;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private String status;

    public Booking(Room room, Guest guest, LocalDate checkInDate, LocalDate checkOutDate, String status) {
        super();
        this.room = HotelUtils.requireNonNull(room, "Room");
        this.guest = HotelUtils.requireNonNull(guest, "Guest");
        HotelUtils.validateBookingDates(checkInDate, checkOutDate);
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        setStatus(status);
    }

    public Room getRoom() {
        return room;
    }

    public Guest getGuest() {
        return guest;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    public final void setStatus(String status) {
        this.status = HotelUtils.normalizeBookingStatus(status);
    }

    @Override     // Composite key a room cannot have more than one booking starting on the same check-in date
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(room, booking.room) &&
                Objects.equals(checkInDate, booking.checkInDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(room, checkInDate);
    }

    @Override
    public String toString() {
        return "Booking{" + "room=" + room + ", guest=" + guest + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate + ", status='" + status + '\'' + ", createdAt=" + createdAt + '}';
    }
}