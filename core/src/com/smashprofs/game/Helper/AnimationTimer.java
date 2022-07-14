package com.smashprofs.game.Helper;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * A timer for making sure that the Player punch animation only gets played from the beginning
 * when it was run completely.
 */
public class AnimationTimer {
    private long startTime;
    private final float duration;

    public AnimationTimer(float durationInSeconds) {
        this.duration = durationInSeconds * 1000;
    }

    public void start() {
        this.startTime = TimeUtils.millis();
    }

    public boolean isFinished() {
        return TimeUtils.timeSinceMillis(startTime) >= duration;
    }

}
