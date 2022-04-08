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
        //System.out.println(distanceBetweenPlayersLength);

        if (distanceBetweenPlayersLength < playerOne.getAttackReach()) {

            float attackPower = 20f;

            Vector2 From1To2 = new Vector2((playerTwo.getPosition().x - playerOne.getPosition().x) * attackPower , ((playerTwo.getPosition().y - playerOne.getPosition().y) + 0.05f) * attackPower);
            Vector2 From2To1 = new Vector2((playerOne.getPosition().x - playerTwo.getPosition().x) * attackPower, ((playerOne.getPosition().y - playerTwo.getPosition().y) + 0.05f) * attackPower);

            From1To2 = From1To2.nor();
            From2To1 = From2To1.nor(); //normalize


            //System.out.println("Player is in range attack range");
            if(playerOne.isStandardAttackInput()){
                System.out.println("Player is attacking");
                //take damage and yeeeeeet
                playerTwo.getB2dbody().applyLinearImpulse(From1To2, playerOne.getB2dbody().getWorldCenter(), true);
            }
            if(playerTwo.isStandardAttackInput()){
                System.out.println("Player is attacking");
                //take damage and yeeeeeet
                playerOne.getB2dbody().applyLinearImpulse(From2To1, playerOne.getB2dbody().getWorldCenter(), true);
            }
        }


    }
}
