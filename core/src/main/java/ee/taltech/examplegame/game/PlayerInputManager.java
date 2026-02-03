package ee.taltech.examplegame.game;

import com.badlogic.gdx.Gdx;
import ee.taltech.examplegame.network.ServerConnection;
import message.dto.Direction;
import message.PlayerMovementMessage;
import message.PlayerShootingMessage;

/**
 * Listens for user input in the GameScreen regarding player movement and shooting,
 * forwards the corresponding messages to the server.
 */
public class PlayerInputManager {

    public void handleMovementInput() {
        var movementMessage = new PlayerMovementMessage();

        // detect key presses and send a movement message with the desired direction to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            movementMessage.setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            movementMessage.setDirection(Direction.RIGHT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            movementMessage.setDirection(Direction.UP);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            movementMessage.setDirection(Direction.DOWN);
        }

        // don't send anything if player is not moving
        if (movementMessage.getDirection() == null) return;


        // message is sent to the server
        ServerConnection
                .getInstance()
                .getClient()
                .sendUDP(movementMessage);  // UDP, because nothing bad happens when some messages don't reach the server
    }


    public void handleShootingInput() {
        var shootingMessage = new PlayerShootingMessage();

        // detect key presses and send shooting message to the server
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            shootingMessage.setDirection(Direction.LEFT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            shootingMessage.setDirection(Direction.RIGHT);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
            shootingMessage.setDirection(Direction.UP);
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            shootingMessage.setDirection(Direction.DOWN);
        }

        // don't send anything if player is not shooting
        if (shootingMessage.getDirection() == null) return;

        // message is sent to the server
        ServerConnection
            .getInstance()
            .getClient()
            .sendUDP(shootingMessage);
    }
}
