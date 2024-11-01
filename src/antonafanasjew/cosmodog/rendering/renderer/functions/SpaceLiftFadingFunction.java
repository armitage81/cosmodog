package antonafanasjew.cosmodog.rendering.renderer.functions;

import java.util.function.Function;

public class SpaceLiftFadingFunction implements Function<Float, Float> {

    private float completionRateDarknessAtBegin;
    private float completionRateDarknessAtEnd;
    private float completionRateFadingIn;
    private float completionRateFadingOut;
    private float completionRateFullVisibility;

    public static SpaceLiftFadingFunction instance(float completionRateDaknessAtBegin, float completionRateDarknessAtEnd, float completionRateFadingIn, float completionRateFadingOut) {
        return new SpaceLiftFadingFunction(completionRateDaknessAtBegin, completionRateDarknessAtEnd, completionRateFadingIn, completionRateFadingOut);
    }

    public SpaceLiftFadingFunction(float completionRateDaknessAtBegin, float completionRateDarknessAtEnd, float completionRateFadingIn, float completionRateFadingOut) {
        this.completionRateDarknessAtBegin = completionRateDaknessAtBegin;
        this.completionRateDarknessAtEnd = completionRateDarknessAtEnd;
        this.completionRateFadingIn = completionRateFadingIn;
        this.completionRateFadingOut = completionRateFadingOut;
        this.completionRateFullVisibility = 1 - completionRateDaknessAtBegin - completionRateFadingIn - completionRateFadingOut - completionRateDarknessAtEnd;
    }

    @Override
    public Float apply(Float completionRate) {

        float thresholdStartFadingIn = completionRateDarknessAtBegin;
        float thresholdFullOpacity = thresholdStartFadingIn + completionRateFadingIn;
        float thresholdStartFadingOut = 1 - completionRateFadingOut - completionRateDarknessAtEnd;
        float thresholdDarknessAtTheEnd = 1 - completionRateDarknessAtEnd;

        //Darkness
        if (completionRate < thresholdStartFadingIn) {
            return 0.0f;
        }

        //Fading in
        if (completionRate >= thresholdStartFadingIn && completionRate < thresholdFullOpacity) {
            return (completionRate - thresholdStartFadingIn) / completionRateFadingIn;
        }

        //Full visibility
        if (completionRate >= thresholdFullOpacity && completionRate < thresholdStartFadingOut) {
            return 1f;
        }

        //Fading out
        if (completionRate >= thresholdStartFadingOut && completionRate < thresholdDarknessAtTheEnd) {
            return 1 - ((completionRate - thresholdStartFadingOut)) / completionRateFadingOut;
        }

        //Darkness
        if (completionRate >= thresholdDarknessAtTheEnd) {
            return 0.0f;
        }

        return -1f;
    }

}
