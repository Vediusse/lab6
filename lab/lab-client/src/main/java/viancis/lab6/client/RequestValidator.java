package viancis.lab6.client;

import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;
import viancis.lab6.common.models.MusicBand;
import viancis.lab6.common.models.MusicBandForm;
import viancis.lab6.common.models.User;
import viancis.lab6.common.models.UserForm;

import java.util.HashMap;
import java.util.Objects;


public class RequestValidator {
    private final Sender sender;
    private HashMap<String, CommandType> commandMap;

    public void setToken(String token) {
        this.token = token;
    }

    String token = null;

    public RequestValidator(Sender sender, HashMap<String, CommandType> commands) {
        this.commandMap = commands;
        this.sender = sender;
    }

    public Request validateRequest(ClientInput.UserParseCommand userParse) throws NotEnoughArgumentException, NullCommandException, AuthException {
        CommandType command = commandMap.get(userParse.command());
        if (command == null) {
            throw new NullCommandException();
        }

        int expectedArgCount = command.getArgs();
        int actualArgCount = userParse.commandArgs().length;

        if (expectedArgCount >= 0 && actualArgCount != expectedArgCount) {
            throw new NotEnoughArgumentException();
        }

        if (Objects.equals(userParse.command(), "commands")) {
            throw new NotEnoughArgumentException();
        }

        if(command.isNeedAuth() && token == null){
            throw new AuthException();
        }


        MusicBand musicBand = null;
        User user = null;



        if(command.isNeedLogin()){
            UserForm userForm = new UserForm(sender);
            user = userForm.register();
        }


        if (command.isNeedForm()) {
            MusicBandForm form = new MusicBandForm(sender);
            musicBand = form.createNew();
        }

        return new Request(userParse.command(), userParse.commandArgs(), musicBand, user, this.token);
    }


    private void handleExecuteScriptCommand() {
        sender.printMessage(new Message(Category.INFO, "Команда выполнения скрипта еще не реализована"));
    }


    public class NotEnoughArgumentException extends Throwable {
    }

    public class AuthException extends Throwable {
    }

    public class NullCommandException extends Throwable {
    }
}
