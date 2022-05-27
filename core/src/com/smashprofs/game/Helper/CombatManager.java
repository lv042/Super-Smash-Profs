package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.smashprofs.game.Actors.Projectiles.HomingMissile;

import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Actors.Projectiles.HomingMissile;
import com.smashprofs.game.Actors.Projectiles.Projectile;
import com.smashprofs.game.Actors.Projectiles.ThrowingStar;

import java.util.ArrayList;

public class CombatManager {
    private Vector2 distanceBetweenPlayers = new Vector2(0, 0);

    private B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private float distanceBetweenPlayersLength = distanceBetweenPlayers.len();

    private static final CombatManager combatManager_INSTANCE = new CombatManager();

    public static ArrayList<Projectile> projectileArrayList = new ArrayList<Projectile>();

    public static ArrayList<Projectile> projectilesToDestroy = new ArrayList<Projectile>();


    //private constructor to avoid client applications to use constructor
    private CombatManager() {
    }

    public static CombatManager getCombatManager_INSTANCE() {
        return combatManager_INSTANCE;
    }

    public void update(float deltatime, Player playerOne, Player playerTwo, World world)  {
        distanceBetweenPlayers = new Vector2(Math.abs(playerOne.getPosition().x - playerTwo.getPosition().x), Math.abs(playerOne.getPosition().y - playerTwo.getPosition().y));
        distanceBetweenPlayersLength = distanceBetweenPlayers.len();
        //System.out.println(distanceBetweenPlayersLength);


        updateProjectiles(deltatime);
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
        if(playerOne.isShooting()){
            System.out.println("Bullet spawned ");
            HomingMissile proj = new HomingMissile(world, playerOne, playerTwo);

            projectileArrayList.add(proj);

        }
        if(playerTwo.isShooting()) {
            System.out.println("Bullet spawned");
            ThrowingStar proj = new ThrowingStar(world, playerTwo);
            //HomingMissle proj = new HomingMissle(world, playerTwo, playerOne);

            projectileArrayList.add(proj);
        }

        if (contactListener.isPlayerTwoGotShoot()) {
            attackPlayer(playerOne, playerTwo, 1.5f, 2f);
            contactListener.setPlayerTwoGotShoot(false);
            contactListener.setBulletHit(false);
            System.out.println("abracadabra");
        }
        if (contactListener.isPlayerOneGotShoot()) {
            attackPlayer(playerTwo, playerOne, 1.5f, 2f);
            contactListener.setPlayerOneGotShoot(false);
            contactListener.setBulletHit(false);
            System.out.println("adadadadada");
        }




    }

    private void updateProjectiles(float deltatime) {
        for (Projectile projectile: projectileArrayList) {
            projectile.update(deltatime);
        }
        //System.out.println(projectileArrayList.size());
    }



    public void attackPlayer(Player attacker, Player target, float attackKnockback , float yAttackKnockback){
        Vector2 attackVector = new Vector2(0, yAttackKnockback);
        if(attacker.getPosition().x < target.getPosition().x) attackVector.x = 1 * attackKnockback;
        else attackVector.x = -1 * attackKnockback;


        target.applyForces(attackVector.x, attackVector.y );
        target.setHP(target.getHP() - target.getAttackDamage());
        soundManager.playSound(target.getDamageSoundMp3());
    }

    public void drawProjectiles(SpriteBatch batch){
        for (Projectile projectile: projectileArrayList) {
            // VORSICHT!! Könnte mit dem FATAL ERROR zusammenhängen!
//            if(projectile.active) {
//                projectile.draw(batch);
//            }
            projectile.draw(batch);

            //if(projectilesToDestroy.contains(projectile)) {
            //    projectile.sprite.setPosition(10000f, 10000f);
            //}

        }
    }
}
