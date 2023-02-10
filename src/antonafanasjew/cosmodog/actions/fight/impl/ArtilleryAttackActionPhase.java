package antonafanasjew.cosmodog.actions.fight.impl;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.ExplosionAction;
import antonafanasjew.cosmodog.actions.fight.EnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.FightActionResult;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.AttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition;

public class ArtilleryAttackActionPhase extends EnemyAttackActionPhase {

	private static final long serialVersionUID = -847770758457510559L;

	
	private boolean milestone1 = false;
	private boolean milestone2 = false;
	private boolean milestone3 = false;
	private boolean milestone4 = false;
	
	public ArtilleryAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.RANGED_ENEMY_ATTACK_ACTION_DURATION, fightPhaseResult);
	}
	
	
	
	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_ARTILLERY_SHOTS).play();
		AttackingFightPhaseTransition fightPhaseTransition = new ArtilleryAttackingFightPhaseTransition();
		fightPhaseTransition.setPlayer(getFightPhaseResult().getPlayer());
		fightPhaseTransition.setEnemy(getFightPhaseResult().getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);
		getFightPhaseResult().getEnemy().setDirection(DirectionType.DOWN);
		
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		float completion = ((FightPhaseTransition)getFightPhaseTransition()).getCompletion();
		
		if (completion > 1.0) {
			completion = 1.0f;
		}
		
		boolean playSound = false;
		
		if (!milestone1 && (completion >= 0.6)) {
			playSound = true;
			milestone1 = true;
		}
		
		if (!milestone2 && (completion >= 0.7)) {
			playSound = true;
			milestone2 = true;
		}
		
		if (!milestone3 && (completion >= 0.8)) {
			playSound = true;
			milestone3 = true;
		}
		
		if (!milestone4 && (completion >= 0.9)) {
			playSound = true;
			milestone4 = true;
		}
		
		if (playSound) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		}
		
		
		updateCompletion(before, after, gc, sbg);
	}
	
	@Override
	public void onEnd() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		Player player = getFightPhaseResult().getPlayer();
		
		int damage = getFightPhaseResult().getDamage();
		
		String text = "<font:critical> " + String.valueOf(getFightPhaseResult().getDamage());
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getPlayer(), text);
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - damage);
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPositionX(), player.getPositionY()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			player.decreaseLife(damage);
		}
		
		setFightPhaseTransition(null);
	}
	
	


}
