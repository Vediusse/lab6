package viancis.lab6.common.models;



import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;



public class MusicBand implements Comparable<MusicBand>, Serializable {

    private static final int MAX_IMPACT_SPEED_VALUE = 712;
    private static final int MAX_NAME_SIZE = 50;


    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private static long lastId = 0;

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;

    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$")
    @NotNull
    @NotEmpty
    @Size(min = 2, max = MAX_NAME_SIZE)
    private String name;

    @Valid
    @NotNull
    private Coordinates coordinates;
    @NotNull
    private LocalDateTime creationDate;
    @NotNull
    private Integer numberOfParticipants;
    @NotNull
    private long singlesCount;


    private ZonedDateTime establishmentDate;


    private MusicGenre genre;

    @Valid
    private Person frontMan;

    private User author;


    public MusicBand() {
        this.creationDate = LocalDateTime.now();
        this.coordinates = new Coordinates();
    }

    public MusicBand(int id,String name, Double x, Long y, Integer numberOfParticipants, long singlesCount, MusicGenre genre, ZonedDateTime establishmentDate, Person frontMan) {
        this(id,name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate);
        this.frontMan = frontMan;
    }


    public MusicBand(int id,String name, Double x, Long y, Integer numberOfParticipants, long singlesCount, MusicGenre genre, ZonedDateTime establishmentDate) {
        this();
        this.id = id;
        this.creationDate = LocalDateTime.now();
        this.coordinates = new Coordinates(x, y);
        this.setName(name);
        this.setNumberOfParticipants(numberOfParticipants);
        this.setSinglesCount(singlesCount);
        this.setGenre(genre);
        this.setEstablishmentDate(establishmentDate);
    }

    public MusicBand(int id, String name, Double x,Long  y, LocalDateTime creationDate, Integer numberOfParticipants, long singlesCount, ZonedDateTime establishmentDate, MusicGenre genre, Person frontMan, User author) {
        this(id, name,  x,  y,  numberOfParticipants,  singlesCount,  genre,  establishmentDate,  frontMan);
        this.frontMan = frontMan;
        this.author = author;
    }


    private long generateUniqueID() {
        return ++lastId;
    }


    @Override
    public int compareTo(MusicBand other) {
        return Long.compare(this.getId(), other.getId());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID should be greater than 0");
        }
        this.id = id;
        lastId = Math.max(id, lastId);
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
                        \t\tfrontMan={%s},
                        \t\tauthor={%s}
                        \t}""",
                getId(), getName(), formatCoordinates(), formatDate(getCreationDate()), getNumberOfParticipants(), getSinglesCount(),
                formatDate(getEstablishmentDate()), getGenre(), formatFrontMan(), getAuthor());
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


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}

