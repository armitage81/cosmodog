package antonafanasjew.cosmodog.writing.dynamics;

import java.util.List;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.writing.model.TextBlock;

public class TextBlockDynamicsStampCalculator implements TextBlockDynamicsCalculator {

	@Override
	public List<Integer> letterIntervalDurations(TextBlock textBlock) {
		List<Integer> l = Lists.newArrayList();
		for (int i = 0; i < textBlock.getText().length(); i++) {
			l.add(0);
		}
		l.add(TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS * 10);
		return l;
	}

	@Override
	public int dynamicsDuration(TextBlock textBlock) {
		return 0;
	}

}
