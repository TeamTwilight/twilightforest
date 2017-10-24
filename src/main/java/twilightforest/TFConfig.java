package twilightforest;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Config(modid = TwilightForestMod.ID)
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
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

	@Config.Comment("Meta of item used to create the Twilight Forest Portal, -1 for any metadata")
	public static int portalCreationMeta = -1;

	@Config.Comment("Anti-Builder blacklist. (domain:block:meta) meta is optional")
	public static String[] antibuilderBlacklist = {"minecraft:bedrock", "tombmanygraves:grave_block"};

	@Config.Comment("Rotate trophy heads in gui model. Has close to no performance impact at all. For those who don't like fun.")
	public static boolean rotateTrophyHeadsGui = true;

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(TwilightForestMod.ID)) {
			ConfigManager.sync(TwilightForestMod.ID, Config.Type.INSTANCE);
		}
	}

	public static List<IBlockState> getAntiBuilderBlacklist() {
		List<IBlockState> blacklist = Lists.newArrayList();
		for (String s : antibuilderBlacklist) {
			String[] data = s.split(":");
			if (data.length < 2)
				continue;
			Block block = Block.REGISTRY.getObject(new ResourceLocation(data[0], data[1]));
			if (block != Blocks.AIR) {
				int meta = 0;
				if (data.length >= 3) {
					try {
						meta = Integer.parseInt(data[2]);
					} catch (NumberFormatException e) {
						meta = 0;
					}
				}
				blacklist.add(block.getStateFromMeta(meta));
			}
		}
		return blacklist;
	}
}
