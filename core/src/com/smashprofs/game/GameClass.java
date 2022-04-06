package com.smashprofs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import com.badlogic.gdx.Game;
import helper.Constants;
import helper.TileMapHelper;

public class GameClass extends Game {

	// The properties of our Hero
	static class Hero {
		static float WIDTH;
		static float HEIGHT;
		static float MAX_VELOCITY = 10f;
		static float JUMP_VELOCITY = 40f;
		static float DAMPING = 0.7f; //time it takes until the player stops moving after stopping input

		enum State {
			Standing, Walking, Jumping
		}

		Vector2 position = new Vector2();
		final Vector2 velocity = new Vector2();
		State state = State.Walking;
		float stateTime = 0;
		boolean facesRight = true;
		boolean grounded = false;

	}



	SpriteBatch batch;
	Texture img;

	// Screen dimension variables
	private int screenWidth;
	private int screenHeight;
	// 2D Camera
	private OrthographicCamera camera;

	private Texture heroTextureIdle;
	private Texture heroTextureWalk;
	private Animation<TextureRegion> stand;
	private Animation<TextureRegion> walk;
	private Animation<TextureRegion> jump;

	public Hero luca;

	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};

	private Array<Rectangle> tiles = new Array<Rectangle>();

	// Create Singleton
	public static GameClass INSTANCE;
	// Constructor
	public GameClass() { if(INSTANCE == null) INSTANCE = this; }

	@Override
	public void create () {
		

		// Get screen dimensions
		this.screenWidth = Gdx.graphics.getWidth();
		this.screenHeight = Gdx.graphics.getHeight();

		// Initialize camera
		this.camera = new OrthographicCamera();
		// this.camera.setToOrtho(false, screenWidth, screenHeight);
		this.camera.setToOrtho(false, 30, 20);


		// this.camera.position.x = luca.position.x; // Set camera position to character position

		INSTANCE.setScreen(new GameScreen(camera));
		camera.update();


		// load the hero frames, split them, and assign them to Animations
		heroTextureIdle = new Texture("Sprites/herochar_idle_anim_strip_4.png");
		heroTextureWalk = new Texture("Sprites/herochar_run_anim_strip_6.png");
		TextureRegion[] idle = TextureRegion.split(heroTextureIdle, 16, 16)[0];
		TextureRegion[] walking = TextureRegion.split(heroTextureWalk, 16, 16)[0];
		stand = new Animation(0.15f, idle[0], idle[1], idle[2], idle[3]);
		jump = new Animation(0, idle[1]);
		walk = new Animation(0.15f, walking[0], walking[1], walking[2], walking[3], walking[4], walking[5]);
		walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

		// figure out the width and height of the hero for collision
		// detection and rendering by converting a hero frames pixel
		// size into world units (1 unit == 8 pixels)
		Hero.WIDTH = 1 / 8f * idle[0].getRegionWidth();
		Hero.HEIGHT = 1 / 8f * idle[0].getRegionHeight();

		// create the hero we want to move around the world
		luca = new Hero();
		luca.position.set(17, 10);

		this.camera.position.x = luca.position.x; // Set camera position to character position

	}

	public void updateLuca (float deltaTime) {
		if (deltaTime == 0) return;
		if (deltaTime > 0.1f) deltaTime = 0.1f; // I dont know whats going on here

		luca.stateTime += deltaTime;

		// check input and apply to velocity & state
		if ((Gdx.input.isKeyPressed(Input.Keys.SPACE)  && luca.grounded)) {
			luca.velocity.y += Hero.JUMP_VELOCITY;
			luca.state = Hero.State.Jumping;
			luca.grounded = false;

		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) ) {
			luca.velocity.x = -Hero.MAX_VELOCITY;
			if (luca.grounded) luca.state = Hero.State.Walking;
			luca.facesRight = false;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			luca.velocity.x = Hero.MAX_VELOCITY;
			if (luca.grounded) luca.state = Hero.State.Walking;
			luca.facesRight = true;
		}

		//if (Gdx.input.isKeyJustPressed(Input.Keys.B)) debug = !debug; //deactivates debug mode

		// apply gravity if we are falling
		luca.velocity.add(0, Constants.GRAVITY);

		// clamp the velocity to the maximum, x-axis only
		luca.velocity.x = MathUtils.clamp(luca.velocity.x, -Hero.MAX_VELOCITY, Hero.MAX_VELOCITY);

		// If the velocity is < 1, set it to 0 and set state to Standing
		if (Math.abs(luca.velocity.x) < 1) {
			luca.velocity.x = 0;
			if (luca.grounded) luca.state = Hero.State.Standing;
		}

		// multiply by delta time so we know how far we go
		// in this frame
		luca.velocity.scl(deltaTime); //makes movement framerate independent

		// perform collision detection & response, on each axis, separately
		// if the luca is moving right, check the tiles to the right of it's
		// right bounding box edge, otherwise check the ones to the left
		/*Rectangle lucaRect = rectPool.obtain();
		lucaRect.set(luca.position.x, luca.position.y, Hero.WIDTH, Hero.HEIGHT);
		int startX, startY, endX, endY;
		if (luca.velocity.x > 0) {
			startX = endX = (int)(luca.position.x + Hero.WIDTH + luca.velocity.x);
		} else {
			startX = endX = (int)(luca.position.x + luca.velocity.x);
		}*/


