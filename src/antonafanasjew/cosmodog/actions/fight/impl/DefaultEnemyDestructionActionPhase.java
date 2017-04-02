package antonafanasjew.cosmodog.actions.fight.impl;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.fight.EnemyDestructionActionPhase;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DroppedCollectibleFactory;
import antonafanasjew.cosmodog.view.transitions.EnemyDestructionFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.DefaultEnemyDestructionFightPhaseTransition;

public class DefaultEnemyDestructionActionPhase extends EnemyDestructionActionPhase {

	private static final long serialVersionUID = -3853130683025678558L;
	
	public DefaultEnemyDestructionActionPhase(Player player, Enemy enemy) {
		super(Constants.ENEMY_DESTRUCTION_ACTION_DURATION, player, enemy);
	}

	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		EnemyDestructionFightPhaseTransition fightPhaseTransition = new DefaultEnemyDestructionFightPhaseTransition();
		fightPhaseTransition.setPlayer(getPlayer());
		fightPhaseTransition.setEnemy(getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		updateCompletion(before, after, gc, sbg);
	}
	
	@Override
	public void onEnd() {
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		
		Enemy enemy = getEnemy();
		
		InventoryItem item = enemy.getInventoryItem();
		
		if (item != null) {
			Collectible dropped = DroppedCollectibleFactory.createCollectibleFromDroppedItem(item);
			if (dropped != null) {
				
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_DROPPED_ITEM).play();

				dropped.setPositionX(enemy.getPositionX());
				dropped.setPositionY(enemy.getPositionY());
				
				Piece piece = cosmodogMap.pieceAtTile(enemy.getPositionX(), enemy.getPositionY());
				if (piece != null && piece instanceof Collectible) {
					CollectibleComposed newCollectible = new CollectibleComposed();
					newCollectible.setPositionX(enemy.getPositionX());
					newCollectible.setPositionY(enemy.getPositionY());
					newCollectible.addElement((Collectible)piece);
					newCollectible.addElement(dropped);
					cosmodogMap.getMapPieces().remove(piece);
					dropped = newCollectible;
				}
				
				cosmodogMap.getMapPieces().add(dropped);
			}
		}
		
		cosmodogMap.getEnemies().remove(getEnemy());
		setFightPhaseTransition(null);
	}

}
