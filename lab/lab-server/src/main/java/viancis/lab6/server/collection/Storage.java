package viancis.lab6.server.collection;

import viancis.lab6.common.models.MusicBand;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.PriorityQueue;

import viancis.lab6.server.collection.Collection;

public class Storage {

    private final InputStream inputStream;

    public Storage(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        this.inputStream = inputStream;
    }

    public PriorityQueue<MusicBand> readCollection() throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Collection.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Collection collection = (Collection) jaxbUnmarshaller.unmarshal(inputStream);
            if (collection != null) {
                return collection.getMusicBands();
            } else {
                throw new IOException("Failed to read collection from XML file");
            }
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new IOException("Failed to read collection from XML file: " + e.getMessage());
        }
    }

    public void writeCollection(PriorityQueue<MusicBand> collection) throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Collection.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Collection collectionObject = new Collection(collection, this);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(collectionObject, outputStream);

            byte[] xmlBytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlBytes);

        } catch (JAXBException e) {
            e.printStackTrace();
            throw new IOException("Failed to write collection to XML file: " + e.getMessage());
        }
    }

    public void addElement(PriorityQueue<MusicBand> collection, MusicBand element) {
        collection.add(element);
    }

    public MusicBand getElementById(PriorityQueue<MusicBand> collection, int id) {
        for (MusicBand element : collection) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public void deleteElementById(PriorityQueue<MusicBand> collection, int id) {
        MusicBand elementToRemove = null;
        for (MusicBand element : collection) {
            if (element.getId() == id) {
                elementToRemove = element;
                break;
            }
        }
        if (elementToRemove != null) {
            collection.remove(elementToRemove);
        }
    }


    public void updateElementById(PriorityQueue<MusicBand> collection, int id, MusicBand updatedElement) {
        for (MusicBand element : collection) {
            if (element.getId() == id) {
                collection.remove(element);
                collection.add(updatedElement);
                break;
            }
        }
    }

    public PriorityQueue<MusicBand> getAllOrNull() {
        try {
            return readCollection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
