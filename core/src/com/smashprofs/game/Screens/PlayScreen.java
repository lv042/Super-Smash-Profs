package com.smashprofs.game.Screens;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
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

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class PlayScreen implements Screen {

    private Game game;
    public static Viewport viewport; // Manages a Camera and determines how world coordinates are mapped to and from the screen.
    private Hud hud;


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

    private VFXManager vfxManager = VFXManager.getVFXManager_INSTANCE();

    private OrthographicCamera gamecamera; //set by camera manager

    //batch and game world
    public static SpriteBatch batch;

    //Box2D
    public static World world;

    //debug
    private Box2DDebugRenderer box2DDebugRenderer; //renders outline of box2d bodies


    //factories
    private PlayerFactory playerFactory = null;


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

    public void update(float deltatime) {

        tiledMapRenderer.setView(gamecamera);

        playerOne.update(deltatime); // Most of the code above should go in this method
        playerTwo.update(deltatime); // update method must be before most of the code below otherwise some values are null;
        checkInput(deltatime);
        //combatManager
        combatManager.update(deltatime, playerOne, playerTwo, world);
        vfxManager.update(deltatime);



        //updates the physics 60 times per second
        world.step(1/60f, 6, 2); //higher iterations make physics more accurate but also way slower

//        //contactListener.update();
//
//        if(contactListener.bodiesToDestroy.size > 0){
//            contactListener.bodiesToDestroy.clear();
//        }

        viewport.setScreenPosition(0, 0);
        //debug
        //DrawDebugLine(playerOne.getPosition(), playerTwo.getPosition(), gamecamera.combined);


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

        playerFactory = PlayerFactory.getPlayerFactory_INSTANCE();
        playerFactory.resetFactory();

        soundManager.setupMusic(gameSong);
        this.combatManager = CombatManager.getCombatManager_INSTANCE();
        this.game = game;
        gamecamera = cameraManager.getGameCamera();
        viewport = new FillViewport(Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM, gamecamera);

        //StretchViewport is a Viewport that stretches the screen to fill the window.
        //Screen Viewport is a Viewport that show as much of the world as possible on the screen -> makes the the world you see depend on the size of the window.
        //FitViewport is a Viewport that maintains the aspect ratio of the world and fills the window. -> Probalby the best option.

        createTileMap();

        //playerOne = new Player(world, Player.InputState.WASD, playerOneSpawnPoint, "Alex Boss", "PlayerOne");
        playerOne = playerFactory.getPlayer(PlayerTypes.Alex);
        //playerTwo = new Player(world, Player.InputState.ARROWS, playerTwoSpawnPoint, "Jens Huhn", "PlayerTwo");
        playerTwo = playerFactory.getPlayer(PlayerTypes.Maurice);

        contactListener = B2dContactListener.getContactListener_INSTANCE();
        world.setContactListener(contactListener);

        System.out.println("playerOne: " + playerOne);
        System.out.println("playerTwo: " + playerTwo);
        hud = new Hud(game.batch, playerOne, playerTwo);


        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log(controller.getUniqueId(), controller.getName());
        }

    }

    private void createTileMap() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("1/Map1New2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        //tiledMapRenderer.setBlending(true);

        this.batch = new SpriteBatch();

        world = new World(new Vector2(0, 0), true); //y value -> gravity -> now handled by the player class
        box2DDebugRenderer = new Box2DDebugRenderer();


        //define body
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
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

        update(delta);


        //render our tiledmap debug outlines to screen
        box2DDebugRenderer.render(world, gamecamera.combined);

        //batch.setProjectionMatrix(cameraManager.getGameCamera().combined);
        batch.setProjectionMatrix(gamecamera.combined);

        batch.begin();

        playerOne.draw(batch);
        playerTwo.draw(batch);
        combatManager.drawProjectiles(batch);
        vfxManager.drawVFX(batch);

        final Sprite sprite;
        final Body body;


        batch.end();


        // Muss unter batch.end() stehen
        hud.stage.draw();
        hud.updateHud(delta, playerOne, playerTwo);


       if(hud.testwin(playerOne,playerTwo))
       {
           game.setScreen(new WinScreen(game));
           WinScreen.setWinner(hud.getWinner());
       }


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
