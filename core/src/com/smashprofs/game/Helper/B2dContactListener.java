package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.io.IOException;

public class B2dContactListener implements ContactListener {

    boolean PlayerOneGotShoot = false;
    boolean PlayerTwoGotShoot = false;
    boolean BulletHit = false;

    boolean P1NotTouchingTile = false;

    boolean P2NotTouchingTile = false;

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



    @Override
    public void beginContact(Contact contact) {
        //System.out.println("beginContact");
        if("PlayerOne".equals(contact.getFixtureA().getBody().getUserData()) && "Bullet".equals(contact.getFixtureB().getBody().getUserData()))
            PlayerOneGotShoot = true;
        if("PlayerTwo".equals(contact.getFixtureA().getBody().getUserData()) && "Bullet".equals(contact.getFixtureB().getBody().getUserData()))
            PlayerTwoGotShoot = true;

        //if("Bullet".equals(contact.getFixtureB().getBody().getUserData())) BulletHit = true;
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

    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");

        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)) {
            shutdownCommand = "shutdown -h now";
        }
        else if ("Windows".equals(operatingSystem)) {
            shutdownCommand = "shutdown.exe -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }


}