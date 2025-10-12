package antonafanasjew.cosmodog.math;

import java.util.function.Function;

public class BrokenFunctionElement<RETURN> {

    private float duration;
    private Function<Float, RETURN> function;

    private BrokenFunctionElement() {

    }

    public static <T> BrokenFunctionElement<T> instance(float duration, Function<Float, T> f) {
        BrokenFunctionElement<T> instance = new BrokenFunctionElement<>();
        instance.duration = duration;
        instance.function = f;
        return instance;
    }

    public float getDuration() {
        return duration;
    }

    public Function<Float, RETURN> getFunction() {
        return function;
    }
}
