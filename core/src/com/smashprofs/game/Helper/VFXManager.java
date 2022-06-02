package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.smashprofs.game.Actors.VFXObject;


public class VFXManager {

    public static DelayedRemovalArray<VFXObject> VFXObjectList = new DelayedRemovalArray<>();

    //implements singelton sound manager

    private static final VFXManager VFXManager_INSTANCE = new VFXManager();


    //private constructor to avoid client applications to use constructor
    private VFXManager() {
    }

    public static VFXManager getVFXManager_INSTANCE() {return VFXManager_INSTANCE;}



    public void spawnExplosion(explosionType explosionType, Vector2 spawnpoint) {
        Texture VFXTexture = null;
        String sound = null;
        boolean centered = false;
        boolean spriteIsSquare = false;

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
    }


    public void update(float deltatime) {
        for (VFXObject explosion: VFXObjectList) {
            //TODO: @Alex Bitte die auskommentierten sout's als log.debug übernehmen :)
            explosion.update(deltatime);
            //System.out.println("Is active before loop: " + explosion.active);
            if(!explosion.active) {
                VFXObjectList.removeValue(explosion, true);
            }
            else if(explosion.userData.equals("Destroyed")){

                explosion.active = false;
                //System.out.println("Explosion was set to inactive");
                //System.out.println("Is active: " + explosion.active);

            }
        }
    }

    public void drawVFX(SpriteBatch batch) {
        for(VFXObject explosion : VFXObjectList) {
            explosion.draw(batch);
        }
    }


}
