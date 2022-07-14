package com.smashprofs.game.Actors.Players;


import com.smashprofs.game.Actors.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.smashprofs.game.Helper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A controllable Player
 */
public abstract class Player extends GameObject implements PlayerView {

    private static Logger log = LogManager.getLogger(Player.class);

    public static final float PPM = 100;
    private final Animation<TextureRegion> stand, run, jump, punch;
    private final float stompSpeed = -5f;
    private final String deathSoundMp3 = "sounds/death.mp3";
    private final String punchSoundMp3 = "sounds/punch.mp3";
    private final String stompSoundWav = "sounds/stomp.wav";
    private final String damageSoundMp3 = "sounds/damage.mp3";
    private final CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();
    private final B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();
    private final float attackReach = 0.2f;
    private final float respawnDamping = 0.1f;
    private final int attackDamage = 10;
    private final boolean collideWithOtherPlayers = false;
    private final int maxExtraJumps = 1; //currently, only works with one extra jump
    private final Vector2 spawnpoint;
    private final float maxVelocity = 1f;
    private final float jumpForce = 2.2f;
    private final float walkingSpeedMultiplier = 1.1f;
    private final SoundManager soundManager;
    private final String userData;
    private float damping = 0.999f; //the closer this value is to zero the more the player will slow down
    InputState currentInputState;
    PlayerTypes playerType;
    float stateTime = 0;
    float playerCollisionBoxRadius = 5;
    boolean isGrounded = false;
    boolean isPunching = false;
    AnimationTimer animTimer;
    boolean standardAttackInput = false;
    private final Texture playerStand, playerRun, playerJump, playerPunch;
    private String playerName;
    private Vector2 position;
    private boolean isStomping;
    private boolean isDead = false;
    private boolean stompHitground;
    private float previousY = 0;
    private float currentY = 0;
    private boolean isNotTouchingTiles = true;
    private float HP = 100;
    private boolean isBlocking = false;
    private int jumpCount = 0;
    private BodyDef bdef;
    private boolean isExtraJumpReady;
    private State currentState;
    private float gravity = -0.098f;
    private final float startingGravity = gravity;
    private Vector2 forcesCombined = new Vector2(0, 0);
    private boolean isShooting = false;
    private boolean facingRight = true;
    private int isFacingRightAxe = 1;  // must be 1 or -1 otherwise the first projectile spawns in the player
    private float lastAxis;
    
    private final Sprite sprite = new Sprite(); //Sprite of the GameObject

