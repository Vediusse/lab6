package viancis.lab6.server.collection;

import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.User;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Collection {

    public PriorityQueue<MusicBand> getMusicBands() {
        return musicBands;
    }

    private final PriorityQueue<MusicBand> musicBands;

    private Storage storage = null;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public Collection(PriorityQueue<MusicBand> musicBands, Storage storage) {
        this.musicBands = musicBands;
        this.storage = storage;
    }

    public Collection() {
        musicBands = new PriorityQueue<>();
        this.storage = new Storage();
    }


    public String show() {
        readLock.lock();
        try {
            StringBuilder result = new StringBuilder();
            result.append("\n");
            for (MusicBand musicBand : musicBands) {
                result.append(musicBand).append("\n\n\n\n\n");
            }
            return result.toString();
        } finally {
            readLock.unlock();
        }
    }

    public String add(MusicBand musicBand, User author) {
        writeLock.lock();
        try {
            if (musicBand != null) {
                long maxId = findMaxId();
                musicBand.setId(maxId + 1);
                storage.addElement(musicBands, musicBand, author);
                return "     Новая банда была успешно создана";
            }
            return "Не удалось создать элемент";
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            writeLock.unlock();
        }
    }


    public void addInCollection(MusicBand musicBand, User author) {
        writeLock.lock();
        try {
            if (musicBand != null) {
                this.musicBands.add(musicBand);
                musicBand.setAuthor(author);
            }
        }finally {
            writeLock.unlock();
        }
    }

    private long findMaxId() {
        readLock.lock();
        try {
            long maxId = 0;
            for (MusicBand band : musicBands) {
                if (band.getId() > maxId) {
                    maxId = band.getId();
                }
            }
            return maxId;
        } finally {
            readLock.unlock();
        }
    }

    public String countGreaterThanNumberOfParticipants(int numberOfParticipants) {
        readLock.lock();
        try {
            int count = 0;
            for (MusicBand musicBand : musicBands) {
                if (musicBand.getNumberOfParticipants() > numberOfParticipants) {
                    count++;
                }
            }
            return "Количество элементов, значение поля numberOfParticipants которых больше " + numberOfParticipants + ": " + count;
        } finally {
            readLock.unlock();
        }
    }

    public String viewWithDetails(String nameDetails) {
        readLock.lock();
        try {
            StringBuilder textBuilder = new StringBuilder();
            boolean found = false;
            for (MusicBand model : musicBands) {
                if (model.getName().startsWith(nameDetails)) {
                    found = true;
                    textBuilder.append(model).append("\n");
                }
            }
            if (!found) {
                textBuilder.append("Пока тут ничего нет - эх, ну ладно: ").append(nameDetails);
            }
            return textBuilder.toString();
        } finally {
            readLock.unlock();
        }
    }

    public String printUniqueEstablishmentDate() {
        readLock.lock();
        try {
            Set<ZonedDateTime> uniqueEstablishmentDates = new HashSet<>();
            for (MusicBand band : musicBands) {
                uniqueEstablishmentDates.add(band.getEstablishmentDate());
            }
            StringBuilder result = new StringBuilder();
            result.append("     Уникальные значения поля establishmentDate:\n");
            for (ZonedDateTime establishmentDate : uniqueEstablishmentDates) {
                result.append("     ").append(establishmentDate).append("\n");
            }
            return result.toString();
        } finally {
            readLock.unlock();
        }
    }

    public String removeById(int idNumber,User user) {
        writeLock.lock();
        try {
            MusicBand musicBandbyId = storage.getElementById(musicBands, idNumber);
            if (musicBandbyId == null) {
                return "     Not Found";
            }
            String removeBandName = musicBandbyId.getName();
            MusicBand band = storage.deleteElementById(musicBands, idNumber,user);
            if (band == null){
                return "Не удалось удалить банду";
            }
            return "     Старая банда " + removeBandName + " была успешно изменена";
        } catch (SQLException e) {
            return "     Не удалось удалить элемент";
        } finally {
            writeLock.unlock();
        }
    }

    public String removeGreater(int idNumber, User user) {
        writeLock.lock();
        try {
            MusicBand musicBandById = storage.getElementById(musicBands, idNumber);
            if (musicBandById != null) {
                StringBuilder bandsToRemoveString = new StringBuilder();
                List<MusicBand> bandsToRemove = new ArrayList<>();
                for (MusicBand band : musicBands) {
                    if (band.compareTo(musicBandById) > 0) {
                        bandsToRemove.add(band);
                    }
                }
                for (MusicBand bandToRemove : bandsToRemove) {
                    MusicBand deletedMusicBand = storage.deleteElementById(musicBands, bandToRemove.getId(),user);
                    if (deletedMusicBand == null){
                        bandsToRemoveString.append("Не удалось удалить элемент с id ").append(bandToRemove.getId()).append("\n");
                        continue;
                    }
                    bandsToRemoveString.append(bandToRemove.toString());;
                }
                return bandsToRemoveString.toString();
            } else {
                return "     Music band with ID " + idNumber + " not found.";
            }
        } catch (SQLException e) {
            return "     Не удалось удалить элемент";
        } finally {
            writeLock.unlock();
        }
    }

    public MusicBand getByIdOrNull(int id) {
        readLock.lock();
        try {
            return storage.getElementById(musicBands, id);
        } finally {
            readLock.unlock();
        }
    }

    public String removeHead(User user) {
        writeLock.lock();
        try {
            MusicBand removedBand = musicBands.peek();
            if (removedBand != null) {
                MusicBand deletedMusicBand = storage.deleteElementById(musicBands, removedBand.getId(),user);
                if (deletedMusicBand == null){
                    return "Ошибка удаления";
                }
                return deletedMusicBand.toString();
            } else {
                return "     The collection is empty, nothing to remove.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            writeLock.unlock();
        }
    }

    public String removeLower(int idNumber, User user) {
        writeLock.lock();
        try {
            MusicBand musicBandById = storage.getElementById(musicBands, idNumber);

            if (musicBandById != null) {
                StringBuilder bandsToRemoveString = new StringBuilder();
                List<MusicBand> bandsToRemove = new ArrayList<>();
                for (MusicBand band : musicBands) {
                    if (band.compareTo(musicBandById) < 0) {
                        bandsToRemove.add(band);
                    }
                }
                for (MusicBand bandToRemove : bandsToRemove) {
                    MusicBand deletedMusicBand = storage.deleteElementById(musicBands, bandToRemove.getId(),user);
                    if (deletedMusicBand == null){
                        bandsToRemoveString.append("Не удалось удалить элемент с id ").append(bandToRemove.getId()).append("\n");
                        continue;
                    }
                    bandsToRemoveString.append(bandToRemove.toString());;
                }
                return bandsToRemoveString.toString();
            } else {
                return "     Music band with ID " + idNumber + " not found.";
            }
        } catch (SQLException e) {
            return "     Не удалось удалить элемент";
        } finally {
            writeLock.unlock();
        }
    }

    public String update(MusicBand oldElement, MusicBand updateElement, User user) {
        writeLock.lock();
        try {
            String removeBandName = oldElement.getName();
            boolean removed = storage.updateElementById(musicBands, oldElement, updateElement, user);
            if (removed) {
                return "     Старая банда " + removeBandName + " была успешно изменена";
            } else {
                return "     Старая банда " + removeBandName + " не была найдена";
            }
        } catch (SQLException e) {
            return e.getMessage();
        } finally {
            writeLock.unlock();
        }
    }
}
