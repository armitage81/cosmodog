package antonafanasjew.cosmodog.writing.model;

import java.util.Map;

/**
 * Container for multiple not connected narration sequences.
 */
public class Narration {
	
	private String id;
	private Map<String, NarrativeSequence> narrativeSequences;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Map<String, NarrativeSequence> getNarrativeSequences() {
		return narrativeSequences;
	}

	public void setNarrativeSequences(Map<String, NarrativeSequence> narrativeSequences) {
		this.narrativeSequences = narrativeSequences;
	}

	
	
}
