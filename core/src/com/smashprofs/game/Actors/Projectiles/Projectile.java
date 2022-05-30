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

public class  Projectile extends GameObject {
    private BodyDef bdef;
    // public Body b2dbody;
    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();
    private com.smashprofs.game.Actors.Players.Player originPlayer;

    private Vector2 projectileSpawnpoint;
    public double rotation = 0;



    public Boolean active;
    World world;

    public Projectile(World world, Player originPlayer, String userData, Texture projectileTexture) {
        super(userData);
        sprite = new Sprite(projectileTexture);
        this.active = true;
        this.userData = userData;
        this.world = world;
        this.originPlayer = originPlayer;
        //setTexture(new Texture("prettyplayer.png"));
        //setTexture(new Texture(Gdx.files.internal("prettyplayer.png")));


        //setPosition(12, 12);

        sprite.setBounds(originPlayer.getPlayerSprite().getX() / PPM, originPlayer.getPlayerSprite().getY() / PPM, sprite.getWidth()/PPM, sprite.getHeight()/PPM);
        create();
        //scale(1/PPM);



        initialMovement();

    }

    void initialMovement() {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * 0.1f, 0));
    }

    public void create() {

        bdef = new BodyDef();
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + (originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius() * 3f / PPM), originPlayer.getPosition().y);
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

    public Boolean isActive() {
        return active;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return this.b2dbody;
    }

    public void destroy() {
        //world.destroyBody(b2dbody);
        //sprite.setPosition(100000f, 100000f);
        //active = false;

        sprite.getTexture().dispose();
        System.out.println("Disposed projectile texture via .destroy()");
        //destroy sprite
        //sprite.getTexture().dispose();
    }

    public void setActive(boolean b) {
        active = b;
    }
}



