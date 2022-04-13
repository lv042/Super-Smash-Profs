package com.smashprofs.game.Helper;

import com.badlogic.gdx.math.Vector2;
import com.smashprofs.game.Actors.PlayerClass;

public class CombatManager {
    private Vector2 distanceBetweenPlayers = new Vector2(0, 0);

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

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





            //System.out.println("Player is in range attack range");
            if(playerOne.isStandardAttackInput() && !playerTwo.isBlocking()){
                //System.out.println("Player is attacking");
                //take damage and yeeeeeet

                //playerTwo.getB2dbody().applyLinearImpulse(From1To2, playerOne.getB2dbody().getWorldCenter(), true);
                //playerTwo.setHP(playerTwo.getHP() - playerOne.getAttackDamage());


                attackPlayer(playerOne, playerTwo, attackKnockback , 0.2f);

            }
            if(playerTwo.isStandardAttackInput() && !playerOne.isBlocking()){
               //System.out.println("Player is attacking");
                // take damage and yeeeeeet

                //playerOne.getB2dbody().applyLinearImpulse(From2To1, playerOne.getB2dbody().getWorldCenter(), true);
                //playerOne.setHP(playerOne.getHP() - playerOne.getAttackDamage());
                attackPlayer(playerTwo, playerOne, attackKnockback , 2f);

            }
            attackKnockback = 2f;
            if(playerOne.isStompHitground())attackPlayer(playerOne, playerTwo, attackKnockback, 2f);
            if(playerTwo.isStompHitground())attackPlayer(playerTwo, playerOne, attackKnockback, 2f);

        }




    }

    public void attackPlayer(PlayerClass attacker, PlayerClass target, float attackKnockback ,float yAttackKnockback){
        Vector2 attackVector = new Vector2(0, yAttackKnockback);
        if(attacker.getPosition().x < target.getPosition().x) attackVector.x = 1 * attackKnockback;
        else attackVector.x = -1 * attackKnockback;


        target.applyForces(attackVector.x, attackVector.y );
        target.setHP(target.getHP() - target.getAttackDamage());
        soundManager.playSound(target.getDamageSoundMp3());
    }
}
