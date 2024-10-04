package antonafanasjew.cosmodog.ingamemenu.progress;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.Mappings;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.QuadraticDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ProgressRenderer implements Renderer {

	private static final int ROWS = 7;
	private static final int ROW_PADDING = 10;
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
		
		Animation softwareAnimation = ApplicationContext.instance().getAnimations().get("software");
		Animation armorAnimation = ApplicationContext.instance().getAnimations().get("armor");
		Animation fuelTankAnimation = ApplicationContext.instance().getAnimations().get("fueltank");
		Animation bottleAnimation = ApplicationContext.instance().getAnimations().get("bottle");
		Animation foodCompartmentAnimation = ApplicationContext.instance().getAnimations().get("foodcompartment");
		Animation soulEssenseAnimation = ApplicationContext.instance().getAnimations().get("soulessence");
		Animation mapAnimation = ApplicationContext.instance().getAnimations().get("chart");
		Animation insightAnimation = ApplicationContext.instance().getAnimations().get("insight");

		Animation pistolAnimation = ApplicationContext.instance().getAnimations().get("pistol");
		Animation shotgunAnimation = ApplicationContext.instance().getAnimations().get("shotgun");
		Animation rifleAnimation = ApplicationContext.instance().getAnimations().get("rifle");
		Animation machinegunAnimation = ApplicationContext.instance().getAnimations().get("machinegun");
		Animation rpgAnimation = ApplicationContext.instance().getAnimations().get("rpg");
		List<Animation> weaponAnimations = List.of(pistolAnimation, shotgunAnimation, rifleAnimation, machinegunAnimation, rpgAnimation);

		Animation pistolAmmoAnimation = ApplicationContext.instance().getAnimations().get("pistolAmmo");
		Animation shotgunAmmoAnimation = ApplicationContext.instance().getAnimations().get("shotgunAmmo");
		Animation rifleAmmoAnimation = ApplicationContext.instance().getAnimations().get("rifleAmmo");
		Animation machinegunAmmoAnimation = ApplicationContext.instance().getAnimations().get("machinegunAmmo");
		Animation rpgAmmoAnimation = ApplicationContext.instance().getAnimations().get("rpgAmmo");
		List<Animation> weaponAmmoAnimations = List.of(pistolAmmoAnimation, shotgunAmmoAnimation, rifleAmmoAnimation, machinegunAmmoAnimation, rpgAmmoAnimation);
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		Inventory inv = player.getInventory();
		
		SoftwareInventoryItem software = (SoftwareInventoryItem)inv.get(InventoryItemType.SOFTWARE);
		InsightInventoryItem insight = (InsightInventoryItem)inv.get(InventoryItemType.INSIGHT);
		ChartInventoryItem chart = (ChartInventoryItem)inv.get(InventoryItemType.CHART);
		
		int noSoftware = software == null ? 0 : software.getNumber();
		int maxSoftware = Constants.NUMBER_OF_SOFTWARE_PIECES_IN_GAME;
		int noArmor = player.getGameProgress().getArmors();
		int maxArmor = Constants.NUMBER_OF_ARMOR_PIECES_IN_GAME;
		int noFuelTanks = player.getGameProgress().getFuelTanks();
		int maxFuelTanks = Constants.NUMBER_OF_FUEL_TANK_PIECES_IN_GAME;
		int noBottles = player.getGameProgress().getBottles();
		int maxBottles = Constants.NUMBER_OF_BOTTLES_IN_GAME;
		int noFoodCompartments = player.getGameProgress().getFoodCompartments();
		int maxFoodCompartments = Constants.NUMBER_OF_FOOD_COMPARTMENTS_IN_GAME;
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
		
		
		FontRefToFontTypeMap fontTypeSubheader = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.SubHeader);
		FontRefToFontTypeMap fontTypeInformational = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Informational);
		FontRefToFontTypeMap fontTypeInactive = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Inactive);
		
		for (int i = 0; i < ROWS; i++) {
			
			DrawingContext rowDc = new TileDrawingContext(inGameMenuContentDrawingContext, 1, ROWS, 0, i);
			rowDc = new CenteredDrawingContext(rowDc, ROW_PADDING);
			
			DrawingContext labelDc = new TileDrawingContext(rowDc, 15, 1, 0, 0, 2, 1);
			DrawingContext contentDc = new TileDrawingContext(rowDc, 15, 1, 2, 0, 13, 1);
			
			if (i == 0) {
				
				Book textBook;
				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Score", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
				textBook = TextPageConstraints.fromDc(contentDc).textToBook(String.valueOf(player.getGameProgress().getGameScore()), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
			}
			
			if (i == 1) {
				
				Book textBook;
				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Infobits", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
				
				float barWidth = contentDc.w() / maxInfobits * noInfobits;
				graphics.setColor(new org.newdawn.slick.Color(0f, 0.5f, 0f));
				graphics.fillRect(contentDc.x(), contentDc.y(), barWidth, contentDc.h());
				graphics.setColor(org.newdawn.slick.Color.orange);
				graphics.drawRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h());
				int percentage = (int)((float)noInfobits / (float)maxInfobits * 100f);
				
				textBook = TextPageConstraints.fromDc(contentDc).textToBook(noInfobits + "/" + maxInfobits + " (" + percentage + "%)", fontTypeInformational);
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
				
				
			}
			
			if (i == 2) {
				
				Book textBook;
				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Secrets", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
				
				float barWidth = contentDc.w() / maxSecrets * noSecrets;
				
				graphics.setColor(new org.newdawn.slick.Color(0f, 0.5f, 0f));
				graphics.fillRect(contentDc.x(), contentDc.y(), barWidth, contentDc.h());
				graphics.setColor(org.newdawn.slick.Color.orange);
				graphics.drawRect(contentDc.x(), contentDc.y(), contentDc.w(), contentDc.h());
				
				int percentage = (int)((float)noSecrets / (float)maxSecrets * 100f);
				
				textBook = TextPageConstraints.fromDc(contentDc).textToBook(noSecrets + "/" + maxSecrets + " (" + percentage + "%)", fontTypeInformational);
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
				
			}
			
			if (i == 3) {

				Book textBook;

				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Collectibles", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				int columns = 8;
				int currentColumn = 0;

				DrawingContext dc;
				DrawingContext iconDc;
				DrawingContext amountDc;

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				soulEssenseAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noSoulEssenses, maxSoulEssenses), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				bottleAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noBottles, maxBottles), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				foodCompartmentAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noFoodCompartments, maxFoodCompartments), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				armorAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noArmor, maxArmor), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				fuelTankAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noFuelTanks, maxFuelTanks), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				mapAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noCharts, maxMaps), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				softwareAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noSoftware, maxSoftware), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				dc = new TileDrawingContext(contentDc, columns, 1, currentColumn++, 0);
				iconDc = new QuadraticDrawingContext(new TileDrawingContext(dc, 2, 1, 0, 0));
				amountDc = new TileDrawingContext(dc, 2, 1, 1, 0);
				insightAnimation.draw(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				textBook = TextPageConstraints.fromDc(amountDc).textToBook(String.format("%s/%s", noInsights, maxInsights), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

			}
			
			if (i == 4) {
				
				Book textBook;
				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Weapons", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				DrawingContext dc;
				DrawingContext iconDc;

				List<WeaponType> weaponTypes = player.getArsenal().getWeaponsOrder().subList(1, player.getArsenal().getWeaponsOrder().size());
				for (int j = 0; j < weaponTypes.size(); j++) {
					iconDc = new TileDrawingContext(contentDc, weaponTypes.size(), 1, j, 0);

					boolean hasWeapon;
					boolean hasFirstAmmoUpgrade;
					boolean hasSecondAmmoUpgrade;

					Weapon weapon = player.getArsenal().getWeaponsCopy().get(weaponTypes.get(j));

					if (weapon == null) {
						hasWeapon = false;
						hasFirstAmmoUpgrade = false;
						hasSecondAmmoUpgrade = false;
					} else {
						hasWeapon = true;
						hasFirstAmmoUpgrade = weapon.getMaxAmmunition() > weapon.getWeaponType().getMaxAmmo();
						hasSecondAmmoUpgrade = weapon.getMaxAmmunition() > weapon.getWeaponType().getMaxAmmo() + 1;
					}

					if (!hasWeapon) {
						textBook = TextPageConstraints.fromDc(iconDc).textToBook(weaponTypes.get(j).name(), fontTypeInactive);
						TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
					} else {
						String weaponName = weaponTypes.get(j).name();
						if (hasFirstAmmoUpgrade) {
							weaponName = weaponName + " +";
						}
						if (hasSecondAmmoUpgrade) {
							weaponName = weaponName + " +";
						}
						textBook = TextPageConstraints.fromDc(iconDc).textToBook(weaponName, fontTypeInformational);
						TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
					}
				}
			}
			if (i == 5) {
				Book textBook;
				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Tools", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);

				Iterator<CollectibleTool.ToolType> toolTypeIterator = Arrays.stream(CollectibleTool.ToolType.values()).iterator();
				for (int j = 0; j < 20; j++) {
					DrawingContext toolDc = new TileDrawingContext(contentDc, 20, 1, j, 0);
					toolDc = new CenteredDrawingContext(toolDc, 5);
					if (toolTypeIterator.hasNext()) {
						CollectibleTool.ToolType toolType = toolTypeIterator.next();
						InventoryItemType inventoryItemType = Mappings.COLLECTIBLE_TOOL_TYPE_TO_INVENTORY_ITEM_TYPE.get(toolType);
						InventoryItem inventoryItem = player.getInventory().get(inventoryItemType);
						String animationId = Mappings.INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.get(inventoryItemType);
						Animation inventoryItemAnimation = ApplicationContext.instance().getAnimations().get(animationId);
						if (inventoryItem != null) {
							graphics.setColor(Color.green);
							graphics.fillRect(toolDc.x(), toolDc.y(), toolDc.w(), toolDc.h());
							graphics.setColor(Color.orange);
							graphics.setLineWidth(3);
							graphics.drawRect(toolDc.x(), toolDc.y(), toolDc.w(), toolDc.h());
							inventoryItemAnimation.draw(toolDc.x(), toolDc.y(), toolDc.w(), toolDc.h());
						} else {
							graphics.setColor(Color.lightGray);
							graphics.fillRect(toolDc.x(), toolDc.y(), toolDc.w(), toolDc.h());
							graphics.setColor(Color.orange);
							graphics.setLineWidth(3);
							graphics.drawRect(toolDc.x(), toolDc.y(), toolDc.w(), toolDc.h());
							inventoryItemAnimation.draw(toolDc.x(), toolDc.y(), toolDc.w(), toolDc.h(), new Color(0,0,0));
						}
					} else {
						break;
					}
				}
			}
			if (i == 6) {
				
				Book textBook;
				textBook = TextPageConstraints.fromDc(labelDc).textToBook("Play Time", fontTypeSubheader);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
				
				textBook = TextPageConstraints.fromDc(contentDc).textToBook(cosmodogGame.getTimer().playTimeRepresentationDaysHoursMinutesSeconds("%s days %s hours %s minutes %s seconds"), fontTypeInformational);
				TextBookRendererUtils.renderVerticallyCenteredLabel(gameContainer, graphics, textBook);
				
				
			}
			
		}
		
		
	}

}
