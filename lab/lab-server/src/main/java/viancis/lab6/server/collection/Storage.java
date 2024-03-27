package viancis.lab6.server.collection;

import viancis.lab6.common.models.MusicBand;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

public class Storage {

    private final String path;

    public Storage(final String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if (path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        this.path = path;
    }


    public PriorityQueue<MusicBand> readCollection() throws IOException {
        var file = new File(path);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("File can't be created");
            }
        }
        if (!file.isFile()) {
            throw new IOException(path + " is not a valid file");
        }
        if (!file.canRead()) {
            throw new IOException("File can't be read");
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Collection.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Collection collection = (Collection) jaxbUnmarshaller.unmarshal(file);
            return collection.getMusicBands();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void writeCollection(PriorityQueue<MusicBand> collection) throws IOException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("File can't be created");
                }
            }
            if (!file.isFile()) {
                throw new IOException(path + " is not a valid file");
            }
            if (!file.canWrite()) {
                throw new IOException("File can't be written");
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(Collection.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Collection collectionObject = new Collection(collection);

            marshaller.marshal(collectionObject, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }



}
