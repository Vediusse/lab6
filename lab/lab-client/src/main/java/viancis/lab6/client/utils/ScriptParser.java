package viancis.lab6.client.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScriptParser {
    public static List<String> parseScript(InputStream inputStream) {
        List<String> commands = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("#")) {
                    commands.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("     └──Невозможно прочитать файл");
        }
        return commands;
    }

    private static String getUserHome() {
        return System.getProperty("user.home");
    }
}

