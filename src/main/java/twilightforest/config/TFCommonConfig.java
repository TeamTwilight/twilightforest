package twilightforest.config;

import net.minecraft.server.permissions.PermissionLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TFCommonConfig {

	final Dimension DIMENSION = new Dimension();
	final Portal PORTAL = new Portal();
	final MagicTrees MAGIC_TREES = new MagicTrees();
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
				.defineEnum("portalCreationPermission", PermissionLevel.ALL);
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
				.comment(ConfigComments.TRANSFORMATION_CORE)
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
		ModConfigSpec.EnumValue<PermissionLevel> portalCreationPermission;
		ModConfigSpec.BooleanValue disablePortalCreation;
		ModConfigSpec.BooleanValue checkPortalPlacement;
		ModConfigSpec.BooleanValue destructivePortalLightning;
		ModConfigSpec.BooleanValue shouldReturnPortalBeUsable;
		ModConfigSpec.ConfigValue<String> portalAdvancementLock;
		ModConfigSpec.IntValue maxPortalSize;
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
