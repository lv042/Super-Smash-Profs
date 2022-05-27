package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;

public class HomingMissile extends Projectile {

    Vector2 targetVector = new Vector2();
    Player targetPlayer;

    public HomingMissle(World world, Player playerOrigin, OrthographicCamera camera, Player playerTarget){
        super(world, playerOrigin, "Bullet" ,  camera);
        b2dbody.setFixedRotation(false);
        // Deactivated for performance benefits
        //b2dbody.setAngularVelocity(10);
        this.targetPlayer = playerTarget;
    }
    @Override
    void moveProjectile(){
        return; // movement is handled in update
    }

    @Override
    public void update(float delta){
        super.update(delta);
        System.out.println("target: " + targetPlayer);
        Vector2 tartgetPlayPos = targetPlayer.getPosition();
        Vector2 targetVector = new Vector2(tartgetPlayPos.x - b2dbody.getPosition().x, tartgetPlayPos.y - b2dbody.getPosition().y);
        b2dbody.setLinearVelocity(targetVector.nor().scl(0.4f));
    }
}
