package antonafanasjew.cosmodog.writing.model;

/**
 * A text block is a smallest unit in the narration that can be described with meta data. (f.i. rendering information or display speed)
 * The narrative writing is a list of text blocks. Each text block has two important properties: displayType and dynamicsType
 * The first defines how the text should be displayed, the second - how fast.
 * Additionally, the text block can end with a line break or a paragraph.
 */
public class TextBlock {
	
	public static final String DEFAULT_DISPLAY_TYPE = "default";
	public static final String DEFAULT_DYNAMICS_TYPE = "default";
	
	private String speaker;
	private String speakerLabel;
	private String text;
	private String displayType = DEFAULT_DISPLAY_TYPE;
	private String dynamicsType = DEFAULT_DYNAMICS_TYPE;
	
	private boolean endsWithLineBreak;
	private boolean endsWithParagraph;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getDisplayType() {
		return displayType;
	}
	
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	public String getDynamicsType() {
		return dynamicsType;
	}
	
	public void setDynamicsType(String dynamicsType) {
		this.dynamicsType = dynamicsType;
	}
	
	public boolean isEndsWithLineBreak() {
		return endsWithLineBreak;
	}
	
	public void setEndsWithLineBreak(boolean endsWithLineBreak) {
		this.endsWithLineBreak = endsWithLineBreak;
	}
	
	public boolean isEndsWithParagraph() {
		return endsWithParagraph;
	}
	
	public void setEndsWithParagraph(boolean endsWithParagraph) {
		this.endsWithParagraph = endsWithParagraph;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public String getSpeakerLabel() {
		return speakerLabel;
	}

	public void setSpeakerLabel(String speakerLabel) {
		this.speakerLabel = speakerLabel;
	}
	
	public TextBlock copy() {
		TextBlock textBlock = new TextBlock();
		
		textBlock.setDisplayType(this.getDisplayType());
		textBlock.setDynamicsType(this.getDynamicsType());
		textBlock.setEndsWithLineBreak(this.isEndsWithLineBreak());
		textBlock.setEndsWithParagraph(this.isEndsWithParagraph());
		textBlock.setSpeaker(this.getSpeaker());
		textBlock.setSpeakerLabel(this.getSpeakerLabel());
		textBlock.setText(this.getText());
		
		return textBlock;
	}
	
	public TextBlock subTextBlock(int length) {
		TextBlock retVal = copy();
		String text = retVal.getText();
		if (text.length() < length) {
			retVal.setEndsWithLineBreak(false);
			retVal.setEndsWithParagraph(false);
		}
		retVal.setText(text.substring(0, length));
		return retVal;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(text.toString());
		if (endsWithLineBreak) {
			sb.append(System.getProperty("line.separator"));
		}
		if (endsWithParagraph) {
			sb.append(System.getProperty("line.separator"));
			sb.append("----------------------------------");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	public int length() {
		return this.getText().length();
	}
	

}
