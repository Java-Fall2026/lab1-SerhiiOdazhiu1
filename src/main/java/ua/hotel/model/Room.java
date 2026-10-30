package ua.hotel.model;
import ua.common.BaseEntity;
import ua.hotel.util.HotelUtils;
import java.util.Objects;

public class Room extends BaseEntity{
    private final int roomNumber;
    private final RoomType roomType;
    private double pricePerNight;

    public Room(int roomNumber, RoomType roomType, double pricePerNight){
        super();
        this.roomNumber = HotelUtils.requirePositive(roomNumber, "Room number");
        this.roomType = HotelUtils.requireNonNull(roomType, "Room type");
        setPricePerNight(pricePerNight);
    }

    public int getRoomNumber(){
        return roomNumber;
    }

    public RoomType getRoomType(){
        return roomType;
    }

    public double getPricePerNight(){
        return pricePerNight;
    }

    public final void setPricePerNight(double pricePerNight){
        this.pricePerNight = HotelUtils.requirePositive(pricePerNight, "Price per night");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomNumber == room.roomNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString() {
        return "Room{" + "roomNumber=" + roomNumber + ", roomType='" + roomType + '\'' + ", pricePerNight=" + pricePerNight + ", createdAt=" + createdAt + '}';
    }
}