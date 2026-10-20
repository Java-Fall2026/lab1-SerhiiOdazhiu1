package ua.hotel.model;
import ua.common.BaseEntity;
import java.time.LocalDate;
import ua.hotel.util.HotelUtils;
import java.util.Objects;


public record Guest(String passportNumber, String firstName, String lastName, LocalDate birthDate){

    public Guest {
        passportNumber = HotelUtils.requireNotBlank(passportNumber, "Passport number");
        firstName = HotelUtils.capitalize(HotelUtils.requireNotBlank(firstName, "First name"));
        lastName = HotelUtils.capitalize(HotelUtils.requireNotBlank(lastName, "Last name"));
        birthDate = HotelUtils.validateBirthDate(birthDate);
    }

}