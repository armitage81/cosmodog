package antonafanasjew.cosmodog.geometry;

/**
 * Contains methods do calculate oscillations, that is a periodic "there and back again" operations.
 * Can be used to render oscillating objects such as moving machine parts, portal effects etc.
 */
public class Oscillations {

    /**
     * Calculates an oscillation value based on timestamp.
     * Uses the sine function.
     *
     * Oscillation always starts in the middle if the offset is not given.
     *
     * The value returned is between the passed arguments minValue and maxValue.
     * The duration of the oscillation period and the offset of the period can be passed as arguments.
     *
     * @param timestamp Timestamp for which the oscillation will be calculated.
     * @param minValue Minimum value of the oscillation.
     * @param maxValue Maximum value of the oscillation.
     * @param periodDurationInMillis The length of the period of the oscillation.
     * @param periodOffsetInMillis Period offset.
     *
     * @return Oscillation value between minValue and maxValue, depending on the timestamp.
     */
    public static float oscillation (
            long timestamp,
            float minValue,
            float maxValue,
            int periodDurationInMillis,
            int periodOffsetInMillis) {

        float lengthDiff = maxValue - minValue;

        long periodTimeInMillis = (timestamp + periodOffsetInMillis) % periodDurationInMillis;

        double periodTimeInRadians = (double)periodTimeInMillis / (double)periodDurationInMillis * 2 * Math.PI;

        double sinValue = 1 + Math.sin(periodTimeInRadians);

        double sinValueForLengthAmplitude = sinValue / 2 * lengthDiff;

        float retVal = (float)(minValue + sinValueForLengthAmplitude);

        //Cleaning up to avoid rounding errors pushing the result out of bounds.
        if (retVal < minValue) {
            retVal = minValue;
        }

        if (retVal > maxValue) {
            retVal = maxValue;
        }

        return retVal;

    }

}
