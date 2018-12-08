package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

/**
 * Renders bars for water and food.
 */
public class VitalDataInterfaceRenderer implements Renderer {

	private static final float LABEL_WIDTH = 70;

	@Override
	public void render(GameContainer gameContainer, Graphics g, Object renderingParameter) {

		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE) == false) {
			return;
		}
		
		DrawingContext vitalDataDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().vitalDataDrawingContext();
		
		long timestamp = System.currentTimeMillis();
		boolean flick = (timestamp / Constants.FLICKING_RATE_IN_MILLIS) % 2 == 0;
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
				
		g.translate(vitalDataDrawingContext.x(), vitalDataDrawingContext.y());
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
		g.fillRect(0, 0, vitalDataDrawingContext.w(), vitalDataDrawingContext.h());
		g.translate(-vitalDataDrawingContext.x(), -vitalDataDrawingContext.y());
		
		vitalDataDrawingContext = new CenteredDrawingContext(vitalDataDrawingContext, 1);
		
		DrawingContext thirstDrawingContext = new TileDrawingContext(vitalDataDrawingContext, 1, 2, 0, 0);
		thirstDrawingContext = new CenteredDrawingContext(thirstDrawingContext, 2);
		
		DrawingContext hungerDrawingContext = new TileDrawingContext(vitalDataDrawingContext, 1, 2, 0, 1);
		hungerDrawingContext = new CenteredDrawingContext(hungerDrawingContext, 2);
		
		SimpleDrawingContext thirstLabelDrawingContext = new SimpleDrawingContext(thirstDrawingContext, 0, 0, LABEL_WIDTH, thirstDrawingContext.h());
		SimpleDrawingContext thirstBarsDrawingContext = new SimpleDrawingContext(thirstDrawingContext, LABEL_WIDTH, 0, thirstDrawingContext.w() - LABEL_WIDTH, thirstDrawingContext.h());
		
		
		TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, g, thirstLabelDrawingContext, "WATER", FontType.Hud, 0);
		
		
		
		
		
		int noOfWaterBars = player.getCurrentMaxWater() / Player.INITIAL_MAX_WATER;
		int noOfFullWaterBars = player.getWater() / Player.INITIAL_MAX_WATER;
		int waterInLastBar = player.getWater() % Player.INITIAL_MAX_WATER; 
		
		for (int i = 0; i < noOfWaterBars; i++) {
			DrawingContext thirstBarDrawingContext = new TileDrawingContext(thirstBarsDrawingContext, 1, noOfWaterBars, 0, i);
			float maxWaterBarWidth = thirstBarDrawingContext.w();
			float currentWaterBarWidth;
			if (i < noOfFullWaterBars) {
				currentWaterBarWidth = maxWaterBarWidth;
			} else if (i > noOfFullWaterBars) {
				currentWaterBarWidth = 0f;
			} else {
				currentWaterBarWidth = (float)waterInLastBar / (float)Player.INITIAL_MAX_WATER * maxWaterBarWidth;
			}
		
			g.translate(thirstBarDrawingContext.x(), thirstBarDrawingContext.y());
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
			
			boolean waterBarLow = ((float)player.getWater()) / player.getCurrentMaxWater() <= Constants.FLICKING_THRESHOLD;
			 
			if (flick || !waterBarLow) {
				ApplicationContext.instance().getAnimations().get("waterBar").draw(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
			}
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
			g.drawRect(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
			
			g.translate(-thirstBarDrawingContext.x(), -thirstBarDrawingContext.y());
		}
		

		
		
		
		
		
		
		SimpleDrawingContext hungerLabelDrawingContext = new SimpleDrawingContext(hungerDrawingContext, 0, 0, LABEL_WIDTH, hungerDrawingContext.h());
		SimpleDrawingContext hungerBarsDrawingContext = new SimpleDrawingContext(hungerDrawingContext, LABEL_WIDTH, 0, hungerDrawingContext.w() - LABEL_WIDTH, hungerDrawingContext.h());
		
		
		TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, g, hungerLabelDrawingContext, "FOOD", FontType.Hud, 0);
		
		int noOfFoodBars = player.getCurrentMaxFood() / Player.INITIAL_MAX_FOOD;
		int noOfFullFoodBars = player.getFood() / Player.INITIAL_MAX_FOOD;
		int foodInLastBar = player.getFood() % Player.INITIAL_MAX_FOOD; 
		
		for (int i = 0; i < noOfFoodBars; i++) {
			DrawingContext hungerBarDrawingContext = new TileDrawingContext(hungerBarsDrawingContext, 1, noOfFoodBars, 0, i);
			float maxFoodBarWidth = hungerBarDrawingContext.w();
			float currentFoodBarWidth;
			if (i < noOfFullFoodBars) {
				currentFoodBarWidth = maxFoodBarWidth;
			} else if (i > noOfFullFoodBars) {
				currentFoodBarWidth = 0f;
			} else {
				currentFoodBarWidth = (float)foodInLastBar / (float)Player.INITIAL_MAX_FOOD * maxFoodBarWidth;
			}
		
			g.translate(hungerBarDrawingContext.x(), hungerBarDrawingContext.y());
			
			g.setColor(Color.gray);
			g.fillRect(0, 0, maxFoodBarWidth, hungerBarDrawingContext.h());
			
			boolean foodBarLow = ((float)player.getFood()) / player.getCurrentMaxFood() <= Constants.FLICKING_THRESHOLD;
			 
			if (flick || !foodBarLow) {
				ApplicationContext.instance().getAnimations().get("foodBar").draw(0, 0, currentFoodBarWidth, hungerBarDrawingContext.h());
			}
			
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxFoodBarWidth, hungerBarDrawingContext.h());
			g.drawRect(0, 0, currentFoodBarWidth, hungerBarDrawingContext.h());
			
			g.translate(-hungerBarDrawingContext.x(), -hungerBarDrawingContext.y());
		}
		

		
		
		
	}

	
}
