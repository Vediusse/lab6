package viancis.lab6.server.commands;


import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.server.collection.Collection;

public class AddCommand extends AbstractCommand {
    public AddCommand() {
        super("add", "добавляет новый элемент в коллекци", 0, CommandType.WITH_FORM);
        this.needForm = true;
    }


    @Override
    public Response execute(Request request, Collection collection) {
        if (request.commandArgs().length == args) {
            if (collection != null) {
                if (request.element() != null) {
                    collection.add(request.element());
                    return new Response("LIFELINE");
                }

            } else {
                return new Response(false, "Old collection is null", null);
            }
        }
        return new Response(false, "Required " + args + " position argument " + request.commandArgs().length + " were given!");
    }
}
