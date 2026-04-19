package com.apogames.logic.game.menu;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.backend.SequentiallyThinkingScreenModel;
import com.apogames.logic.common.Localization;
import com.apogames.logic.entity.ApoButton;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.card.Card;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sun.org.apache.bcel.internal.Const;

import java.util.Locale;

public class Menu extends SequentiallyThinkingScreenModel {

    public static final String FUNCTION_BACK = "QUIT";

    public static final String FUNCTION_PLAY = "PLAY";

    public static final String FUNCTION_LANGUAGE = "LANGUAGE";

    public static final String FUNCTION_VERIFIER_LEFT = "VERIFIER_LEFT";
    public static final String FUNCTION_VERIFIER_RIGHT = "VERIFIER_RIGHT";
    public static final String FUNCTION_DIFFICULTY_LEFT = "DIFFICULTY_LEFT";
    public static final String FUNCTION_DIFFICULTY_RIGHT = "DIFFICULTY_RIGHT";

    public static final String FUNCTION_GUESS_ONE_FIRST = "GUESS_ONE_FIRST";
    public static final String FUNCTION_GUESS_ONE_SECOND = "GUESS_ONE_SECOND";
    public static final String FUNCTION_GUESS_ONE_THIRD = "GUESS_ONE_THIRD";

    public static final String FUNCTION_GUESS_TWO_FIRST = "GUESS_TWO_FIRST";
    public static final String FUNCTION_GUESS_TWO_SECOND = "GUESS_TWO_SECOND";
    public static final String FUNCTION_GUESS_TWO_THIRD = "GUESS_TWO_THIRD";

    public static final int MIN_VERIFIER = 4;
    public static final int MAX_VERIFIER = 6;

    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private int curVerifier = MIN_VERIFIER;
    private Difficulty curDifficulty = Difficulty.EASY;

    private Card cardOne;
    private Card cardTwo;

    private boolean german = true;

