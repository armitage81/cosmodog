package antonafanasjew.cosmodog.view.transitions.impl;

import java.util.List;

import antonafanasjew.cosmodog.view.transitions.EnemyAttackingFightPhaseTransition;

import com.google.common.collect.Lists;

public class ArtilleryAttackingFightPhaseTransition extends EnemyAttackingFightPhaseTransition {

	private static final float VISIBLE_SALVE_TIME = 0.45f;
	
	private static final float RISING_COMPLETION_THRESHOLD = VISIBLE_SALVE_TIME;
	private static final float FALLING_COMPLETION_THRESHOLD = 1f - VISIBLE_SALVE_TIME;
	
	private static final float VISIBLE_GRENADE_TIME = 0.1f;
	private static final int MAX_GRENADES = 4;
	
	private static final float TIME_INTERVAL_BETWEEN_GRENADES = (VISIBLE_SALVE_TIME - VISIBLE_GRENADE_TIME) / (MAX_GRENADES - 1);
	
	public class GrenadeTransition {
		public boolean risingNotFalling; //Describes a rising grenade if true, falling otherwise;
		public boolean leftNotRight; //Describes the grenade from the left cannon if true, right cannon otherwise.
		public float relativeHeight; //Describes the relative height. 0f means just shot or landed, 1f means reached the max height
		
		public String toString() {
			return "Rising:" + (risingNotFalling ? "Yes" : "No") + " Left:" + (leftNotRight ? "Yes" : "No") + " RelH:" + relativeHeight;
		}
	}
	

	public List<GrenadeTransition> grenadeTransitions() {
		
		List<GrenadeTransition> retVal = Lists.newArrayList();
		
		float completion = getCompletion();
		
		if (completion < RISING_COMPLETION_THRESHOLD) {
			int numberOfVisibleGrenades = (int)(completion / TIME_INTERVAL_BETWEEN_GRENADES) + 1;
			numberOfVisibleGrenades = numberOfVisibleGrenades > MAX_GRENADES ? MAX_GRENADES : numberOfVisibleGrenades;
			for (int i = 0; i < numberOfVisibleGrenades; i++) {
				GrenadeTransition grenadeTransition = new GrenadeTransition();
				grenadeTransition.leftNotRight = i % 2 == 0;
				grenadeTransition.risingNotFalling = true;
				grenadeTransition.relativeHeight = (completion - i * TIME_INTERVAL_BETWEEN_GRENADES) / VISIBLE_GRENADE_TIME;
				if (grenadeTransition.relativeHeight <= 1.0f) {
					retVal.add(grenadeTransition);
				}
			}
		} else if (completion >= FALLING_COMPLETION_THRESHOLD) {
			int numberOfVisibleGrenades = (int)((completion - FALLING_COMPLETION_THRESHOLD) / TIME_INTERVAL_BETWEEN_GRENADES) + 1;
			numberOfVisibleGrenades = numberOfVisibleGrenades > MAX_GRENADES ? MAX_GRENADES : numberOfVisibleGrenades;
			for (int i = 0; i < numberOfVisibleGrenades; i++) {
				GrenadeTransition grenadeTransition = new GrenadeTransition();
				grenadeTransition.leftNotRight = i % 2 == 0;
				grenadeTransition.risingNotFalling = false;
				grenadeTransition.relativeHeight = 1 - ((completion - FALLING_COMPLETION_THRESHOLD - i * TIME_INTERVAL_BETWEEN_GRENADES) / VISIBLE_GRENADE_TIME);
				if (grenadeTransition.relativeHeight >= 0f) {
					retVal.add(grenadeTransition);
				}
			}			
		}
		
		return retVal;
	}

	public boolean playerTakingDamage() {
		return getCompletion() >= FALLING_COMPLETION_THRESHOLD;
	}
	
}
