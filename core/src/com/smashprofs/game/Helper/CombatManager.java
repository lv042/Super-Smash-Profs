package com.smashprofs.game.Helper;

public class CombatManager {


    private static final CombatManager combatManager_INSTANCE = new CombatManager();

    //private constructor to avoid client applications to use constructor
    private CombatManager() {
    }

    private static CombatManager getCombatManager_INSTANCE() {
        return combatManager_INSTANCE;
    }
}
