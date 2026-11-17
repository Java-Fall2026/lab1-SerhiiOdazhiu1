package ua.hotel.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.hotel.model.Guest;
import ua.hotel.model.Room;
import ua.hotel.model.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericRepositoryTest {

    private GenericRepository<Room> newRoomRepo() {
        return new GenericRepository<>("rooms", r -> String.valueOf(r.getRoomNumber()));
    }

    private GenericRepository<Guest> newGuestRepo() {
        return new GenericRepository<>("guests", Guest::passportNumber);
    }

    @Test
    @DisplayName("add then findByIdentity returns the added item")
    void addAndFind() {
        GenericRepository<Room> repo = newRoomRepo();
        Room room = new Room(101, RoomType.SINGLE, 850.0);
        repo.add(room);
        Optional<Room> found = repo.findByIdentity("101");
        assertTrue(found.isPresent());
        assertEquals(room, found.get());
    }

    @Test
    @DisplayName("findByIdentity returns empty Optional for a missing identity")
    void findMissingReturnsEmpty() {
        GenericRepository<Room> repo = newRoomRepo();
        assertTrue(repo.findByIdentity("999").isEmpty());
    }

    @Test
    @DisplayName("add with an existing identity throws DuplicateIdentityException")
    void addDuplicateThrows() {
        GenericRepository<Room> repo = newRoomRepo();
        repo.add(new Room(101, RoomType.SINGLE, 850.0));
        assertThrows(DuplicateIdentityException.class,
                () -> repo.add(new Room(101, RoomType.DOUBLE, 900.0)));
    }

    @Test
    @DisplayName("addIfAbsent returns false and keeps the size for a duplicate")
    void addIfAbsentDuplicate() {
        GenericRepository<Room> repo = newRoomRepo();
        repo.add(new Room(101, RoomType.SINGLE, 850.0));
        assertFalse(repo.addIfAbsent(new Room(101, RoomType.DOUBLE, 900.0)));
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("removeByIdentity works for present and missing identities")
    void removePresentAndMissing() {
        GenericRepository<Room> repo = newRoomRepo();
        repo.add(new Room(101, RoomType.SINGLE, 850.0));
        assertTrue(repo.removeByIdentity("101"));
        assertFalse(repo.removeByIdentity("101"));
        assertEquals(0, repo.size());
    }

    @Test
    @DisplayName("changing the list returned by findAll does not change the repository")
    void findAllReturnsCopy() {
        GenericRepository<Room> repo = newRoomRepo();
        repo.add(new Room(101, RoomType.SINGLE, 850.0));
        List<Room> all = repo.findAll();
        all.clear();
        assertEquals(1, repo.size());
    }

    @Test
    @DisplayName("the same repository class works with two different types")
    void worksWithTwoTypes() {
        GenericRepository<Room> rooms = newRoomRepo();
        GenericRepository<Guest> guests = newGuestRepo();
        rooms.add(new Room(101, RoomType.SINGLE, 850.0));
        guests.add(new Guest("AB123456", "Oleh", "Petrenko", LocalDate.of(2000, 1, 1)));
        assertTrue(rooms.findByIdentity("101").isPresent());
        assertTrue(guests.findByIdentity("AB123456").isPresent());
    }
}