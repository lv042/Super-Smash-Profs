package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.smashprofs.game.Actors.Projectiles.HomingMissile;
import com.smashprofs.game.Actors.Projectiles.Projectile;
import com.smashprofs.game.Screens.PlayScreen;
import com.smashprofs.game.Helper.explosionType;

import java.util.ArrayList;
import java.util.logging.Logger;

public class B2dContactListener implements ContactListener {

    //public Array<Body> bodiesToDestroy = new Array<Body>();
    boolean PlayerOneGotShoot = false;
    boolean PlayerTwoGotShoot = false;

    static final Logger logger = Logger.getLogger("B2dContactListener");

    boolean BulletHit = false;

    boolean P1NotTouchingTile = false;

    boolean P2NotTouchingTile = false;

    private VFXManager vfxManager = VFXManager.getVFXManager_INSTANCE();

    SoundManager soundManager = SoundManager.getSoundManager_INSTANCE();

    private final String explosionSoundWav = "sounds/stomp.wav";


    private CombatManager combatManager = CombatManager.getCombatManager_INSTANCE();

//    public void update() {
//        for (Body body : bodiesToDestroy) {
//            body.setActive(false);
//
//        }
//    }

    public boolean isP1NotTouchingTile() {

        return P1NotTouchingTile;
    }

    public boolean isP2NotTouchingTile() {
        return P2NotTouchingTile;
    }

    public boolean isPlayerOneGotShoot() {
        return PlayerOneGotShoot;
    }

    public void setPlayerOneGotShoot(boolean playerOneGotShoot) {
        PlayerOneGotShoot = playerOneGotShoot;
    }

    public boolean isPlayerTwoGotShoot() {
        return PlayerTwoGotShoot;
    }

    public void setPlayerTwoGotShoot(boolean playerTwoGotShoot) {
        PlayerTwoGotShoot = playerTwoGotShoot;
    }

    public boolean isBulletHit() {
        return BulletHit;
    }

    public void setBulletHit(boolean bulletHit) {
        BulletHit = bulletHit;
    }

    private static B2dContactListener  contactListener = new B2dContactListener();

    public static B2dContactListener getContactListener_INSTANCE() {
        return contactListener;
    }


    public ArrayList<Body> bodiesToDestroy = new ArrayList<>();



    @Override
    public void beginContact(Contact contact) {







        //System.out.println("beginContact");
        if("PlayerOne".equals(contact.getFixtureA().getBody().getUserData()) && contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet")) {
            PlayerOneGotShoot = true;
            //contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet");
            //bodiesToDestroy.add(contact.getFixtureB().getBody());

            // Add body which had contact with PlayerOne to bodiesToDestroy
            //if(!bodiesToDestroy.contains(contact.getFixtureB().getBody())) {
            //    bodiesToDestroy.add(contact.getFixtureB().getBody());
            //}

            //System.out.println("Fixture B: " + contact.getFixtureB().getUserData());
            logger.info("Fixture B: " + contact.getFixtureB().getUserData());
            //System.out.println("UserData of Fixture B starts with Bullet: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet"));
            logger.info("UserData of Fixture B starts with Bullet: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet"));
        }

        if("PlayerTwo".equals(contact.getFixtureA().getBody().getUserData()) && contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet")) {
            PlayerTwoGotShoot = true;

            logger.info("Fixture B: " + contact.getFixtureB().getUserData());
            logger.info("UserData of Fixture B starts with Bullet: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet"));
        }


