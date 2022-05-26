package com.smashprofs.game.Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.smashprofs.game.Actors.Alex;
import com.smashprofs.game.Actors.Luca;
import com.smashprofs.game.Actors.Maurice;
import com.smashprofs.game.Actors.Player;
import com.smashprofs.game.Screens.PlayScreen;

import static com.smashprofs.game.Actors.Player.PPM;
import static com.smashprofs.game.Screens.PlayScreen.world;

public class PlayerFactory {

    private Vector2 playerOneSpawnPoint = new Vector2(3.4f * PPM, 0.85f * PPM);

    private Vector2 playerTwoSpawnPoint = new Vector2( 5.522669f * PPM,0.53562f * PPM);

    private Vector2 currentSpawnPoint = null;

    private Player.InputState currentInputState = null;

    private String userData = null;

    static final PlayerFactory PlayerFactory_INSTANCE = new PlayerFactory();

    public static PlayerFactory getPlayerFactory_INSTANCE() {
        return PlayerFactory_INSTANCE;
    }

    private int playersCreated = 0;


    private PlayerFactory() {

    }

    public void resetFactory() {
        playersCreated = 0;
    }

    public Player getPlayer  (PlayerTypes player){

        if(playersCreated == 0){
            currentSpawnPoint = playerOneSpawnPoint;
            currentInputState = Player.InputState.WASD;
            userData = "PlayerOne";

        }
        else if(playersCreated == 1){
            currentSpawnPoint = playerTwoSpawnPoint;
            currentInputState = Player.InputState.ARROWS;
            userData = "PlayerTwo";
        }


        switch (player){
            case Luca:
                playersCreated++;
                System.out.println("Created LucaPlayer");
                return new Luca(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Luca Kanne", userData);

            case Alex:
                playersCreated++;
                System.out.println("Created AlexPlayer");
                return new Alex(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Alex Boss", userData);
//            case Leo:
//                return new Leo(3);
            case Maurice:
                playersCreated++;
                System.out.println("Created MauricePlayer");
                return new Maurice(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Maurice Boss", userData);
//            case Jens:
//                return new Jens(5);
//            case Martin:
//                return new Martin(6);
            default:
                return null;
        }
    }
}
