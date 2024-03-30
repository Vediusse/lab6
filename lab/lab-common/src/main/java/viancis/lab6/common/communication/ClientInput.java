package viancis.lab6.common.communication;

import viancis.lab6.common.models.MusicBand; // TODO useless import
import viancis.lab6.common.util.SplitterCommand;

import java.io.InputStream; 
import java.io.Serializable; // TODO useless import
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.PriorityQueue; // TODO useless import
import java.util.Scanner;

/**
 * Класс отвечающий за работу с пользователем в интерактивном режиме
 */
public class ClientInput {

    private final Scanner scanner;

    /**
     * Конструктор
     */
    public ClientInput(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    }

    public UserParseCommand getParsedCommand() {
        try { 
            SplitterCommand splitter = new SplitterCommand();
            System.out.print(">> ");
            String line = scanner.nextLine();
            String[] inputString = splitter.smartSplit(line).toArray(new String[0]);
            String commandName = inputString[0].toLowerCase();
            String[] commandArgs = Arrays.copyOfRange(inputString, 1, inputString.length);
            return new UserParseCommand(commandName,commandArgs);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public record UserParseCommand(String command, String[] commandArgs){

    }

}
