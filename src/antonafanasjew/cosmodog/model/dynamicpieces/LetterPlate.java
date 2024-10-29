package antonafanasjew.cosmodog.model.dynamicpieces;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.death.RespawnAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.LetterPlateSequence;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class LetterPlate extends DynamicPiece {

	private static final long serialVersionUID = 3046060909933670030L;

	private static final short NUMBER_OF_SHAPES = 4;
	public static final char CONTROL_LETTER_RESET = '-';

	public static final String[] ALPHABETHS = {
		"a",
		"l",
		" ",
		"oycfgij",
		"bdhlkmq",
		"nprtsu",
		"aevwxz",
		"cfgijkmqsuvwxz",
		"" + CONTROL_LETTER_RESET
	};
	
	private char[] possibleCharacters;
	private char character;
	private boolean pressed = false;
	private boolean active = true;
	
	private static short shapeLoopCounter = 0;
	private short shapeNumber = (short)((shapeLoopCounter++) % NUMBER_OF_SHAPES);

	public static LetterPlate create(Position position, String characters) {
		LetterPlate letterPlate = new LetterPlate(characters);
		letterPlate.setPosition(position);
		return letterPlate;
	}
	
	private LetterPlate(String characters) {
		possibleCharacters = characters.toCharArray();
		selectCharacter(); 
	}
	
	@Override
	public void interactWhenSteppingOn() {
		
		//After finishing the riddle, the plates will be deactivated. From then on, no interaction is needed.
		if (!active) {
			return;
		}
		
		//The plate will be rendered as pressed and the corresponding sound will be played.
		pressed = true;
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_LETTER_PLATE).play();
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		
		//The previously entered letter sequence is stored in the game progress object. We add the letter of the plate we step on to the sequence.
		LetterPlateSequence sequence = player.getGameProgress().getLetterPlateSequence();
		char letter = this.getCharacter();
		
		if (letter == CONTROL_LETTER_RESET) {
			sequence.resetSequence();
			return;
		}
		
		sequence.addLetter(letter);
		
		//If it was the last letter of the correct sequence, we print the corresponding success message over the player's head.
		//We also deactivate all letter plates in the game. They are not dangerous anymore.
		if (sequence.sequenceComplete()) {
			OverheadNotificationAction.registerOverheadNotification(player, "\"The opposite of nervousness...\"");
			OverheadNotificationAction.registerOverheadNotification(player, LetterPlateSequence.FULL_SEQUENCE.toUpperCase() + "!!!");
			Collection<DynamicPiece> letterPlates = map.getDynamicPieces().get(LetterPlate.class);
			for (DynamicPiece piece : letterPlates) {
				((LetterPlate)piece).setActive(false);
				((LetterPlate)piece).setPressed(false);
			}
		} else {
			//We print the currently entered sequence over the player's head. It is not complete. It could be a valid sub sequence, or a wrong one.
			StringBuffer message = new StringBuffer(sequence.getCurrentSequence().toUpperCase() + "...");
			//If the currently entered sequence is wrong due to the last entered character, we append the corresponding message to the message over the player's head.
			OverheadNotificationAction.registerOverheadNotification(player, message.toString());
			
			if (!sequence.sequenceCorrect()) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
				OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> Something is wrong.");
				OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> I must start anew.");
				RespawnAction wrongSequenceConsequenceAction = new RespawnAction(Position.fromCoordinates(218, 191, MapType.MAIN), true, true);
				ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.RESPAWNING, wrongSequenceConsequenceAction);
			}
		}
		
		//If the sequence was not correct, we reset it.
		if (!sequence.sequenceCorrect()) {
			sequence.resetSequence();
		}
	}
	
	@Override
	public void interactWhenLeaving() {
		
		if (!active) {
			return;
		}
		
		pressed = false;
	}

	@Override
	public boolean wrapsCollectible() {
		return false;
	}

	@Override
	public void interactWhenApproaching() {
		selectCharacter();
	}

	private void selectCharacter() {
		int randomIndex = ThreadLocalRandom.current().nextInt(0, possibleCharacters.length);
		character = possibleCharacters[randomIndex];
	}
	
	public short getShapeNumber() {
		return shapeNumber;
	}
	
	public char getCharacter() {
		return character;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
}
