package com.smashprofs.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.smashprofs.game.Helper.*;
import com.smashprofs.game.Screens.PlayScreen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.Controller;

import java.util.ArrayList;

public abstract class Player extends GameObject{


    public static final float PPM = 100;
    private final World world;
    public float damping = 0.9995f; //the closer this value is to zero the more the player will slow down
    InputState currentInputState;
    float stateTime = 0;
    float playerCollisionBoxRadius = 5;
    boolean isGrounded = false;
    boolean standardAttackInput = false;
    private Texture playerStand;
    private Texture playerRun;
    private Texture playerJump;
    private final Animation<TextureRegion> stand;
    private final Animation<TextureRegion> run;
    private final Animation<TextureRegion> jump;
    private String playerName;
    private Vector2 poistion;
    private float timeCount;
    private float worldTimer;
    private final float stompSpeed = -5f;
    private final String deathSoundMp3 = "death.mp3";
    private final String punchSoundMp3 = "punch.mp3";
    private final String stompSoundWav = "stomp.wav";
    private boolean isStomping;
    private boolean isDead = false;
    private final String damageSoundMp3 = "damage.mp3";
    private boolean stompHitground;
    private float previousY = 0;
    private float currentY = 0;
    private final CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();
    
    private final B2dContactListener contactListener = B2dContactListener.getContactListener_INSTANCE();
    private final float attackReach = 0.2f;
    private final Batch batch = new SpriteBatch();
    private final float respawnDamping = 0.1f;

    private boolean isNotTouchingTiles = true;
    private float HP = 100;
    private boolean isBlocking = false;
    private final int attackDamage = 10;
    private final boolean collideWithOtherPlayers = false;
    private final int maxExtraJumps = 1; //currently, only works with one extra jump
    private int jumpCount = 0;
    private BodyDef bdef;
    private final Vector2 spawnpoint;

    private final float maxVelocity = 1.6f;
    private boolean isExtraJumpReady;
    private final float jumpForce = 2.5f;
    private final float walkingSpeedMultiplier = 1.3f;
    private State currentState;
    private final SoundManager soundManager;
    private final String userData;
    private float gravity = -0.098f;
    private Vector2 forcesCombined = new Vector2(0, 0);
    private final float startingGravity = gravity;
    private boolean isShooting = false;
    private boolean facingRight = true;
    private int isFacingRightAxe = 0;
    private boolean touchingGround;

    private Sprite playerSprite = sprite; //Sprite of the GameObject

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public ArrayList<Projectile> projectiles;

    public ArrayList<Controller> controllers;

