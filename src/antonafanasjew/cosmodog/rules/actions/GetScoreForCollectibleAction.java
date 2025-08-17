package antonafanasjew.cosmodog.rules.actions;

import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GetScoreForCollectibleAction extends AbstractRuleAction {

	private static final long serialVersionUID = 5740390670029721375L;

	@Override
	public void execute(GameEvent event) {

		GameEventPieceInteraction gameEventPieceInteraction = (GameEventPieceInteraction)event;
		
		Piece piece = gameEventPieceInteraction.getPiece();
		
		if (piece instanceof Collectible) {
		
			Collectible collectible = (Collectible)piece;
			
			Player player = ApplicationContextUtils.getPlayer();
			
			int scorePoints = 0;
			
			if (collectible instanceof CollectibleGoodie) {
				CollectibleGoodie goodie = (CollectibleGoodie)collectible; 
				scorePoints = goodie.getGoodieType().getScorePoints();
			} else if (collectible instanceof CollectibleTool) {
				CollectibleTool tool = (CollectibleTool)collectible; 
				scorePoints = tool.getToolType().getScorePoints();
			} else if (collectible instanceof CollectibleWeapon) {
				scorePoints = CollectibleWeapon.SCORE_POINTS_FOR_FOUND_WEAPON;
			} else if (collectible instanceof CollectibleAmmo) {
				scorePoints = CollectibleAmmo.SCORE_POINTS_FOR_FOUND_AMMO;
			}
			
			if (scorePoints > 0) {
				String text = "+" + scorePoints;
				OverheadNotificationAction.registerOverheadNotification(player, text);
				player.getGameProgress().addToScore(scorePoints);
			}
		}
	}

}
