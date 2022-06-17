package com.smashprofs.game.Actors.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Helper.PlayerTypes;

/**
 * Maurice as a game character.
 * Special ability:
 * Multi-direction throwing star.
 */
public class Maurice extends Player {


    /**
     * Maurice's default constructor. Creates Maurice at the set spawnpoint.
     * @param world
     * The world Maurice will be created in.
     * @param inputState
     * The controls Maurice should listen to.
     * @param spawnpoint
     * The point in the world Maurice will spawn in.
     * @param playerName
     * The name Maurice will have in the game.
     * @param userData
     * Maurice's userData.
     */
    public Maurice(World world, Player.InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, PlayerTypes.Maurice, userData, new Texture("Sprites/Momo/momo_strip.png"), new Texture("Sprites/Momo/momo_run.png"), new Texture("Sprites/Momo/momo_run.png"), new Texture("Sprites/Momo/momo_punch.png"));
        // TODO: Change punch texture to momo_punch.png when file available.


    }


}
