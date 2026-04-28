package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class OddEvenValueVerifier extends Verifier {

    public OddEvenValueVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        super(solution, isFirst, isSecond, isThird);
        this.setColumn(2);
    }

    public Verifier getCopy() {
        Verifier verifier = new OddEvenValueVerifier(super.getSolution(), isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new OddEvenValueVerifier(newSolution, isFirst(), isSecond(), isThird());
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
        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        if (isFirst()) {
            if (first % 2 != 0) {
                boolean firstCheck = getSolution().getFirst() % 2 != 0;
                if (firstCheck) {
                    this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_isodd"));
                } else {
                    this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_iseven"));
                }
                return firstCheck;
            }
            boolean firstSameCheck = getSolution().getFirst() % 2 == 0;
            if (firstSameCheck) {
                this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_iseven"));
            } else {
                this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_isodd"));
            }
            return firstSameCheck;
        }
        if (isSecond()) {
            if (second % 2 != 0) {
                boolean secondCheck = getSolution().getSecond() % 2 != 0;
                if (secondCheck) {
                    this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_isodd"));
                } else {
                    this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_iseven"));
                }
                return secondCheck;
            }
            boolean secondSameCheck = getSolution().getSecond() % 2 == 0;
            if (secondSameCheck) {
                this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_iseven"));
            } else {
                this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_isodd"));
            }
            return secondSameCheck;
        }
        if (isThird()) {
            if (third % 2 != 0) {
                boolean thirdCheck = getSolution().getThird() % 2 != 0;
                if (thirdCheck) {
                    this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_isodd"));
                } else {
                    this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_iseven"));
                }
                return thirdCheck;
            }
            boolean thirdSameCheck = getSolution().getThird() % 2 == 0;
            if (thirdSameCheck) {
                this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_iseven"));
            } else {
                this.setCheck(getTranslation() + " " + loc.getCommon().get("verifier_oddevenvalue_check_isodd"));
            }
            return thirdSameCheck;
        }
        return false;
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
        return VerifyIDEnum.OddEvenValueVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int v = isFirst() ? first : isSecond() ? second : third;
        return new int[]{ v % 2 == 0 ? 0 : 1 };
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);
        }

        int icon = 1;
        if (this.isSecond()) {
            icon = 2;
        } else if (this.isThird()) {
            icon = 3;
        }

        // Calculate icon positions
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_oddevenvalue_title");
        int[] icons = {icon};
        VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, icons, 25);

        // Draw icons
        for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
            IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
        }

        if (all) {
            IconDraw.renderIcon(mainPanel, changeX + 20, changeY + 130, 30, 30, icon);
            IconDraw.renderIcon(mainPanel, changeX + 220, changeY + 130, 30, 30, icon);

            renderFillSeparator(mainPanel, changeX, changeY, 1);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        int icon = 1;
        if (this.isSecond()) {
            icon = 2;
        } else if (this.isThird()) {
            icon = 3;
        }

        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_oddevenvalue_title");
        int[] icons = {icon};
        VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE, icons, 25);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        String col1Text = loc.getCommon().get("verifier_oddevenvalue_col1_text");
        mainPanel.drawString(col1Text, changeX + 55, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);

        String col2Text = loc.getCommon().get("verifier_oddevenvalue_col2_text");
        mainPanel.drawString(col2Text, changeX + 255, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
    }
}
