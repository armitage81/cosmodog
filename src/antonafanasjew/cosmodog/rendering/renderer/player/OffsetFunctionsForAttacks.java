package antonafanasjew.cosmodog.rendering.renderer.player;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.math.BrokenFunction;
import antonafanasjew.cosmodog.math.BrokenFunctionElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class OffsetFunctionsForAttacks {

    private static OffsetFunctionsForAttacks instance = new OffsetFunctionsForAttacks();

    public static OffsetFunctionsForAttacks instance() {
        return instance;
    }

    private final Random random = new Random();

    private Supplier<Short> randomOffsetProvider = () -> {
        short[] values = {-1, 0};
        return values[random.nextInt(values.length)];
    };

    private OffsetFunctionsForAttacks() {

    }

    public BrokenFunction<Float> functionForWeaponType(WeaponType weaponType, float maxOffset) {
        return switch (weaponType) {
            case FISTS -> {
                float firstOffset = maxOffset / 4.0f;
                BrokenFunction<Float> offsetFunction = new BrokenFunction<>();
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.5f, x -> firstOffset * x));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.5f, x -> -firstOffset * x + firstOffset));
                offsetFunction.registerValueForFullCompletion(0.0f);
                yield offsetFunction;
            }
            case PISTOL -> {
                float firstOffset = maxOffset / 8.0f;
                float remainingOffset = maxOffset / 4.0f;
                float wholeOffset = firstOffset + remainingOffset;
                BrokenFunction<Float> offsetFunction = new BrokenFunction<>();
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.2f, x -> firstOffset * x));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.2f, x -> firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.2f, x -> remainingOffset * x + firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.4f, x -> -wholeOffset * x + wholeOffset));
                offsetFunction.registerValueForFullCompletion(0.0f);
                yield offsetFunction;
            }
            case SHOTGUN -> {
                float firstOffset = maxOffset / 6.0f;
                BrokenFunction<Float> offsetFunction = new BrokenFunction<>();
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.2f, x -> firstOffset * x));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.1f, x -> firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.1f, x -> -firstOffset * x + firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.6f, x -> 0.0f));
                offsetFunction.registerValueForFullCompletion(0.0f);
                yield offsetFunction;
            }
            case RIFLE -> {
                float firstOffset = maxOffset / 4.0f;
                float recoilOffset = firstOffset - maxOffset / 8.0f;
                BrokenFunction<Float> offsetFunction = new BrokenFunction<>();
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.1f, x -> firstOffset * x));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.4f, x -> firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.4f, x -> recoilOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.1f, x -> -recoilOffset * x + recoilOffset));
                offsetFunction.registerValueForFullCompletion(0.0f);
                yield offsetFunction;
            }
            case MACHINEGUN -> {
                float firstOffset = maxOffset / 4.0f;
                BrokenFunction<Float> offsetFunction = new BrokenFunction<>();
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.8f, x ->  (float)randomOffsetProvider.get()));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.2f, x -> 0.0f));
                offsetFunction.registerValueForFullCompletion(0.0f);
                yield offsetFunction;
            }
            case RPG -> {
                float firstOffset = maxOffset / 6.0f;
                float negativeOffset = maxOffset / 3.0f;
                BrokenFunction<Float> offsetFunction = new BrokenFunction<>();
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.4f, x -> firstOffset * x));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.2f, x -> firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.1f, x -> (-firstOffset - negativeOffset) * x + firstOffset));
                offsetFunction.registerElement(BrokenFunctionElement.instance(0.3f, x -> negativeOffset * x - negativeOffset));
                offsetFunction.registerValueForFullCompletion(0.0f);
                yield offsetFunction;
            }
            default -> throw new IllegalArgumentException();
        };
    }

    public void setRandomOffsetProvider(Supplier<Short> randomOffsetProvider) {
        this.randomOffsetProvider = randomOffsetProvider;
    }
}
