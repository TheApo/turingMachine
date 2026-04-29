package com.apogames.logic.game;

import com.apogames.logic.Constants;
import com.apogames.logic.backend.GameScreen;
import com.apogames.logic.backend.ScreenModel;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.logic.Logic;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.menu.Menu;
import com.apogames.logic.game.tutorial.Tutorial;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;


public class MainPanel extends GameScreen {

    private Logic game;
    private Menu menu;
    private Tutorial tutorial;

    private FPSLogger logger = new FPSLogger();

    public MainPanel() {
        super();
        if ((this.getButtons() == null) || (this.getButtons().size() <= 0)) {
            ButtonProvider button = new ButtonProvider(this);
            button.init();
        }

        Gdx.graphics.setContinuousRendering(false);
        Localization.getInstance().setLocale(Constants.REGION);

        if (this.game == null) {
            this.game = new Logic(this);
        }
        if (this.menu == null) {
            this.menu = new Menu(this);
        }
        if (this.tutorial == null) {
            this.tutorial = new Tutorial(this);
        }

        this.changeToMenu();
    }

    public final void changeToGame(int amount, Difficulty difficulty) {
        this.game.setAmount(amount);
        this.game.setDifficulty(difficulty);
        changeModel(game);
    }

    public void changeToMenu() {
        this.changeModel(this.menu);
    }

    public void changeToTutorial() {
        this.changeModel(this.tutorial);
    }

    /**
     * Quit game.
     */
    public final void quitGame() {
        this.saveProperties();
        Gdx.app.exit();
    }

    /**
     * Update level chooser.
     */
    public void saveProperties() {
    }

    private void changeModel(final ScreenModel model) {
        if (this.model != null) {
            this.model.dispose();
        }

        this.model = model;

        this.setButtonsInvisible();
        this.model.setNeededButtonsVisible();
        this.model.init();
    }
    
    public final void setButtonsInvisible() {
    	for (int i = 0; i < this.getButtons().size(); i++) {
            this.getButtons().get(i).setVisible(false);
        }
    }

    public void think(final float delta) {
        super.think(delta);
        if (model != null) model.think(delta);
    }

    public void render(float delta) {
        super.render(delta);

        if (model != null) {
            model.render();
            model.drawOverlay();
        }
    }

    public void renderBackground() {
        this.getRenderer().begin(ShapeType.Filled);
        this.getRenderer().setColor(Constants.COLOR_BACKGROUND[0], Constants.COLOR_BACKGROUND[1], Constants.COLOR_BACKGROUND[2], 1);
        this.getRenderer().rect(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        this.getRenderer().end();
    }

}
