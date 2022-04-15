package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.smashprofs.game.Actors.PlayerClass;

public class B2dContactListener implements ContactListener {

    PlayerClass playerOne;
    PlayerClass playerTwo;



    CombatManager combatManager = CombatManager.getCombatManager_INSTANCE();

    public B2dContactListener(PlayerClass playerOne, PlayerClass playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("beginContact");

        System.out.println("Fixture B: " + contact.getFixtureA().getBody().getUserData());

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