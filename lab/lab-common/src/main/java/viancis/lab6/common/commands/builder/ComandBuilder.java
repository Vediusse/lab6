package viancis.lab6.common.commands.builder;

import viancis.lab6.common.commands.AbstractCommand;
import viancis.lab6.common.commands.InterfaceCommand;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ComandBuilder {


    public HashMap<String, InterfaceCommand> createCommands(Sender sender) {
        HashMap<String, InterfaceCommand> commands = new HashMap<>();

        String[] commandClasses = {
                "viancis.lab6.common.commands.AddCommand",
                "viancis.lab6.common.commands.ClearCommand",
                "viancis.lab6.common.commands.CountGreaterThanNumberOfParticipantsCommand",
                "viancis.lab6.common.commands.ExecuteScriptCommand",
                "viancis.lab6.common.commands.ExitCommand",
                "viancis.lab6.common.commands.FilterStartsWithName",
                "viancis.lab6.common.commands.HelpCommand",
                "viancis.lab6.common.commands.InfoCommand",
                "viancis.lab6.common.commands.PrintUniqueEstablishmentDate",
                "viancis.lab6.common.commands.RemoveByIdCommand",
                "viancis.lab6.common.commands.RemoveGreaterCommand",
                "viancis.lab6.common.commands.RemoveHeadCommand",
                "viancis.lab6.common.commands.RemoveLowerCommand",
                "viancis.lab6.common.commands.SaveCommand",
                "viancis.lab6.common.commands.ShowCommand",
                "viancis.lab6.common.commands.UpdateIdCommand"
        };

        for (String commandClassName : commandClasses) {
            try {
                Class<?> commandClass = Class.forName(commandClassName);
                InterfaceCommand command = (InterfaceCommand) commandClass.getDeclaredConstructor().newInstance();
                commands.put(command.getName(), command);
            } catch (ClassNotFoundException e) {
                sender.printMessage(new Message(Category.WARNING, "Class not found: " + commandClassName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AbstractCommand.commandMap = commands;
        return commands;
    }
}
