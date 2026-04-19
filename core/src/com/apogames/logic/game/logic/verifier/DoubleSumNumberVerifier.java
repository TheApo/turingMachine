package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class DoubleSumNumberVerifier extends Verifier {

    private final int value;

    public DoubleSumNumberVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird, int value) {
        super(solution, isFirst, isSecond, isThird);

        this.value = value;
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new DoubleSumNumberVerifier(super.getSolution(), isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new DoubleSumNumberVerifier(newSolution, isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
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
        int sum = first + second;
        int sumResult = getSolution().getFirst() + getSolution().getSecond();
        if (isFirst()) {
            if (isThird()) {
                sum = first + third;
                sumResult = getSolution().getFirst() + getSolution().getThird();
            }
        } else {
            sum = second + third;
            sumResult = getSolution().getSecond() + getSolution().getThird();
        }

        this.setCheck("");
        if (sum > this.value && sumResult > this.value) {
            this.setCheck(getName()+" > "+this.value);
            return true;
        } else if (sum < this.value && sumResult < this.value) {
            this.setCheck(getName()+" < "+this.value);
            return true;
        } else if (sum == this.value && sumResult == this.value) {
            this.setCheck(getName()+" = "+this.value);
            return true;
        }
        if (sum > this.value) {
            this.setCheck(getName()+" <= "+this.value);
        } else if (sum < this.value) {
            this.setCheck(getName()+" >= "+this.value);
        } else {
            this.setCheck(getName()+" != "+this.value);
        }
        return false;
    }

    @Override
    public String getText() {
        return "The Verifier compares the sum of the two given numbers to "+this.value+". This sum can be less than, equal to, or greater than "+this.value;
    }

    @Override
    public String getName() {
        String number = "first";
        String numberSecond = "second";
        if (!this.isFirst()) {
          number = "second";
          numberSecond = "third";
        } if (this.isThird()) {
            numberSecond = "third";
        }
        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();
        return loc.getCommon().get("verifier_doublesumnumber_check_sumof") + " " + number + " " + loc.getCommon().get("common_and") + " " + numberSecond;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.DoubleSumNumberVerifier.getValue();
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);
        }

        int icon = 1;
        int iconTwo = 2;
        if (!this.isFirst()) {
            icon = 2;
            iconTwo = 3;
        } else if (this.isThird()) {
            iconTwo = 3;
        }

        // Calculate icon positions
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_doublesumnumber_title").replace("{0}", String.valueOf(this.value));
        int[] icons = {icon, iconTwo};
        VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, icons, 25);

        // Draw icons
        for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
            IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
        }

        if (all) {
            IconDraw.renderIcon(mainPanel, changeX + 23, changeY + 132, 20, 20, icon);
            IconDraw.renderIcon(mainPanel, changeX + 23 + 133, changeY + 132, 20, 20, icon);
            IconDraw.renderIcon(mainPanel, changeX + 23 + 133 * 2, changeY + 132, 20, 20, icon);

            IconDraw.renderIcon(mainPanel, changeX + 23 + 30, changeY + 132, 20, 20, iconTwo);
            IconDraw.renderIcon(mainPanel, changeX + 23 + 133 + 30, changeY + 132, 20, 20, iconTwo);
            IconDraw.renderIcon(mainPanel, changeX + 23 + 133 * 2 + 30, changeY + 132, 20, 20, iconTwo);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        int icon = 1;
        int iconTwo = 2;
        if (!this.isFirst()) {
            icon = 2;
            iconTwo = 3;
        } else if (this.isThird()) {
            iconTwo = 3;
        }

        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_doublesumnumber_title").replace("{0}", String.valueOf(this.value));
        int[] icons = {icon, iconTwo};
        VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE, icons, 25);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        int columnWidth = 133;
        String[] textKeys = {"verifier_doublesumnumber_col1_text", "verifier_doublesumnumber_col2_text", "verifier_doublesumnumber_col3_text"};
        String[] symbols = {"<", "=", ">"};

        for (int col = 0; col < 3; col++) {
            int x = changeX + 65 + col * columnWidth;

            mainPanel.drawString(symbols[col] + " " + this.value, x + 20, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);

            String text = loc.getCommon().get(textKeys[col]).replace("{0}", String.valueOf(this.value));
            com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
            mainPanel.drawString(text, x, changeY + 170, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
        }

        mainPanel.drawString("+", changeX + 45, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString("+", changeX + 45 + 133, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
        mainPanel.drawString("+", changeX + 45 + 133 * 2, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font15, DrawString.BEGIN, false, false);
    }
}
