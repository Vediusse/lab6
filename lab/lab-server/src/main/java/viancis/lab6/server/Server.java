package viancis.lab6.server;

import viancis.lab6.common.commands.InterfaceCommand;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.collection.Collection;
import viancis.lab6.server.collection.Storage;
import viancis.lab6.common.commands.builder.ComandBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException; // TODO useless import
import java.util.HashMap;

public final class Server {

    private static final int PORT = 9876;
    private static final String ENV_NAME = "FILENAME";

    private static final Sender sender = new Sender(System.out);


    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        var sender = new Sender(System.out);
        String path = "lab-server/src/main/java/viancis/lab6/server/file/default.xml"; // TODO lab-server/resources/default.xml - ?
        try{
            var storageManager = new Storage(path);
            var collectionManager = new Collection(storageManager.readCollection());
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        sender.printMessage(new Message(Category.SUCCESS,"не ошибка"));
                        try {
                            storageManager.writeCollection(collectionManager.getMusicBands());
                        } catch (IOException e) {
                            sender.printMessage(new Message(Category.ERROR,"Ошибка"));
                        }
                    }));
            ServerHandler serverHandler = new ServerHandler(sender, createCommands(),collectionManager);
            serverHandler.handleRequests();
        } catch (IOException | IllegalArgumentException e) {
            sender.printMessage(new Message(Category.ERROR,"Ошибка"));
        } finally {
            sender.printMessage(new Message(Category.ERROR,"Ошибка")); // TODO в чем смысл тогда...
        }

    }
    private static String getPath() { // TODO never used localy
        var path = System.getenv(ENV_NAME);
        if (path == null) {
            System.exit(1);
        }
        return path;
    }

    public static HashMap<String, InterfaceCommand> createCommands() {
        ComandBuilder builder = new ComandBuilder();
        return builder.createCommands(sender);
    }
}
