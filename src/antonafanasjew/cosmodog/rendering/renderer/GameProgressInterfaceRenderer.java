package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;

public class GameProgressInterfaceRenderer implements Renderer {


	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext context, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		
		Player player = cosmodogGame.getPlayer();
				
		g.translate(context.x(), context.y());
		g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.75f));
		g.fillRect(0, 0, context.w(), context.h());
		g.translate(-context.x(), -context.y());
		
		context = new CenteredDrawingContext(context, 1);
		
		DrawingContext cxCol0 = new TileDrawingContext(context, 2, 1, 0, 0);
		DrawingContext cxCol1 = new TileDrawingContext(context, 2, 1, 1, 0);
		
		DrawingContext cxCol0Row0 = new TileDrawingContext(cxCol0, 1, 2, 0, 0);
		DrawingContext cxCol0Row1 = new TileDrawingContext(cxCol0, 1, 2, 0, 1);
		
		DrawingContext cxCol1Row0 = new TileDrawingContext(cxCol1, 1, 2, 0, 0);
		DrawingContext cxCol1Row1 = new TileDrawingContext(cxCol1, 1, 2, 0, 1);
		
		
		DrawingContext cxCol0Row0Label = new TileDrawingContext(cxCol0Row0, 2, 1, 0, 0);
		DrawingContext cxCol0Row0Value = new TileDrawingContext(cxCol0Row0, 2, 1, 1, 0);
		
		DrawingContext cxCol0Row1Label = new TileDrawingContext(cxCol0Row1, 2, 1, 0, 0);
		DrawingContext cxCol0Row1Value = new TileDrawingContext(cxCol0Row1, 2, 1, 1, 0);
		
		DrawingContext cxCol1Row0Label = new TileDrawingContext(cxCol1Row0, 2, 1, 0, 0);
		DrawingContext cxCol1Row0Value = new TileDrawingContext(cxCol1Row0, 2, 1, 1, 0);
		
		DrawingContext cxCol1Row1Label = new TileDrawingContext(cxCol1Row1, 2, 1, 0, 0);
		DrawingContext cxCol1Row1Value = new TileDrawingContext(cxCol1Row1, 2, 1, 1, 0);
		
		
		int infobitsCollected = player.getGameProgress().getInfobits();
		int infobitsRemaining = cosmodogMap.getInfobits().size();
		int infobitsAll = infobitsCollected + infobitsRemaining;
		String infobitsValue = String.valueOf(infobitsCollected) + "/" +String.valueOf(infobitsAll);
		
		int score = player.getGameProgress().getGameScore();
		String scoreValue = String.valueOf(score);
		
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol0Row0Label, LetterTextRenderingParameter.fromText("INFOBITS"));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol0Row0Value, LetterTextRenderingParameter.fromText(infobitsValue));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol0Row1Label, LetterTextRenderingParameter.fromText("SCORE"));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol0Row1Value, LetterTextRenderingParameter.fromText(scoreValue));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol1Row0Label, LetterTextRenderingParameter.fromText("SECRETS"));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol1Row0Value, LetterTextRenderingParameter.fromText("???"));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol1Row1Label, LetterTextRenderingParameter.fromText("ARTIFACTS"));
		LetterTextRenderer.getInstance().render(gameContainer, g, cxCol1Row1Value, LetterTextRenderingParameter.fromText("???"));
		
		
