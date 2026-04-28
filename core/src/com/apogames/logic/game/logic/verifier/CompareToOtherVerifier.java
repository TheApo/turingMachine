package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class CompareToOtherVerifier extends Verifier {

    public CompareToOtherVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        super(solution, isFirst, isSecond, isThird);
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new CompareToOtherVerifier(super.getSolution(), isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new CompareToOtherVerifier(newSolution, isFirst(), isSecond(), isThird());
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
        if (isFirst()) {
            if (isSecond()) {
                if (first == second) {
                    if (getSolution().getFirst() == getSolution().getSecond()) {
                        this.setCheck("first = second");
                    } else {
                        this.setCheck("first != second");
                    }
                    return getSolution().getFirst() == getSolution().getSecond();
                }
                if (first < second) {
                    if (getSolution().getFirst() < getSolution().getSecond()) {
                        this.setCheck("first < second");
                    } else {
                        this.setCheck("first >= second");
                    }
                    return getSolution().getFirst() < getSolution().getSecond();
                }
                if (getSolution().getFirst() > getSolution().getSecond()) {
                    this.setCheck("first > second");
                } else {
                    this.setCheck("first <= second");
                }
                return getSolution().getFirst() > getSolution().getSecond();
            }
            if (isThird()) {
                if (first == third) {
                    if (getSolution().getFirst() == getSolution().getThird()) {
                        this.setCheck("first = third");
                    } else {
                        this.setCheck("first != third");
                    }
                    return getSolution().getFirst() == getSolution().getThird();
                }
                if (first < third) {
                    if (getSolution().getFirst() < getSolution().getThird()) {
                        this.setCheck("first < third");
                    } else {
                        this.setCheck("first >= third");
                    }
                    return getSolution().getFirst() < getSolution().getThird();
                }
                if (getSolution().getFirst() > getSolution().getThird()) {
                    this.setCheck("first > third");
                } else {
                    this.setCheck("first <= third");
                }
                return getSolution().getFirst() > getSolution().getThird();
            }
        }

        if (second == third) {
            if (getSolution().getSecond() == getSolution().getThird()) {
                this.setCheck("second = third");
            } else {
                this.setCheck("second != third");
            }
            return getSolution().getSecond() == getSolution().getThird();
        }
        if (second < third) {
            if (getSolution().getSecond() < getSolution().getThird()) {
                this.setCheck("second < third");
            } else {
                this.setCheck("second >= third");
            }
            return getSolution().getSecond() < getSolution().getThird();
        }
        if (getSolution().getSecond() > getSolution().getThird()) {
            this.setCheck("second > third");
        } else {
            this.setCheck("second <= third");
        }
        return getSolution().getSecond() > getSolution().getThird();
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        String number = "erste";
        String numberSecond = "zweite";
        if (!this.isFirst()) {
            number = "zweite";
            numberSecond = "dritte";
        } else if (this.isThird()) {
            numberSecond = "dritte";
        }
        return "Die "+number+" und die "+numberSecond+" Zahl wird verglichen.";
    }

    @Override
    public int getId() {
        return VerifyIDEnum.CompareToOtherVerifier.getValue();
    }

    @Override
    public int[] getCellsForGuess(int first, int second, int third) {
        int a, b;
        if (isFirst() && isSecond()) {
            a = first; b = second;
        } else if (isFirst() && isThird()) {
            a = first; b = third;
        } else {
            a = second; b = third;
        }
        return new int[]{ cmpCol(a, b) };
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

        // Calculate icon positions before rendering
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_comparetoother_title");
        int[] icons = {icon, iconTwo};
        VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, icons, 25);

        // Draw icons from calculated positions
        for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
            IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
        }

        if (all) {
            IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 130, 30, 30, icon);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 130, 30, 30, icon);
            IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 130, 30, 30, icon);

            IconDraw.renderIcon(mainPanel, changeX + 75, changeY + 130, 30, 30, iconTwo);
            IconDraw.renderIcon(mainPanel, changeX + 75 + 133, changeY + 130, 30, 30, iconTwo);
            IconDraw.renderIcon(mainPanel, changeX + 75 + 133 * 2, changeY + 130, 30, 30, iconTwo);

            renderFillSeparator(mainPanel, changeX, changeY, 2);
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_comparetoother_title");

        int icon1 = 1;
        int icon2 = 2;
        if (!this.isFirst()) {
            icon1 = 2;
            icon2 = 3;
        } else if (this.isThird()) {
            icon2 = 3;
        }

        int[] icons = {icon1, icon2};

        VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE, icons, 25);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        com.apogames.logic.common.Localization loc = com.apogames.logic.common.Localization.getInstance();

        // Render 3 columns: <, =, >
        int columnWidth = 133;
        String[] textKeys = {"verifier_comparetoother_col1_text", "verifier_comparetoother_col2_text", "verifier_comparetoother_col3_text"};
        String[] symbols = {"<", "=", ">"};

        for (int col = 0; col < 3; col++) {
            int x = changeX + 60 + col * columnWidth;

            mainPanel.drawString(symbols[col], x, changeY + 135, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);

            String text = loc.getCommon().get(textKeys[col]);
            com.badlogic.gdx.graphics.g2d.BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
            mainPanel.drawString(text, x + 2, changeY + 170, Constants.COLOR_BLACK, font, DrawString.MIDDLE, false, false);
        }
    }
}
