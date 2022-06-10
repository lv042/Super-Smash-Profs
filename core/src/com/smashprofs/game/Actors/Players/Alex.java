package com.smashprofs.game.Actors.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Helper.PlayerTypes;

public class Alex extends Player {


    public Alex(World world, InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, PlayerTypes.Alex, userData, new Texture("Sprites/Alex/alex_stand.png"), new Texture("Sprites/Alex/alex_run.png"), new Texture("Sprites/Alex/alex_run.png"));


    }


}
