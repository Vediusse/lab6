package viancis.lab6.common.messages;


import java.io.PrintStream;

public class Sender {

    private static final String ANSI_RESET = "\u001B[0m";
    private static PrintStream printStream;


    public Sender(PrintStream printStream) {
        Sender.printStream = printStream;
    }



    public void printMessage(Message message) {
        printStream.println(message + ANSI_RESET);
    }

    public void printInLineMessage(Message message) {
        printStream.print(message + ANSI_RESET);
    }

    public void printCommandPreview() {
        printStream.print(">> ");
    }


}
