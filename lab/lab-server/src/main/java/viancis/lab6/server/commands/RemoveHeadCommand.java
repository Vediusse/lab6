package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.util.PriorityQueue;

public class RemoveHeadCommand extends AbstractCommand {
    public RemoveHeadCommand() {
        super("remove_head", "выводит первый элемент коллекции и удаляет его",0, CommandType.WITHOUT_ARGUMENTS);
    }


    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            return new Response(collection.removeHead());
        }
        return new Response(false,"Required " + args + " position argument");
    }
}
