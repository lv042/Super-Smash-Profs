package com.smashprofs.game.Actors.Projectiles;
import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.ShapeCreator;

import static com.smashprofs.game.Actors.Players.Player.PPM;


public class CircleStar extends Projectile {
    private Random rand = new Random();
    private float projectileSpeed = 5;
    private float projectileRotationSpeed = -12f;

    Player playerOrigin;



    public CircleStar(World world, PlayerView playerOrigin) {
        // super(world, playerOrigin.getPosition(), "Circle", ShapeCreator.getCircleShape(3f), 8f, new Texture("projectiles/star.png"), 15, 2f, B2dContactListener.CIRCLESTAR_ENTITY);
        super(world, playerOrigin, "Circle", ShapeCreator.getCircleShape(3f), 20f, new Texture("projectiles/star.png"), 15, 2f, B2dContactListener.CIRCLESTAR_ENTITY);

        int randInt = rand.nextInt(9999);
        userData = "Circle#" + randInt;
        b2dbody.setUserData("Circle#" + randInt);


        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        b2dbody.setAngularVelocity(projectileRotationSpeed);
        sprite.flip(true, false);
        sprite.setScale(0.7f, 0.7f);
        this.playerOrigin = playerOrigin;

    }
    @Override
    void initialMovement(float speed) {
        // no initial movement
    }

    public void moveProjectile(float speed, Vector2 center){
//        Vector2 radius = center.cpy().sub(this.b2dbody.getPosition());
//        Vector2 force = radius.rotate90(1).nor().scl(speed);
//        this.b2dbody.setLinearVelocity(force.x, force.y);


    }

    public void calculateOrbit(float currentOrbitDegrees, float distanceFromCenterPoint, Vector2 centerPoint) {
        float radians = (float) Math.toRadians(currentOrbitDegrees) * 10;

        float x = (float) ((Math.cos(radians) * distanceFromCenterPoint) + centerPoint.x);
        float y = (float) ((Math.sin(radians) * distanceFromCenterPoint) + centerPoint.y);

        b2dbody.setTransform(x, y, b2dbody.getAngle());

    }


    @Override
    public void update(float delta){
        super.update(delta);
        moveProjectile(projectileSpeed, playerOrigin.getB2dbody().getPosition());
        calculateOrbit(b2dbody.getAngle(), 25 / PPM, new Vector2
                (playerOrigin.getPlayerSpriteView().getX() + (playerOrigin.getPlayerSpriteView().getWidth() / 2),
                        playerOrigin.getPlayerSpriteView().getY() + (playerOrigin.getPlayerSpriteView().getHeight() / 2)));
    }




}
