package com.smashprofs.tests;

import com.badlogic.gdx.Gdx;
import com.smashprofs.tests.helper.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class assetest {


    @Test
    public void testForMainMenuFiles() {
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/ssp.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/bgmenu.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/exitButtonActive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/exitButtonInactive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/playButtonActive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/playButtonInactive.png").exists());
    }
}
