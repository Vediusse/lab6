package viancis.lab6.common.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Validator {

    public void validateMusicBand(MusicBand musicBand) {
        validateId(musicBand.getId(), "ID");
        validateName(musicBand.getName(), "Name");
        validateCoordinates(musicBand.getCoordinates());
        validateCreationDate(musicBand.getCreationDate(), "Creation date");
        validateNumberOfParticipants(musicBand.getNumberOfParticipants(), "Number of participants");
        validateSinglesCount(musicBand.getSinglesCount(), "Singles count");
        validateEstablishmentDate(musicBand.getEstablishmentDate(), "Establishment date");
        validateMusicGenre(musicBand.getGenre(), "MusicGenre");
        validateFrontMan(musicBand.getFrontMan());
    }

    public void validateCoordinates(Coordinates coordinates) {
        validateNotNull(coordinates.getX(), "X coordinate");
        validateNotNull(coordinates.getY(), "Y coordinate");
    }

    public void validatePerson(Person person) {
        String name = person.getName();
        Color eyeColor = Color.valueOf(person.getEyeColor()); // Используйте метод valueOf для получения значения перечисления из строки
        Color hairColor = Color.valueOf(person.getHairColor());
        Country nationality = Country.valueOf(person.getNationality());

        validateName(name, "Person name");
        validateColor(eyeColor, "Eye color");
        validateColor(hairColor, "Hair color");
        validateCountry(nationality, "Nationality");
    }

    public void validateMusicGenre(MusicGenre musicGenre, String propertyName) {
        validateNotNull(musicGenre, propertyName + " cannot be null");
    }

    public void validateYValue(Long y) {
        if (y <= -263) {
            throw new IllegalArgumentException("Y value should be greater than -263");
        }
    }

    public void validateIntegerNotNull(Integer value, String propertyName) {
        validateNotNull(value, propertyName);
    }

    public void validateColor(Color color, String propertyName) {
        validateNotNull(color, propertyName);
    }

    public void validateCountry(Country country, String propertyName) {
        validateNotNull(country, propertyName);
    }

    public void validateId(long id, String propertyName) {
        if (id <= 0) {
            throw new IllegalArgumentException(propertyName + " should be greater than 0");
        }
    }

    public void validateName(String name, String propertyName) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(propertyName + " cannot be null or empty");
        }
    }

    public void validateLongNotNull(Long value, String propertyName) {
        validateNotNull(value, propertyName);
    }

    public void validateDoubleNotNull(Double value, String propertyName) {
        validateNotNull(value, propertyName);
    }

    public void validateNumberOfParticipants(Integer numberOfParticipants, String propertyName) {
        if (numberOfParticipants != null && numberOfParticipants <= 0) {
            throw new IllegalArgumentException(propertyName + " should be greater than 0");
        }
    }

    public void validateSinglesCount(long singlesCount, String propertyName) {
        if (singlesCount <= 0) {
            throw new IllegalArgumentException(propertyName + " should be greater than 0");
        }
    }

    public void validateCreationDate(LocalDateTime creationDate, String propertyName) {
        validateNotNull(creationDate, propertyName + " cannot be null");
    }

    public void validateEstablishmentDate(ZonedDateTime establishmentDate, String propertyName) {
        validateNotNull(establishmentDate, propertyName + " cannot be null");
    }

    public void validateZoneId(ZoneId zoneId, String propertyName) {
        validateNotNull(zoneId, propertyName + " cannot be null");
    }

    public void validateFrontMan(Person frontMan) {
        if (frontMan != null) {
            validatePerson(frontMan);
        }
    }

    private void validateNotNull(Object value, String errorMessage) {
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
