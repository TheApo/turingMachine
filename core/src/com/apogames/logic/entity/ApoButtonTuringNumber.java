package com.apogames.logic.entity;

import com.apogames.logic.backend.DrawString;
import com.apogames.logic.backend.GameScreen;
import com.apogames.logic.game.logic.IconDraw;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * The type Apo button color.
 */
public class ApoButtonTuringNumber extends ApoButtonColor {

	private final int icon;

    public ApoButtonTuringNumber(int x, int y, int width, int height, String function, String text, float[] color, float[] colorText, int icon) {
		super(x, y, width, height, function, text, color, colorText);
		this.icon = icon;
	}

	public int getIcon() {
		return icon;
	}

	public void render(GameScreen screen, int changeX, int changeY ) {
		if ( this.isVisible() ) {
			if (!this.isOnlyText()) {
				screen.getRenderer().begin(ShapeType.Filled);
				screen.getRenderer().setColor(getColor()[0], getColor()[1], getColor()[2], getColor()[3]);
				screen.getRenderer().roundedRect(this.getX() + changeX, this.getY() + changeY, getWidth(), getHeight(), 10);
				screen.getRenderer().rect(this.getX() + changeX, this.getY() + changeY + getHeight() - 20, getWidth(), 20);

				IconDraw.renderIcon(screen, (int)(changeX + this.getX() + this.getWidth()/2 + 5), (int)(changeY + this.getY() + 5), (int)(this.getWidth()/2f - 10), (int)(this.getWidth()/2f - 10), this.icon);

				screen.getRenderer().end();

				drawString(screen, (int)(changeX - this.getWidth()/2f + 10), changeY + 5, getColorBorder(), DrawString.BEGIN);
			}
		}
	}
}
