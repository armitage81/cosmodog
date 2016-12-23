package antonafanasjew.cosmodog.rules.actions.gameprogress;

import java.util.Collection;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class DeactivateMinesAction extends AbstractRuleAction {

	private static final long serialVersionUID = 8620572862854412890L;
	private QuadrandType quadrandType;

	private int minX;
	private int maxX;
	private int minY;
	private int maxY;

	private boolean initialized = false;

	public DeactivateMinesAction(QuadrandType quadrandType) {
		this.quadrandType = quadrandType;
	}

	@Override
	public void execute(GameEvent event) {

		initIfNecessary();

		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		gameProgress.getMinesDeactivationInfo().put(quadrandType, Boolean.TRUE);

		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Collection<DynamicPiece> mines = map.getDynamicPieces().get(Mine.class);

		for (DynamicPiece piece : mines) {
			Mine mine = (Mine) piece;
			if (inQuadrand(mine, quadrandType, map)) {
				if (mine.getState() != Mine.STATE_DESTROYED) {
					mine.setState(Mine.STATE_DEACTIVATED);
				}
			}
		}

	}

	private boolean inQuadrand(Mine mine, QuadrandType quadrandType, CosmodogMap map) {
		int posX = mine.getPositionX();
		int posY = mine.getPositionY();
		return posX >= minX && posX < maxX && posY >= minY && posY < maxY;
	}

	private void initIfNecessary() {

		if (!initialized) {

			CosmodogMap map = ApplicationContextUtils.getCosmodogMap();

			int mapWidht = map.getWidth();
			int mapHeight = map.getHeight();

			minX = 0;
			minY = 0;
			maxX = mapWidht;
			maxY = mapHeight;

			if (quadrandType.equals(QuadrandType.NW)) {
				maxX = mapWidht / 2;
				maxY = mapHeight / 2;
			}

			if (quadrandType.equals(QuadrandType.NE)) {
				minX = mapWidht / 2;
				maxY = mapHeight / 2;
			}

			if (quadrandType.equals(QuadrandType.SW)) {
				maxX = mapWidht / 2;
				minY = mapHeight / 2;
			}

			if (quadrandType.equals(QuadrandType.SE)) {
				minX = mapWidht / 2;
				minY = mapHeight / 2;
			}

			initialized = true;
		}
	}

}
