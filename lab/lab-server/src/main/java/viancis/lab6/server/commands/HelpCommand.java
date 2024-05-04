package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.User;
import viancis.lab6.server.collection.Collection;

import java.util.*;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help", "вывести справку по доступным командам",0, CommandType.WITHOUT_ARGUMENTS);
    }

    @Override
    public Response execute(Request request, Collection collectionMap, User user) {
        if (request.commandArgs().length == args) {
            List<InterfaceCommand> sortedCommands = new ArrayList<>(commandMap.values());
            sortedCommands.sort(Comparator.comparing(InterfaceCommand::getName));
            StringJoiner result = new StringJoiner("\n");
            for (InterfaceCommand command : sortedCommands) {
                result.add("     " + command.getName() + " - " + command.getDescription());
            }
            return new Response(result.toString());
        }
        return new Response(false,"Required " + args + " position argument");
    }
}

