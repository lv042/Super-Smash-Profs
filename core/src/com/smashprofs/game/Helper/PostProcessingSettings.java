package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import com.crashinvaders.vfx.effects.util.MixEffect;

public class PostProcessingSettings {

    VfxManager postProcessingManager = new VfxManager(Pixmap.Format.RGBA8888);

    public void setUpManager()
    {
        FilmGrainEffect filmGrainEffect = new FilmGrainEffect();
        filmGrainEffect.setNoiseAmount(0.08f);
        OldTvEffect oldTvEffect = new OldTvEffect();
        VignettingEffect vignettingEffect = new VignettingEffect(false);
        vignettingEffect.setIntensity(0.5f);
        MotionBlurEffect motionBlurEffect = new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MIX, 0.2f);
        BloomEffect bloomEffect = new BloomEffect();

        postProcessingManager.addEffect(bloomEffect);
        postProcessingManager.addEffect(motionBlurEffect);
        postProcessingManager.addEffect(vignettingEffect);
        postProcessingManager.addEffect(filmGrainEffect);
        postProcessingManager.addEffect(oldTvEffect);
    }

    public VfxManager getPostProcessingManager()
    {
        setUpManager();
        return postProcessingManager;
    }


}
