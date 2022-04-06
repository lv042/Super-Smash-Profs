package com.smashprofs.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import helper.TileMapHelper;


public class Luca {

    // Constructor
    public static Luca INSTANCE;
    public Luca() { if(INSTANCE == null) INSTANCE = this; }


    public Body createHero() {
        Body playerBody;

        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(0, 0);
        playerBodyDef.fixedRotation = true;
        playerBody = GameScreen.INSTANCE.getWorld().createBody(playerBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8/2f, 8/2f);

        playerBody.createFixture(shape, 1.0f);
        shape.dispose();

        return playerBody;
    }
}
// Habe die cells deaktiviert die zerstört werden wenn man da gegen springt
// außerdem sollten wir auch noch die tmx file machen
// ! Gamescreen.java ist aktuell noch mega unstrukturiert wir sollten später mal sowas wie ein Klassendiagramm machen
