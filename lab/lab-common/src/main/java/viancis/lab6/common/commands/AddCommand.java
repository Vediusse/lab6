package viancis.lab6.common.commands;


import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.models.Crud;

public class AddCommand extends AbstractCommand {
    public AddCommand() {
        super("add", "добавляет новый элемент в коллекци",1);
        this.needForm = true;
    }


    @Override
    public Response execute(Request request) {
        if (this.toEnoughArguments(request.command(), args)) {
            if (Request.priorityQueue() != null) {
                Crud crud = new Crud(Request.priorityQueue());
                if (request.element() != null){
                    return new Response(crud.add(request.element()));
                }

            } else {
                return new Response(false, "Old collection is null", null);
            }
        }
        return new Response(false, "Required " + args + " position argument");
    }
}
