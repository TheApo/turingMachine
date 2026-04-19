package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SmallerValueVerifier extends Verifier {

    private final int value;

    public SmallerValueVerifier(Solution solution, int value) {
        super(solution, false, false, false);

        this.value = value;
        this.setColumn(2);
    }

    public int getValue() {
        return this.value;
    }

    public Verifier getCopy() {
        Verifier verifier = new SmallerValueVerifier(super.getSolution(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SmallerValueVerifier(newSolution, this.value);
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

        if (first < this.value || second < this.value || third < this.value) {
            if (getSolution().getFirst() < this.value || getSolution().getSecond() < this.value || getSolution().getThird() < this.value) {
                this.setCheck(loc.getCommon().get("verifier_smallervalue_check_oneissmaller") + " " + this.value);
            } else {
                this.setCheck(loc.getCommon().get("verifier_smallervalue_check_noneissmaller") + " " + this.value);
            }
            return getSolution().getFirst() < this.value || getSolution().getSecond() < this.value || getSolution().getThird() < this.value;
        } else {
            if (getSolution().getFirst() >= this.value && getSolution().getSecond() >= this.value && getSolution().getThird() >= this.value) {
                this.setCheck(loc.getCommon().get("verifier_smallervalue_check_noneissmaller") + " " + this.value);
            } else {
                this.setCheck(loc.getCommon().get("verifier_smallervalue_check_oneissmaller") + " " + this.value);
            }
            return getSolution().getFirst() >= this.value && getSolution().getSecond() >= this.value && getSolution().getThird() >= this.value;
        }
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
        return VerifyIDEnum.SmallerValueVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 2; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 15 + 200 * i, changeY + 132, 20, 20, 1);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 40 + 200 * i, changeY + 132, 20, 20, 2);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 40 * 2 + 200 * i, changeY + 132, 20, 20, 3);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 1);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_smallervalue_title").replace("{0}", String.valueOf(this.value));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        // Column 1: icons with < value
        mainPanel.drawString(", ", changeX + 37, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        mainPanel.drawString(", ", changeX + 77, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        mainPanel.drawString("< "+this.value, changeX + 117, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);

        String col1Text = loc.getCommon().get("verifier_smallervalue_col1_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col1Text, changeX + 100, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        // Column 2: icons with >= value
        mainPanel.drawString(", ", changeX + 37 + 200, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        mainPanel.drawString(", ", changeX + 77 + 200, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        mainPanel.drawString(">= "+this.value, changeX + 117 + 200, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);

        String col2Text = loc.getCommon().get("verifier_smallervalue_col2_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col2Text, changeX + 100 + 200, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
