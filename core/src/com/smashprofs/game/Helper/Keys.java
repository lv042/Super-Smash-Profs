package com.smashprofs.game.Helper;

/**
 * Available keys for modifying the .properties file.
 */
public enum Keys {
    GAMETIME("Gametime"),
    TIMESPLAYED("Times_played");

    private final String value;

    Keys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
