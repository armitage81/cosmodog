package antonafanasjew.cosmodog.writing.model;

import java.util.List;

import org.junit.Test;

import antonafanasjew.cosmodog.writing.io.NarrativeSequenceReaderImpl;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxContent;

public class TextBlockLineTest {

	
	@Test
	public void testName() throws Exception {
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		NarrativeSequence ns = r.read("story.main.0002.alisaenters.html");

		WritingTextBox textBox = new WritingTextBox(800, 150, 5, 10, 16, 24);
		WritingTextBoxContent textBoxContent = new WritingTextBoxContent(textBox, ns.getTextBlocks());
		
		List<TextBlockLine> lines = textBoxContent.getTextChunksForLines();
		
		
		
		for (TextBlockLine line : lines) {
			System.out.println(line.subTextBlockLine(line.aggregatedText().length()).aggregatedText());
		}
		
	}
	
}
