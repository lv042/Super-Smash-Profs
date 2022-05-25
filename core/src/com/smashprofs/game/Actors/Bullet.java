package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

public class Bullet extends Projectile{


    public Bullet(World world, Player playerOrigin) {
        super(world, playerOrigin, "Bullet", new Texture("fireball.png"));
        userData = "Bullet";
    }


}
