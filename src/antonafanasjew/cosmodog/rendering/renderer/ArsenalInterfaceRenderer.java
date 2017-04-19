package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.Mappings;

public class ArsenalInterfaceRenderer implements Renderer {

	/*
	 * Weapons animations have different widths, f.i. rifle is twice as wide as pistol.
	 * This map gives the information about the widths to construct appropriate drawing contexts
	 */
	private Map<WeaponType, Integer> columnsForWeaponType = Maps.newHashMap();
		
	
	private int columns;
	
	public ArsenalInterfaceRenderer() {
		columnsForWeaponType.put(WeaponType.FISTS, 0);
		columnsForWeaponType.put(WeaponType.PISTOL, 1);
		columnsForWeaponType.put(WeaponType.SHOTGUN, 2);
		columnsForWeaponType.put(WeaponType.RIFLE, 2);
		columnsForWeaponType.put(WeaponType.MACHINEGUN, 3);
		columnsForWeaponType.put(WeaponType.RPG, 3);
		
		for (int n : columnsForWeaponType.values()) {
			columns += n;
		}
	}
	
	
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
		
		Arsenal arsenal = player.getArsenal();

		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		
		int columnsUsed = 0;
		for (WeaponType weaponType : arsenal.getWeaponsOrder()) {
			
			//Do not render fists.
			if (weaponType.equals(WeaponType.FISTS)) {
				continue;
			}
			
			
			int widthInColumns = columnsForWeaponType.get(weaponType);
			DrawingContext iconDc = new TileDrawingContext(context, columns, 1, columnsUsed, 0, widthInColumns, 1);
			
			
			
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);

			
			
			if (weapon != null) {
			
				if (selectedWeaponType != null && selectedWeaponType.equals(weaponType)) {
					g.setColor(Color.yellow);
				} else if (weapon.getAmmunition() == 0){
					g.setColor(Color.lightGray);
				} else {
					g.setColor(Color.white);
				}
				g.fillRect(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
				
				String animationId = Mappings.WEAPON_TYPE_2_ICON_ANIMATION_ID.get(weaponType);
				Animation animation = applicationContext.getAnimations().get(animationId);
				
				float heightWidthRation = animation.getHeight() / (float)animation.getWidth();
				
				DrawingContext weaponDc = new TileDrawingContext(iconDc, 1, 7, 0, 0, 1, 6);
				
				weaponDc = new CenteredDrawingContext(weaponDc, weaponDc.w(), weaponDc.w() * heightWidthRation);
				
				animation.draw(weaponDc.x(), weaponDc.y(), weaponDc.w(), weaponDc.h());
				
				float currentVsMaxAmmoRatio = weapon.getAmmunition() / (float)weaponType.getMaxAmmo();
				
				g.setColor(Color.red);
				g.fillRect(iconDc.x(), iconDc.y() + iconDc.h() - 10, iconDc.w() * currentVsMaxAmmoRatio, 10);
				
				g.setColor(Color.black);
				g.drawRect(iconDc.x(), iconDc.y() + iconDc.h() - 10, iconDc.w() * currentVsMaxAmmoRatio, 10);
				
			
			}
			
			g.setColor(Color.black);
			g.drawRect(iconDc.x(), iconDc.y(), iconDc.w(), iconDc.h());
			
			columnsUsed += widthInColumns;
		}
		
	}


}
