package viancis.lab6.common.commands;

import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;

public interface InterfaceCommand {
    String getDescription();

    String getName();

    boolean isNeedForm();
    int getArgs();

    boolean toEnoughArguments(String commandName, int amountArguments);

    Response execute(Request request);
}
