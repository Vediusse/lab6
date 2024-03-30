package viancis.lab6.client;

import viancis.lab6.common.commands.AbstractCommand; // TODO useless import
import viancis.lab6.common.commands.InterfaceCommand;
import viancis.lab6.common.commands.builder.ComandBuilder;
import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.MusicBandForm;

import java.util.HashMap;


public class RequestValidator {
    private final Sender sender;
    private HashMap<String, InterfaceCommand> commandMap;

    public RequestValidator(Sender sender) {
        ComandBuilder builder = new ComandBuilder();
        this.commandMap = builder.createCommands(sender);
        this.sender = sender;
    }

    public Request validateRequest(ClientInput.UserParseCommand userParse) throws NotEnoughArgumentException, NullCommandException {
        InterfaceCommand command = commandMap.get(userParse.command());
        if (command == null) {
            throw new NullCommandException();
        }

        if (!command.toEnoughArguments(userParse.command(), command.getArgs())) {
            throw new NotEnoughArgumentException();
        }

        if (command.isNeedForm()) {
            MusicBandForm form = new MusicBandForm(sender);
            MusicBand musicBand = form.createNew();
            return new Request(userParse.command(), userParse.commandArgs(), musicBand);
        }

        return new Request(userParse.command(), userParse.commandArgs(), null);
    }

    public class NotEnoughArgumentException extends Throwable { // TODO ошибки в отдельный пакет?
    }

    public class NullCommandException extends Throwable {
    }
}
