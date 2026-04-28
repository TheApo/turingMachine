package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class AllSumNumberVerifier extends Verifier {

    private int value;

    private final int COUNT = 2;

    public AllSumNumberVerifier(Solution solution, int value) {
        super(solution, false, false, false);

        this.value = value;
        this.setColumn(COUNT + 1);
    }

    public Verifier getCopy() {
        Verifier verifier = new AllSumNumberVerifier(super.getSolution(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new AllSumNumberVerifier(newSolution, this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        this.setCheck("");
        int sum = first + second + third;
        int sumResult = getSolution().getFirst() + getSolution().getSecond() + getSolution().getThird();

        if (sum > this.value && sumResult > this.value) {
            this.setCheck("first + second + third > "+this.value);
            return true;
        } else if (sum < this.value && sumResult < this.value) {
            this.setCheck("first + second + third < "+this.value);
            return true;
        } else if (sum == this.value && sumResult == this.value) {
            this.setCheck("first + second + third = "+this.value);
            return true;
        }

        if (sum > this.value) {
            this.setCheck("first + second + third <= "+this.value);
            this.removePossibilities(this.value + 1, false, true, possibleSolutions, index);
        } else if (sum < this.value) {
            this.setCheck("first + second + third >= "+this.value);
            this.removePossibilities(this.value - 1, true, false, possibleSolutions, index);
        } else {
            this.setCheck("first + second + third " + com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_allsumnumber_check_isnot") + " " + this.value);
            this.removePossibilities(this.value, false, false, possibleSolutions, index);
        }

        return false;
    }

    private void removePossibilities(int value, boolean bigger, boolean smaller, ArrayList<Solution> possibleSolutions, int index) {
        if (possibleSolutions == null || possibleSolutions.isEmpty()) {
            return;
        }
        Solution solution = possibleSolutions.get(index);
        int sum = solution.getFirst() + solution.getSecond() + solution.getThird();
        if (bigger && !smaller) {
            if (sum <= value) {
                possibleSolutions.remove(index);
            }
        } else if (!bigger && smaller) {
            if (sum >= value) {
                possibleSolutions.remove(index);
            }
        } else {
            if (sum == value) {
                possibleSolutions.remove(index);
            }
        }
    }

    @Override
    public String getText() {
        return "The Verifier compares the sum of all numbers to "+this.value+". This sum can be less than, equal to, or greater than "+this.value;
    }


    @Override
    public String getName() {
        return "Die Summe aller Zahlen verglichen mit "+this.value;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.AllSumNumberVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        return new int[]{ cmpCol(first + second + third, this.value) };
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 3; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 10 + i * 28, changeY + 132, 20, 20, i + 1);
                IconDraw.renderIcon(mainPanel, changeX + 10 + 133 + i * 28, changeY + 132, 20, 20, i + 1);
                IconDraw.renderIcon(mainPanel, changeX + 10 + 133 * 2 + i * 28, changeY + 132, 20, 20, i + 1);
            }

            renderFillSeparator(mainPanel, changeX, changeY, COUNT);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_allsumnumber_title").replace("{0}", String.valueOf(this.value));

        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        // Render 3 columns
        int columnWidth = 133;
        String[] textKeys = {"verifier_allsumnumber_col1_text", "verifier_allsumnumber_col2_text", "verifier_allsumnumber_col3_text"};
        String[] symbols = {"<", "=", ">"};

        for (int col = 0; col < 3; col++) {
            int x = changeX + 66 + col * columnWidth;

            // Symbol: "< 7", "= 7", "> 7"
            mainPanel.drawString(symbols[col] + " " + this.value, x + 24, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);

            // Description text
            String text = loc.getCommon().get(textKeys[col]).replace("{0}", String.valueOf(this.value));
            com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
            mainPanel.drawString(text, x, changeY + 170, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
        }

        // Render "+" signs between icons
        for (int col = 0; col < 3; col++) {
            for (int i = 0; i < 2; i++) {
                mainPanel.drawString("+", changeX + 31 + col * columnWidth + i * 28, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            }
        }
    }
}
