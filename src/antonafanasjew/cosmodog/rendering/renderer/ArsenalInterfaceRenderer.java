package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.Mappings;

import com.google.common.collect.Maps;

public class ArsenalInterfaceRenderer implements Renderer {

	/*
	 * Weapons animations have different widths, f.i. rifle is twice as wide as pistol.
	 * This map gives the information about the widths to construct appropriate drawing contexts
	 */
	private Map<WeaponType, Integer> columnsForWeaponType = Maps.newHashMap();
	
	private Map<WeaponType, String> imageIdForBoxForWeaponType = Maps.newHashMap();
		
	
	private int columns;
	
	public ArsenalInterfaceRenderer() {
		columnsForWeaponType.put(WeaponType.FISTS, 0);
		columnsForWeaponType.put(WeaponType.PISTOL, 1);
		columnsForWeaponType.put(WeaponType.SHOTGUN, 2);
		columnsForWeaponType.put(WeaponType.RIFLE, 2);
		columnsForWeaponType.put(WeaponType.MACHINEGUN, 3);
		columnsForWeaponType.put(WeaponType.RPG, 3);
		
		imageIdForBoxForWeaponType.put(WeaponType.FISTS, "");
		imageIdForBoxForWeaponType.put(WeaponType.PISTOL, "ui.ingame.weaponboxsimple");
		imageIdForBoxForWeaponType.put(WeaponType.SHOTGUN, "ui.ingame.weaponboxdouble");
		imageIdForBoxForWeaponType.put(WeaponType.RIFLE, "ui.ingame.weaponboxdouble");
		imageIdForBoxForWeaponType.put(WeaponType.MACHINEGUN, "ui.ingame.weaponboxtriple");
		imageIdForBoxForWeaponType.put(WeaponType.RPG, "ui.ingame.weaponboxtriple");
		
		for (int n : columnsForWeaponType.values()) {
			columns += n;
		}
	}
	
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext context, Object renderingParameter) {
		
		
		DrawingContext[] dcs = new DrawingContext[5];
		dcs[0] = new SimpleDrawingContext(context, 856, 640, 56, 52);
		dcs[1] = new SimpleDrawingContext(context, 941, 640, 56, 52);
		dcs[2] = new SimpleDrawingContext(context, 1026, 640, 56, 52);
		dcs[3] = new SimpleDrawingContext(context, 1111, 640, 56, 52);
		dcs[4] = new SimpleDrawingContext(context, 1196, 640, 56, 52);
		
		Map<WeaponType, DrawingContext> contextsForWeapons = Maps.newHashMap();
		
		contextsForWeapons.put(WeaponType.PISTOL, dcs[0]);
		contextsForWeapons.put(WeaponType.SHOTGUN, dcs[1]);
		contextsForWeapons.put(WeaponType.RIFLE, dcs[2]);
		contextsForWeapons.put(WeaponType.MACHINEGUN, dcs[3]);
		contextsForWeapons.put(WeaponType.RPG, dcs[4]);
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
				
		Arsenal arsenal = player.getArsenal();

		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		
		int columnsUsed = 0;
		for (WeaponType weaponType : arsenal.getWeaponsOrder()) {
			
			//Do not render fists.
			if (weaponType.equals(WeaponType.FISTS)) {
				continue;
			}
			
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
			
			DrawingContext weaponDc = contextsForWeapons.get(weaponType);
			
			if (weapon == null) {
				g.setColor(Color.black);
			} else if (selectedWeaponType != null && selectedWeaponType.equals(weaponType)) {
				g.setColor(Color.yellow);
			} else if (weapon.getAmmunition() == 0) {
				g.setColor(Color.darkGray);
			} else {
				g.setColor(Color.white);
			}
			g.fillRect(weaponDc.x(), weaponDc.y(), weaponDc.w(), weaponDc.h());

			if (weapon != null) {
			
				
				String animationId = Mappings.WEAPON_TYPE_2_ICON_ANIMATION_ID.get(weaponType);
				Animation animation = applicationContext.getAnimations().get(animationId);
				
				
				//DrawingContext weaponIconDc = new TileDrawingContext(weaponDc, 1, 8, 0, 0, 1, 6);
				//weaponIconDc = new SimpleDrawingContext(weaponIconDc, weaponIconDc.w() - 80, 0, 80, 64);
				DrawingContext ammoDc = new TileDrawingContext(weaponDc, 1, 8, 0, 6, 1, 2);
				ammoDc = new CenteredDrawingContext(ammoDc, 40, ammoDc.h());
				
				animation.draw(weaponDc.x(), weaponDc.y(), weaponDc.w(), weaponDc.h());

				
				
				float currentVsMaxAmmoRatio = weapon.getAmmunition() / (float)weapon.getMaxAmmunition();
				
				g.setColor(Color.red);
				g.fillRect(ammoDc.x(), ammoDc.y(), ammoDc.w() * currentVsMaxAmmoRatio, 10);
				
				g.setColor(Color.black);
				g.drawRect(ammoDc.x(), ammoDc.y(), ammoDc.w() * currentVsMaxAmmoRatio, 10);
				
				
			
			}
			
		}
		
	}


}
