package viancis.lab6.server.commands;



import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.io.Serializable;
import java.util.PriorityQueue;

public interface InterfaceCommand {
    String getDescription();

    String getName();

    boolean isNeedForm();
    int getArgs();

    boolean toEnoughArguments(String commandName, int amountArguments);

    CommandType getType();

    Response execute(Request request, Collection collection);
}

