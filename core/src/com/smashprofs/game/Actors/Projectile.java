package com.smashprofs.game.Actors;

public class Projectile {
    private float projectileSpeed;
    private float projectileDamage;
    private float projectileRange;
    private float projectileCooldown; // ???

    public Projectile(float projectileSpeed, float projectileDamage, float projectileRange, float projectileCooldown) {
        this.projectileSpeed = projectileSpeed;
        this.projectileDamage = projectileDamage;
        this.projectileRange = projectileRange;
        this.projectileCooldown = projectileCooldown;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileDamage() {
        return projectileDamage;
    }

    public float getProjectileRange() {
        return projectileRange;
    }

    public float getProjectileCooldown() {
        return projectileCooldown;
    }

}
