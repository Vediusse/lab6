package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.User;
import viancis.lab6.server.collection.Collection;

import java.util.HashMap;
import java.util.PriorityQueue;

public class ClearCommand extends AbstractCommand {
    public ClearCommand() {
        super("clear", "очищает коллекцию",0, CommandType.WITHOUT_ARGUMENTS);
    }



    @Override
    public Response execute(Request request, Collection collection, User user) {
        if (this.toEnoughArguments(request.command(), args)) {
                if (collection != null) {
                    collection.getMusicBands().clear();
                    return new Response("     Все банды были удалены");
                } else {
                    return new Response(false ,"     Незя удалить пустоту");
                }

        }
        return new Response(false, "Required " + args + " position argument " + request.commandArgs().length + " were given!");
    }
}
