package com.smashprofs.game.Helper;

import com.smashprofs.game.Actors.PlayerClass;

public class CameraManager {

    private static final CameraManager cameraManager_INSTANCE = new CameraManager();

    //private constructor to avoid client applications to use constructor
    private CameraManager() {
    }

    public static CameraManager getCameraManager_INSTANCE() {
        return cameraManager_INSTANCE;
    }

    public void setCamera(PlayerClass playerOne, PlayerClass playerTwo) {

    }



}
