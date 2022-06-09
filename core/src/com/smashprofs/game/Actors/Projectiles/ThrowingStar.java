package com.smashprofs.game.Actors.Projectiles;
import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Helper.ShapeCreator;

/**
 * A throwing star which the player will throw in a specific direction
 */
public class ThrowingStar extends Projectile {
    Random rand = new Random();
    private float projectileSpeed = 10;
    private float projectileRotationSpeed = -12f;

    Vector2 projectileDirection;

    /**
     * Spawns a throwing star with a specific moving speed pointing in a specific direction next to the originPosition
     * @param world
     * The world the star will exist in
     * @param originPosition
     * The position the star will spawn at
     * @param direction
     * The direction the star will fly in
     */
    public ThrowingStar(World world, Vector2 originPosition, Vector2 direction) {
        super(world, originPosition, "Star", ShapeCreator.getCircleShape(3f), 3f, new Texture("projectiles/star.png"), 15);



        int randInt = rand.nextInt(9999);
        userData = "Star#" + randInt;
        b2dbody.setUserData("Star#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        b2dbody.setAngularVelocity(projectileRotationSpeed);
        sprite.flip(true, false);
        sprite.setScale(0.5f, 0.5f);

        projectileDirection = direction;
    }
    @Override
    void initialMovement() {

    }

    public void moveProjectile(){
        b2dbody.setLinearVelocity(projectileDirection);
    }

    @Override
    public void update(float delta){
        super.update(delta);
        moveProjectile();

    }




}
