package antonafanasjew.cosmodog.rendering.renderer.functions;

import java.util.function.Function;

public class SpaceLiftSkyTurningBlackFunction implements Function<Float, Float> {

    private final float completionRateBlueSkyAtBegin;
    private final float completionRateTurningBlack;

    public static SpaceLiftSkyTurningBlackFunction instance(float completionRateBlueSkyAtBegin, float completionRateTurningBlack) {
        return new SpaceLiftSkyTurningBlackFunction(completionRateBlueSkyAtBegin, completionRateTurningBlack);
    }

    public SpaceLiftSkyTurningBlackFunction(float completionRateBlueSkyAtBegin, float completionRateTurningBlack) {
        this.completionRateBlueSkyAtBegin = completionRateBlueSkyAtBegin;
        this.completionRateTurningBlack = completionRateTurningBlack;
    }

    @Override
    public Float apply(Float completionRate) {

        float thresholdStartTurningBlack = completionRateBlueSkyAtBegin;
        float thresholdFullyBlack = thresholdStartTurningBlack + completionRateTurningBlack;

        //Blue sky.
        if (completionRate < thresholdStartTurningBlack) {
            return 0.0f;
        }

        //Turning black
        if (completionRate >= thresholdStartTurningBlack && completionRate < thresholdFullyBlack) {
            return (completionRate - thresholdStartTurningBlack) / completionRateTurningBlack;
        }

        //Fully black
        if (completionRate >= thresholdFullyBlack) {
            return 1f;
        }

        return -1f;
    }

}
