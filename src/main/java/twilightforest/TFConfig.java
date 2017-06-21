package twilightforest;

import net.minecraftforge.common.config.Config;

@Config(modid = TwilightForestMod.ID)
public class TFConfig {

	public static final Dimension dimension = new Dimension();

	public static class Dimension {
		@Config.Comment("What ID number to assign to the Twilight Forest dimension.  Change if you are having conflicts with another mod.")
		public int dimensionID = 7;

		@Config.Comment("If set, this will override the normal world seed when generating parts of the Twilight Forest Dimension.")
		public String twilightForestSeed = "";
	}

	public static final Performance performance = new Performance();

	public static class Performance {
		@Config.Comment("Amount of canopy coverage.  Lower numbers improve chunk generation speed at the cost of a thinner forest.")
		@Config.RangeDouble(min = 0)
		public float canopyCoverage = 1.7F;

		@Config.Comment("Chance that a chunk in the Twilight Forest will contain a twilight oak tree.  Higher numbers reduce the number of trees, increasing performance.")
		public int twilightOakChance = 48;
	}

	@Config.Comment("Make cicadas silent  for those having sound library problems, or otherwise finding them annoying")
	public static boolean silentCicadas = false;

	@Config.Comment("Allow portals to the Twilight Forest to be made outside of dimension 0.  May be considered an exploit.")
	public static boolean allowPortalsInOtherDimensions = false;

	@Config.Comment("Allow portals only for admins (ops).  This severly reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.")
	public static boolean adminOnlyPortals = false;

	@Config.Comment("Disable Twilight Forest portal creation entirely.  Provided for server operators looking to restrict action to the dimension.")
	public static boolean disablePortalCreation = false;

	@Config.Comment("Disable the uncrafting function of the uncrafting table.  Provided as an option when interaction with other mods produces exploitable recipes.")
	public static boolean disableUncrafting = false;

	@Config.Comment("Name of item used to create the Twilight Forest Portal")
	public static String portalCreationItem = "minecraft:diamond";

}
