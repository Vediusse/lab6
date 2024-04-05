package viancis.lab6.server;

import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.server.collection.Storage;
import viancis.lab6.server.logger.CustomLogger;


import java.io.IOException;
import java.io.InputStream;

public final class Server {
    private static final int PORT = 9876;
    private static final String ENV_NAME = "/logs/log.logs";
    private static final String PATH = "/file/default.xml";

    private static final Sender sender = new Sender(System.out);

    private static final CustomLogger logger = new CustomLogger();


    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        Sender sender = new Sender(System.out);

        try{
            InputStream resourceStream = Server.class.getResourceAsStream(PATH);
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + PATH);
            }
            Storage storageManager = new Storage(resourceStream);
            Collection collectionManager = new Collection(storageManager.readCollection(), storageManager);
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {

                        try {
                            sender.printMessage(new Message(Category.SUCCESS,"База данных была успешна сохранена - спасибо ты такой классный"));
                            logger.logMessageToFile(Category.WARNING,"Даза Банных сохранена");
                            storageManager.writeCollection(collectionManager.getMusicBands());

                        } catch (IOException e) {
                            sender.printMessage(new Message(Category.ERROR,"Ошибка записи файла бд было утерено"));
                        }
                    }));
            ServerHandler serverHandler = new ServerHandler(sender, collectionManager);
            serverHandler.handleRequests();
        } catch (IOException | IllegalArgumentException e) {
            sender.printMessage(new Message(Category.ERROR,"Ошибка чтения файла коллекция теперь пуста"));
        }
    }


}
