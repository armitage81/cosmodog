package antonafanasjew.cosmodog.writing.model;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Container for a narrative sequence. A narrative sequence is a dialog or narration text that consists of 
 * text blocks. Each text block represents a section in a dialog (f.i. fluid text)
 * A text block is the smallest text unit that can be augmented with display meta data.
 */
public class NarrativeSequence {
	
	private String id;
	private List<TextBlock> textBlocks = Lists.newArrayList();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<TextBlock> getTextBlocks() {
		return textBlocks;
	}
	public void setTextBlocks(List<TextBlock> textBlocks) {
		this.textBlocks = textBlocks;
	}

	@Override
	public String toString() {
		return Joiner.on("").join(textBlocks);
	}
	
}
