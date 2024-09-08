package antonafanasjew.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;
import com.google.common.base.Function;
import com.google.common.collect.Sets;

import java.util.Set;

public class ComposedMovementFunction implements Function<Long, Vector> {

    private Set<AbstractMovementFunction> elements = Sets.newHashSet();

    public Set<AbstractMovementFunction> getElements() {
        return elements;
    }

    @Override
    public Vector apply(Long units) {
        Vector v = Vector.empty();
        for (AbstractMovementFunction e : elements) {
            v = v.add(e.apply(units));
        }
        return v;
    }
}
