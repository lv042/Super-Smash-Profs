package com.smashprofs.game.Actors.Projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Players.Player;

public class Landmine extends Projectile {

    private final Animation<TextureRegion> mineExplode;
    private final Animation<TextureRegion> mineWait;
    private final Texture mineWaiting;
    private final Texture mineExploding;
    float stateTime = 0;
    
    boolean playerInDetonationRadius = false;
    private State currentState;

    public enum State {
        WAITING, EXPLODING
    }

    public Landmine(World world, Player originPlayer) {
        super(world, originPlayer, "Landmine", new Texture("landmine.png"));
        b2dbody.setFixedRotation(false);
        this.currentState = State.WAITING;

        this.mineWaiting = new Texture("landmine.png");
        this.mineExploding = new Texture("explosions/explosion-2.png");

        TextureRegion[] exploding = TextureRegion.split(mineExploding, 100, 100)[0];
        mineExplode = new Animation(0.25f, exploding[0], exploding[1], exploding[2], exploding[3]);
        mineExplode.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] waiting = TextureRegion.split(mineWaiting, 32, 32)[0];
        mineWait = new Animation(0.25f, waiting[0]);
        mineWait.setPlayMode(Animation.PlayMode.NORMAL);


    }


    @Override
    public void update(float delta){
        super.update(delta);



        rotation = -2.25*(b2dbody.getAngle()*2*Math.PI*4);
        sprite.setRotation( (float) rotation);
        sprite.setRegion(getRenderTexture(stateTime)); //set the texture to the current state of the movement

    }

    public TextureRegion getRenderTexture(float stateTime) {
        TextureRegion frame = null;
        switch (this.currentState) {
            case WAITING:
                sprite.setRegion(mineWaiting);
                frame = mineWait.getKeyFrame(stateTime);
                break;

            case EXPLODING:
                sprite.setRegion(mineExploding);
                frame = mineExplode.getKeyFrame(stateTime);
                break;

        }
        return frame;
    }

    private void setCurrentState() {
        // TODO: Implement logic when to set 
        if(!playerInDetonationRadius) {
            this.currentState = State.WAITING;
        }
        else if(playerInDetonationRadius) {
            this.currentState = State.EXPLODING;
        }
    }

}


