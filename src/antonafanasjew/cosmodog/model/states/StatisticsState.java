package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
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
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class StatisticsState extends CosmodogAbstractState {

	@Override
	public void everyEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.getInput().clearKeyPressedRecord();
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
		String playTimeRepresentation = cosmodogGame.getTimer().playTimeRepresentationDaysHoursMinutesSeconds("%s D %s H %s M %s S");
		
		
		
		DrawingContext dc = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());

		DrawingContext topContainerDrawingContext = new TileDrawingContext(dc, 1, 10, 0, 0);
		
		DrawingContext centerContainerDrawingContext = new TileDrawingContext(dc, 1, 10, 0, 1, 1, 8);
		centerContainerDrawingContext = new TileDrawingContext(centerContainerDrawingContext, 3, 1, 1, 0);
		
		FontRefToFontTypeMap fontTypeMainHeader = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.MainHeader);
		FontRefToFontTypeMap fontTypeControlsHint = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
		
		Book statistics = TextPageConstraints.fromDc(topContainerDrawingContext).textToBook("Statistics", fontTypeMainHeader);
		TextBookRendererUtils.renderCenteredLabel(gc, g, statistics);
		
		DrawingContext scoreDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 0);
		renderLabelAndValue(gc, g, scoreDc, "Score", String.valueOf(score));
		
		DrawingContext infobitsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 1);
		renderLabelAndValue(gc, g, infobitsDc, "Found infobits", String.valueOf(noInfobits) + "/" + String.valueOf(maxInfobits));
		
		DrawingContext softwareDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 2);
		renderLabelAndValue(gc, g, softwareDc, "Found software pieces", String.valueOf(noSoftware) + "/" + String.valueOf(maxSoftware));
		
		DrawingContext armorDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 3);
		renderLabelAndValue(gc, g, armorDc, "Found armor plates", String.valueOf(noArmor) + "/" + String.valueOf(maxArmor));
		
		DrawingContext soulEssencesDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 4);
		renderLabelAndValue(gc, g, soulEssencesDc, "Found soul essences", String.valueOf(noSoulEssenses) + "/" + String.valueOf(maxSoulEssenses));
		
		DrawingContext chartsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 5);
		renderLabelAndValue(gc, g, chartsDc, "Found map pieces", String.valueOf(noCharts) + "/" + String.valueOf(maxMaps));
		
		DrawingContext artifactsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 6);
		renderLabelAndValue(gc, g, artifactsDc, "Found monoliths", String.valueOf(noInsights) + "/" + String.valueOf(maxInsights));
		
		DrawingContext secretsDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 7);
		renderLabelAndValue(gc, g, secretsDc, "Found secrets", String.valueOf(noSecrets) + "/" + String.valueOf(maxSecrets));
		
		DrawingContext enemiesDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 8);
		renderLabelAndValue(gc, g, enemiesDc, "Enemies left", String.valueOf(enemiesLeft));
		
		DrawingContext playTimeDc = new TileDrawingContext(centerContainerDrawingContext, 1, 10, 0, 9);
		renderLabelAndValue(gc, g, playTimeDc, "Play time", playTimeRepresentation);
		
		DrawingContext bottomContainerDrawingContext = new TileDrawingContext(dc, 1, 10, 0, 9, 1, 1);
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			Book controlHint = TextPageConstraints.fromDc(bottomContainerDrawingContext).textToBook("Press [ENTER]", fontTypeControlsHint);
			TextBookRendererUtils.renderCenteredLabel(gc, g, controlHint);
		}
		
	}

	private void renderLabelAndValue(GameContainer gc, Graphics g, DrawingContext dc, String label, String value) {
		FontRefToFontTypeMap fontTypeSubHeader = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.SubHeader);
		FontRefToFontTypeMap fontTypeInformational = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Informational);
		DrawingContext labelDc = new TileDrawingContext(dc, 1, 2, 0, 0);
		DrawingContext valueDc = new TileDrawingContext(dc, 1, 2, 1, 0);
		Book labelBook = TextPageConstraints.fromDc(labelDc).textToBook(label, fontTypeSubHeader);
		Book valueBook = TextPageConstraints.fromDc(valueDc).textToBook(value, fontTypeInformational);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, labelBook);
		TextBookRendererUtils.renderVerticallyCenteredLabel(gc, g, valueBook);
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

}
