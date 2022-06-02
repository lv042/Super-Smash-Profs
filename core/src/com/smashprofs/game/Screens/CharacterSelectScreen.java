package com.smashprofs.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.VfxManager;
import org.w3c.dom.Text;

public class CharacterSelectScreen implements Screen {
    int width = 1920;
    int height = 1080;
    private float zoomFactor = 1f;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Texture uiElementsRaw;
    private TextureRegion[][] uiElementsSplit;
    //private Texture arrowRightInactive, arrowRightActive, arrowLeftInactive, arrowLeftActive, playButtonInactive, playButtonActive;
    private TextureRegionDrawable arrowRightInactive, arrowRightActive, arrowLeftInactive, arrowLeftActive, playButtonInactive, playButtonActive;


    private TextureRegion[] alex, maurice, luca, leo;
    private ImageButton left, right;
    private TextureRegion arrowRight, arrowLeft;
    private Table table;
    private VfxManager postProcessingManager;
    private int carouselCounter = 0;
    private TextureRegion[] playerImages;
    private ImageButton currentSelection;
    SpriteBatch batch;
    Game game;

    public CharacterSelectScreen(Game game) {
        this.camera = new OrthographicCamera(width, height);
        this.batch = new SpriteBatch();
        this.game = game;
        this.viewport = new FillViewport(this.width, this.height, this.camera);
        this.stage = new Stage(this.viewport, this.batch);
        table = new Table();

        this.alex = TextureRegion.split(new Texture("Sprites/Alex/alex_stand.png"), 100, 100)[0];
        this.maurice = TextureRegion.split(new Texture("Sprites/Momo/momo_strip.png"), 100, 100)[0];
        this.luca = TextureRegion.split(new Texture("Sprites/Luca/luca_stand.png"), 100, 100)[0];
        this.leo = TextureRegion.split(new Texture("Sprites/Leo/leo_stand.png"), 100, 100)[0];
        this.playerImages = new TextureRegion[]{alex[0], maurice[0], luca[0], leo[0]};
        this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages[0]));


        this.uiElementsRaw = new Texture("charselect/buttons/buttonUI.png");

        uiElementsSplit = TextureRegion.split(uiElementsRaw, 16, 16);
        TextureRegion arrowTextureLeft = uiElementsSplit[9][7];
        TextureRegion arrowTextureRight = uiElementsSplit[9][7];
        this.arrowLeft = arrowTextureLeft;
        this.arrowRight = arrowTextureRight;
        arrowLeft.flip(true, false);
        arrowRight.flip(false, false);

        //arrowRightInactive = new Texture("charselect/buttons/arrowRightInactive.png");
        arrowRightActive = new TextureRegionDrawable(arrowRight);
        //arrowLeftInactive = new Texture("charselect/buttons/arrowLeftInactive.png");
        arrowLeftActive = new TextureRegionDrawable(arrowLeft);
        //playButtonInactive = new Texture("mainmenu/buttons/playButtonInactive.png");
        //playButtonActive = new Texture("mainmenu/buttons/playButtonActive.png");

        left = new ImageButton(arrowLeftActive);
        right = new ImageButton(arrowRightActive);

        left.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                    decrementCarouselCounter();
            }
        });
        right.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                incrementCarouselCounter();
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
        table.top();
        table.add(currentSelection);
        table.row();
        table.add(left).padBottom(100).maxSize(300, 100).padTop(500).padRight(400);
        table.add(right).padBottom(100).maxSize(300, 100).padTop(500);


        table.setDebug(true);
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


    public void incrementCarouselCounter() {
        if(carouselCounter < playerImages.length-1) {
            carouselCounter++;
        }
        else if (carouselCounter == playerImages.length -1) {
            carouselCounter = 0;
            }
        System.out.println("CarouselCounter: " + carouselCounter);
        updateSelectorFields();
    }

    public void decrementCarouselCounter() {
        if(carouselCounter > 0) {
            carouselCounter--;
        }
        else if (carouselCounter == 0) {
            carouselCounter = playerImages.length-1;
        }
        System.out.println("CarouselCounter: " + carouselCounter);
        updateSelectorFields();
    }

    public void updateSelectorFields() {
            //this.currentSelection = new TextureRegionDrawable(playerImages[carouselCounter]);
            //this.currentSelection.setBackground(new TextureRegionDrawable(playerImages[carouselCounter]));
        table.removeActor(currentSelection);
        this.currentSelection = new ImageButton(new TextureRegionDrawable(playerImages[carouselCounter]));
        table.row();
        table.add(currentSelection);
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