    /**
     * Creates a playable player that has a b2dBody, a sprite and animation for his individual states.
     * @param world
     * The world the player lives in.
     * @param inputState
     * The input method the player can be controlled with.
     * @param spawnpoint
     * The point the player will spawn at.
     * @param playerName
     * The player's in-game name.
     * @param playerType
     * The player's type.
     * @param userData
     * The userData of the player as a game object.
     * @param playerStandTex
     * The stand texture stripe (4 frames)
     * @param playerRunTex
     * The run texture stripe (6 frames)
     * @param playerJumpTex
     * The jump texture stripe (2 frames)
     * @param playerPunchTex
     * The punch texture stripe (3 frames)
     */
    Player(World world, InputState inputState, Vector2 spawnpoint, String playerName, PlayerTypes playerType, String userData, Texture playerStandTex, Texture playerRunTex, Texture playerJumpTex, Texture playerPunchTex) {
        super(userData);

        this.playerType = playerType;
        this.userData = userData;

        this.playerStand = playerStandTex;
        this.playerRun = playerRunTex;
        this.playerJump = playerJumpTex;
        this.playerPunch = playerPunchTex;

        TextureRegion[] standing = TextureRegion.split(playerStand, 100, 100)[0];
        stand = new Animation<>(0.25f, standing[0], standing[1], standing[2], standing[3]);
        stand.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] running = TextureRegion.split(playerRun, 100, 100)[0];
        run = new Animation<>(0.15f, running[0], running[1], running[2], running[3], running[4], running[5]);
        run.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] jumping = TextureRegion.split(playerJump, 100, 100)[0];
        jump = new Animation<>(1f, running[0], running[3]); //Originally -> jump = new Animation(0.15f, running[0], jumping[1], jumping[2]); -> but jump animation looks like poop
        jump.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] punching = TextureRegion.split(playerPunch, 100, 100)[0];
        punch = new Animation<>(0.05f, punching[0], punching[1], punching[2]);
        punch.setPlayMode(Animation.PlayMode.NORMAL);

        this.currentState = State.STANDING;
        this.animTimer = new AnimationTimer(punch.getAnimationDuration());

        sprite.setBounds(0, 15, 25 / PPM, 25 / PPM);

        this.currentInputState = inputState;
        this.spawnpoint = spawnpoint;
        this.playerName = playerName;

        definePlayer(world);

        soundManager = SoundManager.getSoundManager_INSTANCE();

        log.debug("Player created:" + this.playerName);

    }



    private void updatePosition(float deltatime) {
        position = b2dbody.getPosition();
    }

    /**
     * Update the player and all of his parameters.
     * Do necessary checks.
     * @param deltatime
     * The game delta time.
     */
    public void update(float deltatime) {
        updatePosition(deltatime);
        touchingTiles();
        checkGrounded();
        managePlayerInput(deltatime);

        applyForces(0, 0);
        checkHealth();
        limitPlayersToEdge();
        respawnPlayers();


        if (deltatime == 0) return;
        if (deltatime > 0.1f) deltatime = 0.1f;
        stateTime += deltatime;

        setAnimationState();
        setAnimationPosition();


    }


    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    private void touchingTiles() {
        if (userData.equals("PlayerTwo")) {
            isNotTouchingTiles = contactListener.isP2NotTouchingTile();
        } else if (userData.equals("PlayerOne")) {
            isNotTouchingTiles = contactListener.isP1NotTouchingTile();

        }

    }

    /**
     * Refreshes the texture drawn on the player's position.
     * Sets the texture position to the player body's position.
     * Flips the sprite according to the players walking direction.
     * Sets the texture to the most up-to-date animation frame.
     */
    private void setAnimationPosition() {
        sprite.setPosition(b2dbody.getPosition().x - sprite.getWidth() / 2, b2dbody.getPosition().y - sprite.getHeight() / 4); //set the position of the animation to the center of the body


        sprite.setRegion(getRenderTexture(stateTime)); //set the texture to the current state of the movement
        sprite.setFlip(facingRight, false); // lets the player face the correct direction
    }

    /**
     * Sets the player state based on velocities and isGrounded.
     */
    private void setAnimationState() {
        // Set State depending on x-velocity of the player body
        if (getB2dbody().getLinearVelocity().x == 0f && isGrounded()) {
            this.currentState = State.STANDING;


        } else if (getB2dbody().getLinearVelocity().x != 0f && isGrounded()) {
            this.currentState = State.RUNNING;
        } else if (!isGrounded() && getB2dbody().getLinearVelocity().y > 0 && previousY != currentY) {
            this.currentState = State.JUMPING;
        }
    }


    /**
     * Defining the player. Creating the body definitions, the fixture definitions and the b2dBody.
     * @param world
     * The world the player's b2dBody will be created in.
     */
    private void definePlayer(World world) {
        bdef = new BodyDef();
        bdef.position.set(spawnpoint.x / PPM, spawnpoint.y / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(playerCollisionBoxRadius / PPM);

        fDef.shape = shape;

        fDef.filter.categoryBits = B2dContactListener.PLAYER_ENTITY;
        fDef.filter.maskBits = B2dContactListener.CIRCLESTAR_ENTITY | B2dContactListener.PROJECTILE_ENTITY | B2dContactListener.WORLD_ENTITY; //collides with everything except the player
        fDef.filter.groupIndex = -1; // prevents player from colliding with other players
        //fDef.density = 0.1f;
        //fDef.restitution = 0.4f;
        // fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);

    }

    /**
     * Checks if the player has enough health.
     * If not, the player dies.
     */
    private void checkHealth() {
        if (getHP() <= 0) {
            die();
        }
    }

    /**
     * Lets the player die.
     * If the player is not dead already, sets isDead to true and plays the deathSound.
     */
    private void die() {
        if (!isDead) {
            setDead(true);
            soundManager.playSound(deathSoundMp3);
        }
    }

    public boolean isShooting() {
        return isShooting;
    }

    public float getPlayerCollisionBoxRadius() {
        return playerCollisionBoxRadius;
    }

    /**
     * Handles input from controllers and the keyboard.
     * Controls are also mapped in here.
     * @param dt
     * The game delta time.
     */
    private void managePlayerInput(float dt) {

        Array<Controller> controllers2 = Controllers.getControllers();

        float upDownInput = 0;
        float leftRightInput = 0;
        boolean jumpInput = false;
        isBlocking = false;
        boolean stompInput = false;

        // Falls Player den InputState "WASD" hat
        if (currentInputState == InputState.WASD) {
            // Prüfe, ob mindestens 1 Controller angeschlossen ist
            if (controllers2.size > 0) {
                Controller p1Controller = controllers2.get(0);
                if (Math.abs(p1Controller.getAxis(Xbox360Pad.AXIS_LEFT_Y)) > Xbox360Pad.CONTROLLER_DEADZONE) {
                    leftRightInput = p1Controller.getAxis(Xbox360Pad.AXIS_LEFT_Y);
                    upDownInput = p1Controller.getAxis(Xbox360Pad.AXIS_RIGHT_X);
                    jumpInput = p1Controller.getButton(Xbox360Pad.BUTTON_A);
                    standardAttackInput = p1Controller.getButton(Xbox360Pad.BUTTON_B);
                    isBlocking = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
                    stompInput = p1Controller.getButton(Xbox360Pad.BUTTON_Y);

                    //TODO: Rate limiter needed here!! Otherwise, a lot of projectiles will spawn at once!
                    isShooting = p1Controller.getButton(Xbox360Pad.BUTTON_X);
                    //log.debug(p1Controller.getAxis(Xbox360Pad.AXIS_LEFT_X));

                }
            } else {
                leftRightInput = Util.adAxis();
                upDownInput = Util.wsAxis();
                jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.W);
                standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.V) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
                isBlocking = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
                stompInput = Gdx.input.isKeyJustPressed(Input.Keys.S);
                isShooting = Gdx.input.isKeyJustPressed(Input.Keys.F);

            }
            if(standardAttackInput) {
                animTimer.start();
            }
            if(animTimer.isFinished()) {
                isPunching = false;
            }
            else{
                isPunching = true;
            }
        }


        if (currentInputState == InputState.ARROWS) {

            if(controllers2.size > 1) {
                Controller p2Controller = controllers2.get(1);
                if (Math.abs(p2Controller.getAxis(Xbox360Pad.AXIS_LEFT_Y)) > Xbox360Pad.CONTROLLER_DEADZONE) {
                    leftRightInput = p2Controller.getAxis(Xbox360Pad.AXIS_LEFT_Y);
                    upDownInput = p2Controller.getAxis(Xbox360Pad.AXIS_RIGHT_X);
                    jumpInput = p2Controller.getButton(Xbox360Pad.BUTTON_A);
                    standardAttackInput = p2Controller.getButton(Xbox360Pad.BUTTON_B);
                    isBlocking = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
                    stompInput = p2Controller.getButton(Xbox360Pad.BUTTON_Y);

                    isShooting = p2Controller.getButton(Xbox360Pad.BUTTON_X);
                    //log.debug(p2Controller.getAxis(Xbox360Pad.AXIS_LEFT_X));

                }
            } else {
                leftRightInput = Util.leftrightAxis();
                upDownInput = Util.updownAxis();
                jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.UP);
                standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT);
                isBlocking = Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
                stompInput = Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
                isShooting = Gdx.input.isKeyJustPressed(Input.Keys.P);
            }
            if(standardAttackInput) {
                animTimer.start();
            }
            if(animTimer.isFinished()) {
                isPunching = false;
            }
            else{
                isPunching = true;
            }
        }

        // Play attack sound
        if (standardAttackInput) {
            soundManager.playSound(punchSoundMp3);
        }

        //calculate direction
        if (b2dbody.getLinearVelocity().x > 0) {
            facingRight = true;
            isFacingRightAxe = 1;
        }
        if (b2dbody.getLinearVelocity().x < 0) {
            facingRight = false;
            isFacingRightAxe = -1;
        }


        // Double jumping
        if (jumpCount <= maxExtraJumps && (jumpInput) && (isGrounded || isExtraJumpReady)) {

            jumpCount++;

            getB2dbody().applyLinearImpulse(new Vector2(0, getB2dbody().getLinearVelocity().y * (-0.8f) + getJumpForce()), getB2dbody().getWorldCenter(), true);

            isExtraJumpReady = true;
        }

        //walking left and right
        if (getB2dbody().getLinearVelocity().x < getMaxVelocity() && getB2dbody().getLinearVelocity().x > - getMaxVelocity()) {
            getB2dbody().applyLinearImpulse(new Vector2(0.05f * leftRightInput * getWalkingSpeedMultiplier(), 0.0f), getB2dbody().getWorldCenter(), true);
        }

        //Damping velocity when changing axis
        if(leftRightInput!=lastAxis&&leftRightInput!=0&&!isGrounded())
        {
                getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x-(getB2dbody().getLinearVelocity().x * 0.3f), getB2dbody().getLinearVelocity().y));
                lastAxis = leftRightInput;
        }


        //damping
        if (isGrounded()) {
            getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x * damping, getB2dbody().getLinearVelocity().y));
        }

        //stomp

        if (stompInput && !isGrounded) {
            applyForces(0, stompSpeed);
            //System.out.println("Stomping");
            log.debug("Stomping");
            isStomping = true;
            setHP(getHP() - 0.1f);
        }
    }

    /**
     * Returns the matching frame of the player animation.
     * @param stateTime
     * The current state time.
     * @return
     * The frame of the animation matching the current state time and the player state.
     */
    private TextureRegion getRenderTexture(float stateTime) {
        TextureRegion frame = null;

        if(isPunching) {
            sprite.setRegion(playerPunch);
            frame = punch.getKeyFrame(stateTime);
        }
        else {
            switch (this.currentState) {
                case STANDING:
                    sprite.setRegion(playerStand);
                    frame = stand.getKeyFrame(stateTime);
                    break;

                case RUNNING:

                    sprite.setRegion(playerRun);
                    frame = run.getKeyFrame(stateTime);
                    break;

                case JUMPING:
                    sprite.setRegion(playerJump);
                    frame = jump.getKeyFrame(stateTime);
                    break;

            }
        }

        // log.debug("isGrounded: " + isGrounded());
        return frame;


    }

    private boolean reachedWorldEdge() {

        return b2dbody.getPosition().y < 0;
    }

    /**
     * Checks if the player is touching the ground.
     * Sets isGrounded, isNotTouchingTiles, isExtraJumpReady and isStomping accordingly.
     */
    private void checkGrounded() {
        currentY = b2dbody.getPosition().y;

        if (b2dbody.getLinearVelocity().y - getGravity() >= 0.2 || b2dbody.getLinearVelocity().y - getGravity() <= -0.2) {
            isNotTouchingTiles = true;
        }

        if (!isNotTouchingTiles) {
            if (isStomping()) {
                soundManager.playSound(stompSoundWav);
                setStompHitground(true);
                isStomping = false;
            } else {
                setStompHitground(false);
                isStomping = false;
            }
            isGrounded = true;
            setGravity(getStartingGravity());

            jumpCount = 0;
            isExtraJumpReady = false;

            previousY = b2dbody.getLinearVelocity().y;

        } else {
            isGrounded = false;
        }
        previousY = b2dbody.getLinearVelocity().y;


    }

    private float getGravity() {
        return gravity;
    }

    private void setGravity(float gravity) {
        this.gravity = gravity;
    }

    //respawn jumping
    private void respawnPlayers() {
        if (reachedWorldEdge()) {
            getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x * getRespawnDamping(), 2.6f));

            //lower gravity for some seconds :)
            setGravity(startingGravity * 0.3f);
            soundManager.playSound(getDamageSoundMp3());

            //prevents player from stomping into the void to get a respawn jump and then still have the current stomping active
            setStompHitground(false);
            isStomping = false;

            //damages the player
            setHP(getHP() - 10);

        }

    }

    public void applyForces(float x, float y) {

        //all forces applied to the player should be done with this method
        //all forces applied to the player should be done with this method
        forcesCombined = new Vector2(0 + x, gravity + y);
        getB2dbody().applyLinearImpulse(forcesCombined, getB2dbody().getWorldCenter(), true);
        forcesCombined.setZero();
    }

    private void limitPlayersToEdge() {
        //sets player velocity to 0 if they are at the edge of the map
        float pushBack = 1f;

        // log.debug("b2dBody position: " + getB2dbody().getPosition());
        if (getB2dbody().getPosition().x  > 8.5) {

            getB2dbody().setLinearVelocity(new Vector2(-pushBack, getB2dbody().getLinearVelocity().y + 0.1f));
            //lower gravity for some seconds :)g
            setGravity(startingGravity * 0.3f);
            return;
        }
        if (getB2dbody().getPosition().x < 3) {
            getB2dbody().setLinearVelocity(new Vector2(pushBack, getB2dbody().getLinearVelocity().y + 0.1f));
            //lower gravity for some seconds :)
            setGravity(startingGravity * 0.3f);
        }
    }



    public enum InputState {
        WASD, ARROWS
    }

    private enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }

    // GETTER AND SETTER METHODS:

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public float getHP() {
        return HP;
    }

    public void setHP(float HP) {
        this.HP = HP;

    }

    public PlayerTypes getPlayerType() {
        return this.playerType;
    }
    public int getAttackDamage() {
        return attackDamage;
    }

    public boolean isStandardAttackInput() {
        return standardAttackInput;
    }

    public float getStartingGravity() {
        return startingGravity;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public int getIsFacingRightAxe() {
        return isFacingRightAxe;
    }

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

    public boolean isStomping() {
        return isStomping;
    }

    public BodyDef getBdef() {
        return bdef;
    }

    public Vector2 getSpawnpoint() {
        return spawnpoint;
    }
    public Sprite getPlayerSprite() {
        return sprite;
    }

    public boolean isStompHitground() {
        return stompHitground;
    }

    public void setStompHitground(boolean stompHitground) {
        this.stompHitground = stompHitground;
    }

    public Player getInstancePlayer(Player player) {
        return this;
    }

    public String getDamageSoundMp3() {
        return damageSoundMp3;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getPositionView() {
        return position;
        // returns the location witnout
    }
    public Sprite getPlayerSpriteView() {
        return this.sprite;
    }
    public int getIsFacingRightAxeView() {
        return this.isFacingRightAxe;
    }
    public float getPlayerCollisionBoxRadiusView() {return this.playerCollisionBoxRadius;}

    public float getAttackReach() {
        return attackReach;
    }




}