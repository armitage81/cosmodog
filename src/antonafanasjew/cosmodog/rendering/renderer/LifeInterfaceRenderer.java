package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class LifeInterfaceRenderer implements Renderer {

	private static final float LABEL_WIDTH = 70;
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, Object renderingParameter) {
		
		@SuppressWarnings("unused")
		DrawingContext lifeDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().lifeDrawingContext();
		
		DrawingContext humanLifeDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().humanLifeDrawingContext();
		DrawingContext robustnessDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().robustnessDrawingContext();
		DrawingContext fuelDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().fuelDrawingContext();
		
		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE) == false) {
			return;
		}
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		
		long timestamp = System.currentTimeMillis();
		boolean flick = (timestamp / Constants.FLICKING_RATE_IN_MILLIS) % 2 == 0;

		FontRefToFontTypeMap fontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.HudLabels);
		
		//RENDERING LIFE ROW
		SimpleDrawingContext lifeLabelDrawingContext = new SimpleDrawingContext(humanLifeDrawingContext, 0, 0, LABEL_WIDTH, humanLifeDrawingContext.h());
		SimpleDrawingContext lifeBarDrawingContext = new SimpleDrawingContext(humanLifeDrawingContext, LABEL_WIDTH, 0, humanLifeDrawingContext.w() - LABEL_WIDTH, humanLifeDrawingContext.h());

		Book textBook;
		
		textBook = TextPageConstraints.fromDc(lifeLabelDrawingContext).textToBook("LIFE", fontType);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, g, textBook);
		
		g.translate(lifeBarDrawingContext.x(), lifeBarDrawingContext.y());
		
		float potentialMaxLife = player.getMaxLife();
		float potentialLife = player.getLife();
		
		float lifeLentForHunger = player.getLifeLentForHunger();
		float lifeLentForThirst = player.getLifeLentForThirst();
		float lifeLentForFrost = player.getLifeLentForFrost();
		
		float maxLifeBarWidth = lifeBarDrawingContext.w() * potentialMaxLife / Player.MAX_POSSIBLE_LIFE;
		float lifeRatio = potentialLife / potentialMaxLife;
		float currentLifeBarWidth = lifeRatio * maxLifeBarWidth;
		float oneLifeUnitBarWidth = maxLifeBarWidth / potentialMaxLife;

		float hungerLentBarWidth = lifeBarDrawingContext.w() * lifeLentForHunger / Player.MAX_POSSIBLE_LIFE;
		float thirstLentBarWidth = lifeBarDrawingContext.w() * lifeLentForThirst / Player.MAX_POSSIBLE_LIFE;
		float frostLentBarWidth = lifeBarDrawingContext.w() * lifeLentForFrost / Player.MAX_POSSIBLE_LIFE;
		
		g.setColor(Color.gray);
		g.fillRect(0, 0, maxLifeBarWidth, lifeBarDrawingContext.h());
		
		boolean lifeBarLow = ((float)player.getLife() - lifeLentForHunger - lifeLentForThirst - lifeLentForFrost) / player.getMaxLife() <= Constants.FLICKING_THRESHOLD;
 
		if (flick || !lifeBarLow) {
			ApplicationContext.instance().getAnimations().get("lifeBar").draw(0, 0, currentLifeBarWidth, lifeBarDrawingContext.h());
		}
		
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
			
			textBook = TextPageConstraints.fromDc(robustnessLabelDrawingContext).textToBook("ARMOR", fontType);
			TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, g, textBook);
			
			g.translate(robustnessBarDrawingContext.x(), robustnessBarDrawingContext.y());
			
			float maxRobustness = vehicle.getActualMaxLife();
			float maxRobustnessBarWidth = robustnessBarDrawingContext.w() * maxRobustness / Vehicle.MAX_POSSIBLE_LIFE;
			float currentRobustness = vehicle.getActualLife();
			float currentRobustnessBarWidth = currentRobustness / maxRobustness * maxRobustnessBarWidth;
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxRobustnessBarWidth, robustnessBarDrawingContext.h());
			

			boolean robustnessBarLow = ((float)vehicle.getLife()) / vehicle.getMaxLife() <= Constants.FLICKING_THRESHOLD;
			 
			if (flick || !robustnessBarLow) {
				ApplicationContext.instance().getAnimations().get("robustnessBar").draw(0, 0, currentRobustnessBarWidth, robustnessBarDrawingContext.h());
			}
			
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
			
			textBook = TextPageConstraints.fromDc(fuelLabelDrawingContext).textToBook("FUEL", fontType);
			TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, g, textBook);
			
			g.translate(fuelBarDrawingContext.x(), fuelBarDrawingContext.y());

			//Max fuel can be increased with fuel tanks up to this value.
			float highestMaxFuel = Vehicle.HIGHEST_MAX_FUEL;
			float maxFuel = vehicle.getMaxFuel();
			float currentFuel = vehicle.getFuel();

			float maxFuelBarWidth = fuelBarDrawingContext.w() / highestMaxFuel * maxFuel;

			float oneFuelUnitBarWidth = maxFuelBarWidth / maxFuel;

			float currentFuelBarWidth = currentFuel * oneFuelUnitBarWidth;
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxFuelBarWidth, fuelBarDrawingContext.h());
			
			boolean fuelBarLow = ((float)vehicle.getFuel()) / vehicle.getMaxFuel() <= Constants.FLICKING_THRESHOLD;
			 
			if (flick || !fuelBarLow) {
				ApplicationContext.instance().getAnimations().get("fuelBar").draw(0, 0, currentFuelBarWidth, fuelBarDrawingContext.h());
			}
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, currentFuelBarWidth, fuelBarDrawingContext.h());
			
			g.setColor(new Color(100, 96, 31, 0.10f));
			g.setLineWidth(1);
			for (int i = 1; i < maxFuel; i++) {
				float lineOffsetX = oneFuelUnitBarWidth * i;
				g.drawLine(lineOffsetX, 0, lineOffsetX, fuelBarDrawingContext.h());
			}
			
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxFuelBarWidth, fuelBarDrawingContext.h());
			
			
			g.translate(-fuelBarDrawingContext.x(), -fuelBarDrawingContext.y());
		
		}
		
	}
	
}
