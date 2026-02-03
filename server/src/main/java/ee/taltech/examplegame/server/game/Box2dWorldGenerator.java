package ee.taltech.examplegame.server.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import ee.taltech.examplegame.server.TMXLoaders.HijackedTmxLoader;
import ee.taltech.examplegame.server.TMXLoaders.MyServer;

import static constant.Constants.PPM;

public class Box2dWorldGenerator {

    public void initializeWorld(World world) {
        TiledMap tiledMap = getMap();
        MapLayer mapLayer = tiledMap.getLayers().get("Kollisionid"); // Get the collision layer from the Tiled map // assuming the layer is named "Collisions"

        BodyDef bdef = new BodyDef(); // Definition for creating a physics body
        PolygonShape shape = new PolygonShape(); // Shape used for collision detection
        FixtureDef fdef = new FixtureDef(); // Fixture configuration for the body
        Body body; // Reference to the created body

        for (PolygonMapObject object : mapLayer.getObjects().getByType(PolygonMapObject.class)) {
            Rectangle rectangle = object.getPolygon().getBoundingRectangle(); // for loop to get only rectangular objects in the collision map. There are different types of objects in Tiled maps.

            bdef.type = BodyDef.BodyType.StaticBody; // Make the body static (doesn't move)
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / PPM,
                    (rectangle.getY() + rectangle.getHeight() / 2) / PPM);  // Set body's center position in meters

            body = world.createBody(bdef);

            shape.setAsBox(rectangle.getWidth() / 2 / PPM, rectangle.getHeight() / 2 / PPM); // Set shape size as half-width/height in meters
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

    public TiledMap getMap() {
        return new HijackedTmxLoader(new MyServer.MyFileHandleResolver())
                .load("shared/src/main/java/TMXAssets/skibidi.tmx");
    }
}
