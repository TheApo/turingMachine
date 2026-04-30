package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class DoubleAmountNumberVerifier extends Verifier {

    private final boolean chooseFirstValue;
    private final int value;
    private final int secondValue;

    private final int[] helpResults = new int[4];
    private final int[] helpSecondResults = new int[4];

    public DoubleAmountNumberVerifier(Solution solution, int value, int secondValue, boolean first) {
        super(solution, false, false, false);

        this.value = value;
        this.secondValue = secondValue;
        this.chooseFirstValue = first;
        this.setResult();
        this.setColumn(4);
    }

    public Verifier getCopy() {
        Verifier verifier = new DoubleAmountNumberVerifier(super.getSolution(), this.value, this.secondValue, this.chooseFirstValue);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new DoubleAmountNumberVerifier(newSolution, this.value, this.secondValue, this.chooseFirstValue);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        list.add(new DoubleAmountNumberVerifier(sol, this.value, this.secondValue, true));
        list.add(new DoubleAmountNumberVerifier(sol, this.value, this.secondValue, false));
        return list;
    }

    @Override
    public int[] getConfigCells() {
        // 2 rows × 4 cols. chooseFirstValue=true → row 0 (cells 0..3), false → row 1 (cells 4..7)
        if (this.chooseFirstValue) return new int[]{0, 1, 2, 3};
        return new int[]{4, 5, 6, 7};
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
        int amount = getAmountFor(this.value, first, second, third);
        int amountSolution = getAmountFor(this.value, getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        int amountSecond = getAmountFor(this.secondValue, first, second, third);
        int amountSolutionSecond = getAmountFor(this.secondValue, getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        boolean check = chooseFirstValue ? amount == amountSolution : amountSecond == amountSolutionSecond;

        String op = check ? "=" : "!=";
        StringBuilder sb = new StringBuilder();
        sb.append(this.value).append(" ").append(op).append(" ").append(amount).append("x");
        sb.append(hitSeparator());
        sb.append(this.secondValue).append(" ").append(op).append(" ").append(amountSecond).append("x");
        this.setCheck(sb.toString());

        return check;
    }

    private int getAmountFor(int value, int first, int second, int third) {
        int amount = 0;
        if (first == value) {
            amount += 1;
        }
        if (second == value) {
            amount += 1;
        }
        if (third == value) {
            amount += 1;
        }
        return amount;
    }

    public String getStringForValue(int value, com.apogames.logic.common.Localization loc) {
        if (value == 1) {
            return loc.getCommon().get("common_number_one");
        } else if (value == 2) {
            return loc.getCommon().get("common_number_two");
        } else if (value == 3) {
            return loc.getCommon().get("common_number_three");
        }
        return loc.getCommon().get("common_number_zero");
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Die Zahl "+this.value+" kommt genau so haeufig in der Loesung vor";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.DoubleAmountNumberVerifier.getValue();
    }

    @Override
    public int getRows() {
        return 2;
    }

    @Override
    public int[] getRowYOffsets() {
        return TWO_ROW_Y_OFFSETS;
    }

    @Override
    public int getCellHeight() {
        return 35;
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int cols = getColumn();
        int amountValue = getAmountFor(this.value, first, second, third);
        int amountSecond = getAmountFor(this.secondValue, first, second, third);
        return new int[] {
                0 * cols + amountValue,
                1 * cols + amountSecond
        };
    }

    private static final int[] TWO_ROW_Y_OFFSETS = {125, 160};

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 3);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_doubleamountnumber_title").replace("{0}", String.valueOf(this.value)).replace("{1}", String.valueOf(this.secondValue));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col1_line1").replace("{0}", String.valueOf(this.value)), changeX + 50, changeY + 125, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpResults[0], changeX + 50, changeY + 140, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col1_line2").replace("{0}", String.valueOf(this.secondValue)), changeX + 50, changeY + 165, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpSecondResults[0], changeX + 50, changeY + 180, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col2_line1").replace("{0}", String.valueOf(this.value)), changeX + 150, changeY + 125, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpResults[1], changeX + 150, changeY + 140, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col2_line2").replace("{0}", String.valueOf(this.secondValue)), changeX + 150, changeY + 165, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpSecondResults[1], changeX + 150, changeY + 180, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col3_line1").replace("{0}", String.valueOf(this.value)), changeX + 250, changeY + 125, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpResults[2], changeX + 250, changeY + 140, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col3_line2").replace("{0}", String.valueOf(this.secondValue)), changeX + 250, changeY + 165, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpSecondResults[2], changeX + 250, changeY + 180, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col4_line1").replace("{0}", String.valueOf(this.value)), changeX + 350, changeY + 125, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpResults[3], changeX + 350, changeY + 140, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_doubleamountnumber_col4_line2").replace("{0}", String.valueOf(this.secondValue)), changeX + 350, changeY + 165, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString("eg. " + this.helpSecondResults[3], changeX + 350, changeY + 180, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }

    private void setResult() {
        this.helpResults[0] = getResultValue(this.value, 0);
        this.helpResults[1] = getResultValue(this.value, 1);
        this.helpResults[2] = getResultValue(this.value, 2);
        this.helpResults[3] = getResultValue(this.value, 3);

        this.helpSecondResults[0] = getResultValue(this.secondValue, 0);
        this.helpSecondResults[1] = getResultValue(this.secondValue, 1);
        this.helpSecondResults[2] = getResultValue(this.secondValue, 2);
        this.helpSecondResults[3] = getResultValue(this.secondValue, 3);
    }

    private int getResultValue(int value, int howMuch) {
        StringBuilder result = new StringBuilder();
        while (result.length() < 3) {
            int number = (int)(Math.random() * 5) + 1;
            if (number != value) {
                result.append(number);
            }
        }

        if (howMuch > 0) {
            ArrayList<Integer> values = new ArrayList<>();
            values.add(0);
            values.add(1);
            values.add(2);
            for (int i = 0; i < howMuch; i++) {
                int index = values.remove((int)(Math.random() * values.size()));
                result.replace(index, index + 1, ""+value);
            }
        }

        return Integer.parseInt(result.toString());
    }
}
