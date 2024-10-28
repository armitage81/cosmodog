package antonafanasjew.cosmodog.actions.tooltip;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

import java.io.Serial;

public class WeaponTooltipAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -1790364270251650196L;

	public static WeaponTooltipAction create(int duration, Weapon weapon) {
		return new WeaponTooltipAction(duration, weapon);
	}
	
	public WeaponTooltipAction(int duration, Weapon weapon) {
		super(duration);
		this.getProperties().put("weapon", weapon);
	}
	
}
