package com.smashprofs.game.Screens;


import com.smashprofs.game.GameClass;
import com.smashprofs.game.Helper.util;
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
import com.smashprofs.game.Sprites.PlayerClass;
import com.smashprofs.game.Scenes.Hud;
import static com.smashprofs.game.Sprites.PlayerClass.PPM;

public class PlayScreen implements Screen {


    private GameClass game;

    private float jumpForce = 3f;

    boolean playMusic = true;
    Texture texture;
    private OrthographicCamera cameragame;
    public Viewport viewport; // Manages a Camera and determines how world coordinates are mapped to and from the screen.
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    private PlayerClass playerOne;
    private PlayerClass playerTwo;


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
        checkInput(deltatime);

        //updates the physics 60 times per second
        world.step(1/60f, 6, 2); //higher iterations make pyhsics more accurate but also way slower

        updateCamera();
        tiledMapRenderer.setView(cameragame);
        viewport.setScreenPosition(0, 0);
        respawnPlayers();
        limitPlayersToEdge();
        playerOne.checkGrounded();
        playerTwo.checkGrounded();


    }

    private void respawnPlayers() {
        if(playerOne.reachedWorldEdge()){
            //playerOne.getB2dbody().applyLinearImpulse(new Vector2(0, 2f), playerOne.getB2dbody().getWorldCenter(), true);
            playerOne.getB2dbody().setLinearVelocity(new Vector2(playerOne.getB2dbody().getLinearVelocity().x * playerOne.getRespawnDamping(), 5f));
            System.out.println("Player respawn jump");
        }
        if(playerTwo.reachedWorldEdge()){
            //playerOne.getB2dbody().applyLinearImpulse(new Vector2(0, 2f), playerOne.getB2dbody().getWorldCenter(), true);
            playerTwo.getB2dbody().setLinearVelocity(new Vector2(playerOne.getB2dbody().getLinearVelocity().x * playerOne.getRespawnDamping(), 5f));
            System.out.println("Player respawn jump");
        }
    }

    private void limitPlayersToEdge(){
        //sets player velocity to 0 if they are at the edge of the map
        float pushBack = 1f;

        if(playerOne.getB2dbody().getPosition().x > viewport.getWorldWidth()){
            playerOne.getB2dbody().setLinearVelocity(new Vector2(-pushBack, playerOne.getB2dbody().getLinearVelocity().y));
        }
        if(playerOne.getB2dbody().getPosition().x < 0){
            playerOne.getB2dbody().setLinearVelocity(new Vector2( pushBack, playerOne.getB2dbody().getLinearVelocity().y) );
        }
        if(playerTwo.getB2dbody().getPosition().x > viewport.getWorldWidth()){
            playerTwo.getB2dbody().setLinearVelocity(new Vector2(-pushBack, playerTwo.getB2dbody().getLinearVelocity().y));
        }
        if(playerTwo.getB2dbody().getPosition().x < 0){
            playerTwo.getB2dbody().setLinearVelocity(new Vector2( pushBack, playerTwo.getB2dbody().getLinearVelocity().y) );
        }
    }


    public PlayScreen(GameClass game) {


        this.game = game;
        texture = new Texture("badlogic.jpg");
        cameragame = new OrthographicCamera();
        viewport = new FillViewport(GameClass.V_WIDTH / PPM, GameClass.V_HEIGHT / PPM, cameragame);
        hud = new Hud(game.batch);
        // StretchViewport is a Viewport that stretches the screen to fill the window.
        //Screen Viewport is a Viewport that show as much of the world as possible on the screen -> makes the the world you see depend on the size of the window.
        //FitViewport is a Viewport that maintains the aspect ratio of the world and fills the window. -> Probalby the best option.

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("1/Map 1.tmx");
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(map, 1 / PPM);

        cameragame.position.set((viewport.getWorldWidth() / 2), (viewport.getWorldHeight() / 2) , 0); // sets the position of the camera to the center of the screen -> later you can use the util class


        world = new World(new Vector2(0, -10f), true); //y value -> gravity
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

        playerOne = new PlayerClass(world, PlayerClass.InputState.WASD);
        playerTwo = new PlayerClass(world, PlayerClass.InputState.ARROWS);

        util.setupMusic(playMusic);

    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 1, 1, 1); //-> light blue
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.updateHud(delta);
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