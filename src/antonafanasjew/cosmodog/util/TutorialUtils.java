package antonafanasjew.cosmodog.util;

import java.util.List;

import com.google.common.collect.Lists;

public class TutorialUtils {

	public static final List<String> INITIAL_TUTORIAL_TEXTS = Lists.newArrayList();
	
	static {
		INITIAL_TUTORIAL_TEXTS.add("[---TUTORIAL---] <p> Welcome to Cosmodog, a game about exploration, adventure and survival.");
		INITIAL_TUTORIAL_TEXTS.add("[---TUTORIAL---] <p> Your goal is to find and collect artifacts that represent alien knowledge. If you have enough, you might be able to find a way from the planet.");
		INITIAL_TUTORIAL_TEXTS.add("[---TUTORIAL---] <p> The time in this game stays still until you execute an action. Check the top interface bar to see the current game time.");
		INITIAL_TUTORIAL_TEXTS.add("[---TUTORIAL---] <p> You have basic needs. Find supply rations and approach water to still thirst.");
		INITIAL_TUTORIAL_TEXTS.add("[---TUTORIAL---] <p> As everything in the game, thirst and hunger will increase only when you execute an action causing the time to pass by.");
		INITIAL_TUTORIAL_TEXTS.add("[---TUTORIAL---] <p> Now use the arrow keys to move.");
	}
	
	
}
