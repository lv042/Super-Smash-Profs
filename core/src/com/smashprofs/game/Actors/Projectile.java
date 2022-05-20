package com.smashprofs.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Helper.CameraManager;

import static com.smashprofs.game.Actors.Player.PPM;

public class  Projectile extends Sprite{
    private BodyDef bdef;

    public Body b2dbody;

    private CameraManager cameraManager = CameraManager.getCameraManager_INSTANCE();

    private Player originPlayer;

    private Vector2 projectileSpawnpoint;
    private double rotation = 0;

    String userData = "JOeMama";

    OrthographicCamera camera;

    public Boolean active = true;
    World world;

    public Projectile(World world, Player originPlayer, String userData, OrthographicCamera camera) {
        super(new Texture(Gdx.files.internal("fireball.png")));

        this.userData = userData;
        this.world = world;
        this.originPlayer = originPlayer;
        //this.camera = camera;

        //setTexture(new Texture("prettyplayer.png"));
        //setTexture(new Texture(Gdx.files.internal("prettyplayer.png")));


        //setPosition(12, 12);

        setBounds(0, 0, getWidth()/PPM, getHeight()/PPM);
        //scale(1/PPM);

        create();

        moveProjectile();

    }

    void moveProjectile() {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * 1.2f, 0));
    }

    public void create() {

        bdef = new BodyDef();
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius() * 2.1f / PPM, originPlayer.getPosition().y);
        bdef.position.set(projectileSpawnpoint);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = false;
        b2dbody = world.createBody(bdef);
        b2dbody.setUserData(userData);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(3 / PPM);

        //Implement the player textures and animations -> @Maurice @Alex





        fDef.shape = shape;

        //filters collisions with other objects
        if (true) {
            fDef.filter.groupIndex = 0;
        }

        //fDef.density = 0.1f;
        //fDef.restitution = 0.4f;
        // fDef.friction = 0.5f;
        //@Maurice @Alex Ihr könnte gerne mal mit diesen Werten rumspielen und schauen was am besten passt :D

        b2dbody.createFixture(fDef);
        // !!!!!!!!!!!!!!!!!!!!!!
        //setOrigin(0, 0);
        setOrigin(getWidth()*0.5f, getHeight()*0.5f);

    }

    public void update(float delta) {
        System.out.println("!!!! projectile update");
        //this.setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);

        //setBounds(b2dbody.getPosition().x - getWidth() / 2/PPM, b2dbody.getPosition().y - getHeight() / 2 /PPM, getTexture().getWidth()/PPM, getTexture().getHeight()/PPM );


        setPosition(b2dbody.getPosition().x - getWidth() / 2f, b2dbody.getPosition().y - getHeight() / 2f);


        //setRotation(rotation);
        // Toggle Projectile activity state based on its body's active state
        if(b2dbody.isActive()){
            active = true;
        }
        else if (!b2dbody.isActive()){
            active = false;
        }

        //rotation += 0.8f;
        //setPosition(b2dbody.getPosition().x - getWidth() / 2/PPM, b2dbody.getPosition().y - getHeight() / 2 /PPM);
        rotation = b2dbody.getAngle()*2*Math.PI*4;
        setRotation((float) rotation);


    }

    @Override
    public void draw(Batch batch) {


        System.out.println("projectile draw");
        super.draw(batch);
    }


    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        ShapeRenderer debugRenderer = new ShapeRenderer();
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public Boolean isActive() {
        return active;
    }

    public Sprite getSprite() {
        return this;
    }

    public Body getBody() {
        return b2dbody;
    }
    public void destroy() {
        world.destroyBody(b2dbody);
    }

}






