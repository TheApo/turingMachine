package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SumMultiplerValueVerifier extends Verifier {

    private final int value;

    public SumMultiplerValueVerifier(Solution solution, int value) {
        super(solution, false, false, false);

        this.value = value;
        this.setColumn(3);
    }

    public int getValue() {
        return this.value;
    }

    public Verifier getCopy() {
        Verifier verifier = new SumMultiplerValueVerifier(super.getSolution(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SumMultiplerValueVerifier(newSolution, this.value);
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

        int sum = first + second + third;

        String s = "";
        if (sum % 3 == 0) {
            s += "3";
        }
        if (sum % 4 == 0) {
            if (s.length() != 0) {
                s += " " + loc.getCommon().get("common_and") + " ";
            }
            s += "4";
        }
        if (sum % 5 == 0) {
            if (s.length() != 0) {
                s += " " + loc.getCommon().get("common_and") + " ";
            }
            s += "5";
        }

        if (sum % this.value == 0) {
            if (getSolution().getSum() % this.value == 0) {
                this.setCheck(sum + " " + loc.getCommon().get("verifier_summultiplervalue_check_isamultipleof") + " " + s);
            } else {
                this.setCheck(sum + " " + loc.getCommon().get("verifier_summultiplervalue_check_isamultipleof") + " " + s + " " + loc.getCommon().get("verifier_summultiplervalue_check_butwrong"));
            }
        } else {
            this.setCheck(sum + " " + loc.getCommon().get("verifier_summultiplervalue_check_isamultipleof") + " " + s + " " + loc.getCommon().get("verifier_summultiplervalue_check_butwrong"));
        }
        return sum % this.value == 0 && getSolution().getSum() % this.value == 0;
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
        return VerifyIDEnum.SumMultiplerValueVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 3; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 5 + 133 * i, changeY + 132, 20, 20, 1);
                IconDraw.renderIcon(mainPanel, changeX + 5 + 28 + 133 * i, changeY + 132, 20, 20, 2);
                IconDraw.renderIcon(mainPanel, changeX + 5 + 28 * 2 + 133 * i, changeY + 132, 20, 20, 3);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_summultiplervalue_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        String col1Line1 = loc.getCommon().get("verifier_summultiplervalue_col1_line1");
        mainPanel.drawString(col1Line1, changeX + 5 + 28 * 3, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col1Line2 = loc.getCommon().get("verifier_summultiplervalue_col1_line2");
        mainPanel.drawString(col1Line2, changeX + 67, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        String col2Line1 = loc.getCommon().get("verifier_summultiplervalue_col2_line1");
        mainPanel.drawString(col2Line1, changeX + 5 + 28 * 3 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col2Line2 = loc.getCommon().get("verifier_summultiplervalue_col2_line2");
        mainPanel.drawString(col2Line2, changeX + 67 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        String col3Line1 = loc.getCommon().get("verifier_summultiplervalue_col3_line1");
        mainPanel.drawString(col3Line1, changeX + 5 + 28 * 3 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col3Line2 = loc.getCommon().get("verifier_summultiplervalue_col3_line2");
        mainPanel.drawString(col3Line2, changeX + 67 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 2; i++) {
            mainPanel.drawString("+", changeX + 25 + 28 * i, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("+", changeX + 25 + 28 * i + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("+", changeX + 25 + 28 * i + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }
    }
}
