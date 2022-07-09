package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class B2dContactListener implements ContactListener {

    //public Array<Body> bodiesToDestroy = new Array<Body>();
    private boolean PlayerOneGotShoot = false;
    private boolean PlayerTwoGotShoot = false;
    //add map with key3
    private static Logger log = LogManager.getLogger(B2dContactListener.class);

    public final static short PLAYER_ENTITY = 0b01;   // 1
    public final static short WORLD_ENTITY = 0b10;  // 2
    public final static short PROJECTILE_ENTITY = 0b11; // 3
    public final static short CIRCLESTAR_ENTITY = 0b100; // 4




    private boolean P1NotTouchingTile = false;

    private boolean P2NotTouchingTile = false;

    private VAFXManager vafxManager = VAFXManager.getVFXManager_INSTANCE();

    private CombatManager combatManager = CombatManager.getCombatManager_INSTANCE();



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




    private static B2dContactListener  contactListener = new B2dContactListener();

    public static B2dContactListener getContactListener_INSTANCE() {
        return contactListener;
    }


    private ArrayList<Body> bodiesToDestroy = new ArrayList<>();



    @Override
    public void beginContact(Contact contact) {







        //System.out.println("beginContact");
        if("PlayerOne".equals(contact.getFixtureA().getBody().getUserData()) && (contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket")
                || contact.getFixtureB().getBody().getUserData().toString().startsWith("Mine") || contact.getFixtureB().getBody().getUserData().toString().startsWith("Star")
                || contact.getFixtureB().getBody().getUserData().toString().startsWith("Circle") || contact.getFixtureB().getBody().getUserData().toString().startsWith("IBM"))) {

            PlayerOneGotShoot = true;
            System.out.println("PlayerOneGotShoot");


            //System.out.println("Fixture B: " + contact.getFixtureB().getUserData());
            log.debug("Fixture B: " + contact.getFixtureB().getUserData());
            //System.out.println("UserData of Fixture B starts with Rocket: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket"));
            log.debug("UserData of Fixture B starts with Rocket: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket"));
        }

        if("PlayerTwo".equals(contact.getFixtureA().getBody().getUserData()) && (contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket")
        || contact.getFixtureB().getBody().getUserData().toString().startsWith("Mine") || contact.getFixtureB().getBody().getUserData().toString().startsWith("Star")
                || contact.getFixtureB().getBody().getUserData().toString().startsWith("Circle") || contact.getFixtureB().getBody().getUserData().toString().startsWith("IBM"))) {

            PlayerTwoGotShoot = true;
            System.out.println("PlayerTwoGotShoot");


            //System.out.println("Fixture B: " + contact.getFixtureB().getUserData());
            log.debug("Fixture B: " + contact.getFixtureB().getUserData());
            //System.out.println("UserData of Fixture B starts with Rocket: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket"));
            log.debug("UserData of Fixture B starts with Rocket: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket"));
        }




        if ("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerTwo".equals(contact.getFixtureB().getBody().getUserData())){
            //System.out.println("P2 Touching Tile:" + P2NotTouchingTile);
            log.debug("P2 Touching Tile:" + P2NotTouchingTile);
            P2NotTouchingTile = false;
        }

        if ("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerOne".equals(contact.getFixtureB().getBody().getUserData())){
            //System.out.println("P1 Touching Tile: " + P1NotTouchingTile);
            log.debug("P1 Touching Tile: " + P1NotTouchingTile);
            P1NotTouchingTile = false;
        }

        //RocketS

        // TODO Implement Rocket and landmine damage with boolean variables
        //  because no references to real classes :)

        if(contact.getFixtureB().getBody().getUserData().toString().startsWith("Rocket")){
            bodiesToDestroy.add(contact.getFixtureB().getBody());
            contact.getFixtureB().getBody().setUserData("Destroyed");

            vafxManager.spawnExplosion(explosionType.rocketExplosion, contact.getFixtureB().getBody().getPosition());
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits

        }
        if(contact.getFixtureA().getBody().getUserData().toString().startsWith("Rocket")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            contact.getFixtureA().getBody().setUserData("Destroyed");

            vafxManager.spawnExplosion(explosionType.rocketExplosion, contact.getFixtureA().getBody().getPosition());


            //contact.getFixtureB().getFilterData().categoryBits
        }

        //IBM


        if(contact.getFixtureB().getBody().getUserData().toString().startsWith("IBM")){
            bodiesToDestroy.add(contact.getFixtureB().getBody());
            contact.getFixtureB().getBody().setUserData("Destroyed");

            //vafxManager.spawnExplosion(explosionType.rocketExplosion, contact.getFixtureB().getBody().getPosition());
            vafxManager.spawnExplosion(explosionType.electricZap, contact.getFixtureB().getBody().getPosition());
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits



        }
        if(contact.getFixtureA().getBody().getUserData().toString().startsWith("IBM")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            contact.getFixtureA().getBody().setUserData("Destroyed");

            //vafxManager.spawnExplosion(explosionType.rocketExplosion, contact.getFixtureA().getBody().getPosition());
            vafxManager.spawnExplosion(explosionType.electricZap, contact.getFixtureB().getBody().getPosition());


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
            vafxManager.spawnExplosion(explosionType.landMineExplosion, contact.getFixtureB().getBody().getPosition());
            //PlayScreen.getWorld().destroyBody(contact.getFixtureB().getBody());
            //contact.getFixtureB().getFilterData().categoryBits

        }
        if(!("Tile".equals(contact.getFixtureB().getBody().getUserData())) && contact.getFixtureA().getBody().getUserData().toString().startsWith("Mine")){
            bodiesToDestroy.add(contact.getFixtureA().getBody());
            contact.getFixtureA().getBody().setUserData("Destroyed");
            vafxManager.spawnExplosion(explosionType.landMineExplosion, contact.getFixtureA().getBody().getPosition());

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

    @Override
    public void endContact(Contact contact) {

//        if("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerOne".equals(contact.getFixtureB().getBody().getUserData())){
//            //System.out.println("P1 Touching Tile: " + P1NotTouchingTile);
//            log.debug("P1 Touching Tile: " + P1NotTouchingTile);
//            P1NotTouchingTile = true;
//        }
//        if("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerTwo".equals(contact.getFixtureB().getBody().getUserData())) // null because not touching anything
//        {
//            //System.out.println("P2 Touching Tile:" + P2NotTouchingTile);
//            log.debug("P2 Touching Tile:" + P2NotTouchingTile);
//            P2NotTouchingTile = true;
//        }

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


    public void resetContactListener() {
        bodiesToDestroy.clear();
    }
}