    public Player(World world, InputState inputState, Vector2 spawnpoint, String playerName, String userData, Texture playerStandTex, Texture playerRunTex, Texture playerJumpTex) {
        super(world, userData);


        this.userData = userData;

        /*playerStand = new Texture("Sprites/Alex_stand.png");
        playerRun = new Texture("Sprites/Alex_run.png");
        playerJump = new Texture("Sprites/Alex_jump.png");*/

        this.playerStand = playerStandTex;
        this.playerRun = playerRunTex;
        this.playerJump = playerJumpTex;

        TextureRegion[] standing = TextureRegion.split(playerStand, 100, 100)[0];
        stand = new Animation(0.25f, standing[0], standing[1], standing[2], standing[3]);
        stand.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] running = TextureRegion.split(playerRun, 100, 100)[0];
        run = new Animation(0.15f, running[0], running[1], running[2], running[3], running[4], running[5]);
        run.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] jumping = TextureRegion.split(playerJump, 100, 100)[0];
        jump = new Animation(1f, running[0], running[3]); //Originaly -> jump = new Animation(0.15f, running[0], jumping[1], jumping[2]); -> but jump animation looks like poop
        jump.setPlayMode(Animation.PlayMode.LOOP);

        this.currentState = State.STANDING;

        projectiles = new ArrayList<Projectile>();


        playerSprite.setBounds(0, 15, 25 / PPM, 25 / PPM);
        //this.setRegion(alexStand);
        this.world = world;
        this.currentInputState = inputState;
        this.spawnpoint = spawnpoint;
        this.playerName = playerName;

        definePlayer();

        soundManager = SoundManager.getSoundManager_INSTANCE();
        //LeosHomingMissle loli = new LeosHomingMissle(world, this);
        //leosProjectiles.add(loli);

        System.out.println("Player created:" + this.playerName);

    }

    public boolean isStompHitground() {
        return stompHitground;
    }
    
    public Player getInstancePlayer(Player player) {
        return this;
    }

    public void setStompHitground(boolean stompHitground) {
        this.stompHitground = stompHitground;
    }

    public String getDamageSoundMp3() {
        return damageSoundMp3;
    }

    public Vector2 getPosition() {
        return poistion;
    }

    public float getAttackReach() {
        return attackReach;
    }

    public void updatePosition(float deltatime) {
        poistion = b2dbody.getPosition();
    }

    public void update(float deltatime) {
        //System.out.println("Player update");
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

        //System.out.println(projectiles.size());
        for (Projectile projectile : projectiles) {
            projectile.update(deltatime);
            // -> Geht irgendwie nicht. Wählt nicht den richtigen Body aus.
            // Geht aber mit:
            Body projectileBody = projectile.getBody();
            Sprite projectileSprite = projectile.getSprite();
            Texture projectileTexture = projectile.getTexture();

            // setBounds funktioniert, der Rest nicht.

            //projectile.setOrigin(projectileBody.getPosition().x - projectile.getBoundingRectangle().getWidth()/2, projectileBody.getPosition().y - projectile.getBoundingRectangle().getHeight()/2);
            //projectile.setBounds(projectileBody.getPosition().x - ((projectileSprite.getBoundingRectangle().getWidth() / 2)*PPM)/PPM, projectileBody.getPosition().y - ((projectileSprite.getBoundingRectangle().getHeight() / 2)*PPM) /PPM, 32/PPM, 32/PPM);
            //projectile.setBounds(projectileBody.getPosition().x - getWidth() / 2/PPM, projectileBody.getPosition().y - getHeight() / 2 /PPM, getWidth(), getHeight());
            //projectile.setBounds(projectileBody.getPosition().x - projectileSprite.getWidth() / 2/PPM, projectileBody.getPosition().y - projectileSprite.getHeight() / 2 /PPM, projectileTexture.getWidth()/PPM, projectileTexture.getHeight()/PPM);
            //projectile.setPosition(projectileBody.getPosition().x - projectileSprite.getWidth() / 2/PPM, projectileBody.getPosition().y - projectileSprite.getHeight() / 2 /PPM);
            //projectile.setRotation(projectileBody.getAngle() * MathUtils.radiansToDegrees);
            //projectileSprite.setRotation(projectileBody.getAngle() * MathUtils.radiansToDegrees);
            //projectile.setOrigin(projectile.getTexture().getWidth()/2f, projectile.getTexture().getHeight()/2f);

            //System.out.println("Projectile updated from ArrayList");

/*            if(projectileBody.isActive()){
                projectile.active = true;
            }
            else if (!projectileBody.isActive()){
                projectile.active = false;
                System.out.println("Set projectile to inactive");
            }*/


        }

        //System.out.println(projectiles.size());
        for (Projectile lProj : projectiles) {
            lProj.update(deltatime);
        }

    }




    public void draw(Batch batch) {
        playerSprite.draw(batch);
        for (Projectile projectile : projectiles) {
            if(projectile.isActive()) {
                projectile.draw(batch);
                //System.out.println("Projectile drawn from ArrayList");
            }

        }
    }

    private void touchingTiles() {
        if (userData.equals("PlayerTwo")) {
            isNotTouchingTiles = contactListener.isP2NotTouchingTile();
        }

       else if (userData.equals("PlayerOne")) {
            isNotTouchingTiles = contactListener.isP1NotTouchingTile();

        }

    }


    private void setAnimationPosition() {
        playerSprite.setPosition(b2dbody.getPosition().x - playerSprite.getWidth() /2, b2dbody.getPosition().y - playerSprite.getHeight() / 4); //set the position of the animation to the center of the body


        playerSprite.setRegion(getRenderTexture(stateTime)); //set the texture to the current state of the movement
        playerSprite.setFlip(facingRight, false); // lets the player face the correct direction
    }

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
    
    private void setTouchingGround(boolean touchingGround) {
        this.touchingGround = touchingGround;
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

    //basically our constructor
    private void definePlayer() {
        bdef = new BodyDef();
        bdef.position.set(spawnpoint.x / PPM, spawnpoint.y / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(playerCollisionBoxRadius / PPM);

        //Implement the player textures and animations -> @Maurice @Alex


        fDef.shape = shape;

        if (collideWithOtherPlayers) {
            fDef.filter.groupIndex = 0;
        } else {
            fDef.filter.groupIndex = -1;
        }

        //fDef.density = 0.1f;
        //fDef.restitution = 0.4f;
        // fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);


    }

    private void checkHealth() {
        if (getHP() <= 0) {
            die();
        }
    }

    private void die() {
        if (!isDead) {
            setDead(true);
            soundManager.playSound(deathSoundMp3);
            //b2dbody.setLinearVelocity(0, 0);
            //b2dbody.setTransform(spawnpoint.x / PPM, spawnpoint.y / PPM, 0);
        }
    }

    public boolean isShooting() {
        return isShooting;
    }

    public float getPlayerCollisionBoxRadius() {
        return playerCollisionBoxRadius;
    }

    public void managePlayerInput(float dt) {



        Array<Controller> controllers2 = Controllers.getControllers();


        float upDownInput = 0;
        float leftRightInput = 0;
        boolean jumpInput = false;
        isBlocking = false;
        boolean stompInput = false;


        if (currentInputState == InputState.WASD) {

            Controller p1Controller = controllers2.get(0);

//            leftRightInput = Util.adAxis();
//            upDownInput = Util.wsAxis();
//            jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.W);
//            standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.V) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
//            isBlocking = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
//            stompInput = Gdx.input.isKeyJustPressed(Input.Keys.S);
//            isShooting = Gdx.input.isKeyJustPressed(Input.Keys.F);

            if(Math.abs(p1Controller.getAxis(Xbox360Pad.AXIS_LEFT_Y)) > Xbox360Pad.CONTROLLER_DEADZONE) {
                leftRightInput = p1Controller.getAxis(Xbox360Pad.AXIS_LEFT_Y);
            }
            upDownInput = p1Controller.getAxis(Xbox360Pad.AXIS_RIGHT_X);
            jumpInput = p1Controller.getButton(Xbox360Pad.BUTTON_A);
            standardAttackInput = p1Controller.getButton(Xbox360Pad.BUTTON_B);
            isBlocking = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
            stompInput = p1Controller.getButton(Xbox360Pad.BUTTON_Y);

            //TODO: Rate limiter needed here!! Otherwise, a lot of projectiles will spawn at once!
            isShooting = p1Controller.getButton(Xbox360Pad.BUTTON_X);

            //System.out.println(p1Controller.getAxis(Xbox360Pad.AXIS_LEFT_X));

        }
        if (currentInputState == InputState.ARROWS) {
            leftRightInput = Util.leftrightAxis();
            upDownInput = Util.updownAxis();
            jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.UP);
            standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT);
            isBlocking = Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
            stompInput = Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
            isShooting = Gdx.input.isKeyJustPressed(Input.Keys.P);
        }
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
        //System.out.println(facingRight);

        //jumping
        //System.out.println(jumpCount);
        //System.out.println(isExtraJumpReady);

        if (jumpCount <= maxExtraJumps && (jumpInput) && (isGrounded || isExtraJumpReady)) {

            jumpCount++;


            getB2dbody().applyLinearImpulse(new Vector2(0, getB2dbody().getLinearVelocity().y*(-0.8f) + getJumpForce()), getB2dbody().getWorldCenter(), true);


            //System.out.println("Jumping");
            isExtraJumpReady = true;
/*
            final Timer timer = new Timer();

            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                                isExtraJumpReady = true;

                }
            };
            timer.schedule(task, 100); // delay in milliseconds*/

        }

        //walking left and right
        if (getB2dbody().getLinearVelocity().x < getMaxVelocity()) {
            getB2dbody().applyLinearImpulse(new Vector2(0.05f * leftRightInput * getWalkingSpeedMultiplier(), 0.0f), getB2dbody().getWorldCenter(), true);

        }


        //damping
        if (isGrounded()) {
            getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x * damping, getB2dbody().getLinearVelocity().y));

        }

        //stomp

        if (stompInput && !isGrounded) {
            applyForces(0, stompSpeed);
            System.out.println("Stomping");
            isStomping = true;
            setHP(getHP() - 0.1f);
        }
    }

    public TextureRegion getRenderTexture(float stateTime) {
        TextureRegion frame = null;
        switch (this.currentState) {
            case STANDING:
                playerSprite.setRegion(playerStand);
                frame = stand.getKeyFrame(stateTime);
                break;

            case RUNNING:

                playerSprite.setRegion(playerRun);
                frame = run.getKeyFrame(stateTime);
                break;

            case JUMPING:
                playerSprite.setRegion(playerJump);
                frame = jump.getKeyFrame(stateTime);
                break;
        }
        //System.out.println(getCurrentState());
        //System.out.println(isGrounded());
        return frame;
/*
        Batch batch = getBatch();
        batch.begin();
        if(isFacingRight()) {
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        }
        else {
            batch.draw(frame, getX() + getWidth(), getY() + getHeight(), -getWidth(), getHeight());

        }
        */
        // batch.end();


    }

    public boolean reachedWorldEdge() {

        return b2dbody.getPosition().y < 0;
    }

    //Check if the player is touching the ground
    public void checkGrounded() {
        currentY = b2dbody.getPosition().y;
        if (b2dbody.getLinearVelocity().y - getGravity() <= 0.1 && b2dbody.getLinearVelocity().y - getGravity() >= -0.1 && !isNotTouchingTiles) {

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
            //System.out.println("grounded");

            jumpCount = 0;
            isExtraJumpReady = false;

            previousY = b2dbody.getLinearVelocity().y;

        } else {
            isGrounded = false;
        }
        previousY = b2dbody.getLinearVelocity().y;
       // System.out.println(isTouchingTiles);


    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    //respawn jumping
    public void respawnPlayers() {
        if (reachedWorldEdge()) {
            //getB2dbody().applyLinearImpulse(new Vector2(0, 2f), getB2dbody().getWorldCenter(), true);
            getB2dbody().setLinearVelocity(new Vector2(getB2dbody().getLinearVelocity().x * getRespawnDamping(), 2.6f));
            //System.out.println("Player respawn jump");

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
        forcesCombined = new Vector2(0 + x, gravity + y);
        getB2dbody().applyLinearImpulse(forcesCombined, getB2dbody().getWorldCenter(), true);
        forcesCombined.setZero();
    }

    public void limitPlayersToEdge() {
        //sets player velocity to 0 if they are at the edge of the map
        float pushBack = 1f;


        if (getB2dbody().getPosition().x > PlayScreen.getViewport().getWorldWidth()) {
            getB2dbody().setLinearVelocity(new Vector2(-pushBack, getB2dbody().getLinearVelocity().y + 0.1f));
            //lower gravity for some seconds :)
            setGravity(startingGravity * 0.3f);
            return;
        }
        if (getB2dbody().getPosition().x < 0) {
            getB2dbody().setLinearVelocity(new Vector2(pushBack, getB2dbody().getLinearVelocity().y + 0.1f));
            //lower gravity for some seconds :)
            setGravity(startingGravity * 0.3f);
        }
    }

    public enum InputState {
        WASD, ARROWS
    }

    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }

    public void shoot(HomingMissle bullet){
        projectiles.add(bullet);
    }

}
