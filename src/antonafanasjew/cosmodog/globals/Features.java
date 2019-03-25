package antonafanasjew.cosmodog.globals;

import java.util.Map;
import java.util.concurrent.Callable;

import org.newdawn.slick.util.Log;

import com.google.common.collect.Maps;

/**
 * Keeps constants for features. Each feature in the game
 * should be depending on this constants 
 */
public class Features {

	/**
	 * The hunger feature including hunger, rations.
	 */
	public static final String FEATURE_HUNGER = "feature.hunger";
	
	/**
	 * The thirst feature including thirst, water places.
	 */
	public static final String FEATURE_THIRST = "feature.thirst";
	
	/**
	 * The infobit feature including infobits, score.
	 */
	public static final String FEATURE_INFOBITS = "feature.infobits";
	
	/**
	 * The soul essence feature including soul essence, life increase.
	 */
	public static final String FEATURE_SOULESSENCE = "feature.soulessence";
	
	/**
	 * Collision feature including collision validators.
	 */
	public static final String FEATURE_COLLISION = "feature.collision";
	
	/**
	 * Fuel feature including fuel reduction, gas stations.
	 */
	public static final String FEATURE_FUEL = "feature.fuel";
	
	/**
	 * Story feature including dialogs, cutscenes.
	 */
	public static final String FEATURE_STORY = "feature.story";
	
	/**
	 * Tutorial feature including tutorial text messages.
	 */
	public static final String FEATURE_TUTORIAL = "feature.tutorial";
	
	/**
	 * Feature for temperature effects on player.
	 */
	public static final String FEATURE_TEMPERATURE = "feature.temperature";
	
	public static final String FEATURE_MUSIC = "feature.music";
	
	public static final String FEATURE_DAMAGE = "feature.damage";
	
	public static final String FEATURE_GODFISTS = "feature.godfists";
	
	public static final String FEATURE_INTERFACE = "feature.interface";
	
	public static final String FEATURE_SIGHTRADIUS = "feature.sightradius";
	
	private static final Features instance = new Features();
	
	/**
	 * Features object instance.
	 * @return The singleton instance.
	 */
	public static Features getInstance() {
		return instance;
	}
	
	private Map<String, Boolean> featureFlags = Maps.newHashMap();

	private Features() {
		getFeatureFlags().put(FEATURE_HUNGER, true);
		getFeatureFlags().put(FEATURE_THIRST, true);
		getFeatureFlags().put(FEATURE_INFOBITS, true);
		getFeatureFlags().put(FEATURE_SOULESSENCE, true);
		getFeatureFlags().put(FEATURE_COLLISION, true);
		getFeatureFlags().put(FEATURE_FUEL, true);
		getFeatureFlags().put(FEATURE_STORY, false);
		getFeatureFlags().put(FEATURE_TUTORIAL, true);
		getFeatureFlags().put(FEATURE_TEMPERATURE, true);
		getFeatureFlags().put(FEATURE_MUSIC, true);
		getFeatureFlags().put(FEATURE_DAMAGE, true);
		getFeatureFlags().put(FEATURE_GODFISTS, false);
		getFeatureFlags().put(FEATURE_INTERFACE, true);
		getFeatureFlags().put(FEATURE_SIGHTRADIUS, true);
	}
	
	/**
	 * Indicates whether a given feature is enabled or not.
	 * @param feature The feature id.
	 * @return true if the feature is enabled, false otherwise.
	 */
	public boolean featureOn(String feature) {
		return getFeatureFlags().get(feature) != null && getFeatureFlags().get(feature);
	}
	
	/**
	 * Runs a procedure if the given feature is active.
	 *
	 * @param feature The feature id.
	 * @param procedure The procedure that is bound to the feature.
	 */
	public void featureBoundProcedure(String feature, Runnable procedure) {
		featureBoundProcedure(feature, procedure, null);
	}

	/**
	 * Runs a procedure if the given feature is active. Otherwise runs the alternative procedure.
	 * 
	 * @param feature The feature id.
	 * @param procedure The procedure that is bound to the feature.
	 * @param otherwise The procedure that will be executed in case the feature is not enabled.
	 */
	public void featureBoundProcedure(String feature, Runnable procedure, Runnable otherwise) {
		if (featureOn(feature)) {
			procedure.run();
		} else {
			if (otherwise != null) {
				otherwise.run();
			}
		}
	}
	
	/**
	 * Returns a value of the given function if the given feature is enabled, default value otherwise.
	 * @param feature The feature id.
	 * @param function The feature bound function whose return value will be returned.
	 * @param defaultValue Default value that will be returned if the feature is not enabled.
	 * @return Value of the given function if the given feature is enabled, default value otherwise.
	 */
	public <T>T featureBoundFunction(String feature, Callable<T> function, T defaultValue) {
		try {
			return featureOn(feature) ? function.call() : defaultValue;
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return defaultValue;
		}
	}

	/**
	 * Returns the flags for each feature.
	 * @return Feature activation flags.
	 */
	public Map<String, Boolean> getFeatureFlags() {
		return featureFlags;
	}

}
