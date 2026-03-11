package twilightforest.init;

import twilightforest.TwilightForestMod;

public abstract class TFKeyBindsCategories {
	public record Category(String internalName) {}
	public static final Category TRAVELLERS_GEAR = new Category(addCategoryPrefix("travellers_gear"));

	@SuppressWarnings("SameParameterValue")
	private static String addCategoryPrefix(String s) {
		return "key." + TwilightForestMod.ID + ".categories." + s;
	}
}
