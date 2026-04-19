package com.apogames.logic.game.logic;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.backend.SequentiallyThinkingScreenModel;
import com.apogames.logic.common.Localization;
import com.apogames.logic.entity.ApoButton;
import com.apogames.logic.entity.ApoButtonCheckbox;
import com.apogames.logic.entity.ApoButtonOnlyX;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.card.Card;
import com.apogames.logic.game.logic.ai.ApoSolver;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Level;
import com.apogames.logic.game.logic.level.Solution;
import com.apogames.logic.game.logic.verifier.Verifier;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Logic extends SequentiallyThinkingScreenModel {

    public static final String FUNCTION_LOGIC_BACK = "LOGIC_QUIT";
    public static final String FUNCTION_FIRST = "FIRST";
    public static final String FUNCTION_SECOND = "SECOND";
    public static final String FUNCTION_THIRD = "THIRD";
    public static final String FUNCTION_VERIFIER = "VERIFIER_STEP";

    public static final String FUNCTION_GUESS_FIRST = "GUESS_FIRST";
    public static final String FUNCTION_GUESS_SECOND = "GUESS_SECOND";
    public static final String FUNCTION_GUESS_THIRD = "GUESS_THIRD";
    public static final String FUNCTION_GUESS_SOLUTION = "GUESS_SOLVE";

    public static final String FUNCTION_STEP_TO_NEXT = "NEXT";

    public static final String FUNCTION_SOLVED_NEXTROUND = "NEXT_ROUND";
    public static final String FUNCTION_MOUSE_OVER = "MOUSE_OVER";
    public static final String FUNCTION_HELP = "HELP";

    public static final String FUNCTION_VERIFIER_START = "verifeyer_";

    private final int width = 400;
    private final int height = 200;
    private final int startX = 5;
    private final int startY = 170;

    private boolean solved = false;

    private boolean help = false;

    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private Level level;

    private int amount;

    private Difficulty difficulty = Difficulty.EASY;

    private boolean showResult = false;

    private Step step = Step.PROPOSOL;

    private Card solutionCard;

    private ArrayList<Solution> stillPossibleSolutions = new ArrayList<>();

    public Logic(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_LOGIC_BACK).setVisible(true);
    }

    @Override
    public void init() {
        if (getGameProperties() == null) {
            setGameProperties(new LogicPreferences(this));
            loadProperties();
        }

        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNewLevel();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    private void setNewLevel() {
        this.step = Step.PROPOSOL;
        this.level = (new ApoSolver()).getNextLevel(this.amount, this.difficulty);//new Level(this.amount, this.difficulty);
        this.showResult = false;
        this.solved = false;
        getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setText("Verify");
        getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setId("button_step_verify");

        this.setNeededButtonsVisible();
        this.setButtonsVisibility();

        this.removeButtonsForVerifeyers();
        this.createButtonsForVerifeyers();

        this.stillPossibleSolutions.clear();
        this.stillPossibleSolutions = new ApoSolver().getNewPossibleGuesses();
    }

    private void removeButtonsForVerifeyers() {
        for (int i = getMainPanel().getButtons().size() - 1; i >= 0; i--) {
            if (getMainPanel().getButtons().get(i).getFunction().startsWith(FUNCTION_VERIFIER_START)) {
                getMainPanel().getButtons().remove(i);
            }
        }
    }

    private void createButtonsForVerifeyers() {
        int myX = this.startX;
        int myY = this.startY;
        int index = 0;
        for (Verifier verify : this.level.getVerifiers()) {
            for (int i = 0; i < verify.getColumn(); i++) {
                int curX = (int)(myX + (float)width / verify.getColumn() * i);
                int curY = myY + 125;
                int curWidth = width / verify.getColumn();
                int curHeight = 70;
                String function = FUNCTION_VERIFIER_START + index + "_" + i;
                getMainPanel().getButtons().add(new ApoButtonOnlyX(curX, curY, curWidth, curHeight, function, Constants.COLOR_RED, Constants.COLOR_YELLOW));
            }
            index += 1;
            myX += width;
            if (myX > this.startX + this.width) {
                myX = this.startX;
                myY += this.height;
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);

        if (keyCode == Input.Keys.N) {
            this.setNewLevel();
        }

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

        if (this.showResult) {
            if (function.equals(FUNCTION_SOLVED_NEXTROUND)) {
                setNewLevel();
            } else if (function.equals(Logic.FUNCTION_LOGIC_BACK)) {
                quit();
            }
            return;
        }

        if (function.startsWith(Logic.FUNCTION_VERIFIER_START)) {
            ApoButton buttonByFunction = getMainPanel().getButtonByFunction(function);
            buttonByFunction.setSelect(!buttonByFunction.isSelect());
        }

        if (function.startsWith(Logic.FUNCTION_FIRST) ||
            function.startsWith(Logic.FUNCTION_SECOND) ||
            function.startsWith(Logic.FUNCTION_THIRD)) {
            ApoButtonCheckbox buttonByFunction = (ApoButtonCheckbox)(getMainPanel().getButtonByFunction(function));
            buttonByFunction.setChecked(!buttonByFunction.isChecked());
        } else if (function.startsWith(Logic.FUNCTION_VERIFIER)) {
            if (step == Step.QUESTION) {
                String sub = function.substring(function.lastIndexOf("_") + 1);
                int index = Integer.parseInt(sub) - 1;
                this.level.getRounds()[this.level.getCurRound()].getVerifier()[index] = this.level.getVerifiers().get(index).check(this.level.getGuess().getFirst(), this.level.getGuess().getSecond(), this.level.getGuess().getThird());
                if (!this.level.isTippAlreadyIn(this.level.getVerifiers().get(index).getCheck(), index)) {
                    this.level.getRounds()[this.level.getCurRound()].getTipp()[index] = this.level.getVerifiers().get(index).getCheck();
                }
                getMainPanel().getButtonByFunction(function).setVisible(false);
                askedEnough();
            }
        } else {
            switch (function) {
                case Logic.FUNCTION_LOGIC_BACK:
                    quit();
                    break;
                case Logic.FUNCTION_GUESS_FIRST:
                    if (this.step != Step.QUESTION) {
                        this.level.getGuess().setFirst(nextGuess(this.level.getGuess().getFirst()));
                        getMainPanel().getButtonByFunction(function).setText(this.level.getGuess().getFirst() + "");
                    }
                    break;
                case Logic.FUNCTION_GUESS_SECOND:
                    if (this.step != Step.QUESTION) {
                        this.level.getGuess().setSecond(nextGuess(this.level.getGuess().getSecond()));
                        getMainPanel().getButtonByFunction(function).setText(this.level.getGuess().getSecond() + "");
                    }
                    break;
                case Logic.FUNCTION_GUESS_THIRD:
                    if (this.step != Step.QUESTION) {
                        this.level.getGuess().setThird(nextGuess(this.level.getGuess().getThird()));
                        getMainPanel().getButtonByFunction(function).setText(this.level.getGuess().getThird() + "");
                    }
                    break;
                case Logic.FUNCTION_GUESS_SOLUTION:
                    this.showResult();
                    break;
                case Logic.FUNCTION_STEP_TO_NEXT:
                    nextStep();
                    break;
                case Logic.FUNCTION_HELP:
                    ApoButtonCheckbox button = (ApoButtonCheckbox)getMainPanel().getButtonByFunction(function);
                    button.setChecked(!button.isChecked());
                    this.help = button.isChecked();
                    break;
            }
        }
    }

    private void showResult() {
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_SOLUTION).setVisible(false);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_SOLVED_NEXTROUND).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setVisible(false);
        this.showResult = !this.showResult;
        this.solved = this.level.getSolution().isSame(this.level.getGuess());
        this.level.fillLastCheck();
        this.solutionCard = new Card(this.level.getSolution().getCopy(), 0, 0, 180, 240, true);
    }

    private void askedEnough() {
        int count = 0;
        Boolean[] verifiers = this.level.getRounds()[this.level.getCurRound()].getVerifier();
        for (Boolean verify : verifiers) {
            if (verify != null) {
                count += 1;
            }
        }

        this.stillPossibleSolutions.clear();
        this.stillPossibleSolutions = new ApoSolver().getPossibleGuessesForTipps(this.level);

        if (count >= 3) {
            ArrayList<ApoButton> buttonByStartingFunction = this.getButtonByStartingFunction(FUNCTION_VERIFIER);
            for (int i = 0; i < buttonByStartingFunction.size() && i < this.level.getAmount(); i++) {
                buttonByStartingFunction.get(i).setVisible(false);
            }
            this.nextStep();
        }
    }

    private void nextStep() {
        if (this.step == Step.PROPOSOL) {
            ArrayList<ApoButton> buttonByStartingFunction = this.getButtonByStartingFunction(FUNCTION_VERIFIER);
            for (int i = 0; i < buttonByStartingFunction.size() && i < this.level.getAmount(); i++) {
                buttonByStartingFunction.get(i).setVisible(true);
            }
            this.level.getRounds()[this.level.getCurRound()].setGuess(this.level.getGuess());
            getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setText("End");
            getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setId("button_step_end");
        }
        if (this.step == Step.QUESTION) {
            ArrayList<ApoButton> buttonByStartingFunction = this.getButtonByStartingFunction(FUNCTION_VERIFIER);
            for (int i = 0; i < buttonByStartingFunction.size() && i < this.level.getAmount(); i++) {
                buttonByStartingFunction.get(i).setVisible(false);
            }
            if (this.level.getCurRound() >= 0) {
                getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_SOLUTION).setVisible(true);
            }

            getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setText("Verify");
            getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setId("button_step_verify");
            if (this.level.getCurRound() == Constants.MAX_ROUNDS) {
                getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setVisible(false);
            }
        }
        if (this.step == Step.END) {
            this.level.nextRound();
            getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_SOLUTION).setVisible(false);
            getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setText("Verify");
            getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setId("button_step_verify");
        }
        this.step = this.step.next(1);
        if (this.step == Step.PROPOSOL) {
            this.nextStep();
        }
    }

    private int nextGuess(int guess) {
        guess += 1;
        if (guess > 5) {
            guess = 1;
        }
        return guess;
    }

    private void setButtonsVisibility() {
        getMainPanel().getButtonByFunction(Logic.FUNCTION_LOGIC_BACK).setVisible(true);

        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_SOLUTION).setVisible(false);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_SOLVED_NEXTROUND).setVisible(false);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_MOUSE_OVER).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_HELP).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_STEP_TO_NEXT).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_FIRST).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_FIRST).setText("1");
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_SECOND).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_SECOND).setText("2");
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_THIRD).setVisible(true);
        getMainPanel().getButtonByFunction(Logic.FUNCTION_GUESS_THIRD).setText("3");

        this.help = ((ApoButtonCheckbox)(getMainPanel().getButtonByFunction(Logic.FUNCTION_HELP))).isChecked();

        setVisibleByStartingFunction(Logic.FUNCTION_FIRST, true);
        setVisibleByStartingFunction(Logic.FUNCTION_SECOND, true);
        setVisibleByStartingFunction(Logic.FUNCTION_THIRD, true);
        setVisibleByStartingFunction(Logic.FUNCTION_VERIFIER, false);
    }

    private void setVisibleByStartingFunction(String startingFunction, boolean visible) {
        ArrayList<ApoButton> buttonByStartingFunction = getButtonByStartingFunction(startingFunction);
        for (ApoButton apoButton : buttonByStartingFunction) {
            apoButton.setVisible(visible);
        }
    }

    public ArrayList<ApoButton> getButtonByStartingFunction(String function) {
        ArrayList<ApoButton> buttons = new ArrayList<>();
        for (ApoButton apoButton : getMainPanel().getButtons()) {
            if (apoButton.getFunction().startsWith(function)) {
                buttons.add(apoButton);
                if (apoButton instanceof ApoButtonCheckbox) {
                    ((ApoButtonCheckbox) (apoButton)).setChecked(false);
                }
            }
        }
        return buttons;
    }

    public void mouseWheelChanged(int changed) {
    }

    @Override
    protected void quit() {
        removeButtonsForVerifeyers();
        getMainPanel().changeToMenu();
    }

    @Override
    public void doThink(float delta) {

    }

    @Override
    public void render() {
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);


        this.fillVerifiers(width, height, startX, startY);

        int startYNotes = 50;
        this.renderIcon(2 * width + 2 * startX + 115, startYNotes - 20, 30, 30, 25);

        this.renderIcon(Constants.GAME_WIDTH - 310 + 55, startYNotes - 10, 20, 20, 10);

        for (int i = 0; i < this.level.getRounds().length; i++) {
            if (this.level.getRounds()[i].getVerifier() != null) {
                for (int v = 0; v < this.level.getAmount(); v++) {
                    if (this.level.getRounds()[i].getVerifier()[v] != null) {
                        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], Constants.COLOR_BUTTONS_DARK[3]);
                        if (this.level.getRounds()[i].getVerifier()[v]) {
                            getMainPanel().getRenderer().rectLine(Constants.GAME_WIDTH - 180 + v*30 - 2, startYNotes + (i+1)*20 + 15, Constants.GAME_WIDTH - 180 + v*30 + 6, startYNotes + (i+1)*20 + 20, 3);
                            getMainPanel().getRenderer().rectLine(Constants.GAME_WIDTH - 180 + v*30 + 14, startYNotes + (i+1)*20 + 3, Constants.GAME_WIDTH - 180 + v*30 + 6, startYNotes + (i+1)*20 + 20, 3);
                        } else {
                            getMainPanel().getRenderer().rectLine(Constants.GAME_WIDTH - 180 + v*30 - 2, startYNotes + (i+1)*20 + 2, Constants.GAME_WIDTH - 180 + v*30 + 14, startYNotes + (i+1)*20 + 18, 3);
                            getMainPanel().getRenderer().rectLine(Constants.GAME_WIDTH - 180 + v*30 + 14, startYNotes + (i+1)*20 + 2, Constants.GAME_WIDTH - 180 + v*30 - 2, startYNotes + (i+1)*20 + 18, 3);
                        }
                    }
                }
            }
        }

        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], Constants.COLOR_BUTTONS_DARK[3]);
        getMainPanel().getRenderer().rect(Constants.GAME_WIDTH - 310, startYNotes - 10, 2, 270);
        getMainPanel().getRenderer().rect(Constants.GAME_WIDTH - 205, startYNotes - 10, 2, 270);
        getMainPanel().getRenderer().rect(Constants.GAME_WIDTH - 330, startYNotes + 15, 310, 2);

        getMainPanel().getRenderer().rect(0, 160, 810, 2);
        getMainPanel().getRenderer().rect(810, 0, 2, Constants.GAME_HEIGHT);

        getMainPanel().getRenderer().rect(0, 0, 2, Constants.GAME_HEIGHT);
        getMainPanel().getRenderer().rect(Constants.GAME_WIDTH - 2, 0, 2, Constants.GAME_HEIGHT);
        getMainPanel().getRenderer().rect(0, 0, Constants.GAME_WIDTH, 2);
        getMainPanel().getRenderer().rect(0, Constants.GAME_HEIGHT - 2, Constants.GAME_WIDTH, 2);

        if (getMainPanel().getButtonByFunction(Logic.FUNCTION_MOUSE_OVER).isBOver()) {
            getMainPanel().getRenderer().setColor(Constants.COLOR_BACKGROUND[0], Constants.COLOR_BACKGROUND[1], Constants.COLOR_BACKGROUND[2], 1f);
            getMainPanel().getRenderer().roundedRect(30, 40, 745, 80, 20);
        }

        final int startXVerifier = 815;
        final int startYVerifier = startYNotes + 280;
        int x = startXVerifier;
        int y = startYVerifier;
        final int widthVerifier = (Constants.GAME_WIDTH - startXVerifier - 10) / 2;
        final int heightVerifier = (400) / 3;
        if (this.help) {
            int addY;

            for (int index = 0; index < this.level.getVerifiers().size(); index++) {
                addY = 25;
                for (int i = 0; i < this.level.getRounds().length; i++) {
                    if (this.level.getRounds()[i] != null && this.level.getRounds()[i].getTipp()[index] != null && this.level.getRounds()[i].getTipp()[index].length() > 0) {
                        this.level.getVerifiers().get(index).renderFillExtraInformation(getMainPanel(), x + 10, y + 10 + addY, this.level.getRounds()[i].getTipp()[index]);
                        addY += 20;
                    }
                }

                x += widthVerifier;
                if (index % 2 != 0) {
                    x = startXVerifier;
                    y += heightVerifier;
                }
            }
        }

        getMainPanel().getRenderer().end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Line);
        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], Constants.COLOR_BUTTONS_DARK[3]);
        getMainPanel().getRenderer().rect(2 * width + 2 * startX + 30, startYNotes - 30, 3 * 55 + 5, 300);
        getMainPanel().getRenderer().rect(Constants.GAME_WIDTH - 340, startYNotes - 30, 330, 300);

        x = startXVerifier;
        y = startYVerifier;
        if (this.help) {
            for (int index = 0; index < this.level.getVerifiers().size(); index++) {
                getMainPanel().getRenderer().rect(x, y, widthVerifier - 5, heightVerifier - 5);

                x += widthVerifier;
                if (index % 2 != 0) {
                    x = startXVerifier;
                    y += heightVerifier;
                }
            }
        }

        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();

        getMainPanel().drawString(Localization.getInstance().getCommon().get("game_round")+" " + (this.level.getCurRound()+1), 10, 10, Constants.COLOR_BUTTONS_DARK, AssetLoader.font25, DrawString.BEGIN, false, false);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("game_step")+" " + Localization.getInstance().getCommon().get(this.step.getId()), 550, 10, Constants.COLOR_BUTTONS_DARK, AssetLoader.font25, DrawString.BEGIN, false, false);

        this.fillTextVerifiers(width, height, startX, startY);

        getMainPanel().drawString("R", Constants.GAME_WIDTH - 330, startYNotes - 5, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.BEGIN, false, false);
        for (int i = 1; i <= 12; i++) {
            getMainPanel().drawString(""+i, Constants.GAME_WIDTH - 330, startYNotes + 5 + i*20, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }

        for (int i = 65; i < 65 + this.level.getAmount(); i++) {
            getMainPanel().drawString(String.valueOf((char)(i)), Constants.GAME_WIDTH - 180 + (i-65)*30, startYNotes - 5, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.BEGIN, false, false);
        }

        for (int i = 0; i < this.level.getRounds().length; i++) {
            if (this.level.getRounds()[i].getGuess() != null) {
                getMainPanel().drawString(""+this.level.getRounds()[i].getGuess().getFirst(), Constants.GAME_WIDTH - 290, startYNotes + 5 + (i+1)*20, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.MIDDLE, false, false);
                getMainPanel().drawString(""+this.level.getRounds()[i].getGuess().getSecond(), Constants.GAME_WIDTH - 260, startYNotes + 5 + (i+1)*20, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.MIDDLE, false, false);
                getMainPanel().drawString(""+this.level.getRounds()[i].getGuess().getThird(), Constants.GAME_WIDTH - 230, startYNotes + 5 + (i+1)*20, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.MIDDLE, false, false);
            }
        }

        if (this.help) {
            x = startXVerifier;
            y = startYVerifier;
            int addY;
            for (int index = 0; index < this.level.getVerifiers().size(); index++) {
                getMainPanel().drawString(Localization.getInstance().getCommon().get("verifier") + " " + (char) (index + 65), x + 5, y + 10, Constants.COLOR_BUTTONS_DARK, AssetLoader.font25, DrawString.BEGIN, false, false);
                addY = 25;
                for (int i = 0; i < this.level.getRounds().length; i++) {
                    if (this.level.getRounds()[i] != null && this.level.getRounds()[i].getTipp()[index] != null && this.level.getRounds()[i].getTipp()[index].length() > 0) {
                        this.level.getVerifiers().get(index).renderTextExtraInformation(getMainPanel(), x + 10, y + 10 + addY, this.level.getRounds()[i].getTipp()[index]);
                        //getMainPanel().drawString(this.level.getRounds()[i].getTipp()[index], x + 10, y + 10 + addY, Constants.COLOR_BUTTONS_DARK, AssetLoader.font15, DrawString.BEGIN, false, false);
                        addY += 20;
                    }
                }

                x += widthVerifier;
                if (index % 2 != 0) {
                    x = startXVerifier;
                    y += heightVerifier;
                }
            }
        }

        String yourCode = Localization.getInstance().getCommon().get("chose_your_code");
        if (this.step == Step.QUESTION) {
            yourCode = Localization.getInstance().getCommon().get("your_code");
        }
        getMainPanel().drawString(yourCode, 402, 50, Constants.COLOR_BUTTONS_DARK, AssetLoader.font30, DrawString.MIDDLE, false, false);

        if (this.help) {
            ApoButton menuButton = getMainPanel().getButtonByFunction(FUNCTION_LOGIC_BACK);
            ApoButton helpButton = getMainPanel().getButtonByFunction(FUNCTION_HELP);
            float centerX = menuButton.getX() + (helpButton.getX() + helpButton.getWidth() - menuButton.getX()) / 2f + 20;
            getMainPanel().drawString(Localization.getInstance().getCommon().get("still_possible") + this.stillPossibleSolutions.size(), centerX, Constants.GAME_HEIGHT - 30, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }

        getMainPanel().spriteBatch.end();

        for (ApoButton button : this.getMainPanel().getButtons()) {
            button.render(this.getMainPanel());
        }

        if (getMainPanel().getButtonByFunction(Logic.FUNCTION_MOUSE_OVER).isBOver()) {
            renderMouseOver();
        }

        if (this.showResult) {
            renderResult();
        }
    }

    private void drawThickLine(float x1, float y1, float x2, float y2, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        float nx = -dy / len;
        float ny = dx / len;
        float halfThickness = thickness / 2f;

        float xOffset = nx * halfThickness;
        float yOffset = ny * halfThickness;

        // 4 Ecken des Rechtecks berechnen
        float[] verts = new float[]{
                x1 + xOffset, y1 + yOffset,
                x1 - xOffset, y1 - yOffset,
                x2 - xOffset, y2 - yOffset,
                x2 + xOffset, y2 + yOffset
        };

        getMainPanel().getRenderer().triangle(verts[0], verts[1], verts[2], verts[3], verts[4], verts[5]);
        getMainPanel().getRenderer().triangle(verts[0], verts[1], verts[4], verts[5], verts[6], verts[7]);
    }

    private void renderMouseOver() {
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        getMainPanel().getRenderer().setColor(Constants.COLOR_BACKGROUND[0], Constants.COLOR_BACKGROUND[1], Constants.COLOR_BACKGROUND[2], 1f);
        getMainPanel().getRenderer().roundedRect(30, 40, 745, 55, 20);
        getMainPanel().getRenderer().end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Line);
        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], 1f);
        getMainPanel().getRenderer().roundedRectLine(30, 40, 745, 55, 20);
        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();
        if (this.step == Step.PROPOSOL) {
            getMainPanel().drawString(Localization.getInstance().getCommon().get("step_proposol_help"), 425, 57, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        } else if (this.step == Step.QUESTION) {
            getMainPanel().drawString(Localization.getInstance().getCommon().get("step_question_help"), 425, 57, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        } else if (this.step == Step.END) {
            String[] result = Localization.getInstance().getCommon().get("step_end_help").split(";");
            getMainPanel().drawString(result[0], 425, 70, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
            getMainPanel().drawString(result[1], 425, 45, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }
        getMainPanel().spriteBatch.end();
    }

    public void renderIcon(int startX, int startY, int width, int height, int addWidth) {
        IconDraw.renderIcon(getMainPanel(), startX - width/2 - width - addWidth, startY, width, height, 1);
        IconDraw.renderIcon(getMainPanel(), startX - width/2, startY, width, height, 2);
        IconDraw.renderIcon(getMainPanel(), startX + width/2 + addWidth, startY, width, height, 3);
    }

    private void fillTextVerifiers(int width, int height, int startX, int startY) {
        int checkX = startX;
        int checkY = startY;
        for (Verifier verifier : this.level.getVerifiers()) {
            verifier.renderAllText(getMainPanel(), checkX, checkY);

            checkX += width;
            if (checkX > 2 * width) {
                checkX = startX;
                checkY += height;
            }
        }
    }

    private void fillVerifiers(int width, int height, int startX, int startY) {
        int checkX = startX;
        int checkY = startY;
        int index = 0;
        int start = 30;
        for (Verifier verifier : this.level.getVerifiers()) {
            verifier.renderFill(getMainPanel(), checkX, checkY, true);
            if (this.level.getRounds()[this.level.getCurRound()].getVerifier()[index] != null) {
                IconDraw.renderSolutionCheck(getMainPanel(), this.level.getRounds()[this.level.getCurRound()].getVerifier()[index], checkX + start, checkY + 35, 40, 40);
            }

            checkX += width;
            if (checkX > 2 * width) {
                checkX = startX;
                checkY += height;
            }
            index += 1;
        }
    }

    private void renderResult() {
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        final int completeWidth = 850;
        final int completeHeight = 680;
        final float startX = Constants.GAME_WIDTH/2f - 425;
        final float startY = Constants.GAME_HEIGHT/2f - 290;
        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS[0], Constants.COLOR_BUTTONS[1], Constants.COLOR_BUTTONS[2], 1f);
        if (!this.solved) {
            getMainPanel().getRenderer().setColor(Constants.COLOR_RED_LIGHT[0], Constants.COLOR_RED_LIGHT[1], Constants.COLOR_RED_LIGHT[2], 1f);
        }
        getMainPanel().getRenderer().roundedRect(startX, startY, completeWidth, completeHeight, 20);

        for (int i = 1; i < 4; i++) {
            getMainPanel().getRenderer().setColor(Constants.COLOR_NUMBER_THREE[0], Constants.COLOR_NUMBER_THREE[1], Constants.COLOR_NUMBER_THREE[2], 1f);
            if (i == 1) {
                getMainPanel().getRenderer().setColor(Constants.COLOR_NUMBER_ONE[0], Constants.COLOR_NUMBER_ONE[1], Constants.COLOR_NUMBER_ONE[2], 1f);
            } else if (i == 2) {
                getMainPanel().getRenderer().setColor(Constants.COLOR_NUMBER_TWO[0], Constants.COLOR_NUMBER_TWO[1], Constants.COLOR_NUMBER_TWO[2], 1f);
            }
            getMainPanel().getRenderer().roundedRect(Constants.GAME_WIDTH/2f - 96 + (i-1)*64, Constants.GAME_HEIGHT/2f - 20, 64, 64, 10);
            getMainPanel().getRenderer().rect(Constants.GAME_WIDTH/2f - 96 + (i-1)*64, Constants.GAME_HEIGHT/2f + 64 - 40, 64, 20);

            IconDraw.renderIcon(getMainPanel(), (int)(Constants.GAME_WIDTH/2f - 96 + (i-1)*64 + 22 + 15), (int)(Constants.GAME_HEIGHT/2f - 15), (int)(22), (int)(22), i);
        }

        float x = startX + 5;
        float y = startY + 54 + 280;
        float width = Constants.GAME_WIDTH/2f - x;
        float height = 90;
        for (int i = 0; i < this.level.getVerifiers().size(); i++) {
            getMainPanel().getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], 1f);
            getMainPanel().getRenderer().roundedRect(x - 2, y, width, height, 10);

            this.level.getVerifiers().get(i).renderFill(getMainPanel(), (int) (x), (int)(y - 28), false);

            IconDraw.renderSolutionCheck(getMainPanel(), this.level.getLastChecks()[i], (int)(x + 30), (int)(y + 25), 40, 40);

            x += width + 5;
            if (i % 2 == 1) {
                x = startX + 5;
                y += height + 5;
            }
        }

        getMainPanel().getRenderer().end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Line);
        getMainPanel().getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], 1f);
        if (!this.solved) {
            getMainPanel().getRenderer().setColor(Constants.COLOR_RED_DARK[0], Constants.COLOR_RED_DARK[1], Constants.COLOR_RED_DARK[2], 1f);
        }
        getMainPanel().getRenderer().roundedRectLine(startX, startY, completeWidth, completeHeight, 20);
        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();

        float[] color = Constants.COLOR_BUTTONS_DARK;
        String s = Localization.getInstance().getCommon().get("solution_congratulation");
        if (!this.solved) {
            s = Localization.getInstance().getCommon().get("solution_wrong");
            color = Constants.COLOR_RED_DARK;
        }

        BitmapFont font = AssetLoader.font30;
        getMainPanel().getGlyphLayout().setText(font, s);
        if (getMainPanel().getGlyphLayout().width > 300) {
            font = AssetLoader.font25;
        }
        getMainPanel().drawString(s, Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - 260, color, font, DrawString.MIDDLE, false, false);

        getMainPanel().drawString(Localization.getInstance().getCommon().get("solution_guess"), Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - 60, Constants.COLOR_BUTTONS_DARK, AssetLoader.font30, DrawString.MIDDLE, false, false);

        if (this.solved) {
            Localization loc = Localization.getInstance();
            int rounds = this.level.getCurRound() + 1;
            int checks = this.level.getVerifyChecks();
            String roundWord = loc.plural(rounds, "solution_congratulation_detail_2_single", "solution_congratulation_detail_2");
            String checkWord = loc.plural(checks, "solution_congratulation_detail_4_single", "solution_congratulation_detail_4");
            getMainPanel().drawString(loc.getCommon().get("solution_congratulation_detail_1") + " " + rounds + " " + roundWord, Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - 210, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
            getMainPanel().drawString(loc.getCommon().get("solution_congratulation_detail_3") + " " + checks + " " + checkWord, Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - 180, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        } else {
            getMainPanel().drawString(Localization.getInstance().getCommon().get("solution_wrong_detail_1"), Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - 210, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
            getMainPanel().drawString(Localization.getInstance().getCommon().get("solution_wrong_detail_2"), Constants.GAME_WIDTH / 2f, Constants.GAME_HEIGHT / 2f - 180, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }

        for (int i = 0; i < 3; i++) {
            s = "" + this.level.getGuess().getThird();
            if (i == 0) {
                s = "" + this.level.getGuess().getFirst();
            } else if (i == 1) {
                s = "" + this.level.getGuess().getSecond();
            }
            getMainPanel().drawString(s, (int) (Constants.GAME_WIDTH/2f - 96 + (i)*64 + 5), (int)(Constants.GAME_HEIGHT/2f + 22), Constants.COLOR_WHITE, AssetLoader.font40, DrawString.BEGIN, true, false);
        }

        x = startX + 5;
        y = startY + 64 + 280;
        for (int i = 0; i < this.level.getVerifiers().size(); i++) {
            this.level.getVerifiers().get(i).renderTextBig(getMainPanel(), (int) (x), (int)(y - 40));

            x += width + 5;
            if (i % 2 == 1) {
                x = startX + 5;
                y += height + 5;
            }
        }

        getMainPanel().drawString(Localization.getInstance().getCommon().get("solution_solution"), startX + 15 + this.solutionCard.getWidth()/2f, startY + 30, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
        getMainPanel().drawString(Localization.getInstance().getCommon().get("solution_solution"), startX + completeWidth - 15 - this.solutionCard.getWidth()/2f, startY + 30, Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);

        getMainPanel().spriteBatch.end();

        this.solutionCard.render(getMainPanel(), (int)(startX + 25), (int)(startY + 50));
        this.solutionCard.render(getMainPanel(), (int)(startX + completeWidth - 25 - this.solutionCard.getWidth()), (int)(startY + 50));

        getMainPanel().getButtonByFunction(FUNCTION_SOLVED_NEXTROUND).render(this.getMainPanel());
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
