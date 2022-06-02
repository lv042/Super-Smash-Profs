package com.smashprofs.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class VFXObject extends GameObject {

    private static Logger log = LogManager.getLogger(VFXObject.class);


    public Boolean active;
    public Vector2 spawnpoint;
    public Texture rawTexture;
    private final Animation<TextureRegion> explode;
    float stateTime = 0;

    float centeringFactor = 0f;


    public VFXObject(String userData, Vector2 Spawnpoint, Texture texture, Boolean centered, Boolean spriteIsSquare) {
        super(userData);
        sprite = new Sprite(texture);
        this.active = true;
        this.spawnpoint = Spawnpoint;
        this.rawTexture = texture;

        TextureRegion[] exploding;

        if(spriteIsSquare) {
            exploding = TextureRegion.split(rawTexture, rawTexture.getHeight(), rawTexture.getHeight())[0];
        }
        else{
            exploding = TextureRegion.split(rawTexture, rawTexture.getWidth()/8, rawTexture.getHeight())[0];
        }

        explode = new Animation(0.1f, exploding[0], exploding[1], exploding[2], exploding[3], exploding[4], exploding[5], exploding[6], exploding[7]);
        explode.setPlayMode(Animation.PlayMode.NORMAL);



        //sprite.setRegion(rawTexture);
        this.active = true;
        this.userData = userData;

        if(spriteIsSquare) {
            sprite.setBounds(Spawnpoint.x/PPM, (Spawnpoint.y)/PPM, sprite.getHeight()/PPM, sprite.getHeight()/PPM);
        }
        else {
            sprite.setBounds(Spawnpoint.x/PPM, (Spawnpoint.y)/PPM, sprite.getWidth()/8/PPM, sprite.getHeight()/PPM);
        }

        //sprite.setPosition(Spawnpoint.x - sprite.getHeight()/2f, Spawnpoint.y - sprite.getHeight()/2f);


        //This part must be after set bounds otherwise the
        if(centered) {
            this.centeringFactor = sprite.getHeight()/2f;
        } else if(rawTexture.getHeight() == 64) {
            this.centeringFactor = 5f/PPM;
        } else if(rawTexture.getHeight() == 256) {
            this.centeringFactor = 40f/PPM;
        }

        //System.out.println("centering factor: "+ centeringFactor);
        log.debug("centering factor: "+ centeringFactor);



        if(spriteIsSquare) {
            sprite.setPosition(Spawnpoint.x - sprite.getHeight()/2f, Spawnpoint.y - centeringFactor);
        }
        else {
            sprite.setPosition(Spawnpoint.x - sprite.getWidth()/8/2f, Spawnpoint.y - centeringFactor);
        }
        //sprite.setOrigin(0, -1000f);
        sprite.setScale(1f);


    }

    public void dispose() {


        sprite.getTexture().dispose();
        //System.out.println("Disposed VFXObj texture via .dispose()");
        log.info("Disposed VFXObj texture via .dispose()");

    }


    @Override
    public void update(float delta){
        super.update(delta);
        stateTime += delta;
        sprite.setRegion(getRenderTexture(stateTime)); //set the texture to the current state of the movement
        if(explode.isAnimationFinished(stateTime)) {
            this.active = false;
        }

    }

    public TextureRegion getRenderTexture(float stateTime) {
        TextureRegion frame = null;
                sprite.setRegion(rawTexture);
                frame = explode.getKeyFrame(stateTime);
        return frame;
    }
}
