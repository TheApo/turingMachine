package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class InAndDecreasingVerifier extends Verifier {

    public InAndDecreasingVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new InAndDecreasingVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new InAndDecreasingVerifier(newSolution);
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

        int increasing = getInAndDecreasing(first, second, third);
        int increasingSolution = getInAndDecreasing(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        if (increasing == 0) {
            if (increasing == increasingSolution) {
                this.setCheck(loc.getCommon().get("common_sequence_none"));
            } else {
                this.setCheck(loc.getCommon().get("common_sequence_2or3digit"));
            }
        } else if (increasing == 1) {
            if (increasing == increasingSolution) {
                this.setCheck(loc.getCommon().get("common_sequence_2digit"));
            } else {
                this.setCheck(loc.getCommon().get("common_sequence_not2digit"));
            }
        } else if (increasing == 2) {
            if (increasing == increasingSolution) {
                this.setCheck(loc.getCommon().get("common_sequence_3digit"));
            } else {
                this.setCheck(loc.getCommon().get("common_sequence_not3digit"));
            }
        }

        return increasing == increasingSolution;
    }

    private int getInAndDecreasing(int first, int second, int third) {
        int increasing = 0;
        if (first + 1 == second) {
            increasing += 1;
            if (first + 2 == third) {
                increasing += 1;
            }
            return increasing;
        } else if (second + 1 == third) {
            increasing += 1;
            return increasing;
        }
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
        return "Gibt es auf oder absteigende Zahlen?";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.InAndDecreasingVerifier.getValue();
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
        String title = Localization.getInstance().getCommon().get("verifier_inanddecreasing_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font15, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        mainPanel.drawString(loc.getCommon().get("verifier_inanddecreasing_col1_line1"), changeX + 65, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_inanddecreasing_col1_line2"), changeX + 65, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_inanddecreasing_col2_line1"), changeX + 65 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_inanddecreasing_col2_line2"), changeX + 65 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_inanddecreasing_col3_line1"), changeX + 65 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_inanddecreasing_col3_line2"), changeX + 65 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
