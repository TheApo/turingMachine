package com.apogames.logic.game.card;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.GameScreen;
import com.apogames.logic.entity.ApoButtonTuringNumber;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.Solution;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Card {

    private final Solution solution;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private boolean correct;

    private final ApoButtonTuringNumber[] numbers;

    private boolean withoutSolution = false;

    public Card(Solution solution, int x, int y, int width, int height, boolean correct) {
        this.solution = solution;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.correct = correct;

        this.numbers = new ApoButtonTuringNumber[3];
        this.numbers[0] = new ApoButtonTuringNumber(this.x, this.y, this.width/3, this.width/3, "", String.valueOf(solution.getFirst()), Constants.COLOR_NUMBER_ONE, Constants.COLOR_WHITE, 1);
        this.numbers[1] = new ApoButtonTuringNumber(this.x + this.width/3, this.y, this.width/3, this.width/3, "", String.valueOf(solution.getSecond()), Constants.COLOR_NUMBER_TWO, Constants.COLOR_WHITE, 2);
        this.numbers[2] = new ApoButtonTuringNumber(this.x + this.width*2/3, this.y, this.width/3, this.width/3, "", String.valueOf(solution.getThird()), Constants.COLOR_NUMBER_THREE, Constants.COLOR_WHITE, 3);

        for (ApoButtonTuringNumber number : this.numbers) {
            number.setFont(AssetLoader.font40);
        }
    }

    public boolean isWithoutSolution() {
        return withoutSolution;
    }

    public void setWithoutSolution(boolean withoutSolution) {
        this.withoutSolution = withoutSolution;
    }

    public Solution getSolution() {
        return solution;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setMenuCardSolution() {
        int[][] card = CardHelper.BLUE[this.solution.getFirst() - 1];
        int[][] cardSecond = CardHelper.YELLOW[this.solution.getSecond() - 1];
        int[][] cardThird = CardHelper.PURPLE[this.solution.getThird() - 1];
        for (int y = 0; y < card.length; y++) {
            for (int x = 0; x < card[0].length; x++) {
                if (card[y][x] == 1 && cardSecond[y][x] == 1 && cardThird[y][x] == 1) {
                    this.correct = CardHelper.MENU_CORRECT[y][x];
                }
            }
        }
    }

    public void render(GameScreen screen) {
        this.render(screen, 0, 0);
    }

    public void render(GameScreen screen, int changeX, int changeY) {
        screen.getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        screen.getRenderer().setColor(Constants.COLOR_BACKGROUND_CARD[0], Constants.COLOR_BACKGROUND_CARD[1], Constants.COLOR_BACKGROUND_CARD[2], 1f);
        screen.getRenderer().roundedRect(this.x + changeX - 10, this.y + changeY + this.width/3f, this.width + 20, this.height + 20 - this.width/3f, 10);

        int tileSize = this.width / 12;
        int[][] card = CardHelper.BLUE[this.solution.getFirst() - 1];
        int[][] cardSecond = CardHelper.YELLOW[this.solution.getSecond() - 1];
        int[][] cardThird = CardHelper.PURPLE[this.solution.getThird() - 1];
        for (int y = 0; y < card.length; y++) {
            for (int x = 0; x < card[0].length; x++) {
                if (card[y][x] == 1) {
                    if (cardSecond[y][x] == 1 && cardThird[y][x] == 1) {
                        IconDraw.renderSolutionCheck(screen, this.correct, this.x + changeX + x * tileSize, this.y + this.width/3 + changeY + 10 + y * tileSize, tileSize, tileSize);
                    } else {
                        screen.getRenderer().setColor(Constants.COLOR_BACKGROUND_CARD_2[0], Constants.COLOR_BACKGROUND_CARD_2[1], Constants.COLOR_BACKGROUND_CARD_2[2], 1f);
                        if (cardSecond[y][x] != 1) {
                            screen.getRenderer().setColor(Constants.COLOR_BACKGROUND_CARD_3[0], Constants.COLOR_BACKGROUND_CARD_3[1], Constants.COLOR_BACKGROUND_CARD_3[2], 1f);
                        }
                        screen.getRenderer().rect(this.x + changeX + x * tileSize, this.y + this.width/3f + changeY + 10 + y * tileSize, tileSize, tileSize);
                    }
                }

                screen.getRenderer().setColor(Constants.COLOR_GREY[0], Constants.COLOR_GREY[1], Constants.COLOR_GREY[2], 1f);
                if ((x - 1 < 0 && card[y][x] == 1) || (x - 1 >= 0 && card[y][x-1] != card[y][x])) {
                    screen.getRenderer().rect(this.x + changeX + x * tileSize - 1, this.y + this.width/3f + changeY + 10 + y * tileSize, 2, tileSize);
                }
                if ((y - 1 < 0 && card[y][x] == 1) || (y - 1 >= 0 && card[y - 1][x] != card[y][x])) {
                    screen.getRenderer().rect(this.x + changeX + x * tileSize, this.y + this.width/3f + changeY + 10 + y * tileSize - 1, tileSize, 2);
                }
                if (x + 1 >= card[0].length && card[y][x] == 1) {
                    screen.getRenderer().rect(this.x + changeX + (x + 1) * tileSize - 1, this.y + this.width/3f + changeY + 10 + y * tileSize, 2, tileSize);
                }
                if (y + 1 >= card.length && card[y][x] == 1) {
                    screen.getRenderer().rect(this.x + changeX + x * tileSize, this.y + this.width/3f + changeY + 10 + (y + 1) * tileSize - 1, tileSize, 2);
                }
            }
        }

        screen.getRenderer().end();

        if (!this.withoutSolution) {
            for (ApoButtonTuringNumber number : this.numbers) {
                number.render(screen, changeX, changeY);
            }
        }
    }
}
