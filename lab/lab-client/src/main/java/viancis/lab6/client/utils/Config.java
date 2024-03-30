package viancis.lab6.client.utils;

// TODO lobmok usefull 
public final class Config {
    private static final String SYS_ENVIRONMENT = "HUMAN_INFO";
    private static final int MAX_PORT_VALUE = 65535;
    private static final int RESPONSE_TIMER = 5000; // TODO not used
    private static boolean isWorking = true;

    private Config() {

    }


    public static String getSystemEnvironment() {
        return SYS_ENVIRONMENT;
    }

    public static String getFilePath() {
        return System.getenv(getSystemEnvironment());
    }


    public static boolean isWorking() {
        return isWorking;
    }

    public static void setIsWorking(boolean isWorking) {
        Config.isWorking = isWorking;
    }

    public static int getMaxPortValue() {
        return MAX_PORT_VALUE;
    }
}
