package com.main.view;

public enum FontSize {
    SMALL,
    MEDIUM,
    LARGE;

    public static String getCssPath(FontSize fontSize) {
        return switch (fontSize) {
            case SMALL -> "css/fontSmall.css";
            case MEDIUM -> "css/fontMedium.css";
            case LARGE -> "css/fontBig.css";
            default -> null;
        };
    }
}
