package antonafanasjew.cosmodog.writing.dynamics;

import java.util.List;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.writing.model.TextBlock;

public class TextBlockDynamicsLongCalculator implements TextBlockDynamicsCalculator {

	@Override
	public int dynamicsDuration(TextBlock textBlock) {
		return 3 * (TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS * textBlock.getText().length() + 1);
	}

	@Override
	public List<Integer> letterIntervalDurations(TextBlock textBlock) {
		List<Integer> l = Lists.newArrayList();
		for (int i = 0; i < textBlock.getText().length() + 1; i++) {
			l.add(3 * TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS);
		}
		return l;
	}

}
