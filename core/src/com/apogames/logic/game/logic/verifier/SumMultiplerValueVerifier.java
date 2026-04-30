package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SumMultiplerValueVerifier extends Verifier {

    private final int value;

    public SumMultiplerValueVerifier(Solution solution, int value) {
        super(solution, false, false, false);

        this.value = value;
        this.setColumn(3);
    }

    public int getValue() {
        return this.value;
    }

    public Verifier getCopy() {
        Verifier verifier = new SumMultiplerValueVerifier(super.getSolution(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SumMultiplerValueVerifier(newSolution, this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        list.add(new SumMultiplerValueVerifier(sol, 3));
        list.add(new SumMultiplerValueVerifier(sol, 4));
        list.add(new SumMultiplerValueVerifier(sol, 5));
        return list;
    }

    @Override
    public int[] getConfigCells() {
        // value=3 → cell 0, value=4 → cell 1, value=5 → cell 2
        if (this.value == 3) return new int[]{0};
        if (this.value == 4) return new int[]{1};
        return new int[]{2};
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();
        this.setCheck("");

        int sum = first + second + third;
        boolean check = sum % this.value == 0 && getSolution().getSum() % this.value == 0;

        int[] divisors = {3, 4, 5};
        boolean[] hits = {sum % 3 == 0, sum % 4 == 0, sum % 5 == 0};
        String multipleOf = loc.getCommon().get("verifier_summultiplervalue_check_isamultipleof");
        String notWord = loc.getCommon().get("common_not");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < divisors.length; i++) {
            if (!hits[i]) continue;
            if (sb.length() > 0) sb.append(hitSeparator());
            sb.append(sum).append(" ");
            if (!check) sb.append(notWord).append(" ");
            sb.append(multipleOf).append(" ").append(divisors[i]);
        }
        this.setCheck(sb.toString());

        return check;
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Ist eine Zahl kleiner "+this.value;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SumMultiplerValueVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int s = first + second + third;
        return hitsOf(s % 3 == 0, s % 4 == 0, s % 5 == 0);
    }

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 3; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 5 + 133 * i, changeY + 132, 20, 20, 1);
                IconDraw.renderIcon(mainPanel, changeX + 5 + 28 + 133 * i, changeY + 132, 20, 20, 2);
                IconDraw.renderIcon(mainPanel, changeX + 5 + 28 * 2 + 133 * i, changeY + 132, 20, 20, 3);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_summultiplervalue_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        String col1Line1 = loc.getCommon().get("verifier_summultiplervalue_col1_line1");
        mainPanel.drawString(col1Line1, changeX + 5 + 28 * 3, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col1Line2 = loc.getCommon().get("verifier_summultiplervalue_col1_line2");
        mainPanel.drawString(col1Line2, changeX + 67, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        String col2Line1 = loc.getCommon().get("verifier_summultiplervalue_col2_line1");
        mainPanel.drawString(col2Line1, changeX + 5 + 28 * 3 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col2Line2 = loc.getCommon().get("verifier_summultiplervalue_col2_line2");
        mainPanel.drawString(col2Line2, changeX + 67 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        String col3Line1 = loc.getCommon().get("verifier_summultiplervalue_col3_line1");
        mainPanel.drawString(col3Line1, changeX + 5 + 28 * 3 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        String col3Line2 = loc.getCommon().get("verifier_summultiplervalue_col3_line2");
        mainPanel.drawString(col3Line2, changeX + 67 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font10, DrawString.MIDDLE, false, false);

        for (int i = 0; i < 2; i++) {
            mainPanel.drawString("+", changeX + 25 + 28 * i, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("+", changeX + 25 + 28 * i + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("+", changeX + 25 + 28 * i + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }
    }
}
