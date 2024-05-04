package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.User;
import viancis.lab6.server.collection.Collection;

public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand() {
        super("update", " обновляет значение элемента коллекции, id которого равен заданному",1, CommandType.WITH_ARGUMENTS_FORM);
        this.needForm = true;
    }

    @Override
    public Response execute(Request request, Collection collection, User user) {
        if (request.commandArgs().length == args) {
            if(user == null){
                return new Response("Ошибка авторизации");
            }
            int idNumber;
            try {
                idNumber = Integer.parseInt(request.commandArgs()[0]);
            } catch (NumberFormatException e) {
                return new Response(false, "Arg is not a number");
            }

            MusicBand musicBandById = collection.getByIdOrNull(idNumber);
            if (musicBandById == null) {
                return new Response(false,"Not Found");
            }

            return new Response(true,collection.update(musicBandById,request.element(),user));
        }
        return new Response(false,request.commandArgs().length + " " + args);
    }
}
