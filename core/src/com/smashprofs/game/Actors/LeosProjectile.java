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

public abstract class LeosProjectile extends GameObject {


    public static final float PPM = 100;
    private final World world;
    float stateTime = 0;
    Player originPlayer;

    private Vector2 position;

    private final B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();

    private BodyDef bdef;
    private final String userData;
    private float gravity = -0.098f;
    private Vector2 forcesCombined = new Vector2(0, 0);
    private final float startingGravity = gravity;
    public Boolean isActive = true;


    public LeosProjectile(World world, String userData, Texture projectileTex) {
        super(world, userData);

        sprite.setTexture(projectileTex);

        this.userData = userData;
        this.originPlayer = originPlayer;
        sprite.setBounds(0, 15, 32 / PPM, 32 / PPM);
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

    public void updatePosition(float deltatime) {
        position = b2dbody.getPosition();
    }

    @Override
    public void update(float deltatime) {
        System.out.println(" - - - - - - - LeosProjectile update");
        updatePosition(deltatime);
        applyForces(0, 10);

        if (deltatime == 0) return;
        if (deltatime > 0.1f) deltatime = 0.1f;
        stateTime += deltatime;

        setAnimationPosition();

        if(!b2dbody.isActive()) {
          isActive = false;
        }

        sprite.setRotation(b2dbody.getAngle());

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }


    private void setAnimationPosition() {
        sprite.setPosition(b2dbody.getPosition().x - sprite.getWidth() / 2, b2dbody.getPosition().y - sprite.getHeight() / 4); //set the position of the animation to the center of the body
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
        //bdef.position.set(spawnpoint.x / PPM, spawnpoint.y / PPM);
        bdef.position.set(10 / PPM, 10 / PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = false;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(3 / PPM);

        fDef.shape = shape;

        b2dbody.createFixture(fDef);

    }

    void moveProjectile() {

        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * 1.2f, 0));
    }

    public void applyForces(float x, float y) {

        //all forces applied to the player should be done with this method
        forcesCombined = new Vector2(0 + x, gravity + y);
        getB2dbody().applyLinearImpulse(forcesCombined, getB2dbody().getWorldCenter(), true);
        forcesCombined.setZero();
    }


}
