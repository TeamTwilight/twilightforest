package twilightforest;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	private final static String config = TwilightForestMod.ID + ".config.";

	@Config.LangKey(config + "dimension")
	public static final Dimension dimension = new Dimension();

	public static class Dimension {
		@Config.LangKey(config + "dimension_id")
		@Config.RequiresMcRestart
		public int dimensionID = 7;

		@Config.LangKey(config + "dimension_seed")
		@Config.RequiresWorldRestart
		public String twilightForestSeed = "";
	}

	@Config.LangKey(config + "tree_tweaks")
	public static final Performance performance = new Performance();

	public static class Performance {
		@Config.LangKey(config + "canopy_coverage")
		@Config.RangeDouble(min = 0)
		public float canopyCoverage = 1.7F;

		@Config.LangKey(config + "twilight_oaks")
		@Config.RangeInt(min = 0)
		public int twilightOakChance = 48;
	}

	@Config.LangKey(config + "silent_cicadas")
	public static boolean silentCicadas = false;

	@Config.LangKey(config + "portals_in_other_dimensions")
	public static boolean allowPortalsInOtherDimensions = false;

	@Config.LangKey(config + "admin_portals")
	public static boolean adminOnlyPortals = false;

	@Config.LangKey(config + "portals")
	public static boolean disablePortalCreation = false;

	@Config.LangKey(config + "portal_creator")
	public static String portalCreationItem = "minecraft:diamond";

	@Config.LangKey(config + "portal_creator_meta")
	@Config.RangeInt(min = -1)
	public static int portalCreationMeta = -1;

	@Config.LangKey(config + "uncrafting")
	public static boolean disableUncrafting = false;

	@Config.LangKey(config + "antibuilder_blacklist")
	public static String[] antibuilderBlacklist = {"minecraft:bedrock", "tombmanygraves:grave_block"};

	@Config.LangKey(config + "animate_trophyitem")
	public static boolean rotateTrophyHeadsGui = true;

	@Config.LangKey(config + "loading_screen")
	public static final TFConfig.loadingScreen loadingScreen = new loadingScreen();

	public static class loadingScreen {
		@Config.LangKey(config + "loading_icon_enable")
		public boolean enable = true;

		@Config.LangKey(config + "loading_screen_swap_frequency")
		@Config.RangeInt(min = 0)
		public int cycleLoadingScreenFrequency = 0;

		@Config.LangKey(config + "loading_icon_wobble_bounce_frequency")
		@Config.RangeDouble(min = 0F)
		public float frequency = 5F;

		@Config.LangKey(config + "loading_icon_scale")
		@Config.RangeDouble(min = 0F)
		public float scale = 3F;

		@Config.LangKey(config + "loading_icon_bounciness")
		@Config.RangeDouble(min = 0F)
		public float scaleDeviation = 5.25F;

		@Config.LangKey(config + "loading_icon_tilting")
		@Config.RangeDouble(min = 0F, max = 360F)
		public float tiltRange = 11.25F;

		@Config.LangKey(config + "loading_icon_tilt_pushback")
		@Config.RangeDouble(min = 0F, max = 360F)
		public float tiltConstant = 22.5F;

		@Config.LangKey(config + "loading_icon_stacks")
		public String[] loadingIconStacks = {
				"twilightforest:experiment_115",
				"twilightforest:magic_map",
				"twilightforest:charm_of_life_2",
				"twilightforest:charm_of_keeping_3",
				"twilightforest:phantom_helmet",
				"twilightforest:lamp_of_cinders",
				"twilightforest:carminite",
				"twilightforest:block_and_chain",
				"twilightforest:yeti_helmet",
				"twilightforest:hydra_chop",
				"twilightforest:magic_beans",
				"twilightforest:ironwood_raw",
				"twilightforest:naga_scale",
				"twilightforest:experiment_115:2",
				"twilightforest:miniature_structure",
				"twilightforest:miniature_structure:6",
				"twilightforest:knightmetal_block",
				"twilightforest:tower_device:10",
				"twilightforest:twilight_sapling:5",
				"twilightforest:twilight_sapling:6",
				"twilightforest:twilight_sapling:7",
				"twilightforest:twilight_sapling:8",
				"twilightforest:twilight_sapling:9",
				"twilightforest:borer_essence"
		};

		private List<ItemStack> loadingScreenIcons;

		public List<ItemStack> getLoadingScreenIcons() {
			if (loadingScreenIcons == null || loadingScreenIcons.isEmpty()) loadLoadingScreenIcons();
			return loadingScreenIcons;
		}

		void loadLoadingScreenIcons() {
			List<ItemStack> iconList = Lists.newArrayList();

			for (String s : loadingIconStacks) {
				String[] data = s.split(":");

				Item item = Item.REGISTRY.getObject(new ResourceLocation(data[0], data[1]));
				int meta;

				if (item == null) continue;
				try                 { meta = Integer.parseInt(data[2]); }
				catch (Exception e) { meta = 0;                         }

				iconList.add(new ItemStack(item, 1, meta));
			}

			loadingScreenIcons = iconList;
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(TwilightForestMod.ID)) {
			ConfigManager.sync(TwilightForestMod.ID, Config.Type.INSTANCE);

			loadAntiBuilderBlacklist();

			loadingScreen.loadLoadingScreenIcons();
		}
	}

	private static void loadAntiBuilderBlacklist() {
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
						TwilightForestMod.LOGGER.warn("Had a slight hiccup processing \"" + s + "\" as part of the antibuilder blacklist.");
						meta = 0;
					}
				}
				blacklist.add(block.getStateFromMeta(meta));
			}
		}

		antibuilderStateBlacklist = blacklist;
	}

	private static List<IBlockState> antibuilderStateBlacklist;

	public static List<IBlockState> getAntiBuilderBlacklist() {
		if (antibuilderStateBlacklist == null || antibuilderStateBlacklist.isEmpty()) loadAntiBuilderBlacklist();
		return antibuilderStateBlacklist;
	}
}
