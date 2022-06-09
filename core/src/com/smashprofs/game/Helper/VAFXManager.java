package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.smashprofs.game.Actors.VFXObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class VAFXManager {

    private static Logger log = LogManager.getLogger(VAFXManager.class);

    public static DelayedRemovalArray<VFXObject> VFXObjectList = new DelayedRemovalArray<>();

    //implements singelton sound manager

    private static final VAFXManager VAFX_MANAGER___INSTANCE = new VAFXManager();


    //private constructor to avoid client applications to use constructor
    private VAFXManager() {
    }

    public static VAFXManager getVFXManager_INSTANCE() {return VAFX_MANAGER___INSTANCE;}



    public void spawnExplosion(explosionType explosionType, Vector2 spawnpoint) {
        Texture VFXTexture = null;
        String sound = null;
        boolean centered = false;
        boolean spriteIsSquare = false;

        log.debug("Spawning explosion. Type: " + explosionType);

        switch (explosionType) {
            case rocketExplosion:
                VFXTexture = new Texture("explosions/explosion-6.png");
                sound = "sounds/stomp.wav";
                centered = true;
                spriteIsSquare = true;
            break;
            case landMineExplosion:
                VFXTexture = new Texture("explosions/explosion-2.png");
                sound = "sounds/stomp.wav";
                centered = false;
                spriteIsSquare = true;
            break;
            case lightningStrike:
                VFXTexture = new Texture("explosions/lightning.png");
                sound = "sounds/stomp.wav";
                centered = false;
                spriteIsSquare = false;
                break;
        }

        VFXObjectList.add(new VFXObject("test", spawnpoint, VFXTexture, sound, centered, spriteIsSquare));
        log.debug("Added explosion to VFXObjectList.");
    }


    public void update(float deltatime) {
        for (VFXObject explosion: VFXObjectList) {
            explosion.update(deltatime);
            log.debug("Is active before loop: " + explosion.active);

            if(!explosion.active) {
                VFXObjectList.removeValue(explosion, true);
            }
            else if(explosion.userData.equals("Destroyed")){

                explosion.active = false;
                log.debug("Explosion was set to inactive");
                log.debug("Is active: " + explosion.active);

            }
        }
    }

    public void drawVFX(SpriteBatch batch) {
        for(VFXObject explosion : VFXObjectList) {
            explosion.draw(batch);
        }
    }


}
