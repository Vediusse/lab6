package viancis.lab6.client;

import viancis.lab6.common.communication.ClientInput;
import viancis.lab6.common.communication.Request;
import viancis.lab6.common.communication.Response;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Application {

    private final Scanner scanner = new Scanner(System.in);

    private final Sender sender = new Sender(System.out);

    private final RequestValidator commandValidator = new RequestValidator(sender); // TODO not used
    private boolean listeningAndSendingModeOn = true;

    private ClientInput clientInput = new ClientInput(System.in);
    private Handler handlerClient;
    private final RequestValidator requestValidator = new RequestValidator(sender);

    public void infoAndConnect() {
        sender.printMessage(new Message(Category.INFO,
                """
                        Для начала работы с приложением необходимо ввести адрес сервера.
                        После подключения вы сможете взаимодействовать с коллекцией в интерактивном режиме.
                        Для получения справки по командам введите 'help'."""));
        ConnectionForm connectionForm = new ConnectionForm(sender, scanner);
        this.handlerClient = connectionForm.getConnectionHandler();
        this.connect();
    }

    public void connect() {
        while (listeningAndSendingModeOn) {
            ClientInput.UserParseCommand userParseCommand = this.clientInput.getParsedCommand();
            try {
                Request request = requestValidator.validateRequest(userParseCommand);
                handleRegularCommand(request);
            } catch (RequestValidator.NotEnoughArgumentException e) {
                sender.printMessage(new Message(Category.WARNING, "Слишком много аргументов"));
                continue;
            } catch (RequestValidator.NullCommandException e) {
                sender.printMessage(new Message(Category.ERROR, "Нулевая команда"));
                continue;
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
    }

    private void printResponse(Response response) {
        if (response.success()) {
            sender.printMessage(new Message(Category.SUCCESS, response.result()));
        } else {
            sender.printMessage(new Message(Category.WARNING, response.errorMessage()));
        }
    }

    private void handleNullCommand() { // TODO never used. используй лучше функ интерфейс "() -> {}"
        // Ничего не делать, просто продолжить ожидание команды
    }

    private void handleExecuteScriptCommand() { // TODO never used
        sender.printMessage(new Message(Category.INFO, "Команда выполнения скрипта еще не реализована"));
    }

    private void handleRegularCommand(Request command) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            byte[] commandBytes = baos.toByteArray();

            // Отправка массива байтов на сервер
            this.handlerClient.sendData(commandBytes);
        } catch (IOException e) {
            e.printStackTrace();
            sender.printMessage(new Message(Category.ERROR, "Не удалось отправить команду на сервер"));
        }
    }
}
