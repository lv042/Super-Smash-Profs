package com.smashprofs.game.Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.smashprofs.game.Actors.Projectiles.Projectile;
import com.smashprofs.game.Screens.PlayScreen;

import java.io.IOException;
import java.util.ArrayList;

public class B2dContactListener implements ContactListener {

    //public Array<Body> bodiesToDestroy = new Array<Body>();
    boolean PlayerOneGotShoot = false;
    boolean PlayerTwoGotShoot = false;
    boolean BulletHit = false;

    boolean P1NotTouchingTile = false;

    boolean P2NotTouchingTile = false;

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

    public Array<Body> bodiesToDestroy;



    @Override
    public void beginContact(Contact contact) {
        //System.out.println("beginContact");
        if("PlayerOne".equals(contact.getFixtureA().getBody().getUserData()) && "Bullet".equals(contact.getFixtureB().getBody().getUserData())) {
            PlayerOneGotShoot = true;
            //contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet");
            //bodiesToDestroy.add(contact.getFixtureB().getBody());

            // Add body which had contact with PlayerOne to bodiesToDestroy
            bodiesToDestroy.add(contact.getFixtureB().getBody());

            System.out.println("Fixture B: " + contact.getFixtureB().getUserData());
            System.out.println("UserData of Fixture B starts with Bullet: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet"));
        }

        if("PlayerTwo".equals(contact.getFixtureA().getBody().getUserData()) && "Bullet".equals(contact.getFixtureB().getBody().getUserData())) {
            PlayerTwoGotShoot = true;
            // bodiesToDestroy.add(contact.getFixtureB().getBody());
            //contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet");

            // Add body which had contact with PlayerTwo to bodiesToDestroy
            bodiesToDestroy.add(contact.getFixtureB().getBody());

            System.out.println("Fixture B: " + contact.getFixtureB().getUserData());
            System.out.println("UserData of Fixture B starts with Bullet: " + contact.getFixtureB().getBody().getUserData().toString().startsWith("Bullet"));
        }


//        if("Bullet".equals(contact.getFixtureB().getBody().getUserData())) BulletHit = true;
//        System.out.println("PlayerOneGotShoot: " + PlayerOneGotShoot);
//        System.out.println("PlayerTwoGotShoot: " + PlayerTwoGotShoot);
//        System.out.println("BulletHit: " + BulletHit);

        if ("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerTwo".equals(contact.getFixtureB().getBody().getUserData())){
            System.out.println("P2 Touching Tile:" + P2NotTouchingTile);
            P2NotTouchingTile = false;
        }

        if ("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerOne".equals(contact.getFixtureB().getBody().getUserData())){
            System.out.println("P1 Touching Tile: " + P1NotTouchingTile);
            P1NotTouchingTile = false;
        }


        //final Body toRemove = contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody ? contact.getFixtureA().getBody()
        //        : contact.getFixtureB().getBody();




        // Destroys every body in bodiesToDestroy
        System.out.println("BodiesToDestroy Size: " + bodiesToDestroy.size);
        for (Body bodyToDestroy: bodiesToDestroy) {

            final Body toRemove = bodyToDestroy;

            // Destroy projectile with the same userData as bodyToDestroy
            for(Projectile projectile: CombatManager.projectileArrayList) {
                if(projectile.userData.equals(bodyToDestroy.getUserData())) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run () {
                            //projectile.destroy();  -> Bringt nichts, siehe Implementierung von .destroy()
                            CombatManager.projectileArrayList.remove(projectile);
                        }
                    });
                }
            }

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run () {
                    PlayScreen.getWorld().destroyBody(toRemove);
                }
            });
        }

        // Previously implemented way to remove all the colliding bodies
/*        if("Bullet".equals(contact.getFixtureB().getBody().getUserData())) {
            final Body toRemove = contact.getFixtureB().getBody();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run () {
                    PlayScreen.getWorld().destroyBody(toRemove);
                }
            });
        }*/


    }

    @Override
    public void endContact(Contact contact) {

        if("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerOne".equals(contact.getFixtureB().getBody().getUserData())){
            System.out.println("P1 Touching Tile: " + P1NotTouchingTile);
            P1NotTouchingTile = true;
        }
        if("Tile".equals(contact.getFixtureA().getBody().getUserData()) && "PlayerTwo".equals(contact.getFixtureB().getBody().getUserData())) // null because not touching anything
        {
            System.out.println("P2 Touching Tile:" + P2NotTouchingTile);
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