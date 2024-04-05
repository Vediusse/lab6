package viancis.lab6.client;

import viancis.lab6.client.utils.Config;
import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionForm {
    private final Sender sender;
    private final Scanner scanner;
    private Handler connectionHandlerClient;


    public ConnectionForm(Sender sender, Scanner scanner) {
        this.sender = sender;
        this.scanner = scanner;
    }

    public Handler getConnectionHandler() {
        sender.printMessage(new Message(Category.INFO, "Пожалуйста, введите адрес сервера в"
                + " локальной сети с которым вы хотите работать"));
        this.connectionHandlerClient = this.askAdress();
        sender.printMessage(new Message(Category.INFO, "Пожалуйста, введите порт сервера в"
                + " с которым вы хотите работать"));
        this.askPortAndSet();
        sender.printMessage(new Message(Category.SUCCESS, "Спасибо, большое ты правильно ввел  текст"));
        return connectionHandlerClient;
    }

    private Handler askAdress() {
        try {
            sender.printCommandPreview();
            String address = scanner.nextLine();
            Handler handler = new Handler(address);
            sender.printMessage(new Message(Category.SUCCESS, "Адрес в сети найден"));
            return handler;
        } catch (UnknownHostException e) {
            sender.printMessage(new Message(Category.ERROR, "Такого адреса не существует в сети, повторите ввод"));
            this.askAdress();
        } catch (SocketException e) {
            sender.printMessage(new Message(Category.ERROR, "Адрес в сети найден"));
            this.askAdress();
        } catch (NoSuchElementException e) {
            return null;
        }
        return null;
    }

    private void askPortAndSet() {
        try {
            sender.printCommandPreview();
            String inputedPort = scanner.nextLine();
            int port = Integer.parseInt(inputedPort);
            if (connectionHandlerClient != null) {
                if (port >= 1 && port <= Config.getMaxPortValue()) {
                    connectionHandlerClient.setServerPort(port);
                } else {
                    sender.printMessage(new Message(Category.ERROR, "Вы ввели неверный порт, повторите ввод"));
                    askPortAndSet();
                }
            } else {
                sender.printMessage(new Message(Category.ERROR, "Порт был занят"));
                askPortAndSet();
            }
        } catch (NoSuchElementException e) {
            connectionHandlerClient = null;
        } catch (NumberFormatException e) {
            sender.printMessage(new Message(Category.ERROR, "Вы ввели неверный порт, повторите ввод"));
            askPortAndSet();
        }
    }
}
