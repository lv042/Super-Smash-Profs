package com.smashprofs.game.Actors;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;


public abstract class GameObject {
    public Body b2dbody;
    public Sprite sprite;
    final public String userData;


    public GameObject(World world, String userData) {
        this.b2dbody = null; // cant be final because other classes cant change body
        this.sprite = null;
        this.userData = userData;
    }

    public void update(float delta) {
        System.out.println("GameObject updated");

    }

    // Sprite related methods:dw
    public void draw(Batch batch) {
        sprite.draw(batch);
    }




}
