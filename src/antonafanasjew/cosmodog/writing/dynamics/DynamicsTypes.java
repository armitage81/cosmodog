package antonafanasjew.cosmodog.writing.dynamics;

public enum DynamicsTypes {

	//Normal intervals between letters.
	DEFAULT,
	
	//Immediate display
	STAMP,
	
	//Slow, with a pause after the last letter
	UNSURE,
	
	//Pauses at the beginning and at the end of the word, no intervals between letters
	SARCASTIC,
	
	//Pauses between each letter
	LONG
	
}
