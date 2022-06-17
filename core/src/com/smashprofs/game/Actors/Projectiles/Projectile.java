package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Actors.GameObject;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.CameraManager;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Screens.PlayScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Actors.Players.Player.PPM;

/**
 * A basic projectile
 */
public class  Projectile extends GameObject {

    private static Logger log = LogManager.getLogger(Projectile.class);

    private BodyDef bdef;
    // public Body b2dbody;
    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();
    private com.smashprofs.game.Actors.Players.Player originPlayer;

    private Vector2 projectileSpawnpoint;
    public double rotation = 0;
    public float spawnOffset;

    private short categoryBits = B2dContactListener.PROJECTILE_ENTITY;

    public final int damageOnHit;

    /**
     * The delay in seconds before you can use this attack again.
     */
    public static float delayInSeconds;

    public final Shape bodyShape;
    public Boolean active;
    World world;

    /**
     * Constructor for creating a projectile that spawns at the position of the originPlayer
     * @param world
     * The world in which the Projectile will exist (b2d)
     * @param originPlayer
     * The player whose position will be used as spawnpoint for the projectile
     * @param userData
     * The userData of the Projectile
     * @param bodyShape
     * The shape definition to control the body shape
     * @param projectileTexture
     * The texture which gets rendered for the projectile
     * @param damageOnHit
     * The amount of damage the projectile will do to a player
     */
    public Projectile(World world, Player originPlayer, String userData, Shape bodyShape, float spawnOffset, Texture projectileTexture, int damageOnHit, float delayInSec, short categoryBits) {
        super(userData);
        this.damageOnHit = damageOnHit;
        delayInSeconds = delayInSec;
        sprite = new Sprite(projectileTexture);
        this.active = true;
        this.userData = userData;
        this.world = world;
        this.originPlayer = originPlayer;
        this.bodyShape = bodyShape;
        this.spawnOffset = spawnOffset;
        this.categoryBits = categoryBits;
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + (originPlayer.getIsFacingRightAxe() * spawnOffset / PPM), originPlayer.getPosition().y);


        sprite.setBounds(originPlayer.getPlayerSprite().getX() / PPM, originPlayer.getPlayerSprite().getY() / PPM, sprite.getWidth()/PPM, sprite.getHeight()/PPM);
        create();


    }



    /**
     * Constructor for creating a projectile that spawns at the given originPosition
     * @param world
     * The world in which the Projectile will exist (b2d)
     * @param originPosition
     * The position at which the projectile will spawn
     * @param userData
     * The userData of the Projectile
     * @param bodyShape
     * The shape definition to control the body shape
     * @param projectileTexture
     * The texture which gets rendered for the projectile
     * @param damageOnHit
     * The amount of damage the projectile will do to a player
     */
    public Projectile(World world, Vector2 originPosition, String userData, Shape bodyShape, float spawnOffset, Texture projectileTexture, int damageOnHit, float delayInSec, short categoryBits) {
        super(userData);
        this.damageOnHit = damageOnHit;
        delayInSeconds = delayInSec;
        sprite = new Sprite(projectileTexture);
        this.active = true;
        this.userData = userData;
        this.world = world;
        this.bodyShape = bodyShape;
        this.spawnOffset = spawnOffset;
        this.categoryBits = categoryBits;
        //projectileSpawnpoint = new Vector2(originPosition.x, originPosition.y );
        projectileSpawnpoint = new Vector2(originPosition.x + (spawnOffset / PPM), originPosition.y);


        sprite.setBounds(originPosition.x / PPM, originPosition.y / PPM, sprite.getWidth()/PPM, sprite.getHeight()/PPM);
        create();


    }




    /**
     * Applies a horizontal linearVelocity to the Projectile
     */
    void initialMovement(float speed) {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * speed, 0));
    }

    /**
     * Creates BodyDef(), b2dBody and FixtureDef.
     * Sets the group of bodies the Projectile can collide with.
     * Sets the sprite origin.
     */
    public void create() {

        bdef = new BodyDef();
        bdef.position.set(projectileSpawnpoint);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = false;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        //CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        //shape.setRadius(3 / PPM);


        //fDef.shape = shape;
        fDef.shape = bodyShape;

        //filters collisions with other objects
        fDef.filter.categoryBits = categoryBits;

        //fDef.density = 0.1f;
        //fDef.restitution = 0.4f;
        // fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);
        sprite.setOrigin(sprite.getWidth()*0.5f, sprite.getHeight()*0.5f);


        //System.out.println("projectile address: " + this);
        log.debug("projectile address: " + this);
    }

    /**
     * Sets the position of the sprite to the current position of the b2dBody.
     * Sets the rotation of the sprite to the current rotation of the b2dBody.
     * @param delta
     * The game delta time
     */
    public void update(float delta) {
        sprite.setPosition(b2dbody.getPosition().x - sprite.getWidth() / 2f, b2dbody.getPosition().y - sprite.getHeight() / 2f);

        rotation = -2.25*(b2dbody.getAngle()*2*Math.PI*4);
        sprite.setRotation((float) rotation);

        //log.debug("Projectile b2dBody userData: " + b2dbody.getUserData());
    }

    /**
     * Draws the projectile
     * @param batch
     * Batch in which the projectile will get drawn
     */
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        // log.debug("Projectile drawn");
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

    /**
     * Returns if the projectile is active
     * @return
     * true if projectile is active, false if projectile is inactive
     */
    public Boolean isActive() {
        return active;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return this.b2dbody;
    }

    /**
     * Destroys the b2dBody of the projectile
     */
    public void destroyBody() {
        log.debug("Deleting Projectile body" + b2dbody.getUserData());
        PlayScreen.getWorld().destroyBody(this.b2dbody);
    }

    /**
     * Disposes the texture of the projectile sprite
     */
    public void destroy() {
        sprite.getTexture().dispose();
        log.debug("Disposed projectile texture via .destroy()");
    }

    /**
     * Sets the activity state of the projectile
     * @param b
     * The value to set active to
     */
    public void setActive(boolean b) {
        active = b;
    }

    /**
     * Get the delayInSeconds
     * @return
     * The delay in seconds before you can use this attack again.
     */
    public static float getDelayInSeconds() {
        return delayInSeconds;
    }

}



