package com.smashprofs.game.Helper;

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
