package com.apogames.logic.game.logic.level;

public enum Difficulty {
    EASY (1),
    MEDIUM(2),
    EXPERT(3),
    INSANE(4);

    private int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Difficulty next(int add) {
        for (Difficulty difficulty : values()) {
            if (difficulty.getValue() == this.value + add) {
                return difficulty;
            }
        }
        return add > 0 ? EASY : INSANE;
    }
}
