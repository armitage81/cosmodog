package antonafanasjew.cosmodog.writing.dynamics;

import java.util.List;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.writing.model.TextBlock;

public class TextBlockDynamicsUnsureCalculator implements TextBlockDynamicsCalculator {

	@Override
	public int dynamicsDuration(TextBlock textBlock) {
		return (TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS * textBlock.getText().length() + 1) + 2 * TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS;
	}

	@Override
	public List<Integer> letterIntervalDurations(TextBlock textBlock) {
		List<Integer> l = Lists.newArrayList();
		for (int i = 0; i < textBlock.getText().length() + 1; i++) {
			if (i == 1) {
				l.add(3 * TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS);	
			} else {
				l.add(TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS);
			}
		}
		return l;
	}

}
