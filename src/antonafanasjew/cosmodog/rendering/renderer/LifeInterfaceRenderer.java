package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;

public class LifeInterfaceRenderer implements Renderer {

	private AbstractRenderer defaultTextRenderer;
	private FeatureBoundRenderer hungerTextRenderer;
	private FeatureBoundRenderer thirstTextRenderer;
	private FeatureBoundRenderer infobitsTextRenderer;

	public LifeInterfaceRenderer() {
		defaultTextRenderer = new TextRenderer();
		hungerTextRenderer = new FeatureBoundRenderer(new TextRenderer(), Features.FEATURE_HUNGER);
		thirstTextRenderer = new FeatureBoundRenderer(new TextRenderer(), Features.FEATURE_THIRST);
		infobitsTextRenderer = new FeatureBoundRenderer(new TextRenderer(), Features.FEATURE_INFOBITS);
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext context, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		Player player = cosmodogGame.getPlayer();
		
		
		
//		g.translate(context.x(), context.y());
//		g.setColor(new Color(0, 0, 0, 0.5f));
//		g.fillRect(0, 0, context.w(), context.h());
//		g.translate(-context.x(), -context.y());
		
		TileDrawingContext row1Context = new TileDrawingContext(context, 1, 2, 0, 0);
		TileDrawingContext row2Context = new TileDrawingContext(context, 1, 2, 0, 1);
		
		//TileDrawingContext row3Context = new TileDrawingContext(context, 1, 5, 0, 2, 1, 3);
		
//		TileDrawingContext topPart1DrawingContext = new TileDrawingContext(row3Context, 5, 2, 0, 0);
//		TileDrawingContext topPart2DrawingContext = new TileDrawingContext(row3Context, 5, 2, 1, 0);
//		TileDrawingContext topPart3DrawingContext = new TileDrawingContext(row3Context, 5, 2, 2, 0);
//		TileDrawingContext topPart4DrawingContext = new TileDrawingContext(row3Context, 5, 2, 3, 0);
//		TileDrawingContext topPart5DrawingContext = new TileDrawingContext(row3Context, 5, 2, 4, 0);
//		
//		TileDrawingContext topPart6DrawingContext = new TileDrawingContext(row3Context, 5, 2, 0, 1);
//		TileDrawingContext topPart7DrawingContext = new TileDrawingContext(row3Context, 5, 2, 1, 1);
//		TileDrawingContext topPart8DrawingContext = new TileDrawingContext(row3Context, 5, 2, 2, 1);
//		TileDrawingContext topPart9DrawingContext = new TileDrawingContext(row3Context, 5, 2, 3, 1);
//		TileDrawingContext topPart10DrawingContext = new TileDrawingContext(row3Context, 5, 2, 4, 1);
		
//		defaultTextRenderer.render(gameContainer, g, topPart1DrawingContext, "Score");
//		defaultTextRenderer.render(gameContainer, g, topPart6DrawingContext, String.valueOf(player.getGameProgress().getGameScore()));
//
//		hungerTextRenderer.render(gameContainer, g, topPart2DrawingContext, "Food");
//		String hungerText = String.valueOf(Player.MAX_HUNGER - player.getHunger()) + "/" + String.valueOf(Player.MAX_HUNGER);
//		hungerTextRenderer.render(gameContainer, g, topPart7DrawingContext, hungerText);
//		
//		thirstTextRenderer.render(gameContainer, g, topPart3DrawingContext, "Water");
//		String thirstText = String.valueOf(Player.MAX_THIRST - player.getThirst()) + "/" + String.valueOf(Player.MAX_THIRST);
//		thirstTextRenderer.render(gameContainer, g, topPart8DrawingContext, thirstText);
//		
//		infobitsTextRenderer.render(gameContainer, g, topPart4DrawingContext, "Infobits collected");
//		String infobitsText = String.valueOf(player.getGameProgress().getInfobits()) + "/" + cosmodogMap.getInfobits().size();
//		infobitsTextRenderer.render(gameContainer, g, topPart9DrawingContext, infobitsText);
//
//		defaultTextRenderer.render(gameContainer, g, topPart5DrawingContext, "Planetary time");
//		String calendarString = cosmodogGame.getPlanetaryCalendar().toString(Locale.getDefault());
//		defaultTextRenderer.render(gameContainer, g, topPart10DrawingContext, calendarString);
		
		
		
		renderVehicleRobustness(g, player, row1Context);
		renderPlayerLife(g, player, row2Context);
		
	}

