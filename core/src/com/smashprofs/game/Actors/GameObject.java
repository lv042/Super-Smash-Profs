package com.smashprofs.game.Actors;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Helper.B2dContactListener;
import com.smashprofs.game.Helper.CameraManager;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Helper.Util;
import com.smashprofs.game.Screens.PlayScreen;

import java.util.ArrayList;


public abstract class GameObject {
    public Body b2dbody;
    public Sprite sprite;
    public World world;
    public String userData;


    public GameObject(World world, String userData) {
        this.b2dbody = null;
        this.sprite = new Sprite();
        this.userData = userData;
        this.world = world;
    }

    public void update(float delta) {
        System.out.println("GameObject updated");

    }

    // Sprite related methods:
    public void draw(Batch batch) {
        sprite.draw(batch);
    }




}
