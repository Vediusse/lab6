package viancis.lab6.server.collection;

import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

import viancis.lab6.server.collection.Collection;
import viancis.lab6.server.db.ConnectionBataDase;
import viancis.lab6.server.db.ManagerDb;
import viancis.lab6.server.db.RequestsDb;

public class Storage {

    public void addElement(PriorityQueue<MusicBand> collection, MusicBand element, User author) throws SQLException {
        ManagerDb db = new ManagerDb(new ConnectionBataDase());
        collection.add(element);
        element.setAuthor(author);
        db.addMusikBand(element, author);



    }

    public MusicBand getElementById(PriorityQueue<MusicBand> collection, long id) {
        for (MusicBand element : collection) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public MusicBand deleteElementById(PriorityQueue<MusicBand> collection, long id, User user) throws SQLException {
        MusicBand elementToRemove = this.getElementById(collection, id);
        if (Objects.equals(elementToRemove.getAuthor().getLogin(), user.getLogin())) {
            collection.remove(elementToRemove);
            ManagerDb db = new ManagerDb(new ConnectionBataDase());
            db.deleteMusikBandById(elementToRemove.getId());
            return elementToRemove;
        }
        return null;
    }


    public boolean updateElementById(PriorityQueue<MusicBand> collection, MusicBand oldElement, MusicBand updatedElement, User user) throws SQLException {
        if(Objects.equals(oldElement.getAuthor().getLogin(), user.getLogin())){
            boolean removed = collection.removeIf(band -> band.equals(oldElement));
            if (removed) {

                ManagerDb db = new ManagerDb(new ConnectionBataDase());
                updatedElement.setAuthor(user);
                collection.add(updatedElement);
                long id = db.updateMusikBandById(oldElement.getId(), updatedElement);
                updatedElement.setId(id);
            }
            return removed;
        }
        return false;
    }


}
