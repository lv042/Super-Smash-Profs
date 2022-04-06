package helper;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.smashprofs.game.GameScreen;

public class TileMapHelper {
        public TiledMap tiledMap;
        public OrthogonalTiledMapRenderer renderer;


        public TileMapHelper() {

        }

        /**
         * @return
         */
        public void setupMap() {

            float unitScale = 1 / 8f;
            tiledMap = new TmxMapLoader().load("main.tmx");
            this.renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
            parseMap();
        }

        //iterate through tiles
        private void parseMap() {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");
            for (int x = 0; x < layer.getWidth(); x++) {
                for (int y = 0; y < layer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell == null)
                        continue;

                    MapObjects cellObjects = cell.getTile().getObjects();
                    if (cellObjects.getCount() != 1)
                        continue;

                    MapObject mapObject = cellObjects.get(0);

                    // Code von Luca
                    final int tileSize = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getTileWidth();
                    //

                    if (mapObject instanceof RectangleMapObject) {
                        RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                        Rectangle rectangle = rectangleObject.getRectangle();

                        BodyDef bodyDef = getBodyDef(x * tileSize + tileSize / 2f + rectangle.getX() - (tileSize - rectangle.getWidth()) / 2f, y * tileSize + tileSize / 2f + rectangle.getY() - (tileSize - rectangle.getHeight()) / 2f);

                        Body body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
                        PolygonShape polygonShape = new PolygonShape();
                        polygonShape.setAsBox(rectangle.getWidth() / 2f, rectangle.getHeight() / 2f);
                        body.createFixture(polygonShape, 0.0f);
                        polygonShape.dispose();
                    } else if (mapObject instanceof EllipseMapObject) {
                        EllipseMapObject circleMapObject = (EllipseMapObject) mapObject;
                        Ellipse ellipse = circleMapObject.getEllipse();

                        BodyDef bodyDef = getBodyDef(x * tileSize + tileSize / 2f + ellipse.x, y * tileSize + tileSize / 2f + ellipse.y);

                        if (ellipse.width != ellipse.height)
                            throw new IllegalArgumentException("Only circles are allowed.");

                        Body body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
                        CircleShape circleShape = new CircleShape();
                        circleShape.setRadius(ellipse.width / 2f);
                        body.createFixture(circleShape, 0.0f);
                        circleShape.dispose();
                    } else if (mapObject instanceof PolygonMapObject) {
                        PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                        Polygon polygon = polygonMapObject.getPolygon();

                        BodyDef bodyDef = getBodyDef(x * tileSize + polygon.getX(), y * tileSize + polygon.getY());

                        Body body = GameScreen.INSTANCE.getWorld().createBody(bodyDef);
                        PolygonShape polygonShape = new PolygonShape();
                        polygonShape.set(polygon.getVertices());
                        body.createFixture(polygonShape, 0.0f);
                        polygonShape.dispose();
                    }
                }
            }
        }

        private BodyDef getBodyDef(float x, float y) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(x, y);

            return bodyDef;
        }
    }

