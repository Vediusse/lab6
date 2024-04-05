package viancis.lab6.common.models;

import javafx.beans.property.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@XmlRootElement(name = "MusicBand")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "coordinates", "creationDate", "numberOfParticipants", "singlesCount", "establishmentDate", "genre", "frontMan"})
public class MusicBand implements Comparable<MusicBand>, Serializable {
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private static int lastId = 0;

    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement(required = true)
    private int id;

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private Coordinates coordinates;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationDate;

    @XmlElement(required = true)
    private Integer numberOfParticipants;

    @XmlElement(required = true)
    private long singlesCount;

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime establishmentDate;

    @XmlElement(required = true)
    private MusicGenre genre;

    @XmlElement
    private Person frontMan;


    public MusicBand() {
        this.id = generateUniqueID();
        this.creationDate = LocalDateTime.now();
        this.coordinates = new Coordinates();
    }

    public MusicBand(String name, Double x, Long y, Integer numberOfParticipants, long singlesCount, MusicGenre genre, ZonedDateTime establishmentDate, Person frontMan) {
        this(name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate);
        this.frontMan = frontMan;
    }


    public MusicBand(String name, Double x, Long y, Integer numberOfParticipants, long singlesCount, MusicGenre genre, ZonedDateTime establishmentDate) {
        this.id = generateUniqueID();
        this.creationDate = LocalDateTime.now();
        this.coordinates = new Coordinates(x, y);
        this.setName(name);
        this.setNumberOfParticipants(numberOfParticipants);
        this.setSinglesCount(singlesCount);
        this.setGenre(genre);
        this.setEstablishmentDate(establishmentDate);
    }


    private int generateUniqueID() {
        return ++lastId;
    }


    @Override
    public int compareTo(MusicBand other) {
        return Integer.compare(this.getId(), other.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID should be greater than 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new IllegalArgumentException("Creation date cannot be null");
        }
        this.creationDate = creationDate;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        if (numberOfParticipants != null && numberOfParticipants <= 0) {
            throw new IllegalArgumentException("Number of participants should be greater than 0");
        }
        this.numberOfParticipants = numberOfParticipants;
    }

    public long getSinglesCount() {
        return singlesCount;
    }

    public void setSinglesCount(long singlesCount) {
        if (singlesCount <= 0) {
            throw new IllegalArgumentException("Singles count should be greater than 0");
        }
        this.singlesCount = singlesCount;
    }

    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(ZonedDateTime establishmentDate) {
        if (establishmentDate == null) {
            throw new IllegalArgumentException("Establishment date cannot be null");
        }
        this.establishmentDate = establishmentDate;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public void setGenre(MusicGenre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        this.genre = genre;
    }

    public Person getFrontMan() {
        return frontMan;
    }

    public void setFrontMan(Person frontMan) {
        this.frontMan = frontMan;
    }

    @Override
    public String toString() {
        return String.format("""
                        \tMusicBand{
                        \t\tid=%d,
                        \t\tname='%s',
                        \t\tcoordinates={%s\t\t},
                        \t\tcreationDate=%s,
                        \t\tnumberOfParticipants=%d,
                        \t\tsinglesCount=%d,
                        \t\testablishmentDate=%s,
                        \t\tgenre=%s,
                        \t\tfrontMan={%s}
                        \t}""",
                getId(), getName(), formatCoordinates(), formatDate(getCreationDate()), getNumberOfParticipants(), getSinglesCount(),
                formatDate(getEstablishmentDate()), getGenre(), formatFrontMan());
    }

    private String formatCoordinates() {
        return getCoordinates() != null ? "\t" + getCoordinates().toString() : "\tnull,";
    }

    private String formatDate(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } else {
            return "\tnull,";
        }
    }

    private String formatDate(ZonedDateTime zonedDateTime) {
        if (zonedDateTime != null) {
            return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
        } else {
            return "\tnull,";
        }
    }

    private String formatFrontMan() {
        return getFrontMan() != null ? "\t" + getFrontMan() : "null";
    }



}