	private void renderPlayerLife(Graphics g, Player player,
			TileDrawingContext column1Row2Context) {
		DrawingContext lifePackDrawingContext = new TileDrawingContext(column1Row2Context, 1, 2, 0, 0);
		lifePackDrawingContext = new CenteredDrawingContext(lifePackDrawingContext, 1);
		
		int maxLifePacks = lifePacksFromCurrentLife(player.getMaxLife());
		int lifePacks = lifePacksFromCurrentLife(player.getLife());
		
		for (int i = 0; i < maxLifePacks; i++) {
			
			float x = i * lifePackDrawingContext.h();
			float y = 0;
			float w = lifePackDrawingContext.h(); //On purpose to make it quadratic.
			float h = lifePackDrawingContext.h();
			
			DrawingContext lifePackContext = new SimpleDrawingContext(lifePackDrawingContext, x, y, w, h);
			g.translate(lifePackContext.x(), lifePackContext.y());
			
			String animationId = i < lifePacks ? "lifePack" : "emptyLifePack";
			ApplicationContext.instance().getAnimations().get(animationId).draw(0, 0, lifePackContext.w(), lifePackContext.h());
			g.translate(-lifePackContext.x(), -lifePackContext.y());
		}
		
		DrawingContext lifeBarDrawingContext = new TileDrawingContext(column1Row2Context, 1, 2, 0, 1);
		lifeBarDrawingContext = new CenteredDrawingContext(lifeBarDrawingContext, 1);
		
		
		for (int i = 0; i < Player.LIFE_UNITS_IN_LIFEPACK; i++) {
			
			float x = i * lifeBarDrawingContext.h();
			float y = 0;
			float w = lifeBarDrawingContext.h(); //On purpose to make it quadratic.
			float h = lifeBarDrawingContext.h();
			
			DrawingContext lifeUnitContext = new SimpleDrawingContext(lifeBarDrawingContext, x, y, w, h);
			g.translate(lifeUnitContext.x(), lifeUnitContext.y());
			
			int lifeUnits = lifeUnitsFromCurrentLife(player.getLife());
			
			String animationId = i < (lifeUnits) ? "lifeUnit" : "emptyLifeUnit";
			ApplicationContext.instance().getAnimations().get(animationId).draw(0, 0, lifeUnitContext.w(), lifeUnitContext.h());
			g.translate(-lifeUnitContext.x(), -lifeUnitContext.y());
		}
	}

	private void renderVehicleRobustness(Graphics g, Player player,
			TileDrawingContext column1Row1Context) {
		if (player.getInventory().hasVehicle()) {
			
			VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItem.INVENTORY_ITEM_VEHICLE);
			Vehicle vehicle = vehicleItem.getVehicle();
		
			DrawingContext carRobustnessPackDrawingContext = new TileDrawingContext(column1Row1Context, 1, 2, 0, 0);
			carRobustnessPackDrawingContext = new CenteredDrawingContext(carRobustnessPackDrawingContext, 1);
			
			int maxCarRobustnessPacks = lifePacksFromCurrentLife(vehicle.getMaxLife());
			int carRobustnessPacks = lifePacksFromCurrentLife(vehicle.getLife());
			
			for (int i = 0; i < maxCarRobustnessPacks; i++) {
				
				float x = i * carRobustnessPackDrawingContext.h();
				float y = 0;
				float w = carRobustnessPackDrawingContext.h(); //On purpose to make it quadratic.
				float h = carRobustnessPackDrawingContext.h();
				
				DrawingContext carRobustnessPackContext = new SimpleDrawingContext(carRobustnessPackDrawingContext, x, y, w, h);
				g.translate(carRobustnessPackContext.x(), carRobustnessPackContext.y());
				
				String animationId = i < carRobustnessPacks ? "robustnessPack" : "emptyLifePack";
				ApplicationContext.instance().getAnimations().get(animationId).draw(0, 0, carRobustnessPackContext.w(), carRobustnessPackContext.h());
				g.translate(-carRobustnessPackContext.x(), -carRobustnessPackContext.y());
			}
			
			DrawingContext carRobustnessBarDrawingContext = new TileDrawingContext(column1Row1Context, 1, 2, 0, 1);
			carRobustnessBarDrawingContext = new CenteredDrawingContext(carRobustnessBarDrawingContext, 1);
			
			
			for (int i = 0; i < Player.LIFE_UNITS_IN_LIFEPACK; i++) {
				
				float x = i * carRobustnessBarDrawingContext.h();
				float y = 0;
				float w = carRobustnessBarDrawingContext.h(); //On purpose to make it quadratic.
				float h = carRobustnessBarDrawingContext.h();
				
				DrawingContext carRobustnessUnitContext = new SimpleDrawingContext(carRobustnessBarDrawingContext, x, y, w, h);
				g.translate(carRobustnessUnitContext.x(), carRobustnessUnitContext.y());
				
				int lifeUnits = lifeUnitsFromCurrentLife(vehicle.getLife());
				
				String animationId = i < (lifeUnits) ? "robustnessUnit" : "emptyRobustnessUnit";
				ApplicationContext.instance().getAnimations().get(animationId).draw(0, 0, carRobustnessUnitContext.w(), carRobustnessUnitContext.h());
				g.translate(-carRobustnessUnitContext.x(), -carRobustnessUnitContext.y());
			}
		
		
		
		}
	}


	private int lifePacksFromCurrentLife(int currentLife) {
		
		if (currentLife == 0) {
			return 0;
		}
		
		return (currentLife - 1) / Player.LIFE_UNITS_IN_LIFEPACK;
	}
	
	private int lifeUnitsFromCurrentLife(int currentLife) {
		if (currentLife == 0) {
			return 0;
		}
		
		int retVal = currentLife % Player.LIFE_UNITS_IN_LIFEPACK;
		
		if (retVal == 0) {
			retVal = 10;
		}
		
		return retVal;
	}
	
}
