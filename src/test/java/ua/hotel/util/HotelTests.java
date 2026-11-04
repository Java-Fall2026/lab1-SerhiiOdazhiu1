package ua.hotel.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.hotel.model.Booking;
import ua.hotel.model.BookingStatus;
import ua.hotel.model.Guest;
import ua.hotel.model.Room;
import ua.hotel.model.RoomType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HotelTests {

    private Booking sampleBooking() {
        Room room = new Room(101, RoomType.DOUBLE, 1200.0);
        Guest guest = new Guest("AB123456", "Oleh", "Petrenko", LocalDate.of(2000, 1, 1));
        return new Booking(room, guest, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 5), BookingStatus.CONFIRMED);
    }

    // --- valid object creation ---

    @Test
    @DisplayName("Room is created with valid data")
    void roomCreatedWithValidData() {
        Room room = new Room(101, RoomType.SINGLE, 850.0);
        assertEquals(101, room.getRoomNumber());
        assertEquals(RoomType.SINGLE, room.getRoomType());
        assertEquals(850.0, room.getPricePerNight());
    }

    @Test
    @DisplayName("Guest record trims passport and capitalizes names")
    void guestNormalizesFields() {
        Guest guest = new Guest("  AB123456  ", "  oLEH", "pETRENKO  ", LocalDate.of(2000, 1, 1));
        assertEquals("AB123456", guest.passportNumber());
        assertEquals("Oleh", guest.firstName());
        assertEquals("Petrenko", guest.lastName());
    }

    @Test
    @DisplayName("parseLine builds a Room from a valid CSV row")
    void parseLineValidRow() throws InvalidDataException {
        Room room = RoomCsvReader.parseLine("102,DELUXE,2400.0", 3);
        assertEquals(102, room.getRoomNumber());
        assertEquals(RoomType.DELUXE, room.getRoomType());
        assertEquals(2400.0, room.getPricePerNight());
    }

    // --- InvalidDataException on bad rows ---

    @Test
    @DisplayName("parseLine rejects a non-numeric room number and reports the line")
    void parseLineBadNumber() {
        InvalidDataException e = assertThrows(InvalidDataException.class,
                () -> RoomCsvReader.parseLine("abc,SINGLE,900.0", 5));
        assertTrue(e.getMessage().contains("Line 5"));
    }

    @Test
    @DisplayName("parseLine rejects an unknown room type")
    void parseLineUnknownType() {
        InvalidDataException e = assertThrows(InvalidDataException.class,
                () -> RoomCsvReader.parseLine("104,PENTHOUSE,3000.0", 6));
        assertTrue(e.getMessage().contains("Line 6"));
    }

    @Test
    @DisplayName("parseLine rejects a negative price")
    void parseLineNegativePrice() {
        assertThrows(InvalidDataException.class,
                () -> RoomCsvReader.parseLine("105,SUITE,-500.0", 7));
    }

    @Test
    @DisplayName("parseLine rejects a row with a missing column")
    void parseLineWrongColumnCount() {
        InvalidDataException e = assertThrows(InvalidDataException.class,
                () -> RoomCsvReader.parseLine("106,DOUBLE", 8));
        assertTrue(e.getMessage().contains("expected 3 columns"));
    }

    // --- calculated methods ---

    @Test
    @DisplayName("nights counts days between check-in and check-out")
    void nightsCalculation() {
        assertEquals(4, HotelUtils.nights(sampleBooking()));
    }

    @Test
    @DisplayName("totalPrice multiplies nights by price per night")
    void totalPriceCalculation() {
        assertEquals(4800.0, HotelUtils.totalPrice(sampleBooking()));
    }

    @Test
    @DisplayName("Switch expressions return the expected labels")
    void switchExpressions() {
        assertEquals("Dvomisnyi", HotelUtils.roomTypeLabel(RoomType.DOUBLE));
        assertEquals("Vyselyty", HotelUtils.availableAction(BookingStatus.CHECKED_IN));
    }
}