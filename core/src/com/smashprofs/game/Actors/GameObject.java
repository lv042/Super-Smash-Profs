package com.smashprofs.game.Actors;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;


public abstract class GameObject {
    public Body b2dbody;    // Can't be final because of the change in child classes
    public Sprite sprite;   // <>
    public String userData; // <> userData is a string to identify the object in the game


    public GameObject(World world, String userData) {
        this.b2dbody = null; // cant be final because other classes cant change body
        this.sprite = null;
        this.userData = userData;
    }

    public void update(float delta) {
        System.out.println("GameObject updated"); // TODO: alex
    }

    // Sprite related methods:dw
    public void draw(Batch batch) {
        sprite.draw(batch);
    }




}
