package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.util.PriorityQueue;

public class FilterStartsWithName extends AbstractCommand {
    public FilterStartsWithName() {
        super("filter", "вывести справку по доступным командам",-1, CommandType.WITH_MANY_ARGUMENTS_FORM);
    }


    @Override
    public Response execute(Request request, Collection collection) {
        String concatenatedArgs = String.join(" ", request.commandArgs());
        return new Response(collection.viewWithDetails(concatenatedArgs));
    }


    private String concatenateStringArray(String command) {
        String[] words = command.split("\\s+");
        StringBuilder arguments = new StringBuilder();
        for (int i = 1; i < words.length; i++) {
            arguments.append(words[i]).append(" ");
        }
        return arguments.toString().trim();
    }


}