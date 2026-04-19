package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.CheckHelp;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

public abstract class Verifier {

    private final String REPLACEMENT_STRING = "    ";

    private final float extraInformationWidth = (Constants.GAME_WIDTH - 805) / 2f - 15;

    private String verifier = "A";
    private final boolean isFirst;
    private final boolean isSecond;
    private final boolean isThird;

    private int column = 2;

    private final Solution solution;

    private String check = "";

    public Verifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        this.solution = solution;

        this.isFirst = isFirst;
        this.isSecond = isSecond;
        this.isThird = isThird;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getTranslation() {
        String number = "first";
        if (this.isSecond) {
            number = "second";
        } else if (this.isThird()) {
            number = "third";
        }
        return number;
    }

    public String getTranslationWithValue(int value) {
        Localization loc = Localization.getInstance();
        String number = loc.getCommon().get("common_number_one");
        if (value == 2) {
            number = loc.getCommon().get("common_number_two");
        } else if (value == 3) {
            number = loc.getCommon().get("common_number_three");
        } else if (value == 0) {
            number = loc.getCommon().get("common_number_zero");
        }
        return number;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getValue() {
        return 0;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public Solution getSolution() {
        return solution;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isSecond() {
        return isSecond;
    }

    public boolean isThird() {
        return isThird;
    }

    public abstract boolean check(int first, int second, int third);

    public abstract boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index);

    public abstract String getText();

    public abstract String getName();

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {

    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY) {
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[0], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[1], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[2], 1f);
        mainPanel.getRenderer().roundedRect(changeX, changeY + 100, 395, 95, 10);
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], 1f);
        mainPanel.getRenderer().roundedRect(changeX, changeY, 395, 120, 10);

        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[0], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[1], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[2], 1f);
        for (int i = 0; i < this.getDifficulty().getValue(); i++) {
            mainPanel.getRenderer().circle(changeX + 35 + i * 10, changeY + 10, 4);
        }
    }

    public void renderFillSeparator(MainPanel mainPanel, int changeX, int changeY, int count) {
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], Constants.COLOR_VERIFIER_BACKGROUND[3]);
        if (this.getColumn() != count + 1) {
            this.setColumn(count + 1);
        }
        if (count == 1 || count == 3) {
            mainPanel.getRenderer().rect(changeX + 200, changeY + 125, 2, 70);
        } else if (count == 2) {
            mainPanel.getRenderer().rect(changeX + 133, changeY + 125, 2, 70);
            mainPanel.getRenderer().rect(changeX + 266, changeY + 125, 2, 70);
        }
        if (count == 3) {
            mainPanel.getRenderer().rect(changeX + 100, changeY + 125, 2, 70);
            mainPanel.getRenderer().rect(changeX + 300, changeY + 125, 2, 70);
        }
    }

    public void renderFillSeparatorHorizontal(MainPanel mainPanel, int changeX, int changeY, int count) {
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], Constants.COLOR_VERIFIER_BACKGROUND[3]);
        if (count == 1) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 400 - 20, 2);
        } else if (count == 2) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 200 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 200, changeY + 160, 200 - 20, 2);
        } else if (count == 3) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 133 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 133, changeY + 160, 133 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 133 * 2, changeY + 160, 133 - 20, 2);
        } else if (count == 4) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 100 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 100, changeY + 160, 100 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 100 * 2, changeY + 160, 100 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 100 * 3, changeY + 160, 100 - 20, 2);
        }
    }

    public abstract Verifier getCopy();

    public abstract Verifier getCopyWithSolution(Solution newSolution);

    public void renderFillExtraInformation(MainPanel mainPanel, int changeX, int changeY, String check) {
        if (check.contains("first") || check.contains("second") || check.contains("third")) {
            int startX;
            BitmapFont font15 = AssetLoader.font15;
            int width = 17;
            String replacementString = REPLACEMENT_STRING;
            mainPanel.getGlyphLayout().setText(font15, check);
            if (mainPanel.getGlyphLayout().width > extraInformationWidth) {
                font15 = AssetLoader.font12;
                width = 10;
            }

            String replaceCheck = check;
            int indexFirst = replaceCheck.indexOf("first");
            int indexSecond = replaceCheck.indexOf("second");
            int indexThird = replaceCheck.indexOf("third");
            while (indexFirst >= 0 || indexSecond >= 0 || indexThird >= 0) {
                if (indexFirst >= 0 && (indexFirst < indexSecond || indexSecond < 0) && (indexFirst < indexThird || indexThird < 0)) {
                    mainPanel.getGlyphLayout().setText(font15, replaceCheck.substring(0, indexFirst));
                    startX = (int)(mainPanel.getGlyphLayout().width);
                    replaceCheck = replaceCheck.replaceFirst("first", replacementString);

                    IconDraw.renderIcon(mainPanel, changeX + startX, changeY, width, width, 1);
                } else if (indexSecond >= 0 && (indexSecond < indexFirst || indexFirst < 0) && (indexSecond < indexThird || indexThird < 0)) {
                    mainPanel.getGlyphLayout().setText(font15, replaceCheck.substring(0, indexSecond));
                    startX = (int)(mainPanel.getGlyphLayout().width);
                    replaceCheck = replaceCheck.replaceFirst("second", replacementString);

                    IconDraw.renderIcon(mainPanel, changeX + startX, changeY, width, width, 2);
                } else if (indexThird >= 0 && (indexThird < indexFirst || indexFirst < 0) && (indexThird < indexSecond || indexSecond < 0)) {
                    mainPanel.getGlyphLayout().setText(font15, replaceCheck.substring(0, indexThird));
                    startX = (int)(mainPanel.getGlyphLayout().width);
                    replaceCheck = replaceCheck.replaceFirst("third", replacementString);

                    IconDraw.renderIcon(mainPanel, changeX + startX, changeY, width, width, 3);
                }
                indexFirst = replaceCheck.indexOf("first");
                indexSecond = replaceCheck.indexOf("second");
                indexThird = replaceCheck.indexOf("third");
            }
        }
    }

    public void renderAllText(MainPanel mainPanel, int changeX, int changeY) {
        mainPanel.drawString(""+this.verifier, changeX + 5, changeY + 5, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);
        mainPanel.drawString(Localization.getInstance().getCommon().get("verifier_verifies"), changeX + 100, changeY + 10, Constants.COLOR_ORANGE, AssetLoader.font20, DrawString.BEGIN, false, false);

        this.renderText(mainPanel, changeX, changeY);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        this.renderTextBig(mainPanel, changeX, changeY);
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    }

    public void renderTextExtraInformation(MainPanel mainPanel, int changeX, int changeY, String check) {
        String replaceCheck = check;
        mainPanel.getGlyphLayout().setText(AssetLoader.font15, replaceCheck);
        if (check.contains("first") || check.contains("second") || check.contains("third")) {
            replaceCheck = check.replace("first", REPLACEMENT_STRING);
            replaceCheck = replaceCheck.replace("second", REPLACEMENT_STRING);
            replaceCheck = replaceCheck.replace("third", REPLACEMENT_STRING);
        }

        if (mainPanel.getGlyphLayout().width >= extraInformationWidth) {
            mainPanel.drawString(replaceCheck, changeX, changeY, Constants.COLOR_BUTTONS_DARK, AssetLoader.font12, DrawString.BEGIN, false, false);
        } else {
            mainPanel.drawString(replaceCheck, changeX, changeY, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }
    }

    public abstract int getId();

    public Difficulty getDifficulty() {
        return Difficulty.EASY;
    }

    public String[] getString(String value) {
        String result = value;
        result = result.replace("this.value", String.valueOf(this.getValue()));
        return result.split(";");
    }
}
