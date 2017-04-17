package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Locale;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

public class LifeInterfaceRenderer implements Renderer {

	private static final float LABEL_WIDTH = 70;
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext context, Object renderingParameter) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = cosmodogGame.getPlayer();
				
		g.translate(context.x(), context.y());
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
		g.fillRect(0, 0, context.w(), context.h());
		g.translate(-context.x(), -context.y());
		
		context = new CenteredDrawingContext(context, 1);

		
		
		DrawingContext lifeDrawingContext = new TileDrawingContext(context, 1, 4, 0, 0);
		lifeDrawingContext = new CenteredDrawingContext(lifeDrawingContext, 2);
		
		DrawingContext robustnessDrawingContext = new TileDrawingContext(context, 1, 4, 0, 1);
		robustnessDrawingContext = new CenteredDrawingContext(robustnessDrawingContext, 2);
		
		DrawingContext fuelDrawingContext = new TileDrawingContext(context, 1, 4, 0, 2);
		fuelDrawingContext = new CenteredDrawingContext(fuelDrawingContext, 2);
		
		DrawingContext environmentDataDrawingContext = new TileDrawingContext(context, 1, 4, 0, 3);
		environmentDataDrawingContext = new CenteredDrawingContext(environmentDataDrawingContext, 2);
		
		
		
		//RENDERING LIFE ROW
		
		
		
		SimpleDrawingContext lifeLabelDrawingContext = new SimpleDrawingContext(lifeDrawingContext, 0, 0, LABEL_WIDTH, lifeDrawingContext.h());
		SimpleDrawingContext lifeBarDrawingContext = new SimpleDrawingContext(lifeDrawingContext, LABEL_WIDTH, 0, lifeDrawingContext.w() - LABEL_WIDTH, lifeDrawingContext.h());

		LetterTextRenderer.getInstance().render(gameContainer, g, lifeLabelDrawingContext, LetterTextRenderingParameter.fromText("LIFE"));
				
		
		g.translate(lifeBarDrawingContext.x(), lifeBarDrawingContext.y());
		
		float potentialMaxLife = player.getMaxLife();
		float potentialLife = player.getLife();
		
		float lifeLentForHunger = player.getLifeLentForHunger();
		float lifeLentForThirst = player.getLifeLentForThirst();
		float lifeLentForFrost = player.getLifeLentForFrost();
		
		float maxLifeBarWidth = lifeBarDrawingContext.w() * potentialMaxLife / Player.MAX_POSSIBLE_LIFE;
		float currentLifeBarWidth = potentialLife / potentialMaxLife * maxLifeBarWidth;
		float oneLifeUnitBarWidth = maxLifeBarWidth / potentialMaxLife;

		float hungerLentBarWidth = lifeBarDrawingContext.w() * lifeLentForHunger / Player.MAX_POSSIBLE_LIFE;
		float thirstLentBarWidth = lifeBarDrawingContext.w() * lifeLentForThirst / Player.MAX_POSSIBLE_LIFE;
		float frostLentBarWidth = lifeBarDrawingContext.w() * lifeLentForFrost / Player.MAX_POSSIBLE_LIFE;
		
		g.setColor(Color.gray);
		g.fillRect(0, 0, maxLifeBarWidth, lifeBarDrawingContext.h());
		ApplicationContext.instance().getAnimations().get("lifeBar").draw(0, 0, currentLifeBarWidth, lifeBarDrawingContext.h());
		
		float thirstLentBarOffset = currentLifeBarWidth - frostLentBarWidth - hungerLentBarWidth - thirstLentBarWidth ;
		float hungerLentBarOffset = thirstLentBarOffset + thirstLentBarWidth;
		float frostLentBarOffset = hungerLentBarOffset + hungerLentBarWidth;
		
		g.setColor(Color.blue);
		g.fillRect(thirstLentBarOffset, 0, thirstLentBarWidth, lifeBarDrawingContext.h());
		
		g.setColor(Color.green);
		g.fillRect(hungerLentBarOffset, 0, hungerLentBarWidth, lifeBarDrawingContext.h());
		
		g.setColor(Color.cyan);
		g.fillRect(frostLentBarOffset, 0, frostLentBarWidth, lifeBarDrawingContext.h());
		
		g.setColor(Color.black);
		g.setLineWidth(2);
		g.drawRect(0, 0, currentLifeBarWidth, lifeBarDrawingContext.h());
		
		g.setColor(new Color(100, 96, 31, 0.10f));
		g.setLineWidth(1);
		for (int i = 1; i < potentialMaxLife; i++) {
			float lineOffsetX = oneLifeUnitBarWidth * i;
			g.drawLine(lineOffsetX, 0, lineOffsetX, lifeBarDrawingContext.h());
		}
		
		g.setColor(Color.black);
		g.setLineWidth(2);
		g.drawRect(0, 0, maxLifeBarWidth, lifeBarDrawingContext.h());

		g.translate(-lifeBarDrawingContext.x(), -lifeBarDrawingContext.y());
		
		
		
		//RENDERING ROBUSTNESS ROW
		
		
		
		VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
		
		if (vehicleInventoryItem != null) {
		
			Vehicle vehicle = vehicleInventoryItem.getVehicle();
			
			SimpleDrawingContext robustnessLabelDrawingContext = new SimpleDrawingContext(robustnessDrawingContext, 0, 0, LABEL_WIDTH, robustnessDrawingContext.h());
			SimpleDrawingContext robustnessBarDrawingContext = new SimpleDrawingContext(robustnessDrawingContext, LABEL_WIDTH, 0, robustnessDrawingContext.w() - LABEL_WIDTH, robustnessDrawingContext.h());
			
			LetterTextRenderer.getInstance().render(gameContainer, g, robustnessLabelDrawingContext, LetterTextRenderingParameter.fromText("ARMOR"));
			
			
			g.translate(robustnessBarDrawingContext.x(), robustnessBarDrawingContext.y());
			
			float maxRobustness = vehicle.getActualMaxLife();
			float maxRobustnessBarWidth = robustnessBarDrawingContext.w() * maxRobustness / Vehicle.MAX_POSSIBLE_LIFE;
			float currentRobustness = vehicle.getActualLife();
			float currentRobustnessBarWidth = currentRobustness / maxRobustness * maxRobustnessBarWidth;
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxRobustnessBarWidth, robustnessBarDrawingContext.h());
			ApplicationContext.instance().getAnimations().get("robustnessBar").draw(0, 0, currentRobustnessBarWidth, robustnessBarDrawingContext.h());
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, currentRobustnessBarWidth, robustnessBarDrawingContext.h());
			
			g.setColor(new Color(100, 96, 31, 0.10f));
			g.setLineWidth(1);
			for (int i = 1; i < maxRobustness; i++) {
				float lineOffsetX = oneLifeUnitBarWidth * i;
				g.drawLine(lineOffsetX, 0, lineOffsetX, robustnessBarDrawingContext.h());
			}
			
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxRobustnessBarWidth, robustnessBarDrawingContext.h());
			
			
			g.translate(-robustnessBarDrawingContext.x(), -robustnessBarDrawingContext.y());
		
		}
		
		
		//RENDERING FUEL ROW
		
		if (vehicleInventoryItem != null) {
		
			Vehicle vehicle = vehicleInventoryItem.getVehicle();
			
			SimpleDrawingContext fuelLabelDrawingContext = new SimpleDrawingContext(fuelDrawingContext, 0, 0, LABEL_WIDTH, fuelDrawingContext.h());
			SimpleDrawingContext fuelBarDrawingContext = new SimpleDrawingContext(fuelDrawingContext, LABEL_WIDTH, 0, fuelDrawingContext.w() - LABEL_WIDTH, fuelDrawingContext.h());
			
			LetterTextRenderer.getInstance().render(gameContainer, g, fuelLabelDrawingContext, LetterTextRenderingParameter.fromText("FUEL"));
			
			
			g.translate(fuelBarDrawingContext.x(), fuelBarDrawingContext.y());
			
			float maxFuel = Vehicle.MAX_FUEL;
			float maxFuelBarWidth = fuelBarDrawingContext.w();
			float currentFuel = vehicle.getFuel();
			float currentFuelBarWidth = currentFuel / maxFuel * maxFuelBarWidth;
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxFuelBarWidth, fuelBarDrawingContext.h());
			
			ApplicationContext.instance().getAnimations().get("fuelBar").draw(0, 0, currentFuelBarWidth, fuelBarDrawingContext.h());
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, currentFuelBarWidth, fuelBarDrawingContext.h());
			
			g.setColor(new Color(100, 96, 31, 0.10f));
			g.setLineWidth(1);
			for (int i = 1; i < maxFuel; i++) {
				float lineOffsetX = oneLifeUnitBarWidth * i;
				g.drawLine(lineOffsetX, 0, lineOffsetX, fuelBarDrawingContext.h());
			}
			
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxFuelBarWidth, fuelBarDrawingContext.h());
			
			
			g.translate(-fuelBarDrawingContext.x(), -fuelBarDrawingContext.y());
		
		}
		
		
		
		//RENDERING ENVIRONMENT DATA ROW
		
		
		
		TileDrawingContext radiationLabelDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 0, 0);
		TileDrawingContext radiationValueDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 1, 0);
		TileDrawingContext supplyTrackerLabelDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 2, 0);
		TileDrawingContext supplyTrackerValueDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 3, 0);
		TileDrawingContext planetaryCalendarLabelDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 4, 0);
		TileDrawingContext planetaryCalendarValueDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 5, 0);
		
		LetterTextRenderer.getInstance().render(gameContainer, g, radiationLabelDrawingContext, LetterTextRenderingParameter.fromText("RAD"));
		
		String radiationValue;
		GeigerZaehlerInventoryItem geigerZaehler = (GeigerZaehlerInventoryItem)player.getInventory().get(InventoryItemType.GEIGERZAEHLER);
		if (geigerZaehler == null) {
			radiationValue = "--";
		} else {
			int posX = player.getPositionX();
			int posY = player.getPositionY();
			int radiationTiles = 0;
			for (int i = posX - 1; i <= posX + 1; i++) {
				for (int j = posY - 1; j <= posY + 1; j++) {
					int radiationTileId = map.getTileId(i, j, Layers.LAYER_META_RADIATION);
					if (TileType.RADIATION.getTileId() == radiationTileId) {
						radiationTiles++;
					}
				}
			}
			radiationValue = String.valueOf(radiationTiles);
		}
		
		LetterTextRenderer.getInstance().render(gameContainer, g, radiationValueDrawingContext, LetterTextRenderingParameter.fromText(radiationValue));
		
		
		LetterTextRenderer.getInstance().render(gameContainer, g, supplyTrackerLabelDrawingContext, LetterTextRenderingParameter.fromText("BOX"));

		String supplyTrackerValue = "--";
		
		SupplyTrackerInventoryItem supplyTracker = (SupplyTrackerInventoryItem)player.getInventory().get(InventoryItemType.SUPPLYTRACKER);
		
		if (supplyTracker != null) {
			
			Piece closestSupply = PlayerMovementCache.getInstance().getClosestSupply();
			
			if (closestSupply != null) {
				DirectionType dirType = PositionUtils.targetDirection(player, closestSupply);
				supplyTrackerValue = dirType.getRepresentation().toUpperCase();
			}
			
		}		
	
		LetterTextRenderer.getInstance().render(gameContainer, g, supplyTrackerValueDrawingContext, LetterTextRenderingParameter.fromText(supplyTrackerValue));
		
		LetterTextRenderer.getInstance().render(gameContainer, g, planetaryCalendarLabelDrawingContext, LetterTextRenderingParameter.fromText("TIME:"));
		LetterTextRenderer.getInstance().render(gameContainer, g, planetaryCalendarValueDrawingContext, LetterTextRenderingParameter.fromText(cosmodogGame.getPlanetaryCalendar().toTimeString(Locale.getDefault())));
		
	}
	
}
