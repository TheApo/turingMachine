package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class PreciseEvenNumberVerifier extends Verifier {

    public PreciseEvenNumberVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(4);
    }

    public Verifier getCopy() {
        Verifier verifier = new PreciseEvenNumberVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new PreciseEvenNumberVerifier(newSolution);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        int countEven = countAmount(first, second, third);
        int countEvenSolution = countAmount(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        this.setCheck("");
        if (countEven == countEvenSolution) {
            this.setCheck(this.getTranslationWithValue(countEven) + " " + loc.getCommon().get("verifier_preciseevennumber_check_evennumbers"));
        } else {
            this.setCheck(loc.getCommon().get("common_not") + " " + this.getTranslationWithValue(countEven) + " " + loc.getCommon().get("verifier_preciseevennumber_check_evennumbers"));
        }
        return countEven == countEvenSolution;
    }

    private int countAmount(int first, int second, int third) {
        int result = 0;
        if (first % 2 == 0) {
            result += 1;
        }
        if (second % 2 == 0) {
            result += 1;
        }
        if (third % 2 == 0) {
            result += 1;
        }
        return result;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getName() {
        return "Die Anzahl der gerade Zahlen im Verhältnis zu den Ungeraden";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.PreciseEvenNumberVerifier.getValue();
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 3);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_preciseevennumber_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font15, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        int columnWidth = 100;
        int[] columnOffsets = {50, 150, 250, 350};
        String[] columnKeys = {"verifier_preciseevennumber_col1_", "verifier_preciseevennumber_col2_", "verifier_preciseevennumber_col3_", "verifier_preciseevennumber_col4_"};

        for (int col = 0; col < 4; col++) {
            int x = changeX + columnOffsets[col];
            String prefix = columnKeys[col];

            for (int line = 1; line <= 3; line++) {
                String text = loc.getCommon().get(prefix + "line" + line);

                int y;
                if (line == 3) {
                    y = changeY + 169;
                } else {
                    y = changeY + 127 + (line - 1) * 18;
                }

                com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
                if (line == 3) font = AssetLoader.font15;

                mainPanel.drawString(text, x, y, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
            }
        }
    }
}