//		DrawingContext thirstDrawingContext = new TileDrawingContext(context, 1, 2, 0, 0);
//		thirstDrawingContext = new CenteredDrawingContext(thirstDrawingContext, 2);
//		
//		DrawingContext hungerDrawingContext = new TileDrawingContext(context, 1, 2, 0, 1);
//		hungerDrawingContext = new CenteredDrawingContext(hungerDrawingContext, 2);
//		
//		TileDrawingContext thirstLabelDrawingContext = new TileDrawingContext(thirstDrawingContext, 10, 1, 0, 0, 3, 1);
//		TileDrawingContext thirstBarDrawingContext = new TileDrawingContext(thirstDrawingContext, 10, 1, 3, 0, 7, 1);
//		
//		
//		String thirstLabel = "WATER";
//		List<Letter> thirstLabelLetters = LetterUtils.lettersForText(thirstLabel, ApplicationContext.instance().getCharacterLetters(), ApplicationContext.instance().getCharacterLetters().get('?'));
//		List<DrawingContext> letterDrawingContexts = LetterUtils.letterLineDrawingContexts(thirstLabelLetters, 0, thirstLabelDrawingContext);
//		
//		for (int i = 0; i < thirstLabelLetters.size(); i++) {
//			DrawingContext dc = letterDrawingContexts.get(i);
//			Letter l = thirstLabelLetters.get(i);
//			g.drawImage(l.getImage(), dc.x(), dc.y());
//			
//		}
//		
//		
//		g.translate(thirstBarDrawingContext.x(), thirstBarDrawingContext.y());
//		
//		float maxWaterBarWidth = thirstBarDrawingContext.w();
//		float maxWater = Player.MAX_THIRST;
//		float currentWater = Player.MAX_THIRST - player.getThirst();
//		float currentWaterBarWidth = currentWater / maxWater * maxWaterBarWidth;
//		
//		
//		g.setColor(Color.gray);
//		g.fillRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
//		g.setColor(Color.blue);
//		g.fillRect(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
//		g.setColor(Color.black);
//		g.setLineWidth(2);
//		g.drawRect(0, 0, maxWaterBarWidth, thirstBarDrawingContext.h());
//		g.drawRect(0, 0, currentWaterBarWidth, thirstBarDrawingContext.h());
//		
//		g.translate(-thirstBarDrawingContext.x(), -thirstBarDrawingContext.y());
//		
//		
//		
//		
//		
//		TileDrawingContext hungerLabelDrawingContext = new TileDrawingContext(hungerDrawingContext, 10, 1, 0, 0, 3, 1);
//		TileDrawingContext hungerBarDrawingContext = new TileDrawingContext(hungerDrawingContext, 10, 1, 3, 0, 7, 1);
//		
//		String foodLabel = "FOOD";
//		List<Letter> foodLabelLetters = LetterUtils.lettersForText(foodLabel, ApplicationContext.instance().getCharacterLetters(), ApplicationContext.instance().getCharacterLetters().get('?'));
//		List<DrawingContext> foodLetterDrawingContexts = LetterUtils.letterLineDrawingContexts(foodLabelLetters, 0, hungerLabelDrawingContext);
//		
//		for (int i = 0; i < foodLabelLetters.size(); i++) {
//			DrawingContext dc = foodLetterDrawingContexts.get(i);
//			Letter l = foodLabelLetters.get(i);
//			g.drawImage(l.getImage(), dc.x(), dc.y());
//			
//		}
//		
//		
//		g.translate(hungerBarDrawingContext.x(), hungerBarDrawingContext.y());
//		
//		float maxFoodBarWidth = hungerBarDrawingContext.w();
//		float maxFood = Player.MAX_HUNGER;
//		float currentFood = Player.MAX_HUNGER - player.getHunger();
//		float currentFoodBarWidth = currentFood / maxFood * maxFoodBarWidth;
//		
//		g.setLineWidth(3);
//		g.setColor(Color.gray);
//		g.fillRect(0, 0, maxFoodBarWidth, hungerBarDrawingContext.h());
//		g.setColor(Color.green);
//		g.fillRect(0, 0, currentFoodBarWidth, hungerBarDrawingContext.h());
//		g.setColor(Color.black);
//		g.setLineWidth(2);
//		g.drawRect(0, 0, maxFoodBarWidth, hungerBarDrawingContext.h());
//		g.drawRect(0, 0, currentFoodBarWidth, hungerBarDrawingContext.h());
//		
//		g.translate(-hungerBarDrawingContext.x(), -hungerBarDrawingContext.y());
//		
		
		
	}

	
}
