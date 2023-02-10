package antonafanasjew.cosmodog.model;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class CollectibleComposed extends Collectible {

	private static final long serialVersionUID = -2874636467823247143L;

	private List<Collectible> elements = Lists.newArrayList();
	
	public CollectibleComposed() {
		super(CollectibleType.COMPOSED);
	}

	public List<Collectible> getElements() {
		return elements;
	}
	
	public void addElement(Collectible element) {
		getElements().add(element);
	}

	@Override
	public String toString() {
		return "[" + elements.stream().map(e -> e.toString()).collect(Collectors.joining(", ")) + "]";
	}

}
