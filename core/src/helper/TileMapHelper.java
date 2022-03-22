package helper;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapHelper {
    private TiledMap tiledMap;


    public TileMapHelper() {

    }

    /**
     *
     * @return
     */
    public OrthogonalTiledMapRenderer setupMap(){

        float unitScale = 1 / 16f;
        tiledMap = new TmxMapLoader().load("Simple.tmx");
        return new OrthogonalTiledMapRenderer(tiledMap, unitScale);
    }
}
