package ee.taltech.examplegame.server.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.game.object.Player;
import message.PlayerShootingMessage;

import java.time.Duration;
import java.time.LocalTime;

import static constant.Constants.BULLET_TIMEOUT_IN_MILLIS;

public class PlayerShootingListener extends Listener {
    private final Player player;
    private LocalTime date = LocalTime.now();

    public PlayerShootingListener(Player player) {
        this.player = player;
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof PlayerShootingMessage playerShootingMessage) {
            Log.info("Player " + connection.getID() + " shot in direction: " + playerShootingMessage.getDirection());

            // Preventing player from shooting too often
            if (LocalTime.now().isAfter(date.plus(Duration.ofMillis(BULLET_TIMEOUT_IN_MILLIS)))) {
                date = LocalTime.now();
            } else {
                return;
            }

            player.shoot(playerShootingMessage.getDirection());
        }

        super.received(connection, object);
    }
}