    public Menu(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_BACK).setVisible(true);
    }

    @Override
    public void init() {
        if (getGameProperties() == null) {
            setGameProperties(new MenuPreferences(this));
            loadProperties();
        }

        if (this.cardOne == null) {
            this.cardOne = new Card(new Solution(), 100, Constants.GAME_HEIGHT / 2 - 100, 240, 320, true);
            this.cardOne.setMenuCardSolution();
            this.cardOne.setWithoutSolution(true);
        }
        if (this.cardTwo == null) {
            this.cardTwo = new Card(new Solution(), Constants.GAME_WIDTH - 340, Constants.GAME_HEIGHT/2 - 100, 240, 320, false);
            this.cardTwo.setMenuCardSolution();
            this.cardTwo.setWithoutSolution(true);
        }

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.german = Localization.getInstance().getLocale().getLanguage().equals("de");

        this.setNeededButtonsVisible();
        this.setButtonsVisibility();
    }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);

        keys[keyCode] = false;
    }

    public void mouseMoved(int mouseX, int mouseY) {
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        this.isPressed = false;
    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        if (isRightButton && !this.isPressed) {
            this.isPressed = true;
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (isRightButton) {
            if (!this.isPressed) {
                this.mousePressed(x, y, isRightButton);
            }
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        int number = 0;
        switch (function) {
            case Menu.FUNCTION_BACK:
                quit();
                break;
            case Menu.FUNCTION_VERIFIER_LEFT:
                setVerifier(-1);
                break;
            case Menu.FUNCTION_VERIFIER_RIGHT:
                setVerifier(+1);
                break;
            case Menu.FUNCTION_DIFFICULTY_LEFT:
                setDifficulty(-1);
                break;
            case Menu.FUNCTION_DIFFICULTY_RIGHT:
                setDifficulty(+1);
                break;
            case Menu.FUNCTION_GUESS_ONE_FIRST:
                number = addNumber(this.cardOne.getSolution().getFirst(), 1);
                this.cardOne.getSolution().setFirst(number);
                this.cardOne.setMenuCardSolution();
                getMainPanel().getButtonByFunction(function).setText(String.valueOf(number));
                break;
            case Menu.FUNCTION_GUESS_TWO_FIRST:
                number = addNumber(this.cardTwo.getSolution().getFirst(), 1);
                this.cardTwo.getSolution().setFirst(number);
                this.cardTwo.setMenuCardSolution();
                getMainPanel().getButtonByFunction(function).setText(String.valueOf(number));
                break;
            case Menu.FUNCTION_GUESS_ONE_SECOND:
                number = addNumber(this.cardOne.getSolution().getSecond(), 1);
                this.cardOne.getSolution().setSecond(number);
                this.cardOne.setMenuCardSolution();
                getMainPanel().getButtonByFunction(function).setText(String.valueOf(number));
                break;
            case Menu.FUNCTION_GUESS_TWO_SECOND:
                number = addNumber(this.cardTwo.getSolution().getSecond(), 1);
                this.cardTwo.getSolution().setSecond(number);
                this.cardTwo.setMenuCardSolution();
                getMainPanel().getButtonByFunction(function).setText(String.valueOf(number));
                break;
            case Menu.FUNCTION_GUESS_ONE_THIRD:
                number = addNumber(this.cardOne.getSolution().getThird(), 1);
                this.cardOne.getSolution().setThird(number);
                this.cardOne.setMenuCardSolution();
                getMainPanel().getButtonByFunction(function).setText(String.valueOf(number));
                break;
            case Menu.FUNCTION_GUESS_TWO_THIRD:
                number = addNumber(this.cardTwo.getSolution().getThird(), 1);
                this.cardTwo.getSolution().setThird(number);
                this.cardTwo.setMenuCardSolution();
                getMainPanel().getButtonByFunction(function).setText(String.valueOf(number));
                break;
            case Menu.FUNCTION_PLAY:
                this.getMainPanel().changeToGame(this.curVerifier, this.curDifficulty);
                break;
            case Menu.FUNCTION_LANGUAGE:
                this.german = !this.german;

                if (this.german) {
                    Localization.getInstance().setLocale(Locale.GERMAN);
                } else {
                    Localization.getInstance().setLocale(Locale.ENGLISH);
                }

                changeIDForLanguage(function);
                break;
        }
    }

    private int addNumber(int value, int add) {
        int result = value + add;
        if (result > Constants.MAX_VALUE) {
            result = Constants.MIN_VALUE;
        } else if (result < Constants.MIN_VALUE) {
            result = Constants.MAX_VALUE;
        }
        return result;
    }

    private void setVerifier(int add) {
        this.curVerifier += add;
        if (this.curVerifier < MIN_VERIFIER) {
            this.curVerifier = MAX_VERIFIER;
        } else if (this.curVerifier > MAX_VERIFIER) {
            this.curVerifier = MIN_VERIFIER;
        }
    }

    private void setDifficulty(int add) {
        this.curDifficulty = this.curDifficulty.next(add);
    }

    private void setButtonsVisibility() {
        changeIDForLanguage(FUNCTION_LANGUAGE);

        getMainPanel().getButtonByFunction(Menu.FUNCTION_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_PLAY).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_VERIFIER_LEFT).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_VERIFIER_RIGHT).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_DIFFICULTY_LEFT).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_DIFFICULTY_RIGHT).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_ONE_FIRST).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_ONE_SECOND).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_ONE_THIRD).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_TWO_FIRST).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_TWO_SECOND).setVisible(true);
        getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_TWO_THIRD).setVisible(true);
        if (!Constants.IS_HTML) {
            getMainPanel().getButtonByFunction(Menu.FUNCTION_LANGUAGE).setVisible(true);
        }

        setButtonToPosition(getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_ONE_FIRST), this.cardOne.getX(), this.cardOne.getY(), String.valueOf(this.cardOne.getSolution().getFirst()));
        setButtonToPosition(getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_ONE_SECOND), this.cardOne.getX() + this.cardOne.getWidth()/3, this.cardOne.getY(), String.valueOf(this.cardOne.getSolution().getSecond()));
        setButtonToPosition(getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_ONE_THIRD), this.cardOne.getX() + this.cardOne.getWidth()*2/3, this.cardOne.getY(), String.valueOf(this.cardOne.getSolution().getThird()));

        setButtonToPosition(getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_TWO_FIRST), this.cardTwo.getX(), this.cardTwo.getY(), String.valueOf(this.cardTwo.getSolution().getFirst()));
        setButtonToPosition(getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_TWO_SECOND), this.cardTwo.getX() + this.cardTwo.getWidth()/3, this.cardTwo.getY(), String.valueOf(this.cardTwo.getSolution().getSecond()));
        setButtonToPosition(getMainPanel().getButtonByFunction(Menu.FUNCTION_GUESS_TWO_THIRD), this.cardTwo.getX() + this.cardTwo.getWidth()*2/3, this.cardTwo.getY(), String.valueOf(this.cardTwo.getSolution().getThird()));

        if (Constants.IS_HTML) {
            getMainPanel().getButtonByFunction(Menu.FUNCTION_BACK).setVisible(false);
        }
    }

    private void changeIDForLanguage(String functionLanguage) {
        if (this.german) {
            getMainPanel().getButtonByFunction(functionLanguage).setId("button_language_de");
        } else {
            getMainPanel().getButtonByFunction(functionLanguage).setId("button_language_en");
        }
    }

    private void setButtonToPosition(ApoButton button, int x, int y, String text) {
        button.setX(x);
        button.setY(y);
        button.setText(text);
    }

    public void mouseWheelChanged(int changed) {
    }

    @Override
    protected void quit() {
        getMainPanel().quitGame();
    }

    @Override
    public void doThink(float delta) {

    }

    @Override
    public void render() {
        int startY = (int)(Constants.GAME_HEIGHT/2f + 50);

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS[0], Constants.COLOR_BUTTONS[1], Constants.COLOR_BUTTONS[2], Constants.COLOR_BUTTONS[3]);
        getMainPanel().getRenderer().roundedRect(Constants.GAME_WIDTH/2f - 250, startY - 210, 500, 300, 15);

        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();

        getMainPanel().drawString(Constants.PROPERTY_NAME, Constants.GAME_WIDTH/2f, 40, Constants.COLOR_BUTTONS_DARK, AssetLoader.font40, DrawString.MIDDLE, true, false);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("title_description"), Constants.GAME_WIDTH/2f, 85, Constants.COLOR_BUTTONS_DARK, AssetLoader.font25, DrawString.MIDDLE, true, false);

        getMainPanel().drawString(Localization.getInstance().getCommon().get("menu_help_1"), Constants.GAME_WIDTH/2f, 150, Constants.COLOR_BUTTONS_DARK, AssetLoader.font25, DrawString.MIDDLE, true, false);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("menu_help_2"), Constants.GAME_WIDTH/2f, 180, Constants.COLOR_BUTTONS_DARK, AssetLoader.font25, DrawString.MIDDLE, true, false);

        String menu_verifier = Localization.getInstance().getCommon().get("menu_verifier");
        BitmapFont font = AssetLoader.font40;
        getMainPanel().getGlyphLayout().setText(font, menu_verifier);
        if (getMainPanel().getGlyphLayout().width > 250) {
            font = AssetLoader.font30;
        }
        getMainPanel().drawString(menu_verifier, Constants.GAME_WIDTH/2f, startY - 30, Constants.COLOR_BUTTONS_DARK, font, DrawString.MIDDLE, true, false);
        getMainPanel().drawString(""+this.curVerifier, Constants.GAME_WIDTH/2f, startY + 34, Constants.COLOR_BUTTONS_DARK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        String menu_difficulty = Localization.getInstance().getCommon().get("menu_difficulty");
        font = AssetLoader.font40;
        getMainPanel().getGlyphLayout().setText(font, menu_difficulty);
        if (getMainPanel().getGlyphLayout().width > 250) {
            font = AssetLoader.font30;
        }

        getMainPanel().drawString(menu_difficulty, Constants.GAME_WIDTH/2f, startY - 180, Constants.COLOR_BUTTONS_DARK, font, DrawString.MIDDLE, true, false);
        getMainPanel().drawString(this.curDifficulty.name(), Constants.GAME_WIDTH/2f, startY - 116, Constants.COLOR_BUTTONS_DARK, AssetLoader.font30, DrawString.MIDDLE, true, false);

        getMainPanel().drawString("Version: "+Constants.VERSION, Constants.GAME_WIDTH/2f, Constants.GAME_HEIGHT - 15, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.MIDDLE, true, false);

        getMainPanel().spriteBatch.end();

        this.cardOne.render(getMainPanel(), 0, 0);
        this.cardTwo.render(getMainPanel(), 0, 0);

        for (ApoButton button : this.getMainPanel().getButtons()) {
            button.render(this.getMainPanel());
        }
    }

//	        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//			Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
//
//			getMainPanel().getRenderer().begin(ShapeType.Line);
//			getMainPanel().getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
//			getMainPanel().getRenderer().roundedRectLine((WIDTH - width)/2f, startY, width, height, 5);
//			getMainPanel().getRenderer().end();


    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
