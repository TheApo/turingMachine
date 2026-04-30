package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SpecificDoubleSumEqualNumberVerifier extends Verifier {

    private final int value;

    public SpecificDoubleSumEqualNumberVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird, int value) {
        super(solution, isFirst, isSecond, isThird);

        this.value = value;
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new SpecificDoubleSumEqualNumberVerifier(super.getSolution(), isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SpecificDoubleSumEqualNumberVerifier(newSolution, isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        // Drei mögliche Paare: (first,second), (first,third), (second,third)
        list.add(new SpecificDoubleSumEqualNumberVerifier(sol, true, true, false, this.value));
        list.add(new SpecificDoubleSumEqualNumberVerifier(sol, true, false, true, this.value));
        list.add(new SpecificDoubleSumEqualNumberVerifier(sol, false, true, true, this.value));
        return list;
    }

    @Override
    public int[] getConfigCells() {
        // cell 0 = first+second, cell 1 = first+third, cell 2 = second+third
        if (isFirst() && isSecond()) return new int[]{0};
        if (isFirst() && isThird()) return new int[]{1};
        return new int[]{2};
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
        int sum = first + second;
        int sumResult = getSolution().getFirst() + getSolution().getSecond();
        if (isFirst()) {
            if (isThird()) {
                sum = first + third;
                sumResult = getSolution().getFirst() + getSolution().getThird();
            }
        } else {
            sum = second + third;
            sumResult = getSolution().getSecond() + getSolution().getThird();
        }

        boolean check = sum == this.value && sumResult == this.value;

        String[] pairLabels = {"first + second", "first + third", "second + third"};
        boolean[] hits = {
                first + second == this.value,
                first + third == this.value,
                second + third == this.value};
        String op = check ? "=" : "!=";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (!hits[i]) continue;
            if (sb.length() > 0) sb.append(hitSeparator());
            sb.append(pairLabels[i]).append(" ").append(op).append(" ").append(this.value);
        }
        this.setCheck(sb.toString());

        return check;
    }

    @Override
    public String getText() {
        return "The Verifier compares the sum of the two given numbers to "+this.value+".";
    }

    @Override
    public String getName() {
        return "Sum of 2 specific numbers equal to "+this.value;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SpecificDoubleSumEqualNumberVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        return hitsOf(first + second == this.value,
                      first + third == this.value,
                      second + third == this.value);
    }

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 132, 20, 20, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 132, 20, 20, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 132, 20, 20, 2);

            IconDraw.renderIcon(mainPanel, changeX + 15 + 30, changeY + 132, 20, 20, 2);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 + 30, changeY + 132, 20, 20, 3);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 30, changeY + 132, 20, 20, 3);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_specificdoublesumequalumber_title").replace("{0}", String.valueOf(this.value));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        mainPanel.drawString("= "+this.value, changeX + 70, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col1Text = loc.getCommon().get("verifier_specificdoublesumequalumber_col1_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col1Text, changeX + 65, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        mainPanel.drawString("= "+this.value, changeX + 70 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col2Text = loc.getCommon().get("verifier_specificdoublesumequalumber_col2_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col2Text, changeX + 65 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        mainPanel.drawString("= "+this.value, changeX + 70 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col3Text = loc.getCommon().get("verifier_specificdoublesumequalumber_col3_text").replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(col3Text, changeX + 65 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        mainPanel.drawString("+", changeX + 37, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString("+", changeX + 37 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString("+", changeX + 37 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
    }
}
