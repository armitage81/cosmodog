package antonafanasjew.cosmodog.writing.io;

import java.io.IOException;

import antonafanasjew.cosmodog.writing.model.NarrativeSequence;

public interface NarrativeSequenceReader {

	NarrativeSequence read(String narrativeSequenceId) throws IOException ;
	
}
