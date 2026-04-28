package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class OddEvenGeneralVerifier extends Verifier {

    public OddEvenGeneralVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(2);
    }

    public Verifier getCopy() {
        Verifier verifier = new OddEvenGeneralVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new OddEvenGeneralVerifier(newSolution);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        int countEven = 0;
        if (first % 2 == 0) {
            countEven += 1;
        }
        if (second % 2 == 0) {
            countEven += 1;
        }
        if (third % 2 == 0) {
            countEven += 1;
        }

        int countEvenSolution = 0;
        if (getSolution().getFirst() % 2 == 0) {
            countEvenSolution += 1;
        }
        if (getSolution().getSecond() % 2 == 0) {
            countEvenSolution += 1;
        }
        if (getSolution().getThird() % 2 == 0) {
            countEvenSolution += 1;
        }

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        this.setCheck("");
        if (countEven > 1) {
            if (countEvenSolution > 1) {
                this.setCheck(loc.getCommon().get("verifier_oddevengeneral_check_moreeven"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_oddevengeneral_check_moreodd"));
                this.removePossibilities(true, possibleSolutions, index);
            }
            return countEvenSolution > 1;
        }

        if (countEvenSolution <= 1) {
            this.setCheck(loc.getCommon().get("verifier_oddevengeneral_check_moreodd"));
        } else {
            this.setCheck(loc.getCommon().get("verifier_oddevengeneral_check_moreeven"));
            this.removePossibilities(false, possibleSolutions, index);
        }
        return countEvenSolution <= 1;
    }

    private void removePossibilities(boolean odd, ArrayList<Solution> possibleSolutions, int index) {
        if (possibleSolutions == null || possibleSolutions.isEmpty()) {
            return;
        }
        Solution solution = possibleSolutions.get(index);
        int countEven = 0;
        if (solution.getFirst() % 2 == 0) {
            countEven += 1;
        }
        if (solution.getSecond() % 2 == 0) {
            countEven += 1;
        }
        if (solution.getThird() % 2 == 0) {
            countEven += 1;
        }

        if ((odd && countEven > 1) || (!odd && countEven <= 1)) {
            possibleSolutions.remove(index);
        }

    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getName() {
        return "Die Anzahl der gerade Zahlen im Verhältnis zu den Ungeraden";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.OddEvenGeneralVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int evens = (first % 2 == 0 ? 1 : 0) + (second % 2 == 0 ? 1 : 0) + (third % 2 == 0 ? 1 : 0);
        return new int[]{ evens > 1 ? 0 : 1 };
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 1);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_oddevengeneral_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font15, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        String col1Text = loc.getCommon().get("verifier_oddevengeneral_col1_text");
        mainPanel.drawString(col1Text, changeX + 100, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        String col2Text = loc.getCommon().get("verifier_oddevengeneral_col2_text");
        mainPanel.drawString(col2Text, changeX + 300, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
    }
}
