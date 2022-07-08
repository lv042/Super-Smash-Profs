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
 * A retro IBM PC that will give you a bad headache if you're not careful enough!
 */
public class IBM extends Projectile {
    private static Logger log = LogManager.getLogger(HomingMissile.class);


    private Vector2 projectileDirection;

    private PlayerView originPlayer;
    private Random rand = new Random();
    private float speed = 0.7f;
    private float rotationSpeed = 4f;

    /**
     * Default constructor for the IBM retro PC.
     * @param world
     * The world the IBM PC will spawn in.
     * @param playerOrigin
     * The origin player of the IBM PC.
     */
    public IBM(World world, PlayerView playerOrigin){
        super(world, playerOrigin, "IBM" , ShapeCreator.getCircleShape(3f), playerOrigin.getPlayerCollisionBoxRadiusView()*3f,  new Texture("projectiles/ibmpc.png"), 10, 3f, B2dContactListener.PROJECTILE_ENTITY);

        int randInt = rand.nextInt(9999);
        userData = "IBM#" + randInt;
        b2dbody.setUserData("IBM#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        b2dbody.setAngularVelocity(rotationSpeed);
        sprite.setScale(0.045f, 0.045f);
        sprite.flip(true, false);
        log.debug("Created new IBM: " + this);
        this.originPlayer = playerOrigin;
        projectileDirection = new Vector2(originPlayer.getIsFacingRightAxeView() * speed, 0);

    }





    @Override
    public void update(float delta){
        super.update(delta);
        moveProjectile(delta);
    }

    private void moveProjectile(float delta) {
        b2dbody.setLinearVelocity(projectileDirection);
    }
}
