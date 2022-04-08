package com.smashprofs.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.smashprofs.game.GameClass;

public class Hud{
    public Stage stage; //stage to hold all the actors -> A 2D scene graph containing hierarchies of actors. Stage handles the viewport and distributes input events.

    private Viewport viewport; // viewport for the hud so the hud doesnt move with the normal camera
    private Integer worldTimer = 300;
    private float timeCount; // time counter for the hud
    private int score; // score counter for the hud
    Label countdownLabel; // label for the countdown;
    Label scoreLabel; // label for the score
    Label playerTwo; // label for the time
    Label playerOne; // label for the level
    Label modeLabel; // label for the world
    Label playerLabel; // label for the player

    public Hud(SpriteBatch spriteBatch){

        timeCount = 0;
        score = 0;
        this.viewport = new FitViewport(GameClass.V_WIDTH, GameClass.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top(); // aligns the table to the top of the screen
        table.setFillParent(true); // fills the entire screen with the table





        // "%03d" is a format specifier for a 3 digit integer
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerTwo = new Label("Jens Huhn", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerOne = new Label("Martin Boik", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        modeLabel = new Label("1 vs. 1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label("PLAYER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(playerOne).expandX().padTop(2);
        table.add(countdownLabel).expandX().padTop(2);
        table.add(playerTwo).expandX().padTop(2);
        table.row(); // new row
        stage.addActor(table);

    }

    public void updateHud(float delta){


        }

    public void dispose() {
        stage.dispose();
    }
}
