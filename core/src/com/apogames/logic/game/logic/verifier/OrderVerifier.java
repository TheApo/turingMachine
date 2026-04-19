package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class OrderVerifier extends Verifier {

    public OrderVerifier(Solution solution) {
        super(solution, false, false, false);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new OrderVerifier(super.getSolution());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new OrderVerifier(newSolution);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    @Override
    public boolean check(int first, int second, int third) {
        return check(first, second, third, null, -1);
    }

    @Override
    public boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index) {
        int order = getOrder(first, second, third);
        int orderSolution = getOrder(getSolution().getFirst(), getSolution().getSecond(), getSolution().getThird());

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        this.setCheck("");
        if (order == 0) {
            if (order == orderSolution) {
                this.setCheck(loc.getCommon().get("verifier_order_col1_line1"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_order_check_ascendingordescending"));
            }
        } else if (order == 1) {
            if (order == orderSolution) {
                this.setCheck(loc.getCommon().get("verifier_order_col2_line1"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_order_check_notascending"));
            }
        } else {
            if (order == orderSolution) {
                this.setCheck(loc.getCommon().get("verifier_order_col3_line1"));
            } else {
                this.setCheck(loc.getCommon().get("verifier_order_check_notdescending"));
            }
        }

        return order == orderSolution;
    }

    private int getOrder(int first, int second, int third) {
        int order = 0;
        if ((first + 1 == second) && (first + 2 == third)) {
            return 1;
        }
        if ((first - 1 == second) && (first - 2 == third)) {
            return 2;
        }
        return order;
    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getName() {
        return "Aufsteigende, Absteigende oder keine Sortierung";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.OrderVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_order_title");
        VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font15, Constants.COLOR_ORANGE);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        Localization loc = Localization.getInstance();

        // Column 1: No order
        mainPanel.drawString(loc.getCommon().get("verifier_order_col1_line1"), changeX + 62, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_order_col1_line2"), changeX + 62, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        // Column 2: Ascending
        mainPanel.drawString(loc.getCommon().get("verifier_order_col2_line1"), changeX + 62 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_order_col2_line2"), changeX + 62 + 133, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);

        // Column 3: Descending
        mainPanel.drawString(loc.getCommon().get("verifier_order_col3_line1"), changeX + 62 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
        mainPanel.drawString(loc.getCommon().get("verifier_order_col3_line2"), changeX + 62 + 133 * 2, changeY + 170, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.MIDDLE, false, false);
    }
}
