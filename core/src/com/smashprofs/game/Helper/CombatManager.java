package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.smashprofs.game.Actors.Projectiles.*;

import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Actors.Projectiles.HomingMissile;
import com.smashprofs.game.Actors.VFXObject;
import com.smashprofs.game.Screens.PlayScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class CombatManager {
    private Vector2 distanceBetweenPlayers = new Vector2(0, 0);

    private B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();

    private SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private float distanceBetweenPlayersLength = distanceBetweenPlayers.len();

    private static final CombatManager combatManager_INSTANCE = new CombatManager();

    public static DelayedRemovalArray<Projectile> projectileArrayList = new DelayedRemovalArray<>();

    private VAFXManager vfxManager = VAFXManager.getVFXManager_INSTANCE();
    private VAFXManager vafxManager = VAFXManager.getVFXManager_INSTANCE();

    private static Logger log = LogManager.getLogger(CombatManager.class);



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

            //Landmine proj = new Landmine(world,playerOne);

        }
        if(playerOne.isShooting()){
            //System.out.println("Bullet spawned ");
            log.info("Bullet spawned");
            HomingMissile proj = new HomingMissile(world, playerOne, playerTwo);
            // CircleStar proj = new CircleStar(world, playerOne);

            projectileArrayList.add(proj);

        }
        if(playerTwo.isShooting()) {
            //System.out.println("Bullet spawned");
            log.info("Bullet spawned");
            //ThrowingStar proj = new ThrowingStar(world, playerTwo);
            ThrowingStar proj = new ThrowingStar(world, new Vector2(playerTwo.getPosition().x + 10 / PPM, playerTwo.getPosition().y) , new Vector2(1,0));
            projectileArrayList.add(proj);

            ThrowingStar proj1 = new ThrowingStar(world, new Vector2(playerTwo.getPosition().x - 10 / PPM, playerTwo.getPosition().y), new Vector2(-1,0));
            projectileArrayList.add(proj1);

            ThrowingStar proj2 = new ThrowingStar(world, new Vector2(playerTwo.getPosition().x, playerTwo.getPosition().y + 10 / PPM), new Vector2(0,1));
            projectileArrayList.add(proj2);

            ThrowingStar proj3 = new ThrowingStar(world, new Vector2(playerTwo.getPosition().x, playerTwo.getPosition().y - 10 / PPM), new Vector2(0,-1));
            projectileArrayList.add(proj3);









            //HomingMissile proj = new HomingMissile(world, playerTwo, playerOne);
            //Landmine proj = new Landmine(world, playerTwo);
            //proj.setCurrentState(Landmine.State.EXPLODING);

            //
            // vfxManager.spawnExplosion(VFXManager.explosionType.rocketExplosion, playerTwo.getPosition());


        }

        //BULLETS

        if (contactListener.isPlayerTwoGotShoot()) {
            attackPlayer(playerOne, playerTwo, 1.5f, 2f);
            contactListener.setPlayerTwoGotShoot(false);
            contactListener.setBulletHit(false);
            //System.out.println("abracadabra");
        }
        if (contactListener.isPlayerOneGotShoot()) {
            attackPlayer(playerTwo, playerOne, 1.5f, 2f);
            contactListener.setPlayerOneGotShoot(false);
            contactListener.setBulletHit(false);
            //System.out.println("adadadadada");
        }

        //LANDMINES

        if (contactListener.isPlayerTwoGotShoot()) {
            attackPlayer(playerOne, playerTwo, 1.5f, 2f);
            contactListener.setPlayerTwoGotShoot(false);
            contactListener.setBulletHit(false);
            //System.out.println("abracadabra");
        }
        if (contactListener.isPlayerOneGotShoot()) {
            attackPlayer(playerTwo, playerOne, 1.5f, 2f);
            contactListener.setPlayerOneGotShoot(false);
            contactListener.setBulletHit(false);
            //System.out.println("adadadadada");
        }





    }


    //Projectile attack

    public void attackPlayer(Vector2 damageOrigin, int damage, Player target, float attackKnockback , float yAttackKnockback){
        Vector2 attackVector = new Vector2(0, yAttackKnockback);
        if(damageOrigin.x < target.getPosition().x) attackVector.x = 1 * attackKnockback;
        else attackVector.x = -1 * attackKnockback;


        target.applyForces(attackVector.x, attackVector.y );
        target.setHP(target.getHP() - damage);
        soundManager.playSound("stomp.wav");
    }

    //Melee attack

    public void attackPlayer(Player attacker, Player target, float attackKnockback , float yAttackKnockback){
        Vector2 attackVector = new Vector2(0, yAttackKnockback);
        if(attacker.getPosition().x < target.getPosition().x) attackVector.x = 1 * attackKnockback;
        else attackVector.x = -1 * attackKnockback;


        target.applyForces(attackVector.x, attackVector.y );
        target.setHP(target.getHP() - target.getAttackDamage());
        soundManager.playSound(target.getDamageSoundMp3());
    }


    private void updateProjectiles(float deltatime) {
        for (Projectile projectile: projectileArrayList) {
            //TODO: @Alex Bitte die auskommentierten sout's als log.debug übernehmen :)
            projectile.update(deltatime);
            //System.out.println("Is active before loop: " + projectile.active);
            log.debug("Is active before loop: " + projectile.active);
            if(!projectile.active) {
                //System.out.println("Trying to remove body");
                log.debug("Trying to remove body");
                projectile.destroyBody();
                projectileArrayList.removeValue(projectile, true);
            }
            else if(projectile.getBody().getUserData().equals("Destroyed")){

                projectile.active = false;
                //System.out.println("Projectile was set to inactive");
                log.debug("Projectile was set to inactive");
                //System.out.println("Is active: " + projectile.active);
                log.debug("Is active: " + projectile.active);

            }
        }
    }



    public void drawProjectiles(SpriteBatch batch){
        for (Projectile projectile: projectileArrayList) {

            // TODO: Gehört das so?
            if(projectile.active && projectile.b2dbody.isActive() && projectile.isActive()) {
                projectile.draw(batch);
            }

        }
    }

    public void resetCombatManager() {
        projectileArrayList.clear();
    }
}
