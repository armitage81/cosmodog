package antonafanasjew.cosmodog.tiledmap.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StreamUtils;
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
import antonafanasjew.cosmodog.tiledmap.TiledRectObject;
import antonafanasjew.cosmodog.tiledmap.TiledTile;
import antonafanasjew.cosmodog.tiledmap.TiledTileObject;
import antonafanasjew.cosmodog.tiledmap.Tileset;

public class XmlTiledMapReader implements TiledMapReader {

	private String fileName;

	public XmlTiledMapReader(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public CustomTiledMap readTiledMap() throws TiledMapIoException {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(fileName);
		try {
			Document document = (Document) builder.build(xmlFile);
			Element root = document.getRootElement();
			CustomTiledMap map = fromMapElement(root);
			return map;
			
		} catch (JDOMException | IOException e) {
			throw new TiledMapIoException(e.getMessage(), e);
		}
	}
	
	private CustomTiledMap fromMapElement(Element element) throws IOException {
		CustomTiledMap map = new CustomTiledMap();
		
		String version = element.getAttribute("version").getValue();
		String tiledVersion = element.getAttribute("tiledversion").getValue();
		String infinite = element.getAttribute("infinite").getValue();
		String nextLayerId = element.getAttribute("nextlayerid").getValue();
		String orientation = element.getAttribute("orientation").getValue();
		String renderorder = element.getAttribute("renderorder").getValue();
		String width = element.getAttribute("width").getValue();
		String height = element.getAttribute("height").getValue();
		String tilewidth = element.getAttribute("tilewidth").getValue();
		String tileheight = element.getAttribute("tileheight").getValue();
		String nextobjectid = element.getAttribute("nextobjectid").getValue();
		
		map.setVersion(version);
		map.setTiledVersion(tiledVersion);
		map.setInfinite(infinite);
		map.setOrientation(orientation);
		map.setRenderorder(renderorder);
		map.setWidth(Integer.parseInt(width));
		map.setHeight(Integer.parseInt(height));
		map.setTileWidth(Integer.parseInt(tilewidth));
		map.setTileHeight(Integer.parseInt(tileheight));
		map.setNextLayerId(nextLayerId);
		map.setNextObjectId(Integer.parseInt(nextobjectid));
		
		map.setTileset(fromTilesetElement(element.getChild("tileset")));
		map.setMapLayers(fromMapLayerList(element.getChildren("layer")));
		List<TiledObjectGroup> objectGroups = fromObjectGroupList(element.getChildren("objectgroup"));
		
		for (TiledObjectGroup objectGroup : objectGroups) {
			map.getObjectGroups().put(objectGroup.getName(), objectGroup);
		}
		
		map.afterInitialization();
		
		return map;
		
	}
	
	private List<TiledMapLayer> fromMapLayerList(List<Element> elements) throws IOException {
		List<TiledMapLayer> layers = Lists.newArrayList();
		for (Element element : elements) {
			layers.add(fromMapLayerElement(element));
		}
		return layers;
	}

	private TiledMapLayer fromMapLayerElement(Element element) throws IOException {
		TiledMapLayer layer = new TiledMapLayer();
		
		String id = element.getAttribute("id").getValue();
		String name = element.getAttribute("name").getValue();
		String width = element.getAttribute("width").getValue();
		String height = element.getAttribute("height").getValue();
		Attribute visibleAttribute = element.getAttribute("visible");
		//Per default this attribute is skipped, defaulting to visible=true
		boolean visible = visibleAttribute == null || !visibleAttribute.getValue().equals("0");
		Attribute opacityAttribute = element.getAttribute("opacity");
		float opacity = opacityAttribute == null ? 1.0f : Float.parseFloat(opacityAttribute.getValue());
		
		layer.setId(id);
		layer.setName(name);
		int mapWidth = Integer.parseInt(width);
		int mapHeight = Integer.parseInt(height);
		layer.setWidth(mapWidth);
		layer.setHeight(mapHeight);
		layer.setVisible(visible);
		layer.setOpacity(opacity);
		
		Element data = element.getChild("data");
		
		layer.setData(fromDataElement(data, mapWidth, mapHeight));
		
		return layer;
	}

	private List<TiledTile> fromDataElement(Element data, int mapWidth, int mapHeight) throws IOException {
		//return fromTileList(data.getChildren("tile"));
		List<TiledTile> retVal = Lists.newArrayList();
		InputStream is = null;
		try {
			String compression = data.getAttributeValue("compression");
			byte[] bytes = Base64Coder.decode(data.getText().trim());
			if (compression == null)
				is = new ByteArrayInputStream(bytes);
			else if (compression.equals("gzip"))
				is = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes), bytes.length));
			else if (compression.equals("zlib"))
				is = new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(bytes)));
			else
				throw new IOException("Unrecognised compression (" + compression + ") for TMX Layer Data");

			byte[] temp = new byte[4];
			for (int y = 0; y < mapHeight; y++) {
				for (int x = 0; x < mapWidth; x++) {
					int read = is.read(temp);
					while (read < temp.length) {
						int curr = is.read(temp, read, temp.length - read);
						if (curr == -1) break;
						read += curr;
					}
					if (read != temp.length) {
						throw new IOException("Error Reading TMX Layer Data: Premature end of tile data");
					}
					
					int gid = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8 | unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;
					
					//To use less memory do not create a TiledTile object in case the gid is 0
					TiledTile tile = null;
					
					if (gid > 0) {
						tile = new TiledTile();
						tile.setGid(gid);
					}
					
					retVal.add(tile); 
				}
			}
		} catch (Exception e) {
			throw e;
		}
		finally {
			StreamUtils.closeQuietly(is);
		}
		
		return retVal;
		
	}

	private int unsignedByteToInt (byte b) {
		return b & 0xFF;
	}
	
	private List<TiledObjectGroup> fromObjectGroupList(List<Element> elements) {
		List<TiledObjectGroup> tiledObjectGroups = Lists.newArrayList();
		for (Element element : elements) {
			tiledObjectGroups.add(fromObjectGroupElement(element));
		}
		return tiledObjectGroups;
	}
	
	private TiledObjectGroup fromObjectGroupElement(Element element) {
		TiledObjectGroup tiledObjectGroup = new TiledObjectGroup();
		tiledObjectGroup.setId(element.getAttributeValue("id"));
		tiledObjectGroup.setName(element.getAttributeValue("name"));
		List<Element> objectElements = element.getChildren("object");
		for (Element objectElement : objectElements) {
			TiledObject tiledObject = fromObjectElement(objectElement);
			tiledObjectGroup.getObjects().put(tiledObject.getName(), tiledObject);
		}
		return tiledObjectGroup;
	}
	
	private TiledObject fromObjectElement(Element element) {
		
		TiledObject retVal = null;
		
		
		
		boolean isEllipseElement = element.getChild("ellipse") != null;
		boolean isTileElement = element.getAttribute("gid") != null;
		boolean isPolylineElement = element.getChild("polyline") != null;
		boolean isPolygonElement = element.getChild("polygon") != null;
		boolean isRectElement = !isEllipseElement && !isPolygonElement && !isPolylineElement && !isTileElement;
		
		boolean isFigureBased = isRectElement || isEllipseElement;
		boolean isLineBased = isPolylineElement || isPolygonElement;
		
		if (isEllipseElement) {
			retVal = new TiledEllipseObject();
		}
		if (isRectElement) {
			retVal = new TiledRectObject();
		}
		if (isPolygonElement) {
			retVal = new TiledPolygonObject();
		}
		if (isPolylineElement) {
			retVal = new TiledPolylineObject();
		}
		if (isTileElement) {
			retVal = new TiledTileObject();
		}
		
		retVal.setId(Integer.parseInt(element.getAttributeValue("id")));
		retVal.setX(Float.parseFloat(element.getAttributeValue("x")));
		retVal.setY(Float.parseFloat(element.getAttributeValue("y")));
		retVal.setName(element.getAttributeValue("name"));
		retVal.setType(element.getAttributeValue("type"));
		
		Element properties = element.getChild("properties");
		if (properties != null) {
			List<Element> propertyElements = properties.getChildren("property");
			for (Element propertyElement : propertyElements) {
				retVal.getProperties().put(propertyElement.getAttributeValue("name"), propertyElement.getAttributeValue("value"));
			}
		}
		
		if (isFigureBased) {
			TiledFigureObject o = (TiledFigureObject)retVal;
			o.setWidth(Float.parseFloat(element.getAttributeValue("width")));
			o.setHeight(Float.parseFloat(element.getAttributeValue("height")));
		}
		
		if (isLineBased) {
			TiledLineObject o = (TiledLineObject)retVal;
			
			Element lineElement = element.getChild("polygon");
			if (lineElement == null) {
				lineElement = element.getChild("polyline");
			}
			
			String pointsText = lineElement.getAttributeValue("points");
			String[] points = pointsText.split(" ");
			for (String point : points) {
				String[] coordinates = point.split(",");
				//Take care. This coordinates are all relating to the x/y starting point of the polygon which is considered as 0/0
				//When creating the polygon object, the actual x/y position should be added to each point.
				float cx = Float.parseFloat(coordinates[0]);
				float cy = Float.parseFloat(coordinates[1]);
				Point p = new Point();
				p.x = cx + o.getX();
				p.y = cy + o.getY();
				o.getPoints().add(p);
			}
		}
		
		if (isTileElement) {
			TiledTileObject o = (TiledTileObject)retVal;
			o.setGid(Integer.parseInt(element.getAttributeValue("gid")));
		}
		

		
		return retVal;
	}

	private Tileset fromTilesetElement(Element element) {
		Tileset tileset = new Tileset();
		String firstgid = element.getAttribute("firstgid").getValue();
		String name = element.getAttribute("name").getValue();
		String tilewidth = element.getAttribute("tilewidth").getValue();
		String tileheight = element.getAttribute("tileheight").getValue();
		
		int columns = Integer.valueOf(element.getAttribute("columns").getValue());
		int tileCount = Integer.valueOf(element.getAttribute("tilecount").getValue());
		
		tileset.setFirstgid(Integer.parseInt(firstgid));
		tileset.setName(name);
		tileset.setTilewidth(Integer.parseInt(tilewidth));
		tileset.setTileheight(Integer.parseInt(tileheight));
		tileset.setColumns(columns);
		tileset.setTileCount(tileCount);
		tileset.setTileImage(fromTileImageElement(element.getChild("image")));
		
		return tileset;
	}

	private TileImage fromTileImageElement(Element element) {
		TileImage tileImage = new TileImage();
		
		String source = element.getAttribute("source").getValue();
		String width = element.getAttribute("width").getValue();
		String height = element.getAttribute("height").getValue();
		
		tileImage.setSource(source);
		tileImage.setWidth(Integer.parseInt(width));
		tileImage.setHeight(Integer.parseInt(height));
		
		return tileImage;
	}

}
