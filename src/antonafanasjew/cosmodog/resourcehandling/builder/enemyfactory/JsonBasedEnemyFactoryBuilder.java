package antonafanasjew.cosmodog.resourcehandling.builder.enemyfactory;

import antonafanasjew.cosmodog.domains.*;
import antonafanasjew.cosmodog.listener.life.EnemyLifeListener;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.builder.AbstractEnemyFactory;
import antonafanasjew.cosmodog.model.actors.builder.EnemyFactory;
import antonafanasjew.cosmodog.resourcehandling.AbstractJsonBasedResourceWrapperBuilder;
import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Sets;
import org.json.JSONObject;

import java.util.Set;


public class JsonBasedEnemyFactoryBuilder extends AbstractJsonBasedResourceWrapperBuilder<EnemyFactory> {

	@Override
	protected EnemyFactory build(JSONObject object) {

		String enemyType = object.getString("id");
		String armorType = object.getString("armorType");
		String weaponType = object.getString("weaponType");
		String chaussieType = object.getString("chaussieType");
		int maxLife = object.getInt("maxLife");
		int speedFactor = object.getInt("speedFactor");
		String visionStealth = object.getJSONObject("vision").getString("stealth");
		String visionNight = object.getJSONObject("vision").getString("night");
		String visionFull = object.getJSONObject("vision").getString("full");


		return new AbstractEnemyFactory() {
			
			@Override
			protected Enemy buildEnemyInternal() {
				Enemy enemy = new Enemy();
				enemy.setUnitType(UnitType.valueOf(enemyType));
				enemy.setArmorType(ArmorType.valueOf(armorType));
				enemy.setWeaponType(WeaponType.valueOf(weaponType));
				enemy.setChaussieType(ChaussieType.valueOf(chaussieType));
				enemy.setMaxLife(maxLife);
				enemy.setLife(maxLife);
				enemy.setSpeedFactor(speedFactor);
				
				enemy.getStealthVision().getElements().addAll(visionElementsFromString(visionStealth));
				enemy.getNightVision().getElements().addAll(visionElementsFromString(visionNight));
				enemy.getDefaultVision().getElements().addAll(visionElementsFromString(visionFull));

				if (enemy.getUnitType() == UnitType.SOLARTANK) {
					enemy.setActiveAtDayTimeOnly(true);
				}
				
				enemy.getLifeListeners().add(new EnemyLifeListener());
				return enemy;
			}
		};
	}

	private Set<Position> visionElementsFromString(String visionElementsText) {

		Set<Position> retVal = Sets.newHashSet();

		if (visionElementsText.isEmpty()) {
			return retVal;
		}

		String[] visionParts = visionElementsText.split(" ");

		for (String visionPart : visionParts) {
			String[] xAndY = visionPart.split("/");
			short x = (short)Integer.parseInt(xAndY[0]);
			short y = (short)Integer.parseInt(xAndY[1]);
			Position position = Position.fromCoordinates(x, y, null);
			retVal.add(position);
		}

		return retVal;
	}

	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/enemyfactorybuilder/enemyfactorymapping.json";
	}

}
