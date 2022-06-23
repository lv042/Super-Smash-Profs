package com.smashprofs.tests;

import com.badlogic.gdx.Gdx;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;

public class testPlayers {
    private com.smashprofs.game.Game Game;

    @Before
   public final void prepare(){
       Game.unitTestMode=true;
   }

   @Test
    public void testJump(){
        //PlayScreen.playerOne.jumpInput = true;
   }

    @Test
    public void badlogicLogoFileExists() {
        Assert.assertTrue(Gdx.files.internal("1/Map1New2.tmx").exists());
    }
}
