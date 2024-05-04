package viancis.lab6.server.commands.builder;

import viancis.lab6.server.commands.AbstractCommand;
import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.server.commands.InterfaceCommand;

import java.util.HashMap;

public class ComandBuilder {


    private boolean debug = true;
    private final HashMap<String, CommandType> commandType = new HashMap<>();

    public ComandBuilder(boolean debug) {
        this.debug = debug;
    }

    public ComandBuilder() {
    }

    public HashMap<String, InterfaceCommand> createCommands(Sender sender) {
        HashMap<String, InterfaceCommand> commands = new HashMap<>();

        String[] commandClasses = {
                "viancis.lab6.server.commands.AddCommand",
                "viancis.lab6.server.commands.ClearCommand",
                "viancis.lab6.server.commands.CountGreaterThanNumberOfParticipantsCommand",
                "viancis.lab6.server.commands.ExecuteScriptCommand",
                "viancis.lab6.server.commands.ExitCommand",
                "viancis.lab6.server.commands.FilterStartsWithName",
                "viancis.lab6.server.commands.HelpCommand",
                "viancis.lab6.server.commands.InfoCommand",
                "viancis.lab6.server.commands.PrintUniqueEstablishmentDate",
                "viancis.lab6.server.commands.RemoveByIdCommand",
                "viancis.lab6.server.commands.RemoveGreaterCommand",
                "viancis.lab6.server.commands.RemoveHeadCommand",
                "viancis.lab6.server.commands.RemoveLowerCommand",
                "viancis.lab6.server.commands.ShowCommand",
                "viancis.lab6.server.commands.LoginCommand",
                "viancis.lab6.server.commands.AuthCommand",
                "viancis.lab6.server.commands.UpdateIdCommand"
        };

        for (String commandClassName : commandClasses) {
            try {
                Class<?> commandClass = Class.forName(commandClassName);
                InterfaceCommand command = (InterfaceCommand) commandClass.getDeclaredConstructor().newInstance();
                commands.put(command.getName(), command);

                this.commandType.put(command.getName(), command.getType());
            } catch (ClassNotFoundException e) {
                if (this.debug){
                    sender.printMessage(new Message(Category.WARNING, "Class not found: " + commandClassName));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AbstractCommand.commandMap = commands;
        return commands;
    }


    public HashMap<String, CommandType> getCommandType(Sender sender) {
        return this.commandType;
    }
}
