package viancis.lab6.server.collection;

import viancis.lab6.common.models.MusicBand;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

import javax.xml.bind.annotation.*;
import java.util.PriorityQueue;

@XmlRootElement(name = "Collection")
@XmlAccessorType(XmlAccessType.FIELD)
public class Collection {
    @XmlElement(name = "MusicBand")
    private PriorityQueue<MusicBand> musicBands;

    private Storage storage = null;

    public Collection(PriorityQueue<MusicBand> musicBands, Storage storage) {
        this.musicBands = musicBands;
        this.storage = storage;
    }

    public Collection() {
        musicBands = new PriorityQueue<>();
    }


    public PriorityQueue<MusicBand> getMusicBands() {
        return musicBands;
    }

    public void setMusicBands(PriorityQueue<MusicBand> musicBands) {
        this.musicBands = musicBands;
    }

    public String show() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        for (MusicBand musicBand : this.getMusicBands()) {
            result.append(musicBand).append("\n\n\n\n\n");
        }
        return result.toString();
    }

    public String add(MusicBand musicBand) {
        if (musicBand != null) {
            int maxId = findMaxId();
            musicBand.setId(maxId + 1);
            storage.addElement(this.getMusicBands(),musicBand);
            return "     Новая банда была успешно создана";
        }
        return "     Interupt adding";
    }

    private int findMaxId() {
        int maxId = 0;
        for (MusicBand band : this.getMusicBands()) {
            if (band.getId() > maxId) {
                maxId = band.getId();
            }
        }
        return maxId;
    }

    public String countGreaterThanNumberOfParticipants(int numberOfParticipants) {
        try {
            int count = 0;
            for (MusicBand musicBand : this.getMusicBands()) {
                if (musicBand.getNumberOfParticipants() > numberOfParticipants) {
                    count++;
                }
            }
            return "Количество элементов, значение поля numberOfParticipants которых больше " + numberOfParticipants + ": " + count;

        } catch (NumberFormatException e) {
            return "     Второй аргумент не является числом";
        }
    }

    public String viewWithDetails(String nameDetails) {
        StringBuilder textBuilder = new StringBuilder();
        boolean found = false;
        for (MusicBand model : this.getMusicBands()) {
            if (model.getName().startsWith(nameDetails)) {
                found = true;
                textBuilder.append(model).append("\n");
            }
        }
        if (!found) {
            textBuilder.append("Пока тут ничего нет - эх, ну ладно: ").append(nameDetails);
        }
        return textBuilder.toString();
    }

    public String printUniqueEstablishmentDate() {
        List<MusicBand> musicBands = new ArrayList<>(this.getMusicBands());
        Set<ZonedDateTime> uniqueEstablishmentDates = new HashSet<>();
        for (MusicBand band : musicBands) {
            uniqueEstablishmentDates.add(band.getEstablishmentDate());
        }
        StringBuilder result = new StringBuilder();
        result.append("     Уникальные значения поля establishmentDate:\n");
        for (java.time.ZonedDateTime establishmentDate : uniqueEstablishmentDates) {
            result.append("     ").append(establishmentDate).append("\n");
        }
        return result.toString();
    }

    public String removeById(int idNumber) {
        try {
            MusicBand musicBandbyId = getByIdOrNull(idNumber);

            if (musicBandbyId == null) {
                return "     Not Found";
            }
            String removeBandName = musicBandbyId.getName();
            storage.deleteElementById(this.getMusicBands(), idNumber);
            return "     Старая банда " + removeBandName + " была успешно изменена";
        } catch (NumberFormatException e) {
            return "     Second Argument is not a number";
        }
    }

    public MusicBand getByIdOrNull(int id) {
        return storage.getElementById(this.getMusicBands(), id);
    }

    public String removeGreater(int idNumber) {
        try {
            MusicBand musicBandById = getByIdOrNull(idNumber);
            if (musicBandById != null) {
                List<MusicBand> bandsToRemove = new ArrayList<>();
                for (MusicBand band : this.getMusicBands()) {
                    if (band.compareTo(musicBandById) > 0) {
                        bandsToRemove.add(band);
                    }
                }
                for (MusicBand bandToRemove : bandsToRemove) {
                    storage.deleteElementById(this.getMusicBands(), bandToRemove.getId());
                }
                return "     Элементы были удалены";
            } else {
                return "     Music band with ID " + idNumber + " not found.";
            }
        } catch (NumberFormatException e) {
            return "     Second Argument is not a number";
        }
    }

    public String removeHead() {
        MusicBand removedBand = this.getMusicBands().poll();
        if (removedBand != null) {
            return "     Removed head of the collection: " + removedBand;
        } else {
            return "     The collection is empty, nothing to remove.";
        }
    }

    public String removeLower(int idNumber) {
        try {
            MusicBand musicBandById = getByIdOrNull(idNumber);
            if (musicBandById != null) {
                List<MusicBand> bandsToRemove = new ArrayList<>();
                for (MusicBand band : this.getMusicBands()) {
                    if (band.compareTo(musicBandById) < 0) {
                        bandsToRemove.add(band);
                    }
                }
                for (MusicBand bandToRemove : bandsToRemove) {
                    storage.deleteElementById(this.getMusicBands(), bandToRemove.getId());
                }
                return "     Элементы были удалены";
            } else {
                return "     Music band with ID " + idNumber + " not found.";
            }
        } catch (NumberFormatException e) {
            return "     isNan";
        }
    }

}
