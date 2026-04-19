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

        String firstString = getEvenOrOddString(first, loc);
        String secondString = getEvenOrOddString(second, loc);
        String thirdString = getEvenOrOddString(third, loc);

        String result = "";
        if (check) {
            result += "first " + loc.getCommon().get("common_is") + " " + firstString + " " + loc.getCommon().get("common_or") + " second " + loc.getCommon().get("common_is") + " " + secondString + " " + loc.getCommon().get("common_or") + " third " + loc.getCommon().get("common_is") + " " + thirdString;
        } else {
            result += "first " + loc.getCommon().get("common_is") + " " + getOpposite(firstString, loc) + " " + loc.getCommon().get("common_or") + " second " + loc.getCommon().get("common_is") + " " + getOpposite(secondString, loc) + " " + loc.getCommon().get("common_or") + " third " + loc.getCommon().get("common_is") + " " + getOpposite(thirdString, loc);
        }
        this.setCheck(result);

        return check;
    }

    private String getOpposite(String firstString, com.apogames.logic.common.Localization loc) {
        if (firstString.equals(loc.getCommon().get("common_even"))) {
            return loc.getCommon().get("common_odd");
        }
        return loc.getCommon().get("common_even");
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
