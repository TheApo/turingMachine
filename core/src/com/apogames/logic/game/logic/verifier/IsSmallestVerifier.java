package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class IsSmallestVerifier extends Verifier {

    public IsSmallestVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new IsSmallestVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new IsSmallestVerifier(newSolution);
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

        if (first < second && first < third) {
            boolean firstSmallest = getSolution().getFirst() < getSolution().getSecond() && getSolution().getFirst() < getSolution().getThird();
            if (firstSmallest) {
                this.setCheck("first " + loc.getCommon().get("verifier_issmallest_check_issmallest"));
            } else {
                this.setCheck("first " + loc.getCommon().get("verifier_issmallest_check_isnotsmallest"));
                this.removePossibilities(true, false, false, possibleSolutions, index);
            }
            return firstSmallest;
        }
        if (second < first && second < third) {
            boolean secondSmallest = getSolution().getSecond() < getSolution().getFirst() && getSolution().getSecond() < getSolution().getThird();
            if (secondSmallest) {
                this.setCheck("second " + loc.getCommon().get("verifier_issmallest_check_issmallest"));
            } else {
                this.setCheck("second " + loc.getCommon().get("verifier_issmallest_check_isnotsmallest"));
                this.removePossibilities(false, true, false, possibleSolutions, index);
            }
            return secondSmallest;
        }
        if (third < first && third < second) {
            boolean thirdSmallest = getSolution().getThird() < getSolution().getFirst() && getSolution().getThird() < getSolution().getSecond();
            if (thirdSmallest) {
                this.setCheck("third " + loc.getCommon().get("verifier_issmallest_check_issmallest"));
            } else {
                this.setCheck("third " + loc.getCommon().get("verifier_issmallest_check_isnotsmallest"));
                this.removePossibilities(false, false, true, possibleSolutions, index);
            }
            return thirdSmallest;
        }
        return false;
    }

    private void removePossibilities(boolean first, boolean second, boolean third, ArrayList<Solution> possibleSolutions, int index) {
        if (possibleSolutions == null || possibleSolutions.isEmpty()) {
            return;
        }
        Solution solution = possibleSolutions.get(index);
        if (first) {
            if (solution.getFirst() < solution.getSecond() && solution.getFirst() < solution.getThird()) {
                possibleSolutions.remove(index);
            }
        } else if (second) {
            if (solution.getSecond() < solution.getFirst() && solution.getSecond() < solution.getThird()) {
                possibleSolutions.remove(index);
            }
        } else if (third) {
            if (solution.getThird() < solution.getFirst() && solution.getThird() < solution.getSecond()) {
                possibleSolutions.remove(index);
            }
        }
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Ist die kleinste Zahl auch die kleinste";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.IsSmallestVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        if (first < second && first < third) return new int[]{0};
        if (second < first && second < third) return new int[]{1};
        if (third < first && third < second) return new int[]{2};
        return new int[0];
    }

    public Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 132, 30, 30, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 + 60, changeY + 122, 20, 20, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 60, changeY + 122, 20, 20, 1);

            IconDraw.renderIcon(mainPanel, changeX + 15 + 60, changeY + 122, 20, 20, 2);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 132, 30, 30, 2);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 60, changeY + 145, 20, 20, 2);

            IconDraw.renderIcon(mainPanel, changeX + 15 + 60, changeY + 145, 20, 20, 3);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 + 60, changeY + 145, 20, 20, 3);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 132, 30, 30, 3);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_issmallest_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        int columnWidth = 133;
        String[] textKeys = {"verifier_issmallest_col1_text", "verifier_issmallest_col2_text", "verifier_issmallest_col3_text"};

        for (int col = 0; col < 3; col++) {
            int x = changeX + 62 + col * columnWidth;

            mainPanel.drawString("<", changeX + 60 + col * columnWidth, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

            String text = loc.getCommon().get(textKeys[col]);
            mainPanel.drawString(text, x, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        }
    }
}
