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

    public ThrowingStar(World world, Vector2 originPosition) {
        super(world, originPosition, "Star", new Texture("star.png"), 15);

        int randInt = rand.nextInt(9999);
        userData = "Star#" + randInt;
        b2dbody.setUserData("Star#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        b2dbody.setAngularVelocity(projectileRotationSpeed);
        sprite.flip(true, false);
        sprite.setScale(0.5f, 0.5f);
        sprite.setScale(5f, 5f);


    }
    @Override
    void initialMovement() {
        //No movement
        // init movement is handled in update
    }

    @Override
    public void update(float delta){
        super.update(delta);

    }


}
