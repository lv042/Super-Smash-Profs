package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Landmine extends Projectile {

    public Landmine(World world, Player originPlayer) {
        super(world, originPlayer, "Landmine", new Texture("Fireball2.png"));
        b2dbody.setFixedRotation(false);
    }


    @Override
    public void update(float delta){
        super.update(delta);
    }
}
