package antonafanasjew.cosmodog.actions;

import com.google.common.collect.Lists;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class PhaseRegistry implements Serializable {

	private final List<AsyncAction> phases = Lists.newArrayList();
	private boolean triggered = false;
	private int numberOfPhases;

	public void registerPhase(String phaseName, AsyncAction phase) {
		phase.getProperties().put("phaseName", phaseName);
		this.phases.add(phase);
	}

	public void trigger() {
		this.triggered = true;
		this.numberOfPhases = phases.size();
	}

	public void update(int millis, GameContainer gc, StateBasedGame sbg) {
		
		if (!phases.isEmpty()) {
			AsyncAction phase = phases.getFirst();
			phase.update(millis, gc, sbg);
			if (phase.hasFinished()) {
				phases.removeFirst();
				phase.afterUnregistration();
			}
		}

	}

	public Optional<AsyncAction> currentPhase() {
		if (phases.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(phases.getFirst());
		}

	}

	public Optional<Integer> currentPhaseNumber() {
		if (phases.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(numberOfPhases - phases.size());
		}
	}

	public int numberOfPhases() {
		return numberOfPhases;
	}

	public boolean hasFinished() {
		return triggered && currentPhase().isEmpty();
	}
	
}
