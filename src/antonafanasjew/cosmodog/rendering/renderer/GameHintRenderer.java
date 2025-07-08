package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.structures.PortalPuzzle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.*;

public class GameHintRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		boolean modalWindow = ApplicationContext.instance().getCosmodog().getCosmodogGame().getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.MODAL_WINDOW) != null;

		if (modalWindow) {
			return;
		}

		Player player = ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer();
		PlayerMovementCache playerMovementCache = PlayerMovementCache.getInstance();

		List<String> hints = new ArrayList<>();

		boolean starving = player.starving();
		boolean dehydrating = player.dehydrating();
		boolean freezing = player.getLifeLentForFrost() > 0;

		List<String> conditions = new ArrayList<>();
		if (starving) {
			conditions.add("starving");
		}
		if (dehydrating) {
			conditions.add("dehydrating");
		}
		if (freezing) {
			conditions.add("freezing");
		}
		if (!conditions.isEmpty()) {
			String conditionHint;
			if (conditions.size() == 1) {
				conditionHint = String.format("You are %s!", conditions.getFirst());
			} else if (conditions.size() == 2) {
				conditionHint = String.format("You are %s and %s!", conditions.get(0), conditions.get(1));
			} else {
				conditionHint = String.format("You are %s, %s and %s!", conditions.get(0), conditions.get(1), conditions.get(2));
			}
			hints.add(conditionHint);
		}


		boolean poisoned = player.isPoisoned();

		if (poisoned) {
			int turnsTillDeath = Constants.TURNS_BEFORE_DEATH_BY_POISON - player.getTurnsPoisoned();
			hints.add(String.format("Death by poison in %s turns!", turnsTillDeath));
		}

		//When in a Sokoban puzzle
		MoveableGroup moveableGroup = playerMovementCache.getActiveMoveableGroup();
		if (moveableGroup != null && moveableGroup.isResetable() && !moveableGroup.solved()) {
			hints.add("Press R to reset.");
		}

		//When in a portal puzzle
		PortalPuzzle portalPuzzle = playerMovementCache.getActivePortalPuzzle();
		if (portalPuzzle != null) {
			hints.add("Press R to reset.");
		}

		if (player.getTurnsWormAlerted() > 0) {
			int turnsUntilWormAttack = player.getGameProgress().getTurnsTillWormAppears() - player.getTurnsWormAlerted();
			hints.add(String.format("Snow worm appears in %s turns!", turnsUntilWormAttack));
		}

		if (!hints.isEmpty()) {

			DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameHintDrawingContext();
			dc = new TileDrawingContext(dc, 1, 5, 0, 0);
			dc = new CenteredDrawingContext(dc, 400, 50);
			graphics.setColor(new Color(0, 0, 0, 0.75f));
			graphics.fillRect(dc.x(), dc.y(), dc.w(), dc.h());
			graphics.setColor(Color.orange);
			graphics.drawRect(dc.x(), dc.y(), dc.w(), dc.h());

			long time = System.currentTimeMillis();

			int loopPhase = (int)(time / 1000) % (hints.size());

			if (time / 200 % 5 > 0) {
				String hint = hints.get(loopPhase);
				FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontProvider.FontTypeName.GameHint);
				Book textBook = TextPageConstraints.fromDc(dc).textToBook(hint, fontRefToFontTypeMap);
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
			}

		}

	}

}
