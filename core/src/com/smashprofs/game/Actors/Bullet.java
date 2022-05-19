package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet extends Projectile{


    public Bullet(World world, Player playerOrigin, OrthographicCamera camera) {
        super(world, playerOrigin, "Bullet" ,  camera);
        userData = "Bullet";
    }


}
