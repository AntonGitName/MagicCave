package edu.amd.spbstu.magiccave.util;

/**
 * @author iAnton
 * @since 10/03/15
 */
public enum GameMode {
    RANDOM(0),
    SCENARIO(1);

    private final int value;

    GameMode(int value) {
        this.value = value;
    }
    public static GameMode fromValue(int value) {
        for (GameMode gameMode : values()) {
            if (gameMode.value == value) {
                return gameMode;
            }
        }
        return RANDOM;
    }
    public int getValue() {
        return value;
    }
}