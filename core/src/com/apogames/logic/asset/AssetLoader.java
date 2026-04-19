/*
 * Copyright (c) 2005-2020 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 */

package com.apogames.logic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * The type Asset loader.
 */
public class AssetLoader {

	public static final String FONT_CHARACTERS = FreeTypeFontGenerator.DEFAULT_CHARS
			+ "\u00C4\u00D6\u00DC\u00E4\u00F6\u00FC\u00DF"
			+ "\u00C0\u00C1\u00C2\u00C3\u00C5\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD\u00CE\u00CF"
			+ "\u00D0\u00D1\u00D2\u00D3\u00D4\u00D5\u00D7\u00D8\u00D9\u00DA\u00DB\u00DD\u00DE"
			+ "\u00E0\u00E1\u00E2\u00E3\u00E5\u00E6\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED\u00EE\u00EF"
			+ "\u00F0\u00F1\u00F2\u00F3\u00F4\u00F5\u00F7\u00F8\u00F9\u00FA\u00FB\u00FD\u00FE\u00FF"
			+ "\u2013\u2014\u2018\u2019\u201C\u201D\u201E\u2026\u20AC";

	public static BitmapFont font10;
	public static BitmapFont font12;
	public static BitmapFont font15;
	public static BitmapFont font20;
	public static BitmapFont font25;
	public static BitmapFont font30;
	public static BitmapFont font40;

	public static void load() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"));
		font10 = generateFont(generator, 10);
		font12 = generateFont(generator, 12);
		font15 = generateFont(generator, 15);
		font20 = generateFont(generator, 20);
		font25 = generateFont(generator, 25);
		font30 = generateFont(generator, 30);
		font40 = generateFont(generator, 40);
		generator.dispose();
	}

	private static BitmapFont generateFont(FreeTypeFontGenerator generator, int size) {
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = size;
		param.flip = true;
		param.characters = FONT_CHARACTERS;
		param.hinting = FreeTypeFontGenerator.Hinting.Full;
		param.minFilter = Texture.TextureFilter.Linear;
		param.magFilter = Texture.TextureFilter.Linear;
		param.genMipMaps = true;
		return generator.generateFont(param);
	}

	public static void dispose() {
		font40.dispose();
		font30.dispose();
		font25.dispose();
		font20.dispose();
		font15.dispose();
		font12.dispose();
		font10.dispose();
	}
}
