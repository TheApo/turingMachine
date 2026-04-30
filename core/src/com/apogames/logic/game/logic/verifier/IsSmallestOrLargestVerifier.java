package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class IsSmallestOrLargestVerifier extends Verifier {

    private final boolean smallest;

    public IsSmallestOrLargestVerifier(Solution solution, boolean smallest) {
        super(solution, false, false, false);

        this.smallest = smallest;
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new IsSmallestOrLargestVerifier(super.getSolution(), this.smallest);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new IsSmallestOrLargestVerifier(newSolution, this.smallest);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        list.add(new IsSmallestOrLargestVerifier(sol, true));
        list.add(new IsSmallestOrLargestVerifier(sol, false));
        return list;
    }

    @Override
    public int[] getConfigCells() {
        // 2 rows × 3 cols. smallest=true → row 0 (cells 0,1,2), smallest=false → row 1 (cells 3,4,5)
        if (this.smallest) return new int[]{0, 1, 2};
        return new int[]{3, 4, 5};
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        this.setCheck("");

        boolean check = false;
        if (smallest) {
            if (first < second && first < third) {
                check = getSolution().getFirst() < getSolution().getSecond() && getSolution().getFirst() < getSolution().getThird();
            }
            if (second < first && second < third) {
                check = getSolution().getSecond() < getSolution().getFirst() && getSolution().getSecond() < getSolution().getThird();
            }
            if (third < first && third < second) {
                check = getSolution().getThird() < getSolution().getFirst() && getSolution().getThird() < getSolution().getSecond();
            }
        } else {
            if (first > second && first > third) {
                check = getSolution().getFirst() > getSolution().getSecond() && getSolution().getFirst() > getSolution().getThird();
            }
            if (second > first && second > third) {
                check = getSolution().getSecond() > getSolution().getFirst() && getSolution().getSecond() > getSolution().getThird();
            }
            if (third > first && third > second) {
                check = getSolution().getThird() > getSolution().getFirst() && getSolution().getThird() > getSolution().getSecond();
            }
        }

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();
        String[] names = {"first", "second", "third"};
        boolean[] smallestHits = {
                first < second && first < third,
                second < first && second < third,
                third < first && third < second};
        boolean[] biggestHits = {
                first > second && first > third,
                second > first && second > third,
                third > first && third > second};
        String smallestWord = loc.getCommon().get(check ? "common_issmallest" : "common_isnotsmallest");
        String biggestWord = loc.getCommon().get(check ? "common_isbiggest" : "common_isnotbiggest");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (smallestHits[i]) {
                if (sb.length() > 0) sb.append(hitSeparator());
                sb.append(names[i]).append(" ").append(smallestWord);
            }
            if (biggestHits[i]) {
                if (sb.length() > 0) sb.append(hitSeparator());
                sb.append(names[i]).append(" ").append(biggestWord);
            }
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
        return "Ist die kleinste oder größste Zahl";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.IsSmallestOrLargestVerifier.getValue();
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
        return hitsOf(
                first < second && first < third,
                second < first && second < third,
                third < first && third < second,
                first > second && first > third,
                second > first && second > third,
                third > first && third > second);
    }

    private static final int[] TWO_ROW_Y_OFFSETS = {125, 160};

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            for (int i = 0; i < 2; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 127 + 35 * i, 30, 30, 1);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 + 60, changeY + 122 + 40 * i, 15, 15, 1);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 60, changeY + 122 + 40 * i, 15, 15, 1);

                IconDraw.renderIcon(mainPanel, changeX + 15 + 60, changeY + 122 + 40 * i, 15, 15, 2);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 127 + 35 * i, 30, 30, 2);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 60, changeY + 140 + 40 * i, 15, 15, 2);

                IconDraw.renderIcon(mainPanel, changeX + 15 + 60, changeY + 140 + 40 * i, 15, 15, 3);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 + 60, changeY + 140 + 40 * i, 15, 15, 3);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 127 + 35 * i, 30, 30, 3);
            }

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_issmallestorlargest_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        for (int i = 0; i < 3; i++) {
            mainPanel.drawString("<", changeX + 60 + i * 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
            mainPanel.drawString(">", changeX + 60 + i * 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }
    }
}
