package antonafanasjew.cosmodog.writing.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

import antonafanasjew.cosmodog.writing.model.NarrativeSequence;
import antonafanasjew.cosmodog.writing.model.TextBlock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class NarrativeSequenceReaderImpl implements NarrativeSequenceReader {

	public static final String WRITING_PATH = "antonafanasjew/cosmodog/writing";
	
	@Override
	public NarrativeSequence read(String narrativeSequenceId) throws IOException {
		
		String writingResourcePath = WRITING_PATH + "/" + narrativeSequenceId;
		InputStream is = NarrativeSequenceReader.class.getClassLoader().getResourceAsStream(writingResourcePath);
		
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document) builder.build(is);
			Element root = document.getRootElement();
			NarrativeSequence narrationSequence = fromHtmlElement(narrativeSequenceId, root);
			return narrationSequence;
			
		} catch (JDOMException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	private NarrativeSequence fromHtmlElement(String narrativeSequenceId, Element element) {
		NarrativeSequence narrativeSequence = new NarrativeSequence();
		narrativeSequence.setId(narrativeSequenceId);
		Element bodyElement = element.getChild("body");
		List<Element> divs = bodyElement.getChildren("div");
		for (Element div : divs) {
			List<TextBlock> textBlocks = fromDivElement(div);
			narrativeSequence.getTextBlocks().addAll(textBlocks);
		}
		return narrativeSequence;
	}

	private List<TextBlock> fromDivElement(Element div) {
		List<TextBlock> retVal = Lists.newArrayList();
		String style = div.getAttributeValue("style");
		String[] styleParts = style.split(";");
		Map<String, String> styleParams = Maps.newHashMap();
		for (String stylePart : styleParts) {
			String[] styleParamAndValue = stylePart.split(":");
			String styleParam = styleParamAndValue[0];
			String styleParamValue = "";
			if (styleParamAndValue.length > 1) {
				styleParamValue = styleParamAndValue[1];
			}
			styleParams.put(styleParam, styleParamValue);
		}
		
		String speaker = styleParams.get("speaker");
		String speakerLabel = styleParams.get("speakerLabel");
		if (speakerLabel == null) {
			speakerLabel = "";
		}
		
		List<Content> textContents = div.getContent();
		TextBlock lastTextBlock = null;
		for (Content textContent : textContents) {
			if (textContent instanceof Text) {
				Text textText = (Text)textContent;
				String text = textText.getTextTrim();
				if (text.length() > 0) {
					String[] words = text.split("\\s+");
					for (String word : words) {
						TextBlock textBlock = new TextBlock();
						textBlock.setSpeaker(speaker);
						textBlock.setSpeakerLabel(speakerLabel);
						textBlock.setText(word);
						retVal.add(textBlock);
						lastTextBlock = textBlock; //We do this to update the text block with paragraph or new line if the element comes next.					
					}
				}
			} else if (textContent instanceof Element) {
				Element textElement = (Element)textContent;
				if (textElement.getName().equals("span")) {
					String text = textElement.getText();
					String[] words = text.split("\\s+");
					
					for (String word : words) {
					
						TextBlock textBlock = new TextBlock();
						textBlock.setSpeaker(speaker);
						textBlock.setSpeakerLabel(speakerLabel);
						
						String classAttrValue = textElement.getAttributeValue("class");
						if (classAttrValue.contains("_")) {
							String[] classAttrValueParts = classAttrValue.split("_");
							String dynamicsType = classAttrValueParts[0];
							String displayType = classAttrValueParts[1];
							textBlock.setDynamicsType(dynamicsType);
							textBlock.setDisplayType(displayType);
						} else {
							textBlock.setDynamicsType(classAttrValue);
						}
						
						textBlock.setText(word);
						retVal.add(textBlock);
						lastTextBlock = textBlock;
					
					}
					
				} else if (textElement.getName().equals("p")) {
					if (lastTextBlock != null) {
						lastTextBlock.setEndsWithLineBreak(true);
					}
				} else if (textElement.getName().equals("hr")) {
					if (lastTextBlock != null) {
						lastTextBlock.setEndsWithParagraph(true);
					}
				}
			}
		}
		
		return retVal;
	}

	public static void main(String[] args) throws IOException {
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		NarrativeSequence ns = r.read("story.main.0002.alisaenters.html");
		System.out.println(ns.toString());
	}
	
}
