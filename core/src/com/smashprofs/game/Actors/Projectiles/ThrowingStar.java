package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Actors.Players.Player;

public class ThrowingStar extends Projectile {


    public ThrowingStar(World world, Player playerOrigin) {
        super(world, playerOrigin, "Bullet", new Texture("star.png"));
        userData = "Bullet";
    }

    @Override
    public void update(float delta){
        super.update(delta);
        rotation = -2.25*(b2dbody.getAngle()*2*Math.PI*4);
        sprite.setRotation((float)rotation);
        //sprite.rotate( delta * rotationFactor);

    }


}
