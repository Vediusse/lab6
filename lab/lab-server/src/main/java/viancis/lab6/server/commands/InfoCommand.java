package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.server.collection.Collection;

import java.text.SimpleDateFormat;
import java.util.PriorityQueue;

public class InfoCommand extends AbstractCommand {
    public InfoCommand() {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)",0, CommandType.WITHOUT_ARGUMENTS);
    }



    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            if (collection.getMusicBands().isEmpty()) {
                return new Response(false,"     Коллекция пуста.");
            }

            Class<?> queueClass = collection.getMusicBands().getClass();
            String typeName = queueClass.getName();

            StringBuilder info = new StringBuilder();
            info.append("     Тип коллекции: ").append(typeName).append("\n");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            info.append("     Количество элементов: ").append(collection.getMusicBands().size()).append("\n");

            for (MusicBand musicBand : collection.getMusicBands()) {
                info.append("     ").append("Элемент: ").append(musicBand).append("\n");
            }
            return new Response(info.toString());
        }

        return new Response(false,"Required " + args + " position argument");
    }
}
