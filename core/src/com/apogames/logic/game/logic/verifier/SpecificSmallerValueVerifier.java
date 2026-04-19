package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SpecificSmallerValueVerifier extends Verifier {

    private final int value;

    public SpecificSmallerValueVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird, int value) {
        super(solution, isFirst, isSecond, isThird);

        this.value = value;
        this.setColumn(3);
    }

    public int getValue() {
        return this.value;
    }

    public Verifier getCopy() {
        Verifier verifier = new SpecificSmallerValueVerifier(super.getSolution(), isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SpecificSmallerValueVerifier(newSolution, isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        this.setCheck("");

        boolean check = false;
        if (isFirst() && first < this.value) {
            check = getSolution().getFirst() < this.value;
        }
        if (isSecond() && second < this.value) {
            check = getSolution().getSecond() < this.value;
        }

        if (isThird() && third < this.value) {
            check = getSolution().getThird() < this.value;
        }

        String result = "";
        if (check) {
            if (first < this.value) {
                result += "first < "+this.value;
            } else {
                result += "first >= "+this.value;
            }
            if (second < this.value) {
                result += " " + loc.getCommon().get("common_or") + " second < "+this.value;
            } else {
                result += " " + loc.getCommon().get("common_or") + " second >= "+this.value;
            }
            if (third < this.value) {
                result += " " + loc.getCommon().get("common_or") + " third < "+this.value;
            } else {
                result += " " + loc.getCommon().get("common_or") + " third >= "+this.value;
            }
        } else {
            if (first < this.value) {
                result += "first >= "+this.value;
            } else {
                result += "first < "+this.value;
            }
            if (second < this.value) {
                result += " " + loc.getCommon().get("common_or") + " second >= "+this.value;
            } else {
                result += " " + loc.getCommon().get("common_or") + " second < "+this.value;
            }
            if (third < this.value) {
                result += " " + loc.getCommon().get("common_or") + " third >= "+this.value;
            } else {
                result += " " + loc.getCommon().get("common_or") + " third < "+this.value;
            }
        }

        this.setCheck(result);
        return check;
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Ist eine Zahl kleiner "+this.value;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SpecificSmallerValueVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 132, 30, 30, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 132, 30, 30, 2);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 132, 30, 30, 3);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_specificsmallervalue_title").replace("{0}", String.valueOf(this.value));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        mainPanel.drawString("< "+this.value, changeX + 50, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        String col1Text = loc.getCommon().get("verifier_specificsmallervalue_col1_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col1Text, changeX + 62, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString("< "+this.value, changeX + 50 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        String col2Text = loc.getCommon().get("verifier_specificsmallervalue_col2_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col2Text, changeX + 62 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString("< "+this.value, changeX + 50 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        String col3Text = loc.getCommon().get("verifier_specificsmallervalue_col3_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col3Text, changeX + 62 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
