package antonafanasjew.cosmodog.rendering.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
	public void render(GameContainer gc, Graphics g, Object renderingParameter) {

		Player player = ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer();
		
		DebuggerInventoryItem debugger = (DebuggerInventoryItem)player.getInventory().get(InventoryItemType.DEBUGGER);

		List<Position> enemiesHoldingItems = ApplicationContext.instance().getCosmodog().getCosmodogGame().getMap().getEnemies().stream().filter(e -> {
			return e.getInventoryItem() != null;
		}).map(e -> Position.fromCoordinates(e.getPositionX(), e.getPositionY())).collect(Collectors.toList());
		
		List<Position> collectiblePositions = ApplicationContext.instance().getCosmodog().getCosmodogGame().getMap().getMapPieces().entrySet().stream().filter(e -> {
			Piece piece = e.getValue();
			
			boolean tool =  piece instanceof CollectibleTool;
			boolean goodie = piece instanceof CollectibleGoodie;
			boolean supplies = goodie && ((CollectibleGoodie)piece).getGoodieType().equals(GoodieType.supplies);
			boolean cognition = goodie && ((CollectibleGoodie)piece).getGoodieType().equals(GoodieType.cognition);
			boolean weapon = piece instanceof CollectibleWeapon;
			boolean log = piece instanceof CollectibleLog;
			boolean relevantCollectible = tool || (goodie && !supplies && !cognition) || weapon || log;
			
			return relevantCollectible;
			
		}).map(e -> e.getKey()).collect(Collectors.toList());
		
		List<Position> relevantPositions = new ArrayList<>();
		relevantPositions.addAll(collectiblePositions);
		relevantPositions.addAll(enemiesHoldingItems);
				
		Collections.sort(relevantPositions, new Comparator<Position>() {

			@Override
			public int compare(Position o1, Position o2) {
				Position o1Pos = Position.fromCoordinates(o1.getX(), o1.getY());
				Position o2Pos = Position.fromCoordinates(o2.getX(), o2.getY());
				Position playerPos = Position.fromCoordinates(player.getPositionX(), player.getPositionY());
				
				float distance1 = CosmodogMapUtils.distanceBetweenPositions(o1Pos, playerPos);
				float distance2 = CosmodogMapUtils.distanceBetweenPositions(o2Pos, playerPos);
				
				return distance1 > distance2 ? 1 : (distance1 < distance2 ? -1 : 0);
			}

		});
		
		String closestCollectiblePosition = relevantPositions.size() == 0 ? "No Collectibles" : relevantPositions.get(0).toString();
		
		if (debugger != null) {
			if (debugger.isPositionDisplayed()) {
				String positionInfo = String.format("Pos: %s/%s, Closest Coll: %s", player.getPositionX(), player.getPositionY(), closestCollectiblePosition);
				DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
				FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Debug);
				Book textBook = TextPageConstraints.fromDc(dc).textToBook(positionInfo, fontRefToFontTypeMap);
				TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
			}
		}
		
	}

}
