package viancis.lab6.server.commands;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.util.PriorityQueue;

public class PrintUniqueEstablishmentDate extends AbstractCommand {
    public PrintUniqueEstablishmentDate() {
        super("print_unique_establishment_date", "выводит уникальные значения поля establishmentDate всех элементов в коллекции",0, CommandType.WITHOUT_ARGUMENTS);
    }

    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            return new Response(collection.printUniqueEstablishmentDate());
        }
        return new Response(false,"Required " + args + " position argument");
    }
}