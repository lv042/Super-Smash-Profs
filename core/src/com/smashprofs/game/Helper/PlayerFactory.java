package com.smashprofs.game.Helper;

import com.badlogic.gdx.math.Vector2;
import com.smashprofs.game.Actors.Players.*;
import com.smashprofs.game.Screens.PlayScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.smashprofs.game.Actors.Players.Player.PPM;

public class PlayerFactory {

    private static Logger log = LogManager.getLogger(PlayerFactory.class);


    private Vector2 playerOneSpawnPoint = new Vector2(4.5f * PPM, 0.85f * PPM); //new Vector2(3.4f * PPM, 0.85f * PPM);

    private Vector2 playerTwoSpawnPoint = new Vector2( 7.25f * PPM,0.5f * PPM); //new Vector2( 5.522669f * PPM,0.53562f * PPM);

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
        log.debug("PlayerFactory has been reset");
    }

    public Player getPlayer  (PlayerTypes player){

        if(playersCreated == 0){
            currentSpawnPoint = playerOneSpawnPoint;
            currentInputState = Player.InputState.WASD;
            userData = "PlayerOne";
            log.debug("Creating PlayerOne...");
        }
        else if(playersCreated == 1){
            currentSpawnPoint = playerTwoSpawnPoint;
            currentInputState = Player.InputState.ARROWS;
            userData = "PlayerTwo";
            log.debug("Creating PlayerTwo...");
        }


        switch (player){
            case Luca:
                playersCreated++;
                //System.out.println("Created LucaPlayer");
                log.debug("Created LucaPlayer");
                return new Luca(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Luca Kanne", userData);
            case Alex:
                playersCreated++;
                //System.out.println("Created AlexPlayer");
                log.debug("Created AlexPlayer");
                return new Alex(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Alex Boss", userData);
            case Maurice:
                playersCreated++;
                //System.out.println("Created MauricePlayer");
                log.debug("Created MauricePlayer");
                return new Maurice(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Maurice Boi", userData);
            case Leo:
                playersCreated++;
                //System.out.println("Created MauricePlayer");
                log.debug("Created LeoPlayer");
                return new Leo(PlayScreen.getWorld(), currentInputState, currentSpawnPoint, "Leo The Miner", userData);
//            case Jens:
//                return new Jens(5);
//            case Martin:
//                return new Martin(6);
            default:
                log.warn("Created NO player!");
                return null;
        }
    }
}
