package com.smashprofs.game.Actors;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Actors.Projectiles.Projectile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class GameObject {

    private static Logger log = LogManager.getLogger(GameObject.class);

    public Body b2dbody;    // Can't be final because of the change in child classes
    public Sprite sprite;   // <>
    public String userData; // <> userData is a string to identify the object in the game
    public float gravity = -0.098f;
    public Vector2 gravityVector = new Vector2(0, gravity);


    public GameObject(String userData) {
        this.b2dbody = null; // can't be final because then other classes can't change body
        this.sprite = null;
        this.userData = userData;
    }

    public void update(float delta) {
        //log.debug("GameObject updated");
    }

    // Sprite related methods:dw
    public void draw(Batch batch) {
        sprite.draw(batch);
    }




}