//		startY = (int)(luca.position.y);
//		endY = (int)(luca.position.y + Hero.HEIGHT);
//		getTiles(startX, startY, endX, endY, tiles);
//		lucaRect.x += luca.velocity.x;
//		for (Rectangle tile : tiles) {
//			if (lucaRect.overlaps(tile)) {
//				luca.velocity.x = 0;
//				break;
//			}
//		}
		/*lucaRect.x = luca.position.x;

		// if the luca is moving upwards, check the tiles to the top of its
		// top bounding box edge, otherwise check the ones to the bottom
		if (luca.velocity.y > 0) {
			startY = endY = (int)(luca.position.y + Hero.HEIGHT + luca.velocity.y);
		} else {
			startY = endY = (int)(luca.position.y + luca.velocity.y);
		}
		startX = (int)(luca.position.x);
		endX = (int)(luca.position.x + Hero.WIDTH);
		getTiles(startX, startY, endX, endY, tiles);
		lucaRect.y += luca.velocity.y;
		for (Rectangle tile : tiles) {
			if (lucaRect.overlaps(tile)) {
				// we actually reset the luca y-position here
				// so it is just below/above the tile we collided with
				// this removes bouncing :)
				if (luca.velocity.y > 0) {
					luca.position.y = tile.y - Hero.HEIGHT;
					// we hit a block jumping upwards, let's destroy it!
					TiledMapTileLayer layer = (TiledMapTileLayer)GameScreen.INSTANCE.tileMapHelper.tiledMap.getLayers().get("Ground");
					//layer.setCell((int)tile.x, (int)tile.y, null); //keep deactivated otherwise will delete the cell above with each jump
				} else {
					luca.position.y = tile.y + tile.height;
					// if we hit the ground, mark us as grounded so we can jump
					luca.grounded = true;
				}
				luca.velocity.y = 0;
				break;
			}
		}
		rectPool.free(lucaRect);*/

		// unscale the velocity by the inverse delta time and set
		// the latest position
		luca.position.add(luca.velocity);
		luca.velocity.scl(1 / deltaTime);

		// Apply damping to the velocity on the x-axis so we don't
		// walk infinitely once a key was pressed
		luca.velocity.x *= Hero.DAMPING; // damping must be lower than 1
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//img.dispose();
	}



	private void getTiles (int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
		TiledMapTileLayer layer = (TiledMapTileLayer)GameScreen.INSTANCE.tileMapHelper.tiledMap.getLayers().get("Ground");
		rectPool.freeAll(tiles);
		tiles.clear();
		for (int y = startY; y <= endY; y++) {
			for (int x = startX; x <= endX; x++) {
				TiledMapTileLayer.Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = rectPool.obtain();
					rect.set(x, y, 1, 1);
					tiles.add(rect);
				}
			}
		}
	}

	public void renderHero (float deltaTime) {
		// based on the Hero state, get the animation frame
		TextureRegion frame = null;
		switch (luca.state) {
			case Standing:
				frame = stand.getKeyFrame(luca.stateTime);
				break;
			case Walking:
				frame = walk.getKeyFrame(luca.stateTime);
				break;
			case Jumping:
				frame = jump.getKeyFrame(luca.stateTime);
				break;
		}

		// draw the Hero, depending on the current velocity
		// on the x-axis, draw the koala facing either right
		// or left
		Batch batch = GameScreen.INSTANCE.tileMapHelper.renderer.getBatch();
		batch.begin();
		if (luca.facesRight) {
			batch.draw(frame, luca.position.x, luca.position.y, Hero.WIDTH, Hero.HEIGHT);
		} else {
			batch.draw(frame, luca.position.x + Hero.WIDTH, luca.position.y, -Hero.WIDTH, Hero.HEIGHT);
		}

		Vector2 position = GameScreen.INSTANCE.heroBody.getPosition();
		GameScreen.INSTANCE.heroBody.getPosition();
		// GameScreen.INSTANCE.heroBody.setLinearVelocity(1000,1000);
		luca.position.set(position);
		batch.end();

		GameScreen.INSTANCE.herobDef.position.set(position.scl(1f/Constants.PPM));

	}




}
