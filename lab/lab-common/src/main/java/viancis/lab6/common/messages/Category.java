package viancis.lab6.common.messages;

public enum Category {
    ERROR("\u001B[31m"),
    INFO("\u001B[34m"),
    WARNING("\u001B[33m"),
    INPUT("\u001B[95m"),
    SUCCESS("\u001B[32m");

    private final String colorCode;

    Category(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
