package twilightforest.config;

import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class TFCommonConfig {

	final Dimension DIMENSION = new Dimension();
	final Portal PORTAL = new Portal();
	final MagicTrees MAGIC_TREES = new MagicTrees();
	final UncraftingStuff UNCRAFTING_STUFFS = new UncraftingStuff();
	final ShieldInteractions SHIELD_INTERACTIONS = new ShieldInteractions();

	final ModConfigSpec.BooleanValue casketUUIDLocking;
	final ModConfigSpec.BooleanValue disableSkullCandles;
	final ModConfigSpec.BooleanValue defaultItemEnchants;
	final ModConfigSpec.BooleanValue bossDropChests;
	final ModConfigSpec.IntValue cloudBlockPrecipitationDistance;
	final ModConfigSpec.EnumValue<TFConfig.MultiplayerFightAdjuster> multiplayerFightAdjuster;

	public TFCommonConfig(ModConfigSpec.Builder builder) {
		builder.comment(ConfigComments.DIMENSION).translation(TFConfig.CONFIG_ID + "dim_settings").push("Dimension Settings");
		{
			DIMENSION.newPlayersSpawnInTF = builder
				.translation(TFConfig.CONFIG_ID + "spawn_in_tf")
				.comment(ConfigComments.SPAWN_IN_TF)
				.define("newPlayersSpawnInTF", false);
			DIMENSION.portalForNewPlayerSpawn = builder
				.translation(TFConfig.CONFIG_ID + "portal_for_new_player")
				.comment(ConfigComments.NEW_PORTAL)
				.define("portalForNewPlayer", false);
		}
		builder.pop();

		builder.comment(ConfigComments.PORTAL).translation(TFConfig.CONFIG_ID + "portal_settings").push("Portal Settings");
		{
			PORTAL.originDimension = builder
				.translation(TFConfig.CONFIG_ID + "origin_dimension")
				.comment(ConfigComments.ORIGIN_DIMENSION)
				.define("originDimension", "minecraft:overworld");
			PORTAL.allowPortalsInOtherDimensions = builder
				.translation(TFConfig.CONFIG_ID + "portals_in_other_dimensions")
				.comment(ConfigComments.OTHER_DIMENSION_PORTALS)
				.define("allowPortalsInOtherDimensions", false);
			PORTAL.portalCreationPermission = builder
				.translation(TFConfig.CONFIG_ID + "portal_permission")
				.comment(ConfigComments.PORTAL_PERMISSION)
				.defineInRange("portalCreationPermission", 0, 0, Commands.LEVEL_OWNERS);
			PORTAL.disablePortalCreation = builder
				.translation(TFConfig.CONFIG_ID + "disable_portal")
				.comment(ConfigComments.DISABLE_PORTAL)
				.define("disablePortalCreation", false);
			PORTAL.checkPortalPlacement = builder
				.translation(TFConfig.CONFIG_ID + "check_portal_placement")
				.comment(ConfigComments.CHECK_PORTAL)
				.define("checkPortalPlacement", true);
			PORTAL.destructivePortalLightning = builder
				.translation(TFConfig.CONFIG_ID + "destructive_portal_lighting")
				.comment(ConfigComments.PORTAL_LIGHTNING)
				.define("destructivePortalLightning", true);
			PORTAL.shouldReturnPortalBeUsable = builder
				.translation(TFConfig.CONFIG_ID + "portal_return")
				.comment(ConfigComments.RETURN_PORTAL)
				.define("shouldReturnPortalBeUsable", true);
			PORTAL.portalAdvancementLock = builder
				.translation(TFConfig.CONFIG_ID + "portal_unlocked_by_advancement")
				.comment(ConfigComments.PORTAL_ADVANCEMENT)
				.define("portalUnlockedByAdvancement", "");
			PORTAL.maxPortalSize = builder
				.translation(TFConfig.CONFIG_ID + "max_portal_size")
				.comment(ConfigComments.PORTAL_SIZE)
				.defineInRange("maxPortalSize", 64, 4, Integer.MAX_VALUE);
		}
		builder.pop();

		casketUUIDLocking = builder
			.worldRestart()
			.translation(TFConfig.CONFIG_ID + "casket_uuid_locking")
			.comment(ConfigComments.CASKET_UUID_LOCKING)
			.define("casketUUIDLocking", false);

		disableSkullCandles = builder
			.translation(TFConfig.CONFIG_ID + "disable_skull_candles")
			.comment(ConfigComments.DISABLE_SKULL_CANDLES)
			.define("disableSkullCandleCreation", false);

		defaultItemEnchants = builder
			.worldRestart()
			.translation(TFConfig.CONFIG_ID + "default_item_enchantments")
			.comment(ConfigComments.DEFAULT_ENCHANTS)
			.define("showEnchantmentsOnItems", true);

		bossDropChests = builder
			.translation(TFConfig.CONFIG_ID + "boss_drop_chests")
			.comment(ConfigComments.BOSS_CHESTS)
			.define("bossesSpawnDropChests", true);

		cloudBlockPrecipitationDistance = builder
			.translation(TFConfig.CONFIG_ID + "cloud_precipitation")
			.comment(ConfigComments.CLOUD_PRECIP_SERVER)
			.defineInRange("cloudBlockPrecipitationDistance", 32, 0, Integer.MAX_VALUE);

		multiplayerFightAdjuster = builder
			.worldRestart()
			.translation(TFConfig.CONFIG_ID + "multiplayer_fight_adjuster")
			.comment(ConfigComments.MULTIPLAYER_ADJUSTER)
			.defineEnum("multiplayerFightAdjuster", TFConfig.MultiplayerFightAdjuster.NONE);

		builder.comment(ConfigComments.UNCRAFTING_TABLE).translation(TFConfig.CONFIG_ID + "uncrafting").push("Uncrafting Table");
		{
			UNCRAFTING_STUFFS.uncraftingXpCostMultiplier = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "uncrafting_xp_cost")
				.comment(ConfigComments.UNCRAFTING_MULTIPLIER)
				.defineInRange("uncraftingXpCostMultiplier", 1.0D, 0.0D, Double.MAX_VALUE);
			UNCRAFTING_STUFFS.repairingXpCostMultiplier = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "repairing_xp_cost")
				.comment(ConfigComments.REPAIR_MULTIPLIER)
				.defineInRange("repairingXpCostMultiplier", 1.0D, 0.0D, Double.MAX_VALUE);
			UNCRAFTING_STUFFS.disableUncraftingRecipes = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "uncrafting_recipes")
				.comment(ConfigComments.RECIPE_WHITELIST)
				.defineListAllowEmpty("disableUncraftingRecipes", List.of("twilightforest:giant_log_to_oak_planks"), () -> "", s -> s instanceof String);
			UNCRAFTING_STUFFS.reverseRecipeBlacklist = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "uncrafting_recipes_flip")
				.comment(ConfigComments.RECIPE_BLACKLIST)
				.define("flipRecipeList", false);
			UNCRAFTING_STUFFS.blacklistedUncraftingModIds = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "uncrafting_mod_ids")
				.comment(ConfigComments.MOD_ID_WHITELIST)
				.defineListAllowEmpty("blacklistedUncraftingModIds", new ArrayList<>(), () -> "", s -> s instanceof String string && ResourceLocation.isValidNamespace(string));
			UNCRAFTING_STUFFS.flipUncraftingModIdList = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "uncrafting_mod_id_flip")
				.comment(ConfigComments.MOD_ID_BLACKLIST)
				.define("flipIdList", false);
			UNCRAFTING_STUFFS.allowShapelessUncrafting = builder
				.worldRestart().
				translation(TFConfig.CONFIG_ID + "shapeless_uncrafting")
				.comment(ConfigComments.SHAPELESS_UNCRAFTING)
				.define("enableShapelessCrafting", false);
			UNCRAFTING_STUFFS.disableIngredientSwitching = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "ingredient_switching")
				.comment(ConfigComments.INGREDIENT_SWITCHING)
				.define("disableIngredientSwitching", false);
			UNCRAFTING_STUFFS.disableUncraftingOnly = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "disable_uncrafting")
				.comment(ConfigComments.DISABLE_UNCRAFTING)
				.define("disableUncrafting", false);
			UNCRAFTING_STUFFS.disableEntireTable = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "disable_uncrafting_table")
				.comment(ConfigComments.DISABLE_TABLE)
				.define("disableUncraftingTable", false);
		}
		builder.pop();

		builder.comment(ConfigComments.MAGIC_TREES).translation(TFConfig.CONFIG_ID + "magic_trees").push("Magic Trees");
		{
			MAGIC_TREES.timeRange = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "time_range")
				.comment(ConfigComments.TIME_CORE)
				.defineInRange("timeCoreRange", 16, 0, 128);

			MAGIC_TREES.transformationRange = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "transformation_range")
				.comment(ConfigComments.TRANFORMATION_CORE)
				.defineInRange("transformationCoreRange", 16, 0, 128);

			MAGIC_TREES.miningRange = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "mining_range")
				.comment(ConfigComments.MINING_CORE)
				.defineInRange("miningCoreRange", 16, 0, 128);

			MAGIC_TREES.sortingRange = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "sorting_range")
				.comment(ConfigComments.SORTING_CORE)
				.defineInRange("sortingCoreRange", 16, 0, 128);
		}
		builder.pop();

		builder.comment(ConfigComments.SHIELD_PARRYING).translation(TFConfig.CONFIG_ID + "shield").push("Shield Parrying");
		{
			SHIELD_INTERACTIONS.parryNonTwilightAttacks = builder
				.translation(TFConfig.CONFIG_ID + "parry_non_twilight")
				.comment(ConfigComments.PARRY_NON_TF)
				.define("parryNonTwilightAttacks", false);
			SHIELD_INTERACTIONS.shieldParryTicks = builder
				.translation(TFConfig.CONFIG_ID + "parry_window")
				.comment(ConfigComments.PARRY_WINDOW)
				.defineInRange("shieldParryTicksArrow", 40, 0, Integer.MAX_VALUE);
		}
		builder.pop();
	}

	static class Dimension {
		ModConfigSpec.BooleanValue newPlayersSpawnInTF;
		ModConfigSpec.BooleanValue portalForNewPlayerSpawn;
	}

	static class Portal {
		ModConfigSpec.ConfigValue<String> originDimension;
		ModConfigSpec.BooleanValue allowPortalsInOtherDimensions;
		ModConfigSpec.IntValue portalCreationPermission;
		ModConfigSpec.BooleanValue disablePortalCreation;
		ModConfigSpec.BooleanValue checkPortalPlacement;
		ModConfigSpec.BooleanValue destructivePortalLightning;
		ModConfigSpec.BooleanValue shouldReturnPortalBeUsable;
		ModConfigSpec.ConfigValue<String> portalAdvancementLock;
		ModConfigSpec.IntValue maxPortalSize;
	}

	static class UncraftingStuff {
		ModConfigSpec.DoubleValue uncraftingXpCostMultiplier;
		ModConfigSpec.DoubleValue repairingXpCostMultiplier;
		ModConfigSpec.BooleanValue allowShapelessUncrafting;
		ModConfigSpec.BooleanValue disableIngredientSwitching;
		ModConfigSpec.ConfigValue<List<? extends String>> disableUncraftingRecipes;
		ModConfigSpec.BooleanValue reverseRecipeBlacklist;
		ModConfigSpec.ConfigValue<List<? extends String>> blacklistedUncraftingModIds;
		ModConfigSpec.BooleanValue flipUncraftingModIdList;
		ModConfigSpec.BooleanValue disableUncraftingOnly;
		ModConfigSpec.BooleanValue disableEntireTable;
	}

	static class MagicTrees {
		ModConfigSpec.IntValue timeRange;
		ModConfigSpec.IntValue transformationRange;
		ModConfigSpec.IntValue miningRange;
		ModConfigSpec.IntValue sortingRange;
	}

	static class ShieldInteractions {
		ModConfigSpec.BooleanValue parryNonTwilightAttacks;
		ModConfigSpec.IntValue shieldParryTicks;
	}
}
