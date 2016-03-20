package antonafanasjew.cosmodog.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import antonafanasjew.cosmodog.globals.Constants;

import com.google.common.collect.Lists;

/**
 * Represents a score list. Singleton
 */
public class ScoreList extends ArrayList<ScoreEntry> {

	private static final long serialVersionUID = -3873507549917746865L;

	public static final int MAX_ELEMENTS = Constants.MAX_ELEMENTS_IN_SCORE_LIST;
	
	private static ScoreList instance = new ScoreList();

	private ScoreList() {

	}
	
	public static ScoreList getInstance() {
		return instance;
	}

	public void addNewScoreEntry(ScoreEntry scoreEntry) {
		List<ScoreEntry> l = Lists.newArrayList();
		l.addAll(this);
		l.add(scoreEntry);
		Collections.sort(l);
		Collections.reverse(l);
		if (l.size() > MAX_ELEMENTS) {
			l = l.subList(0, MAX_ELEMENTS);
		}
		this.clear();
		this.addAll(l);
	}

}
