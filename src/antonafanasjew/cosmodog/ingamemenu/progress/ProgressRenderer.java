package antonafanasjew.cosmodog.ingamemenu.progress;

import org.junit.experimental.max.MaxCore;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SoftwareInventoryItem;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.QuadraticDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class ProgressRenderer implements Renderer {

	private static final int ROWS = 8;
	private static final int ROW_PADDING = 10;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		Animation softwareAnimation = ApplicationContext.instance().getAnimations().get("software");
		Animation armorAnimation = ApplicationContext.instance().getAnimations().get("armor");
		Animation soulEssenseAnimation = ApplicationContext.instance().getAnimations().get("soulessence");
		Animation mapAnimation = ApplicationContext.instance().getAnimations().get("chart");
		Animation insightAnimation = ApplicationContext.instance().getAnimations().get("insight");
		
		Player player = ApplicationContextUtils.getPlayer();
		
		Inventory inv = player.getInventory();
		
		SoftwareInventoryItem software = (SoftwareInventoryItem)inv.get(InventoryItemType.SOFTWARE);
		InsightInventoryItem insight = (InsightInventoryItem)inv.get(InventoryItemType.INSIGHT);
		ChartInventoryItem chart = (ChartInventoryItem)inv.get(InventoryItemType.CHART);
		
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
		
		graphics.setLineWidth(1);
		
		for (int i = 0; i < ROWS; i++) {
			DrawingContext rowDc = new TileDrawingContext(drawingContext, 1, ROWS, 0, i);
			rowDc = new CenteredDrawingContext(rowDc, ROW_PADDING);
			
			DrawingContext labelDc = new TileDrawingContext(rowDc, 15, 1, 0, 0);
			DrawingContext contentDc = new TileDrawingContext(rowDc, 15, 1, 2, 0, 13, 1);
			
			if (i == 0) {
				
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Score", FontType.GameProgressLabel, 0);
				
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, contentDc, String.valueOf(player.getGameProgress().getGameScore()), FontType.GameProgressLabelInBars, 0);
			}
			
			if (i == 1) {
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Infobits", FontType.GameProgressLabel, 0);
				
				float barWidth = contentDc.w() / maxInfobits * noInfobits;
				
				graphics.setColor(Color.lightGray);
				graphics.fillRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h());
				
				graphics.setColor(Color.green);
				graphics.fillRect(contentDc.x(), contentDc.y(), barWidth, contentDc.h());
				
				graphics.setColor(Color.orange);
				graphics.drawRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h());
				
				int percentage = Math.round((float)noInfobits / ((float)maxInfobits / 100f));
				
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, contentDc, noInfobits + "/" + maxInfobits + " (" + percentage + "%)", FontType.GameProgressLabelInBars, 0);
				
			}
			
			if (i == 2) {
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Secrets", FontType.GameProgressLabel, 0);
				
				float barWidth = contentDc.w() / maxSecrets * noSecrets;
				
				graphics.setColor(Color.lightGray);
				graphics.fillRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h());
				
				graphics.setColor(Color.green);
				graphics.fillRect(contentDc.x(), contentDc.y(), barWidth, contentDc.h());
				
				graphics.setColor(Color.orange);
				graphics.drawRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h());
				
				int percentage = Math.round((float)noSecrets / ((float)maxSecrets / 100f));
				
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, contentDc, noSecrets + "/" + maxSecrets + " (" + percentage + "%)", FontType.GameProgressLabelInBars, 0);
				
			}
			
			if (i == 3) {
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Soul Essenses", FontType.GameProgressLabel, 0);
				
				for (int j = 0; j < maxSoulEssenses; j++) {
					DrawingContext dc = new TileDrawingContext(contentDc, maxSoulEssenses, 1, j, 0);
					dc = new CenteredDrawingContext(dc, 2);
					DrawingContext quadDc = new QuadraticDrawingContext(dc);
					quadDc = new CenteredDrawingContext(quadDc, 2);
					
					if (j < noSoulEssenses) {
						soulEssenseAnimation.draw(quadDc.x(), quadDc.y(), quadDc.w(), quadDc.h());
					}
					
					graphics.drawRect(dc.x(), dc.y(), dc.w(), dc.h());
				}
			}
			
			if (i == 4) {
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Armor", FontType.GameProgressLabel, 0);
				
				for (int j = 0; j < maxArmor; j++) {
					DrawingContext dc = new TileDrawingContext(contentDc, maxArmor, 1, j, 0);
					dc = new CenteredDrawingContext(dc, 2);
					DrawingContext quadDc = new QuadraticDrawingContext(dc);
					quadDc = new CenteredDrawingContext(quadDc, 2);
					
					if (j < noArmor) {
						armorAnimation.draw(quadDc.x(), quadDc.y(), quadDc.w(), quadDc.h());
					}
					
					graphics.drawRect(dc.x(), dc.y(), dc.w(), dc.h());
				}
			}
			
			if (i == 5) {
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Map pieces", FontType.GameProgressLabel, 0);
				int mapsInRow = maxMaps / 2;
				for (int j = 0; j < maxMaps; j++) {
					int k = j % mapsInRow;
					int l = j / mapsInRow;
					DrawingContext dc = new TileDrawingContext(contentDc, mapsInRow, 2, k, l);
					dc = new CenteredDrawingContext(dc, 1);
					DrawingContext quadDc = new QuadraticDrawingContext(dc);
					quadDc = new CenteredDrawingContext(quadDc, 2);
					
					if (j < noCharts) {
						mapAnimation.draw(quadDc.x(), quadDc.y(), quadDc.w(), quadDc.h());
					}
					
					graphics.drawRect(dc.x(), dc.y(), dc.w(), dc.h());
				}
			}
			
			if (i == 6) {
				
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Software", FontType.GameProgressLabel, 0);
				
				for (int j = 0; j < maxSoftware; j++) {
					DrawingContext dc = new TileDrawingContext(contentDc, maxSoftware, 1, j, 0);
					dc = new CenteredDrawingContext(dc, 2);
					DrawingContext quadDc = new QuadraticDrawingContext(dc);
					quadDc = new CenteredDrawingContext(quadDc, 2);
					
					if (j < noSoftware) {
						softwareAnimation.draw(quadDc.x(), quadDc.y(), quadDc.w(), quadDc.h());
					}
					
					graphics.drawRect(dc.x(), dc.y(), dc.w(), dc.h());
				}
			}
			
			if (i == 7) {
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, labelDc, "Artifacts", FontType.GameProgressLabel, 0);
				for (int j = 0; j < maxInsights; j++) {
					DrawingContext boxDc = new TileDrawingContext(contentDc, maxInsights, 1, j, 0);
					boxDc = new CenteredDrawingContext(boxDc, 1);
					boxDc = new QuadraticDrawingContext(boxDc);
					DrawingContext picDc = new CenteredDrawingContext(boxDc, 2);
					
					if (j < noInsights) {
						insightAnimation.draw(picDc.x(), picDc.y(), picDc.w(), picDc.h());
					}
					
					graphics.drawRect(boxDc.x(), boxDc.y(), boxDc.w(), boxDc.h());
				}
			}
			
		}
		
		
	}

}
