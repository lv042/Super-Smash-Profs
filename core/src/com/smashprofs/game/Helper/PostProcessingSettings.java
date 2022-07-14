package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.Pixmap;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import com.crashinvaders.vfx.effects.util.MixEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Creates and returns preconfigured VfxManagers to use on different screens.
 */
public class PostProcessingSettings {

    private static Logger log = LogManager.getLogger(PostProcessingSettings.class);

    VfxManager postProcessingManager = new VfxManager(Pixmap.Format.RGBA8888);

    /**
     * Sets up a pre-configured VfxManager.
     */
    private void setUpManager()
    {
        log.debug("Setting up the VFXManager...");
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
        log.debug("Finished setting up VFXManager");
    }

    /**
     * Get a preconfigured VfxManager.
     * @return
     * A preconfigured VfxManager.
     */
    public VfxManager getPostProcessingManager()
    {
        setUpManager();
        log.debug("Returning preconfigured postProcessingManager");
        return postProcessingManager;
    }


}
