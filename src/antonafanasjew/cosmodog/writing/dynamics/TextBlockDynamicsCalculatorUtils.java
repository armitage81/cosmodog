package antonafanasjew.cosmodog.writing.dynamics;

import java.util.List;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.writing.model.TextBlock;
import antonafanasjew.cosmodog.writing.model.TextBlockBox;
import antonafanasjew.cosmodog.writing.model.TextBlockLine;

public class TextBlockDynamicsCalculatorUtils {

	public static List<Integer> letterIntervalDurations(TextBlockLine textBlockLine) {
		List<Integer> retVal = Lists.newArrayList();
		for (TextBlock textBlock : textBlockLine) {
			retVal.addAll(TextBlockDynamicsCalculatorFactory.instance().getDynamicsCalculatorForTextBlock(textBlock).letterIntervalDurations(textBlock));
		}
		return retVal;
	}

	public static List<Integer> letterIntervalDurations(TextBlockBox textBlockBox) {
		List<Integer> retVal = Lists.newArrayList();
		for (TextBlockLine textBlockLine : textBlockBox) {
			retVal.addAll(letterIntervalDurations(textBlockLine));
		}
		return retVal;
	}

}
