package antonafanasjew.cosmodog.actions.fight;


import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.UnitType;
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

import java.io.Serial;

public class DefaultEnemyDestructionActionPhase extends EnemyDestructionActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	private static int enemyDestructionActionDuration(Enemy enemy) {
		if (enemy.getUnitType().equals(UnitType.GUARDIAN)) {
			return 8000;
		}
		return Constants.ENEMY_DESTRUCTION_ACTION_DURATION;
	}

	public DefaultEnemyDestructionActionPhase(Player player, Enemy enemy) {
		super(enemyDestructionActionDuration(enemy), player, enemy);
	}

	@Override
	public void onTrigger() {
		Enemy enemy = ((Enemy)getProperties().get("enemy"));
		if (enemy.getUnitType().equals(UnitType.GUARDIAN)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_GUARDIAN_DESTROYED).play();
		} else {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		}
	}

	@Override
	public void onEnd() {
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();

		Enemy enemy = ((Enemy)getProperties().get("enemy"));
		
		InventoryItem item = enemy.getInventoryItem();
		
		if (item != null) {
			Collectible dropped = DroppedCollectibleFactory.createCollectibleFromDroppedItem(item);
			if (dropped != null) {
				
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_DROPPED_ITEM).play();

				dropped.setPosition(enemy.getPosition());

				Piece piece = cosmodogMap.pieceAtTile(enemy.getPosition());
				if (piece instanceof Collectible) {
					CollectibleComposed newCollectible = new CollectibleComposed();
					newCollectible.setPosition(enemy.getPosition());
					newCollectible.addElement((Collectible)piece);
					newCollectible.addElement(dropped);
					cosmodogMap.getMapPieces().remove(piece.getPosition());
					dropped = newCollectible;
				}
				
				cosmodogMap.getMapPieces().put(dropped.getPosition(), dropped);
			}
		}
		cosmodogMap.getEnemies().remove(enemy);
	}
}
