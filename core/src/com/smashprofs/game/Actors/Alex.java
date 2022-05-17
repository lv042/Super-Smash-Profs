package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Alex extends Player {


    public Alex(World world, InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, userData, new Texture("Sprites/Alex_stand.png"), new Texture("Sprites/Alex_run.png"), new Texture("Sprites/Alex_jump.png"));


    }


}
