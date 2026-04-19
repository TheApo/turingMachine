package com.apogames.logic.game.logic;

import com.apogames.logic.Constants;
import com.apogames.logic.backend.GameScreen;

public class IconDraw {

    public static void renderIcon(GameScreen screen, int changeX, int changeY, int width, int height, int icon) {
        if (icon == 1) {
            screen.getRenderer().setColor(Constants.COLOR_NUMBER_ONE_ICON[0], Constants.COLOR_NUMBER_ONE_ICON[1], Constants.COLOR_NUMBER_ONE_ICON[2], Constants.COLOR_NUMBER_ONE_ICON[3]);
            screen.getRenderer().triangle(changeX + width/2f, changeY, changeX, changeY + height, changeX + width, changeY + height);
        } else if (icon == 2) {
            screen.getRenderer().setColor(Constants.COLOR_NUMBER_TWO_ICON[0], Constants.COLOR_NUMBER_TWO_ICON[1], Constants.COLOR_NUMBER_TWO_ICON[2], Constants.COLOR_NUMBER_TWO_ICON[3]);
            screen.getRenderer().roundedRect(changeX, changeY, width, height, 5);
        } else if (icon == 3) {
            screen.getRenderer().setColor(Constants.COLOR_NUMBER_THREE_ICON[0], Constants.COLOR_NUMBER_THREE_ICON[1], Constants.COLOR_NUMBER_THREE_ICON[2], Constants.COLOR_NUMBER_THREE_ICON[3]);
            screen.getRenderer().circle(changeX + width/2f, changeY + height/2f, width/2f);
        }
    }

    public static void renderSolutionCheck(GameScreen screen, boolean isCorrect, int checkX, int checkY, int width, int height) {
        if (isCorrect) {
            screen.getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], 1f);
            screen.getRenderer().rect(checkX, checkY, width, height);
            screen.getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], Constants.COLOR_WHITE[3]);
            screen.getRenderer().rectLine(checkX + 4, checkY + width*2/3f, checkX + width/2f, checkY + width - 4, 3);
            screen.getRenderer().rectLine(checkX + width - 4, checkY + 4, checkX + width/2f, checkY + width - 4, 3);
        } else {
            screen.getRenderer().setColor(Constants.COLOR_RED[0], Constants.COLOR_RED[1], Constants.COLOR_RED[2], Constants.COLOR_RED[3]);
            screen.getRenderer().rect(checkX, checkY, width, height);
            screen.getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
            screen.getRenderer().rectLine(checkX + 4, checkY + 4, checkX + width - 4, checkY + width - 4, 3);
            screen.getRenderer().rectLine(checkX + width - 4, checkY + 4, checkX + 4, checkY + width - 4, 3);
        }
    }
}
