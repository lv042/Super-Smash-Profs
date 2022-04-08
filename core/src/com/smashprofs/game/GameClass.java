package com.smashprofs.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smashprofs.game.Screens.PlayScreen;

public class GameClass extends Game {
	public SpriteBatch batch;
	Texture img;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this)); // passes game to set screen on its own
	}

	@Override
	public void render () {
/*		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
