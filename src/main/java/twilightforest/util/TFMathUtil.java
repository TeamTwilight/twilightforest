package twilightforest.util;

import java.util.Arrays;

public abstract class TFMathUtil {
	public static double interpolateToTarget(double oValue, double targetValue, double dtInTicks, double TAU) {
		return targetValue - (targetValue - oValue) * Math.exp(-dtInTicks / TAU);
	}

	public static double probabilityOfAtLeastOneSuccess(double successProbability, double tries) {
		return(1 - Math.pow(1 - successProbability, tries));
	}

	public static int taxicabGeometryDistance(int... coordinateDiffs) {
		return Arrays.stream(coordinateDiffs).map(Math::abs).sum();
	}

	public static int chebyshevGeometryDistance(int... coordinateDiffs) {
		return Arrays.stream(coordinateDiffs).map(Math::abs).max().orElse(0);
	}
}
