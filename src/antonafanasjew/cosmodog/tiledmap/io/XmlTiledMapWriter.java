package antonafanasjew.cosmodog.tiledmap.io;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import antonafanasjew.cosmodog.tiledmap.*;
import com.badlogic.gdx.utils.Base64Coder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import antonafanasjew.cosmodog.CustomTiledMap;

public class XmlTiledMapWriter implements TiledMapWriter {

    public void writeTiledMap(CustomTiledMap map, String filePath) throws TiledMapIoException {
        Element root = toMapElement(map);
        Document doc = new Document(root);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        try {
            outputter.output(doc, new FileWriter(filePath));
        } catch (IOException e) {
            throw new TiledMapIoException(e.getMessage(), e);
        }
    }

    private Element toMapElement(CustomTiledMap map) {
        Element mapElement = new Element("map");
        mapElement.setAttribute("version", map.getVersion());
        mapElement.setAttribute("tiledversion", map.getTiledVersion());
        mapElement.setAttribute("orientation", map.getOrientation());
        mapElement.setAttribute("renderorder", map.getRenderorder());
        mapElement.setAttribute("width", String.valueOf(map.getWidth()));
        mapElement.setAttribute("height", String.valueOf(map.getHeight()));
        mapElement.setAttribute("tilewidth", String.valueOf(map.getTileWidth()));
        mapElement.setAttribute("tileheight", String.valueOf(map.getTileHeight()));
        mapElement.setAttribute("infinite", map.getInfinite());
        mapElement.setAttribute("nextlayerid", map.getNextLayerId());
        mapElement.setAttribute("nextobjectid", String.valueOf(map.getNextObjectId()));

        mapElement.addContent(toTilesetElement(map.getTileset()));
        for (TiledMapLayer layer : map.getMapLayers()) {
            mapElement.addContent(toLayerElement(layer));
        }
        List<TiledObjectGroup> objectGroups = map.getObjectGroups().values().stream().sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getId()))).toList();
        for (TiledObjectGroup objectGroup : objectGroups) {
            mapElement.addContent(toObjectGroupElement(objectGroup));
        }

        return mapElement;
    }

    private Element toTilesetElement(Tileset tileset) {
        Element tilesetElement = new Element("tileset");
        tilesetElement.setAttribute("firstgid", String.valueOf(tileset.getFirstgid()));
        tilesetElement.setAttribute("name", tileset.getName());
        tilesetElement.setAttribute("tilewidth", String.valueOf(tileset.getTilewidth()));
        tilesetElement.setAttribute("tileheight", String.valueOf(tileset.getTileheight()));
        tilesetElement.setAttribute("tilecount", String.valueOf(tileset.getTileCount()));
        tilesetElement.setAttribute("columns", String.valueOf(tileset.getColumns()));

        tilesetElement.addContent(toTileImageElement(tileset.getTileImage()));

        return tilesetElement;
    }

    private Element toTileImageElement(TileImage tileImage) {
        Element imageElement = new Element("image");
        imageElement.setAttribute("source", tileImage.getSource());
        imageElement.setAttribute("width", String.valueOf(tileImage.getWidth()));
        imageElement.setAttribute("height", String.valueOf(tileImage.getHeight()));
        return imageElement;
    }

    private Element toLayerElement(TiledMapLayer layer) {
        Element layerElement = new Element("layer");
        layerElement.setAttribute("id", layer.getId());
        layerElement.setAttribute("name", layer.getName());
        layerElement.setAttribute("width", String.valueOf(layer.getWidth()));
        layerElement.setAttribute("height", String.valueOf(layer.getHeight()));
        layerElement.setAttribute("visible", layer.isVisible() ? "1" : "0");
        layerElement.setAttribute("opacity", String.valueOf(layer.getOpacity()));

        Element dataElement = toDataElement(layer.getData(), layer.getWidth(), layer.getHeight(), "gzip");
        layerElement.addContent(dataElement);
        return layerElement;
    }

    public static Element toDataElement(List<TiledTile> tiles, int mapWidth, int mapHeight, String compression) {
        if (tiles.size() != mapWidth * mapHeight) {
            throw new IllegalArgumentException("Tile list size does not match map dimensions.");
        }

        byte[] rawData = new byte[tiles.size() * 4];
        for (int i = 0; i < tiles.size(); i++) {
            int gid = tiles.get(i) != null ? tiles.get(i).getGid() : 0;
            rawData[i * 4]     = (byte) (gid & 0xFF);
            rawData[i * 4 + 1] = (byte) ((gid >> 8) & 0xFF);
            rawData[i * 4 + 2] = (byte) ((gid >> 16) & 0xFF);
            rawData[i * 4 + 3] = (byte) ((gid >> 24) & 0xFF);
        }
        try {
            byte[] finalData;
            if (compression == null) {
                finalData = rawData;
            } else if (compression.equals("gzip")) {
                finalData = compressWithGzip(rawData);
            } else if (compression.equals("zlib")) {

                    finalData = compressWithZlib(rawData);

            } else {
                throw new IOException("Unrecognized compression type: " + compression);
            }

            char[] charArray = Base64Coder.encode(finalData);
            String encoded = new String(charArray);

            Element dataElement = new Element("data");
            dataElement.setAttribute("encoding", "base64");
            if (compression != null) {
                dataElement.setAttribute("compression", compression);
            }
            dataElement.setText(encoded);

            return dataElement;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] compressWithGzip(byte[] input) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(byteStream)) {
            gzipOut.write(input);
        }
        return byteStream.toByteArray();
    }

    private static byte[] compressWithZlib(byte[] input) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (DeflaterOutputStream zlibOut = new DeflaterOutputStream(byteStream)) {
            zlibOut.write(input);
        }
        return byteStream.toByteArray();
    }

    private Element toObjectGroupElement(TiledObjectGroup group) {
        Element groupElement = new Element("objectgroup");
        groupElement.setAttribute("id", group.getId());
        groupElement.setAttribute("name", group.getName());

        Comparator<TiledObject> c = Comparator.comparingInt(TiledObject::getId);

        List<TiledObject> objects = group.getObjects().values().stream().sorted(c).toList();

        for (TiledObject object : objects) {
            groupElement.addContent(toObjectElement(object));
        }

        return groupElement;
    }

    public static Element toObjectElement(TiledObject object) {
        Element objectElement = new Element("object");

        objectElement.setAttribute("id", String.valueOf(object.getId()));
        objectElement.setAttribute("x", String.valueOf(object.getX()));
        objectElement.setAttribute("y", String.valueOf(object.getY()));
        objectElement.setAttribute("name", object.getName() != null ? object.getName() : "");
        objectElement.setAttribute("type", object.getType() != null ? object.getType() : "");

        if (object instanceof TiledFigureObject fig) {
            objectElement.setAttribute("width", String.valueOf(fig.getWidth()));
            objectElement.setAttribute("height", String.valueOf(fig.getHeight()));
        } else if (object instanceof  TiledPolygonObject || object instanceof TiledPolylineObject) {
            objectElement.setAttribute("width", "1");
            objectElement.setAttribute("height", "1");
        }

        if (object instanceof TiledTileObject tile) {
            objectElement.setAttribute("gid", String.valueOf(tile.getGid()));
        }

        if (object instanceof TiledEllipseObject) {
            objectElement.addContent(new Element("ellipse"));
        }

        if (object instanceof TiledPolygonObject || object instanceof TiledPolylineObject) {
            TiledLineObject line = (TiledLineObject) object;
            StringBuilder pointsBuilder = new StringBuilder();
            for (int i = 0; i < line.getPoints().size(); i++) {
                TiledLineObject.Point p = line.getPoints().get(i);
                double localX = p.xx - line.getX();
                double localY = p.yy - line.getY();
                pointsBuilder.append(localX).append(",").append(localY);
                if (i < line.getPoints().size() - 1) {
                    pointsBuilder.append(" ");
                }
            }

            Element lineElement = new Element(object instanceof TiledPolygonObject ? "polygon" : "polyline");
            lineElement.setAttribute("points", pointsBuilder.toString());
            objectElement.addContent(lineElement);
        }

        if (!object.getProperties().isEmpty()) {
            Element properties = new Element("properties");
            for (Map.Entry<String, String> entry : object.getProperties().entrySet()) {
                Element property = new Element("property");
                property.setAttribute("name", entry.getKey());
                property.setAttribute("value", entry.getValue());
                properties.addContent(property);
            }
            objectElement.addContent(properties);
        }

        return objectElement;
    }
}