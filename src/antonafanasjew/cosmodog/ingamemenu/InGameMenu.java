package antonafanasjew.cosmodog.ingamemenu;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class InGameMenu {

	private int startMenuFrameIndex;
	private int currentMenuFrameIndex;
	
	private List<InGameMenuFrame> menuFrames = Lists.newArrayList();

	public InGameMenu(InGameMenuFrame... inGameMenuFrames) {
		this(0, inGameMenuFrames);
	}
	
	public InGameMenu(int startIndex, InGameMenuFrame... inGameMenuFrames) {
		Preconditions.checkState(startIndex < inGameMenuFrames.length);
		for (InGameMenuFrame inGameMenuFrame : inGameMenuFrames) {
			this.getMenuFrames().add(inGameMenuFrame);
		}
		this.startMenuFrameIndex = startIndex;
		this.currentMenuFrameIndex = startIndex;
	}

	public InGameMenuFrame currentMenuFrame() {
		return menuFrames.get(currentMenuFrameIndex);
	}

	public void switchToNextMenuFrame() {
		currentMenuFrameIndex = (currentMenuFrameIndex + 1) % getMenuFrames().size();
	}
	
	public void switchToPreviousMenuFrame() {
		currentMenuFrameIndex = (currentMenuFrameIndex - 1);
		if (currentMenuFrameIndex < 0) {
			currentMenuFrameIndex = getMenuFrames().size() - 1;
		}
	}
	
	public int getStartMenuFrameIndex() {
		return startMenuFrameIndex;
	}

	public void setStartMenuFrameIndex(int startMenuFrameIndex) {
		this.startMenuFrameIndex = startMenuFrameIndex;
	}

	public int getCurrentMenuFrameIndex() {
		return currentMenuFrameIndex;
	}

	public void setCurrentMenuFrameIndex(int currentMenuFrameIndex) {
		this.currentMenuFrameIndex = currentMenuFrameIndex;
	}

	public List<InGameMenuFrame> getMenuFrames() {
		return menuFrames;
	}

}
