package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import com.smashprofs.game.Helper.Keys;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Helper.gamePropertiesManager;

public class CharacterSelectScreen implements Screen {
    int width = 1920;
    int height = 1080;
    private float zoomFactor = 1f;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private SoundManager soundManager;

    private TextureRegion[] alex, maurice, luca, leo;
    private Image left1, right1, left2, right2, playButton;
    private Table table;
    private VfxManager postProcessingManager;
    private static int carouselCounter = 0;
    private static int carouselCounter2 = 0;
    private TextureRegion[] playerImages;
    private static String[] playerNames;
    private ImageButton currentSelection, currentSelection2;
    private Label p1Label, p2Label;
    private BitmapFont labelFont;
    SpriteBatch batch;
    Game game;

    public CharacterSelectScreen(Game game) {
        this.camera = new OrthographicCamera(width, height);
        this.batch = new SpriteBatch();
        this.game = game;
        this.viewport = new FillViewport(this.width, this.height, this.camera);
        this.stage = new Stage(this.viewport, this.batch);
        this.soundManager = SoundManager.getSoundManager_INSTANCE();
        table = new Table();

        this.alex = TextureRegion.split(new Texture("Sprites/Alex/alex_stand.png"), 100, 100)[0];
        this.maurice = TextureRegion.split(new Texture("Sprites/Momo/momo_strip.png"), 100, 100)[0];
        this.luca = TextureRegion.split(new Texture("Sprites/Luca/luca_stand.png"), 100, 100)[0];
        this.leo = TextureRegion.split(new Texture("Sprites/Leo/leo_stand.png"), 100, 100)[0];
        this.playerImages = new TextureRegion[]{alex[0], maurice[0], luca[0], leo[0]};
        this.playerNames = new String[]{"Alex", "Maurice", "Luca", "Leo"};

        this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages[0]));
        this.currentSelection2 = new ImageButton(new TextureRegionDrawable(playerImages[0]));

        this.labelFont = new BitmapFont();
        labelFont.getData().setScale(4f);
        this.p1Label = new Label(playerNames[carouselCounter], new Label.LabelStyle(labelFont, Color.valueOf("FFFFFF")));
        this.p2Label = new Label(playerNames[carouselCounter2], new Label.LabelStyle(labelFont, Color.valueOf("FFFFFF")));

        playButton = new Image(new Texture("mainmenu/buttons/playButtonInactive.png"));

        left1 = new Image(new Texture("ui/arrowLeft.png"));
        left2 = new Image(new Texture("ui/arrowLeft.png"));
        right1 = new Image(new Texture("ui/arrowRight.png"));
        right2 = new Image(new Texture("ui/arrowRight.png"));

        left1.scaleBy(2f);
        left2.scaleBy(2f);
        right1.scaleBy(2f);
        right2.scaleBy(2f);

        left1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                    decrementCarouselCounter(1);
            }
        });
        left2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                decrementCarouselCounter(2);
            }
        });
        right1.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                incrementCarouselCounter(1);
            }
        });
        right2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                incrementCarouselCounter(2);
            }
        });

        playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                soundManager.playSound("sounds/minecraft_click.mp3");
                game.setScreen(new PlayScreen((com.smashprofs.game.Game) game));
                //game.setScreen(new MainMenuScreen(game));
            }
        });

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        //Create Table
        //Set table to fill stage
        table.setFillParent(true);
        //Set alignment of contents in the table.
        renderTable();


        //table.setDebug(true);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        this.stage.act();
        this.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }


    public void incrementCarouselCounter(int NumberOfCarouselCounter) {

        if(NumberOfCarouselCounter == 1) {
            if(carouselCounter < playerImages.length-1) {
                carouselCounter++;
            }
            else if (carouselCounter == playerImages.length -1) {
                carouselCounter = 0;
            }
            System.out.println("CarouselCounter1: " + carouselCounter);
            System.out.println("CurrentPlayer1: " + playerNames[carouselCounter]);
        } else if (NumberOfCarouselCounter == 2) {
            if(carouselCounter2 < playerImages.length-1) {
                carouselCounter2++;
            }
            else if (carouselCounter2 == playerImages.length -1) {
                carouselCounter2 = 0;
            }
            System.out.println("CarouselCounter2: " + carouselCounter2);
            System.out.println("CurrentPlayer2: " + playerNames[carouselCounter2]);
        }

        updateScreenContents();
    }

    public void decrementCarouselCounter(int NumberOfCarouselCounter) {

        if(NumberOfCarouselCounter == 1) {
            if(carouselCounter > 0) {
                carouselCounter--;
            }
            else if (carouselCounter == 0) {
                carouselCounter = playerImages.length-1;
            }
            System.out.println("CarouselCounter1: " + carouselCounter);
            System.out.println("CurrentPlayer1: " + playerNames[carouselCounter]);
        }
        else if(NumberOfCarouselCounter == 2) {
            if(carouselCounter2 > 0) {
                carouselCounter2--;
            }
            else if (carouselCounter2 == 0) {
                carouselCounter2 = playerImages.length-1;
            }
            System.out.println("CarouselCounter2: " + carouselCounter2);
            System.out.println("CurrentPlayer2: " + playerNames[carouselCounter2]);
        }

        updateScreenContents();
    }

    public void updateScreenContents() {
            //this.currentSelection = new TextureRegionDrawable(playerImages[carouselCounter]);
            //this.currentSelection.setBackground(new TextureRegionDrawable(playerImages[carouselCounter]));
        //table.removeActor(currentSelection);
        //this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages[carouselCounter]));
        //table.row();
        //table.add(currentSelection);

        this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages[carouselCounter]));
        this.p1Label.setText(playerNames[carouselCounter]);
        this.currentSelection2 = new ImageButton(new TextureRegionDrawable(playerImages[carouselCounter2]));
        this.p2Label.setText(playerNames[carouselCounter2]);
        table.clear();

        renderTable();
    }

    public void renderTable() {

        table.top();
        table.row().padTop(height/5f);
        table.add(currentSelection).colspan(2).width(width/2f);
        table.add(currentSelection2).colspan(2).width(width/2f);
        table.row().height(100).padTop(100).left();
        table.add(p1Label).colspan(2).center();
        table.add(p2Label).colspan(2).center();
        table.row().height(100).padTop(100).left();
        table.add(left1).maxSize(left1.getWidth(), left1.getHeight()).center();
        table.add(right1).maxSize(right1.getWidth(), right1.getHeight()).center();
        table.add(left2).maxSize(left2.getWidth(), left2.getHeight()).center();
        table.add(right2).maxSize(right2.getWidth(), right2.getHeight()).center();
        table.row().padTop(height/5f);
        table.add(playButton).center().colspan(4);
        stage.addActor(table);

    }

    public static String getSelectedPlayerOne() {
        return playerNames[carouselCounter];
    }
    public static String getSelectedPlayerTwo() {
        return playerNames[carouselCounter2];
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
