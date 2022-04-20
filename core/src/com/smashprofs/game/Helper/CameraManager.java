package com.smashprofs.game.Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.smashprofs.game.Actors.PlayerClass;

import static com.smashprofs.game.Actors.PlayerClass.PPM;
import static com.smashprofs.game.Screens.PlayScreen.viewport;

public class CameraManager {

    private OrthographicCamera  gameCamera = new OrthographicCamera();

    private static final CameraManager cameraManager_INSTANCE = new CameraManager();

    //private constructor to avoid client applications to use constructor
    private CameraManager() {

    }

    public static CameraManager getCameraManager_INSTANCE() {
        return cameraManager_INSTANCE;
    }

    public void setCamera(PlayerClass playerOne, PlayerClass playerTwo) {

    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }
    public void updateCameraManager(PlayerClass playerOne, PlayerClass playerTwo){
        gameCamera.update();
        //dynamic camera -> please someone implement this into CameraManager im to lazy to do it now
        Vector3 middleVector = new Vector3((playerOne.getPosition().x + playerTwo.getPosition().x)/2, 100 / PPM, 0);
        gameCamera.position.set(middleVector);}
}
