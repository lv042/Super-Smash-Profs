package com.smashprofs.game.Actors;

import com.badlogic.gdx.physics.box2d.*;

import static com.smashprofs.game.Actors.PlayerClass.PPM;

public class Bullet extends Projectile{

    private BodyDef bdef;

    private Body body;
    private Body b2dbody;

    World world;



    public Bullet(World world, PlayerClass playerOrigin) {
        super(world, playerOrigin);
    }

}
