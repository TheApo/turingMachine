package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class CompareToValueVerifier extends Verifier {

    private int value;

    public CompareToValueVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird, int value) {
        super(solution, isFirst, isSecond, isThird);

        this.value = value;
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new CompareToValueVerifier(super.getSolution(), isFirst(), isSecond(), isThird(), this.value);
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new CompareToValueVerifier(newSolution, isFirst(), isSecond(), isThird(), this.value);
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
        this.setCheck("");
        if (isFirst()) {
            if (first < this.value) {
                if (getSolution().getFirst() < this.value) {
                    this.setCheck(getTranslation()+" < " + this.value);
                } else {
                    this.setCheck(getTranslation()+" >= " + this.value);
                }
                return getSolution().getFirst() < this.value;
            }
            if (first == this.value) {
                if (getSolution().getFirst() == this.value) {
                    this.setCheck(getTranslation()+" = " + this.value);
                } else {
                    this.setCheck(getTranslation()+" != " + this.value);
                }
                return getSolution().getFirst() == this.value;
            }
            if (getSolution().getFirst() > this.value) {
                this.setCheck(getTranslation()+" > " + this.value);
            } else {
                this.setCheck(getTranslation()+" <= " + this.value);
            }
            return getSolution().getFirst() > this.value;
        }
        if (isSecond()) {
            if (second < this.value) {
                if (getSolution().getSecond() < this.value) {
                    this.setCheck(getTranslation()+" < " + this.value);
                } else {
                    this.setCheck(getTranslation()+" >= " + this.value);
                }
                return getSolution().getSecond() < this.value;
            }
            if (second == this.value) {
                if (getSolution().getSecond() == this.value) {
                    this.setCheck(getTranslation()+" = " + this.value);
                } else {
                    this.setCheck(getTranslation()+" != " + this.value);
                }
                return getSolution().getSecond() == this.value;
            }
            if (getSolution().getSecond() > this.value) {
                this.setCheck(getTranslation()+" > " + this.value);
            } else {
                this.setCheck(getTranslation()+" <= " + this.value);
            }
            return getSolution().getSecond() > this.value;
        }
        if (isThird()) {
            if (third < this.value) {
                if (getSolution().getThird() < this.value) {
                    this.setCheck(getTranslation()+" < " + this.value);
                } else {
                    this.setCheck(getTranslation()+" >= " + this.value);
                }
                return getSolution().getThird() < this.value;
            }
            if (third == this.value) {
                if (getSolution().getThird() == this.value) {
                    this.setCheck(getTranslation()+" = " + this.value);
                } else {
                    this.setCheck(getTranslation()+" != " + this.value);
                }
                return getSolution().getThird() == this.value;
            }
            if (getSolution().getThird() > this.value) {
                this.setCheck(getTranslation()+" > " + this.value);
            } else {
                this.setCheck(getTranslation()+" <= " + this.value);
            }
            return getSolution().getThird() > this.value;
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
        return VerifyIDEnum.CompareToValueVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int v = isFirst() ? first : isSecond() ? second : third;
        return new int[]{ cmpCol(v, this.value) };
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
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_comparetovalue_title").replace("{0}", String.valueOf(this.value));
        int[] icons = {icon};
        VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, icons, 25);

        // Draw icons
        for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
            IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
        }

        if (all) {
            IconDraw.renderIcon(mainPanel, changeX + 22, changeY + 130, 30, 30, icon);
            IconDraw.renderIcon(mainPanel, changeX + 22 + 133, changeY + 130, 30, 30, icon);
            IconDraw.renderIcon(mainPanel, changeX + 22 + 133 * 2, changeY + 130, 30, 30, icon);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        int icon = 1;
        if (this.isSecond()) {
            icon = 2;
        } else if (this.isThird()) {
            icon = 3;
        }

        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_comparetovalue_title").replace("{0}", String.valueOf(this.value));
        int[] icons = {icon};
        VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE, icons, 25);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        int columnWidth = 133;
        String[] textKeys = {"verifier_comparetovalue_col1_text", "verifier_comparetovalue_col2_text", "verifier_comparetovalue_col3_text"};
        String[] symbols = {"<", "=", ">"};

        for (int col = 0; col < 3; col++) {
            int x = changeX + 60 + col * columnWidth;

            mainPanel.drawString(symbols[col] + " " + this.value, x, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.BEGIN, false, false);

            String text = loc.getCommon().get(textKeys[col]).replace("{0}", String.valueOf(this.value));
            com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
            mainPanel.drawString(text, x + 6, changeY + 170, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
        }
    }
}
