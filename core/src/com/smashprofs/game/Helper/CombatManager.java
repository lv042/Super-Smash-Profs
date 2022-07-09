package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Timer;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Actors.Projectiles.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class CombatManager {
    /**
     * The distance between the two players in the game world.
     */
    private Vector2 distanceBetweenPlayers = new Vector2(0, 0);


    private boolean mineAvailable = true;
    private boolean missileAvailable = true;
    private boolean throwingStarAvailable = true;
    private boolean circleStarAvailable = true;
    private boolean ibmAvailable = true;

    /**
     * The box2DContact listener.
     */
    private final B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();

    /**
     * The sound manager.
     */
    private final SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    /**
     * The distance between the two players as a length.
     */
    private float distanceBetweenPlayersLength = distanceBetweenPlayers.len();
    /**
     * The combat manager.
     */
    private static final CombatManager combatManager_INSTANCE = new CombatManager();
    /**
     * The list of projectiles who are currently existing in the game.
     */
    public static DelayedRemovalArray<Projectile> projectileArrayList = new DelayedRemovalArray<>();
    /**
     * The logger.
     */
    private static Logger log = LogManager.getLogger(CombatManager.class);
    private int totalCircles = 0;

    final private int maxCircles = 2;


    //private constructor to avoid client applications to use constructor
    private CombatManager() {
    }

    public static CombatManager getCombatManager_INSTANCE() {
        return combatManager_INSTANCE;
    }

    /**
     * Updates the combatManager. Updates all present projectiles.
     * Spawns projectiles if the players are shooting.
     * Hits the players if they got shot.
     *
     * @param deltatime The game delta time.
     * @param playerOne The first player of the game.
     * @param playerTwo The second player of the game.
     * @param world     The game world.
     */
    public void update(float deltatime, Player playerOne, Player playerTwo, World world) {
        distanceBetweenPlayers = new Vector2(Math.abs(playerOne.getPosition().x - playerTwo.getPosition().x), Math.abs(playerOne.getPosition().y - playerTwo.getPosition().y));
        distanceBetweenPlayersLength = distanceBetweenPlayers.len();


        updateProjectiles(deltatime);
        if (distanceBetweenPlayersLength < playerOne.getAttackReach()) {
            float attackKnockback = 1.5f;
            if (playerOne.isStandardAttackInput() && !playerTwo.isBlocking()) {
                attackPlayer(playerOne, playerTwo, attackKnockback, 0.2f);
            }
            if (playerTwo.isStandardAttackInput() && !playerOne.isBlocking()) {

                attackPlayer(playerTwo, playerOne, attackKnockback, 2f);
            }
            attackKnockback = 2f;
            if (playerOne.isStompHitground()) attackPlayer(playerOne, playerTwo, attackKnockback, 2f);
            if (playerTwo.isStompHitground()) attackPlayer(playerTwo, playerOne, attackKnockback, 2f);
        }
        if (playerOne.isShooting()) {
            shooting(playerOne, playerTwo, world);
        }
        if (playerTwo.isShooting()) {
            shooting(playerTwo, playerOne, world);
        }

        //RocketS

        if (contactListener.isPlayerTwoGotShoot()) {
            attackPlayer(playerOne, playerTwo, 1.5f, 2f);
            contactListener.setPlayerTwoGotShoot(false);
        }
        if (contactListener.isPlayerOneGotShoot()) {
            attackPlayer(playerTwo, playerOne, 1.5f, 2f);
            contactListener.setPlayerOneGotShoot(false);
        }

        //LANDMINES

        if (contactListener.isPlayerTwoGotShoot()) {
            attackPlayer(playerOne, playerTwo, 1.5f, 2f);
            contactListener.setPlayerTwoGotShoot(false);
        }
        if (contactListener.isPlayerOneGotShoot()) {
            attackPlayer(playerTwo, playerOne, 1.5f, 2f);
            contactListener.setPlayerOneGotShoot(false);
        }
    }

    private void shooting(Player playeractive, Player playerinactive, World world) {

        //Ansonsten aber strukturell ziemlich gut :)
        if (playeractive.getPlayerType() == PlayerTypes.Leo) {

            if (mineAvailable) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        mineAvailable = true;
                    }
                }, Landmine.getDelayInSeconds());

                log.debug("Rocket spawned");
                Landmine proj = new Landmine(world, playeractive);
                projectileArrayList.add(proj);
                mineAvailable = false;
            }

        } else if (playeractive.getPlayerType() == PlayerTypes.Maurice) {

            if (throwingStarAvailable) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        throwingStarAvailable = true;
                    }
                }, ThrowingStar.getDelayInSeconds());

                log.debug("ThrowingStar spawned");
                //ThrowingStar proj = new ThrowingStar(world, playerTwo);
                ThrowingStar proj = new ThrowingStar(world, new Vector2(playeractive.getPosition().x + 10 / PPM, playeractive.getPosition().y), new Vector2(1, 0));
                projectileArrayList.add(proj);

                ThrowingStar proj1 = new ThrowingStar(world, new Vector2(playeractive.getPosition().x - 10 / PPM, playeractive.getPosition().y), new Vector2(-1, 0));
                projectileArrayList.add(proj1);

                ThrowingStar proj2 = new ThrowingStar(world, new Vector2(playeractive.getPosition().x, playeractive.getPosition().y + 10 / PPM), new Vector2(0, 1));
                projectileArrayList.add(proj2);

                ThrowingStar proj3 = new ThrowingStar(world, new Vector2(playeractive.getPosition().x, playeractive.getPosition().y - 10 / PPM), new Vector2(0, -1));
                projectileArrayList.add(proj3);

                throwingStarAvailable = false;
            }

        } else if (playeractive.getPlayerType() == PlayerTypes.Alex) {

            if (missileAvailable) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        missileAvailable = true;
                    }
                }, HomingMissile.getDelayInSeconds());

                log.debug("Rocket spawned");
                HomingMissile proj = new HomingMissile(world, playeractive, playerinactive);
                projectileArrayList.add(proj);
                missileAvailable = false;
            }

        } else if (playeractive.getPlayerType() == PlayerTypes.Luca) {
            if (totalCircles < maxCircles) {

                if (circleStarAvailable) {

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            circleStarAvailable = true;
                        }
                    }, CircleStar.getDelayInSeconds());


                    log.debug("CircleStar spawned");
                    CircleStar proj = new CircleStar(world, playeractive);
                    projectileArrayList.add(proj);
                    totalCircles++;
                    circleStarAvailable = false;
                }

            }
        } else if (playeractive.getPlayerType() == PlayerTypes.Viktor) {
            if (ibmAvailable) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        ibmAvailable = true;
                    }
                }, IBM.getDelayInSeconds());

                log.debug("IBM spawned");
                IBM proj = new IBM(world, playeractive);
                projectileArrayList.add(proj);
                ibmAvailable = false;
            }
        }
    }

    //Projectile attack
    //kann das weg oder ist das kunst??????????????????????????????????????????
    // A: HdM = Hochschule der Medien => Medien = Kunst
    public void attackPlayer(Vector2 damageOrigin, int damage, Player target, float attackKnockback, float yAttackKnockback) {
        Vector2 attackVector = new Vector2(0, yAttackKnockback);
        if (damageOrigin.x < target.getPosition().x) attackVector.x = 1 * attackKnockback;
        else attackVector.x = -1 * attackKnockback;


        target.applyForces(attackVector.x, attackVector.y);
        target.setHP(target.getHP() - damage);
        soundManager.playSound("stomp.wav");
    }

    //Melee attack

    /**
     * Melee attack
     *
     * @param attacker         The player who is attacking.
     * @param target           The target the player is attacking.
     * @param attackKnockback  The knock-back the target will receive.
     * @param yAttackKnockback The knock-back the target will receive in y-Axis direction.
     */
    private void attackPlayer(Player attacker, Player target, float attackKnockback, float yAttackKnockback) {
        Vector2 attackVector = new Vector2(0, yAttackKnockback);
        if (attacker.getPosition().x < target.getPosition().x) attackVector.x = 1 * attackKnockback;
        else attackVector.x = -1 * attackKnockback;


        target.applyForces(attackVector.x, attackVector.y);
        target.setHP(target.getHP() - target.getAttackDamage());
        soundManager.playSound(target.getDamageSoundMp3());
    }

    /**
     * Update all projectiles existing in the game world.
     *
     * @param deltatime The game delta time.
     */
    private void updateProjectiles(float deltatime) {
        for (Projectile projectile : projectileArrayList) {
            //TODO: @Alex Bitte die auskommentierten sout's als log.debug übernehmen :)
            projectile.update(deltatime);
            //System.out.println("Is active before loop: " + projectile.active);
            log.debug("Is active before loop: " + projectile.active);
            if (!projectile.active) {
                //System.out.println("Trying to remove body");
                log.debug("Trying to remove body");
                projectile.destroyBody();
                projectileArrayList.removeValue(projectile, true);
            } else if (projectile.getBody().getUserData().equals("Destroyed")) {

                projectile.active = false;
                //System.out.println("Projectile was set to inactive");
                log.debug("Projectile was set to inactive");
                //System.out.println("Is active: " + projectile.active);
                log.debug("Is active: " + projectile.active);

            }
        }
    }


    /**
     * Draw all projectiles existing in the game world.
     *
     * @param batch The batch the projectile sprites should be drawn in.
     */
    public void drawProjectiles(SpriteBatch batch) {
        for (Projectile projectile : projectileArrayList) {

            // TODO: Gehört das so?

            // Kein Plan was deaktviert wird :/ -> daher lieber alles hahah
//            if(projectile.active && projectile.b2dbody.isActive() && projectile.isActive()) {
//                projectile.draw(batch);
//            }
            //so scheints zu funktionieren -> ansonsten pls aendern
            if (projectile.b2dbody.isActive()) {
                projectile.draw(batch);
            }

        }
    }

    /**
     * Resets the combatManager through clearing the projectileArrayList.
     */
    public void resetCombatManager() {
        projectileArrayList.clear();
        totalCircles = 0;
    }
}
