package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.util.PriorityQueue;

public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand() {
        super("update", " обновляет значение элемента коллекции, id которого равен заданному",1, CommandType.WITH_ARGUMENTS_FORM);
        this.needForm = true;
    }

    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            try {
                int idNumber;
                try {
                    idNumber = Integer.parseInt(request.commandArgs()[0]);
                } catch (NumberFormatException e) {
                    return new Response(false, "Arg is not a number");
                }

                MusicBand musicBandById = collection.getByIdOrNull(idNumber);
                if (musicBandById == null) {
                    return new Response(false,"     Not Found");
                }
                if(request.element() == null){
                    return new Response(false,"     Неверный запрос! Как вы сюда попали");
                }
                String removeBandName = musicBandById.getName();
                boolean removed = collection.getMusicBands().removeIf(band -> band.getId() == idNumber);
                if (removed) {
                    collection.add(request.element());
                    return new Response("     Старая банда " + removeBandName + " была успешно изменена");
                } else {
                    return new Response(false,"     Старая банда " + removeBandName + " не была найдена");
                }
            } catch (NumberFormatException e) {
                return new Response(false,"     Second Argument is not a number");
            }
        }
        return new Response(false,"Required " + args + " position argument");
    }
}
