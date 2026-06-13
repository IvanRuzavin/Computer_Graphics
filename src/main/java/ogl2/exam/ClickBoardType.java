package ogl2.exam;

public enum ClickBoardType {
    WIFI_CLICK("WiFi Click", ClickBoardSize.LONG, 0.05f, 0.25f, 0.90f),
    OLED_CLICK("OLED Click", ClickBoardSize.MEDIUM, 0.0f, 0.65f, 0.95f),
    RS232_CLICK("RS232 Click", ClickBoardSize.LONG, 0.65f, 0.65f, 0.68f),
    THUNDER_CLICK("Thunder Click", ClickBoardSize.SMALL, 1.0f, 0.82f, 0.02f),
    TEMP_CLICK("Temp/Humidity Click", ClickBoardSize.MEDIUM, 0.03f, 0.75f, 0.38f);

    private final String displayName;
    private final ClickBoardSize size;
    private final float red;
    private final float green;
    private final float blue;

    ClickBoardType(String displayName,
                   ClickBoardSize size,
                   float red,
                   float green,
                   float blue) {
        this.displayName = displayName;
        this.size = size;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ClickBoardSize getSize() {
        return size;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }
}

enum ClickBoardSize {
    LONG,
    MEDIUM,
    SMALL
}