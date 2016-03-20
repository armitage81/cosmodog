package antonafanasjew.cosmodog.writing.textbox;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import antonafanasjew.cosmodog.writing.io.NarrativeSequenceReaderImpl;
import antonafanasjew.cosmodog.writing.model.NarrativeSequence;
import antonafanasjew.cosmodog.writing.model.TextBlockBox;
import antonafanasjew.cosmodog.writing.model.TextBlockLine;

public class WritingTextBoxTest {

	private WritingTextBoxContent out;
	private NarrativeSequence ns;
	
	
	@Before
	public void init() throws IOException {
		NarrativeSequenceReaderImpl r = new NarrativeSequenceReaderImpl();
		ns = r.read("story.main.0002.alisaenters.html");
		WritingTextBox textBox = new WritingTextBox(800, 150, 5, 10, 16, 24);
		out = new WritingTextBoxContent(textBox, ns.getTextBlocks());
	}
	
	@Test
	public void test() {
		System.out.println(out.getWritingTextBox().getMaxLettersInLine());
		System.out.println(out.getWritingTextBox().getMaxLinesInBox());
		List<TextBlockBox> l = out.getTextChunksForBoxes();
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
	
	
}
