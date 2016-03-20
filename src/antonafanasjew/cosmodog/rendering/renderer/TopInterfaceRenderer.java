package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Locale;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
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

public class TopInterfaceRenderer implements Renderer {

	private AbstractRenderer defaultTextRenderer;
	private FeatureBoundRenderer hungerTextRenderer;
	private FeatureBoundRenderer thirstTextRenderer;
	private FeatureBoundRenderer infobitsTextRenderer;

	public TopInterfaceRenderer() {
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
		
		g.translate(context.x(), context.y());
		g.setColor(Color.black);
		g.fillRect(0, 0, context.w(), context.h());
		g.translate(-context.x(), -context.y());
		
		TileDrawingContext row1Context = new TileDrawingContext(context, 1, 5, 0, 0, 1, 3);
		TileDrawingContext row2Context = new TileDrawingContext(context, 1, 5, 0, 3, 1, 1);
		TileDrawingContext row3Context = new TileDrawingContext(context, 1, 5, 0, 4, 1, 1);
		
		TileDrawingContext topPart1DrawingContext = new TileDrawingContext(row1Context, 5, 2, 0, 0);
		TileDrawingContext topPart2DrawingContext = new TileDrawingContext(row1Context, 5, 2, 1, 0);
		TileDrawingContext topPart3DrawingContext = new TileDrawingContext(row1Context, 5, 2, 2, 0);
		TileDrawingContext topPart4DrawingContext = new TileDrawingContext(row1Context, 5, 2, 3, 0);
		TileDrawingContext topPart5DrawingContext = new TileDrawingContext(row1Context, 5, 2, 4, 0);
		
		TileDrawingContext topPart6DrawingContext = new TileDrawingContext(row1Context, 5, 2, 0, 1);
		TileDrawingContext topPart7DrawingContext = new TileDrawingContext(row1Context, 5, 2, 1, 1);
		TileDrawingContext topPart8DrawingContext = new TileDrawingContext(row1Context, 5, 2, 2, 1);
		TileDrawingContext topPart9DrawingContext = new TileDrawingContext(row1Context, 5, 2, 3, 1);
		TileDrawingContext topPart10DrawingContext = new TileDrawingContext(row1Context, 5, 2, 4, 1);
		
		defaultTextRenderer.render(gameContainer, g, topPart1DrawingContext, "Score");
		defaultTextRenderer.render(gameContainer, g, topPart6DrawingContext, String.valueOf(player.getGameProgress().getGameScore()));

		hungerTextRenderer.render(gameContainer, g, topPart2DrawingContext, "Food");
		String hungerText = String.valueOf(Player.MAX_HUNGER - player.getHunger()) + "/" + String.valueOf(Player.MAX_HUNGER);
		hungerTextRenderer.render(gameContainer, g, topPart7DrawingContext, hungerText);
		
		thirstTextRenderer.render(gameContainer, g, topPart3DrawingContext, "Water");
		String thirstText = String.valueOf(Player.MAX_THIRST - player.getThirst()) + "/" + String.valueOf(Player.MAX_THIRST);
		thirstTextRenderer.render(gameContainer, g, topPart8DrawingContext, thirstText);
		
		infobitsTextRenderer.render(gameContainer, g, topPart4DrawingContext, "Infobits collected");
		String infobitsText = String.valueOf(player.getGameProgress().getInfobits()) + "/" + cosmodogMap.getInfobits().size();
		infobitsTextRenderer.render(gameContainer, g, topPart9DrawingContext, infobitsText);

		defaultTextRenderer.render(gameContainer, g, topPart5DrawingContext, "Planetary time");
		String calendarString = cosmodogGame.getPlanetaryCalendar().toString(Locale.getDefault());
		defaultTextRenderer.render(gameContainer, g, topPart10DrawingContext, calendarString);
		
		CenteredDrawingContext lifeBarDrawingContext = new CenteredDrawingContext(row2Context, 1);
		
		for (int i = 0; i < player.getMaxLife(); i++) {
			
			float x = i * lifeBarDrawingContext.h();
			float y = 0;
			float w = lifeBarDrawingContext.h(); //On purpose to make it quadratic.
			float h = lifeBarDrawingContext.h();
			
			DrawingContext lifeUnitContext = new SimpleDrawingContext(lifeBarDrawingContext, x, y, w, h);
			g.translate(lifeUnitContext.x(), lifeUnitContext.y());
			String animationId = i < (player.getLife()) ? "lifeUnit" : "emptyLifeUnit";
			ApplicationContext.instance().getAnimations().get(animationId).draw(0, 0, lifeUnitContext.w(), lifeUnitContext.h());
			g.translate(-lifeUnitContext.x(), -lifeUnitContext.y());
		}
		
		
		CenteredDrawingContext carRobustnessDrawingContext = new CenteredDrawingContext(row3Context, 1);
		
		if (player.getInventory().hasVehicle()) {
			
			VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItem.INVENTORY_ITEM_VEHICLE);
			Vehicle vehicle = vehicleItem.getVehicle();
			
    		for (int i = 0; i < vehicle.getMaxLife(); i++) {
    			
    			float x = i * carRobustnessDrawingContext.h();
    			float y = 0;
    			float w = carRobustnessDrawingContext.h(); //On purpose to make it quadratic.
    			float h = carRobustnessDrawingContext.h();
    			
    			DrawingContext robustnessUnitContext = new SimpleDrawingContext(carRobustnessDrawingContext, x, y, w, h);
    			g.translate(robustnessUnitContext.x(), robustnessUnitContext.y());
    			String animationId = i < (vehicle.getLife()) ? "robustnessUnit" : "emptyRobustnessUnit";
    			ApplicationContext.instance().getAnimations().get(animationId).draw(0, 0, robustnessUnitContext.w(), robustnessUnitContext.h());
    			g.translate(-robustnessUnitContext.x(), -robustnessUnitContext.y());
    		}
		}
	}

}
