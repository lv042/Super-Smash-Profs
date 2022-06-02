package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.awt.*;

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class ShapeCreator {
    public static PolygonShape getPolygonShape(float width, float height) {
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(width/PPM, height/PPM);
        return bodyShape;
    }
    public static CircleShape getCircleShape(float radiusNoPPM) {
        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(radiusNoPPM/PPM);
        return bodyShape;
    }
}
