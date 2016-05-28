package antonafanasjew.cosmodog.resourcehandling.builder.enemyfactory;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.listener.life.EnemyLifeListener;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.builder.AbstractEnemyFactory;
import antonafanasjew.cosmodog.model.actors.builder.EnemyFactory;
import antonafanasjew.cosmodog.resourcehandling.AbstractResourceWrapperBuilder;
import antonafanasjew.cosmodog.sight.Sight;


public class EnemyFactoryBuilder extends AbstractResourceWrapperBuilder<EnemyFactory> {

	@Override
	protected EnemyFactory build(String line) {
		String[] parts = line.split(";");
		
		String enemyType = parts[0];
		String armorType = parts[1];
		String weaponType = parts[2];
		String chaussieType = parts[3];
		int maxLife = Integer.parseInt(parts[4]);
		int speedFactor = Integer.parseInt(parts[5]);
		String sights = parts[6].substring(1, parts[6].length() - 1); //result is something, like 16/0/90,16/180/90
		
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
				
				String[] sightElements = sights.split(",");
				
				for (String sightElement : sightElements) {
					String[] sightElementParts = sightElement.split("/");
					float sightDistance = Float.valueOf(sightElementParts[0]);
					float sightAngle = Float.valueOf(sightElementParts[1]);
					float sightAngleRelativeToDirection = Float.valueOf(sightElementParts[2]);
					Sight sight = new Sight(sightDistance, sightAngle, sightAngleRelativeToDirection);
					enemy.getSights().add(sight);
				}
				
				enemy.getLifeListeners().add(new EnemyLifeListener());
				return enemy;
			}
		};
	}

	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/enemyfactorybuilder/enemyfactorymapping.txt";
	}

}
