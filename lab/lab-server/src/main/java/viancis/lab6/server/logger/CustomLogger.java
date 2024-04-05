package viancis.lab6.server.logger;

import viancis.lab6.common.messages.Category;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {

    private static final String LOG_FILE_PATH = "/Users/rublev/DEV/lab6/lab/lab-server/src/main/resources/logs/log.logs";

    private Logger fileLogger;
    private FileHandler fileHandler;

    public CustomLogger() {
        try {
            File logDir = new File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            fileLogger = Logger.getLogger("CustomLogger");
            fileHandler = new FileHandler(LOG_FILE_PATH, true);
            fileLogger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize logger", e);
        }
    }

    public void logMessageToFile(Category category, String message) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        String formattedTimestamp = LocalDateTime.now().format(dateFormatter);
        fileLogger.info("[" + category.name() + "] [" + formattedTimestamp + "]\n" + message);
    }

    public void closeLogger() {
        if (fileHandler != null) {
            fileHandler.close();
        }
    }
}