//        if("Bullet".equals(contact.getFixtureB().getBody().getUserData())) BulletHit = true;
//        System.out.println("PlayerOneGotShoot: " + PlayerOneGotShoot);
//        System.out.println("PlayerTwoGotShoot: " + PlayerTwoGotShoot);
//        System.out.println("BulletHit: " + BulletHit);

        if ("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerTwo".equals(contact.getFixtureB().getBody().getUserData())){
            //System.out.println("P2 Touching Tile:" + P2NotTouchingTile);
            logger.info("P2 Touching Tile:" + P2NotTouchingTile);
            P2NotTouchingTile = false;
        }

        if ("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerOne".equals(contact.getFixtureB().getBody().getUserData())){
            //System.out.println("P1 Touching Tile: " + P1NotTouchingTile);
            logger.info("P1 Touching Tile: " + P1NotTouchingTile);
            P1NotTouchingTile = false;
        }

        //BULLETS

        // TODO Implement bullet and landmine damage with boolean variables
        //  because no references to real classes :)

        if(contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet")){
            bodiesToDestroy.add(contact.getFixtureB().getBody());
            soundManager.playSound(explosionSoundWav);
            contact.getFixtureB().getBody().setUserData("Destroyed");


            vfxManager.spawnExplosion(explosionType.rocketExplosion, contact.getFixtureB().getBody().getPosition());
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits

        }
        if(contact.getFixtureA().getBody().getUserData().toString().startsWith("Bullet")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            soundManager.playSound(explosionSoundWav);
            contact.getFixtureA().getBody().setUserData("Destroyed");

            vfxManager.spawnExplosion(explosionType.rocketExplosion, contact.getFixtureA().getBody().getPosition());


            //contact.getFixtureB().getFilterData().categoryBits
        }

        //STAR

        if(contact.getFixtureB().getBody().getUserData().toString().startsWith("Star")){ //should be Star
            bodiesToDestroy.add(contact.getFixtureB().getBody());
            contact.getFixtureB().getBody().setUserData("Destroyed");
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits

        }
        if(contact.getFixtureA().getBody().getUserData().toString().startsWith("Star")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            contact.getFixtureA().getBody().setUserData("Destroyed");

            //contact.getFixtureB().getFilterData().categoryBits
        }

        //LANDMINE

        //should not explode when contacting with ground

        if(!("Tile".equals(contact.getFixtureA().getBody().getUserData())) && contact.getFixtureB().getBody().getUserData().toString().startsWith("Mine")){
            bodiesToDestroy.add(contact.getFixtureB().getBody());
            contact.getFixtureB().getBody().setUserData("Destroyed");
            soundManager.playSound(explosionSoundWav);
            vfxManager.spawnExplosion(explosionType.landMineExplosion, contact.getFixtureB().getBody().getPosition());
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits

        }
        if(!("Tile".equals(contact.getFixtureB().getBody().getUserData())) && contact.getFixtureA().getBody().getUserData().toString().startsWith("Mine")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            contact.getFixtureA().getBody().setUserData("Destroyed");
            soundManager.playSound(explosionSoundWav);
            vfxManager.spawnExplosion(explosionType.landMineExplosion, contact.getFixtureA().getBody().getPosition());

            //contact.getFixtureB().getFilterData().categoryBits
        }

        //MINEBALL

        if(contact.getFixtureB().getBody().getUserData().toString().startsWith("Fire")){
            bodiesToDestroy.add(contact.getFixtureB().getBody());
            contact.getFixtureB().getBody().setUserData("Destroyed");
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits

        }
        if(contact.getFixtureA().getBody().getUserData().toString().startsWith("Fire")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            contact.getFixtureA().getBody().setUserData("Destroyed");

            //contact.getFixtureB().getFilterData().categoryBits
        }



        }

    public void resetContactListener(){
        bodiesToDestroy.clear();
    }

    @Override
    public void endContact(Contact contact) {

        if("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerOne".equals(contact.getFixtureB().getBody().getUserData())){
            //System.out.println("P1 Touching Tile: " + P1NotTouchingTile);
            logger.info("P1 Touching Tile: " + P1NotTouchingTile);
            P1NotTouchingTile = true;
        }
        if("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerTwo".equals(contact.getFixtureB().getBody().getUserData())) // null because not touching anything
        {
            //System.out.println("P2 Touching Tile:" + P2NotTouchingTile);
            logger.info("P2 Touching Tile:" + P2NotTouchingTile);
            P2NotTouchingTile = true;
        }

        //System.out.println(contact.getFixtureA().getBody().getUserData());
        //System.out.println(contact.getFixtureB().getBody().getUserData());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        return;
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        return;
    }



}