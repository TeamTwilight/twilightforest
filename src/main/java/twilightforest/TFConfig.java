package twilightforest;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
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
import twilightforest.world.WorldProviderTwilightForest;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Config(modid = TwilightForestMod.ID)
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFConfig {
	@Config.Ignore
	private final static String config = TwilightForestMod.ID + ".config.";

	@Config.LangKey(config + "dimension")
	@Config.Comment("Settings that are not reversible without consequences.")
	public static Dimension dimension = new Dimension();

	public static class Dimension {
		@Config.LangKey(config + "dimension_id")
		@Config.RequiresMcRestart
		@Config.Comment("What ID number to assign to the Twilight Forest dimension. Change if you are having conflicts with another mod.")
		public int dimensionID = 7;

		@Config.LangKey(config + "dimension_seed")
		@Config.RequiresWorldRestart
		@Config.Comment("If set, this will override the normal world seed when generating parts of the Twilight Forest Dimension.")
		public String twilightForestSeed = "";

		@Config.LangKey(config + "spawn_in_tf")
		@Config.Comment("If true, players spawning for the first time will spawn in the Twilight Forest.")
		public boolean newPlayersSpawnInTF = false;

		@Config.LangKey(config + "skylight_forest")
		@Config.RequiresWorldRestart
		@Config.Comment("If true, Twilight Forest will generate as a void except for Major Structures")
		public boolean skylightForest = false;

		@Config.LangKey(config + "skylight_oaks")
		@Config.RequiresWorldRestart
		@Config.Comment("If true, giant Twilight Oaks will also spawn in void worlds")
		public boolean skylightOaks = true;
	}

	@Config.LangKey(config + "compat")
	@Config.Comment("Should TF Compatibility load? Turn off if TF's Compatibility is causing crashes or if not desired.")
	public static boolean doCompat = true;

	@Config.LangKey(config + "tree_tweaks")
	@Config.Comment("Lets you sacrifice various things to improve world performance.")
	public static Performance performance = new Performance();

	public static class Performance {
		@Config.LangKey(config + "canopy_coverage")
		@Config.RangeDouble(min = 0)
		@Config.Comment("Amount of canopy coverage. Lower numbers improve chunk generation speed at the cost of a thinner forest.")
		public float canopyCoverage = 1.7F;

		@Config.LangKey(config + "twilight_oaks")
		@Config.RangeInt(min = 1)
		@Config.Comment("Chance that a chunk in the Twilight Forest will contain a twilight oak tree. Higher numbers reduce the number of trees, increasing performance.")
		public int twilightOakChance = 48;

		@Config.LangKey(config + "leaves_light_opacity")
		@Config.RangeInt(min = 0, max = 255)
		@Config.Comment("This controls the opacity of leaves, changing the amount of light blocked. Can be used to decrease complexity in some lighting checks.")
		public int leavesLightOpacity = 1;

		@Config.LangKey(config + "glacer_packed_ice")
		@Config.Comment("Setting this true will make Twilight Glaciers generate with Packed Ice instead of regular translucent Ice, decreasing amount of light checking calculations.")
		public boolean glacierPackedIce = false;

		@Config.LangKey(config + "enable_skylight")
		@Config.Comment("If the dimension has per-block skylight values. Disabling this will significantly improve world generation performance, at the cost of flat lighting everywhere." +
				"\nWARNING: Once chunks are loaded without skylight, that data is lost and cannot easily be regenerated. Be careful!")
		@Config.RequiresWorldRestart
		public boolean enableSkylight = true;

		@Config.Ignore
		public boolean shadersSupported = true;
	}

	@Config.LangKey(config + "silent_cicadas")
	@Config.Comment("Make cicadas silent for those having sound library problems, or otherwise finding them annoying.")
	public static boolean silentCicadas = false;

	@Config.LangKey(config + "first_person_effects")
	@Config.Comment("Controls whether various effects from the mod are rendered while in first-person view. Turn this off if you find them distracting.")
	public static boolean firstPersonEffects = true;

	@Config.LangKey(config + "portals_in_other_dimensions")
	@Config.Comment("Allow portals to the Twilight Forest to be made outside of dimension 0. May be considered an exploit.")
	public static boolean allowPortalsInOtherDimensions = false;

	@Config.LangKey(config + "admin_portals")
	@Config.Comment("Allow portals only for admins (Operators). This severely reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.")
	public static boolean adminOnlyPortals = false;

	@Config.LangKey(config + "portals")
	@Config.Comment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.")
	public static boolean disablePortalCreation = false;

	@Config.LangKey(config + "portal_creator")
	@Config.Comment("Registry String IDs of items used to create the Twilight Forest Portal. (domain:regname:meta) meta is optional.")
	public static String[] portalCreationItems = {"minecraft:diamond"};

	@Config.LangKey(config + "portal_lighting")
	@Config.Comment("Set this true if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.")
	public static boolean portalLightning = false;

	@Config.LangKey(config + "portal_return")
	@Config.Comment("If false, the return portal will require the activation item.")
	public static boolean shouldReturnPortalBeUsable = true;

	@Config.LangKey(config + "uncrafting")
	@Config.Comment("Disable the uncrafting function of the uncrafting table. Provided as an option when interaction with other mods produces exploitable recipes.")
	public static boolean disableUncrafting = false;

	@Config.LangKey(config + "antibuilder_blacklist")
	@Config.Comment("Anti-Builder blacklist. (domain:block:meta) meta is optional.")
	public static String[] antibuilderBlacklist = {"minecraft:bedrock", "tombmanygraves:grave_block"};

	@Config.LangKey(config + "animate_trophyitem")
	@Config.Comment("Rotate trophy heads on item model. Has no performance impact at all. For those who don't like fun.")
	public static boolean rotateTrophyHeadsGui = true;

	@Config.LangKey(config + "shield_parry")
	public static ShieldInteractions shieldInteractions = new ShieldInteractions();

	public static class ShieldInteractions {
		@Config.LangKey(config + "parry_non_twilight")
		public boolean parryNonTwilightAttacks = false;

		@Config.LangKey(config + "parry_window_arrow")
		@Config.RangeInt(min = 0)
		public int shieldParryTicksArrow = 40;

		@Config.LangKey(config + "parry_window_fireball")
		@Config.RangeInt(min = 0)
		public int shieldParryTicksFireball = 40;

		@Config.LangKey(config + "parry_window_throwable")
		@Config.RangeInt(min = 0)
		public int shieldParryTicksThrowable = 40;

		@Config.LangKey(config + "parry_window_beam")
		@Config.RangeInt(min = 0)
		public int shieldParryTicksBeam = 10;
	}

	@Config.LangKey(config + "loading_screen")
	@Config.Comment("Client only: Controls for the Loading screen")
	public static final LoadingScreen loadingScreen = new LoadingScreen();

	public static class LoadingScreen {
		@Config.LangKey(config + "loading_icon_enable")
		@Config.Comment("Wobble the Loading icon. Has no performance impact at all. For those who don't like fun.")
		public boolean enable = true;

		@Config.LangKey(config + "loading_screen_swap_frequency")
		@Config.RangeInt(min = 0)
		@Config.Comment("How many ticks between each loading screen change. Set to 0 to not cycle at all.")
		public int cycleLoadingScreenFrequency = 0;

		@Config.LangKey(config + "loading_icon_wobble_bounce_frequency")
		@Config.RangeDouble(min = 0F)
		@Config.Comment("Frequency of wobble and bounce.")
		public float frequency = 5F;

		@Config.LangKey(config + "loading_icon_scale")
		@Config.RangeDouble(min = 0F)
		@Config.Comment("Scale of whole bouncy loading icon.")
		public float scale = 3F;

		@Config.LangKey(config + "loading_icon_bounciness")
		@Config.RangeDouble(min = 0F)
		@Config.Comment("How much the loading icon bounces.")
		public float scaleDeviation = 5.25F;

		@Config.LangKey(config + "loading_icon_tilting")
		@Config.RangeDouble(min = 0F, max = 360F)
		@Config.Comment("How far the loading icon wobbles.")
		public float tiltRange = 11.25F;

		@Config.LangKey(config + "loading_icon_tilt_pushback")
		@Config.RangeDouble(min = 0F, max = 360F)
		@Config.Comment("Pushback value to re-center the wobble of loading icon.")
		public float tiltConstant = 22.5F;

		@Config.LangKey(config + "loading_icon_stacks")
		@Config.Comment("List of items to be used for the wobbling Loading Icon. (domain:item:meta) meta is optional.")
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

		@Config.Ignore
		private List<ItemStack> loadingScreenIcons;

		public List<ItemStack> getLoadingScreenIcons() {
			if (loadingScreenIcons == null || loadingScreenIcons.isEmpty()) loadLoadingScreenIcons();
			return loadingScreenIcons;
		}

		void loadLoadingScreenIcons() {
			ImmutableList.Builder<ItemStack> iconList = ImmutableList.builder();

			iconList.addAll(IMCHandler.getLoadingIconStacks());

			for (String s : loadingIconStacks) {
				String[] data = s.split(":");

				Item item = Item.REGISTRY.getObject(new ResourceLocation(data[0], data[1]));
				int meta;

				if (item == null) continue;
				try                 { meta = Integer.parseInt(data[2]); }
				catch (Exception e) { meta = 0;                         }

				iconList.add(new ItemStack(item, 1, meta));
			}

			loadingScreenIcons = iconList.build();
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(TwilightForestMod.ID)) {
			ConfigManager.sync(TwilightForestMod.ID, Config.Type.INSTANCE);
			loadAntiBuilderBlacklist();
			loadingScreen.loadLoadingScreenIcons();
			if (!event.isWorldRunning()) {
				WorldProviderTwilightForest.syncFromConfig();
			}
		}
	}

	private static void loadAntiBuilderBlacklist() {
		ImmutableSet.Builder<IBlockState> builder = ImmutableSet.builder();

		builder.addAll(IMCHandler.getBlacklistedBlocks());

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

				builder.add(block.getStateFromMeta(meta));
			}
		}

		disallowBreakingBlockList = builder.build();
	}

	@Config.Ignore
	private static ImmutableSet<IBlockState> disallowBreakingBlockList;

	public static ImmutableSet<IBlockState> getDisallowedBlocks() {
		if (disallowBreakingBlockList == null || disallowBreakingBlockList.isEmpty()) loadAntiBuilderBlacklist();
		return disallowBreakingBlockList;
	}
}
