package com.smashprofs.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.smashprofs.tests.helper.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(GdxTestRunner.class)
public class assetest {


    @Test
    public void loadTexture()
    {
        TmxMapLoader mapLoader = new TmxMapLoader();
        TiledMap map = mapLoader.load("../assets/1/Map1New2Remake.tmx");
        assertThat(map, notNullValue());
    }

    @Test
    public void testForMainMenuFiles() {

        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/ssp.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/bgmenu.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/exitButtonActive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/exitButtonInactive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/playButtonActive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/mainmenu/buttons/playButtonInactive.png").exists());
    }

    @Test
    public void testForWinScreenFiles() {
        Assert.assertTrue(Gdx.files.internal("../assets/winscreen/menuButtonActive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/winscreen/menuButtonInactive.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/winscreen/player1.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/winscreen/player2.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/winscreen/pokal.png").exists());
        Assert.assertTrue(Gdx.files.internal("../assets/winscreen/winbg.png").exists());
    }
}
