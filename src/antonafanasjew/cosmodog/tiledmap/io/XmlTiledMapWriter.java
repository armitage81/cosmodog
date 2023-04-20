package antonafanasjew.cosmodog.tiledmap.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.badlogic.gdx.utils.Base64Coder;
import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.tiledmap.TileImage;
import antonafanasjew.cosmodog.tiledmap.TiledEllipseObject;
import antonafanasjew.cosmodog.tiledmap.TiledFigureObject;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolygonObject;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.tiledmap.TiledTile;
import antonafanasjew.cosmodog.tiledmap.TiledTileObject;
import antonafanasjew.cosmodog.tiledmap.Tileset;

public class XmlTiledMapWriter implements TiledMapWriter {

	private String fileName;

	public XmlTiledMapWriter(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void writeTiledMap(CustomTiledMap map) throws TiledMapIoException {
		XMLOutputter outputter = new XMLOutputter();
		Format format = org.jdom2.output.Format.getPrettyFormat();
		format.setEncoding("ISO-8859-1");
		format.setIndent(" ");
		outputter.setFormat(format);

		try {
			Document document = new Document(toMapElement(map));
			outputter.output(document, new FileOutputStream(new File(fileName)));
		} catch (IOException e) {
			throw new TiledMapIoException(e.getMessage(), e);
		}
	}

	private Element toMapElement(CustomTiledMap map) throws IOException {

		Element root = new Element("map");

		root.setAttribute("version", map.getVersion());
		root.setAttribute("orientation", map.getOrientation());
		root.setAttribute("renderorder", map.getRenderorder());
		root.setAttribute("width", String.valueOf(map.getWidth()));
		root.setAttribute("height", String.valueOf(map.getHeight()));
		root.setAttribute("tilewidth", String.valueOf(map.getTileWidth()));
		root.setAttribute("tileheight", String.valueOf(map.getTileHeight()));
		root.setAttribute("nextobjectid", String.valueOf(map.getNextObjectId()));

		Element tileset = toTilesetElement(map.getTileset());
		root.addContent(tileset);
		List<Element> layers = toMapLayerList(map.getMapLayers());
		root.addContent(layers);
		List<Element> objectGroups = toObjectGroupList(map.getObjectGroups());
		root.addContent(objectGroups);

		return root;

	}

	private List<Element> toMapLayerList(List<TiledMapLayer> layers) throws IOException {
		List<Element> layerElements = Lists.newArrayList();
		for (TiledMapLayer layer : layers) {
			layerElements.add(toMapLayerElement(layer));
		}
		return layerElements;
	}

	private Element toMapLayerElement(TiledMapLayer layer) throws IOException {

		Element layerElement = new Element("layer");

		layerElement.setAttribute("name", layer.getName());
		layerElement.setAttribute("width", String.valueOf(layer.getWidth()));
		layerElement.setAttribute("height", String.valueOf(layer.getHeight()));
		layerElement.setAttribute("opacity", String.valueOf(layer.getOpacity()));

		Element dataElement = toDataElement(layer.getData(), layer.getWidth(), layer.getHeight());

		layerElement.addContent(dataElement);

		return layerElement;
	}

	private Element toDataElement(List<TiledTile> tiles, int mapWidth, int mapHeight) throws IOException {
		
		Element dataElement = new Element("data");

		try (
				ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream(); 
				GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressedOutputStream); 
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(gzipOutputStream)
		) {
			byte[] gidData = new byte[tiles.size() * 4];
			int index = 0;

			for (TiledTile tile : tiles) {

				int gid;

				if (tile == null) {
					gid = 0;
				} else {
					gid = tile.getGid();
				}

				gidData[index++] = intToUnsignedByte(gid & 0xff);
				gidData[index++] = intToUnsignedByte((gid & 0xff00) >> 8);
				gidData[index++] = intToUnsignedByte((gid & 0xff0000) >> 16);
				gidData[index++] = intToUnsignedByte((gid & 0xff000000) >> 24);

			}
			bufferedOutputStream.write(gidData);
			gzipOutputStream.finish();
			byte[] compressedData = compressedOutputStream.toByteArray();

			String encodedData = new String(Base64Coder.encode(compressedData));

			dataElement.setAttribute("encoding", "base64");
			dataElement.setAttribute("compression", "gzip");
			dataElement.addContent(encodedData);
		} 
		
		return dataElement;

	}

