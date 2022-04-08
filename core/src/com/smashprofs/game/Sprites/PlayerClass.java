package com.smashprofs.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Helper.util;

import java.util.Timer;
import java.util.TimerTask;
import com.smashprofs.game.Screens.PlayScreen;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerClass extends Sprite {

    private Vector2 poistion;

    public Vector2 getPosition() {
        return poistion;
    }

    private float attackReach = 0.13f;

    public float getAttackReach() {
        return attackReach;
    }

    public static final float PPM = 100;
    private final World world;
    public float damping = 0.9995f; //the closer this value is to zero the more the player will slow down
    InputState currentInputState;

    public void updatePosition(float deltatime) {
        poistion = b2dbody.getPosition();
    }

    public enum InputState {
        WASD, ARROWS
    }

    float playerCollisionBoxRadius = 5;
    boolean isGrounded = true;
    private float respawnDamping = 0.1f;

    private boolean collideWithOtherPlayers = false;
    private int maxExtraJumps = 1; //currently, only works with one extra jump
    private int jumpCount = 0;
    private BodyDef bdef;
    boolean standardAttackInput = false;

    public boolean isStandardAttackInput() {
        return standardAttackInput;
    }

    private Vector2 spawnpoint;
    private Body b2dbody;
    private float maxVelocity = 1.2f;
    private boolean isExtraJumpReady;
    private float jumpForce = 3f;
    private float walkingSpeedMultiplier = 1.005f;
    private State currentState;

    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }
    private Texture texture;
    private SpriteBatch batch;
    private Sprite sprite;

    public PlayerClass(World world, InputState inputState, Vector2 spawnpoint) {
        this.world = world;
        this.currentInputState = inputState;
        this.spawnpoint = spawnpoint;


        definePlayer(inputState);
    }
    
    private boolean facingRight = true;

    public float getRespawnDamping() {
        return respawnDamping;
    }

    public float getDamping() {
        return damping;
    }

    public float getWalkingSpeedMultiplier() {
        return walkingSpeedMultiplier;
    }

    public State getCurrentState() {
        return currentState;
    }
    
    public float getJumpForce() {
        return jumpForce;
    }

    public Body getB2dbody() {
        return b2dbody;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public int getJumpCOunt() {
        return jumpCount;
    }

    public void setJumpCOunt(int jumpCOunt) {
        this.jumpCount = jumpCOunt;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public Vector2 getSpawnPoint() {
        return spawnpoint;
    }

    public boolean getIsExtraJumpReady() {
        return isExtraJumpReady;
    }

    public BodyDef getBdef() {
        return bdef;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getSpawnpoint() {
        return spawnpoint;
    }

    //basically our constructor
    private void definePlayer(InputState inputState) {
        bdef = new BodyDef();
        bdef.position.set(spawnpoint.x / PPM, spawnpoint.y / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2dbody = world.createBody(bdef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects
        
        shape.setRadius(playerCollisionBoxRadius / PPM);

        //Implement the player textures and animations -> @Maurice @Alex


//        texture = new Texture("prettyplayer.png");
//        batch = new SpriteBatch();
//        sprite = new Sprite(texture);


        fDef.shape = shape;

        if(collideWithOtherPlayers) {
            fDef.filter.groupIndex = 0;
        }
        else {
            fDef.filter.groupIndex = -1;
        }

//        fDef.density = 1;
//        fDef.restitution = 0.1f;
//        fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);



    }

    public void managePlayerInput(float dt) {


        int upDownInput = 0;
        int leftRightInput = 0;
        boolean jumpInput = false;


        if (currentInputState == InputState.WASD) {
            leftRightInput = util.adAxis();
            //upDownInput = util.wsAxis();
            jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.W);
            standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.V);

        }
        if (currentInputState == InputState.ARROWS) {
            leftRightInput = util.leftrightAxis();
            //upDownInput = util.updownAxis();
            jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
            standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
        }



        //jumping
        System.out.println(jumpCount);
        System.out.println(isExtraJumpReady);

        if(jumpCount <= maxExtraJumps && (jumpInput) && (isGrounded || isExtraJumpReady)){

            jumpCount++;
            if(isExtraJumpReady){
                getB2dbody().setLinearVelocity(getB2dbody().getLinearVelocity().x, 0.1f);
            }


            getB2dbody().applyLinearImpulse(new Vector2(0, getJumpForce()), getB2dbody().getWorldCenter(), true);

            System.out.println("Jumping");


            final Timer timer = new Timer();

            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                                isExtraJumpReady = true;

                }
            };
            timer.schedule(task, 100); // delay in milliseconds

        }

        //walking left and right
        if(getB2dbody().getLinearVelocity().x < getMaxVelocity()){
            getB2dbody().applyLinearImpulse(new Vector2(0.05f * leftRightInput * getWalkingSpeedMultiplier(), 0.0f), getB2dbody().getWorldCenter(), true);
        }

        //damping
        if(isGrounded()){
            getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x * damping, getB2dbody().getLinearVelocity().y));

        }
    }

    public boolean reachedWorldEdge() {

        if (b2dbody.getPosition().y < 0) {
            return true;
        }
        return false;
    }
    
    //Check if the player is touching the ground
    public void checkGrounded() {
        if (b2dbody.getLinearVelocity().y == 0) {
            isGrounded = true;
            //System.out.println("grounded");

            jumpCount = 0;
            isExtraJumpReady = false;


            return;
        }
        isGrounded = false;

    }

    public void respawnPlayers() {
        if(reachedWorldEdge()){
            //getB2dbody().applyLinearImpulse(new Vector2(0, 2f), getB2dbody().getWorldCenter(), true);
            getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x * getRespawnDamping(), 5f));
            System.out.println("Player respawn jump");
        }

    }

    public void limitPlayersToEdge(){
        //sets player velocity to 0 if they are at the edge of the map
        float pushBack = 1f;

        if(getB2dbody().getPosition().x > PlayScreen.getViewport().getWorldWidth()){
            getB2dbody().setLinearVelocity(new Vector2(-pushBack, getB2dbody().getLinearVelocity().y));
        }
        if(getB2dbody().getPosition().x < 0){
            getB2dbody().setLinearVelocity(new Vector2( pushBack, getB2dbody().getLinearVelocity().y) );
        }
    }



}
