package antonafanasjew.cosmodog.model.actors.builder;

import antonafanasjew.cosmodog.model.actors.Enemy;


public abstract class AbstractEnemyFactory implements EnemyFactory {

	@Override
	public Enemy buildEnemy() {
		return buildEnemyInternal();
	}

	protected abstract Enemy buildEnemyInternal();
	
}
