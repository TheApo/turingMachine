package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SpecificOddEvenVerifier extends Verifier {


    public SpecificOddEvenVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        super(solution, isFirst, isSecond, isThird);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new SpecificOddEvenVerifier(super.getSolution(), isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SpecificOddEvenVerifier(newSolution, isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        list.add(new SpecificOddEvenVerifier(sol, true, false, false));
        list.add(new SpecificOddEvenVerifier(sol, false, true, false));
        list.add(new SpecificOddEvenVerifier(sol, false, false, true));
        return list;
    }

    @Override
    public int[] getConfigCells() {
        // 2 rows × 3 cols. Konfig isFirst → col 0: cells 0 und 3.
        if (isFirst()) return new int[]{0, 3};
        if (isSecond()) return new int[]{1, 4};
        return new int[]{2, 5};
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();
        this.setCheck("");

        boolean check = false;
        if (isFirst()) {
            check = first % 2 == getSolution().getFirst() % 2;
        } else if (isSecond()) {
            check = second % 2 == getSolution().getSecond() % 2;
        } else if (isThird()) {
            check = third % 2 == getSolution().getThird() % 2;
        }

        String[] names = {"first", "second", "third"};
        int[] vals = {first, second, third};
        String isWord = loc.getCommon().get("common_is");
        String even = loc.getCommon().get("common_even");
        String odd = loc.getCommon().get("common_odd");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            boolean isEven = vals[i] % 2 == 0;
            String parity;
            if (check) {
                parity = isEven ? even : odd;
            } else {
                parity = isEven ? odd : even;
            }
            if (sb.length() > 0) sb.append(hitSeparator());
            sb.append(names[i]).append(" ").append(isWord).append(" ").append(parity);
        }
        this.setCheck(sb.toString());

        return check;
    }

    private String getEvenOrOddString(int number, com.apogames.logic.common.Localization loc) {
        if (number % 2 == 0) {
            return loc.getCommon().get("common_even");
        }
        return loc.getCommon().get("common_odd");
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Ist eine bestimmte Zahl gerade oder ungerade";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SpecificOddEvenVerifier.getValue();
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
        int[] arr = { first, second, third };
        int cols = getColumn();
        int[] hits = new int[arr.length];
        for (int col = 0; col < arr.length; col++) {
            int row = arr[col] % 2 == 0 ? 0 : 1;
            hits[col] = row * cols + col;
        }
        return hits;
    }

    private static final int[] TWO_ROW_Y_OFFSETS = {125, 160};

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 2; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 125 + i * 40, 20, 20, 1);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 125 + i * 40, 20, 20, 2);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 125 + i * 40, 20, 20, 3);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 2);
            renderFillSeparatorHorizontal(mainPanel, changeX, changeY, 3);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_specificoddeven_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        String line1_col1 = loc.getCommon().get("verifier_specificoddeven_col1_line1");
        String line2_col1 = loc.getCommon().get("verifier_specificoddeven_col1_line2");
        mainPanel.drawString(line1_col1, changeX + 40, changeY + 130, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString(line2_col1, changeX + 40, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);

        String line1_col2 = loc.getCommon().get("verifier_specificoddeven_col2_line1");
        String line2_col2 = loc.getCommon().get("verifier_specificoddeven_col2_line2");
        mainPanel.drawString(line1_col2, changeX + 40 + 133, changeY + 130, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString(line2_col2, changeX + 40 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);

        String line1_col3 = loc.getCommon().get("verifier_specificoddeven_col3_line1");
        String line2_col3 = loc.getCommon().get("verifier_specificoddeven_col3_line2");
        mainPanel.drawString(line1_col3, changeX + 40 + 133 * 2, changeY + 130, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString(line2_col3, changeX + 40 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
    }
}
