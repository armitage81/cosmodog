package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.listener.life.ComposedLifeListener;
import antonafanasjew.cosmodog.listener.life.LifeListener;
import antonafanasjew.cosmodog.listener.movement.ComposedMovementListener;
import antonafanasjew.cosmodog.listener.movement.MovementListener;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.PositionUtils;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Actors are movable pieces on the map that also have a direction.
 */
public abstract class Actor extends Piece {

	private static final long serialVersionUID = 920294272348338561L;

	private List<MovementListener> movementListeners = Lists.newArrayList();
	private ComposedMovementListener composedMovementListener = new ComposedMovementListener(movementListeners);
	
	private List<LifeListener> lifeListeners = Lists.newArrayList();
	protected ComposedLifeListener composedLifeListener = new ComposedLifeListener(lifeListeners);

	private DirectionType direction;
	
	private int maxLife;
	private int life;
	
	private int lifeLentForThirst;
	private int lifeLentForHunger;
	private int lifeLentForFrost;
	
	public void skipTurn() {
		composedMovementListener.beforeWaiting(this, ApplicationContext.instance());
		composedMovementListener.afterWaiting(this, ApplicationContext.instance());
	}
	
	public void beginFight() {
		composedMovementListener.beforeFight(this, ApplicationContext.instance());
	}
	
	public void endFight() {
		composedMovementListener.afterFight(this, ApplicationContext.instance());
	}
	
	public void beginTeleportation() {
		composedMovementListener.beforeTeleportation(this, ApplicationContext.instance());
	}

	public void beginRespawn() {
		composedMovementListener.beforeRespawn(this, ApplicationContext.instance());
	}

	public void endTeleportation() {
		composedMovementListener.afterTeleportation(this, ApplicationContext.instance());
	}

	public void endRespawn() {
		composedMovementListener.afterRespawn(this, ApplicationContext.instance());
	}

	public void endSwitchingPlane() {
		composedMovementListener.afterSwitchingPlane(this, ApplicationContext.instance());
	}
	
	public void shiftHorizontal(int positionOffset) {
		Position position1 = this.getPosition();
		Position position2 = this.getPosition().shifted(positionOffset, 0);

		composedMovementListener.beforeMovement(this, position1, position2, ApplicationContext.instance());
		composedMovementListener.onLeavingTile(this, position1, position2, ApplicationContext.instance());

		this.setPosition(position2);
		this.setDirection(positionOffset < 0 ? DirectionType.LEFT : DirectionType.RIGHT);

		composedMovementListener.onEnteringTile(this, position1, position2, ApplicationContext.instance());
		composedMovementListener.onInteractingWithTile(this, position1, position2, ApplicationContext.instance());
		composedMovementListener.afterMovement(this, position1, position2, ApplicationContext.instance());
	}

	public void shiftVertical(int positionOffset) {
		Position position1 = this.getPosition();
		Position position2 = this.getPosition().shifted(0, positionOffset);

		composedMovementListener.beforeMovement(this, position1, position2, ApplicationContext.instance());
		composedMovementListener.onLeavingTile(this, position1, position2, ApplicationContext.instance());

		this.setPosition(position2);
		this.setDirection(positionOffset < 0 ? DirectionType.UP : DirectionType.DOWN);

		composedMovementListener.onEnteringTile(this, position1, position2, ApplicationContext.instance());
		composedMovementListener.onInteractingWithTile(this, position1, position2, ApplicationContext.instance());
		composedMovementListener.afterMovement(this, position1, position2, ApplicationContext.instance());
	}


	public List<MovementListener> getMovementListeners() {
		return movementListeners;
	}
	
	public List<LifeListener> getLifeListeners() {
		return lifeListeners;
	}

	/**
	 * Returns the actual life points, that is the real points minus life points lent for 
	 * hunger, thirst and frost.
	 * 
	 * Use this everywhere where you need to decide what happens to the player after a negative effect,
	 * f.i. damage or hunger.
	 * 
	 * Also use this to demonstrate the actually available life points on screen.
	 * 
	 * Do not use it to calculate the amount of life points to be refilled. Use {@link Actor#getLife()} instead.
	 *  
	 */
	public int getActualLife() {
		int life = getLife();
		life -= lifeLentForHunger;
		life -= lifeLentForThirst;
		life -= lifeLentForFrost;
		return life;
	}
	
	/**
	 * Returns the actual max life points, that is the real points minus life points lent for 
	 * hunger, thirst and frost.
	 * 
	 * Use this everywhere where you need to decide what happens to the player after a negative effect,
	 * f.i. damage or hunger.
	 * 
	 * Also use this to demonstrate the actually available max life points on screen.
	 * 
	 * Do not use it to calculate the amount of life points to be refilled. Use {@link Actor#getMaxLife()} instead.
	 */
	public int getActualMaxLife() {
		int life = maxLife;
		life -= lifeLentForHunger;
		life -= lifeLentForThirst;
		life -= lifeLentForFrost;
		return life;
	}
	
	/**
	 * Indicates whether the actor is dead. 
	 * The actor is dead when his actual life points fall to or below zero.
	 * This could happen even if the actor has enough life points, because some of the
	 * life points have been 'lent' to hunger, thirst or frost.
	 * @return true if the actor is dead, false otherwise.
	 */
	public boolean dead() {
		return getActualLife() <= 0;
	}

