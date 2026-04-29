/*
 * Copyright (c) 2005-2013 Dirk Aporius <dirk.aporius@gmail.com>
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

package com.apogames.logic.game;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.entity.ApoButton;
import com.apogames.logic.entity.ApoButtonCheckbox;
import com.apogames.logic.entity.ApoButtonColor;
import com.apogames.logic.entity.ApoButtonTuringNumber;
import com.apogames.logic.game.logic.Logic;
import com.apogames.logic.game.menu.Menu;
import com.apogames.logic.game.tutorial.Tutorial;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ButtonProvider {
	
	private final MainPanel game;
	
	public ButtonProvider(MainPanel game) {
		this.game = game;
	}

	public void init() {
		if ((this.game.getButtons() == null) || (this.game.getButtons().size() <= 0)) {
			this.game.getButtons().clear();
			
			BitmapFont font = AssetLoader.font25;
			String text = "X";
			String function = Menu.FUNCTION_BACK;
			int width = 64;
			int height = 64;
			int x = Constants.GAME_WIDTH - width - 15;
			int y = Constants.GAME_HEIGHT - height - 5;
			ApoButton button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "X";
			function = Logic.FUNCTION_LOGIC_BACK;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH - width - 15;
			y = Constants.GAME_HEIGHT - height - 5;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "Play";
			function = Menu.FUNCTION_PLAY;
			width = 164;
			height = 80;
			x = Constants.GAME_WIDTH/2 - width/2;
			y = Constants.GAME_HEIGHT/2 + 130 + 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE, "button_play");
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "Tutorial";
			function = Menu.FUNCTION_TUTORIAL;
			width = 164;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width/2;
			y = Constants.GAME_HEIGHT/2 + 130 + 50 + 80 + 25;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE, "button_tutorial");
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "<";
			function = Menu.FUNCTION_VERIFIER_LEFT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width - 100;
			y = Constants.GAME_HEIGHT/2 + 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = ">";
			function = Menu.FUNCTION_VERIFIER_RIGHT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + 100;
			y = Constants.GAME_HEIGHT/2 + 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "<";
			function = Menu.FUNCTION_DIFFICULTY_LEFT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 - width - 100;
			y = Constants.GAME_HEIGHT/2 - 150 + 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = ">";
			function = Menu.FUNCTION_DIFFICULTY_RIGHT;
			width = 64;
			height = 64;
			x = Constants.GAME_WIDTH/2 + 100;
			y = Constants.GAME_HEIGHT/2 - 150 + 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE);
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);


			String startFunction = Logic.FUNCTION_FIRST;
			for (int number = 1; number <= 3; number++) {
				if (number == 2) {
					startFunction = Logic.FUNCTION_SECOND;
				} else if (number == 3) {
					startFunction = Logic.FUNCTION_THIRD;
				}
				for (int count = 1; count <= 5; count += 1) {
					text = ""+count;
					function = startFunction+"_"+count;
					width = 40;
					height = 40;
					x = 850 + (number - 1) * (width + 15);
					y = 72 + (count - 1) * (height + 10);
					button = new ApoButtonCheckbox(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK);
					//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
					button.setStroke(1);
					button.setFont(AssetLoader.font25);
					this.game.getButtons().add(button);
				}
			}

			text = "1";
			function = Logic.FUNCTION_GUESS_FIRST;
			width = 80;
			height = 80;
			x = 400 - width/2 - width;
			y = 160 - height;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_ONE, Constants.COLOR_WHITE, 1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "2";
			function = Logic.FUNCTION_GUESS_SECOND;
			x = 400 - width/2;
			y = 160 - height;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_TWO, Constants.COLOR_WHITE, 2);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "3";
			function = Logic.FUNCTION_GUESS_THIRD;
			x = 400 + width/2;
			y = 160 - height;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_THREE, Constants.COLOR_WHITE, 3);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "Solve";
			function = Logic.FUNCTION_GUESS_SOLUTION;
			x = 10;
			y = 100;
			width = 150;
			height = 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK, "button_solve");
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "Next";
			function = Logic.FUNCTION_STEP_TO_NEXT;
			x = 645;
			y = 100;
			width = 150;
			height = 50;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK, "button_next");
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			startFunction = Logic.FUNCTION_VERIFIER;
			int checkX = 5;
			int checkY = 170;
			for (int count = 1; count <= 6; count += 1) {
				text = "Verify";
				function = startFunction+"_"+count;
				width = 80;
				height = 40;
				x = checkX + 10;
				y = checkY + 35;
				button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_VERIFIER_BACKGROUND_LIGHT, Constants.COLOR_BACKGROUND, "button_verify");
				//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
				button.setStroke(1);
				button.setFont(AssetLoader.font20);
				this.game.getButtons().add(button);
				checkX += 400;
				if (checkX > 800) {
					checkX = 5;
					checkY += 200;
				}
			}

			text = "New Game";
			function = Logic.FUNCTION_SOLVED_NEXTROUND;
			width = 200;
			height = 50;
			x = Constants.GAME_WIDTH/2 - width/2;
			y = Constants.GAME_HEIGHT/2 + 335;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE, "button_newgame");
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "i";
			function = Logic.FUNCTION_MOUSE_OVER;
			width = 30;
			height = 30;
			x = 550 - width - 10;
			y = 5;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_BUTTONS_DARK);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			text = "              help";
			function = Logic.FUNCTION_HELP;
			width = 40;
			height = 40;
			x = 820;
			y = Constants.GAME_HEIGHT - height - 5;
			button = new ApoButtonCheckbox(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK, "button_help");
			//ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BACKGROUND, Constants.COLOR_WHITE);
			((ApoButtonCheckbox)(button)).setChecked(true);
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			((ApoButtonCheckbox)(button)).setShowMiddle(false);
			this.game.getButtons().add(button);

			text = "1";
			function = Menu.FUNCTION_GUESS_ONE_FIRST;
			width = 80;
			height = 80;
			x = 0;
			y = 0;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_ONE, Constants.COLOR_WHITE, 1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "2";
			function = Menu.FUNCTION_GUESS_ONE_SECOND;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_TWO, Constants.COLOR_WHITE, 2);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "3";
			function = Menu.FUNCTION_GUESS_ONE_THIRD;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_THREE, Constants.COLOR_WHITE, 3);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "1";
			function = Menu.FUNCTION_GUESS_TWO_FIRST;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_ONE, Constants.COLOR_WHITE, 1);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "2";
			function = Menu.FUNCTION_GUESS_TWO_SECOND;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_TWO, Constants.COLOR_WHITE, 2);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "3";
			function = Menu.FUNCTION_GUESS_TWO_THIRD;
			button = new ApoButtonTuringNumber(x, y, width, height, function, text, Constants.COLOR_NUMBER_THREE, Constants.COLOR_WHITE, 3);
			button.setFont(AssetLoader.font40);
			this.game.getButtons().add(button);

			text = "de";
			function = Menu.FUNCTION_LANGUAGE;
			width = 64;
			height = 64;
			x = 15;
			y = Constants.GAME_HEIGHT - height - 5;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS, Constants.COLOR_BUTTONS_DARK);
			button.setFont(AssetLoader.font30);
			this.game.getButtons().add(button);

			text = "Next";
			function = Tutorial.FUNCTION_TUTORIAL_NEXT;
			width = 220;
			height = 50;
			x = 0;
			y = 0;
			button = new ApoButtonColor(x, y, width, height, function, text, Constants.COLOR_BUTTONS_DARK, Constants.COLOR_WHITE, "button_tutorial_next");
			button.setStroke(1);
			button.setFont(AssetLoader.font25);
			this.game.getButtons().add(button);

			for (int i = 0; i < this.game.getButtons().size(); i++) {
				this.game.getButtons().get(i).setBOpaque(false);
			}
		}
	}
}
