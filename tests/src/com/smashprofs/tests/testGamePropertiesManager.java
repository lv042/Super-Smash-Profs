package com.smashprofs.tests;

import com.badlogic.gdx.Gdx;
import com.smashprofs.game.Helper.Keys;
import com.smashprofs.game.Helper.gamePropertiesManager;
import com.smashprofs.tests.helper.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(GdxTestRunner.class)
public class testGamePropertiesManager {

    @Test
    public void testStringToSeconds() {
        Assert.assertEquals(63, gamePropertiesManager.stringToSeconds("1 min 3 sek"));
        Assert.assertNotEquals(60, gamePropertiesManager.stringToSeconds("1 min 3 sek"));
        Assert.assertEquals(120, gamePropertiesManager.stringToSeconds("2 min 0 sek"));
        Assert.assertNotEquals(120, gamePropertiesManager.stringToSeconds("2 min 1 sek"));
        Assert.assertEquals(25234, gamePropertiesManager.stringToSeconds("420 min 34 sek"));
    }

    @Test
    public void testSecondsToString() {
        Assert.assertEquals("1 min 3 sek", gamePropertiesManager.secondsToString(63));
        Assert.assertNotEquals("1 min 0 sek", gamePropertiesManager.secondsToString(63));
        Assert.assertEquals("2 min 0 sek", gamePropertiesManager.secondsToString(120));
        Assert.assertNotEquals("2 min 0 sek", gamePropertiesManager.secondsToString(121));
        Assert.assertEquals("30 min 46 sek", gamePropertiesManager.secondsToString(1846));
    }

    @Test
    public void testPropertiesFileExists() {
        gamePropertiesManager.firstStart();
        Assert.assertTrue(Gdx.files.internal("../core/src/resources/game_info.properties").exists());
    }

    @Test
    public void testPropertiesExists() {
        gamePropertiesManager.firstStart();
        Assert.assertEquals("teest", gamePropertiesManager.getEntry(Keys.TEST));
    }


}
