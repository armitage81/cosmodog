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
		TileDrawingContext thirstBarDrawingContext = new TileDrawingContext(thirstDrawingContext, 10, 1, 3, 0, 7, 1);
		
		
		LetterTextRenderer.getInstance().render(gameContainer, g, thirstLabelDrawingContext, LetterTextRenderingParameter.fromText("WATER"));
		
		
		g.translate(thirstBarDrawingContext.x(), thirstBarDrawingContext.y());
		
		float maxWaterBarWidth = thirstBarDrawingContext.w();
		float maxWater = Player.MAX_THIRST;
		float currentWater = Player.MAX_THIRST - player.getThirst();
		float currentWaterBarWidth = currentWater / maxWater * maxWaterBarWidth;
		
		
		g.setColor(Color.gray);
		g.fillRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
		ApplicationContext.instance().getAnimations().get("waterBar").draw(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
		g.setColor(Color.black);
		g.setLineWidth(2);
		g.drawRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
		g.drawRect(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
		
		g.translate(-thirstBarDrawingContext.x(), -thirstBarDrawingContext.y());
		
		
		
		
		
		TileDrawingContext hungerLabelDrawingContext = new TileDrawingContext(hungerDrawingContext, 10, 1, 0, 0, 3, 1);
		TileDrawingContext hungerBarDrawingContext = new TileDrawingContext(hungerDrawingContext, 10, 1, 3, 0, 7, 1);
		
		LetterTextRenderer.getInstance().render(gameContainer, g, hungerLabelDrawingContext, LetterTextRenderingParameter.fromText("FOOD"));
		
		
		g.translate(hungerBarDrawingContext.x(), hungerBarDrawingContext.y());
		
		float maxFoodBarWidth = hungerBarDrawingContext.w();
		float maxFood = Player.MAX_HUNGER;
		float currentFood = Player.MAX_HUNGER - player.getHunger();
		float currentFoodBarWidth = currentFood / maxFood * maxFoodBarWidth;
		
		g.setLineWidth(3);
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
