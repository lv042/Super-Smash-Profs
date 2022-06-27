package com.smashprofs.game.Actors.Players;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * The interface for Player to prevent variable manipulation.
 */
public interface PlayerView {
    public Vector2 getPositionView();
    public Sprite getPlayerSpriteView();
    public int getIsFacingRightAxeView();
    public float getHP();
    public String getPlayerName();
}
