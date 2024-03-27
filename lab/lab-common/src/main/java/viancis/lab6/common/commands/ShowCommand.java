package viancis.lab6.common.commands;

import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.Crud;

public class ShowCommand extends AbstractCommand {
    public ShowCommand() {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении",1);
    }


    @Override
    public Response execute(Request request) {
        if (this.toEnoughArguments(request.command(), args)) {
            if (request.priorityQueue() != null) {
                Crud crud = new Crud(request.priorityQueue());
                String collectionContent = crud.show();
                if (!collectionContent.isEmpty()) {
                    return new Response(collectionContent);
                } else {
                    return new Response(false, "The collection is empty");
                }
            } else {
                return new Response(false, "Old collection is null");
            }
        }
        return new Response(false, "Required " + args + " position argument");
    }
}
