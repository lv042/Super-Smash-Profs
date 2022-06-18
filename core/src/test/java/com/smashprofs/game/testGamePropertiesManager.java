package com.smashprofs.game;

import com.smashprofs.game.Helper.gamePropertiesManager;
import org.junit.Assert;
import org.junit.Test;

public class testGamePropertiesManager {

    @Test
    public void testStringToSeconds() {
        Assert.assertEquals(63, gamePropertiesManager.stringToSeconds("1 min 3 sek"));
        Assert.assertNotEquals(60,gamePropertiesManager.stringToSeconds("1 min 3 sek"));
        Assert.assertEquals(120, gamePropertiesManager.stringToSeconds("2 min 0 sek"));
        Assert.assertNotEquals(120, gamePropertiesManager.stringToSeconds("2 min 1 sek"));
        Assert.assertEquals(25234,gamePropertiesManager.stringToSeconds("420 min 34 sek"));
    }

    @Test
    public void testSecondsToString() {
        Assert.assertEquals("1 min 3 sek", gamePropertiesManager.secondsToString(63));
        Assert.assertNotEquals("1 min 0 sek",gamePropertiesManager.secondsToString(63));
        Assert.assertEquals("2 min 0 sek", gamePropertiesManager.secondsToString(120));
        Assert.assertNotEquals("2 min 0 sek", gamePropertiesManager.secondsToString(121));
        Assert.assertEquals("30 min 46 sek",gamePropertiesManager.secondsToString(1846));
    }

}
