package viancis.lab6.common.commands;

import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;


import java.util.*;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super("help", "вывести справку по доступным командам",1);
    }

    @Override
    public Response execute(Request request) {
        if (this.toEnoughArguments(request.command(), args)) {
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

