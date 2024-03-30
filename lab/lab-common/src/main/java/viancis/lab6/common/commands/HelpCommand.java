package viancis.lab6.common.commands;

import viancis.lab6.common.communication.ClientInput; // TODO useless import
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;


import java.util.*; // TODO bad idea
import java.util.stream.Collectors;

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
            // TODO по идеи тут стримы можно эффективно вставить
            // return new Response(
            //     (String) commandMap.values().stream()
            //                         .sorted(Comparator.comparing(InterfaceCommand::getName))
            //                         .map(command -> String.format("\t%s - %s", command.getName(), command.getDescription()))
            //                         .collect(Collectors.joining("\n"))
            // );
        }
        return new Response(false,"Required " + args + " position argument"); // TODO String.format("Req %s pos args", args)
    }
}

