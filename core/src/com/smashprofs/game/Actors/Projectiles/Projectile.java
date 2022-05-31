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
import com.smashprofs.game.Helper.CameraManager;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Screens.PlayScreen;

import static com.smashprofs.game.Actors.Players.Player.PPM;

/**
 * A basic projectile
 */
public class  Projectile extends GameObject {
    private BodyDef bdef;
    // public Body b2dbody;
    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();
    private com.smashprofs.game.Actors.Players.Player originPlayer;

    private Vector2 projectileSpawnpoint;
    public double rotation = 0;

    public final int damageOnHit;

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
     * @param projectileTexture
     * The texture which gets rendered for the projectile
     * @param damageOnHit
     * The amount of damage the projectile will do to a player
     */
    public Projectile(World world, Player originPlayer, String userData, Texture projectileTexture, int damageOnHit) {
        super(userData);
        this.damageOnHit = damageOnHit;
        sprite = new Sprite(projectileTexture);
        this.active = true;
        this.userData = userData;
        this.world = world;
        this.originPlayer = originPlayer;
        //setTexture(new Texture("prettyplayer.png"));
        //setTexture(new Texture(Gdx.files.internal("prettyplayer.png")));
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + (originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius() * 3f / PPM), originPlayer.getPosition().y);



        //setPosition(12, 12);

        sprite.setBounds(originPlayer.getPlayerSprite().getX() / PPM, originPlayer.getPlayerSprite().getY() / PPM, sprite.getWidth()/PPM, sprite.getHeight()/PPM);
        create();
        //scale(1/PPM);



        initialMovement();

    }

    /**
     * Constructor for creating a projectile that spawns at the given originPosition
     * @param world
     * The world in which the Projectile will exist (b2d)
     * @param originPosition
     * The position at which the projectile will spawn
     * @param userData
     * The userData of the Projectile
     * @param projectileTexture
     * The texture which gets rendered for the projectile
     * @param damageOnHit
     * The amount of damage the projectile will do to a player
     */
    public Projectile(World world, Vector2 originPosition, String userData, Texture projectileTexture, int damageOnHit) {
        super(userData);
        this.damageOnHit = damageOnHit;
        sprite = new Sprite(projectileTexture);
        this.active = true;
        this.userData = userData;
        this.world = world;

        //add ppm offset
        projectileSpawnpoint = new Vector2(originPosition.x, originPosition.y );



        sprite.setBounds(originPosition.x / PPM, originPosition.y / PPM, sprite.getWidth()/PPM, sprite.getHeight()/PPM);
        create();


        initialMovement();
    }


    /**
     * Applies a horizontal linearVelocity to the Projectile
     */
    void initialMovement() {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * 0.1f, 0));
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
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(3 / PPM);


        fDef.shape = shape;

        //filters collisions with other objects
        if (true) {
            fDef.filter.groupIndex = 0;

            System.out.println("Group index: " + fDef.filter.groupIndex);
        }

        //fDef.density = 0.1f;
        //fDef.restitution = 0.4f;
        // fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);
        sprite.setOrigin(sprite.getWidth()*0.5f, sprite.getHeight()*0.5f);


        System.out.println("projectile address: " + this);
    }

    /**
     * Sets the position of the sprite to the current position of the b2dBody.
     * Sets the rotation of the sprite to the current rotation of the b2dBody.
     * @param delta
     * The game delta time
     */
    public void update(float delta) {

        //System.out.println("!!!! projectile update");
        //this.setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);

        //setBounds(b2dbody.getPosition().x - getWidth() / 2/PPM, b2dbody.getPosition().y - getHeight() / 2 /PPM, getTexture().getWidth()/PPM, getTexture().getHeight()/PPM );


        sprite.setPosition(b2dbody.getPosition().x - sprite.getWidth() / 2f, b2dbody.getPosition().y - sprite.getHeight() / 2f);


        //rotation += 0.8f;
        //setPosition(b2dbody.getPosition().x - getWidth() / 2/PPM, b2dbody.getPosition().y - getHeight() / 2 /PPM);
        rotation = -2.25*(b2dbody.getAngle()*2*Math.PI*4);
        sprite.setRotation((float) rotation);


        System.out.println("Projectile b2dBody userData: " + b2dbody.getUserData());
    }

    /**
     * Draws the projectile
     * @param batch
     * Batch in which the projectile will get drawn
     */
    @Override
    public void draw(Batch batch) {


        //System.out.println("projectile draw");
        super.draw(batch);
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
        System.out.println("Deleting Projectile body" + b2dbody.getUserData());
        PlayScreen.getWorld().destroyBody(this.b2dbody);
    }

    /**
     * Disposes the texture of the projectile sprite
     */
    public void destroy() {
        //world.destroyBody(b2dbody);
        //sprite.setPosition(100000f, 100000f);
        //active = false;

        sprite.getTexture().dispose();
        System.out.println("Disposed projectile texture via .destroy()");
        //destroy sprite
        //sprite.getTexture().dispose();
    }

    /**
     * Sets the activity state of the projectile
     * @param b
     * The value to set active to
     */
    public void setActive(boolean b) {
        active = b;
    }
}



