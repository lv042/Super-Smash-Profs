package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.smashprofs.game.Actors.Players.Player;

import static com.smashprofs.game.Actors.Players.Player.PPM;

/**
 * Manages the game camera for the two players
 */
public class CameraManager {

    private OrthographicCamera  gameCamera = new OrthographicCamera();

    private static final CameraManager cameraManager_INSTANCE = new CameraManager();

    //private constructor to avoid client applications to use constructor
    private CameraManager() {}

    public static CameraManager getCameraManager_INSTANCE() {
        return cameraManager_INSTANCE;
    }



    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    /**
     * Updates the camera position to the middle point on the vector connecting the two players.
     * @param playerOne
     * The first player.
     * @param playerTwo
     * The second player.
     */
    public void updateCameraManager(Player playerOne, Player playerTwo){
        gameCamera.update();
        Vector3 middleVector = new Vector3((playerOne.getPosition().x + playerTwo.getPosition().x)/2, 110 / PPM, 0);
        gameCamera.position.set(middleVector);}
}
