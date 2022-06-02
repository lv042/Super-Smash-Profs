package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Helper.ShapeCreator;

import java.util.Random;

import static com.smashprofs.game.Actors.Players.Player.PPM;

/**
 * A missile that will follow its target
 */
public class HomingMissile extends Projectile {

    /**
     * The vector pointing to the target position
     */
    Vector2 targetVector = new Vector2();
    /**
     * The targetPlayer the missile will try to hit
     */
    Player targetPlayer;
    Random rand = new Random();
    CircleShape bodyShape = new CircleShape();

    /**
     * Controls the flight speed of the missile
     */
    float speed = 0.7f;

    /**
     * Controls how fast the missile will react to the movement of the targetPlayer
     */
    float missileInertia = 0.05f;

    /**
     * Create a homing missile which will try to hit its targetPlayer
     * @param world
     * The world the missile will exist in
     * @param playerOrigin
     * The player who fired the missile
     * @param playerTarget
     * The player the missile should fly to
     */
    public HomingMissile(World world, Player playerOrigin, Player playerTarget){
        super(world, playerOrigin, "Bullet" , ShapeCreator.getCircleShape(3f), playerOrigin.getPlayerCollisionBoxRadius()*3f,  new Texture("projectiles/missile.png"), 10);

        int randInt = rand.nextInt(9999);
        userData = "Bullet#" + randInt;
        b2dbody.setUserData("Bullet#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        //b2dbody.setAngularVelocity(10);
        this.targetPlayer = playerTarget;
        sprite.flip(true, false);
    }
    @Override
    void initialMovement() {
        super.initialMovement();
        return; // movement is handled in update
    }

    public CircleShape generateBodyShape() {
        bodyShape.setRadius(3/PPM);
        return bodyShape;
    }




    @Override
    public void update(float delta){
        super.update(delta);
        Vector2 tartgetPlayPos = targetPlayer.getPosition();
        Vector2 newTargetVector = new Vector2(tartgetPlayPos.x - b2dbody.getPosition().x, tartgetPlayPos.y - b2dbody.getPosition().y);
        targetVector = targetVector.lerp(newTargetVector, missileInertia);
        b2dbody.setLinearVelocity(targetVector.nor().scl(0.5f));
        sprite.setRotation(targetVector.angleDeg());
    }
}
