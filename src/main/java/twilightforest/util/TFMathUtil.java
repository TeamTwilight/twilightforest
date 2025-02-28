package twilightforest.util;

public abstract class TFMathUtil {
	public static double interpolateToTarget(double oValue, double targetValue, double dtInTicks, double TAU) {
		return targetValue - (targetValue - oValue) * Math.exp(-dtInTicks / TAU);
	}
}
