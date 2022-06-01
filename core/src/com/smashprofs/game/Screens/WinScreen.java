package com.smashprofs.game.Screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Scenes.Hud;

public class WinScreen implements Screen {

    private Game game;
    private SpriteBatch batch;
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private Image menuButton, pokal, winner;
    private Texture menuButtonInactive, menuButtonActive, player1,player2;
    private Table mainTable;
    private Player playerOne,playerTwo;

    public WinScreen(Game game,Player playerOne,Player playerTwo) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1920, 1080, camera);
        this.stage = new Stage(this.viewport, this.batch);
        this.playerOne=playerOne;
        this.playerTwo=playerTwo;

        menuButtonInactive = new Texture("winscreen/menuButtonInactive.png");
        menuButtonActive = new Texture("winscreen/menuButtonActive.png");
        player1= new Texture("winscreen/player1.png");
        player2 = new Texture("winscreen/player2.png");
        mainTable = new Table();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create buttons
        menuButton = new Image(menuButtonInactive);
        pokal = new Image(new Texture("winscreen/pokal.png"));

        // winner
        if(playerOne.getHP()>=playerTwo.getHP()) {
            winner = new Image(player1);
        }
        else {
            winner=new Image(player2);
        }

        //Add listeners to buttons
        menuButton.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor actor) {

                menuButton.setDrawable(new SpriteDrawable(new Sprite(menuButtonActive)));
                if (Gdx.input.isButtonJustPressed(0)) {
                    game.setScreen(new MainMenuScreen(game));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor actor) {
                menuButton.setDrawable(new SpriteDrawable(new Sprite(menuButtonInactive)));
            }
        });



        mainTable.add(pokal).maxSize(290 , 360 ).pad(100).padBottom(50);
        mainTable.row();
        mainTable.add(winner).padBottom(100).maxSize(600, 200);
        mainTable.row();
        mainTable.add(menuButton).padBottom(100).maxSize(300, 100);

        //Add table to stage
        mainTable.setDebug(false);
        stage.addActor(mainTable);
    }






    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        this.stage.getBatch().begin();
        this.stage.getBatch().draw(new Texture("winscreen/winbg.png"), 0, 0, 1920, 1080);
        this.stage.getBatch().end();
        this.stage.act();
        this.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.batch.dispose();
    }
}



