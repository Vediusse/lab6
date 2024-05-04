package viancis.lab6.server;

import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.server.collection.Storage;
import viancis.lab6.server.db.ConnectionBataDase;
import viancis.lab6.server.db.ManagerDb;
import viancis.lab6.server.logger.CustomLogger;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public final class Server {
    private static final int PORT = 9876;
    private static final String ENV_NAME = "/logs/log.logs";
    private static final String PATH = "/file/default.xml";

    private static final Sender sender = new Sender(System.out);



    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        Sender sender = new Sender(System.out);

        try{
            ConnectionBataDase connecton = new ConnectionBataDase();
            ManagerDb db = new ManagerDb(connecton);
            db.initializeDB();
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        sender.printMessage(new Message(Category.SUCCESS,"База данных была успешна сохранена - спасибо ты такой классный"));
                    }));
            ServerHandler serverHandler = new ServerHandler(sender, db.readCollection());
            serverHandler.startRequestReading();
        } catch (IllegalArgumentException e) {
            sender.printMessage(new Message(Category.ERROR,"Ошибка чтения файла коллекция теперь пуста"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
