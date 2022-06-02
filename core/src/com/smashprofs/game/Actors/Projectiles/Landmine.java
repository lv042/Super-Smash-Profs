package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Helper.ShapeCreator;

import java.util.Random;

/**
 * A placeable landmine which falls to the ground and explodes on contact with anything other than the ground.
 */
public class Landmine extends Projectile {

    /**
     * The explosion animation
     */
    private final Animation<TextureRegion> mineExplode;
    /**
     * The idle "animation"
     */
    private final Animation<TextureRegion> mineWait;
    /**
     * The idle texture
     */
    private final Texture mineWaiting;
    /**
     * The explosion texture stripe
     */
    private final Texture mineExploding;
    float stateTime = 0;

    Random rand = new Random();

    @Override

    void initialMovement() {
        //No initial velocity for the landmine
    }

    /**
     * Creates a Landmine in front of the originPlayer
     * @param world
     * The world the Landmine exists in
     * @param originPlayer
     * The player the mine will spawn in front of
     */
    public Landmine(World world, Player originPlayer) {
        super(world, originPlayer, "Null", ShapeCreator.getPolygonShape(9, 3.5f), 25f, new Texture("projectiles/landmine.png"), 25);

        // Create random user data

        userData = "Mine " + rand.nextInt(9999);
        b2dbody.setUserData("Mine " + rand.nextInt(9999));

        b2dbody.setFixedRotation(false);

        this.mineWaiting = new Texture("projectiles/landmine.png");
        this.mineExploding = new Texture("explosions/explosion-2.png");

        TextureRegion[] exploding = TextureRegion.split(mineExploding, 64, 64)[0];
        mineExplode = new Animation(0.1f, exploding[0], exploding[1], exploding[2], exploding[3], exploding[4], exploding[5], exploding[6], exploding[7]);
        mineExplode.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] waiting = TextureRegion.split(mineWaiting, 32, 32)[0];
        mineWait = new Animation(0.25f, waiting[0]);
        mineWait.setPlayMode(Animation.PlayMode.NORMAL);


    }


    /**
     * Updates the Landmine gravity, stateTime, spriteRotation and animation state
     * @param delta
     * The game delta time
     */
    @Override
    public void update(float delta){
        super.update(delta);
        applyGravity(delta);
        rotation = -2.25*(b2dbody.getAngle()*2*Math.PI*4);
        sprite.setRotation( (float) rotation);
        //sprite.setRegion(getRenderTexture(stateTime)); //set the texture to the current state of the movement

    }

    /**
     * Applies gravity as a linear impulse to the world center
     * @param delta
     * The game delta time
     */
    private void applyGravity(float delta) {

        b2dbody.applyLinearImpulse(gravityVector, b2dbody.getWorldCenter(), true);

    }

/*    public TextureRegion getRenderTexture(float stateTime) {
        TextureRegion frame = null;
        switch (this.currentState) {
            case WAITING:
                sprite.setRegion(mineWaiting);
                frame = mineWait.getKeyFrame(stateTime);
                break;

            case EXPLODING:
                sprite.setRegion(mineExploding);
                frame = mineExplode.getKeyFrame(stateTime);
                break;

        }
        return frame;
    }*/


}


