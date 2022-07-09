package com.smashprofs.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.smashprofs.game.Actors.Players.PlayerView;
import com.smashprofs.game.Game;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Helper.SoundManager;

public class  Hud{
    public Stage stage; //stage to hold all the actors -> A 2D scene graph containing hierarchies of actors. Stage handles the viewport and distributes input events.

    private Viewport viewport; // viewport for the hud so the hud doesnt move with the normal camera
    private Integer worldTimer = 120; //120 is the max time for the game
    private float timeCount; // time counter for the hud
    private int score; // score counter for the hud
    private Label countdownLabel; // label for the countdown;
    private Label scoreLabel; // label for the score
    private Label playerTwoHud; // label for the time
    private Label playerOneHud; // label for the level
    private Label modeLabel; // label for the world
    private Label playerLabel; // label for the player
    private Label fpsLabel; // label for the current FPS
    BitmapFont fpsFont; // font for the FPS number. Different styling than the default bitmap font.
    private SoundManager soundManager;

    public Hud(SpriteBatch spriteBatch, PlayerView playerOne, PlayerView playerTwo){
        soundManager=SoundManager.getSoundManager_INSTANCE();
        this.fpsFont = new BitmapFont();
        fpsFont.getData().setScale(0.5f);
        timeCount = 0;
        score = 0;
        this.viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();

        if(Game.debugMode) {
            table.setDebug(true);
        }

        table.top(); // aligns the table to the top of the screen
        table.setFillParent(true); // fills the entire screen with the table





        // "%03d" is a format specifier for a 3 digit integer
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerOneHud = new Label(playerOne.getPlayerName() + " " + playerOne.getHP() + "%", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerTwoHud = new Label(playerTwo.getPlayerName() + " " + playerTwo.getHP() + "%", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        modeLabel = new Label("1 vs. 1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        if(Game.showFPS || Game.debugMode) {
            fpsLabel = new Label( "FPS: "  + Gdx.graphics.getFramesPerSecond(), new Label.LabelStyle(fpsFont, Color.RED));
        }

        table.add(playerOneHud).expandX().padTop(2);
        table.add(countdownLabel).expandX().padTop(2);
        table.add(playerTwoHud).expandX().padTop(2);
        table.row(); // new row
        table.add(fpsLabel).right().padRight(6f).padTop(3f).colspan(3);
        stage.addActor(table);

    }

    public void updateHud(float delta, Player playerOne, Player playerTwo){
            timeCount += delta;
        if(timeCount >= 1 && worldTimer > 0){
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
        playerOneHud.setText(playerOne.getPlayerName() + " " + (int)playerOne.getHP() + "%");
        playerTwoHud.setText(playerTwo.getPlayerName() + " " + (int)playerTwo.getHP() + "%");

        if(Game.showFPS || Game.debugMode) {
            fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            // Set color of FPS label if FPS are low.
            if(Gdx.graphics.getFramesPerSecond() < 30) {
                fpsLabel.setStyle(new Label.LabelStyle(fpsFont, Color.RED));
            }
            else if(Gdx.graphics.getFramesPerSecond() < 45) {
                fpsLabel.setStyle(new Label.LabelStyle(fpsFont, Color.YELLOW));
            }
            else if(Gdx.graphics.getFramesPerSecond() > 55) {
                fpsLabel.setStyle(new Label.LabelStyle(fpsFont, Color.GREEN));
            }
        }

    }

    public boolean testWin(PlayerView playerOne, PlayerView playerTwo)
    {
        if(timeCount>=1){
            soundManager.playSound("sounds/clock.mp3");
        }
        else if (playerOne.getHP()<=0||playerTwo.getHP()<=0){
            soundManager.playSound("sounds/death.mp3");
        }

        return timeCount>=1||playerOne.getHP()<=0||playerTwo.getHP()<=0;

    }



    public void dispose() {
        stage.dispose();
    }


}
