package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.smashprofs.game.Actors.PlayerClass;

public class B2dContactListener implements ContactListener {

    boolean PlayerOneGotShoot = false;
    boolean PlayerTwoGotShoot = false;
    boolean BulletHit = false;

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
        System.out.println("beginContact");
        if("PlayerOne".equals(contact.getFixtureA().getBody().getUserData()) && "Bullet".equals(contact.getFixtureB().getBody().getUserData()))
            PlayerOneGotShoot = true;
        if("PlayerTwo".equals(contact.getFixtureA().getBody().getUserData()) && "Bullet".equals(contact.getFixtureB().getBody().getUserData()))
            PlayerTwoGotShoot = true;

        //if("Bullet".equals(contact.getFixtureB().getBody().getUserData())) BulletHit = true;
        System.out.println("PlayerOneGotShoot: " + PlayerOneGotShoot);
        System.out.println("PlayerTwoGotShoot: " + PlayerTwoGotShoot);
        System.out.println("BulletHit: " + BulletHit);


    }

    @Override
    public void endContact(Contact contact) {
        return;
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