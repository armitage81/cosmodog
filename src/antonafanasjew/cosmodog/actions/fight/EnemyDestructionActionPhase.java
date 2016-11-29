package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

/**
 * Defines the enemy destruction fight action phase, which will be registered during the fight action in case an
 * enemy is destroyed by the player character.
 */
public class EnemyDestructionActionPhase extends AbstractFightActionPhase {

	private static final long serialVersionUID = -3853130683025678558L;

	private Player player;
	private Enemy enemy;
	
	/**
	 * Initialized with the PC and the enemy.
	 * @param player Player character.
	 * @param enemy Enemy actor.
	 */
	public EnemyDestructionActionPhase(Player player, Enemy enemy) {
		super(Constants.ENEMY_DESTRUCTION_ACTION_DURATION);
		this.player = player;
		this.enemy = enemy;
	}

	/**
	 * Initializes the enemy destruction transition.
	 */
	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		
		FightPhaseTransition fightPhaseTransition = new FightPhaseTransition();
		fightPhaseTransition.player = player;
		fightPhaseTransition.enemy = enemy;
		fightPhaseTransition.completion = 0.0f;
		fightPhaseTransition.playerAttack = false;
		fightPhaseTransition.enemyDestruction = true;
		setFightPhaseTransition(fightPhaseTransition);
		
	}
	
	/**
	 * Sets the completion percentage of the transition depending on the passed time.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float newCompletion = (float)after / (float)getDuration();
		newCompletion = newCompletion > 1.0f ? 1.0f : newCompletion;
		getFightPhaseTransition().completion = newCompletion;
	}
	
	/**
	 * Applies the destruction of the enemy on the game model. Removes the enemy from the map. Resets the transition.
	 */
	@Override
	public void onEnd() {
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		cosmodogMap.getEnemies().remove(enemy);
		setFightPhaseTransition(null);
	}

}
