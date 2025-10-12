package antonafanasjew.cosmodog.math;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * This class represents a function that is defined on the interval between 0.0f and 1.0f (exclusive end) and is composed of sub functions
 * that operate on sub-intervals of 0.0f and 1.0f. The sub-intervals are defined via their durations and are exclusive at the end. The sum of all sub-interval's
 * durations must be exact 1.0f.
 *
 * The main purpose is to create a function that is "broken", or with other words has "bends" at places when one sub function stops
 * applying and the next one starts applying.
 *
 * Example:
 *
 * We have an action that is fixed length. During that action,
 * the player figure must be moved 10 pixels to the left (first quarter of the action),
 * then wait a bit (second and third quarter of the action)
 * and then be moved back to its original position. (The fourth quarter of the action).
 * The movements should be linear over time.
 *
 * We can define the offset of the player's figure during the action as results
 * of different functions over corresponding sub-intervals.
 *
 * Element 1: 0.25 -> f(x) = 10x
 * Element 2: 0.5 -> f(x) = 10
 * Element 2: 0.25 -> f(x) = -10x + 10
 *
 * Note that the definition set of x is [0.0f;1.0f]
 * where 0.0f corresponds to the beginning of the duration
 * and 1.0f corresponds with the end of the duration (but will never be reached, since sub-intervals are open-ended).
 * This way, Element 3 defines that x will be -10 * 0 + 10 = 10 at the beginning of the sub-interval 3
 * (overall duration 0.75f)
 * and x will be almost -10 * 1 + 10 = 0 at the (almost) end of the sub-interval 3
 * (overall duration almost 1.0f)
 * We say "almost", because all sub-intervals are open-ended and their completion 1.0f will never be reached.
 * Instead, the beginning of the subsequent sub-interval will be applied when the completion of the sub-interval is exactly 1.0f.
 * For the same reason, a value has to be provided for the input 1.0f, since the completion of the last sub-interval will never be reached. The value cannot be null.
 *
 * @param <RETURN> The return type of the function
 */
public class BrokenFunction<RETURN> implements Function<Float, RETURN> {

    private final List<BrokenFunctionElement<RETURN>> elements = new ArrayList<>();
    private RETURN valueForFullCompletion;

    @Override
    public RETURN apply(Float completion) {

        double sumOfDurations = elements.stream().mapToDouble(BrokenFunctionElement::getDuration).sum();

        if (completion < 0.0f || completion > 1.0f) {
            throw new RuntimeException("A broken function is only defined between 0.0f and 1.0f (inclusive) but the provided argument was " + completion);
        }

        if (Math.abs(sumOfDurations - 1.0f) > 0.00001) {
            throw new RuntimeException("The sum of durations in the elements of the broken function must be exactly 1.0, but it was " + sumOfDurations);
        }

        if (valueForFullCompletion == null) {
            throw new RuntimeException("The value for full completion (1.0f) must be defined, but it is not.");
        }

        if (completion >= 1.0f) {
            return valueForFullCompletion;
        }

        int relevantElementIndex = 0;
        float completedPartOfRelevantElement = completion;
        while (elements.get(relevantElementIndex).getDuration() <= completedPartOfRelevantElement) {
            completedPartOfRelevantElement -= elements.get(relevantElementIndex).getDuration();
            relevantElementIndex++;
        }

        BrokenFunctionElement<RETURN> relevantElement = elements.get(relevantElementIndex);
        float relevantElementCompletion = completedPartOfRelevantElement / relevantElement.getDuration();
        return relevantElement.getFunction().apply(relevantElementCompletion);

    }

    public void registerElement(BrokenFunctionElement<RETURN> element) {
        elements.add(element);
    }

    public void registerValueForFullCompletion(RETURN value) {
        valueForFullCompletion = value;
    }

}
