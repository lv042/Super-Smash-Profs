package com.smashprofs.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import helper.*;
import helper.TileMapHelper;

import com.smashprofs.game.util;
import com.smashprofs.game.inputAxes;



import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {

    public static GameScreen INSTANCE;

    private Texture texture;
    private SpriteBatch batch;
    private Sprite sprite;
    private AssetManager assetManager;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer; //allows to see objects without textures

    //private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    public TileMapHelper tileMapHelper;
    private GameScreen testScreen;
    public Body heroBody;
    public BodyDef herobDef;

    /**
     * The Game Screen
     * @param camera
     * The camera which will be used for displaying the GameScreen
     */
    public GameScreen(OrthographicCamera camera) {
        this.assetManager = new AssetManager();

        // Creating Knight Sprite
        //this.texture = new Texture("knight.png");
        //this.batch = new SpriteBatch();
        //this.sprite = new Sprite(texture);

        this.camera = camera;
        this.world = new World(new Vector2(0,0), false);

        // Debug Code!
        herobDef = new BodyDef();
        herobDef.position.set(10, 17);
        herobDef.fixedRotation = true;
        herobDef.type = BodyDef.BodyType.DynamicBody;

        heroBody = world.createBody(herobDef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1); //64x64 box
        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1;
        fDef.restitution = 0.1f;
        fDef.friction = 0.5f;

        heroBody.createFixture(fDef);






        // Ende Debug Code


        // box2D Debug Renderer
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        this.tileMapHelper = new TileMapHelper();


        if(INSTANCE == null) INSTANCE = this;
        // WICHTIG! Muss unter INSTANCE stehen sonst nullpointer
        tileMapHelper.setupMap();
    }

    /**
     * Renders the GameScreen
     */
    @Override
    public void render(float delta) {
        this.update(delta);
        ScreenUtils.clear(0, 0, 0, 1); //clears the buffer after each frame with the chosen color » white

        // Configure OrthogonalTiledMapRenderer
        //orthogonalTiledMapRenderer.setView(camera);
        //orthogonalTiledMapRenderer.render();
        tileMapHelper.renderer.setView(camera);
        tileMapHelper.renderer.render();



        // GameClass.updateLuca(delta);
        //batch.begin();
        //render objects
        // sprite.draw(batch);
        //sprite.rotate(delta * 25.0f);
        //batch.end();
        GameClass.INSTANCE.renderHero(delta);


        // box2DDebugRenderer.render(world, camera.combined.scl(PPM));
         box2DDebugRenderer.render(world, camera.combined.scl(1f/PPM));


    }

    /**
     * Updates the GameScreen
     * @param delta
     * The time between frames
     */
    private void update(float delta) {
        world.step(1/60, 6, 2);
        GameClass.INSTANCE.updateHero(delta);
        cameraUpdate();

        //batch.setProjectionMatrix(camera.combined);

        tileMapHelper.renderer.setView(camera);

        // Exit on ESC press
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    /**
     * Updates the Camera. Needs to be executed at least once per render.
     */
    private void cameraUpdate() {
        // Set Camera position
        //camera.position.set(new Vector3(17,10,0));
        camera.position.x = GameClass.INSTANCE.luca.position.x; // Set camera position to character position

        camera.update();
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public void dispose() {
        this.box2DDebugRenderer.dispose();
        this.world.dispose();
    }

    @Override
    public void hide() {
        this.dispose();
    }

}


