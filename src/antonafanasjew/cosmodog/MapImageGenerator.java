package antonafanasjew.cosmodog;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapReader;
import antonafanasjew.cosmodog.tiledmap.io.XmlTiledMapReader;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.util.TilesetUtils;

public class MapImageGenerator {

	public static void main(String[] args) throws SlickException, TiledMapIoException {
		
		TiledMapReader tiledMapReader = new XmlTiledMapReader(Constants.PATH_TO_TILED_MAP);
		CustomTiledMap tiledMap = tiledMapReader.readTiledMap();
		CosmodogMap map = InitializationUtils.initializeCosmodogMap(tiledMap);
		generateMapImage(map);
	}

	private static Image generateMapImage(CosmodogMap map) throws SlickException {
		Image mapImage = new Image(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
		Graphics mapCacheGraphics = mapImage.getGraphics();
		// This context starts with 0/0 as it relates to the map image graphics.
		DrawingContext mapDrawingContext = new SimpleDrawingContext(null, 0, 0, (float) mapImage.getWidth(), (float) mapImage.getHeight());

		for (int i = 0; i < Layers.LAYER_META_COLLISIONS; i++) {
			for (int tx = 0; tx < map.getWidth(); tx++) {
				for (int ty = 0; ty < map.getHeight(); ty++) {
					TileDrawingContext tileDc = new TileDrawingContext(mapDrawingContext, map.getWidth(), map.getHeight(), tx, ty);
					if (tx >= 0 && ty >= 0 && tx < map.getWidth() && ty < map.getHeight()) {
						render(map, tileDc, mapCacheGraphics, tx, ty, i);
					}
				}
			}

		}
		
		return mapImage;
	}
	
	private static void render(CosmodogMap map, DrawingContext dc, Graphics g, int tilePosX, int tilePosY, int layerIndex) {
		
		
		Animation animation = null;

		int tileId = map.getTileId(tilePosX, tilePosY, layerIndex);

		int imageIndex = tileId - 1;

		if (imageIndex >= 0) {

			int imagePosX = imageIndex % 9;
			int imagePosY = imageIndex / 9;

			Image tileImage = TilesetUtils.tileByIndex(imagePosX, imagePosY);
			g.drawImage(tileImage, dc.x(), dc.y(), dc.x() + dc.w(), dc.y() + dc.h(), 0, 0, tileImage.getWidth(), tileImage.getHeight());
		}

	}

}
