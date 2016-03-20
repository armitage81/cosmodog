package antonafanasjew.cosmodog.writing.model;

import java.util.List;

import org.junit.Test;

import antonafanasjew.cosmodog.writing.io.NarrativeSequenceReaderImpl;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxContent;

public class TextBlockBoxTest {

	@Test
	public void testName() throws Exception {
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		NarrativeSequence ns = r.read("story.main.0002.alisaenters.html");

		WritingTextBox textBox = new WritingTextBox(800, 150, 0, 5, 20, 30);
		WritingTextBoxContent textBoxContent = new WritingTextBoxContent(textBox, ns.getTextBlocks());
		
		List<TextBlockBox> boxes = textBoxContent.getTextChunksForBoxes();
		
		
		
//		for (TextBlockBox box : boxes) {
//			System.out.println(box.subTextBlockBox(box.aggregatedText().length()).aggregatedText());
//			System.out.println("-------------------");
//		}
		
		TextBlockBox box1 = boxes.get(0);
		
		for (int i = 0; i < box1.aggregatedText().length(); i++) {
			String partialText = box1.subTextBlockBox(i).aggregatedText();
			System.out.println(partialText);
			System.out.println("---------");
		}
		
	}
	
}
