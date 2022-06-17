package com.smashprofs.game.Screens;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import com.crashinvaders.vfx.effects.util.MixEffect;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Game;
import com.smashprofs.game.Helper.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.smashprofs.game.Scenes.Hud;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import static com.smashprofs.game.Actors.Players.Player.PPM;


/**
 * The main game screen
 */
public class PlayScreen implements Screen {

    private static Logger log = LogManager.getLogger(PlayScreen.class);

    private Game game;
    public static Viewport viewport; // Manages a Camera and determines how world coordinates are mapped to and from the screen.
    private Hud hud;
    private WinScreen winScreen;


    //tiled map
    public static ShapeRenderer debugRenderer = new ShapeRenderer();
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Player playerOne;
    private Player playerTwo;


    //managers
    private final B2dContactListener contactListener;
    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private String gameSong = "music/beste music ever.wav";
    private CombatManager combatManager;

    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();

    private VAFXManager vafxManager = VAFXManager.getVFXManager_INSTANCE();

    private OrthographicCamera gamecamera; //set by camera manager

    //batch and game world
    public static SpriteBatch batch;

    //Box2D
    public static World world;

    //collision flags


    //debug
    public Boolean debugMode;
    private Box2DDebugRenderer box2DDebugRenderer; //renders outline of box2d bodies


    //factories
    private PlayerFactory playerFactory = null;

    private VfxManager postProcessingManager;

