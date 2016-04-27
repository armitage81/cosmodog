package antonafanasjew.cosmodog.writing.textbox;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import antonafanasjew.cosmodog.writing.dynamics.TextBlockDynamicsCalculator;
import antonafanasjew.cosmodog.writing.io.NarrativeSequenceReaderImpl;
import antonafanasjew.cosmodog.writing.model.NarrativeSequence;
import antonafanasjew.cosmodog.writing.model.TextBlockBox;
import antonafanasjew.cosmodog.writing.model.TextBlockLine;

public class WritingTextBoxStateTest {

	private WritingTextBoxState out;
	private NarrativeSequence ns;
	
	
	@Before
	public void init() throws IOException {
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		ns = r.read("story.main.0002.alisaenters.html");
//		ns = new NarrativeSequence();
//		TextBlock tb = new TextBlock();
//		tb.setText("ab");
//		tb.setEndsWithParagraph(true);
//		ns.getTextBlocks().add(tb);
		
		WritingTextBox textBox = new WritingTextBox(800, 150, 5, 10, 16, 24);
		WritingTextBoxContent textBoxContent = new WritingTextBoxContent(textBox, ns.getTextBlocks());
		out = new WritingTextBoxState(textBoxContent);
	}
	
	@Test
	public void testChunking() {
		System.out.println(out.getWritingTextBoxContent().getWritingTextBox().getMaxLettersInLine());
		System.out.println(out.getWritingTextBoxContent().getWritingTextBox().getMaxLinesInBox());
		List<TextBlockBox> l = out.getWritingTextBoxContent().getTextChunksForBoxes();
		for (TextBlockBox tbb : l) {
			for (TextBlockLine tbl : tbb) {
				System.out.println(padding("" + tbl.aggregatedText().length(), 5) + " " + tbl.aggregatedText());
			}
			System.out.println("------");
		}
		
	}
	
	private String padding(String text, int n) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n - text.length(); i++) {
			sb.append(" ");
		}
		sb.append(text);
		return sb.toString();
	}
	
	@Test
	public void testDynamics() {
		String t = out.textOfCurrentBox().aggregatedText();
		for (int i = 0; i < t.length() + 5; i++) {
			out.update(TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS);
			System.out.println(out.dynamicTextOfCurrentBox());
		}
		out.switchToNextBoxOrFinish();
		t = out.textOfCurrentBox().aggregatedText();
		for (int i = 0; i < t.length() + 5; i++) {
			out.update(TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS);
			System.out.println(out.dynamicTextOfCurrentBox());
		}
	}
}
