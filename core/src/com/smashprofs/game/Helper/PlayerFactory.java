package com.smashprofs.game.Helper;

import com.smashprofs.game.Actors.Alex;
import com.smashprofs.game.Actors.Luca;
import com.smashprofs.game.Actors.Player;

public class PlayerFactory {

    static final PlayerFactory PlayerFactory_INSTANCE = new PlayerFactory();

    public static PlayerFactory getPlayerFactory_INSTANCE() {
        return PlayerFactory_INSTANCE;
    }

    private int playersCreated = 0;


    private PlayerFactory() {

    }

    public Player getPlayer  (PlayerTypes player){

        switch (player){
            case Luca:

                playersCreated++;
                //return new Luca();
            case Alex:
                playersCreated++;
                //return new Alex();
//            case Leo:
//                return new Leo(3);
//            case Maurice:
//                return new Maurice(4);
//            case Jens:
//                return new Jens(5);
//            case Martin:
//                return new Martin(6);
            default:
                return null;
        }
    }
}
