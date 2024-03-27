package viancis.lab6.common.models;

import javafx.beans.property.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@XmlRootElement(name = "MusicBand")
public class MusicBand implements Comparable<MusicBand> , Serializable {
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    private static int lastId = 0;

    private transient final StringProperty name = new SimpleStringProperty();
    private transient final IntegerProperty id = new SimpleIntegerProperty();
    private transient ObjectProperty<Coordinates> coordinates = new SimpleObjectProperty<>();
    private transient ObjectProperty<LocalDateTime> creationDate = new SimpleObjectProperty<>();;
    private transient IntegerProperty numberOfParticipants = new SimpleIntegerProperty();
    private transient LongProperty singlesCount = new SimpleLongProperty();
    private transient ObjectProperty<ZonedDateTime> establishmentDate = new SimpleObjectProperty<>();
    private transient ObjectProperty<MusicGenre> genre = new SimpleObjectProperty<>();
    private transient ObjectProperty<Person> frontMan = new SimpleObjectProperty<>();

    public MusicBand() {
        this.id.set(generateUniqueID());
        this.creationDate.set(LocalDateTime.now());
        this.coordinates.set(new Coordinates());
    }

    public MusicBand(String name, Long x, Double y, Integer numberOfParticipants, long singlesCount, MusicGenre genre, ZonedDateTime establishmentDate) {
        this();
        this.setName(name);
        this.setNumberOfParticipants(numberOfParticipants);
        this.setSinglesCount(singlesCount);
        this.setGenre(genre);
        this.setEstablishmentDate(establishmentDate);
    }

    public MusicBand(String name, Long x, Double y, Integer numberOfParticipants, long singlesCount, MusicGenre genre, ZonedDateTime establishmentDate, Person frontMan) {
        this(name, x, y, numberOfParticipants, singlesCount, genre, establishmentDate);
        this.setFrontMan(frontMan);
    }

    private int generateUniqueID() {
        return ++lastId;
    }

    @Override
    public int compareTo(MusicBand other) {
        return Integer.compare(this.getId(), other.getId());
    }

    public String getName() {
        return name.get();
    }

    @XmlElement(name = "creationDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate.set(creationDate);
    }


    @XmlElement(name = "name")
    public void setName(String name) {
        this.name.set(name);
    }

    public int getId() {
        return id.get();
    }

    @XmlElement(name = "id")
    public void setId(int id) {
        this.id.set(id);
    }

    public Coordinates getCoordinates() {
        return coordinates.get();
    }

    @XmlElement(name = "coordinates")
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates.set(coordinates);
    }

    public LocalDateTime getCreationDate() {
        return creationDate.get();
    }


    public int getNumberOfParticipants() {
        return numberOfParticipants.get();
    }

    @XmlElement(name = "numberOfParticipants")
    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants.set(numberOfParticipants);
    }

    public long getSinglesCount() {
        return singlesCount.get();
    }

    @XmlElement(name = "singlesCount")
    public void setSinglesCount(long singlesCount) {
        this.singlesCount.set(singlesCount);
    }

    public ZonedDateTime getEstablishmentDate() {
        return establishmentDate.get();
    }

    @XmlElement(name = "establishmentDate")
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    public void setEstablishmentDate(ZonedDateTime establishmentDate) {
        this.establishmentDate.set(establishmentDate);
    }


    public MusicGenre getGenre() {
        return genre.get();
    }

    @XmlElement(name = "genre")
    public void setGenre(MusicGenre genre) {
        this.genre.set(genre);
    }

    public Person getFrontMan() {
        return frontMan.get();
    }

    @XmlElement(name = "frontMan")
    public void setFrontMan(Person frontMan) {
        this.frontMan.set(frontMan);
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

