package viancis.lab6.common.models;

import viancis.lab6.common.messages.Category;
import viancis.lab6.common.messages.Message;
import viancis.lab6.common.messages.Sender;

import java.util.Scanner;

public class UserForm {
    private final Validator validator;
    private final Sender sender;

    public UserForm(Sender sender) {
        this.validator = new Validator();
        this.sender = sender;
    }

    public User register() {
        Scanner scanner = new Scanner(System.in);
        try {
            sender.printInLineMessage(new Message(Category.Input, "Введите имя пользователя: "));
            String username = getNonEmptyInput(scanner);
            validator.validateName(username, "Username");
            sender.printInLineMessage(new Message(Category.Input, "Введите пароль: "));
            String password = getNonEmptyInput(scanner);
            return new User(username, password);
        } catch (IllegalArgumentException e) {
            sender.printInLineMessage(new Message(Category.Input, "     └──Неверная запись поля\n"));
            this.register();
        }
        return null;
    }

    private String getNonEmptyInput(Scanner scanner) {
        String input = "";
        while (input.isEmpty()) {
            try {
                input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    sender.printInLineMessage(new Message(Category.Input, "Пожалуйста, введите непустое значение.\n"));
                    sender.printInLineMessage(new Message(Category.Input, "     "));
                }
            } catch (Exception e) {
                sender.printInLineMessage(new Message(Category.Input, "Ввод был прерван. Пожалуйста, попробуйте снова."));
                System.exit(400);
            }
        }
        return input;
    }
}
