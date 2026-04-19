/*
 * Copyright (c) 2005-2020 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.apogames.logic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * The type Asset loader.
 */
public class AssetLoader {

	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789äöüÄÖÜß!\\\"§$%&/()=?´`*+#-_.:,;<>|^";

	public static BitmapFont font40;
	public static BitmapFont font20;
	public static BitmapFont font10;
	public static BitmapFont font15;
	public static BitmapFont font12;
	public static BitmapFont font25;
	public static BitmapFont font30;

	public static void load() {
		font40 = new BitmapFont(Gdx.files.internal("fonts/pirate40.fnt"), Gdx.files.internal("fonts/pirate40.png"), true);
		font20 = new BitmapFont(Gdx.files.internal("fonts/pirate20.fnt"), Gdx.files.internal("fonts/pirate20.png"), true);
		font15 = new BitmapFont(Gdx.files.internal("fonts/pirate15.fnt"), Gdx.files.internal("fonts/pirate15.png"), true);
		font10 = new BitmapFont(Gdx.files.internal("fonts/pirate10.fnt"), Gdx.files.internal("fonts/pirate10.png"), true);
		font12 = new BitmapFont(Gdx.files.internal("fonts/pirate12.fnt"), Gdx.files.internal("fonts/pirate12.png"), true);
		font25 = new BitmapFont(Gdx.files.internal("fonts/pirate25.fnt"), Gdx.files.internal("fonts/pirate25.png"), true);
		font30 = new BitmapFont(Gdx.files.internal("fonts/pirate30.fnt"), Gdx.files.internal("fonts/pirate30.png"), true);

//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans.ttf"));
//		//font40 = getFontForSize(generator, 40);
//		//font20 = getFontForSize(generator, 20);
//		//font15 = getFontForSize(generator, 15);
//		//font25 = getFontForSize(generator, 25);
//		//font30 = getFontForSize(generator, 30);
//		font12 = getFontForSize(generator, 12);
//		generator.dispose(); // avoid memory leaks, important
	}

//	public static BitmapFont getFontForSize(FreeTypeFontGenerator generator, int size) {
//		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//		parameter.size = size; // font size
//		parameter.characters = FONT_CHARACTERS;
//		//parameter.borderWidth = 1;
//		parameter.flip = true;
//		return generator.generateFont(parameter);
//	}

	public static void dispose() {
		font40.dispose();
		font30.dispose();
		font25.dispose();
		font20.dispose();
		font15.dispose();
		font12.dispose();
		font10.dispose();
//        click.dispose();
	}

}

