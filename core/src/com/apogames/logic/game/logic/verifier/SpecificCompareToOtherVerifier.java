package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;

import java.util.ArrayList;

public class SpecificCompareToOtherVerifier extends Verifier {

    int show;

    public SpecificCompareToOtherVerifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        super(solution, isFirst, isSecond, isThird);

        if (isFirst && isSecond) {
            show = 1;
            if (Math.random() * 100 < 50) {
                show = 2;
            }
        } else if (isThird && isSecond) {
            show = 2;
            if (Math.random() * 100 < 50) {
                show = 3;
            }
        } else {
            show = 3;
            if (Math.random() * 100 < 50) {
                show = 1;
            }
        }
        this.setColumn(3);
    }

    public Verifier getCopy() {
        Verifier verifier = new SpecificCompareToOtherVerifier(super.getSolution(), isFirst(), isSecond(), isThird());
        verifier.setCheck(this.getCheck());
        return verifier;
    }

    public Verifier getCopyWithSolution(Solution newSolution) {
        Verifier verifier = new SpecificCompareToOtherVerifier(newSolution, isFirst(), isSecond(), isThird());
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

        boolean check = false;
        if (this.show == 1) {
            if (isSecond()) {
                if (first == second) {
                    check = getSolution().getFirst() == getSolution().getSecond();
                } else if (first < second) {
                    check = getSolution().getFirst() < getSolution().getSecond();
                } else {
                    check = getSolution().getFirst() > getSolution().getSecond();
                }
            }
            if (isThird()) {
                if (first == third) {
                    check = getSolution().getFirst() == getSolution().getThird();
                } else if (first < third) {
                    check = getSolution().getFirst() < getSolution().getThird();
                } else {
                    check = getSolution().getFirst() > getSolution().getThird();
                }
            }
        } else if (this.show == 2) {
            if (isFirst()) {
                if (second == first) {
                    check = getSolution().getSecond() == getSolution().getFirst();
                } else if (second < first) {
                    check = getSolution().getSecond() < getSolution().getFirst();
                } else {
                    check = getSolution().getSecond() > getSolution().getFirst();
                }
            }
            if (isThird()) {
                if (second == third) {
                    check = getSolution().getSecond() == getSolution().getThird();
                } else if (second < third) {
                    check = getSolution().getSecond() < getSolution().getThird();
                } else {
                    check = getSolution().getSecond() > getSolution().getThird();
                }
            }
        } else {
            if (isFirst()) {
                if (third == first) {
                    check = getSolution().getThird() == getSolution().getFirst();
                } else if (third < first) {
                    check = getSolution().getThird() < getSolution().getFirst();
                } else {
                    check = getSolution().getThird() > getSolution().getFirst();
                }
            }
            if (isSecond()) {
                if (third == second) {
                    check = getSolution().getThird() == getSolution().getSecond();
                } else if (third < second) {
                    check = getSolution().getThird() < getSolution().getSecond();
                } else {
                    check = getSolution().getThird() > getSolution().getSecond();
                }
            }
        }

        String result = "";

        return check;
    }

    @Override
    public String getText() {
        return "";
    }


    @Override
    public String getName() {
        String number = "first";
        if (this.show == 2) {
            number = "second";
        } else if (this.show == 3) {
            number = "third";
        }
        return number;
    }

    @Override
    public int getId() {
        return VerifyIDEnum.SpecificCompareToOtherVerifier.getValue();
    }

    public Difficulty getDifficulty() {
        return Difficulty.EXPERT;
    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);
        }
        int icon = this.show;
        int iconTwo = getOtherIcon();

        IconDraw.renderIcon(mainPanel, changeX + 140, changeY + 35, 20, 20, icon);

        if (all) {
            for (int i = 0; i < 2; i++) {
                IconDraw.renderIcon(mainPanel, changeX + 15, changeY + 125 + i * 40, 30, 30, icon);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133, changeY + 125 + i * 40, 30, 30, icon);
                IconDraw.renderIcon(mainPanel, changeX + 15 + 133 * 2, changeY + 125 + i * 40, 30, 30, icon);

                IconDraw.renderIcon(mainPanel, changeX + 75, changeY + 125 + i * 40, 30, 30, iconTwo);
                IconDraw.renderIcon(mainPanel, changeX + 75 + 133, changeY + 125 + i * 40, 30, 30, iconTwo);
                IconDraw.renderIcon(mainPanel, changeX + 75 + 133 * 2, changeY + 125 + i * 40, 30, 30, iconTwo);

                iconTwo += 1;
                if (iconTwo == this.show) {
                    iconTwo += 1;
                }
                if (iconTwo > 3) {
                    iconTwo = 1;
                }
            }
            renderFillSeparator(mainPanel, changeX, changeY, 2);
            renderFillSeparatorHorizontal(mainPanel, changeX, changeY, 3);
        }
    }

    public int getOtherIcon() {
        if (this.show == 1) {
            return 2;
        } else {
            return 1;
        }
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = com.apogames.logic.common.Localization.getInstance().getCommon().get("verifier_specificcomparetoother_title");

        int icon = this.show;
        int iconTwo = getOtherIcon();
        int[] icons = {icon, iconTwo};

        VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title, changeX + 100, changeY + 40, 280, 25, AssetLoader.font20, Constants.COLOR_ORANGE, icons, 25);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        super.renderText(mainPanel, changeX, changeY);

        mainPanel.drawString("<", changeX + 60, changeY + 130, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString("<", changeX + 60, changeY + 130+ 40, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString("=", changeX + 60 + 133, changeY + 130, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString("=", changeX + 60 + 133, changeY + 130 + 40, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString(">", changeX + 60 + 133 * 2, changeY + 130, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        mainPanel.drawString(">", changeX + 60 + 133 * 2, changeY + 130 + 40, Constants.COLOR_BLACK, AssetLoader.font20, DrawString.MIDDLE, false, false);
    }
}
