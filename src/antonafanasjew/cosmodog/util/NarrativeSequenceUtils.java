package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.notifications.Notification;
import antonafanasjew.cosmodog.writing.model.NarrativeSequence;
import antonafanasjew.cosmodog.writing.model.TextBlock;


public class NarrativeSequenceUtils {

	public static final String INTRO_0001_OPENING = "intro.0001.opening.html";
	
	public static final String STORY_ITEMS_0001_FOUNDINFOBIT = "story.items.0001.foundinfobit.html";
	public static final String STORY_ITEMS_0001_FOUNDRATION = "story.items.0002.foundration.html";
	
	public static final String STORY_MAIN_0001_AFTERLANDING = "story.main.0001.afterlanding.html";
	public static final String STORY_MAIN_0002_ALISAENTERS = "story.main.0002.alisaenters.html";
	
	
	public static final String ALISAS_COMMENTS_0001_FOUND_WATER_01 = "comments/alisascomments.001.foundwater.01.html";
	
		
	public static NarrativeSequence commentNarrativeSequenceFromText(Notification notification) {
		String text = notification.getText();
		return commentNarrativeSequenceFromText(text);
		
	}
	
	public static NarrativeSequence commentNarrativeSequenceFromText(String text) {
		NarrativeSequence ns = new NarrativeSequence();
		String[] paragraphs = text.split("\n\n");
		
		TextBlock lastTextBlock = null;
		
		for (String paragraph : paragraphs) {
			String[] lines = paragraph.split("\n");
			for (String line : lines) {
				String[] words = line.split(" ");
				for (String word : words) {
					lastTextBlock = new TextBlock();
					ns.getTextBlocks().add(lastTextBlock);
					lastTextBlock.setSpeaker(WritingRendererUtils.SPEAKER_SYSTEM);
					lastTextBlock.setSpeakerLabel("");
					lastTextBlock.setText(word);
				}
				lastTextBlock.setEndsWithLineBreak(true);
			}
			lastTextBlock.setEndsWithLineBreak(false);
			lastTextBlock.setEndsWithParagraph(true);
		}
		
		return ns;
	}
	
	
}
