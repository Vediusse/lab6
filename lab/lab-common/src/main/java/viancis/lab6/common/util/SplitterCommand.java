package viancis.lab6.common.util;

import java.util.ArrayList;

public class SplitterCommand {
    public ArrayList<String> smartSplit(String line) {
        ArrayList<String> splittedLine = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        boolean inQuotes = false;

        for (char ch : line.toCharArray()) {
            if (ch == ' ' && !inQuotes) {
                addToSplitList(splittedLine, currentString);
            } else if (ch == '"') {
                inQuotes = !inQuotes;
            } else {
                currentString.append(ch);
            }
        }

        addToSplitList(splittedLine, currentString);
        return splittedLine;
    }

    private void addToSplitList(ArrayList<String> list, StringBuilder str) {
        list.add(str.toString());
        str.setLength(0);
    }
}
