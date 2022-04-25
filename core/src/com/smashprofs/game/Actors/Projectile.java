package com.smashprofs.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Helper.CameraManager;
import com.smashprofs.game.Screens.PlayScreen;

import java.util.WeakHashMap;

import static com.smashprofs.game.Actors.PlayerClass.PPM;
import static com.smashprofs.game.Screens.PlayScreen.debugRenderer;

public abstract class  Projectile extends Sprite{
    private Batch batch = new SpriteBatch();
    private BodyDef bdef;

    Body b2dbody;

    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();

    private PlayerClass originPlayer;

    private Vector2 projectileSpawnpoint;

    private Texture texture;

    private int rotation = 0;

    String userData = "JOeMama";

    OrthographicCamera camera;

    Sprite sprite;


    World world;

    public Projectile(World world, PlayerClass originPlayer, String userData, OrthographicCamera camera) {
        this.userData = userData;
        this.world = world;
        this.originPlayer = originPlayer;
        this.camera = camera;
        create();

        moveProjectile();

    }

    void moveProjectile() {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * 1.2f, 0));
    }

    public void create() {

        bdef = new BodyDef();
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius() * 2.1f / PPM, originPlayer.getPosition().y);
        bdef.position.set(projectileSpawnpoint);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = false;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(3 / PPM);

        //Implement the player textures and animations -> @Maurice @Alex


        texture = new Texture("star.png");
        batch = new SpriteBatch();
        sprite = new Sprite(texture);


        fDef.shape = shape;

        //filters collisions with other objects
        if (true) {
            fDef.filter.groupIndex = 0;
        }

        //fDef.density = 0.1f;
        //fDef.restitution = 0.4f;
        // fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);
        //sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        //sprite.setRegion(region);
        sprite.setPosition(199, 199);

    }

    public void update(float delta) {
        rotation += 1f;
        batch.setProjectionMatrix(new OrthographicCamera().combined);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        ShapeRenderer debugRenderer = new ShapeRenderer();
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }





}






