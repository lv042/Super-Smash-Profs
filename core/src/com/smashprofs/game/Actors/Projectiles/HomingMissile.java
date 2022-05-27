package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;

import java.util.Random;

public class HomingMissile extends Projectile {

    Vector2 targetVector = new Vector2();
    Player targetPlayer;
    Random rand = new Random();

    public HomingMissile(World world, Player playerOrigin, Player playerTarget){
        super(world, playerOrigin, "Bullet" ,  new Texture("missile.png"));
        userData = "Bullet#" + rand.nextInt(9999);

        b2dbody.setUserData("Bullet#" + rand.nextInt(9999));


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

    @Override
    public void update(float delta){
        super.update(delta);
        Vector2 tartgetPlayPos = targetPlayer.getPosition();
        Vector2 targetVector = new Vector2(tartgetPlayPos.x - b2dbody.getPosition().x, tartgetPlayPos.y - b2dbody.getPosition().y);
        b2dbody.setLinearVelocity(targetVector.nor().scl(0.1f));
        sprite.setRotation(targetVector.angleDeg());
    }
}
