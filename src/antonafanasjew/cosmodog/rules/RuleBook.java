package antonafanasjew.cosmodog.rules;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

public class RuleBook extends HashMap<String, Rule> {

	private static final long serialVersionUID = -7491804142103966638L;
	
	public List<Rule> getRulesSortedByPriority() {
		
		List<Rule> retVal = Lists.newArrayList(values());
		
		retVal.sort(new Comparator<Rule>() {

			@Override
			public int compare(Rule o1, Rule o2) {
				if (o1.getPriority() < o2.getPriority()) {
					return -1;
				}
				
				if (o1.getPriority() > o2.getPriority()) {
					return 1;
				}
				
				return 1;
			}
			
		});
		
		return retVal;
	}
	
}
