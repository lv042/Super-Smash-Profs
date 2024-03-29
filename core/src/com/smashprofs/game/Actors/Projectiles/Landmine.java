package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Actors.Players.PlayerView;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.ShapeCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * A placeable landmine which falls to the ground and explodes on contact with anything other than the ground.
 */
public class Landmine extends Projectile {
    private static Logger log = LogManager.getLogger(Landmine.class);


    private Random rand = new Random();

    @Override

    void initialMovement(float speed) {
        //No initial velocity for the landmine
    }

    /**
     * Creates a Landmine in front of the originPlayer
     * @param world
     * The world the Landmine exists in
     * @param originPlayer
     * The player the mine will spawn in front of
     */
    public Landmine(World world, PlayerView originPlayer) {
        super(world, originPlayer, "Mine", ShapeCreator.getCircleShape(5), 15f, new Texture("projectiles/landminesmall.png"), 25, 5f, B2dContactListener.PROJECTILE_ENTITY);

        // Create random user data
        userData = "Mine " + rand.nextInt(9999);
        b2dbody.setUserData("Mine " + rand.nextInt(9999));

        b2dbody.setFixedRotation(false);
        log.debug("Created new Landmine: " + this);
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
    }

    /**
     * Applies gravity as a linear impulse to the world center
     * @param delta
     * The game delta time
     */
    private void applyGravity(float delta) {
        b2dbody.applyLinearImpulse(gravityVector, b2dbody.getWorldCenter(), true);
        // log.debug("Applied gravity to Landmine.");
    }

}


