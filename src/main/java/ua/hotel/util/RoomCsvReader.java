package ua.hotel.util;

import ua.hotel.model.Room;
import ua.hotel.model.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RoomCsvReader {

    private static final Logger log = LoggerFactory.getLogger(RoomCsvReader.class);

    private RoomCsvReader() {}

    public static List<Room> read(String path) {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(path, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue;
                }
                try {
                    rooms.add(parseLine(line, lineNumber));
                } catch (InvalidDataException e) {
                    log.warn("Skipped invalid row: {}", e.getMessage());
                }
            }
        } catch (FileNotFoundException | SecurityException e) {
            log.error("Cannot open file {}", path, e);
        } catch (IOException e) {
            log.error("Read error in {}", path, e);
        } finally {
            log.info("Reading finished, rooms loaded: {}", rooms.size());
        }
        return rooms;
    }


    static Room parseLine(String line, int lineNumber) throws InvalidDataException {
        String[] parts = line.split(",", -1);
        if (parts.length != 3) {
            throw new InvalidDataException("Line " + lineNumber + ": expected 3 columns, got " + parts.length);
        }
        try {
            int roomNumber = Integer.parseInt(parts[0].trim());
            RoomType type = RoomType.valueOf(parts[1].trim());
            double price = Double.parseDouble(parts[2].trim());
            return new Room(roomNumber, type, price);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Line " + lineNumber + ": " + e.getMessage(), e);
        }
    }
}