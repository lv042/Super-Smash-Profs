package com.smashprofs.game.Actors.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Helper.PlayerTypes;
/**
 * Viktor as a game character.
 * Special ability:
 * IBM retro PC.
 */
public class Viktor extends Player {
    /**
     * Viktor's default constructor. Creates Viktor at the set spawnpoint.
     * @param world
     * The world Viktor will be created in.
     * @param inputState
     * The controls Viktor should listen to.
     * @param spawnpoint
     * The point in the world Viktor will spawn in.
     * @param playerName
     * The name Viktor will have in the game.
     * @param userData
     * Viktor's userData.
     */
    public Viktor(World world, InputState inputState, Vector2 spawnpoint, String playerName, String userData) {
        super(world, inputState, spawnpoint, playerName, PlayerTypes.Viktor, userData, new Texture("Sprites/Viktor/viktor_stand.png"),
                new Texture("Sprites/Viktor/viktor_run.png"), new Texture("Sprites/Viktor/viktor_stand.png"), new Texture("Sprites/Viktor/viktor_punch.png"));
                // TODO: Change punch texture to viktor_punch.png when file available.

    }
}
