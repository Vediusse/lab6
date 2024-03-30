package viancis.lab6.common.commands;



import viancis.lab6.common.models.Crud; // TODO never used

import java.util.HashMap;
import java.util.LinkedHashMap; // TODO never used
import java.util.Map; // TODO never used

// TODO попробуй lombok
public abstract class AbstractCommand implements InterfaceCommand {
    private final String name;
    private final String description;

    public static HashMap<String, InterfaceCommand> commandMap;

    public boolean isNeedForm() {
        return needForm;
    }

    public boolean needForm = false;
    public final int args;


    private int uniqueId = 1; 


    public AbstractCommand(String name, String description, int args) {
        this.name = name;
        this.description = description;
        this.args = args;
    }


    public String getName() {
        return name;
    }


    /**
     * It returns the description of the command.
     *
     * @return The description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * It returns a string representation of the object.
     *
     * @return The name of the question and the description of the command.
     */
    @Override
    public String toString() {
        return name + " (" + description + ")"; // TODO String.format("%s (%s)", name, description)
    }


    public boolean toEnoughArguments(String commandName, int amountArguments) {
        String[] commandParts = commandName.split(" ");
        return commandParts.length == amountArguments;
    }


    public boolean lessArguments(String commandName, int amountArguments) {
        String[] commandParts = commandName.split(" ");
        return commandParts.length >= amountArguments;
    }


    public int getArgs() {
        return args;
    }
}
