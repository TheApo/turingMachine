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

        String result = "";
        if (check) {
            if ((first < second && first < third) || (first > second && first > third)) {
                result += "first " + loc.getCommon().get("verifier_issmallestorlargest_check_correct");
            } else {
                result += "first " + loc.getCommon().get("verifier_issmallestorlargest_check_notcorrect");
            }
            if ((second < first && second < third) || (second > first && second > third)) {
                result += "second " + loc.getCommon().get("verifier_issmallestorlargest_check_correct");
            } else {
                result += "second " + loc.getCommon().get("verifier_issmallestorlargest_check_notcorrect");
            }
            if ((third < first && third < second) || (third > first && third > second)) {
                result += "third " + loc.getCommon().get("verifier_issmallestorlargest_check_correct");
            } else {
                result += "third " + loc.getCommon().get("verifier_issmallestorlargest_check_notcorrect");
            }
        }
        this.setCheck(result);
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
