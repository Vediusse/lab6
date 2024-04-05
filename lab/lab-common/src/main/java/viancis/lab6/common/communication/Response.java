package viancis.lab6.common.communication;

import viancis.lab6.common.commands.CommandType;


import java.io.Serializable;
import java.util.HashMap;

public record Response(boolean success, String result, HashMap<String, CommandType> commands) implements Serializable {

    // Дополнительный конструктор для успешных ответов
    public Response( boolean success, String result) {
        this(success,  result, null);
    }

    public Response(String result ) {
        this(true,  result, null);
    }

    public Response(String result, boolean success, HashMap<String, CommandType> commands) {
        this(success, result, commands);
    }
}
