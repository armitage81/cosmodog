package antonafanasjew.cosmodog.writing.dynamics;

import java.util.List;

import antonafanasjew.cosmodog.writing.model.TextBlock;

import com.google.common.collect.Lists;

public class TextBlockDynamicsDefaultCalculator implements TextBlockDynamicsCalculator {

	@Override
	public int dynamicsDuration(TextBlock textBlock) {
		return TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS * textBlock.getText().length() + 1;
	}

	@Override
	public List<Integer> letterIntervalDurations(TextBlock textBlock) {
		List<Integer> l = Lists.newArrayList();
		for (int i = 0; i < textBlock.getText().length() + 1; i++) {
			l.add(TextBlockDynamicsCalculator.DEFAULT_INTERVAL_BETWEEN_LETTERS);
		}
		return l;
	}

}
