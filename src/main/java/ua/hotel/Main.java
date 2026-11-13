package ua.hotel;

import ua.hotel.model.Booking;
import ua.hotel.model.BookingStatus;
import ua.hotel.model.Guest;
import ua.hotel.model.HotelPayment;
import ua.hotel.model.Room;
import ua.hotel.model.RoomType;
import ua.hotel.util.HotelUtils;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.hotel.util.RoomCsvReader;
import java.util.List;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        // 1
        System.out.println("1. Record: stvorennia i dostup do komponentiv");
        Guest guest1 = new Guest("  AB123456  ", "  oLEH", "pETRENKO  ", LocalDate.of(2001, 4, 12));
        System.out.println("Passport: [" + guest1.passportNumber() + "]");
        System.out.println("First name: [" + guest1.firstName() + "]");
        System.out.println("Last name: [" + guest1.lastName() + "]");

        Room room = new Room(101, RoomType.DOUBLE, 1200.0);
        System.out.println("Room: " + room.getRoomNumber() + ", type: " + room.getRoomType());

        // 2
        System.out.println("\n2. Enum: values(), vlasne pole, valueOf");
        for (RoomType type : RoomType.values()) {
            System.out.println(type + " priceFactor=" + type.getPriceFactor());
        }
        try {
            RoomType.valueOf("PENTHOUSE");
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka valueOf: " + e.getMessage());
        }

        // 3
        System.out.println("\n3. Switch-vyrazy i obchysliuvani metody");
        Booking booking = new Booking(room, guest1, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 5), BookingStatus.CONFIRMED);
        System.out.println("Room label: " + HotelUtils.roomTypeLabel(room.getRoomType()));
        System.out.println("Action: " + HotelUtils.availableAction(booking.getStatus()));
        System.out.println("Nights: " + HotelUtils.nights(booking));
        System.out.println("Total price: " + HotelUtils.formatMoney(HotelUtils.totalPrice(booking)));

        // 4
        System.out.println("\n4. Nevalidni dani");
        try {
            new Guest("", "Ivan", "Test", LocalDate.of(2000, 1, 1));
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka 1: " + e.getMessage());
        }
        try {
            new Guest("CD111111", "Ivan", "Malyi", LocalDate.now().minusYears(16));
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka 2: " + e.getMessage());
        }
        try {
            new Booking(room, guest1, LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 5), BookingStatus.CONFIRMED);
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka 3: " + e.getMessage());
        }

        // 5
        System.out.println("\n5. Porivniannia record (equals, hashCode)");
        Guest twin1 = new Guest("XX999999", "Oleh", "Petrenko", LocalDate.of(2001, 4, 12));
        Guest twin2 = new Guest("XX999999", "Oleh", "Petrenko", LocalDate.of(2001, 4, 12));
        System.out.println("twin1 == twin2: " + (twin1 == twin2));
        System.out.println("twin1.equals(twin2): " + twin1.equals(twin2));
        System.out.println("twin1.hashCode() == twin2.hashCode(): " + (twin1.hashCode() == twin2.hashCode()));

        // 6
        System.out.println("\n6. Vyvid record cherez toString()");
        HotelPayment payment = new HotelPayment("PAY-777", booking, HotelUtils.totalPrice(booking), LocalDate.of(2026, 9, 1));
        System.out.println(guest1);
        System.out.println(room);
        System.out.println(booking);
        System.out.println(payment);

        // 7. CSV reading: valid and invalid rows
        System.out.println("\n7. Chytannia CSV-faila");
        List<Room> rooms = RoomCsvReader.read("data/rooms.csv");
        System.out.println("Zavantazheno kimnat: " + rooms.size());

        // 8. Missing file does not stop the program
        System.out.println("\n8. Vidsutnii fail");
        List<Room> none = RoomCsvReader.read("data/none.csv");
        System.out.println("Zavantazheno kimnat: " + none.size());

        // 9. Invalid value in a setter of a mutable class
        System.out.println("\n9. Nevalidne znachennia v setteri");
        System.out.println("Tsina do: " + room.getPricePerNight());
        try {
            room.setPricePerNight(-500.0);
        } catch (IllegalArgumentException e) {
            log.error("Setter rejected price: {}", e.getMessage());
        }
        System.out.println("Tsina pislia: " + room.getPricePerNight());

        // 10. Logging levels
        System.out.println("\n10. Rivni lohuvannia");
        log.info("Program finished, rooms from file: {}", rooms.size());
    }
}