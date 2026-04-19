package com.apogames.logic.game.logic.level;

public class CheckHelp {

    private final boolean check;
    private final int checkPart;

    public CheckHelp(boolean check, int checkPart) {
        this.check = check;
        this.checkPart = checkPart;
    }

    public boolean isCheck() {
        return check;
    }

    public int getCheckPart() {
        return checkPart;
    }
}
