package ee.taltech.examplegame.server.game.object;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryonet.Connection;
import ee.taltech.examplegame.server.game.GameInstance;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.PlayerState;

import static constant.Constants.BULLET_HEIGHT_IN_PIXELS;
import static constant.Constants.BULLET_WIDTH_IN_PIXELS;
import static constant.Constants.JUMP_VELOCITY;
import static constant.Constants.PLAYER_LIVES_COUNT;
import static constant.Constants.PLAYER_SPEED;
import static constant.Constants.PPM;

/**
 * Server-side representation of a player in the game. This class listens for player movements or shooting actions
 * and changes the player's server-side state accordingly. Lives management.
 */
@Getter
@Setter
public class Player {
    private final Connection connection;
    // Keep track of listener objects for each player connection, so they can be disposed when the game ends
    private final PlayerMovementListener movementListener = new PlayerMovementListener(this);
    private final PlayerShootingListener shootingListener = new PlayerShootingListener(this);

    private final int id;
    private final GameInstance game;
    private float x = 600f;
    private float y = 600f;
    private int lives = PLAYER_LIVES_COUNT;

    private float velocityY = 0f;
    private final World world;
    private Body body;
    private long lastTimeShot;

    /**
     * Initializes a new server-side representation of a Player with a game reference and connection to client-side.
     *
     * @param connection Connection to client-side.
     * @param game Game instance that this player is a part of.
     */
    public Player(Connection connection, GameInstance game, World world) {
        this.connection = connection;
        this.id = connection.getID();
        this.game = game;
        this.world = world;
        this.connection.addListener(movementListener);
        this.connection.addListener(shootingListener);
        definePlayer();
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / PPM, y / PPM); // Starts at a visibile position and not in a collision box
        bdef.type = BodyDef.BodyType.DynamicBody; // Dynamic body not static
        bdef.fixedRotation = true; // don't tip / rotate when colliding

        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        // Keep player friction low to avoid grabbing onto vertical walls.
        fdef.friction = 0.0f;
        fdef.density = 1.0f;

        CircleShape shape = new CircleShape(); // Also circleShapes, PolygonShapes etc.
        // Adjust hit-box size as needed.
        shape.setRadius(30 / PPM);

        fdef.shape = shape;
        body.createFixture(fdef);
        shape.dispose();
    }

    /**
     * Moves the player in the specified direction within the arena bounds.
     *
     * @param direction The direction in which the player moves.
     */
    public void move(Direction direction) {
        if (direction == null || body == null) return;

        // Direction comes from the client.
        // Horizontal movement
        if (direction == Direction.LEFT) {
            body.setLinearVelocity(-PLAYER_SPEED, body.getLinearVelocity().y);
        } else if (direction == Direction.RIGHT) {
            body.setLinearVelocity(PLAYER_SPEED, body.getLinearVelocity().y);
        } else if (direction == Direction.DOWN) {
            // Treat DOWN as "stop" for horizontal movement
            body.setLinearVelocity(0f, body.getLinearVelocity().y);
        }

        // Jump (separate from horizontal movement)
        if (direction == Direction.UP) {
            body.setLinearVelocity(body.getLinearVelocity().x, JUMP_VELOCITY);
        }
    }

    public void updateFromPhysics() {
        if (body == null) return;
        this.x = body.getPosition().x * PPM;
        this.y = body.getPosition().y;
    }

    /**
     * Returns the current state of the player, consisting of their position and remaining lives.
     */
    public PlayerState getState() {
        PlayerState playerState = new PlayerState();
        playerState.setId(connection.getID());

        playerState.setX(body.getPosition().x * PPM);
        playerState.setY(body.getPosition().y * PPM);

        playerState.setLives(lives);
        return playerState;
    }


    public void shoot(Direction direction) {
        // x/y are synced from Box2D body position and represent the player's center in pixels.
        // Spawn bullet centered on the player.
        float spawnX = x - BULLET_WIDTH_IN_PIXELS / 2f;
        float spawnY = y - BULLET_HEIGHT_IN_PIXELS / 2f;
        game.addBullet(new Bullet(spawnX, spawnY, direction, id));
    }

    public void decreaseLives() {
        if (lives > 0) {
            setLives(getLives() - 1);
        }
    }

    /**
     * Removes the movement and shooting listeners from the player's connection.
     * This should be called when the player disconnects or the game ends.
     * Disposing of the listeners prevents potential thread exceptions when reusing
     * same connections for future game instances.
     */
    public void dispose() {
        connection.removeListener(movementListener);
        connection.removeListener(shootingListener);
    }

}
