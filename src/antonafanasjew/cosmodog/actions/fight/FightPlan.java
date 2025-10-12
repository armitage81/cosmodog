package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.ArrayList;

import antonafanasjew.cosmodog.actions.fight.FightPlan.FightPhasePlan;
import antonafanasjew.cosmodog.fighting.Damage;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FightPlan extends ArrayList<FightPhasePlan>{

	@Serial
	private static final long serialVersionUID = -761976411668201727L;

	public static class FightPhasePlan {

		private Player player;

		private Enemy enemy;

		private Damage damage;

		private boolean playerAttack;
		
		public static FightPhasePlan instance(Player player, Enemy enemy, Damage damage, boolean playerAttack) {
			FightPhasePlan phasePlan = new FightPhasePlan();
			phasePlan.player = player;
			phasePlan.enemy = enemy;
			phasePlan.damage = damage;
			phasePlan.playerAttack = playerAttack;
			return phasePlan;
		}
		
		public Player getPlayer() {
			return player;
		}

		public Enemy getEnemy() {
			return enemy;
		}

		public Damage getDamage() {
			return damage;
		}

		public boolean isPlayerAttack() {
			return playerAttack;
		}

		public boolean enoughDamageToKillEnemy() {
			return playerAttack && damage.getAmount() >= enemy.getActualLife();
		}

	}

	public int accumulatedDamageForPlayer() {
		int retVal = 0;
		for (FightPhasePlan phasePlan : this) {
			if (!phasePlan.playerAttack) {
				retVal += phasePlan.damage.getAmount();
			}
		}
		return retVal;
	}

	public boolean enoughDamageToKillPlayer() {
		Player player = ApplicationContextUtils.getPlayer();
		return !player.getInventory().hasVehicle() && accumulatedDamageForPlayer() >= player.getActualLife();
	}
}


