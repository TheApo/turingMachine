package com.apogames.logic.entity;

import com.apogames.logic.backend.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class ApoButtonOnlyX extends ApoButton {

	private float[] color;
	private float[] colorBorder;
	
	public ApoButtonOnlyX(int x, int y, int width, int height, String function, float[] color, float[] colorBorder) {
		this(x, y, width, height, function, "", color, colorBorder);
	}
	
	public ApoButtonOnlyX(int x, int y, int width, int height, String function, String text, float[] color, float[] colorBorder) {
		super(x, y, width, height, function, text);
		this.color = color;
		this.colorBorder = colorBorder;		
	}

	/**
	 * malt den Button an die Stelle getX() + changeX und getY() + changeY hin
	 * @param changeX: Verschiebung in x-Richtung
	 * @param changeY: Verschiebung in y-Richtung
	 */
	public void render(GameScreen screen, int changeX, int changeY ) {
		if (this.isVisible()) {
			if (!this.isOnlyText()) {
				if (this.isSelect()) {
					float myStartX = this.getX() + this.getWidth() / 2f - this.getHeight() / 2f;
					float myWidth = this.getHeight();
					float myHeight = this.getHeight();
					screen.getRenderer().begin(ShapeType.Filled);
					screen.getRenderer().setColor(color[0], color[1], color[2], color[3]);
					screen.getRenderer().rectLine(myStartX + changeX, this.getY() + changeY, myStartX + myWidth + changeX, this.getY() + myHeight + changeY, 3);
					screen.getRenderer().rectLine(myStartX + myWidth + changeX, this.getY() + changeY, myStartX + changeX, this.getY() + myHeight + changeY, 3);
					screen.getRenderer().end();
				}
				if (this.isBOver()) {
					screen.getRenderer().begin(ShapeType.Line);
					screen.getRenderer().setColor(colorBorder[0], colorBorder[1], colorBorder[2], colorBorder[3]);
					screen.getRenderer().roundedRectLine(this.getX() + 2 + changeX, this.getY() + changeY, getWidth() - 4 + changeX, this.getHeight(), 5);
					screen.getRenderer().end();
				}
			}
		}
	}
	
}
