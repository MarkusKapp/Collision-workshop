package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;
import message.dto.BulletState;
import message.GameStateMessage;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.BULLET_HEIGHT_IN_PIXELS;
import static constant.Constants.BULLET_WIDTH_IN_PIXELS;
import static constant.Constants.PPM;

/**
 * Initialize a new Arena, which is responsible for updating and rendering the following: players, bullets, map.
 * Updating - modifying the inner state of objects (e.g. Player) based on game state messages received from the server.
 * Rendering - making the (updated) objects visible on the screen.
 */
public class Arena {

    private final List<Player> players = new ArrayList<>();
    private List<BulletState> bullets = new ArrayList<>();

    /**
     * Update players and bullets, so they are later rendered in the correct position.
     *
     * @param gameStateMessage latest game state received from the server
     */
    public void update(GameStateMessage gameStateMessage) {
        gameStateMessage.getPlayerStates().forEach(playerState -> {
            var player = players
                .stream()
                .filter(p -> p.getId() == playerState.getId())
                .findFirst()
                .orElseGet(() -> {
                    var newPlayer = new Player(playerState.getId());
                    players.add(newPlayer);
                    return newPlayer;
                });

            // Server sends pixels; client world is pixels/PPM when rendering with 1/PPM.
            player.setX(playerState.getX() / PPM);
            player.setY(playerState.getY() / PPM);
        });

        this.bullets = gameStateMessage.getBulletStates();
    }

    /**
     * Render map, players and bullets. This makes them visible on the screen.
     *
     * @param spriteBatch used for rendering (and scaling/resizing) all visual elements
     */
    public void render(SpriteBatch spriteBatch) {
        renderPlayers(spriteBatch);
        renderBullets(spriteBatch);
    }

    private void renderPlayers(SpriteBatch spriteBatch) {
        players.forEach(player -> player.render(spriteBatch));
    }

    private void renderBullets(SpriteBatch spriteBatch) {
        bullets.forEach(bullet -> spriteBatch.draw(
            Sprites.bulletTexture,
            bullet.getX() / PPM,
            bullet.getY() / PPM,
            BULLET_WIDTH_IN_PIXELS / PPM,
            BULLET_HEIGHT_IN_PIXELS / PPM
        ));
    }

}
