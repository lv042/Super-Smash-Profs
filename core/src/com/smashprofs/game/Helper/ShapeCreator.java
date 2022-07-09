package com.smashprofs.game.Helper;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Actors.Players.Player.PPM;

/**
 * Can create different shapes that can be used in the fixture creation process.
 */
public class ShapeCreator {
    private static Logger log = LogManager.getLogger(ShapeCreator.class);

    /**
     * Returns a polygon shape of a specified width and height.
     * @param width
     * Width of the polygon shape.
     * @param height
     * Height of the polygon shape.
     * @return
     * The polygon shape.
     */
    //no usages yet
    public static PolygonShape getPolygonShape(float width, float height) {
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(width/PPM, height/PPM);
        log.debug("Created new PolygonShape. Width: " + width + " Height: " + height);
        return bodyShape;
    }

    /**
     * Returns a circle shape of a specified radius.
     * @param radiusNoPPM
     * The radius of the circle shape (Do NOT /PPM this value before inserting!)
     * @return
     * The circle shape.
     */
    public static CircleShape getCircleShape(float radiusNoPPM) {
        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(radiusNoPPM/PPM);
        log.debug("Created new CircleShape. Radius: " + radiusNoPPM);
        return bodyShape;
    }
}
