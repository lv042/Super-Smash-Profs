package com.smashprofs.tests;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Leo;
import com.smashprofs.game.Actors.Players.Player;
import com.smashprofs.game.Helper.PlayerFactory;
import com.smashprofs.game.Helper.PlayerTypes;
import com.smashprofs.game.Helper.ShapeCreator;
import com.smashprofs.tests.helper.GdxTestRunner;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
@Ignore
public class testShapeCreator {
    float PPM =100;


    @Test
    public void testShapes() {
        Assert.assertNotNull(ShapeCreator.getCircleShape(2f));
        Shape circle = new CircleShape();
        circle.setRadius(2f);
        Assert.assertEquals(circle.getRadius(),ShapeCreator.getCircleShape(2f).getRadius()*PPM, 0.0001f);
    }

}
