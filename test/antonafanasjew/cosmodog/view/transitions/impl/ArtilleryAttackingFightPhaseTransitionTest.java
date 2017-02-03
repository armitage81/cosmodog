package antonafanasjew.cosmodog.view.transitions.impl;

import java.util.List;

import org.junit.Test;

import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition.GrenadeTransition;

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
