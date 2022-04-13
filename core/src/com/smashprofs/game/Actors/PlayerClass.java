package com.smashprofs.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Helper.CombatManager;
import com.smashprofs.game.Helper.SoundManager;
import com.smashprofs.game.Helper.Util;

import com.smashprofs.game.Screens.PlayScreen;

public class PlayerClass extends Sprite {


    private Texture alexStand ;
    private Texture alexRun;
    private Texture alexJump;
    private Animation<TextureRegion> stand;
    private Animation<TextureRegion> run;
    private Animation<TextureRegion> jump;

    private String playerName;
    private Vector2 poistion;
    private float timeCount;

    private float worldTimer;
    private float stompSpeed = -5f;

    private String deathSoundMp3 = "death.mp3";

    private String punchSoundMp3 = "punch.mp3";

    private String stompSoundWav = "stomp.wav";
    private boolean isStomping;
    private boolean isDead = false;
    private String damageSoundMp3 = "damage.mp3";
    private boolean stompHitground;

    public boolean isStompHitground() {
        return stompHitground;
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

    private float attackReach = 0.2f;

    public float getAttackReach() {
        return attackReach;
    }

    public static final float PPM = 100;
    private final World world;
    public float damping = 0.9995f; //the closer this value is to zero the more the player will slow down
    InputState currentInputState;

    float stateTime = 0;

    public void updatePosition(float deltatime) {
        poistion = b2dbody.getPosition();
    }

    public void update(float deltatime) {
        applyForces(0, 0);
        checkHealth();
        setPosition(b2dbody.getPosition().x-getWidth()/2, b2dbody.getPosition().y - getHeight()/2);


        if(deltatime == 0 ) return;
        if(deltatime > 0.1f) deltatime = 0.1f;
        stateTime += deltatime;
        renderTexture(deltatime);

    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public enum InputState {
        WASD, ARROWS
    }

    float playerCollisionBoxRadius = 5;
    boolean isGrounded = true;
    private float respawnDamping = 0.1f;

    private float HP = 100;

    private boolean isBlocking = false;

    public boolean isBlocking() {
        return isBlocking;
    }

    private int attackDamage = 10;

    public float getHP() {
        return HP;
    }

    public void setHP(float HP) {
        this.HP = HP;

    }

    public int getAttackDamage() {
        return attackDamage;
    }

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
    private float maxVelocity = 1.6f;
    private boolean isExtraJumpReady;
    private float jumpForce = 2.5f;
    private float walkingSpeedMultiplier = 1.3f;
    private State currentState;

    private SoundManager soundManager;

    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }

    private Texture texture;
    private SpriteBatch batch;
    private Sprite sprite;

    public PlayerClass(World world, InputState inputState, Vector2 spawnpoint, String playerName, PlayScreen screen) {

        alexStand = new Texture("Sprites/herochar_idle_anim_strip_4.png");
        //heroTextureWalk = new Texture("Sprites/herochar_run_anim_strip_6.png");
        TextureRegion[] idle = TextureRegion.split(alexStand, 16, 16)[0];
        stand = new Animation(0.15f, idle[0], idle[1], idle[2], idle[3]);
        //TextureRegion[] walking = TextureRegion.split(heroTextureWalk, 16, 16)[0];
        this.currentState = State.STANDING;

        //super(screen.getAtlas().findRegion("Alex_strip"));
        //alexStand = new TextureRegion(screen.getAtlas().findRegion("Alex_strip"),10,17, 128, 128);
        setBounds(0,0,128/PPM, 128/PPM);
        setRegion(alexStand);
        this.world = world;
        this.currentInputState = inputState;
        this.spawnpoint = spawnpoint;
        this.playerName = playerName;
        //this.batch =

        definePlayer(inputState);

        soundManager = SoundManager.getSoundManager_INSTANCE();

    }




    private float gravity = -0.098f;

    private Vector2 forcesCombined = new Vector2(0, 0);

    private float startingGravity = gravity;

    public float getStartingGravity() {
        return startingGravity;
    }

    private boolean facingRight = true;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    private int isFacingRightAxe = 0;

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

    public void managePlayerInput(float dt) {


        int upDownInput = 0;
        int leftRightInput = 0;
        boolean jumpInput = false;
        isBlocking = false;
        boolean stompInput = false;



        if (currentInputState == InputState.WASD) {
            leftRightInput = Util.adAxis();
            upDownInput = Util.wsAxis();
            jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.W);
            standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.V) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
            isBlocking = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
            stompInput = Gdx.input.isKeyJustPressed(Input.Keys.S);

        }
        if (currentInputState == InputState.ARROWS) {
            leftRightInput = Util.leftrightAxis();
            upDownInput = Util.updownAxis();
            jumpInput = Gdx.input.isKeyJustPressed(Input.Keys.UP);
            standardAttackInput = Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_RIGHT);
            isBlocking = Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT);
            stompInput = Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
        }
        if(standardAttackInput){
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
            if (isExtraJumpReady) {
                getB2dbody().setLinearVelocity(getB2dbody().getLinearVelocity().x, 0.1f);
            }

            // 3=====>
            getB2dbody().applyLinearImpulse(new Vector2(0, getJumpForce()), getB2dbody().getWorldCenter(), true);

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

        if (stompInput  && !isGrounded) {
            applyForces(0, stompSpeed);
            System.out.println("Stomping");
            isStomping = true;
            setHP(getHP() - 0.1f);
        } else {

        }
    }

    public void renderTexture (float deltatime) {
        TextureRegion frame = null;
        switch (this.currentState) {
            case STANDING:
                frame = stand.getKeyFrame(stateTime);
                break;
            case RUNNING:
                frame = run.getKeyFrame(stateTime);
                break;
            case JUMPING:
                frame = jump.getKeyFrame(stateTime);
                break;
        }
//8=>
    }

    public boolean reachedWorldEdge() {

        if (b2dbody.getPosition().y < 0) {
            return true;
        }
        return false;
    }

    //Check if the player is touching the ground
    public void checkGrounded() {

        if (b2dbody.getLinearVelocity().y - getGravity() == 0) {
            if(isStomping()) {
                soundManager.playSound(stompSoundWav);
                setStompHitground(true);
                isStomping = false;
            }
            else {
                setStompHitground(false);
                isStomping = false;
            }
            isGrounded = true;
            setGravity(getStartingGravity());
            //System.out.println("grounded");

            jumpCount = 0;
            isExtraJumpReady = false;

            ;
            return;
        }
        isGrounded = false;

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


}
