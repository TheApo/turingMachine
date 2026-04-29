package com.apogames.logic.game.tutorial;

public enum TutorialStep {
    INTRO_CODE(1),
    SIMPLE_HINT(2),
    HARD_HINT(3),
    RIGHT_UI(4),
    ROUND_FLOW(5),
    DONE(6);

    private final int value;

    TutorialStep(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public TutorialStep next() {
        for (TutorialStep step : values()) {
            if (step.value == this.value + 1) {
                return step;
            }
        }
        return DONE;
    }

    public String titleKey() {
        return "tutorial_step" + value + "_title";
    }

    public String textKey() {
        return "tutorial_step" + value + "_text";
    }
}
