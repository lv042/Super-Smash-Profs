package com.smashprofs.game.Actors.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Luca extends Player {

    public Luca(World world, InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, userData, new Texture("Sprites/Luca/luca_stand.png"), new Texture("Sprites/Luca/luca_run.png"), new Texture("Sprites/Luca/luca_run.png"));


    }
}
