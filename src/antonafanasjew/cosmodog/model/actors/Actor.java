package antonafanasjew.cosmodog.model.actors;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.listener.life.ComposedLifeListener;
import antonafanasjew.cosmodog.listener.life.LifeListener;
import antonafanasjew.cosmodog.listener.movement.ComposedMovementListener;
import antonafanasjew.cosmodog.listener.movement.MovementListener;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.util.PositionUtils;

import com.google.common.collect.Lists;

/**
 * Actors are movable pieces on the map that also have a direction.
 */
public abstract class Actor extends Piece {

	private static final long serialVersionUID = 920294272348338561L;

	private List<MovementListener> movementListeners = Lists.newArrayList();
	private ComposedMovementListener composedMovementListener = new ComposedMovementListener(movementListeners);
	
	private List<LifeListener> lifeListeners = Lists.newArrayList();
	private ComposedLifeListener composedLifeListener = new ComposedLifeListener(lifeListeners);

	private DirectionType direction;
	
	private int maxLife;
	private int life;

	public void shiftHorizontal(int positionOffset) {
		int x1 = this.getPositionX();
		int y1 = this.getPositionY();
		int x2 = this.getPositionX() + positionOffset;
		int y2 = this.getPositionY();
		composedMovementListener.beforeMovement(this, x1, y1, x2, y2, ApplicationContext.instance());
		composedMovementListener.onLeavingTile(this, x1, y1, x2, y2, ApplicationContext.instance());
		this.setPositionX(this.getPositionX() + positionOffset);
		this.setDirection(positionOffset < 0 ? DirectionType.LEFT : DirectionType.RIGHT);
		composedMovementListener.onEnteringTile(this, x1, y1, x2, y2, ApplicationContext.instance());
		composedMovementListener.onInteractingWithTile(this, x1, y1, x2, y2, ApplicationContext.instance());
		composedMovementListener.afterMovement(this, x1, y1, x2, y2, ApplicationContext.instance());
	}

	public void shiftVertical(int positionOffset) {
		int x1 = this.getPositionX();
		int y1 = this.getPositionY();
		int x2 = this.getPositionX();
		int y2 = this.getPositionY() + positionOffset;
		composedMovementListener.beforeMovement(this, x1, y1, x2, y2, ApplicationContext.instance());
		composedMovementListener.onLeavingTile(this, x1, y1, x2, y2, ApplicationContext.instance());
		this.setPositionY(this.getPositionY() + positionOffset);
		this.setDirection(positionOffset < 0 ? DirectionType.UP : DirectionType.DOWN);
		composedMovementListener.onEnteringTile(this, x1, y1, x2, y2, ApplicationContext.instance());
		composedMovementListener.onInteractingWithTile(this, x1, y1, x2, y2, ApplicationContext.instance());
		composedMovementListener.afterMovement(this, x1, y1, x2, y2, ApplicationContext.instance());
	}


	public List<MovementListener> getMovementListeners() {
		return movementListeners;
	}
	
	public List<LifeListener> getLifeListeners() {
		return lifeListeners;
	}

	public boolean dead() {
		return life <= 0;
	}
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		int previousLife = this.life;
		this.life = life;
		composedLifeListener.onLifeChange(this, previousLife, this.life);
	}
	
	public void decreaseLife(int n) {
		this.setLife(this.getLife() - n);
	}

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		int previousMaxLife = this.maxLife;
		this.maxLife = maxLife;
		composedLifeListener.onMaxLifeChange(this, previousMaxLife, this.maxLife);
	}
	
	public void increaseMaxLife(int n, boolean refillLife) {
		setMaxLife(maxLife + n);
		if (refillLife) {
			setLife(maxLife);
		}
	}

	public void lookAtActor(Actor actor) {
		DirectionType dirType = PositionUtils.targetDirection(this, actor);
		this.setDirection(dirType);
	}
	

	public DirectionType getDirection() {
		return direction;
	}

	public void setDirection(DirectionType direction) {
		this.direction = direction;
	}
	
}
