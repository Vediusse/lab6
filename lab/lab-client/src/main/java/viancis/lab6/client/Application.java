package viancis.lab6.client;

import viancis.lab6.client.utils.ScriptParser;
import viancis.lab6.common.commands.CommandType;
import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Application {

    private final Scanner scanner = new Scanner(System.in);

    private final Sender sender = new Sender(System.out);

    private boolean listeningAndSendingModeOn = true;

    private ClientInput clientInput = new ClientInput(System.in);
    private Handler handlerClient;
    private RequestValidator requestValidator;

    private static final String ENV_NAME = "/script.sh";


    public void infoAndConnect() {
        sender.printMessage(new Message(Category.INFO,
                """
                        Для начала работы с приложением необходимо ввести адрес сервера.
                        После подключения вы сможете взаимодействовать с коллекцией в интерактивном режиме.
                        Для получения справки по командам введите 'help'."""));
        ConnectionForm connectionForm = new ConnectionForm(sender, scanner);
        this.handlerClient = connectionForm.getConnectionHandler();
        HashMap<String, CommandType> commandsResponse = this.receiveAllCommands();

        if ( commandsResponse != null) {
            this.requestValidator = new RequestValidator(this.sender, commandsResponse);
            this.connect();
        }else{
            sender.printMessage(new Message(Category.ERROR,"Повторите попытку позже или перезапустите приложение"));
        }

    }

    private HashMap<String, CommandType> receiveAllCommands() {
        Request request = new Request("commands", null, null);
        handleRegularCommand(request);
        try {
            Response response = handlerClient.receiveResponse();
            if (response != null) {
                if(response.commands() != null){
                    return response.commands();
                }else{
                    sender.printMessage(new Message(Category.ERROR, "Не удалось получить список команды "));
                    return null;
                }
            } else {
                sender.printMessage(new Message(Category.ERROR, "Не удалось получить ответ от сервера"));
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            sender.printMessage(new Message(Category.ERROR, "Не удалось получить ответ от сервера"));
        }
        return null;
    }

    public void connect() {
        while (listeningAndSendingModeOn) {
            ClientInput.UserParseCommand userParseCommand = this.clientInput.getParsedCommand();
            this.sendAndReceived(userParseCommand);
        }
    }

    public void sendAndReceived(ClientInput.UserParseCommand userParseCommand){
        if (Objects.equals(userParseCommand.command(), "execute_commands")) {
            this.handleExecuteCommand();
            return;
        }
        try {
            Request request = requestValidator.validateRequest(userParseCommand);
            handleRegularCommand(request);
        } catch (RequestValidator.NotEnoughArgumentException e) {
            sender.printMessage(new Message(Category.WARNING, "Слишком много аргументов было дано " + userParseCommand.commandArgs().length));
            return;
        } catch (RequestValidator.NullCommandException e) {
            sender.printMessage(new Message(Category.ERROR, "Команда не найдена " + userParseCommand.command()));
            return;
        }
        try {
            Response response = handlerClient.receiveResponse();
            if (response != null) {
                printResponse(response);
            } else {
                sender.printMessage(new Message(Category.ERROR, "Не удалось получить ответ от сервера"));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            sender.printMessage(new Message(Category.ERROR, "Не удалось получить ответ от сервера"));
        }
    }

    private void printResponse(Response response) {
        if (response.success()) {
            sender.printMessage(new Message(Category.SUCCESS, response.result()));
        } else {
            sender.printMessage(new Message(Category.WARNING, response.result()));
        }
    }


    private void handleExecuteCommand(){
        InputStream scriptInputStream = getClass().getResourceAsStream(ENV_NAME);
        if (scriptInputStream == null) {
            sender.printMessage(new Message(Category.ERROR,"Не настроенно окружение"));
            return;
        }
        List<String> listCommands = ScriptParser.parseScript(scriptInputStream);
        if (listCommands.isEmpty()) {
            return;
        }
        for (String command : listCommands) {
            ClientInput.UserParseCommand userParseCommand = this.clientInput.getParsedCommand(command);
            this.sendAndReceived(userParseCommand);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                sender.printMessage(new Message(Category.ERROR, "Ты ваще нормальный ERRORERRORERRORERRORERRORERROR ERROR v"));
            }
        }

    }


    private void handleRegularCommand(Request command) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            byte[] commandBytes = baos.toByteArray();

            this.handlerClient.sendData(commandBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
