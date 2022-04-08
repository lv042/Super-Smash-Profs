package com.smashprofs.game.Helper;

import com.badlogic.gdx.math.Vector2;
import com.smashprofs.game.Sprites.PlayerClass;

public class CombatManager {
    private Vector2 distanceBetweenPlayers = new Vector2(0, 0);
    private float distanceBetweenPlayersLength = distanceBetweenPlayers.len();

    private static final CombatManager combatManager_INSTANCE = new CombatManager();

    //private constructor to avoid client applications to use constructor
    private CombatManager() {
    }

    public static CombatManager getCombatManager_INSTANCE() {
        return combatManager_INSTANCE;
    }

    public void update(float deltatime, PlayerClass playerOne, PlayerClass playerTwo) {
        distanceBetweenPlayers = new Vector2(Math.abs(playerOne.getPosition().x - playerTwo.getPosition().x), Math.abs(playerOne.getPosition().y - playerTwo.getPosition().y));
        distanceBetweenPlayersLength = distanceBetweenPlayers.len();
        System.out.println(distanceBetweenPlayersLength);

        if (distanceBetweenPlayersLength < playerOne.getAttackReach()) {
            System.out.println("Player is in range attack range");
            if(playerOne.isStandardAttackInput()){
                System.out.println("Player is attacking");
                //take damage and yeeeeeet
                playerTwo.getB2dbody().applyLinearImpulse(new Vector2(2, 2), playerOne.getB2dbody().getWorldCenter(), true);
            }
            if(playerTwo.isStandardAttackInput()){
                System.out.println("Player is attacking");
                //take damage and yeeeeeet
                playerOne.getB2dbody().applyLinearImpulse(new Vector2(2, 2), playerOne.getB2dbody().getWorldCenter(), true);
            }
        }


    }
}
