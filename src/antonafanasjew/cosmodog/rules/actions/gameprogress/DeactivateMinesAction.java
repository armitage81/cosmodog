package antonafanasjew.cosmodog.rules.actions.gameprogress;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.caching.PiecePredicates;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class DeactivateMinesAction extends AbstractRuleAction {

	@Serial
	private static final long serialVersionUID = 8620572862854412890L;

	private final QuadrandType quadrandType;

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

		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CONSOLE).play();
		
		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		gameProgress.getMinesDeactivationInfo().put(quadrandType, Boolean.TRUE);

		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();

		for (MapDescriptor mapDescriptor : mapDescriptors.values()) {


			CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(mapDescriptor);
			Collection<DynamicPiece> mines = map
					.getMapPieces()
					.piecesOverall(PiecePredicates.MINE)
					.stream()
					.map(e -> (DynamicPiece)e)
					.collect(Collectors.toSet());

			for (DynamicPiece piece : mines) {
				Mine mine = (Mine) piece;
				if (inQuadrand(mine, quadrandType, map)) {
					if (mine.getState() != Mine.STATE_DESTROYED) {
						mine.setState(Mine.STATE_DEACTIVATED);
					}
				}
			}

		}

	}

	private boolean inQuadrand(Mine mine, QuadrandType quadrandType, CosmodogMap map) {
		int posX = (int)mine.getPosition().getX();
		int posY = (int)mine.getPosition().getY();
		return posX >= minX && posX < maxX && posY >= minY && posY < maxY;
	}

	private void initIfNecessary() {

		if (!initialized) {

			int mapWidht = ApplicationContextUtils.mapDescriptorMain().getWidth();
			int mapHeight = ApplicationContextUtils.mapDescriptorMain().getHeight();

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
