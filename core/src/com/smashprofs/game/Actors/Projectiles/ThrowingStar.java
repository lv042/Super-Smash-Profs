package com.smashprofs.game.Actors.Projectiles;
import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;

import static jdk.nashorn.internal.runtime.regexp.joni.Syntax.Java;

public class ThrowingStar extends Projectile {
    Random rand = new Random();


    public ThrowingStar(World world, Player playerOrigin) {
        super(world, playerOrigin, "Null", new Texture("star.png"));

        userData = "Star " + rand.nextInt(9999);
        b2dbody.setUserData("Star " + rand.nextInt(9999));
    }

    @Override
    public void update(float delta){
        super.update(delta);
        rotation = -2.25*(b2dbody.getAngle()*2*Math.PI*4);
        sprite.setRotation((float)rotation);
        //sprite.rotate( delta * rotationFactor);

    }


}