	/**
	 * Returns the life points of the actor. Take care: this are not necessarily 
	 * the available life points, because some of them could have been 'lent' to some negative
	 * effects, like hunger, thirst etc.
	 * 
	 * @return Actors life points.
	 */
	public int getLife() {
		return this.life;
	}
	
	/**
	 * Sets the life points of the actor.
	 * Take care: Even if you set this value to the maximal life points of the actor,
	 * it does not mean that all life points are available. Some of the points
	 * could have been 'lent' to some negative effects, like hunger, thirst etc.
	 * @param life Life points to be set.
	 */
	public void setLife(int life) {
		int previousActualLife = this.getActualLife();
		this.life = life;
		int currentActualLife = this.getActualLife();
		composedLifeListener.onLifeChange(this, previousActualLife, currentActualLife);
	}
	
	/**
	 * Decreases the life points of the actor by the given amount.
	 * Take care: Even if you do not reduce the life points to zero,
	 * it does not mean that there are life points available for the actor. Some of the points
	 * could have been 'lent' to some negative effects, like hunger, thirst etc.
	 * @param life number of points that the life should be reduced by.
	 */
	public void decreaseLife(int n) {
		this.setLife(this.getLife() - n);
	}

	/**
	 * Returns the maximal life points ignoring the 'lent' points.
	 * @return Maximal life points
	 */
	public int getMaxLife() {
		return this.maxLife;
	}
	
	/**
	 * Sets the maximal life points of the actor.
	 * Take care: Even if you set this value,
	 * it does not mean that all maximal life points are available. Some of the points
	 * could have been 'lent' to some negative effects, like hunger, thirst etc.
	 * 
	 * Example: Player has hp = 5/10. Because of 2 hungry turns, 2 units of life points
	 * have been 'lent' to hunger. The actual hp of the player are 3/8. A 3 points damage attack
	 * would kill him. Now, he walks up and collects a goodie that increases the maximal life points by 5 and refills life points.
	 * As a result, players hp = 15/15. His actual hp are then 12/12 (-3 to both as he did another hungry turn) 
	 * 
	 * @param life maximal life points to be set.
	 */
	public void setMaxLife(int maxLife) {
		int previousActualMaxLife = this.getActualMaxLife();
		this.maxLife = maxLife;
		int currentActualMaxLife = this.getActualMaxLife();
		composedLifeListener.onMaxLifeChange(this, previousActualMaxLife, currentActualMaxLife);
	}
	
	/**
	 * Increases the maximal life points of the actor by the given amount.
	 * Take care: Even if you increase the maximal life points,
	 * it does not mean that there are all maximal life points available for the actor. Some of the points
	 * could have been 'lent' to some negative effects, like hunger, thirst etc.
	 * @param n number of points that the maximal life should be increased by.
	 * @param refillLife life points will be refilled if true, otherwise not.
	 */
	public void increaseMaxLife(int n, boolean refillLife) {
		setMaxLife(maxLife + n);
		if (refillLife) {
			setLife(maxLife);
		}
	}

	public int getLifeLentForThirst() {
		return lifeLentForThirst;
	}

	public void setLifeLentForThirst(int lifeLentForThirst) {
		int actualLifeBefore = getActualLife();
		this.lifeLentForThirst = lifeLentForThirst;
		int actualLifeAfter = getActualLife();
		composedLifeListener.onLifeChange(this, actualLifeBefore, actualLifeAfter);
	}

	public void increaseLifeLentForThirst(int n) {
		int lifeLent = this.getLifeLentForThirst();
		lifeLent += n;
		setLifeLentForThirst(lifeLent);
	}
	
	public int getLifeLentForHunger() {
		return lifeLentForHunger;
	}

	public void setLifeLentForHunger(int lifeLentForHunger) {
		int actualLifeBefore = getActualLife();
		this.lifeLentForHunger = lifeLentForHunger;
		int actualLifeAfter = getActualLife();
		composedLifeListener.onLifeChange(this, actualLifeBefore, actualLifeAfter);
	}

	public void increaseLifeLentForHunger(int n) {
		int lifeLent = this.getLifeLentForHunger();
		lifeLent += n;
		setLifeLentForHunger(lifeLent);
	}
	
	public int getLifeLentForFrost() {
		return lifeLentForFrost;
	}

	public void setLifeLentForFrost(int lifeLentForFrost) {
		int actualLifeBefore = getActualLife();
		this.lifeLentForFrost = lifeLentForFrost;
		int actualLifeAfter = getActualLife();
		
		composedLifeListener.onLifeChange(this, actualLifeBefore, actualLifeAfter);	
	}
	
	public void increaseLifeLentForFrost(int n) {
		int lifeLent = this.getLifeLentForFrost();
		lifeLent += n;
		setLifeLentForFrost(lifeLent);
	}
	
	/**
	 * When the player dies, he will be put back at start and his life will be reset.
	 * We cannot update frost, hunger, thirst and life separately, as these updates would
	 * trigger the life change listener every time. 
	 * Player would count as dead and the dying action would trigger multiple times.
	 * That's why we use this method to update everything at once.
	 */
	public void resetLife() {
		int actualLifeBefore = getActualLife();
		this.lifeLentForFrost = 0;
		this.lifeLentForHunger = 0;
		this.lifeLentForThirst = 0;
		this.life = 10;
		int actualLifeAfter = getActualLife();
		composedLifeListener.onLifeChange(this, actualLifeBefore, actualLifeAfter);	
		
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
