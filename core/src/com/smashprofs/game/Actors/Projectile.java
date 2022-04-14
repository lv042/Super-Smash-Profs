package com.smashprofs.game.Actors;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.smashprofs.game.Screens.PlayScreen;

import java.util.WeakHashMap;

import static com.smashprofs.game.Actors.PlayerClass.PPM;

public abstract class  Projectile {
    private BodyDef bdef;

    private Body body;
    private Body b2dbody;

    private PlayerClass originPlayer;

    private Vector2 projectileSpawnpoint;



    World world;

    public Projectile(World world, PlayerClass originPlayer) {
        this.world = world;
        this.originPlayer = originPlayer;
        create();
        moveProjectile();
    }

    private void moveProjectile() {
        b2dbody.setLinearVelocity(new Vector2(originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius(), 0));
    }

    public void create() {
        bdef = new BodyDef();
        projectileSpawnpoint = new Vector2(originPlayer.getPosition().x + originPlayer.getIsFacingRightAxe() * originPlayer.getPlayerCollisionBoxRadius() * 2.1f / PPM, originPlayer.getPosition().y);
        bdef.position.set(projectileSpawnpoint);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2dbody = world.createBody(bdef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape(); // circle shape is better for player characters so that it can be easily hit walls and other objects

        shape.setRadius(3 / PPM);

        //Implement the player textures and animations -> @Maurice @Alex


//        texture = new Texture("prettyplayer.png");
//        batch = new SpriteBatch();
//        sprite = new Sprite(texture);


        fDef.shape = shape;

        if (true) {
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

    public void beginContact(Contact contact) {
        System.out.println("Contact");
    }
}






