package antonafanasjew.cosmodog.tiledmap.io;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import antonafanasjew.cosmodog.tiledmap.TiledTile;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.StreamUtils;
import com.google.common.collect.Lists;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlTiledMapComparator {

    public boolean compareMaps(String file1, String file2) throws IOException {
        try {
            Document doc1 = loadXmlDocument(file1);
            Document doc2 = loadXmlDocument(file2);

            return compareElements(doc1.getRootElement(), doc2.getRootElement(), "/" + doc1.getRootElement().getName());
        } catch (JDOMException e) {
            throw new IOException("Error parsing XML: " + e.getMessage(), e);
        }
    }

    private Document loadXmlDocument(String filePath) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        return builder.build(new File(filePath));
    }

    private boolean compareElements(Element e1, Element e2, String path) {
        if (!e1.getName().equals(e2.getName())) {
            System.out.println("Element name mismatch at " + path + ": " + e1.getName() + " vs " + e2.getName());
            return false;
        }

        if (!compareAttributes(e1.getAttributes(), e2.getAttributes(), path)) {
            return false;
        }

        String t1 = e1.getText().trim();
        String t2 = e2.getText().trim();



        if (!t1.equals(t2)) {
            System.out.println("Text mismatch at " + path + ": '" + t1 + "' vs '" + t2 + "'");
            return false;
        }

        List<Element> children1 = e1.getChildren();
        List<Element> children2 = e2.getChildren();

        if (children1.size() != children2.size()) {
            System.out.println("Child count mismatch at " + path + ": " + children1.size() + " vs " + children2.size());
            return false;
        }

        for (int i = 0; i < children1.size(); i++) {
            Element c1 = children1.get(i);
            Element c2 = children2.get(i);
            String childPath = path + "/" + c1.getName() + "[" + (i + 1) + "]";
            if (!compareElements(c1, c2, childPath)) {
                return false;
            }
        }

        return true;
    }

    private boolean compareAttributes(List<Attribute> attrs1, List<Attribute> attrs2, String path) {
        if (attrs1.size() != attrs2.size()) {
            System.out.println("Attribute count mismatch at " + path + ": " + attrs1.size() + " vs " + attrs2.size());
            return false;
        }

        for (Attribute attr1 : attrs1) {
            Attribute attr2 = findAttributeByName(attr1.getName(), attrs2);
            if (attr2 == null) {
                System.out.println("Missing attribute '" + attr1.getName() + "' at " + path);
                return false;
            }
            if (!attr1.getValue().equals(attr2.getValue())) {
                System.out.println("Attribute value mismatch for '" + attr1.getName() + "' at " + path + ": '" +
                        attr1.getValue() + "' vs '" + attr2.getValue() + "'");
                return false;
            }
        }

        return true;
    }

    private Attribute findAttributeByName(String name, List<Attribute> attributes) {
        for (Attribute attr : attributes) {
            if (attr.getName().equals(name)) {
                return attr;
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {

        String leftMap = System.getProperty("mapPath.left");
        String rightMap = System.getProperty("mapPath.right");


        XmlTiledMapComparator comparator = new XmlTiledMapComparator();
        boolean equal = comparator.compareMaps(leftMap, rightMap);
        System.out.println(equal ? "Maps are equal." : "Maps differ.");
    }
}
