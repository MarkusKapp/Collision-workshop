package constant;

public class Constants {

    // --- PPM based on your tiledmap design
    // Box2D works best when 1 meter ~= 50-200 pixels. We'll use 100px = 1m.
    public static final float PPM = 50f;
    public static final float GRAVITY = -1.62f;
    // --- networking constants ---
    public static final int PORT_TCP = 54555;
    public static final int PORT_UDP = 54777;
    // this should be changed depending on where the server is hosted
    public static final String SERVER_IP = "localhost";

    // --- player constants ---
    public static final float PLAYER_SPEED = 2.0f;
    public static final float JUMP_VELOCITY = 40f;
    public static final int PLAYER_LIVES_COUNT = 10;
    public static final float PLAYER_HEIGHT_IN_PIXELS = 32;
    public static final float PLAYER_WIDTH_IN_PIXELS = 16;

    // --- bullet constants ---
    public static final float BULLET_SPEED = 5.0f;
    public static final long BULLET_TIMEOUT_IN_MILLIS = 200;
    public static final float BULLET_WIDTH_IN_PIXELS = 8;
    public static final float BULLET_HEIGHT_IN_PIXELS = 8;

    // --- game constants ---
    public static final int GAME_TICK_RATE = 60;
    public static final int PLAYER_COUNT_IN_GAME = 2;
    // current values enforce the bounds at the edges of a default-size LibGDX window (defined in Lwjgl3Launcher)
    // changing these constants won't modify the size of the GameScreen
    public static final int ARENA_LOWER_BOUND_X = 0;
    public static final int ARENA_UPPER_BOUND_X = 640;
    public static final int ARENA_LOWER_BOUND_Y = 0;
    public static final int ARENA_UPPER_BOUND_Y = 480;

}
