package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class HomingMissle extends Projectile{

    Vector2 targetVector = new Vector2();
    PlayerClass targetPlayer;

    public HomingMissle(World world, PlayerClass playerOrigin, OrthographicCamera camera, PlayerClass playerTarget){
        super(world, playerOrigin, "Bullet" ,  camera);
        b2dbody.setFixedRotation(false);
        b2dbody.setAngularVelocity(10);
        this.targetPlayer = playerTarget;
    }
    @Override
    void moveProjectile(){
        return; // movement is handled in update
    }

    @Override
    public void update(float delta){
        Vector2 playerPos = targetPlayer.getPosition();
        Vector2 targetVector = new Vector2(playerPos.x - b2dbody.getPosition().x, playerPos.y - b2dbody.getPosition().y);
        b2dbody.setLinearVelocity(targetVector.nor().scl(0.4f));
    }
}
