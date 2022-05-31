package com.smashprofs.game.Actors.Projectiles;
import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;

//import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

public class ThrowingStar extends Projectile {
    Random rand = new Random();
    private float projectileSpeed = 10;
    private float projectileRotationSpeed = -12f;

    Vector2 projectileDirection;




    public ThrowingStar(World world, Player playerOrigin) {
        super(world, playerOrigin, "Star", new Texture("star.png"), 15);

        int randInt = rand.nextInt(9999);
        userData = "Star#" + randInt;
        b2dbody.setUserData("Star#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        b2dbody.setAngularVelocity(projectileRotationSpeed);
        sprite.flip(true, false);
        sprite.setScale(0.5f, 0.5f);


    }

    public ThrowingStar(World world, Vector2 originPosition, Vector2 direction) {
        super(world, originPosition, "Star", new Texture("star.png"), 15);

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
