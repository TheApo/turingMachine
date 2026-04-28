package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SumIsEvenOrOddVerifier extends Verifier {

    public SumIsEvenOrOddVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(2);
    }

    public Verifier getCopy() {
        Verifier verifier = new SumIsEvenOrOddVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SumIsEvenOrOddVerifier(newSolution);
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
        int sumSolution = getSolution().getSum();
        if ((first + second + third) % 2 == 0) {
            if (sumSolution % 2 == 0) {
                this.setCheck(loc.getCommon().get("verifier_sumisevenorodd_check_sumiseven"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_sumisevenorodd_check_sumisodd"));
            }
            return sumSolution % 2 == 0;
        }
        if (sumSolution % 2 != 0) {
            this.setCheck(loc.getCommon().get("verifier_sumisevenorodd_check_sumisodd"));
        } else {
            this.setCheck(loc.getCommon().get("verifier_sumisevenorodd_check_sumiseven"));
        }
        return sumSolution % 2 != 0;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getName() {
        return "Summe is gerade oder ungerade";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SumIsEvenOrOddVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        return new int[]{ (first + second + third) % 2 == 0 ? 0 : 1 };
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 3; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 25 + i * 28, changeY + 132, 20, 20, i + 1);
                IconDraw.renderIcon(mainPanel, changeX + 225 + i * 28, changeY + 132, 20, 20, i + 1);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 1);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_sumisevenorodd_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        String col1Line1 = loc.getCommon().get("verifier_sumisevenorodd_col1_line1");
        mainPanel.drawString(col1Line1, changeX + 110, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col1Line2 = loc.getCommon().get("verifier_sumisevenorodd_col1_line2");
        mainPanel.drawString(col1Line2, changeX + 100, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        String col2Line1 = loc.getCommon().get("verifier_sumisevenorodd_col2_line1");
        mainPanel.drawString(col2Line1, changeX + 310, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.BEGIN, false, false);
        String col2Line2 = loc.getCommon().get("verifier_sumisevenorodd_col2_line2");
        mainPanel.drawString(col2Line2, changeX + 300, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 2; i++) {
            mainPanel.drawString("+", changeX + 46 + i * 28, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("+", changeX + 246 + i * 28, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }
    }
}