    /**
     * Checks if player presses the "ESC" and if so, opens the main menu.
     * @param deltatime
     * The game deltatime
     */
    public void checkInput(float deltatime){

        //Player input is now handeled in the PlayerClass
        //Only external input is handled here

        //exit game
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
           // Gdx.app.exit();
            game.setScreen(new MainMenuScreen(game));
        }
    }


    public static SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Updates the PlayScreen and everything which is part of it.
     * @param deltatime
     * The game delta time
     */
    public void update(float deltatime) {

        tiledMapRenderer.setView(gamecamera);

        playerOne.update(deltatime);
        playerTwo.update(deltatime); // update method must be before most of the code below otherwise some values are null;
        checkInput(deltatime);
        //combatManager
        combatManager.update(deltatime, playerOne, playerTwo, world);
        vafxManager.update(deltatime);



        //updates the physics 60 times per second
        world.step(1/60f, 6, 2); //higher iterations make physics more accurate but also way slower

        viewport.setScreenPosition(0, 0);

        // Draw a debug line between p1 and p2
        if(debugMode) {
            DrawDebugLine(playerOne.getPosition(), playerTwo.getPosition(), gamecamera.combined);
        }

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

    /**
     * World getter-method.
     * @return
     * Returns the game world.
     */
    public static World getWorld(){
        return world;
    }

    /**
     * Constructor of PlayScreen. Sets up postprocessing, creates the player factory, sets viewport,
     * creates Players, initializes connected controllers, resets combat manager and contact listener.
     * @param game
     * The game.
     */
    public PlayScreen(Game game) {
        this.debugMode = Game.debugMode;

        //add pre configured settings to PostProcessingManager
        PostProcessingSettings ppSetUpHandler = new PostProcessingSettings();
        this.postProcessingManager = ppSetUpHandler.getPostProcessingManager();

        playerFactory = PlayerFactory.getPlayerFactory_INSTANCE();

        soundManager.setupMusic(gameSong);
        this.combatManager = CombatManager.getCombatManager_INSTANCE();
        this.game = game;
        gamecamera = cameraManager.getGameCamera();
        viewport = new FillViewport(Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM, gamecamera);

        //StretchViewport is a Viewport that stretches the screen to fill the window.
        //Screen Viewport is a Viewport that show as much of the world as possible on the screen -> makes the the world you see depend on the size of the window.
        //FitViewport is a Viewport that maintains the aspect ratio of the world and fills the window. -> Probalby the best option.

        createTileMap();

        //playerOne = playerFactory.getPlayer(PlayerTypes.Alex);
        switch(CharacterSelectScreen.getSelectedPlayerOne()) {
            case "Alex": playerOne = playerFactory.getPlayer(PlayerTypes.Alex); break;
            case "Maurice": playerOne = playerFactory.getPlayer(    PlayerTypes.Maurice); break;
            case "Luca": playerOne = playerFactory.getPlayer(PlayerTypes.Luca); break;
            case "Leo": playerOne = playerFactory.getPlayer(PlayerTypes.Leo); break;
            case "Viktor": playerOne = playerFactory.getPlayer(PlayerTypes.Viktor); break;
            default: playerOne = playerFactory.getPlayer(PlayerTypes.Alex);
        }

        // playerTwo = playerFactory.getPlayer(PlayerTypes.Maurice);
        switch(CharacterSelectScreen.getSelectedPlayerTwo()) {
            case "Alex": playerTwo = playerFactory.getPlayer(PlayerTypes.Alex); break;
            case "Maurice": playerTwo = playerFactory.getPlayer(PlayerTypes.Maurice); break;
            case "Luca": playerTwo = playerFactory.getPlayer(PlayerTypes.Luca); break;
            case "Leo": playerTwo = playerFactory.getPlayer(PlayerTypes.Leo); break;
            case "Viktor": playerTwo = playerFactory.getPlayer(PlayerTypes.Viktor); break;
            default: playerTwo = playerFactory.getPlayer(PlayerTypes.Alex);
        }

        contactListener = B2dContactListener.getContactListener_INSTANCE();
        world.setContactListener(contactListener);

        log.info("playerOne:" + playerOne);
        log.info("playerTwo:" + playerTwo);

        hud = new Hud(game.batch, playerOne, playerTwo);

        // Log all connected controllers:
        log.info("Connected Controllers:");
        log.info("---------------------------------");
        int controllerIndex = 1;
        for (Controller controller : Controllers.getControllers()) {
            log.info("Controller " + controllerIndex + ": " + controller.getUniqueId(), controller.getName());
            controllerIndex++;
        }
        log.info("---------------------------------");

        winScreen=new WinScreen(game,playerOne,playerTwo);

        // Necessary to avoid leftovers from another game session
        playerFactory.resetFactory();
        combatManager.resetCombatManager();
        contactListener.resetContactListener();

    }
    /**
     * Initializes the tile map. Loads the files, sets up the renderer, creates b2dBodies for
     * shapes in the object layers of the tile map.
     */
    private void createTileMap() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("1/Map1New2Remake.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        //tiledMapRenderer.setBlending(true);

        this.batch = new SpriteBatch();

        world = new World(new Vector2(0, 0), true); //y value -> gravity -> now handled by the player class
        box2DDebugRenderer = new Box2DDebugRenderer();


        //define body
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = B2dContactListener.WORLD_ENTITY;
        fdef.filter.maskBits = B2dContactListener.PLAYER_ENTITY | B2dContactListener.PROJECTILE_ENTITY;
        Body body;

        for(MapObject object : map.getLayers().get("obj").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2 ) / PPM, (rect.getY() + rect.getHeight() / 2 ) / PPM);
            body = world.createBody(bdef);
            body.setUserData("Tile");
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        log.debug("TiledMap created. Initialized object layers and created matching b2dBodies.");
    }


    @Override
    public void show() {

    }

    /**
     * Renders the playScreen after calling update(). Renders every necessary part of the screen
     * (players, items, tilemap, debug renderers, postprocessing filters and HUD).
     * Checks, if one player has won.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1); //-> light blue
        postProcessingManager.cleanUpBuffers();
        // Begin render to an off-screen buffer.
        postProcessingManager.beginInputCapture();

        tiledMapRenderer.render();

        update(delta);


        if (debugMode) {
            //render our tiled map debug outlines to screen
            box2DDebugRenderer.render(world, gamecamera.combined);

            // REMOVE "//" TO PRINT FPS OF TO THE CONSOLE!!-----
            //System.out.println(Gdx.graphics.getFramesPerSecond());
            // -------------------------------------------------
        }

        batch.setProjectionMatrix(gamecamera.combined);

        batch.begin();

        playerOne.draw(batch);
        playerTwo.draw(batch);
        combatManager.drawProjectiles(batch);
        vafxManager.drawVFX(batch);

        batch.end();


        // Muss unter batch.end() stehen
        hud.stage.draw();
        hud.updateHud(delta, playerOne, playerTwo);

        postProcessingManager.endInputCapture();

        postProcessingManager.applyEffects();

        postProcessingManager.renderToScreen();

        //Test for win and set to win screen
       if(hud.testwin(playerOne,playerTwo))
       {
           game.setScreen(winScreen);
       }


    }

    /**
     * Resizes the viewport.
     * @param width
     * The width the viewport should be set to.
     * @param height
     * The height the viewport should be set to.
     */
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

    /**
     * Dispose the PlayScreen and the map, the tiledMapRenderer, the b2dDebug renderer, the world and the hud.
     */
    @Override
    public void dispose() {
        map.dispose();
        tiledMapRenderer.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        hud.dispose();
    }
}
