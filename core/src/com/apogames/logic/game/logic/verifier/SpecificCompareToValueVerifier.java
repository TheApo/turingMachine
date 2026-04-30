package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SpecificCompareToValueVerifier extends Verifier {

    private int value;

    public SpecificCompareToValueVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird, int value) {
        super(solution, isFirst, isSecond, isThird);

        this.value = value;
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new SpecificCompareToValueVerifier(super.getSolution(), isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SpecificCompareToValueVerifier(newSolution, isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        list.add(new SpecificCompareToValueVerifier(sol, true, false, false, this.value));
        list.add(new SpecificCompareToValueVerifier(sol, false, true, false, this.value));
        list.add(new SpecificCompareToValueVerifier(sol, false, false, true, this.value));
        return list;
    }

    @Override
    public int[] getConfigCells() {
        // 3x3 grid: cell = row*3 + col. Konfig isFirst → erste Spalte (col=0): cells 0, 3, 6.
        if (isFirst()) return new int[]{0, 3, 6};
        if (isSecond()) return new int[]{1, 4, 7};
        return new int[]{2, 5, 8};
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
        boolean check = false;
        if (isFirst()) {
            if (first < this.value) {
                check = getSolution().getFirst() < this.value;
            } else if (first == this.value) {
                check = getSolution().getFirst() == this.value;
            } else {
                check = getSolution().getFirst() > this.value;
            }
        }
        if (isSecond()) {
            if (second < this.value) {
                check = getSolution().getSecond() < this.value;
            } else if (second == this.value) {
                check =  getSolution().getSecond() == this.value;
            } else {
                check = getSolution().getSecond() > this.value;
            }
        }
        if (isThird()) {
            if (third < this.value) {
                check =  getSolution().getThird() < this.value;
            } else if (third == this.value) {
                check = getSolution().getThird() == this.value;
            } else {
                check = getSolution().getThird() > this.value;
            }
        }

        String[] names = {"first", "second", "third"};
        int[] vals = {first, second, third};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            String op;
            if (check) {
                op = vals[i] < this.value ? "<" : (vals[i] == this.value ? "=" : ">");
            } else {
                op = vals[i] < this.value ? ">=" : (vals[i] == this.value ? "!=" : "<=");
            }
            if (sb.length() > 0) sb.append(hitSeparator());
            sb.append(names[i]).append(" ").append(op).append(" ").append(this.value);
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
        String number = "first";
        if (this.isSecond()) {
            number = "second";
        } else if (this.isThird()) {
            number = "third";
        }
        return number;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SpecificCompareToValueVerifier.getValue();
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public int[] getRowYOffsets() {
        return ROW_Y_OFFSETS;
    }

    @Override
    public int getCellHeight() {
        return 25;
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int[] arr = { first, second, third };
        int[] hits = new int[arr.length];
        for (int col = 0; col < arr.length; col++) {
            int row = cmpCol(arr[col], this.value);
            hits[col] = row * getColumn() + col;
        }
        return hits;
    }

    private static final int[] ROW_Y_OFFSETS = {122, 147, 172};

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 3; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 22, changeY + 122 + 25 * i, 20, 20, 1);
                IconDraw.renderIcon(mainPanel, changeX + 22 + 133, changeY + 122 + 25 * i, 20, 20, 2);
                IconDraw.renderIcon(mainPanel, changeX + 22 + 133 * 2, changeY + 122 + 25 * i, 20, 20, 3);
            }
            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_specificcomparetovalue_title").replace("{0}", String.valueOf(this.value));
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        for (int i = 0; i < 3; i++) {
            mainPanel.drawString("< " + this.value, changeX + 48 + i * 133, changeY + 127, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("= " + this.value, changeX + 48 + i * 133, changeY + 127 + 25, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
            mainPanel.drawString("> " + this.value, changeX + 48 + i * 133, changeY + 127 + 50, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }
    }
}
