package antonafanasjew.cosmodog.rendering.renderer;

import java.util.*;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.caching.PiecePredicates;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class PositionDebugInfoRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gc, Graphics g, Object renderingParameter) {

		Player player = ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer();
		
		DebuggerInventoryItem debugger = (DebuggerInventoryItem)player.getInventory().get(InventoryItemType.DEBUGGER);

		List<Position> enemiesHoldingItems = ApplicationContext.instance().getCosmodog().getCosmodogGame().mapOfPlayerLocation().allEnemies().stream().filter(e -> {
			return e.getInventoryItem() != null;
		}).map(Piece::getPosition).toList();
		
		List<Position> collectiblePositions = ApplicationContext.instance().getCosmodog().getCosmodogGame().mapOfPlayerLocation().getMapPieces().piecesOverall(PiecePredicates.ALWAYS_TRUE).stream().filter(piece -> {

			boolean tool =  piece instanceof CollectibleTool;
			boolean goodie = piece instanceof CollectibleGoodie;
			boolean supplies = goodie && ((CollectibleGoodie)piece).getGoodieType().equals(GoodieType.supplies);
			boolean cognition = goodie && ((CollectibleGoodie)piece).getGoodieType().equals(GoodieType.cognition);
			boolean weapon = piece instanceof CollectibleWeapon;
			boolean log = piece instanceof CollectibleLog;

            return tool || (goodie && !supplies && !cognition) || weapon || log;
			
		}).map(Piece::getPosition).toList();
		
		List<Position> relevantPositions = new ArrayList<>();
		relevantPositions.addAll(collectiblePositions);
		relevantPositions.addAll(enemiesHoldingItems);
				
		relevantPositions.sort(new Comparator<Position>() {

            @Override
            public int compare(Position o1, Position o2) {
                Position playerPos = player.getPosition();

                float distance1 = CosmodogMapUtils.distanceBetweenPositions(o1, playerPos);
                float distance2 = CosmodogMapUtils.distanceBetweenPositions(o2, playerPos);

                return Float.compare(distance1, distance2);
            }

        });
		
		String closestCollectiblePosition = relevantPositions.isEmpty() ? "No Collectibles" : relevantPositions.getFirst().toString();
		
		if (debugger != null) {
			if (debugger.isPositionDisplayed()) {
				String positionInfo = String.format("Pos: %s, Closest Coll: %s", player.getPosition().toString(), closestCollectiblePosition);
				DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
				FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Debug);
				Book textBook = TextPageConstraints.fromDc(dc).textToBook(positionInfo, fontRefToFontTypeMap);
				TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
			}
		}
		
	}

}
