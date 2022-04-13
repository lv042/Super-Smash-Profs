package com.smashprofs.game.Screens;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.smashprofs.game.GameClass;
import com.smashprofs.game.Helper.CombatManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.smashprofs.game.Actors.PlayerClass;
import com.smashprofs.game.Scenes.Hud;
import static com.smashprofs.game.Actors.PlayerClass.PPM;

public class PlayScreen implements Screen {

    private TextureAtlas atlas;

    private static ShapeRenderer debugRenderer = new ShapeRenderer();

    private String gameSong = "music/beste music ever.wav";

    private GameClass game;

    private float jumpForce = 3f;

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private Vector2 playerOneSpawnPoint = new Vector2(90, 90);

    private Vector2 playerTwoSpawnPoint = new Vector2(110, 90);

    Texture texture;
    private OrthographicCamera cameragame;
    public static Viewport viewport; // Manages a Camera and determines how world coordinates are mapped to and from the screen.
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;
    private PlayerClass playerOne;
    private PlayerClass playerTwo;

    private CombatManager combatManager;


    //Box2D
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer; //renders outline of box2d bodies

    public void updateCamera(){
        cameragame.update();
    }
    public void checkInput(float deltatime){

        playerOne.managePlayerInput(deltatime);
        playerTwo.managePlayerInput(deltatime);

        //exit game
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }



    public void update(float deltatime){
        tiledMapRenderer.setView(cameragame);

        playerOne.checkGrounded();
        playerTwo.checkGrounded();

        //input
        checkInput(deltatime);

        playerOne.updatePosition(deltatime);
        playerTwo.updatePosition(deltatime);

        //combatManager
        combatManager.update(deltatime, playerOne, playerTwo);



        //updates the physics 60 times per second
        world.step(1/60f, 6, 2); //higher iterations make physics more accurate but also way slower

        updateCamera();
        viewport.setScreenPosition(0, 0);

        //player
        playerOne.respawnPlayers();
        playerTwo.respawnPlayers();

        playerOne.limitPlayersToEdge();
        playerTwo.limitPlayersToEdge();





        playerOne.update(deltatime); // Most of the code above should go in this method
        playerTwo.update(deltatime);





        //debug
        DrawDebugLine(new Vector2(0,0), new Vector2(100,100), cameragame.combined);

        //System.out.println("finished update");


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

    public PlayScreen(GameClass game) {

        atlas = new TextureAtlas("Sprites/AlexSpritePack.pack");


        soundManager.setupMusic(gameSong);
        this.combatManager = CombatManager.getCombatManager_INSTANCE();
        this.game = game;
        cameragame = new OrthographicCamera();
        viewport = new FillViewport(GameClass.V_WIDTH / PPM, GameClass.V_HEIGHT / PPM, cameragame);

        //StretchViewport is a Viewport that stretches the screen to fill the window.
        //Screen Viewport is a Viewport that show as much of the world as possible on the screen -> makes the the world you see depend on the size of the window.
        //FitViewport is a Viewport that maintains the aspect ratio of the world and fills the window. -> Probalby the best option.

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("1/Map 1.tmx");
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(map, 1 / PPM);

        cameragame.position.set((viewport.getWorldWidth() / 2), (viewport.getWorldHeight() / 2) , 0); // sets the position of the camera to the center of the screen -> later you can use the util class


        world = new World(new Vector2(0, 0), true); //y value -> gravity -> now handled by the player class
        box2DDebugRenderer = new Box2DDebugRenderer();


        //define body
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(0).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2 ) / PPM, (rect.getY() + rect.getHeight() / 2 ) / PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        playerOne = new PlayerClass(world, PlayerClass.InputState.WASD, playerOneSpawnPoint, "Martin Goib", this);
        playerTwo = new PlayerClass(world, PlayerClass.InputState.ARROWS, playerTwoSpawnPoint, "Jens Huhn", this);

        hud = new Hud(game.batch, playerOne, playerTwo);


    }


    public TextureAtlas getAtlas() {
        return atlas;

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1); //-> light blue
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(cameragame.combined);
        game.batch.begin();
        playerOne.draw(game.batch);
        game.batch.end();
        hud.stage.draw();
        hud.updateHud(delta, playerOne, playerTwo);
        tiledMapRenderer.render();




        //render our tiledmap debug outlines to screen
        box2DDebugRenderer.render(world, cameragame.combined);


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