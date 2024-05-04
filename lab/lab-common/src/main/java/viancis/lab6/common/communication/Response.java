package viancis.lab6.common.communication;

import viancis.lab6.common.commands.CommandType;


import java.io.Serializable;
import java.util.HashMap;

public record Response(boolean success, String result, HashMap<String, CommandType> commands, String commandName) implements Serializable {

    // Дополнительный конструктор для успешных ответов
    public Response( boolean success, String result) {
        this(success,  result, null,null);
    }

    public Response(String result ) {
        this(true,  result, null,null);
    }

    public Response(String result, String commandName) {
        this(true,  result, null,commandName);
    }

    public Response(String result, boolean success, HashMap<String, CommandType> commands) {
        this(success, result, commands,null);
    }
}
