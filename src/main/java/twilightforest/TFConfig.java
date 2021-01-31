package twilightforest;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.world.feature.TFGenCaveStalactite;

import java.util.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFConfig {

	public static Common COMMON_CONFIG;
	public static Client CLIENT_CONFIG;

	public static class Common {

		public Common(ForgeConfigSpec.Builder builder) {
			builder.
					comment("Settings that are not reversible without consequences.").
					push("Dimension Settings");
			{
				DIMENSION.newPlayersSpawnInTF = builder.
						translation(config + "spawn_in_tf").
						comment("If true, players spawning for the first time will spawn in the Twilight Forest.").
						define("newPlayersSpawnInTF", false);
				DIMENSION.skylightForest = builder.
						translation(config + "skylight_forest").
						worldRestart().
						comment("If true, Twilight Forest will generate as a void except for Major Structures").
						define("skylightForest", false);
				DIMENSION.skylightOaks = builder.
						translation(config + "skylight_oaks").
						worldRestart().
						comment("If true, giant Twilight Oaks will also spawn in void worlds").
						define("skylightOaks", true);
				DIMENSION.twilightForestID = builder.
						translation(config + "twilight_dimension_id").
						worldRestart().
						comment("Destination dimension for Twilight Portals and some other logic as well").
						define("twilightDimensionID", "twilightforest:twilightforest");
				builder.
						comment("Weights for various small features").
						push("World-Gen Weights");
				{
					DIMENSION.worldGenWeights.stoneCircleWeight = builder.
							translation(config + "stone_circle_weight").
							worldRestart().
							defineInRange("stoneCircleWeight", 10, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.wellWeight = builder.
							translation(config + "well_weight").
							worldRestart().
							defineInRange("wellWeight", 10, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.stalagmiteWeight = builder.
							translation(config + "stalagmite_weight").
							worldRestart().
							defineInRange("stalagmiteWeight", 12, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.foundationWeight = builder.
							translation(config + "foundation_weight").
							worldRestart().
							defineInRange("foundationWeight", 10, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.monolithWeight = builder.
							translation(config + "monolith_weight").
							worldRestart().
							defineInRange("monolithWeight", 10, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.groveRuinsWeight = builder.
							translation(config + "grove_ruins_weight").
							worldRestart().
							defineInRange("groveRuinsWeight", 5, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.hollowStumpWeight = builder.
							translation(config + "hollow_stump_weight").
							worldRestart().
							defineInRange("hollowStumpWeight", 12, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.fallenHollowLogWeight = builder.
							translation(config + "fallen_hollow_log_weight").
							worldRestart().
							defineInRange("fallenHollowLogWeight", 10, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.fallenSmallLogWeight = builder.
							translation(config + "fallen_small_log_weight").
							worldRestart().
							defineInRange("fallenSmallLogWeight", 10, 0, Integer.MAX_VALUE);
					DIMENSION.worldGenWeights.druidHutWeight = builder.
							translation(config + "druid_hut_weight").
							worldRestart().
							defineInRange("druidHutWeight", 10, 0, Integer.MAX_VALUE);
				}
				builder.pop().
						comment("Defines custom stalactites generated in hollow hills." +

								"\nFormat is \"modid:block size maxLength minHeight weight\", where the properties are:" +

								"\nSize - the maximum length of the stalactite relative to the space between hill floor and ceiling," +

								"\nMax length - maximum length of a stalactite in blocks," +

								"\nMin height - minimum space between the hill floor and the stalactite to let it generate," +

								"\nWeight - how often it generates." +

								"\n\nFor example: \"minecraft:iron_ore 0.7 8 1 24\" would add a stalactite equal to the default iron ore stalactite.").
						push("Custom Hollow Hill Stalactites");
				{
					DIMENSION.hollowHillStalactites.largeHill = builder.
							translation(config + "large_hill").
							worldRestart().
							comment("Blocks generating as stalactites in large hills only").
							define("largeHill", Collections.emptyList());
					DIMENSION.hollowHillStalactites.mediumHill = builder.
							translation(config + "medium_hill").
							worldRestart().
							comment("Blocks generating as stalactites in medium and large hills").
							define("mediumHill", Collections.emptyList());
					DIMENSION.hollowHillStalactites.smallHill = builder.
							translation(config + "small_hill").
							worldRestart().
							comment("Blocks generating as stalactites in all hills").
							define("mediumHill", Collections.emptyList());
					DIMENSION.hollowHillStalactites.useConfigOnly = builder.
							translation(config + "stalactite_config_only").
							worldRestart().
							comment("If true, default stalactites and stalactites defined by other mods will not be used.").
							define("useConfigOnly", false);
				}
				builder.pop();
			}
			builder.pop();
			doCompat = builder.
					worldRestart().
					comment("Should TF Compatibility load? Turn off if TF's Compatibility is causing crashes or if not desired.").
					define("doCompat", true);
			builder.
					comment("Lets you sacrifice various things to improve world performance.").
					push("Performance Tweaks");
			{
				PERFORMANCE.canopyCoverage = builder.
						translation(config + "canopy_coverage").
						comment("Amount of canopy coverage. Lower numbers improve chunk generation speed at the cost of a thinner forest.").
						defineInRange("canopyCoverage", 1.7F, 0, Double.MAX_VALUE);
				PERFORMANCE.twilightOakChance = builder.
						translation(config + "twilight_oaks").
						comment("Chance that a chunk in the Twilight Forest will contain a twilight oak tree. Higher numbers reduce the number of trees, increasing performance.").
						defineInRange("twilightOakChance", 48, 1, Integer.MAX_VALUE);
				PERFORMANCE.leavesLightOpacity = builder.
						translation(config + "leaves_light_opacity").
						comment("This controls the opacity of leaves, changing the amount of light blocked. Can be used to decrease complexity in some lighting checks.").
						defineInRange("leavesLightOpacity", 1, 0, 255);
				PERFORMANCE.glacierPackedIce = builder.
						translation(config + "glacier_packed_ice").
						comment("Setting this true will make Twilight Glaciers generate with Packed Ice instead of regular translucent Ice, decreasing amount of light checking calculations.").
						define("glacierPackedIce", false);
				PERFORMANCE.enableSkylight = builder.
						translation(config + "enable_skylight").
						comment("If the dimension has per-block skylight values. Disabling this will significantly improve world generation performance, at the cost of flat lighting everywhere." +

								"\nWARNING: Once chunks are loaded without skylight, that data is lost and cannot easily be regenerated. Be careful!").
						worldRestart().
						define("enableSkylight", true);
			}
			builder.pop();
			originDimension = builder.
					translation(config + "origin_dimension").
					comment("The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).").
					define("originDimension", "minecraft:overworld");
			allowPortalsInOtherDimensions = builder.
					translation(config + "portals_in_other_dimensions").
					comment("Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.").
					define("allowPortalsInOtherDimensions", false);
			adminOnlyPortals = builder.
					translation(config + "admin_portals").
					comment("Allow portals only for admins (Operators). This severely reduces the range in which the mod usually scans for valid portal conditions, and it scans near ops only.").
					define("adminOnlyPortals", false);
			disablePortalCreation = builder.
					translation(config + "portals").
					comment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.").
					define("disablePortalCreation", false);
			//portalCreationItems = builder.
			//		translation(config + "portal_creator").
			//		comment("Registry String IDs of items used to create the Twilight Forest Portal. (domain:regname).").
			//		define("portalCreationItems", Collections.singletonList("minecraft:diamond"));
			checkPortalDestination = builder.
					translation(config + "check_portal_destination").
					comment("Determines if new portals should be pre-checked for safety. If enabled, portals will fail to form rather than redirect to a safe alternate destination." +

							"\nNote that enabling this also reduces the rate at which portal formation checks are performed.").
					define("checkPortalDestination", false);
			portalLightning = builder.
					translation(config + "portal_lighting").
					comment("Set this true if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.").
					define("portalLightning", false);
			shouldReturnPortalBeUsable = builder.
					translation(config + "portal_return").
					comment("If false, the return portal will require the activation item.").
					define("shouldReturnPortalBeUsable", true);
			progressionRuleDefault = builder.
					translation(config + "progression_default").
					comment("Sets the default value of the game rule controlling enforced progression.").
					define("progressionRuleDefault", true);
			disableUncrafting = builder.
					worldRestart().
					translation(config + "uncrafting").
					comment("Disable the uncrafting function of the uncrafting table. Provided as an option when interaction with other mods produces exploitable recipes.").
					define("disableUncrafting", false);
			builder.
					comment("We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.").
					push("Shield Parrying");
			{
				SHIELD_INTERACTIONS.parryNonTwilightAttacks = builder.
						translation(config + "parry_non_twilight").
						comment("Set to true to parry non-Twilight projectiles.").
						define("parryNonTwilightAttacks", false);
				SHIELD_INTERACTIONS.shieldParryTicksArrow = builder.
						translation(config + "parry_window_arrow").
						comment("The amount of ticks after raising a shield that makes it OK to parry an arrow.").
						defineInRange("shieldParryTicksArrow", 40, 0, Integer.MAX_VALUE);
				SHIELD_INTERACTIONS.shieldParryTicksFireball = builder.
						translation(config + "parry_window_fireball").
						comment("The amount of ticks after raising a shield that makes it OK to parry a fireball.").
						defineInRange("shieldParryTicksFireball", 40, 0, Integer.MAX_VALUE);
				SHIELD_INTERACTIONS.shieldParryTicksThrowable = builder.
						translation(config + "parry_window_throwable").
						comment("The amount of ticks after raising a shield that makes it OK to parry a thrown item.").
						defineInRange("shieldParryTicksThrowable", 40, 0, Integer.MAX_VALUE);
				SHIELD_INTERACTIONS.shieldParryTicksBeam = builder.
						translation(config + "parry_window_beam").
						defineInRange("shieldParryTicksBeam", 10, 0, Integer.MAX_VALUE);
			}
			builder.pop();
		}

		public Dimension DIMENSION = new Dimension();

		public static class Dimension {

			public ForgeConfigSpec.BooleanValue newPlayersSpawnInTF;
			public ForgeConfigSpec.BooleanValue skylightForest;
			public ForgeConfigSpec.BooleanValue skylightOaks;
			public ForgeConfigSpec.ConfigValue<String> twilightForestID;

			public WorldGenWeights worldGenWeights = new WorldGenWeights();

			public static class WorldGenWeights {

				//TODO: Due to the new way of Feature generation, should these be chances?
				public ForgeConfigSpec.IntValue stoneCircleWeight;
				public ForgeConfigSpec.IntValue wellWeight;
				public ForgeConfigSpec.IntValue stalagmiteWeight;
				public ForgeConfigSpec.IntValue foundationWeight;
				public ForgeConfigSpec.IntValue monolithWeight;
				public ForgeConfigSpec.IntValue groveRuinsWeight;
				public ForgeConfigSpec.IntValue hollowStumpWeight;
				public ForgeConfigSpec.IntValue fallenHollowLogWeight;
				public ForgeConfigSpec.IntValue fallenSmallLogWeight;
				public ForgeConfigSpec.IntValue druidHutWeight;
			}

			public HollowHillStalactites hollowHillStalactites = new HollowHillStalactites();

			public class HollowHillStalactites {

				public ForgeConfigSpec.ConfigValue<List<? extends String>> largeHill;
				public ForgeConfigSpec.ConfigValue<List<? extends String>> mediumHill;
				public ForgeConfigSpec.ConfigValue<List<? extends String>> smallHill;
				public ForgeConfigSpec.BooleanValue useConfigOnly;

				public void load() {
					registerHill(smallHill.get(), 1);
					registerHill(mediumHill.get(), 2);
					registerHill(largeHill.get(), 3);
				}

				private void registerHill(List<? extends String> definitions, int tier) {
					for (String definition : definitions) {
						if (!parseStalactite(definition, tier)) {
							TwilightForestMod.LOGGER.warn("Invalid hollow hill stalactite definition: {}", definition);
						}
					}
				}

				private boolean parseStalactite(String definition, int tier) {
					String[] split = definition.split(" ");
					if (split.length != 5) return false;

					Optional<Block> block = parseBlock(split[0]);
					if (!block.isPresent()) return false;

					try {
						TFGenCaveStalactite.addStalactite(tier, block.get().getDefaultState(),
								Float.parseFloat(split[1]),
								Integer.parseInt(split[2]),
								Integer.parseInt(split[3]),
								Integer.parseInt(split[4])
						);
					} catch (NumberFormatException e) {
						return false;
					}
					return true;
				}
			}
		}

		public ForgeConfigSpec.BooleanValue doCompat;

		public Performance PERFORMANCE = new Performance();

		public static class Performance {
			public ForgeConfigSpec.DoubleValue canopyCoverage;
			public ForgeConfigSpec.IntValue twilightOakChance;
			public ForgeConfigSpec.IntValue leavesLightOpacity;
			public ForgeConfigSpec.BooleanValue glacierPackedIce;
			public ForgeConfigSpec.BooleanValue enableSkylight;

			public boolean shadersSupported = true;
		}

		public ForgeConfigSpec.ConfigValue<String> originDimension;
		public ForgeConfigSpec.BooleanValue allowPortalsInOtherDimensions;
		public ForgeConfigSpec.BooleanValue adminOnlyPortals;
		public ForgeConfigSpec.BooleanValue disablePortalCreation;
		public ForgeConfigSpec.ConfigValue<List<? extends String>> portalCreationItems;
		public ForgeConfigSpec.BooleanValue checkPortalDestination;
		public ForgeConfigSpec.BooleanValue portalLightning;
		public ForgeConfigSpec.BooleanValue shouldReturnPortalBeUsable;
		public ForgeConfigSpec.BooleanValue progressionRuleDefault;
		public ForgeConfigSpec.BooleanValue disableUncrafting;

		public ShieldInteractions SHIELD_INTERACTIONS = new ShieldInteractions();

		public static class ShieldInteractions {

			public ForgeConfigSpec.BooleanValue parryNonTwilightAttacks;
			public ForgeConfigSpec.IntValue shieldParryTicksArrow;
			public ForgeConfigSpec.IntValue shieldParryTicksFireball;
			public ForgeConfigSpec.IntValue shieldParryTicksThrowable;
			public ForgeConfigSpec.IntValue shieldParryTicksBeam;
		}

	}

	public static class Client {

		public Client(ForgeConfigSpec.Builder builder) {
			silentCicadas = builder.
					translation(config + "silent_cicadas").
					comment("Make cicadas silent for those having sound library problems, or otherwise finding them annoying.").
					define("silentCicadas", false);
			firstPersonEffects = builder.
					translation(config + "first_person_effects").
					comment("Controls whether various effects from the mod are rendered while in first-person view. Turn this off if you find them distracting.").
					define("firstPersonEffects", true);
			rotateTrophyHeadsGui = builder.
					translation(config + "animate_trophyitem").
					comment("Rotate trophy heads on item model. Has no performance impact at all. For those who don't like fun.").
					define("rotateTrophyHeadsGui", true);
			builder.
					comment("Client only: Controls for the Loading screen").
					push("Loading Screen");
			{
				LOADING_SCREEN.enable = builder.
						translation(config + "loading_icon_enable").
						comment("Wobble the Loading icon. Has no performance impact at all. For those who don't like fun.").
						define("enable", true);
				LOADING_SCREEN.cycleLoadingScreenFrequency = builder.
						translation(config + "loading_screen_swap_frequency").
						comment("How many ticks between each loading screen change. Set to 0 to not cycle at all.").
						defineInRange("cycleLoadingScreenFrequency", 0, 0, Integer.MAX_VALUE);
				LOADING_SCREEN.frequency = builder.
						translation(config + "loading_icon_wobble_bounce_frequency").
						comment("Frequency of wobble and bounce.").
						defineInRange("frequency", 5F, 0F, Double.MAX_VALUE);
				LOADING_SCREEN.scale = builder.
						translation(config + "loading_icon_scale").
						comment("Scale of whole bouncy loading icon.").
						defineInRange("scale", 3F, 0F, Double.MAX_VALUE);
				LOADING_SCREEN.scaleDeviation = builder.
						translation(config + "loading_icon_bounciness").
						comment("How much the loading icon bounces.").
						defineInRange("scaleDeviation", 5.25F, 0F, Double.MAX_VALUE);
				LOADING_SCREEN.tiltRange = builder.
						translation(config + "loading_icon_tilting").
						comment("How far the loading icon wobbles.").
						defineInRange("tiltRange", 11.25F, 0F, 360F);
				LOADING_SCREEN.tiltConstant = builder.
						translation(config + "loading_icon_tilt_pushback").
						comment("Pushback value to re-center the wobble of loading icon.").
						defineInRange("tiltConstant", 22.5F, 0F, 360F);
				LOADING_SCREEN.loadingIconStacks = builder.
						translation(config + "loading_icon_stacks").
						comment("List of items to be used for the wobbling Loading Icon. (domain:item).").
						defineList("loadingIconStacks", Arrays.asList(
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
						), s -> s instanceof String && ResourceLocation.tryCreate((String) s) != null);
			}
			builder.pop();
		}

		public ForgeConfigSpec.BooleanValue silentCicadas;
		public ForgeConfigSpec.BooleanValue firstPersonEffects;
		public ForgeConfigSpec.BooleanValue rotateTrophyHeadsGui;

		public final LoadingScreen LOADING_SCREEN = new LoadingScreen();

		public static class LoadingScreen {

			public ForgeConfigSpec.BooleanValue enable;
			public ForgeConfigSpec.IntValue cycleLoadingScreenFrequency;
			public ForgeConfigSpec.DoubleValue frequency;
			public ForgeConfigSpec.DoubleValue scale;
			public ForgeConfigSpec.DoubleValue scaleDeviation;
			public ForgeConfigSpec.DoubleValue tiltRange;
			public ForgeConfigSpec.DoubleValue tiltConstant;
			public ForgeConfigSpec.ConfigValue<List<? extends String>> loadingIconStacks;

			private ImmutableList<ItemStack> loadingScreenIcons;

			public ImmutableList<ItemStack> getLoadingScreenIcons() {
				return loadingScreenIcons;
			}

			void loadLoadingScreenIcons() {
				ImmutableList.Builder<ItemStack> iconList = ImmutableList.builder();

				iconList.addAll(IMCHandler.getLoadingIconStacks());

				for (String s : loadingIconStacks.get()) {
					parseItemStack(s).ifPresent(iconList::add);
				}

				loadingScreenIcons = iconList.build();
			}
		}

	}

	private static final String config = TwilightForestMod.ID + ".config.";

	@SubscribeEvent
	public static void onConfigChanged(ModConfig.Reloading event) {
		if (event.getConfig().getModId().equals(TwilightForestMod.ID)) {
//			TFDimensions.checkOriginDimension();
			build();
		}
	}

	public static void build() {
		//buildPortalIngredient();
		CLIENT_CONFIG.LOADING_SCREEN.loadLoadingScreenIcons();
	}

	/*public static Ingredient portalIngredient;

	private static void buildPortalIngredient() {
		List<ItemStack> stacks = new ArrayList<>();

		for (String s : COMMON_CONFIG.portalCreationItems.get()) {
			parseItemStack(s).ifPresent(stacks::add);
		}

		if (stacks.isEmpty()) {
			stacks.add(new ItemStack(Items.DIAMOND));
		}

		portalIngredient = Ingredient.fromStacks(stacks.toArray(new ItemStack[0]));
	}*/

	private static Optional<ItemStack> parseItemStack(String string) {
		ResourceLocation id = ResourceLocation.tryCreate(string);
		if (id == null || !ForgeRegistries.ITEMS.containsKey(id)) {
			return Optional.empty();
		} else {
			return Optional.of(new ItemStack(ForgeRegistries.ITEMS.getValue(id)));
		}
	}

	private static Optional<Block> parseBlock(String string) {
		ResourceLocation id = ResourceLocation.tryCreate(string);
		if (id == null || !ForgeRegistries.BLOCKS.containsKey(id)) {
			return Optional.empty();
		} else {
			return Optional.of(ForgeRegistries.BLOCKS.getValue(id));
		}
	}
}
