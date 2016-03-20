package antonafanasjew.cosmodog.writing.dynamics;

import java.util.Map;

import antonafanasjew.cosmodog.writing.model.TextBlock;

import com.google.common.collect.Maps;

public class TextBlockDynamicsCalculatorFactory {

	private static TextBlockDynamicsCalculatorFactory instance = new TextBlockDynamicsCalculatorFactory();
	
	private static TextBlockDynamicsCalculator DEFAULT_DYNAMICS_CALCULATOR = new TextBlockDynamicsDefaultCalculator();
	
	private Map<DynamicsTypes, TextBlockDynamicsCalculator> dynamicsCalculators = Maps.newHashMap();
	
	private TextBlockDynamicsCalculatorFactory() {
		dynamicsCalculators.put(DynamicsTypes.DEFAULT, new TextBlockDynamicsDefaultCalculator());
		dynamicsCalculators.put(DynamicsTypes.STAMP, new TextBlockDynamicsStampCalculator());
		dynamicsCalculators.put(DynamicsTypes.LONG, new TextBlockDynamicsLongCalculator());
		dynamicsCalculators.put(DynamicsTypes.UNSURE, new TextBlockDynamicsUnsureCalculator());
	}
	
	public static TextBlockDynamicsCalculatorFactory instance() {
		return instance;
	}
	
	public TextBlockDynamicsCalculator getDynamicsCalculatorForTextBlock(TextBlock textBlock) {
		String dt = textBlock.getDynamicsType();
		DynamicsTypes type = DynamicsTypes.valueOf(dt.toUpperCase());
		TextBlockDynamicsCalculator calculator = dynamicsCalculators.get(type);
		if (calculator == null) {
			calculator = DEFAULT_DYNAMICS_CALCULATOR;
		}
		return calculator;
	}
	
}
