package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class LifeInterfaceRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext context, Object renderingParameter) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CustomTiledMap tiledMap = ApplicationContextUtils.getCustomTiledMap();
		Player player = cosmodogGame.getPlayer();
				
		g.translate(context.x(), context.y());
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
		g.fillRect(0, 0, context.w(), context.h());
		g.translate(-context.x(), -context.y());
		
		context = new CenteredDrawingContext(context, 1);

		
		
		DrawingContext lifeDrawingContext = new TileDrawingContext(context, 1, 3, 0, 0);
		lifeDrawingContext = new CenteredDrawingContext(lifeDrawingContext, 2);
		
		DrawingContext robustnessDrawingContext = new TileDrawingContext(context, 1, 3, 0, 1);
		robustnessDrawingContext = new CenteredDrawingContext(robustnessDrawingContext, 2);
		
		DrawingContext environmentDataDrawingContext = new TileDrawingContext(context, 1, 3, 0, 2);
		environmentDataDrawingContext = new CenteredDrawingContext(environmentDataDrawingContext, 2);
		
		
		
		//RENDERING LIFE ROW
		
		
		TileDrawingContext lifeLabelDrawingContext = new TileDrawingContext(lifeDrawingContext, 10, 1, 0, 0, 3, 1);
		TileDrawingContext lifeBarDrawingContext = new TileDrawingContext(lifeDrawingContext, 10, 1, 3, 0, 7, 1);

		LetterTextRenderer.getInstance().render(gameContainer, g, lifeLabelDrawingContext, LetterTextRenderingParameter.fromText("LIFE"));
				
		
		g.translate(lifeBarDrawingContext.x(), lifeBarDrawingContext.y());
		
		float maxLifeBarWidth = lifeBarDrawingContext.w() * player.getMaxLife() / Player.MAX_POSSIBLE_LIFE;
		float maxLife = player.getMaxLife();
		float currentLife = player.getLife();
		float currentLifeBarWidth = currentLife / maxLife * maxLifeBarWidth;
		float oneLifeUnitBarWidth = maxLifeBarWidth / maxLife;
		
		g.setColor(Color.gray);
		g.fillRect(0, 0, maxLifeBarWidth, lifeBarDrawingContext.h());
		ApplicationContext.instance().getAnimations().get("lifeBar").draw(0, 0, currentLifeBarWidth, lifeBarDrawingContext.h());
		g.setColor(Color.black);
		g.setLineWidth(2);
		g.drawRect(0, 0, currentLifeBarWidth, lifeBarDrawingContext.h());
		
		g.setColor(new Color(100, 96, 31, 0.10f));
		g.setLineWidth(1);
		for (int i = 1; i < player.getMaxLife(); i++) {
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
			
			TileDrawingContext robustnessLabelDrawingContext = new TileDrawingContext(robustnessDrawingContext, 10, 1, 0, 0, 3, 1);
			TileDrawingContext robustnessBarDrawingContext = new TileDrawingContext(robustnessDrawingContext, 10, 1, 3, 0, 7, 1);
			
			LetterTextRenderer.getInstance().render(gameContainer, g, robustnessLabelDrawingContext, LetterTextRenderingParameter.fromText("ARMOR"));
			
			
			g.translate(robustnessBarDrawingContext.x(), robustnessBarDrawingContext.y());
			
			float maxRobustnessBarWidth = robustnessBarDrawingContext.w() * vehicle.getMaxLife() / Vehicle.MAX_POSSIBLE_LIFE;
			float maxRobustness = vehicle.getMaxLife();
			float currentRobustness = vehicle.getLife();
			float currentRobustnessBarWidth = currentRobustness / maxRobustness * maxRobustnessBarWidth;
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxRobustnessBarWidth, robustnessBarDrawingContext.h());
			ApplicationContext.instance().getAnimations().get("robustnessBar").draw(0, 0, currentRobustnessBarWidth, robustnessBarDrawingContext.h());
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, currentRobustnessBarWidth, robustnessBarDrawingContext.h());
			
			g.setColor(new Color(100, 96, 31, 0.10f));
			g.setLineWidth(1);
			for (int i = 1; i < vehicle.getMaxLife(); i++) {
				float lineOffsetX = oneLifeUnitBarWidth * i;
				g.drawLine(lineOffsetX, 0, lineOffsetX, robustnessBarDrawingContext.h());
			}
			
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxRobustnessBarWidth, robustnessBarDrawingContext.h());
			
			
			g.translate(-robustnessBarDrawingContext.x(), -robustnessBarDrawingContext.y());
		
		}
		
		
		//RENDERING ENVIRONMENT DATA ROW
		
		
		
		TileDrawingContext radiationLabelDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 0, 0);
		TileDrawingContext radiationValueDrawingContext = new TileDrawingContext(environmentDataDrawingContext, 8, 1, 1, 0);
		
		
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
					int radiationTileId = tiledMap.getTileId(i, j, Layers.LAYER_META_RADIATION);
					if (TileType.RADIATION.getTileId() == radiationTileId) {
						radiationTiles++;
					}
				}
			}
			radiationValue = String.valueOf(radiationTiles);
		}
		
		LetterTextRenderer.getInstance().render(gameContainer, g, radiationValueDrawingContext, LetterTextRenderingParameter.fromText(radiationValue));
		
		
	}
	
}
