package ua.hotel;

import ua.hotel.model.Booking;
import ua.hotel.model.Guest;
import ua.hotel.model.HotelPayment;
import ua.hotel.model.Room;
import ua.hotel.util.HotelUtils;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("1. Stvorennia obiekta cherez konstruktor i of()");
        Room room = new Room(101, "double", 1200.0);
        Guest guest1 = Guest.of("  AB123456  ", "  Oleh", "Petrenko  ", LocalDate.of(2001, 4, 12));
        System.out.println("Room: " + room.getRoomNumber());
        System.out.println("Guest: " + guest1.getFirstName() + " " + guest1.getLastName());

        System.out.println("\n2. Normalizatsiia v dii");
        System.out.println("Room type: [" + room.getRoomType() + "]");
        System.out.println("Passport: [" + guest1.getPassportNumber() + "]");
        System.out.println("First name: [" + guest1.getFirstName() + "]");

        System.out.println("\n3. Perekhoplennia pomylok (try/catch)");
        try {
            Guest.of("CD111111", "Ivan", "Malyi", LocalDate.now().minusYears(16));
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka 1: " + e.getMessage());
        }

        try {
            new Room(102, "PENTHOUSE", 3000.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka 2: " + e.getMessage());
        }

        try {
            new Booking(room, guest1, LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 5), "CONFIRMED");
        } catch (IllegalArgumentException e) {
            System.out.println("Pomylka 3: " + e.getMessage());
        }

        System.out.println("\n4. Sproba zipsuvaty obiekt cherez setter");
        System.out.println("Tsina do: " + room.getPricePerNight());
        try {
            room.setPricePerNight(-500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Setter vidkhylyv: " + e.getMessage());
        }
        System.out.println("Tsina pislia: " + room.getPricePerNight());

        System.out.println("\n5. Porivniannia (==, equals, hashCode)");
        Guest guest2 = Guest.of("AB123456", "Serhii", "Ivanov", LocalDate.of(1999, 1, 1));
        Guest guest3 = Guest.of("XX999999", "Oleh", "Petrenko", LocalDate.of(2001, 4, 12));

        System.out.println("guest1 == guest2: " + (guest1 == guest2));
        System.out.println("guest1.equals(guest2): " + guest1.equals(guest2));
        System.out.println("guest1.hashCode() == guest2.hashCode(): " + (guest1.hashCode() == guest2.hashCode()));
        System.out.println("guest1.equals(guest3): " + guest1.equals(guest3));

        System.out.println("\n6. Obchysliuvani metody");
        Booking booking = new Booking(room, guest1, LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 5), "confirmed");
        System.out.println("Nights: " + HotelUtils.nights(booking));
        System.out.println("Total price: " + HotelUtils.totalPrice(booking));

        System.out.println("\n7. Vyvid cherez toString()");
        HotelPayment payment = HotelPayment.of("PAY-777", booking, HotelUtils.totalPrice(booking), LocalDate.of(2026, 9, 1));
        System.out.println(guest1);
        System.out.println(room);
        System.out.println(booking);
        System.out.println(payment);

        System.out.println("\n8. Kody, shcho ne kompiluiutsia (inkapsuliatsiia)");
        // Guest guestError = new Guest("AB123456", "Ivan", "Test", LocalDate.now()); error: private constructor
        // ua.hotel.util.ValidationHelper.requirePositive(5, "num");               error: package-private access
    }
}