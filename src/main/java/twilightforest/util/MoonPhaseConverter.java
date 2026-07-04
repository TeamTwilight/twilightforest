package twilightforest.util;

public class MoonPhaseConverter {
	private static final String[] moonPhases = new String[]{
		"full", "waning_gibbous", "third_quarter", "waning_crescent",
		"new", "waxing_crescent", "first_quarter", "waxing_gibbous"
	};


	public static int convertPhaseToIndex(String phaseType) {
		for (int i = 0; i < moonPhases.length; i++) {
			if (moonPhases[i].equals(phaseType)) return i;
		}
		return 0;
	}

	public static String convertIndexToPhase(int index) {
		return moonPhases[index];
	}
}
