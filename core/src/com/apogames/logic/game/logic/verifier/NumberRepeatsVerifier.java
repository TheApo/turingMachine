package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class NumberRepeatsVerifier extends Verifier {

    public NumberRepeatsVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new NumberRepeatsVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new NumberRepeatsVerifier(newSolution);
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
        int repeat = getRepeat(first, second, third);
        int repeatSolution = getRepeat(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        if (repeat == repeatSolution) {
            if (repeat == 0) {
                this.setCheck(loc.getCommon().get("verifier_numberrepeats_col1_line1"));
            } else if (repeat == 1) {
                this.setCheck(loc.getCommon().get("verifier_numberrepeats_check_double"));
            } else if (repeat == 3) {
                this.setCheck(loc.getCommon().get("verifier_numberrepeats_check_triple"));
            }
        } else {
            if (repeat == 0) {
                this.setCheck(loc.getCommon().get("verifier_numberrepeats_check_doubleortriple"));
            } else if (repeat == 1) {
                this.setCheck(loc.getCommon().get("verifier_numberrepeats_check_notdouble"));
            } else if (repeat == 3) {
                this.setCheck(loc.getCommon().get("verifier_numberrepeats_check_nottriple"));
            }
        }
        return repeat == repeatSolution;
    }

    private int getRepeat(int first, int second, int third) {
        int repeat = 0;
        if (first == second) {
            repeat += 1;
        }
        if (first == third) {
            repeat += 1;
        }
        if (third == second) {
            repeat += 1;
        }
        return repeat;
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Wiederholt sich eine Zahl genau so oft";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.NumberRepeatsVerifier.getValue();
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_numberrepeats_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        int columnWidth = 133;
        int[] columnOffsets = {62, 62 + 133, 62 + 133 * 2};
        String[] columnKeys = {"verifier_numberrepeats_col1_", "verifier_numberrepeats_col2_", "verifier_numberrepeats_col3_"};

        for (int col = 0; col < 3; col++) {
            int x = changeX + columnOffsets[col];
            String prefix = columnKeys[col];
            int maxLines = (col == 0) ? 2 : 3;

            for (int line = 1; line <= maxLines; line++) {
                String text = loc.getCommon().get(prefix + "line" + line);

                int y;
                if (line == maxLines) {
                    // Letzte Zeile (Zahlenbeispiel) - immer auf gleicher Höhe
                    y = changeY + 169;
                } else {
                    // Textzeilen
                    y = changeY + 125 + (line - 1) * 22;
                }

                com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
                if (line == maxLines) font = AssetLoader.font20;

                mainPanel.drawString(text, x, y, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
            }
        }
    }
}
