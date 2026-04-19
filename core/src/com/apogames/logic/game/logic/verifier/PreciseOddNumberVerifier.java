package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class PreciseOddNumberVerifier extends Verifier {

    public PreciseOddNumberVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(4);
    }

    public Verifier getCopy() {
        Verifier verifier = new PreciseOddNumberVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new PreciseOddNumberVerifier(newSolution);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        int countOdd = countAmount(first, second, third);
        int countOddSolution = countAmount(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        this.setCheck("");
        if (countOdd == countOddSolution) {
            this.setCheck(this.getTranslationWithValue(countOdd) + " " + loc.getCommon().get("verifier_preciseoddnumber_check_oddnumbers"));
        } else {
            this.setCheck(loc.getCommon().get("common_not") + " " + this.getTranslationWithValue(countOdd) + " " + loc.getCommon().get("verifier_preciseoddnumber_check_oddnumbers"));
        }
        return countOdd == countOddSolution;
    }

    private int countAmount(int first, int second, int third) {
        int result = 0;
        if (first % 2 != 0) {
            result += 1;
        }
        if (second % 2 != 0) {
            result += 1;
        }
        if (third % 2 != 0) {
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
        return "Die Anzahl der ungeraden Zahlen";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.PreciseOddNumberVerifier.getValue();
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 3);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_preciseoddnumber_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font15, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        // Render 4 columns
        int columnWidth = 100;
        int[] columnOffsets = {50, 150, 250, 350};
        String[] columnKeys = {"verifier_preciseoddnumber_col1_", "verifier_preciseoddnumber_col2_", "verifier_preciseoddnumber_col3_", "verifier_preciseoddnumber_col4_"};

        for (int col = 0; col < 4; col++) {
            int x = changeX + columnOffsets[col];
            String prefix = columnKeys[col];

            for (int line = 1; line <= 3; line++) {
                String text = loc.getCommon().get(prefix + "line" + line);
                int y = changeY + 127 + (line - 1) * 18;

                com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
                if (line == 3) font = AssetLoader.font15; // Example numbers

                mainPanel.drawString(text, x, y, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
            }
        }
    }
}
