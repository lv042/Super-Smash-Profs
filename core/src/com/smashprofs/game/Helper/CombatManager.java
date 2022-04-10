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

            float attackKnockback = 1.5f;


            Vector2 From1To2 = new Vector2((playerTwo.getPosition().x - playerOne.getPosition().x) * 1, ((playerTwo.getPosition().y - playerOne.getPosition().y) + 0.05f) * attackKnockback);
            Vector2 From2To1 = new Vector2((playerOne.getPosition().x - playerTwo.getPosition().x) * 1, ((playerOne.getPosition().y - playerTwo.getPosition().y) + 0.05f) * attackKnockback);

            if(From1To2.x < 0) From1To2.x = -1 * attackKnockback;
            if(From1To2.x > 0) From1To2.x = 1 * attackKnockback;

            if(From2To1.x < 0) From2To1.x = -1 * attackKnockback;
            if(From2To1.x > 0) From2To1.x = 1 * attackKnockback;

            //From1To2 = From1To2.nor();
            //From2To1 = From2To1.nor(); //normalize


            float DirectionRightValue = 1;
            //System.out.println("Player is in range attack range");
            if(playerOne.isStandardAttackInput()){
                //System.out.println("Player is attacking");
                //take damage and yeeeeeet

                playerTwo.getB2dbody().applyLinearImpulse(From1To2, playerOne.getB2dbody().getWorldCenter(), true);
                playerTwo.setHP(playerTwo.getHP() - playerOne.getAttackDamage());
            }
            if(playerTwo.isStandardAttackInput()){
               //System.out.println("Player is attacking");
                // take damage and yeeeeeet

                playerOne.getB2dbody().applyLinearImpulse(From2To1, playerOne.getB2dbody().getWorldCenter(), true);
                playerOne.setHP(playerOne.getHP() - playerOne.getAttackDamage());
            }
        }


    }
}
