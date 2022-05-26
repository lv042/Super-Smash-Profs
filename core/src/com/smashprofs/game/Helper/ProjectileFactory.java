package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.*;
import com.smashprofs.game.Screens.PlayScreen;

import static com.smashprofs.game.Actors.Player.PPM;
import static com.smashprofs.game.Screens.PlayScreen.world;

public class ProjectileFactory {

    private String userData = null;

    private Player targetPlayer;
    static final ProjectileFactory ProjectileFactory_INSTANCE = new ProjectileFactory();

    public static ProjectileFactory getProjectileFactory_INSTANCE() {
        return ProjectileFactory_INSTANCE;
    }

    private int projectilesCreated = 0;


    private ProjectileFactory() {

    }

    public void resetFactory() {
        projectilesCreated = 0;
    }

    public Projectile getProjectile(ProjectileTypes projectile, Player originPlayer, Player targetPlayer) {

        switch(projectile) {
            case HomingMissile:
                projectilesCreated++;
                System.out.println("Created HomingMissile");
                return new HomingMissle(PlayScreen.getWorld(), originPlayer, targetPlayer);
            case Fireball:
                projectilesCreated++;
                System.out.println("Created Fireball");
                return new Bullet(PlayScreen.getWorld(), originPlayer);
            case Landmine:
                projectilesCreated++;
                System.out.println("Created Landmine");
                return new Landmine(PlayScreen.getWorld(), originPlayer);
        }

        return null;
    }

}
