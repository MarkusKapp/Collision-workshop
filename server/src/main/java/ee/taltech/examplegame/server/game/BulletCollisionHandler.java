package ee.taltech.examplegame.server.game;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.game.object.Bullet;
import ee.taltech.examplegame.server.game.object.Player;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.ARENA_LOWER_BOUND_X;
import static constant.Constants.ARENA_LOWER_BOUND_Y;
import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

/**
 * Handles bullet collisions with players, arena boundaries, and map collisions.
 */
public class BulletCollisionHandler {

    /**
     * Checks for collisions between bullets and players, removes bullets that hit players or moved out of bounds.
     *
     * @param bullets The list of bullets in the game.
     * @param players The list of players to check for collisions.
     * @return A list of remaining bullets after handling collisions.
     */
    public List<Bullet> handleCollisions(List<Bullet> bullets, List<Player> players) {
        List<Bullet> bulletsToBeRemoved = new ArrayList<>();

        for (Player player : players) {
            Rectangle hitBox = constructPlayerHitBox(player);
            for (Bullet bullet : bullets) {
                // register a hit only if the bullet was shot by a different player
                if (bullet.getShotById() != player.getId() && hitBox.contains(bullet.getX(), bullet.getY())) {
                    player.decreaseLives();
                    bulletsToBeRemoved.add(bullet);
                    Log.info("Player with id " + player.getId() + " was hit. " + player.getLives() + " lives left.");
                }
            }
        }
        bulletsToBeRemoved.addAll(findOutOfBoundsBullets(bullets));
        bullets.removeAll(bulletsToBeRemoved);  // remove bullets that hit a player or move out of bounds
        return bullets;
    }

    /**
     * Finds bullets that are out of the arena bounds.
     *
     * @param bullets All active bullets.
     * @return Bullets that are out of bounds.
     */
    private List<Bullet> findOutOfBoundsBullets(List<Bullet> bullets) {
        List<Bullet> outOfBoundsBullets = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.getX() < ARENA_LOWER_BOUND_X ||
                bullet.getX() > ARENA_UPPER_BOUND_X ||
                bullet.getY() < ARENA_LOWER_BOUND_Y ||
                bullet.getY() > ARENA_UPPER_BOUND_Y
            ) {
                outOfBoundsBullets.add(bullet);
            }
        }
        return outOfBoundsBullets;
    }

    /**
     * Constructs a rectangular hitbox for a player based on their position.
     * A hitbox is essential for detecting collisions between players and bullets.
     * Only bullets that visually overlap with the player's sprite register as hits.
     */
    private Rectangle constructPlayerHitBox(Player player) {
        return
            new Rectangle(
                (int) (player.getX() - PLAYER_WIDTH_IN_PIXELS / 2f),
                (int) (player.getY() - PLAYER_HEIGHT_IN_PIXELS / 2f),
                (int) PLAYER_WIDTH_IN_PIXELS,  // rectangle width
                (int) PLAYER_HEIGHT_IN_PIXELS  // rectangle height
            );
    }
}
