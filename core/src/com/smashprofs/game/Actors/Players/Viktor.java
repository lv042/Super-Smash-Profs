package com.smashprofs.game.Actors.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Helper.PlayerTypes;

public class Viktor extends Player {

    public Viktor(World world, InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, PlayerTypes.Viktor, userData, new Texture("Sprites/Viktor/viktor_stand.png"),
                new Texture("Sprites/Viktor/viktor_run.png"), new Texture("Sprites/Viktor/viktor_stand.png"));


    }
}
