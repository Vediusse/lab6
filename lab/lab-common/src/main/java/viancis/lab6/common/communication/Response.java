package viancis.lab6.common.communication;

import java.io.Serializable;

public record Response(boolean success, String errorMessage, String result) implements Serializable {

    // Дополнительный конструктор для успешных ответов
    public Response(String result ) {
        this(true, null, result);
    }

    // Дополнительный конструктор для неуспешных ответов
    public Response(boolean success, String errorMessage) {
        this(success, errorMessage, null);
    }
}
