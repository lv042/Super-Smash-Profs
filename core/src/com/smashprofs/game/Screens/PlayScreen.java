package com.smashprofs.game.Screens;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.smashprofs.game.Game;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.CameraManager;
import com.smashprofs.game.Helper.CombatManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Actors.Player;
import com.smashprofs.game.Scenes.Hud;

import java.io.IOException;

import static com.smashprofs.game.Actors.Player.PPM;

public class PlayScreen implements Screen {

    private final B2dContactListener contactListener;

    public static ShapeRenderer debugRenderer = new ShapeRenderer();

    private String gameSong = "music/beste music ever.wav";

    private Game game;

    private float jumpForce = 3f;

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private Vector2 playerOneSpawnPoint = new Vector2(90, 90);

    private Vector2 playerTwoSpawnPoint = new Vector2(110, 90);

    private OrthographicCamera gamecamera;
    public static Viewport viewport; // Manages a Camera and determines how world coordinates are mapped to and from the screen.
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;
    private Player playerOne;
    private Player playerTwo;

    private CombatManager combatManager;

    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();


    public SpriteBatch batch;

    //Box2D
    private static World world;
    private Box2DDebugRenderer box2DDebugRenderer; //renders outline of box2d bodies


    public void checkInput(float deltatime){

        //Player input is now handeled in the PlayerClass
        //Only external input is handled here

        //exit game
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }



    public void update(float deltatime) throws IOException {

        tiledMapRenderer.setView(gamecamera);

        playerOne.update(deltatime); // Most of the code above should go in this method
        playerTwo.update(deltatime); // update method must be before most of the code below otherwise some values are null;
        checkInput(deltatime);
        //combatManager
        combatManager.update(deltatime, playerOne, playerTwo, world);



        //updates the physics 60 times per second
        world.step(1/60f, 6, 2); //higher iterations make physics more accurate but also way slower

        contactListener.update();

        if(contactListener.bodiesToDestroy.size > 0){
            contactListener.bodiesToDestroy.clear();
        }

        viewport.setScreenPosition(0, 0);
        //debug
        DrawDebugLine(playerOne.getPosition(), playerTwo.getPosition(), gamecamera.combined);


        cameraManager.updateCameraManager(playerOne, playerTwo);
    }

    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static Viewport getViewport() {
        return viewport;
    }

    public static World getWorld(){
        return world;
    }

    public PlayScreen(Game game) {


        soundManager.setupMusic(gameSong);
        this.combatManager = CombatManager.getCombatManager_INSTANCE();
        this.game = game;
        gamecamera = cameraManager.getGameCamera();
        viewport = new FillViewport(Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM, gamecamera);

        //StretchViewport is a Viewport that stretches the screen to fill the window.
        //Screen Viewport is a Viewport that show as much of the world as possible on the screen -> makes the the world you see depend on the size of the window.
        //FitViewport is a Viewport that maintains the aspect ratio of the world and fills the window. -> Probalby the best option.

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("1/Map1New2.tmx");
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(map, 1 / PPM);
        tiledMapRenderer.setBlending(true);

        this.batch = new SpriteBatch();

        world = new World(new Vector2(0, 0), true); //y value -> gravity -> now handled by the player class
        box2DDebugRenderer = new Box2DDebugRenderer();


        //define body
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2 ) / PPM, (rect.getY() + rect.getHeight() / 2 ) / PPM);
            body = world.createBody(bdef);
            body.setUserData("Tile");
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        playerOne = new Player(world, Player.InputState.WASD, playerOneSpawnPoint, "Alex Boss", "PlayerOne");
        playerTwo = new Player(world, Player.InputState.ARROWS, playerTwoSpawnPoint, "Jens Huhn", "PlayerTwo");

        contactListener = B2dContactListener.getContactListener_INSTANCE();
        world.setContactListener(contactListener);

        hud = new Hud(game.batch, playerOne, playerTwo);


    }





    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1); //-> light blue
        //Gdx.gl.glClearColor(1, 1, 1, 1);

        tiledMapRenderer.render();
        try {
            update(delta);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //render our tiledmap debug outlines to screen
        box2DDebugRenderer.render(world, gamecamera.combined);

        batch.setProjectionMatrix(cameraManager.getGameCamera().combined);
        batch.begin();

        playerOne.draw(batch);
        playerTwo.draw(batch);

        batch.end();


        // Muss unter batch.end() stehen
        hud.stage.draw();
        hud.updateHud(delta, playerOne, playerTwo);





    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        tiledMapRenderer.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        hud.dispose();
    }
}
