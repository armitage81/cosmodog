package antonafanasjew.cosmodog.listener.movement;

import java.io.Serializable;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public interface MovementListener extends Serializable {

	/**
	 * Executed before the player update his direction for a movement attempt.
	 * Note: Normally, this method handles a change of direction, for instance if the player looked south and now
	 * must walk north, east or west,
	 * but it is also called if the direction does not change, for instance if the player looked south and now
	 * must walk south.
	 *
	 * To check if there was a real direction change, compare the arguments for before and after.
	 *
	 * @param before Direction before the change.
	 * @param after Direction after the change (could stay the same as before.)
	 */
	void beforeTurning(DirectionType before, DirectionType after);

	/**
	 * Executed after the player sets his direction for a movement attempt.
	 *
	 * Note: The same goes as for the 'beforeTurning'. There will not always be a real direction change
	 * when this method is called.
	 *
	 */
	void afterTurning(DirectionType before, DirectionType after);

	/**
	 * Executed before a movement fails due to a collision.
	 * Note: Attacking an enemy is not handled by this case.
	 */
	void beforeBlock(Actor actor, Position position1, Position position2);

	/**
	 * Executed after a movement fails due to a collision.
	 * Note: Attacking an enemy is not handled by this case.
	 */
	void afterBlock(Actor actor, Position position1, Position position2);

	/**
	 * Currently executed exactly before shifting player's position vertically or horizontally.
	 *
	 * Implementations do not do anything with it at the moment. The idea is to use it for portal ray deactivation during movement.
	 */
	void beforeMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);

	/**
	 * Executed immediately after 'beforeMovement' before shifting player.
	 *
	 * Implementations do not do anything with it.
	 */
	void onLeavingTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);

	/**
	 * Executed AFTER shifting player's position vertically or horizontally.
	 *
	 * Implementations:
	 * - RuleBookMovementListener - throws the event that the position has been changed. (Many rules are based on this)
	 * - PlayerMovementListener
	 * -- Saves the information from before the movement: water reserve, whether player was dehydrating or starving, worm alertness.
	 * -- Increments game time.
	 * -- Updates the worm - if the player is in a worm region - by incrementing or resetting worm's alertness.
	 * -- Increments the poison turn count - if the player is poisoned.
	 * -- Reduces water, food and - potentially - fuel.
	 *
	 */
	void onEnteringTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);

	/**
	 * Executed immediately after 'onEnteringTile' when player position has been shifted.
	 *
	 * Implementations:
	 * - PlayerMovementListener
	 * -- Collects collectibles by removing them from the map and updating player's inventory and progress.
	 * -- Refills water if player is close to a water source. Uses previously stored water reserve value to decide whether a 'you drank water' notification is shown.
	 * -- Refills fuel if player entered a gas station, either on foot or via vehicle.
	 * -- Detects nearby mines when the player has a mine detector.
	 * -- Updates the characters of all letter plates in the world. (Currently all of them are part of the same puzzle.)
	 * -- Interacts with the pressure button if there is such a dynamic piece on the entered tile.
	 * -- Decontaminates the player if the entered tile contains a decontamination pool.
	 * --- Note how the poisonous count increases before this in the 'onEnteringTile' method - without consequences.
	 * --- The actual consequence happens in the 'afterMovement' method. When player's poison count reaches a limit there, player's life is set to 0, causing death.
	 * --- Handles contamination: If player is not in a car, has no antidote and the poison is not deactivated, then player is marked as poisoned.
	 */
	void onInteractingWithTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);

	/**
	 * Executed immediately after 'onInteractingWithTile' when player position has been shifted.
	 *
	 * Additionally, implemented in PlayerMovementCache, which is being called without the usage of the interface.
	 * - When the DyingAction ends.
	 * - When Cosmodog model is initialized (after loading a game or starting a new one)
	 *
	 * Implementations:
	 * - PlayerMovementListener
	 * -- Handles the consequences of hunger, thirst, cold, radiation, electricity shock, worm alertness, mine explosions and contamination.
	 * -- Increments turn and updates real time the game is being played.
	 * -- Also updates the mode of snowfall by registering the action 'snow fall change' based on player's location.
	 * - PlayerMovementCache
	 * -- Recalculates game data that only happens once per movement.
	 * --- Closest supply and medkit positions (for the detector device).
	 * --- Closest piece interesting for debugging. (Not used but can be helpful when testing the game and searching for remaining enemies and colelctibles.)
	 * --- Is player on the platform? (This calculation is a bit complex and needed for rendering so no need to execute it in each render loop.)
	 * --- Is there a roof over the player? (Same as with the platform: No need to do this calculation in every render loop.)
	 * --- Is the player in a region of 'roof removal blocker'? (They are needed to not remove roof rendering even if a roof is over the player in some cases. No need to calculate it in every render loop.)
	 * --- Collects all dynamic pieces and their positions in the game to reflect potential changes in them after the turn.
	 * --- Supposed to recalculate all visible dynamic pieces (the ones that are nearby to the player), but currently returns all pieces due to tge issue with camera movements.
	 * --- Updates the number of remaining infobits in the game, counting also enemies' inventories.
	 * --- Recalculates nearby enemies. This is used in inter-character collision validators because there is no need to consider far away enemies. Also used in the next step.
	 * --- Recalculates which nearby enemies have roofs over them. This is needed to decide whether their sight cones should be shown or not.
	 * --- Recalculates the active moveable group. They are relevant in Sokoban puzzles.
	 * - AutosaveMovementListener
	 * -- Autosaves.
	 * - RuleBookMovementListener
	 * -- Throws the 'Ended Turn' event. (It is only used in a rule that checks the winning condition.)
	 */
	void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);
	
	void beforeWaiting(Actor actor, ApplicationContext applicationContext);
	void afterWaiting(Actor actor, ApplicationContext applicationContext);
	
	void beforeFight(Actor actor, ApplicationContext applicationContext);
	void afterFight(Actor actor, ApplicationContext applicationContext);
	
	void beforeTeleportation(Actor actor, ApplicationContext applicationContext);
	void afterTeleportation(Actor actor, ApplicationContext applicationContext);

	void beforeRespawn(Actor actor, ApplicationContext applicationContext);
	void afterRespawn(Actor actor, ApplicationContext applicationContext);

	void beforeSwitchingPlane(Actor actor, ApplicationContext applicationContext);
	void afterSwitchingPlane(Actor actor, ApplicationContext applicationContext);
	
	
}
