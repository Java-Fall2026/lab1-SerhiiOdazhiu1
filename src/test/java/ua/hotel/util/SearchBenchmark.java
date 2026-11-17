package ua.hotel.util;

import ua.hotel.model.Room;
import ua.hotel.model.RoomType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchBenchmark {

    private static final int COUNT = 100_000;
    private static final int ROUNDS = 1_000;
    private static final int REPEATS = 5;
    private static final int WARMUP = 3;

    private static final IdentityExtractor<Room> ROOM_ID =
            r -> String.valueOf(r.getRoomNumber());

    public static void main(String[] args) {
        GenericRepository<Room> repo = new GenericRepository<>("bench", ROOM_ID);
        for (int i = 1; i <= COUNT; i++) {
            repo.add(new Room(i, RoomType.SINGLE, 100.0));
        }

        // Taken once, before any measurement
        List<Room> list = repo.findAll();

        runScenario("first element", repo, list, "1");
        runScenario("last element", repo, list, String.valueOf(COUNT));
        runScenario("missing element", repo, list, "999999");
    }

    private static void runScenario(String name, GenericRepository<Room> repo,
                                    List<Room> list, String target) {
        // Warm-up: JIT compiles both paths before timing
        for (int i = 0; i < WARMUP; i++) {
            measureMap(repo, target);
            measureList(list, target);
        }

        long[] mapTimes = new long[REPEATS];
        long[] listTimes = new long[REPEATS];
        for (int i = 0; i < REPEATS; i++) {
            mapTimes[i] = measureMap(repo, target);
            listTimes[i] = measureList(list, target);
        }

        System.out.println(name + ": Map median " + median(mapTimes) / 1_000
                + " us, List median " + median(listTimes) / 1_000 + " us");
    }

    private static long measureMap(GenericRepository<Room> repo, String target) {
        long start = System.nanoTime();
        for (int i = 0; i < ROUNDS; i++) {
            repo.findByIdentity(target);
        }
        return System.nanoTime() - start;
    }

    private static long measureList(List<Room> list, String target) {
        long start = System.nanoTime();
        for (int i = 0; i < ROUNDS; i++) {
            linearSearch(list, target, ROOM_ID);
        }
        return System.nanoTime() - start;
    }

    private static <E> Optional<E> linearSearch(
            List<E> items, String identity, IdentityExtractor<E> extractor) {
        for (E item : items) {
            if (identity.equals(extractor.identityOf(item))) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private static long median(long[] values) {
        long[] copy = values.clone();
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }
}