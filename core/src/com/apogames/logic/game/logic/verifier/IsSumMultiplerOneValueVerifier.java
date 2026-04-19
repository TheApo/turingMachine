package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class IsSumMultiplerOneValueVerifier extends Verifier {

    private final int value;

    public IsSumMultiplerOneValueVerifier(Solution solution, int value) {
        super(solution, false, false, false);

        this.value = value;
        this.setColumn(2);
    }

    public int getValue() {
        return this.value;
    }

    public Verifier getCopy() {
        Verifier verifier = new IsSumMultiplerOneValueVerifier(super.getSolution(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new IsSumMultiplerOneValueVerifier(newSolution, this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        this.setCheck("");

        int sum = first + second + third;

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        if (sum % this.value == 0) {
            if (getSolution().getSum() % this.value == 0) {
                this.setCheck(loc.getCommon().get("verifier_issummultiplerone_check_ismultiple") + " " + this.value);
            } else {
                this.setCheck(loc.getCommon().get("verifier_issummultiplerone_check_isnotmultiple") + " " + this.value);
            }
            return getSolution().getSum() % this.value == 0;
        } else {
            if (getSolution().getSum() % this.value != 0) {
                this.setCheck(loc.getCommon().get("verifier_issummultiplerone_check_isnotmultiple") + " " + this.value);
            } else {
                this.setCheck(loc.getCommon().get("verifier_issummultiplerone_check_ismultiple") + " " + this.value);
            }
            return getSolution().getSum() % this.value != 0;
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
        return VerifyIDEnum.IsSumMultiplerOneValueVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 2; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 35 + 200 * i, changeY + 132, 20, 20, 1);
                IconDraw.renderIcon(mainPanel, changeX + 35 + 28 + 200 * i, changeY + 132, 20, 20, 2);
                IconDraw.renderIcon(mainPanel, changeX + 35 + 28 * 2 + 200 * i, changeY + 132, 20, 20, 3);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 1);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_issummultiplerone_title").replace("{0}", String.valueOf(this.value));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        mainPanel.drawString("= "+this.value+"x", changeX + 35 + 28 * 3, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String text1 = loc.getCommon().get("verifier_issummultiplerone_col2_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(text1, changeX + 100, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        mainPanel.drawString("!= "+this.value+"x", changeX + 35 + 28 * 3 + 200, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String text2 = loc.getCommon().get("verifier_issummultiplerone_col1_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(text2, changeX + 100 + 200, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 2; i++) {
            mainPanel.drawString("+", changeX + 55 + 28 * i, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("+", changeX + 55 + 28 * i + 200, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }
    }
}
