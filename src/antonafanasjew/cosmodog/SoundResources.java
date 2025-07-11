package antonafanasjew.cosmodog;

import java.util.HashMap;

import org.newdawn.slick.Sound;

/**
 * Map structure holding the sound resources.
 */
public class SoundResources extends HashMap<String, Sound> {

	private static final long serialVersionUID = -2190800514837591016L;

	/**
	 * Sound for: Spacelift when coupling.
	 */
	public static final String SOUND_SPACE_LIFT_LATCH = "sound.spacelift_latch";

	/**
	 * Sound for: Spacelift in motion.
	 */
	public static final String SOUND_SPACE_LIFT = "sound.spacelift";

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
	public static final String SOUND_FOOTSTEPS_WATER = "sound.footsteps.water";
	public static final String SOUND_FOOTSTEPS_HIGH_GRASS = "sound.footsteps.highgrass";
	public static final String SOUND_FOOTSTEPS_SNOW = "sound.footsteps.snow";
	public static final String SOUND_FOOTSTEPS_SAND = "sound.footsteps.sand";
	public static final String SOUND_FOOTSTEPS_GRASS = "sound.footsteps.grass";
	public static final String SOUND_FOOTSTEPS_ROAD = "sound.footsteps.road";
	
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
	
	public static final String SOUND_POPUP = "sound.popup";

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
	 * Step on the letter plate.
	 */
	public static final String SOUND_LETTER_PLATE = "sound.letterplate";

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

	/**
	 * Keyboard sound when approaching a console.
	 */
	public static final String SOUND_CONSOLE = "sound.console";

	/**
	 * Growl of the worm that is eating the player character.
	 */
	public static final String SOUND_WORM_GROWL = "sound.wormgrowl";

	/**
	 * Earthquake before the worm appears.
	 */
	public static final String SOUND_EARTHQUAKE = "sound.earthquake";

	/**
	 * Break crate
	 */
	public static final String SOUND_BREAK_CRATE1 = "sound.breakcrate1";

	public static final String SOUND_BREAK_CRATE2 = "sound.breakcrate2";

	public static final String SOUND_BREAK_CRATE3 = "sound.breakcrate3";

	public static final String SOUND_BREAK_STONE1 = "sound.breakstone1";
	public static final String SOUND_BREAK_STONE2 = "sound.breakstone2";
	public static final String SOUND_BREAK_STONE3 = "sound.breakstone3";

	public static final String SOUND_BREAK_HARDSTONE1 = "sound.breakhardstone1";
	public static final String SOUND_BREAK_HARDSTONE2 = "sound.breakhardstone2";
	public static final String SOUND_BREAK_HARDSTONE3 = "sound.breakhardstone3";

	public static final String SOUND_CUT_TREE1 = "sound.cuttingtree1";
	public static final String SOUND_CUT_TREE2 = "sound.cuttingtree2";
	public static final String SOUND_CUT_TREE3 = "sound.cuttingtree3";

	public static final String SOUND_CUT_BAMBOO1 = "sound.cuttingbamboo1";
	public static final String SOUND_CUT_BAMBOO2 = "sound.cuttingbamboo2";
	public static final String SOUND_CUT_BAMBOO3 = "sound.cuttingbamboo3";

	public static final String SOUND_LOCKED_ALIEN_DOOR = "sound.lockedaliendoor";
	public static final String SOUND_OPENING_ALIEN_DOOR = "sound.openingaliendoor";

	public static final String SOUND_POISONED = "sound.poisoned";

	public static final String SOUND_GUARDIAN_DESTROYED = "sound.guardiandestroyed";

	public static final String SOUND_MENU_SUB = "sound.menu_sub";
	public static final String SOUND_MENU_BACK = "sound.menu_back";
	public static final String SOUND_MENU_SELECT = "sound.menu_select";
	public static final String SOUND_MENU_MOVE = "sound.menu_move";

	public static final String SOUND_AMBIENT_ELECTRICITY = "sound.ambient.electricity";
	public static final String SOUND_AMBIENT_ENERGYWALL = "sound.ambient.energywall";
	public static final String SOUND_AMBIENT_FIRE = "sound.ambient.fire";

	public static final String SOUND_INTRO_MISSILE_ALERT = "sound.intro.missilealert";
	public static final String SOUND_INTRO_SIREN = "sound.intro.siren";

	public static final String SOUND_CUTSCENE_ALISASMESSAGE = "sound.cutscene.alisasmessage";

	public static final String SOUND_TEXT_TYPING = "sound.texttyping";
	
	public static final String SOUND_SLIDING = "sound.sliding";
	
	public static final String SOUND_SECRET_DOOR_SPIKES = "sound.secretdoor.spikes";
	public static final String SOUND_SECRET_DOOR_HYDRAULICS = "sound.secretdoor.hydraulics";
	public static final String SOUND_SECRET_DOOR_ENERGY = "sound.secretdoor.energy";
	public static final String SOUND_SECRET_DOOR_WALL = "sound.secretdoor.wall";

	public static final String SOUND_PORTALS_GUNSHOT = "sound.portals.gunshot";
	public static final String SOUND_PORTALS_JAMMED = "sound.portals.jammed";
	public static final String SOUND_PORTALS_CREATED = "sound.portals.created";
	public static final String SOUND_PORTALS_FAILED = "sound.portals.failed";
	public static final String SOUND_PORTALS_CANCELED = "sound.portals.canceled";
	public static final String SOUND_PORTALS_TELEPORTED = "sound.portals.teleported";

	public static final String SOUND_BUTTON_PUSHED = "sound.buttonpushed";
	public static final String SOUND_SENSOR_PRESENCE_DETECTED = "sound.sensorpresencedetected";
	public static final String SOUND_SENSOR_PRESENCE_LOST = "sound.sensorpresencelost";
	public static final String SOUND_ALERT = "sound.alert";

}
