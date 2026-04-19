package com.apogames.logic;

import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.Game;
import com.apogames.logic.game.MainPanel;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class LogicGame extends Game {

	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new MainPanel());
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

	public void resume() {
		super.resume();
		AssetLoader.load();
	}
}
