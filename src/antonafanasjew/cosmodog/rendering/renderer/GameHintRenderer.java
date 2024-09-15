package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OnScreenNotificationAction;
import antonafanasjew.cosmodog.actions.notification.OnScreenNotificationAction.OnScreenNotificationTransition;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.*;
import java.util.stream.Collectors;

public class GameHintRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		Player player = ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer();
		PlayerMovementCache playerMovementCache = PlayerMovementCache.getInstance();

		Optional<String> resetHint = Optional.empty();

		//When in a Sokoban puzzle
		MoveableGroup moveableGroup = playerMovementCache.getActiveMoveableGroup();
		if (moveableGroup != null && moveableGroup.isResetable() && !moveableGroup.solved()) {
			resetHint = Optional.of("Press R to reset.");
		}

		if (player.getTurnsWormAlerted() > 0) {
			int turnsUntilWormAttack = player.getGameProgress().getTurnsTillWormAppears() - player.getTurnsWormAlerted();
			resetHint = Optional.of(String.format("Snow worm appears in %s turns!", turnsUntilWormAttack));
		}

		if (resetHint.isPresent()) {

			boolean evenPhase = System.currentTimeMillis() / 250 % 2 == 0;

			DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameHintDrawingContext();
			dc = new TileDrawingContext(dc, 1, 5, 0, 0);
			dc = new CenteredDrawingContext(dc, 400, 50);
			graphics.setColor(new Color(0, 0, 0, 0.75f));
			graphics.fillRect(dc.x(), dc.y(), dc.w(), dc.h());
			graphics.setColor(Color.orange);
			graphics.drawRect(dc.x(), dc.y(), dc.w(), dc.h());
			if (evenPhase) {
				FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontProvider.FontTypeName.GameHint);
				Book textBook = TextPageConstraints.fromDc(dc).textToBook(resetHint.get(), fontRefToFontTypeMap);
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
			}
		}


	}

}
