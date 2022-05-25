package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Player;
public class HomingMissle extends Projectile{

    Vector2 targetVector = new Vector2();
    Player targetPlayer;

    public HomingMissle(World world, Player playerOrigin, Player playerTarget){
        super(world, playerOrigin, "Bullet", new Texture("missile.png"));
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
        super.update(delta);
        System.out.println("target: " + targetPlayer);
        Vector2 targetPlayPos = targetPlayer.getPosition();
        Vector2 targetVector = new Vector2(targetPlayPos.x - b2dbody.getPosition().x, targetPlayPos.y - b2dbody.getPosition().y);
        b2dbody.setLinearVelocity(targetVector.nor().scl(0.4f));

    }
}
