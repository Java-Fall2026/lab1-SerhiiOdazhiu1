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

import ua.hotel.util.GenericRepository;
import ua.hotel.util.DuplicateIdentityException;
import ua.hotel.model.Guest;
import ua.hotel.model.HotelPayment;
import ua.hotel.util.DuplicateIdentityException;
import ua.hotel.util.GenericRepository;

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

        // 11. Repository filled from CSV
        System.out.println("\n11. Repozytorii z CSV");
        GenericRepository<Room> roomRepo =
                new GenericRepository<>("rooms", r -> String.valueOf(r.getRoomNumber()));
        for (Room r : rooms) {
            roomRepo.addIfAbsent(r);
        }
        System.out.println("Rozmir repozytoriia: " + roomRepo.size());

        // Duplicate from CSV: add() must throw, addIfAbsent() must return false
        try {
            roomRepo.add(new Room(101, RoomType.SINGLE, 900.0));
        } catch (DuplicateIdentityException e) {
            System.out.println("Dublikat: " + e.getMessage());
        }
        System.out.println("addIfAbsent: " + roomRepo.addIfAbsent(new Room(101, RoomType.SINGLE, 900.0)));

        // 12. Three repositories of different types: search found / not found
        System.out.println("\n12. Tri repozytoriii riznykh typiv");
        GenericRepository<Guest> guestRepo =
                new GenericRepository<>("guests", Guest::passportNumber);
        guestRepo.add(new Guest("AB123456", "Oleh", "Petrenko", LocalDate.of(2001, 4, 12)));

        GenericRepository<Room> roomRepo2 =
                new GenericRepository<>("rooms-typed", r -> String.valueOf(r.getRoomNumber()));
        roomRepo2.add(new Room(201, RoomType.SUITE, 2000.0));

        GenericRepository<HotelPayment> paymentRepo =
                new GenericRepository<>("payments", HotelPayment::paymentId);
        paymentRepo.add(new HotelPayment("PAY-900", booking, 4800.0, LocalDate.of(2026, 9, 1)));

        System.out.println("Guest AB123456 found: " + guestRepo.findByIdentity("AB123456").isPresent());
        System.out.println("Guest ZZ000000 found: " + guestRepo.findByIdentity("ZZ000000").isPresent());
        System.out.println("Room 201 found: " + roomRepo2.findByIdentity("201").isPresent());
        System.out.println("Payment PAY-900 found: " + paymentRepo.findByIdentity("PAY-900").isPresent());

// 13. Duplicate: add() catches the exception, addIfAbsent() returns false
        System.out.println("\n13. Dublikat: add i addIfAbsent");
        try {
            roomRepo2.add(new Room(201, RoomType.SUITE, 2000.0));
        } catch (DuplicateIdentityException e) {
            System.out.println("add: " + e.getMessage());
        }
        System.out.println("addIfAbsent: " + roomRepo2.addIfAbsent(new Room(201, RoomType.SUITE, 2000.0)));

// 14. Remove present and missing
        System.out.println("\n14. Vydalennia");
        System.out.println("Remove 201: " + roomRepo2.removeByIdentity("201"));
        System.out.println("Remove 201 again: " + roomRepo2.removeByIdentity("201"));
    }
}