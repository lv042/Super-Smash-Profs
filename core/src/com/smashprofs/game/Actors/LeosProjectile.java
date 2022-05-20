package com.smashprofs.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Game;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.CameraManager;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Helper.Util;
import com.smashprofs.game.Screens.PlayScreen;

import java.util.ArrayList;

import static com.smashprofs.game.Actors.Player.PPM;

public abstract class LeosProjectile extends GameObject {


    public static final float PPM = 100;
    private final World world;
    float stateTime = 0;
    Player originPlayer;
    private Vector2 projectileSpawnpoint;
    private double rotation = 0;


    private Vector2 position;

    private final B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();

    private BodyDef bdef;
    private final String userData;
    private float gravity = -0.098f;
    private Vector2 forcesCombined = new Vector2(0, 0);
    private final float startingGravity = gravity;
    public Boolean isActive = true;


    public LeosProjectile(World world, Player originPlayer, String userData, Texture projectileTex) {
        super(world, userData);

        sprite.setTexture(projectileTex);

        this.userData = userData;
        this.originPlayer = originPlayer;
        sprite.setBounds(0, 15, 32f / PPM, 32f / PPM);
        this.world = world;
        defineProjectile();
        moveProjectile();

    }

    public LeosProjectile getInstancePlayer(LeosProjectile player) {
        return this;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void update(float deltatime) {
        System.out.println(" - - - - - - - LeosProjectile update");

        if (deltatime == 0) return;
        if (deltatime > 0.1f) deltatime = 0.1f;
        stateTime += deltatime;


        if(!b2dbody.isActive()) {
          isActive = false;
        }

        sprite.setPosition(b2dbody.getPosition().x - sprite.getWidth() / 2f, b2dbody.getPosition().y - sprite.getHeight() / 2f);

        rotation = b2dbody.getAngle()*2*Math.PI*4;
        sprite.setRotation((float) rotation);

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }



    public float getStartingGravity() {
        return startingGravity;
    }


    public Body getB2dbody() {
        return b2dbody;
    }


    public BodyDef getBdef() {
        return bdef;
    }


    //basically our constructor
    private void defineProjectile() {
        bdef = new BodyDef();
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius() * 2.1f / PPM, originPlayer.getPosition().y);
        bdef.position.set(projectileSpawnpoint);
        //bdef.position.set(10 / PPM, 10 / PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = false;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(3 / PPM);

        fDef.shape = shape;

        //filters collisions with other objects:
        if (true) {
            fDef.filter.groupIndex = 0;
        }

        b2dbody.createFixture(fDef);
        sprite.setOrigin(sprite.getWidth()*0.5f, sprite.getHeight()*0.5f);

    }

    void moveProjectile() {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * 1.2f, 0));
    }

}
