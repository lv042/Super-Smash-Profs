package com.mygdx.pahu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import helper.*;
import helper.TileMapHelper;

import com.mygdx.pahu.util;
import com.mygdx.pahu.inputAxes;

import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {

    private Texture texture;
    private SpriteBatch batch;
    private Sprite sprite;
    private AssetManager assetManager;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer; //allows to see objects without textures

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private    TileMapHelper tileMapHelper;


    public GameScreen(OrthographicCamera camera) {
        this.assetManager = new AssetManager();
        this.texture = new Texture("knight.png");
        this.batch = new SpriteBatch();
        this.sprite = new Sprite(texture);

        this.camera = camera;
        this.world = new World(new Vector2(0,0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();

        this.tileMapHelper = new TileMapHelper();
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();






    }

    @Override
    public void render(float delta) {
        this.update(delta);
        ScreenUtils.clear(1, 1, 1, 1); //clears the buffer after each frame with the chosen color » white
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
        batch.begin();
        //render objects
        sprite.draw(batch);
        sprite.rotate(delta * 25.0f);
        batch.end();
        box2DDebugRenderer.render(world, camera.combined.scl(PPM));
        camera.update();
    }

    private void update(float delta) {
        world.step(1/60, 6, 2);
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined);

        orthogonalTiledMapRenderer.setView(camera);

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    private void cameraUpdate() {
        camera.position.set(new Vector3(10,10,0));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

}
