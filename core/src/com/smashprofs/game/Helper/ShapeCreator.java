package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class ShapeCreator {
    private static Logger log = LogManager.getLogger(ShapeCreator.class);

    public static PolygonShape getPolygonShape(float width, float height) {
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(width/PPM, height/PPM);
        log.debug("Created new PolygonShape. Width: " + width + " Height: " + height);
        return bodyShape;
    }
    public static CircleShape getCircleShape(float radiusNoPPM) {
        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(radiusNoPPM/PPM);
        log.debug("Created new CircleShape. Radius: " + radiusNoPPM);
        return bodyShape;
    }
}
