package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class DecreasingVerifier extends Verifier {

    public DecreasingVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new DecreasingVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new DecreasingVerifier(newSolution);
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

        int decreasing = getDecreasing(first, second, third);
        int decreasingSolution = getDecreasing(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        if (decreasing == 0) {
            if (decreasing == decreasingSolution) {
                this.setCheck(loc.getCommon().get("common_sequence_none"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_decreasing_check_2or3digit"));
            }
        } else if (decreasing == 1) {
            if (decreasing == decreasingSolution) {
                this.setCheck(loc.getCommon().get("common_sequence_2digit"));
            } else {
                this.setCheck(loc.getCommon().get("common_sequence_not2digit"));
            }
        } else if (decreasing == 2) {
            if (decreasing == decreasingSolution) {
                this.setCheck(loc.getCommon().get("common_sequence_3digit"));
            } else {
                this.setCheck(loc.getCommon().get("common_sequence_not3digit"));
            }
        }

        return decreasing == decreasingSolution;
    }

    private int getDecreasing(int first, int second, int third) {
        int decreasing = 0;
        if (first - 1 == second) {
            decreasing += 1;
            if (first - 2 == third) {
                decreasing += 1;
            }
        } else if (second - 1 == third) {
            decreasing += 1;
        }
        return decreasing;
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Gibt es absteigende Zahlen?";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.DecreasingVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        return new int[]{ getDecreasing(first, second, third) };
    }

    public Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_decreasing_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        // Column 1: No sequence
        mainPanel.drawString(loc.getCommon().get("verifier_decreasing_col1_line1"), changeX + 65, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_decreasing_col1_line2"), changeX + 65, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        // Column 2: 2-digit sequence
        mainPanel.drawString(loc.getCommon().get("verifier_decreasing_col2_line1"), changeX + 65 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_decreasing_col2_line2"), changeX + 65 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        // Column 3: 3-digit sequence
        mainPanel.drawString(loc.getCommon().get("verifier_decreasing_col3_line1"), changeX + 65 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_decreasing_col3_line2"), changeX + 65 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
