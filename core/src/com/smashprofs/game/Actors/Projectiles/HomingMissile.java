package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Actors.Players.PlayerView;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.ShapeCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * A missile that will follow its target
 */
public class HomingMissile extends Projectile {
    private static Logger log = LogManager.getLogger(HomingMissile.class);

    /**
     * The vector pointing to the target position
     */
    private Vector2 targetVector = new Vector2();
    /**
     * The targetPlayer the missile will try to hit
     */
    private PlayerView targetPlayer;
    /**
     * The random for creating random integers used to append the userData.
     */
    private Random rand = new Random();

    /**
     * Controls the flight speed of the missile
     */
    private float speed = 0.7f;

    /**
     * Controls how fast the missile will react to the movement of the targetPlayer
     */
    private float missileInertia = 0.05f;

    /**
     * Create a homing missile which will try to hit its targetPlayer
     * @param world
     * The world the missile will exist in
     * @param playerOrigin
     * The player who fired the missile
     * @param playerTarget
     * The player the missile should fly to
     */
    public HomingMissile(World world, PlayerView playerOrigin, PlayerView playerTarget){
        super(world, playerOrigin, "Rocket" , ShapeCreator.getCircleShape(3f), playerOrigin.getPlayerCollisionBoxRadiusView()*3f,  new Texture("projectiles/missile.png"), 10, 3f, B2dContactListener.PROJECTILE_ENTITY);

        int randInt = rand.nextInt(9999);
        userData = "Rocket#" + randInt;
        b2dbody.setUserData("Rocket#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        //b2dbody.setAngularVelocity(10);
        this.targetPlayer = playerTarget;
        sprite.flip(true, false);
        log.debug("Created new HomingMissile: " + this);
    }

    /**
     * Applies the initialMovement to the HomingMissile
     */
    @Override
    void initialMovement(float speed) {
        super.initialMovement(speed);
        log.debug("Applied initial movement to HomingMissile.");
        // movement is handled in update
    }

    /**
     * Updates and interpolates the targetVector, corrects the missiles course to
     * hit the target, sets the rotation of the sprite to the angle of the targetVector.
     * @param delta
     * The game delta time
     */
    @Override
    public void update(float delta){
        super.update(delta);
        Vector2 tartgetPlayPos = targetPlayer.getPositionView();
        Vector2 newTargetVector = new Vector2(tartgetPlayPos.x - b2dbody.getPosition().x, tartgetPlayPos.y - b2dbody.getPosition().y);
        targetVector = targetVector.lerp(newTargetVector, missileInertia);
        b2dbody.setLinearVelocity(targetVector.nor().scl(0.5f));
        sprite.setRotation(targetVector.angleDeg());
    }
}
