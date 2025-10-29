package antonafanasjew.cosmodog.actions.fight;


import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.caching.PiecePredicates;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.enemyinventory.EnemyInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DroppedCollectibleFactory;

import java.io.Serial;
import java.util.List;
import java.util.Set;

public class DefaultEnemyDestructionActionPhase extends EnemyDestructionActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	private static int enemyDestructionActionDuration(Set<Enemy> enemies) {
		if (enemies.size() == 1 && enemies.iterator().next().getUnitType().equals(UnitType.GUARDIAN)) {
			return 8000;
		}
		return Constants.ENEMY_DESTRUCTION_ACTION_DURATION;
	}

	public DefaultEnemyDestructionActionPhase(Player player, Set<Enemy> enemies) {
		super(enemyDestructionActionDuration(enemies), player, enemies);
	}

	@Override
	public void onTrigger() {
		Set<Enemy> enemies = ((Set<Enemy>)getProperties().get("enemies"));
		if (enemies.size() == 1 && enemies.iterator().next().getUnitType().equals(UnitType.GUARDIAN)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_GUARDIAN_DESTROYED).play();
		} else {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		}
	}

	@Override
	public void onEnd() {
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();

		Set<Enemy> enemies = ((Set<Enemy>)getProperties().get("enemies"));

		for (Enemy enemy : enemies) {

			EnemyInventoryItem item = enemy.getInventoryItem();

			if (item != null) {
				Collectible dropped = DroppedCollectibleFactory.createCollectibleFromDroppedItem(item);
				if (dropped != null) {

					ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_DROPPED_ITEM).play();

					dropped.setPosition(enemy.getPosition());

					List<Piece> collectibles = cosmodogMap
							.getMapPieces()
							.piecesAtPosition(
									PiecePredicates.COLLECTIBLE,
									enemy.getPosition().getX(),
									enemy.getPosition().getY()
							);

					if (!collectibles.isEmpty()) {
						Collectible collectible = (Collectible) collectibles.getFirst();
						CollectibleComposed newCollectible = new CollectibleComposed();
						newCollectible.setPosition(enemy.getPosition());
						newCollectible.addElement(collectible);
						newCollectible.addElement(dropped);
						cosmodogMap.getMapPieces().removePiece(collectible);
						dropped = newCollectible;
					}

					cosmodogMap.getMapPieces().addPiece(dropped);
				}
			}
			cosmodogMap.getMapPieces().removePiece(enemy);
		}
	}
}
