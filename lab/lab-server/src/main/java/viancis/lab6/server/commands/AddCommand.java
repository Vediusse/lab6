package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.User;
import viancis.lab6.server.collection.Collection;

import java.util.HashMap;

public class AddCommand extends AbstractCommand {
    public AddCommand() {
        super("add", "добавляет новый элемент в коллекци", 0, CommandType.WITH_FORM);
        this.needForm = true;
    }


    @Override
    public Response execute(Request request, Collection collection, User user) {
        if (request.commandArgs().length != args) {
            return new Response(false, request.toString());
        }
        if (collection != null) {
            if (request.element() != null) {
                return new Response(collection.add(request.element(),user));
            } else{
                return new Response("Ты такой прикольный");
            }
        } else {
            return new Response(false, "Old collection is null");
        }
    }
}
