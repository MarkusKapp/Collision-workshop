package ee.taltech.examplegame.server.game;

import ee.taltech.examplegame.server.game.object.Bullet;
import ee.taltech.examplegame.server.game.object.Player;
import lombok.Getter;
import lombok.Setter;
import message.GameStateMessage;
import message.dto.BulletState;
import message.dto.PlayerState;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.GAME_TICK_RATE;

@Getter
@Setter
public class GameStateHandler {

    private boolean allPlayersHaveJoined = false;
    private float gameTime = 0;

    public void incrementGameTimeIfPlayersPresent() {
        if (allPlayersHaveJoined) {
            gameTime += 1f / GAME_TICK_RATE;
        }
    }

    public GameStateMessage getGameStateMessage(List<Player> players, List<Bullet> bullets) {
        // get the state of all players
        var playerStates = new ArrayList<PlayerState>();
        players.forEach(player -> playerStates.add(player.getState()));

        // get state of all bullets
        var bulletStates = new ArrayList<BulletState>();
        bullets.forEach(bullet -> bulletStates.add(bullet.getState()));

        // construct gameStateMessage
        var gameStateMessage = new GameStateMessage();
        gameStateMessage.setPlayerStates(playerStates);
        gameStateMessage.setBulletStates(bulletStates);
        gameStateMessage.setGameTime(Math.round(gameTime));
        gameStateMessage.setAllPlayersHaveJoined(allPlayersHaveJoined);

        return gameStateMessage;
    }


}
