package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents a mine explosion action.
 * <p>
 * Do not mix it with the ExplosionAction which
 * is used for representing explosions of walls destroyed with dynamite.
 * <p>
 * This is a fixed length action.
 * <p>
 * At the beginning, the explosion sound is played and an overhead notification is
 * shown displaying the damage.
 * <p>
 * Then, during the whole duration of the action, an explosion transition rate is being updated in a linear fashion.
 * <p>
 * The transition is used to render the explosion effect on the screen.
 * <p>
 * At the end, the transition is set to null and the player's damage is calculated.
 * <p>
 * Take note: There is no logic to kill the player off in case his live was already low. That's because
 * the death is handled in the player's life listener which is called with every change of the hit points.
 */
public class MineExplosionAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	/**
	 * Represents a transition that is used to render the explosion effect on the screen.
	 * <p>
	 * The position is nominated in tile coordinates.
	 * <p>
	 * Take note: This class is also used by the ExplosionAction class.
	 */
	public static class MineExplosionTransition {

		/**
		 * Horizontal position of the explosion in tile coordinates.
		 */
		public int positionX;

		/**
		 * Vertical position of the explosion in tile coordinates.
		 */
		public int positionY;

		/**
		 * Completion rate of the explosion effect.
		 * <p>
		 * The rate is in the interval 0..1.
		 */
		public float percentage = 0.0f;

	}

	/**
	 * Transition that is used to render the explosion effect on the screen.
	 */
	private MineExplosionTransition transition;

	/**
	 * Creates a new action with the given duration.
	 * <p>
	 * Initializes a transition with the position as of player's tile coordinates.
	 * This transition will be used to render the mine explosion.
	 *
	 * @param duration Duration of the action in milliseconds.
	 */
	public MineExplosionAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.transition = new MineExplosionTransition();
		this.transition.positionX = player.getPositionX();
		this.transition.positionY = player.getPositionY();
	}

	/**
	 * Returns the mine explosion transition for rendering.
	 * The renderer will check whether an action of this type is registered.
	 * If yes, it will extract the transition and render the explosion at the given coordinates.
	 *
	 * @return The mine explosion transition.
	 */
	public MineExplosionTransition getTransition() {
		return transition;
	}

	/**
	 * Initializes the action by playing the explosion sound and registering an
	 * overhead notification about the mine and the damage.
	 */
	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		OverheadNotificationAction.registerOverheadNotification(player, "You stepped on a mine.");
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> " + String.valueOf(Mine.DAMAGE_TO_PLAYER));
	}

	/**
	 * Updates the completion rate of the transition for the explosion effect.
	 * <p>
	 * The completion rate is updated in a linear fashion and is in the interval 0..1.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		getTransition().percentage = actionPercentage;
	}

	/**
	 * Cleans up the transition after the action has ended so the renderer does not render the explosion anymore.
	 * <p>
	 * Then does the damage to the player. If he is in the car, the car is damage, and potentially destroyed.
	 * Otherwise, the player's life is damaged. (His potential death is handled in the life listener, not here.)
	 * <p>
	 * Take note: player's damage can be deactivated by toggling off the feature.
	 */
	@Override
	public void onEnd() {
		transition = null;
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - Mine.DAMAGE_TO_PLAYER);
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPositionX(), player.getPositionY()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			if (Features.getInstance().featureOn(Features.FEATURE_DAMAGE)) {
				player.decreaseLife(Mine.DAMAGE_TO_PLAYER);
			}
		}
	}
}
