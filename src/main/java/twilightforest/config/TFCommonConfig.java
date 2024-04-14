package twilightforest.config;

import net.minecraft.commands.Commands;
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
		builder.comment("Settings that are not reversible without consequences.").push("Dimension Settings");
		{
			DIMENSION.newPlayersSpawnInTF = builder
					.translation(TFConfig.CONFIG_ID + "spawn_in_tf")
					.comment("If true, players spawning for the first time will spawn in the Twilight Forest.")
					.define("newPlayersSpawnInTF", false);
			DIMENSION.portalForNewPlayerSpawn = builder
					.translation(TFConfig.CONFIG_ID + "portal_for_new_player")
					.comment("If true, the return portal will spawn for new players that were sent to the TF if `spawn_in_tf` is true.")
					.define("portalForNewPlayer", false);
		}
		builder.pop();

		builder.comment("All settings regarding the Twilight Forest Portal are found here").push("Portal Settings");
		{
			PORTAL.originDimension = builder
					.translation(TFConfig.CONFIG_ID + "origin_dimension")
					.comment("The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).")
					.define("originDimension", "minecraft:overworld");
			PORTAL.allowPortalsInOtherDimensions = builder
					.translation(TFConfig.CONFIG_ID + "portals_in_other_dimensions")
					.comment("Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.")
					.define("allowPortalsInOtherDimensions", false);
			PORTAL.portalCreationPermission = builder
					.translation(TFConfig.CONFIG_ID + "portal_permission")
					.comment("""
							Allows people with the specified permission or higher to create portals. This is based off of Vanilla's permission system.
							You can read about them here: https://minecraft.wiki/w/Permission_level""")
					.defineInRange("portalCreationPermission", 0, 0, Commands.LEVEL_OWNERS);
			PORTAL.disablePortalCreation = builder
					.translation(TFConfig.CONFIG_ID + "portals")
					.comment("Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.")
					.define("disablePortalCreation", false);
			PORTAL.checkPortalPlacement = builder
					.translation(TFConfig.CONFIG_ID + "check_portal_placement")
					.comment("""
							Determines if new portals should be pre-checked for safety. If false, portals will fail to form rather than redirect to a safe alternate destination.
							Note that disabling this also reduces the rate at which portal formation checks are performed.""")
					.define("checkPortalPlacement", true);
			PORTAL.destructivePortalLightning = builder
					.translation(TFConfig.CONFIG_ID + "destructive_portal_lighting")
					.comment("Set this to false if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.")
					.define("destructivePortalLightning", true);
			PORTAL.shouldReturnPortalBeUsable = builder
					.translation(TFConfig.CONFIG_ID + "portal_return")
					.comment("If false, the return portal will require the activation item.")
					.define("shouldReturnPortalBeUsable", true);
			PORTAL.portalAdvancementLock = builder
					.translation(TFConfig.CONFIG_ID + "portal_unlocked_by_advancement")
					.comment("Use a valid advancement resource location as a string. For example, using the string \"minecraft:story/mine_diamond\" will lock the portal behind the \"Diamonds!\" advancement. Invalid/Empty Advancement resource IDs will leave the portal entirely unlocked.")
					.define("portalUnlockedByAdvancement", "");
			PORTAL.maxPortalSize = builder
					.translation(TFConfig.CONFIG_ID + "max_portal_size")
					.comment("The max amount of water spaces the mod will check for when creating a portal. Very high numbers may cause performance issues.")
					.defineInRange("maxPortalSize", 64, 4, Integer.MAX_VALUE);
		}
		builder.pop();

		casketUUIDLocking = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "casket_uuid_locking")
				.comment("""
						If true, Keepsake Caskets that are spawned when a player dies will not be accessible by other players. Use this if you dont want people taking from other people's death caskets.
						NOTE: server operators will still be able to open locked caskets.""")
				.define("casketUUIDLocking", false);

		disableSkullCandles = builder
				.translation(TFConfig.CONFIG_ID + "disable_skull_candles")
				.comment("If true, disables the ability to make Skull Candles by right clicking a vanilla skull with a candle. Turn this on if you're having mod conflict issues for some reason.")
				.define("disableSkullCandleCreation", false);

		defaultItemEnchants = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "default_item_enchantments")
				.comment("""
						If false, items that come enchanted when you craft them (such as ironwood or steeleaf gear) will not show this way in the creative inventory.
						Please note that this doesnt affect the crafting recipes themselves, you will need a datapack to change those.""")
				.define("showEnchantmentsOnItems", true);

		bossDropChests = builder
				.translation(TFConfig.CONFIG_ID + "boss_drop_chests")
				.comment("""
						If true, Twilight Forest's bosses will put their drops inside of a chest where they originally spawned instead of dropping the loot directly.
						Note that the Knight Phantoms are not affected by this as their drops work differently.""")
				.define("bossesSpawnDropChests", true);

		cloudBlockPrecipitationDistance = builder
				.translation(TFConfig.CONFIG_ID + "cloud_block_precipitation_distance_server")
				.comment("""
						Dictates how many blocks down from a cloud block should the game logic check for handling weather related code.
						Lower if experiencing low tick rate. Set to 0 to turn all cloud precipitation logic off.""")
				.defineInRange("cloudBlockPrecipitationDistance", 32, 0, Integer.MAX_VALUE);

		multiplayerFightAdjuster = builder
				.worldRestart()
				.translation(TFConfig.CONFIG_ID + "multiplayer_fight_adjuster")
				.comment("""
						Determines how bosses should adjust to multiplayer fights. There are 4 possible values that can be put here:
						NONE: doesnt do anything when multiple people participate in a bossfight. Bosses will act the same as they do in singleplayer or solo fights.
						MORE_LOOT: adds additional drops to a boss' loot table based on how many players participated in the fight. These are fully controlled through the entity's loot table, using the `twilightforest:multiplayer_multiplier` loot function. Note that this function will only do things to entities that are included in the `twilightforest:multiplayer_inclusive_entities` tag.
						MORE_HEALTH: increases the health of each boss by 20 hearts for each player nearby when the fight starts.
						MORE_LOOT_AND_HEALTH: does both of the above functions for each boss.""")
				.defineEnum("multiplayerFightAdjuster", TFConfig.MultiplayerFightAdjuster.NONE);

		builder.comment("Settings for all things related to the Uncrafting Table.").push("Uncrafting Table");
		{
			UNCRAFTING_STUFFS.uncraftingXpCostMultiplier = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "uncrafting_xp_cost")
					.comment("""
							Multiplies the total XP cost of uncrafting an item and rounds up.
							Higher values means the recipe will cost more to uncraft, lower means less. Set to 0 to disable the cost altogether.
							Note that this only affects reversed crafting recipes, uncrafting recipes will still use the same cost as they normally would.""")
					.defineInRange("uncraftingXpCostMultiplier", 1.0D, 0.0D, Double.MAX_VALUE);
			UNCRAFTING_STUFFS.repairingXpCostMultiplier = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "repairing_xp_cost")
					.comment("""
							Multiplies the total XP cost of repairing an item and rounds up.
							Higher values means the recipe will cost more to repair, lower means less. Set to 0 to disable the cost altogether.""")
					.defineInRange("repairingXpCostMultiplier", 1.0D, 0.0D, Double.MAX_VALUE);
			UNCRAFTING_STUFFS.disableUncraftingRecipes = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "uncrafting_recipes")
					.comment("""
							If you don't want to disable uncrafting altogether, and would rather disable certain recipes, this is for you.
							To add a recipe, add the mod id followed by the name of the recipe. You can check this in things like JEI.
							Example: "twilightforest:firefly_particle_spawner" will disable uncrafting the particle spawner into a firefly jar, firefly, and poppy.
							If an item has multiple crafting recipes and you wish to disable them all, add the item to the "twilightforest:banned_uncraftables" item tag.
							If you have a problematic ingredient, like infested towerwood for example, add the item to the "twilightforest:banned_uncrafting_ingredients" item tag.""")
					.defineListAllowEmpty("disableUncraftingRecipes", List.of("twilightforest:giant_log_to_oak_planks"), s -> s instanceof String);
			UNCRAFTING_STUFFS.reverseRecipeBlacklist = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "uncrafting_recipes_flip")
					.comment("If true, this will invert the above uncrafting recipe list from a blacklist to a whitelist.")
					.define("flipRecipeList", false);
			UNCRAFTING_STUFFS.blacklistedUncraftingModIds = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "uncrafting_mod_ids")
					.comment("""
							Here, you can disable all items from certain mods from being uncrafted.
							Input a valid mod id to disable all uncrafting recipes from that mod.
							Example: "twilightforest" will disable all uncrafting recipes from this mod.""")
					.defineListAllowEmpty("blacklistedUncraftingModIds", new ArrayList<>(), s -> s instanceof String);
			UNCRAFTING_STUFFS.flipUncraftingModIdList = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "uncrafting_mod_id_flip")
					.comment("If true, this will invert the above option from a blacklist to a whitelist.")
					.define("flipIdList", false);
			UNCRAFTING_STUFFS.allowShapelessUncrafting = builder
					.worldRestart().
					translation(TFConfig.CONFIG_ID + "shapeless_uncrafting")
					.comment("""
							If true, the uncrafting table will also be allowed to uncraft shapeless recipes.
							The table was originally intended to only take shaped recipes, but this option remains for people who wish to keep the functionality.""")
					.define("enableShapelessCrafting", false);
			UNCRAFTING_STUFFS.disableIngredientSwitching = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "disable_ingredient_switching")
					.comment("""
							If true, the uncrafting table will will no longer allow you to switch between ingredients if a recipe uses a tag for crafting.
							This will remove the functionality for ALL RECIPES!
							If you want to prevent certain ingredients from showing up in the first place, use the "twilightforest:banned_uncrafting_ingredients" tag.""")
					.define("disableIngredientSwitching", false);
			UNCRAFTING_STUFFS.disableUncraftingOnly = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "disable_uncrafting")
					.comment("""
							Disables the uncrafting function of the uncrafting table. Recommended as a last resort if there's too many things to change about its behavior (or you're just lazy, I dont judge).
							Do note that special uncrafting recipes are not disabled as the mod relies on them for other things.""")
					.define("disableUncrafting", false);
			UNCRAFTING_STUFFS.disableEntireTable = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "disable_uncrafting_table")
					.comment("""
							Disables any usage of the uncrafting table, as well as prevents it from showing up in loot or crafted.
							Please note that table has more uses than just uncrafting, you can read about them here! http://benimatic.com/tfwiki/index.php?title=Uncrafting_Table
							It is highly recommended to keep the table enabled as the mod has special uncrafting exclusive recipes, but the option remains for people that dont want the table to be functional at all.
							If you are looking to just prevent normal crafting recipes from being reversed, consider using the 'disableUncrafting' option instead.""")
					.define("disableUncraftingTable", false);
		}
		builder.pop();

		builder.comment("Settings for all things related to the magic trees.").push("Magic Trees");
		{
			MAGIC_TREES.timeRange = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "time_range")
					.comment("""
							Defines the radius at which the Timewood Core works. Can be a number anywhere between 1 and 128.
							Set to 0 to prevent the Timewood Core from functioning.""")
					.defineInRange("timeCoreRange", 16, 0, 128);

			MAGIC_TREES.transformationRange = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "transformation_range")
					.comment("""
							Defines the radius at which the Transformation Core works. Can be a number anywhere between 1 and 128.
							Set to 0 to prevent the Transformation Core from functioning.""")
					.defineInRange("transformationCoreRange", 16, 0, 128);

			MAGIC_TREES.miningRange = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "mining_range")
					.comment("""
							Defines the radius at which the Minewood Core works. Can be a number anywhere between 1 and 128.
							Set to 0 to prevent the Minewood Core from functioning.""")
					.defineInRange("miningCoreRange", 16, 0, 128);

			MAGIC_TREES.sortingRange = builder
					.worldRestart()
					.translation(TFConfig.CONFIG_ID + "sorting_range")
					.comment("""
							Defines the radius at which the Sortingwood Core works. Can be a number anywhere between 1 and 128.
							Set to 0 to prevent the Sortingwood Core from functioning.""")
					.defineInRange("sortingCoreRange", 16, 0, 128);
		}
		builder.pop();

		builder.comment("We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.").push("Shield Parrying");
		{
			SHIELD_INTERACTIONS.parryNonTwilightAttacks = builder
					.translation(TFConfig.CONFIG_ID + "parry_non_twilight")
					.comment("Set to true to parry non-Twilight projectiles.")
					.define("parryNonTwilightAttacks", false);
			SHIELD_INTERACTIONS.shieldParryTicks = builder
					.translation(TFConfig.CONFIG_ID + "parry_window")
					.comment("The amount of ticks after raising a shield that makes it OK to parry a projectile. (1 tick = 1/20 second)")
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
