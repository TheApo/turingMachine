package com.apogames.logic.game.logic;

import com.apogames.logic.game.logic.level.Difficulty;

public enum Step {
    PROPOSOL(1, "step_proposol"),
    QUESTION(2, "step_question"),
    END(3, "step_end");

    private final int value;
    private final String id;
    Step(int value, String id) {
        this.value = value;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Step next(int add) {
        for (Step step : values()) {
            if (step.getValue() == this.value + add) {
                return step;
            }
        }
        return PROPOSOL;
    }
}
