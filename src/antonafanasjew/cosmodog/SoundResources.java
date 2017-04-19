package antonafanasjew.cosmodog;

import java.util.HashMap;

import org.newdawn.slick.Sound;

/**
 * Map structure holding the sound resources.
 */
public class SoundResources extends HashMap<String, Sound> {

	private static final long serialVersionUID = -2190800514837591016L;
	
	/**
	 * Sound for: Dropped Item.
	 */
	public static final String SOUND_DROPPED_ITEM = "sound.droppeditem";
	
	/**
	 * Sound for: Collected infobit.
	 */
	public static final String SOUND_COLLECTED = "sound.collected";
	
	/**
	 * Sound for: Eaten supplies.
	 */
	public static final String SOUND_EATEN = "sound.eaten";
	
	/**
	 * Sound for: Drunk water.
	 */
	public static final String SOUND_DRUNK = "sound.drunk";
	
	/**
	 * Sound for: Car start.
	 */
	public static final String SOUND_CARSTART = "sound.carstart";
	
	/**
	 * Sound for: Car is driving.
	 */
	public static final String SOUND_CARDRIVING = "sound.cardriving";
	
	/**
	 * Sound for: Blocked passage.
	 */
	public static final String SOUND_NOWAY = "sound.noway";
	
	/**
	 * Sound for: Foot steps.
	 */
	public static final String SOUND_FOOTSTEPS = "sound.footsteps";
	
	/**
	 * Sound for: Car motor.
	 */
	public static final String SOUND_CARMOTOR = "sound.carmotor";
	
	/**
	 * Sound for: Stopping car engine.
	 */
	public static final String SOUND_MOTOR_DIES = "sound.motordies";

	/**
	 * Sound for: Hit in fight.
	 */
	public static final String SOUND_HIT = "sound.hit";
	
	/**
	 * Sound for: Collected power up. 
	 */
	public static final String SOUND_POWERUP = "sound.powerup";

	/**
	 * Sound for: Explosion.
	 */
	public static final String SOUND_EXPLOSION = "sound.explosion";

	/**
	 * Sound for Teleportation start
	 */
	public static final String SOUND_TELEPORT_START = "sound.teleport_start";
	
	/**
	 * Sound for: Teleportation transfer.
	 */
	public static final String SOUND_TELEPORT_TRANSFER = "sound.teleport_transfer";
	
	/**
	 * Sound for: Teleportation end.
	 */
	public static final String SOUND_TELEPORT_END = "sound.teleport_end";
	
	/**
	 * Weapon reload.
	 */
	public static final String SOUND_RELOAD = "sound.reload";

	/**
	 * Step on the pressure plate.
	 */
	public static final String SOUND_PRESSURE_PLATE = "sound.pressureplate";

	/**
	 * Drain poison.
	 */
	public static final String SOUND_DRAIN_POISON = "sound.drainpoison";

	/**
	 * Artillery shots when enemy is attacking.
	 */
	public static final String SOUND_ARTILLERY_SHOTS = "sound.artilleryshots";

	/**
	 * Secret found jingle.
	 */
	public static final String SOUND_SECRET_FOUND = "sound.secretfound";

	/**
	 * Short engine sound to indicate car movement.
	 */
	public static final String SOUND_CAR_MOVES = "sound.carmoves";

	/**
	 * Entering car.
	 */
	public static final String SOUND_CAR_DOOR = "sound.cardoor";

}
