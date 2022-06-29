package com.smashprofs.game.Exceptions;

/**
 * Exception which is thrown in gamePropertiesManager
 */
public class NegativeSeconds extends Exception{
        public NegativeSeconds(String errorMessage) {
            super(errorMessage);
        }
}
