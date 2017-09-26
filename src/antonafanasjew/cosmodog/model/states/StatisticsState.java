package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SoftwareInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import com.google.common.base.Strings;

public class StatisticsState extends BasicGameState {

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		

	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();
		
		Inventory inv = player.getInventory();
		
		SoftwareInventoryItem software = (SoftwareInventoryItem)inv.get(InventoryItemType.SOFTWARE);
		InsightInventoryItem insight = (InsightInventoryItem)inv.get(InventoryItemType.INSIGHT);
		ChartInventoryItem chart = (ChartInventoryItem)inv.get(InventoryItemType.CHART);
		
		long score = player.getGameProgress().getGameScore(); 
		int noSoftware = software == null ? 0 : software.getNumber();
		int maxSoftware = Constants.NUMBER_OF_SOFTWARE_PIECES_IN_GAME;
		int noArmor = player.getGameProgress().getArmors();
		int maxArmor = Constants.NUMBER_OF_ARMOR_PIECES_IN_GAME;
		int noSoulEssenses = player.getGameProgress().getSoulEssences();
		int maxSoulEssenses = Constants.NUMBER_OF_SOULESSENSE_PIECES_IN_GAME;
		int noCharts = chart == null ? 0 : chart.getNumber();
		int maxMaps = Constants.NUMBER_OF_CHARTS_IN_GAME;
		int noInfobits = player.getGameProgress().getInfobits();
		int maxInfobits = Constants.NUMBER_OF_INFOBITS_IN_GAME;
		int noInsights = insight == null ? 0 : insight.getNumber();
		int maxInsights = Constants.NUMBER_OF_INSIGHTS_IN_GAME;
		int noSecrets = player.getGameProgress().getNumberOfFoundSecrets();
		int maxSecrets = Constants.NUMBER_OF_SECRETS_IN_GAME;
		
		int enemiesLeft = cosmodogGame.getMap().getEnemies().size();
		
		
		
		DrawingContext dc = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());

		DrawingContext topContainerDrawingContext = new TileDrawingContext(dc, 1, 10, 0, 0);
		
		DrawingContext centerContainerDrawingContext = new TileDrawingContext(dc, 1, 10, 0, 1, 1, 8);
		centerContainerDrawingContext = new TileDrawingContext(centerContainerDrawingContext, 3, 1, 1, 0);
		
		TextBookRendererUtils.renderCenteredLabel(gc, g, topContainerDrawingContext, "Statistics", FontType.StatisticsHeader, 0);
		
		DrawingContext bottomContainerDrawingContext = new TileDrawingContext(dc, 1, 10, 0, 9, 1, 1);
		
		DrawingContext scoreDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(scoreDc, 1, 2, 0, 0), format("Score"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(scoreDc, 1, 2, 1, 0), String.valueOf(score), FontType.StatisticsLabel, 0);
		
		DrawingContext infobitsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 1);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(infobitsDc, 1, 2, 0, 0), format("Found infobits"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(infobitsDc, 1, 2, 1, 0), String.valueOf(noInfobits) + "/" + String.valueOf(maxInfobits), FontType.StatisticsLabel, 0);
		
		DrawingContext softwareDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 2);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(softwareDc, 1, 2, 0, 0), format("Found software pieces"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(softwareDc, 1, 2, 1, 0), String.valueOf(noSoftware) + "/" + String.valueOf(maxSoftware), FontType.StatisticsLabel, 0);
		
		DrawingContext armorDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 3);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(armorDc, 1, 2, 0, 0), format("Found armor plates"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(armorDc, 1, 2, 1, 0), String.valueOf(noArmor) + "/" + String.valueOf(maxArmor), FontType.StatisticsLabel, 0);
		
		DrawingContext soulEssencesDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 4);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(soulEssencesDc, 1, 2, 0, 0), format("Found soul essences"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(soulEssencesDc, 1, 2, 1, 0), String.valueOf(noSoulEssenses) + "/" + String.valueOf(maxSoulEssenses), FontType.StatisticsLabel, 0);
		
		DrawingContext chartsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 5);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(chartsDc, 1, 2, 0, 0), format("Found map pieces"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(chartsDc, 1, 2, 1, 0), String.valueOf(noCharts) + "/" + String.valueOf(maxMaps), FontType.StatisticsLabel, 0);
		
		DrawingContext artifactsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 6);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(artifactsDc, 1, 2, 0, 0), format("Found monoliths"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(artifactsDc, 1, 2, 1, 0), String.valueOf(noInsights) + "/" + String.valueOf(maxInsights), FontType.StatisticsLabel, 0);
		
		DrawingContext secretsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 7);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(secretsDc, 1, 2, 0, 0), format("Found secrets"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(secretsDc, 1, 2, 1, 0), String.valueOf(noSecrets) + "/" + String.valueOf(maxSecrets), FontType.StatisticsLabel, 0);
		
		DrawingContext enemiesDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 8);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(enemiesDc, 1, 2, 0, 0), format("Enemies left"), FontType.StatisticsLabel, 0);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, new TileDrawingContext(enemiesDc, 1, 2, 1, 0), String.valueOf(enemiesLeft), FontType.StatisticsLabel, 0);
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, bottomContainerDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			sbg.enterState(CosmodogStarter.CREDITS_STATE_ID, new FadeOutTransition(), new FadeInTransition());		
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.STATISTICS_STATE_ID;
	}

	public String format(String label) {
		return label;
	}
	
}
