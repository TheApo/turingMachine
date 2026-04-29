package com.apogames.logic.game.tutorial;

import com.apogames.logic.backend.DrawString;

public class TutorialAnnotation {

    public final int fromX;
    public final int fromY;
    public final int toX;
    public final int toY;
    public final String label;
    public final int labelX;
    public final int labelY;
    public final DrawString labelAlign;

    public TutorialAnnotation(int fromX, int fromY, int toX, int toY,
                              String label, int labelX, int labelY, DrawString labelAlign) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.label = label;
        this.labelX = labelX;
        this.labelY = labelY;
        this.labelAlign = labelAlign;
    }
}
