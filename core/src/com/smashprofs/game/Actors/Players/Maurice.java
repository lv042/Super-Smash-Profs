package com.smashprofs.game.Actors.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Helper.PlayerTypes;

public class Maurice extends Player {


    public Maurice(World world, Player.InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, PlayerTypes.Maurice, userData, new Texture("Sprites/Momo/momo_strip.png"), new Texture("Sprites/Momo/momo_run.png"), new Texture("Sprites/Momo/momo_run.png"), new Texture("Sprites/Alex/alex_punch.png"));
        // TODO: Change punch texture to momo_punch.png when file available.


    }


}
