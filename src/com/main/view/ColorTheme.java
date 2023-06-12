package com.main.view;

public enum ColorTheme {
    LIGHT,
    DEFAULT,
    DARK;

    public static String getCssPath(ColorTheme colorTheme) {
        return switch (colorTheme) {
            case LIGHT -> "css/themeLight.css";
            case DEFAULT -> "css/themeDefault.css";
            case DARK -> "css/themeDark.css";
            default -> null;
        };
    }
}
