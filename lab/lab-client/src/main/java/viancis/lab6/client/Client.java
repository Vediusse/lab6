package viancis.lab6.client;

public final class Client {
    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated"); // TODO у тебя половина сообщений на ru-lang, половина на eng-lang
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.infoAndConnect();
    }
}
