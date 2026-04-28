package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class AmountNumberVerifier extends Verifier {

    private final int value;

    private final int[] helpResults = new int[4];

    public AmountNumberVerifier(Solution solution, int value) {
        super(solution, false, false, false);

        this.value = value;
        this.setResult();
        this.setColumn(4);
    }

    public Verifier getCopy() {
        Verifier verifier = new AmountNumberVerifier(super.getSolution(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new AmountNumberVerifier(newSolution, this.value);
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
        int amount = 0;
        if (first == this.value) {
            amount += 1;
        }
        if (second == this.value) {
            amount += 1;
        }
        if (third == this.value) {
            amount += 1;
        }

        int amountSolution = 0;
        if (getSolution().getFirst() == this.value) {
            amountSolution += 1;
        }
        if (getSolution().getSecond() == this.value) {
            amountSolution += 1;
        }
        if (getSolution().getThird() == this.value) {
            amountSolution += 1;
        }

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        if (amount == amountSolution) {
            this.setCheck(loc.getCommon().get("verifier_amountnumber_check_thereare") + " " + getStringForValue(amount, loc) + " " + this.value + "s " + loc.getCommon().get("verifier_amountnumber_check_incode"));
        } else {
            this.setCheck(loc.getCommon().get("verifier_amountnumber_check_thereareNOT") + " " + getStringForValue(amount, loc) + " " + this.value + "s " + loc.getCommon().get("verifier_amountnumber_check_incode"));
            removePossibilities(amount, possibleSolutions, index);
        }

        return amount == amountSolution;
    }

    private void removePossibilities(int amount, ArrayList<Solution> possibleSolutions, int index) {
        if (possibleSolutions == null || possibleSolutions.isEmpty()) {
            return;
        }
        Solution solution = possibleSolutions.get(index);
        int checkAmount = 0;
        if (solution.getFirst() == this.value) {
            checkAmount += 1;
        }
        if (solution.getSecond() == this.value) {
            checkAmount += 1;
        }
        if (solution.getThird() == this.value) {
            checkAmount += 1;
        }
        if (checkAmount == amount) {
            possibleSolutions.remove(index);
        }
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
        return VerifyIDEnum.AmountNumberVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int amount = (first == this.value ? 1 : 0) + (second == this.value ? 1 : 0) + (third == this.value ? 1 : 0);
        return new int[]{ amount };
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 3);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_amountnumber_title").replace("{0}", String.valueOf(this.value));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        mainPanel.drawString(loc.getCommon().get("verifier_amountnumber_col1_text").replace("{0}", String.valueOf(this.value)), changeX + 50, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString(this.helpResults[0]+"", changeX + 50, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        mainPanel.drawString(loc.getCommon().get("verifier_amountnumber_col2_text").replace("{0}", String.valueOf(this.value)), changeX + 150, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString(this.helpResults[1]+"", changeX + 150, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        mainPanel.drawString(loc.getCommon().get("verifier_amountnumber_col3_text").replace("{0}", String.valueOf(this.value)), changeX + 250, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString(this.helpResults[2]+"", changeX + 250, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        mainPanel.drawString(loc.getCommon().get("verifier_amountnumber_col4_text").replace("{0}", String.valueOf(this.value)), changeX + 350, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString(this.helpResults[3]+"", changeX + 350, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
    }

    private void setResult() {
        this.helpResults[0] = getResultValue(this.value, 0);
        this.helpResults[1] = getResultValue(this.value, 1);
        this.helpResults[2] = getResultValue(this.value, 2);
        this.helpResults[3] = getResultValue(this.value, 3);
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
