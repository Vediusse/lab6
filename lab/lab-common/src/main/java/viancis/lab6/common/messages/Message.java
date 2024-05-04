package viancis.lab6.common.messages;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Message {
    private final Category category;
    private final String message;
    private final LocalDateTime timestamp;

    public Message(Category category, String message) {
        this.category = category;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }







    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        String formattedTimestamp = timestamp.format(formatter);
        return "[" + category.getColorCode() + category.name() + "\u001B[0m] [" + formattedTimestamp + "] " + "\n" + message;
    }
}
