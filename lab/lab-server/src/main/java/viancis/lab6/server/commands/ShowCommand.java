package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.util.PriorityQueue;

public class ShowCommand extends AbstractCommand {
    public ShowCommand() {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении",0, CommandType.WITHOUT_ARGUMENTS);
    }


    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            if (collection != null) {
                String collectionContent = collection.show();
                if (!collectionContent.isEmpty()) {
                    return new Response(collectionContent);
                } else {
                    return new Response(false, "The collection is empty");
                }
            } else {
                return new Response(false, "Old collection is null");
            }
        }
        return new Response(false, "Required " + args + " position argument");
    }
}
