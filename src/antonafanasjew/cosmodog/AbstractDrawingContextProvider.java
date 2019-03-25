package antonafanasjew.cosmodog;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public abstract class AbstractDrawingContextProvider {

	protected DrawingContext gameContainerDrawingContext;
	protected DrawingContext sceneDrawingContext;
	protected DrawingContext dialogBoxDrawingContext;
	
	protected DrawingContext geigerCounterDrawingContext;
	
	
	protected DrawingContext supplyTrackerDrawingContext;
	protected DrawingContext timeDrawingContext;
	protected DrawingContext infobitsDrawingContext;
	
	protected DrawingContext lifeDrawingContext;
	protected DrawingContext humanLifeDrawingContext;
	protected DrawingContext robustnessDrawingContext;
	protected DrawingContext fuelDrawingContext;
	
	
	protected DrawingContext vitalDataDrawingContext;
	
	protected DrawingContext leftColumnDrawingContext;
	protected DrawingContext rightColumnDrawingContext;
	protected DrawingContext topBarDrawingContext;
	protected DrawingContext bottomBarDrawingContext;
	
	protected DrawingContext inGameMenuHeaderDrawingContext;
	protected DrawingContext inGameMenuContentDrawingContext;
	protected DrawingContext inGameMenuFooterDrawingContext;
	
	protected DrawingContext gameIntroTextFrameDrawingContext;
	protected DrawingContext gameIntroTextDrawingContext;
	protected DrawingContext gameIntroControlsDrawingContext;
	
	protected DrawingContext cutsceneDrawingContext;
	protected DrawingContext cutsceneTextDrawingContext;
	protected DrawingContext cutsceneControlsDrawingContext;
	
	
	protected DrawingContext gameLogDrawingContext;
	
	protected DrawingContext gameLogHeaderDrawingContext;
	protected DrawingContext gameLogContentDrawingContext;
	protected DrawingContext gameLogControlsDrawingContext;
	
	protected DrawingContext textFrameDrawingContext;
	protected DrawingContext textFrameHeaderDrawingContext;
	protected DrawingContext textFrameContentDrawingContext;
	protected DrawingContext textFrameControlsDrawingContext;
	
	
	protected DrawingContext debugConsoleHeaderDrawingContext;
	protected DrawingContext debugConsoleContentDrawingContext;
	protected DrawingContext debugConsoleControlsDrawingContext;
	
	protected DrawingContext startScreenLogoDrawingContext;
	protected DrawingContext startScreenMenuDrawingContext;
	protected DrawingContext startScreenReferencesDrawingContext;
	
	
	protected DrawingContext logOverviewDrawingContext;
	protected DrawingContext logContentDrawingContext;
	
	protected DrawingContext secretFoundMessageDrawingContext;
	
	
	public DrawingContext gameContainerDrawingContext() {
		return this.gameContainerDrawingContext;
	}
	
	public DrawingContext sceneDrawingContext() {
		return this.sceneDrawingContext;
	}
	
	public DrawingContext dialogBoxDrawingContext() {
		return this.dialogBoxDrawingContext;
	}

	public DrawingContext geigerCounterDrawingContext() {
		return geigerCounterDrawingContext;
	}

	public DrawingContext supplyTrackerDrawingContext() {
		return supplyTrackerDrawingContext;
	}

	public DrawingContext timeDrawingContext() {
		return timeDrawingContext;
	}

	public DrawingContext infobitsDrawingContext() {
		return infobitsDrawingContext;
	}

	public DrawingContext lifeDrawingContext() {
		return lifeDrawingContext;
	}
	
	public DrawingContext humanLifeDrawingContext() {
		return humanLifeDrawingContext;
	}
	
	public DrawingContext robustnessDrawingContext() {
		return robustnessDrawingContext;
	}
	
	public DrawingContext fuelDrawingContext() {
		return fuelDrawingContext;
	}
	
	public DrawingContext vitalDataDrawingContext() {
		return vitalDataDrawingContext;
	}

	public DrawingContext leftColumnDrawingContext() {
		return leftColumnDrawingContext;
	}

	public DrawingContext rightColumnDrawingContext() {
		return rightColumnDrawingContext;
	}

	public DrawingContext topBarDrawingContext() {
		return topBarDrawingContext;
	}

	public DrawingContext bottomBarDrawingContext() {
		return bottomBarDrawingContext;
	}

	public DrawingContext inGameMenuHeaderDrawingContext() {
		return inGameMenuHeaderDrawingContext;
	}
	
	public DrawingContext inGameMenuContentDrawingContext() {
		return inGameMenuContentDrawingContext;
	}
	
	public DrawingContext inGameMenuFooterDrawingContext() {
		return inGameMenuFooterDrawingContext;
	}
	
	public DrawingContext cutsceneDrawingContext() {
		return cutsceneDrawingContext;
	}
	
	public DrawingContext cutsceneTextDrawingContext() {
		return cutsceneTextDrawingContext;
	}
	
	public DrawingContext cutsceneControlsDrawingContext() {
		return cutsceneControlsDrawingContext;
	}
	
	public DrawingContext gameLogDrawingContext() {
		return gameLogDrawingContext;
	}
	
	public DrawingContext gameLogHeaderDrawingContext() {
		return gameLogHeaderDrawingContext;
	}
	
	public DrawingContext gameLogContentDrawingContext() {
		return gameLogContentDrawingContext;
	}
	
	public DrawingContext gameLogControlsDrawingContext() {
		return gameLogControlsDrawingContext;
	}
	
	public DrawingContext textFrameDrawingContext() {
		return textFrameDrawingContext;
	}
	
	public DrawingContext textFrameHeaderDrawingContext() {
		return textFrameHeaderDrawingContext;
	}
	
	public DrawingContext textFrameContentDrawingContext() {
		return textFrameContentDrawingContext;
	}
	
	public DrawingContext textFrameControlsDrawingContext() {
		return textFrameControlsDrawingContext;
	}
	
	public DrawingContext debugConsoleHeaderDrawingContext() {
		return debugConsoleHeaderDrawingContext;
	}
	public DrawingContext debugConsoleContentDrawingContext() {
		return debugConsoleContentDrawingContext;
	}
	public DrawingContext debugConsoleControlsDrawingContext() {
		return debugConsoleControlsDrawingContext;
	}
	
	public DrawingContext startScreenLogoDrawingContext() {
		return startScreenLogoDrawingContext;
	}
	
	public DrawingContext startScreenMenuDrawingContext() {
		return startScreenMenuDrawingContext;
	}
	
	public DrawingContext startScreenReferencesDrawingContext() {
		return startScreenReferencesDrawingContext;
	}

	public DrawingContext logOverviewDrawingContext() {
		return logOverviewDrawingContext;
	}
	
	public DrawingContext logContentDrawingContext() {
		return logContentDrawingContext;
	}
	
	public DrawingContext secretFoundMessageDrawingContext() {
		return secretFoundMessageDrawingContext;
	}
	
	public DrawingContext gameIntroTextFrameDrawingContext() {
		return gameIntroTextFrameDrawingContext;
	}
	
	public DrawingContext gameIntroTextDrawingContext() {
		return gameIntroTextDrawingContext;
	}
	
	public DrawingContext gameIntroControlsDrawingContext() {
		return gameIntroControlsDrawingContext;
	}
	
	public static AbstractDrawingContextProvider createDrawingContextProviderForResolution(int width, int height) {
		if (width == 1920 && height == 1080) {
			return new DrawingContextProvider1920x1080();
		}
		if (width == 1280 && height == 720) {
			return new DrawingContextProvider1280x720();
		}
		throw new RuntimeException(String.format("Undefined resolution %s/%s", width, height));
	}
	
}
