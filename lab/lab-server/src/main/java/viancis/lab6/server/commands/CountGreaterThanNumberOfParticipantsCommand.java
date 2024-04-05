package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.util.PriorityQueue;

public class CountGreaterThanNumberOfParticipantsCommand extends AbstractCommand {
    public CountGreaterThanNumberOfParticipantsCommand() {
        super("count_greater_than_number_of_participants", "выводит количество элементов, значение поля numberOfParticipants которых больше заданного",1, CommandType.WITH_ARGUMENTS);
    }


    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            if (collection != null) {
                int numberOfParticipants;
                try {
                    numberOfParticipants = Integer.parseInt(request.commandArgs()[0]);
                } catch (NumberFormatException e) {
                    return new Response(false, "Arg is not a number");
                }
                String result = collection.countGreaterThanNumberOfParticipants(numberOfParticipants);
                return new Response(result);
            } else {
                return new Response(false, "Old collection is null");
            }
        }
        return new Response(false, "Required " + args + " position argument");
    }

}
