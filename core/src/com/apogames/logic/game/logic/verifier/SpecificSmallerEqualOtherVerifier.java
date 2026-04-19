package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SpecificSmallerEqualOtherVerifier extends Verifier {

    public SpecificSmallerEqualOtherVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        super(solution, isFirst, isSecond, isThird);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new SpecificSmallerEqualOtherVerifier(super.getSolution(), isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SpecificSmallerEqualOtherVerifier(newSolution, isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
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
            check = first <= second
                    && first <= third
                    && getSolution().getFirst() <= getSolution().getSecond()
                    && getSolution().getFirst() <= getSolution().getThird();
        } else if (isSecond()) {
            check = second <= first
                    && second <= third
                    && getSolution().getSecond() <= getSolution().getFirst()
                    && getSolution().getSecond() <= getSolution().getThird();
        } else if (isThird()) {
            check = third <= second
                    && third <= first
                    && getSolution().getThird() <= getSolution().getSecond()
                    && getSolution().getThird() <= getSolution().getFirst();
        }

        boolean firstSmallest = first <= second && first <= third;
        boolean secondSmallest = second <= first && second <= third;

        if (check) {
            if (firstSmallest) {
                this.setCheck("first " + loc.getCommon().get("common_issmallest") + " " + loc.getCommon().get("common_or") + " second " + loc.getCommon().get("common_or") + " third " + loc.getCommon().get("common_arenotsmallest"));
            } else if (secondSmallest) {
                this.setCheck("second " + loc.getCommon().get("common_issmallest") + " " + loc.getCommon().get("common_or") + " first " + loc.getCommon().get("common_or") + " third " + loc.getCommon().get("common_arenotsmallest"));
            } else {
                this.setCheck("third " + loc.getCommon().get("common_issmallest") + " " + loc.getCommon().get("common_or") + " first " + loc.getCommon().get("common_or") + " second " + loc.getCommon().get("common_arenotsmallest"));
            }
        } else {
            if (firstSmallest) {
                this.setCheck("first " + loc.getCommon().get("common_isnotsmallest") + " " + loc.getCommon().get("common_or") + " second " + loc.getCommon().get("common_or") + " third " + loc.getCommon().get("common_aresmallest"));
            } else if (secondSmallest) {
                this.setCheck("second " + loc.getCommon().get("common_isnotsmallest") + " " + loc.getCommon().get("common_or") + " first " + loc.getCommon().get("common_or") + " third " + loc.getCommon().get("common_aresmallest"));
            } else {
                this.setCheck("third " + loc.getCommon().get("common_isnotsmallest") + " " + loc.getCommon().get("common_or") + " first " + loc.getCommon().get("common_or") + " second " + loc.getCommon().get("common_aresmallest"));
            }
        }

        return check;
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        return "Ist eine Zahl kleiner oder gleich in vergleich zu den anderen";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SpecificSmallerEqualOtherVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 132, 30, 30, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 132, 30, 30, 2);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 132, 30, 30, 3);

            IconDraw.renderIcon(mainPanel, changeX + 15 + 65 + 133, changeY + 122, 20, 20, 1);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 65, changeY + 145, 20, 20, 1);

            IconDraw.renderIcon(mainPanel, changeX + 15 + 65, changeY + 145, 20, 20, 2);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2 + 65, changeY + 122, 20, 20, 2);

            IconDraw.renderIcon(mainPanel, changeX + 15 + 65, changeY + 122, 20, 20, 3);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 + 65, changeY + 145, 20, 20, 3);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_specificsmallerequal_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font15, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        mainPanel.drawString("<=", changeX + 50, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        String col1Text = loc.getCommon().get("verifier_specificsmallerequal_col1_text");
        mainPanel.drawString(col1Text, changeX + 62, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString("<=", changeX + 50 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        String col2Text = loc.getCommon().get("verifier_specificsmallerequal_col2_text");
        mainPanel.drawString(col2Text, changeX + 62 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        mainPanel.drawString("<=", changeX + 50 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);
        String col3Text = loc.getCommon().get("verifier_specificsmallerequal_col3_text");
        mainPanel.drawString(col3Text, changeX + 62 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
