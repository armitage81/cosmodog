package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;

/**
 * Renders bars for water and food.
 */
public class VitalDataInterfaceRenderer implements Renderer {


	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext context, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
				
		g.translate(context.x(), context.y());
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
		g.fillRect(0, 0, context.w(), context.h());
		g.translate(-context.x(), -context.y());
		
		context = new CenteredDrawingContext(context, 1);
		
		DrawingContext thirstDrawingContext = new TileDrawingContext(context, 1, 2, 0, 0);
		thirstDrawingContext = new CenteredDrawingContext(thirstDrawingContext, 2);
		
		DrawingContext hungerDrawingContext = new TileDrawingContext(context, 1, 2, 0, 1);
		hungerDrawingContext = new CenteredDrawingContext(hungerDrawingContext, 2);
		
		TileDrawingContext thirstLabelDrawingContext = new TileDrawingContext(thirstDrawingContext, 10, 1, 0, 0, 3, 1);
		TileDrawingContext thirstBarsDrawingContext = new TileDrawingContext(thirstDrawingContext, 10, 1, 3, 0, 7, 1);
		
		
		LetterTextRenderer.getInstance().render(gameContainer, g, thirstLabelDrawingContext, LetterTextRenderingParameter.fromText("WATER"));
		
		
		
		
		
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
			ApplicationContext.instance().getAnimations().get("waterBar").draw(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
			g.drawRect(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
			
			g.translate(-thirstBarDrawingContext.x(), -thirstBarDrawingContext.y());
		}
		

		
		
		
		
		
		
		TileDrawingContext hungerLabelDrawingContext = new TileDrawingContext(hungerDrawingContext, 10, 1, 0, 0, 3, 1);
		TileDrawingContext hungerBarsDrawingContext = new TileDrawingContext(hungerDrawingContext, 10, 1, 3, 0, 7, 1);
		
		LetterTextRenderer.getInstance().render(gameContainer, g, hungerLabelDrawingContext, LetterTextRenderingParameter.fromText("FOOD"));
		
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
			ApplicationContext.instance().getAnimations().get("foodBar").draw(0, 0, currentFoodBarWidth, hungerBarDrawingContext.h());
			g.setColor(Color.black);
			g.setLineWidth(2);
			g.drawRect(0, 0, maxFoodBarWidth, hungerBarDrawingContext.h());
			g.drawRect(0, 0, currentFoodBarWidth, hungerBarDrawingContext.h());
			
			g.translate(-hungerBarDrawingContext.x(), -hungerBarDrawingContext.y());
		}
		

		
		
		
	}

	
}
