package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

public class EnemyDestructionActionPhase extends FixedLengthAsyncAction  {

	private static final long serialVersionUID = -3853130683025678558L;

	private Player player;
	private Enemy enemy;
	
	public EnemyDestructionActionPhase(Player player, Enemy enemy) {
		super(Constants.ENEMY_DESTRUCTION_ACTION_DURATION);
		this.player = player;
		this.enemy = enemy;
	}

	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

		FightPhaseTransition fightPhaseTransition = new FightPhaseTransition();
		fightPhaseTransition.player = player;
		fightPhaseTransition.enemy = enemy;
		fightPhaseTransition.completion = 0.0f;
		fightPhaseTransition.playerAttack = false;
		fightPhaseTransition.enemyDestruction = true;
		cosmodogGame.setFightPhaseTransition(fightPhaseTransition);
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		cosmodogGame.getFightPhaseTransition().completion = (float)after / (float)getDuration();
	}
	
	@Override
	public void onEnd() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		cosmodogMap.getEnemies().remove(enemy);
		cosmodogGame.setFightPhaseTransition(null);
	}

}
