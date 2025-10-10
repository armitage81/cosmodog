package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.fighting.Damage;
import com.google.common.collect.Lists;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;
import java.util.List;

public class ArtilleryAttackActionPhase extends EnemyAttackActionPhase {

	private static final float VISIBLE_SALVE_TIME = 0.45f;

	private static final float RISING_COMPLETION_THRESHOLD = VISIBLE_SALVE_TIME;
	private static final float FALLING_COMPLETION_THRESHOLD = 1f - VISIBLE_SALVE_TIME;

	private static final float VISIBLE_GRENADE_TIME = 0.1f;
	private static final int MAX_GRENADES = 4;

	private static final float TIME_INTERVAL_BETWEEN_GRENADES = (VISIBLE_SALVE_TIME - VISIBLE_GRENADE_TIME) / (MAX_GRENADES - 1);

	@Serial
	private static final long serialVersionUID = -847770758457510559L;

	/**
	 * Flag for the first grenade sound.
	 */
	private boolean milestone1 = false;

	/**
	 * Flag for the second grenade sound.
	 */
	private boolean milestone2 = false;

	/**
	 * Flag for the third grenade sound.
	 */
	private boolean milestone3 = false;

	/**
	 * Flag for the fourth grenade sound.
	 */
	private boolean milestone4 = false;

	public ArtilleryAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.RANGED_ENEMY_ATTACK_ACTION_DURATION, fightPhaseResult);
	}
	
	
	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_ARTILLERY_SHOTS).play();
		getFightPhaseResult().getEnemy().setDirection(DirectionType.DOWN);
	}

	@Override
	public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		boolean playSound = false;
		
		if (!milestone1 && (getCompletionRate() >= 0.6)) {
			playSound = true;
			milestone1 = true;
		}
		
		if (!milestone2 && (getCompletionRate() >= 0.7)) {
			playSound = true;
			milestone2 = true;
		}
		
		if (!milestone3 && (getCompletionRate() >= 0.8)) {
			playSound = true;
			milestone3 = true;
		}
		
		if (!milestone4 && (getCompletionRate() >= 0.9)) {
			playSound = true;
			milestone4 = true;
		}
		
		if (playSound) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		}

	}

	@Override
	public void onEnd() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = getFightPhaseResult().getPlayer();
		Damage damage = getFightPhaseResult().getDamage();
		
		String text = "<font:critical> " + String.valueOf(getFightPhaseResult().getDamage().getAmount());
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getPlayer(), text);
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - damage.getAmount());
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPosition()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			player.decreaseLife(damage.getAmount());
		}
		
	}

	public static class Grenade {
		public boolean risingNotFalling; //Describes a rising grenade if true, falling otherwise;
		public boolean leftNotRight; //Describes the grenade from the left cannon if true, right cannon otherwise.
		public float relativeHeight; //Describes the relative height. 0f means just shot or landed, 1f means reached the max height

		public String toString() {
			return "Rising:" + (risingNotFalling ? "Yes" : "No") + " Left:" + (leftNotRight ? "Yes" : "No") + " RelH:" + relativeHeight;
		}
	}


	public List<Grenade> grenades() {

		List<Grenade> retVal = Lists.newArrayList();

		float completion = getCompletionRate();

		if (completion < RISING_COMPLETION_THRESHOLD) {
			int numberOfVisibleGrenades = (int)(completion / TIME_INTERVAL_BETWEEN_GRENADES) + 1;
			numberOfVisibleGrenades = Math.min(numberOfVisibleGrenades, MAX_GRENADES);
			for (int i = 0; i < numberOfVisibleGrenades; i++) {
				Grenade grenade = new Grenade();
				grenade.leftNotRight = i % 2 == 0;
				grenade.risingNotFalling = true;
				grenade.relativeHeight = (completion - i * TIME_INTERVAL_BETWEEN_GRENADES) / VISIBLE_GRENADE_TIME;
				if (grenade.relativeHeight <= 1.0f) {
					retVal.add(grenade);
				}
			}
		} else if (completion >= FALLING_COMPLETION_THRESHOLD) {
			int numberOfVisibleGrenades = (int)((completion - FALLING_COMPLETION_THRESHOLD) / TIME_INTERVAL_BETWEEN_GRENADES) + 1;
			numberOfVisibleGrenades = Math.min(numberOfVisibleGrenades, MAX_GRENADES);
			for (int i = 0; i < numberOfVisibleGrenades; i++) {
				Grenade grenade = new Grenade();
				grenade.leftNotRight = i % 2 == 0;
				grenade.risingNotFalling = false;
				grenade.relativeHeight = 1 - ((completion - FALLING_COMPLETION_THRESHOLD - i * TIME_INTERVAL_BETWEEN_GRENADES) / VISIBLE_GRENADE_TIME);
				if (grenade.relativeHeight >= 0f) {
					retVal.add(grenade);
				}
			}
		}

		return retVal;
	}

	public boolean playerTakingDamage() {
		return getCompletionRate() >= FALLING_COMPLETION_THRESHOLD;
	}
}