	private byte intToUnsignedByte(int i) {
		return (byte) Byte.toUnsignedInt((byte) i);
	}

	private List<Element> toObjectGroupList(Map<String, TiledObjectGroup> objectGroups) {
		List<Element> objectGroupElements = Lists.newArrayList();
		for (String objectGroupName : objectGroups.keySet()) {
			TiledObjectGroup objectGroup = objectGroups.get(objectGroupName);
			objectGroupElements.add(toObjectGroupElement(objectGroup));
		}
		return objectGroupElements;
	}

	private Element toObjectGroupElement(TiledObjectGroup objectGroup) {

		Element objectGroupElement = new Element("objectgroup");
		objectGroupElement.setAttribute("name", objectGroup.getName());

		for (String objectName : objectGroup.getObjects().keySet()) {
			TiledObject object = objectGroup.getObjects().get(objectName);
			Element objectElement = toObjectElement(object);
			objectGroupElement.addContent(objectElement);

		}

		return objectGroupElement;
	}

	private Element toObjectElement(TiledObject object) {

		Element objectElement = new Element("object");

		boolean isEllipse = object instanceof TiledEllipseObject;
		boolean isTile = object instanceof TiledTileObject;
		boolean isPolyline = object instanceof TiledPolylineObject;
		boolean isPolygon = object instanceof TiledPolygonObject;
		boolean isRect = !isEllipse && !isPolygon && !isPolyline && !isTile;

		boolean isFigureBased = isRect || isEllipse;
		boolean isLineBased = isPolyline || isPolygon;

		objectElement.setAttribute("id", String.valueOf(object.getId()));
		if (object.getName() != null) {
			objectElement.setAttribute("name", object.getName());
		}
		if (object.getType() != null) {
			objectElement.setAttribute("type", object.getType());
		}
		objectElement.setAttribute("x", String.valueOf(object.getX()));
		objectElement.setAttribute("y", String.valueOf(object.getY()));

		Map<String, String> properties = object.getProperties();
		if (properties != null) {
			Element propertiesElement = new Element("properties");
			for (String propertyName : properties.keySet()) {
				String propertyValue = properties.get(propertyName);
				Element propertyElement = new Element("property");
				propertyElement.setAttribute("name", propertyName);
				propertyElement.setAttribute("value", propertyValue);

				propertiesElement.addContent(propertyElement);
			}

			objectElement.addContent(propertiesElement);
		}

		if (isFigureBased) {

			TiledFigureObject tiledFigureObject = (TiledFigureObject) object;

			objectElement.setAttribute("width", String.valueOf(tiledFigureObject.getWidth()));
			objectElement.setAttribute("height", String.valueOf(tiledFigureObject.getHeight()));

		}

		if (isLineBased) {

			TiledLineObject tiledLineObject = (TiledLineObject) object;

			Element lineElement;

			if (isPolygon) {
				lineElement = new Element("polygon");
			} else {
				lineElement = new Element("polyline");
			}

			List<Point> points = tiledLineObject.getPoints();

			String pointsAttributeValue = points.stream().map(p -> (p.x - object.getX()) + "," + (p.y - object.getY())).collect(Collectors.joining(" "));

			lineElement.setAttribute("points", pointsAttributeValue);

			objectElement.addContent(lineElement);

		}

		if (isTile) {

			TiledTileObject tiledTileObject = (TiledTileObject) object;

			objectElement.setAttribute("gid", String.valueOf(tiledTileObject.getGid()));

		}

		return objectElement;
	}

	private Element toTilesetElement(Tileset tileset) {

		Element tilesetElement = new Element("tileset");

		tilesetElement.setAttribute("firstgid", String.valueOf(tileset.getFirstgid()));
		tilesetElement.setAttribute("name", tileset.getName());
		tilesetElement.setAttribute("tilewidth", String.valueOf(tileset.getTilewidth()));
		tilesetElement.setAttribute("tileheight", String.valueOf(tileset.getTileheight()));

		Element imageElement = toTileImageElement(tileset.getTileImage());

		tilesetElement.addContent(imageElement);

		return tilesetElement;
	}

	private Element toTileImageElement(TileImage image) {

		Element imageElement = new Element("image");

		imageElement.setAttribute("source", image.getSource());
		imageElement.setAttribute("width", String.valueOf(image.getWidth()));
		imageElement.setAttribute("height", String.valueOf(image.getHeight()));

		return imageElement;
	}

}
