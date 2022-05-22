package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class LeosHomingMissle extends LeosProjectile{

    Vector2 targetVector = new Vector2();


    public LeosHomingMissle(World world, Player playerOrigin){
        super(world,playerOrigin, "Bullet", new Texture("Fireball2.png"));
        b2dbody.setFixedRotation(false);
        b2dbody.setAngularVelocity(10);

    }
    @Override
    void moveProjectile(){
        return; // movement is handled in update
    }

    @Override
    public void update(float delta){
        super.update(delta);

        Vector2 targetVector = new Vector2(25, 0);
        //b2dbody.setLinearVelocity(targetVector.nor().scl(0.4f));
    }
}
