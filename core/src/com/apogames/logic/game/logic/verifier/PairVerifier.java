package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class PairVerifier extends Verifier {

    public PairVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(2);
    }

    public Verifier getCopy() {
        Verifier verifier = new PairVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new PairVerifier(newSolution);
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
        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        int pair = getPair(first, second, third);
        int pairSolution = getPair(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());
        if (pair == 1) {
            if (pair == pairSolution) {
                this.setCheck(loc.getCommon().get("verifier_pair_check_onepair"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_pair_check_nopair"));
            }
        } else {
            if (pair != pairSolution) {
                this.setCheck(loc.getCommon().get("verifier_pair_check_onepair"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_pair_check_nopair"));
            }
        }
        return pair == pairSolution;
    }

    private int getPair(int first, int second, int third) {
        int pair = 0;
        if (first == second) {
            pair += 1;
            if (first == third) {
                pair -= 1;
            }
        } else if (third == second) {
            pair += 1;
        } else if (first == third) {
            pair += 1;
        }
        return pair;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getName() {
        return "Es gib genau ein Paar";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.PairVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        return new int[]{ getPair(first, second, third) == 1 ? 1 : 0 };
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 1);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_pair_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        // Column 1: No pairs
        mainPanel.drawString(loc.getCommon().get("verifier_pair_col1_line1"), changeX + 100, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_pair_col1_line2"), changeX + 100, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        // Column 2: A pair
        mainPanel.drawString(loc.getCommon().get("verifier_pair_col2_line1"), changeX + 300, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_pair_col2_line2"), changeX + 300, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
