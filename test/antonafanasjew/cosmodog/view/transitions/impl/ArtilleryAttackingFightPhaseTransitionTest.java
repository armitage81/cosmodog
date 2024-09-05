package antonafanasjew.cosmodog.view.transitions.impl;

import java.util.List;

import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition.GrenadeTransition;
import org.junit.jupiter.api.Test;

public class ArtilleryAttackingFightPhaseTransitionTest {

	@Test
	public void testIt() {
		List<GrenadeTransition> result;
		ArtilleryAttackingFightPhaseTransition t = new ArtilleryAttackingFightPhaseTransition();
		t.setCompletion(0);
		result = t.grenadeTransitions();
		System.out.println(result);
		
		t.setCompletion(0.21f);
		result = t.grenadeTransitions();
		System.out.println(result);
	}
	
}
