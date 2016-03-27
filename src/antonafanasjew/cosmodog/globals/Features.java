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

	public static final String FEATURE_HUNGER = "feature.hunger";
	public static final String FEATURE_THIRST = "feature.thirst";
	public static final String FEATURE_INFOBITS = "feature.infobits";
	public static final String FEATURE_SOULESSENCE = "feature.soulessence";
	public static final String FEATURE_COLLISION = "feature.collision";
	public static final String FEATURE_MOVEMENT_COSTS = "feature.movementcosts";
	public static final String FEATURE_FUEL = "feature.fuel";
	public static final String FEATURE_STORY = "feature.story";
	public static final String FEATURE_TUTORIAL = "feature.tutorial";
	
	
	private static final Features instance = new Features();
	
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
		getFeatureFlags().put(FEATURE_MOVEMENT_COSTS, true);
		getFeatureFlags().put(FEATURE_FUEL, true);
		getFeatureFlags().put(FEATURE_STORY, true);
		getFeatureFlags().put(FEATURE_TUTORIAL, true);
	}
	
	public boolean featureOn(String feature) {
		return getFeatureFlags().get(feature) != null && getFeatureFlags().get(feature);
	}
	
	/**
	 * Runs a procedure if the given feature is active.
	 */
	public void featureBoundProcedure(String feature, Runnable procedure) {
		featureBoundProcedure(feature, procedure, null);
	}
	
	/**
	 * Runs a procedure if the given feature is active. Otherwise runs the alternative procedure.
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
	
	public <T>T featureBoundFunction(String feature, Callable<T> function, T defaultValue) {
		try {
			return featureOn(feature) ? function.call() : defaultValue;
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return defaultValue;
		}
	}

	public Map<String, Boolean> getFeatureFlags() {
		return featureFlags;
	}

}
