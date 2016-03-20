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


public class EnemyFactoryBuilder extends AbstractResourceWrapperBuilder<EnemyFactory> {

	@Override
	protected EnemyFactory build(String line) {
		String[] parts = line.split(";");
		
		String enemyType = parts[0];
		String armorType = parts[1];
		String weaponType = parts[2];
		String chaussieType = parts[3];
		int maxLife = Integer.parseInt(parts[4]);
		int sightRadius = Integer.parseInt(parts[5]);
		int speedFactor = Integer.parseInt(parts[6]);
		
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
				enemy.setSightRadius(sightRadius);
				enemy.setSpeedFactor(speedFactor);
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
