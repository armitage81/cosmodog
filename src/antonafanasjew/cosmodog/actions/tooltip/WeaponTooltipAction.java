package antonafanasjew.cosmodog.actions.tooltip;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class WeaponTooltipAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -1790364270251650196L;

	public static class WeaponTooltipTransition {
		public Weapon weapon;
		public float completion;
	}
	
	public static WeaponTooltipAction create(int duration, Weapon weapon) {
		return new WeaponTooltipAction(duration, weapon);
	}
	
	private WeaponTooltipTransition transition;

	public WeaponTooltipAction(int duration, Weapon weapon) {
		super(duration);
		transition = new WeaponTooltipTransition();
		transition.weapon = weapon;
	}
	
	@Override
	public void onTrigger() {
		transition.completion = 0.0f;
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		transition.completion = (float)after / (float)getDuration();
		if (transition.completion > 1.0) {
			transition.completion = 1.0f;
		}
	}
	
	@Override
	public void onEnd() {
		transition = null;
	}

	public WeaponTooltipTransition getTransition() {
		return transition;
	}

}
