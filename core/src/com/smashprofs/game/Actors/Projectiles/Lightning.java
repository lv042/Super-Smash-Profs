package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;

public class Lightning extends Projectile{


    public Lightning(World world, Player originPlayer, String userData, Texture projectileTexture, int damageOnHit) {
        super(world, originPlayer, userData, projectileTexture, damageOnHit);
    }



}
