package ua.hotel.model;
import ua.common.BaseEntity;
import java.time.LocalDate;
import ua.hotel.util.HotelUtils;
import java.util.Objects;


public class Guest extends BaseEntity{
    private final String passportNumber;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDate;

    private Guest(String passportNumber, String firstName, String lastName, LocalDate birthDate){
        super();
        this.passportNumber = HotelUtils.requireNotBlank(passportNumber, "Passport number");
        this.firstName = HotelUtils.requireNotBlank(firstName, "First name");
        this.lastName = HotelUtils.requireNotBlank(lastName, "Last name");
        this.birthDate = HotelUtils.validateBirthDate(birthDate);
    }

    public static Guest of(String passportNumber, String firstName, String lastName, LocalDate birthDate) {
        return new Guest(passportNumber, firstName, lastName, birthDate);
    }

    public String getPassportNumber(){
        return passportNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(passportNumber, guest.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }

    @Override
    public String toString() {
        return "Guest{" + "passportNumber='" + passportNumber + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", birthDate=" + birthDate + ", createdAt=" + createdAt + '}';
    }
}