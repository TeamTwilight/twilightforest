package twilightforest.config;

public final class ConfigComments {
	//CLIENT
	public static final String SILENT_CICADAS = "Makes Cicadas silent for those having sound library problems or finding them annoying.";
	public static final String SILENT_CICADAS_ON_HEAD = "Makes Cicadas silent when one is on your head. If the above option is set to true, this won't have any effect.";
	public static final String SCREEN_SHAKE = "Controls whether the screen shakes when a Magic Bean is in the process of growing.";
	public static final String ANIMATE_TROPHIES = "Rotate Trophy heads on item model. Has no performance impact at all. For those who don't like fun.";
	public static final String OPTIFINE = "Disable the nag screen when OptiFine is installed.";
	public static final String LOCKED_TOASTS = "Disables the toasts that appear when entering a locked biome. Not recommended if you're unfamiliar with progression.";
	public static final String QUESTING_RAM_WOOL = "Renders a check mark or X above your crosshair while holding wool when hovering over the Questing Ram depending on if that color of wool has been fed to it already.";
	public static final String FORTIFICATION = """
		Renders how many fortification shields are currently active on your player above your armor bar.
		"Turn this off if other mods render over/under it.""";
	public static final String FORTIFICATION_CREATIVE = "Enables the fortification shield indicator in creative for debugging.";
	public static final String CLOUD_PRECIP_CLIENT = """
		Renders precipitation underneath cloud blocks. -1 sets it to be synced with the common config.
		Set this to a lower number if experiencing poor performance, set to 0 to turn it off""";
	public static final String GIANT_SKINS = "List of player UUIDs whose skins the Giants should use. Leave the list empty to use the skin of the player viewing them.";
	public static final String AURORA_SHADER = "Defines which biomes the aurora shader effect will appear in. Leave the list empty to disable the effect.";
	public static final String PRETTIFY_ORE_METER = "Lines up the dashes and percentages in the Ore Meter GUI.";
	public static final String CHARMS_AS_TOTEMS = "Makes Charms when triggered display like the Totem of Undying instead of our own effects.";

	//COMMON
	public static final String DIMENSION = "Settings that are not reversible without consequences.";
	public static final String SPAWN_IN_TF = "If true, players spawning for the first time will spawn in the Twilight Forest.";
	public static final String NEW_PORTAL = "If true, the return portal will spawn for new players that were sent to the TF if `newPlayersSpawnInTF` is true.";

	public static final String PORTAL = "All settings regarding the Twilight Forest Portal are found here";
	public static final String ORIGIN_DIMENSION = "The dimension you can always travel to the Twilight Forest from, as well as the dimension you will return to. Defaults to the overworld. (domain:regname).";
	public static final String OTHER_DIMENSION_PORTALS = "Allow portals to the Twilight Forest to be made outside of the 'origin' dimension. May be considered an exploit.";
	public static final String PORTAL_PERMISSION = """
					Allows people with the specified permission or higher to create portals. This is based off of Vanilla's permission system.
					You can read about them here: https://minecraft.wiki/w/Permission_level""";
	public static final String DISABLE_PORTAL = "Disable Twilight Forest portal creation entirely. Provided for server operators looking to restrict action to the dimension.";
	public static final String CHECK_PORTAL = """
					Determines if new portals should be pre-checked for safety. If false, portals will fail to form rather than redirect to a safe alternate destination.
					Note that disabling this also reduces the rate at which portal formation checks are performed.""";
	public static final String PORTAL_LIGHTNING = "Set this to false if you want the lightning that zaps the portal to not set things on fire. For those who don't like fun.";
	public static final String RETURN_PORTAL = "If false, the return portal will require the activation item.";
	public static final String PORTAL_ADVANCEMENT = "Use a valid advancement resource location as a string. For example, using the string \"minecraft:story/mine_diamond\" will lock the portal behind the \"Diamonds!\" advancement. Invalid/Empty Advancement resource IDs will leave the portal entirely unlocked.";
	public static final String PORTAL_SIZE = "The max amount of water spaces the mod will check for when creating a portal. Very high numbers may cause performance issues.";

	public static final String UNCRAFTING_TABLE = "Settings for all things related to the Uncrafting Table.";
	public static final String UNCRAFTING_MULTIPLIER = """
					Multiplies the total XP cost of uncrafting an item and rounds up.
					Higher values means the recipe will cost more to uncraft, lower means less. Set to 0 to disable the cost altogether.
					Note that this only affects reversed crafting recipes, uncrafting recipes will still use the same cost as they normally would.""";
	public static final String REPAIR_MULTIPLIER = """
					Multiplies the total XP cost of repairing an item and rounds up.
					Higher values means the recipe will cost more to repair, lower means less. Set to 0 to disable the cost altogether.""";
	public static final String RECIPE_WHITELIST = """
					If you don't want to disable uncrafting altogether, and would rather disable certain recipes, this is for you.
					To add a recipe, add the mod id followed by the name of the recipe. You can check this in things like JEI.
					Example: "twilightforest:firefly_particle_spawner" will disable uncrafting the particle spawner into a firefly jar, firefly, and poppy.
					If an item has multiple crafting recipes and you wish to disable them all, add the item to the "twilightforest:banned_uncraftables" item tag.
					If you have a problematic ingredient, like infested towerwood for example, add the item to the "twilightforest:banned_uncrafting_ingredients" item tag.""";
	public static final String RECIPE_BLACKLIST = "If true, this will invert the above uncrafting recipe list from a blacklist to a whitelist.";
	public static final String MOD_ID_WHITELIST = """
					Here, you can disable all items from certain mods from being uncrafted.
					Input a valid mod id to disable all uncrafting recipes from that mod.
					Example: "twilightforest" will disable all uncrafting recipes from this mod.""";
	public static final String MOD_ID_BLACKLIST = "If true, this will invert the above option from a blacklist to a whitelist.";
	public static final String SHAPELESS_UNCRAFTING = """
					If true, the uncrafting table will also be allowed to uncraft shapeless recipes.
					The table was originally intended to only take shaped recipes, but this option remains for people who wish to keep the functionality.""";
	public static final String INGREDIENT_SWITCHING = """
					If true, the uncrafting table will no longer allow you to switch between ingredients if a recipe uses a tag for crafting.
					This will remove the functionality for ALL RECIPES!
					If you want to prevent certain ingredients from showing up in the first place, use the "twilightforest:banned_uncrafting_ingredients" tag.""";
	public static final String DISABLE_UNCRAFTING = """
					Disables the uncrafting function of the uncrafting table. Recommended as a last resort if there's too many things to change about its behavior (or you're just lazy, I dont judge).
					Do note that special uncrafting recipes are not disabled as the mod relies on them for other things.""";
	public static final String DISABLE_TABLE = """
					Disables any usage of the uncrafting table, as well as prevents it from showing up in loot or crafted.
					Please note that table has more uses than just uncrafting, you can read about them here! http://benimatic.com/tfwiki/index.php?title=Uncrafting_Table
					It is highly recommended to keep the table enabled as the mod has special uncrafting exclusive recipes, but the option remains for people that dont want the table to be functional at all.
					If you are looking to just prevent normal crafting recipes from being reversed, consider using the 'disableUncrafting' option instead.""";

	public static final String MAGIC_TREES = "Settings for all things related to the magic trees.";
	public static final String TIME_CORE = """
					Defines the radius at which the Timewood Core works. Can be a number anywhere between 1 and 128.
					Set to 0 to prevent the Timewood Core from functioning.""";
	public static final String TRANFORMATION_CORE = """
					Defines the radius at which the Transformation Core works. Can be a number anywhere between 1 and 128.
					Set to 0 to prevent the Transformation Core from functioning.""";
	public static final String MINING_CORE = """
					Defines the radius at which the Minewood Core works. Can be a number anywhere between 1 and 128.
					Set to 0 to prevent the Minewood Core from functioning.""";
	public static final String SORTING_CORE = """
					Defines the radius at which the Sortingwood Core works. Can be a number anywhere between 1 and 128.
					Set to 0 to prevent the Sortingwood Core from functioning.""";

	public static final String SHIELD_PARRYING = "We recommend downloading the Shield Parry mod for parrying, but these controls remain for without.";
	public static final String PARRY_NON_TF = "Set to true to parry non-Twilight projectiles.";
	public static final String PARRY_WINDOW = "The amount of ticks after raising a shield that makes it OK to parry a projectile. (1 tick = 1/20 second)";

	public static final String CASKET_UUID_LOCKING = """
				If true, Keepsake Caskets that are spawned when a player dies will not be accessible by other players. Use this if you dont want people taking from other people's death caskets.
				NOTE: server operators will still be able to open locked caskets.""";
	public static final String DISABLE_SKULL_CANDLES = "If true, disables the ability to make Skull Candles by right clicking a vanilla skull with a candle. Turn this on if you're having mod conflict issues for some reason.";
	public static final String DEFAULT_ENCHANTS = """
				If false, items that come enchanted when you craft them (such as ironwood or steeleaf gear) will not show this way in the creative inventory.
				Please note that this doesn't affect the crafting recipes themselves, you will need a datapack to change those.""";
	public static final String BOSS_CHESTS = """
				If true, Twilight Forest's bosses will put their drops inside of a chest where they originally spawned instead of dropping the loot directly.
				Note that the Knight Phantoms are not affected by this as their drops work differently.""";
	public static final String CLOUD_PRECIP_SERVER = """
				Dictates how many blocks down from a cloud block should the game logic check for handling weather related code.
				Lower if experiencing low tick rate. Set to 0 to turn all cloud precipitation logic off.""";
	public static final String MULTIPLAYER_ADJUSTER = """
				Determines how bosses should adjust to multiplayer fights. There are 4 possible values that can be put here:
				NONE: doesnt do anything when multiple people participate in a bossfight. Bosses will act the same as they do in singleplayer or solo fights.
				MORE_LOOT: adds additional drops to a boss' loot table based on how many players participated in the fight. These are fully controlled through the entity's loot table, using the `twilightforest:multiplayer_multiplier` loot function. Note that this function will only do things to entities that are included in the `twilightforest:multiplayer_inclusive_entities` tag.
				MORE_HEALTH: increases the health of each boss by 20 hearts for each player nearby when the fight starts.
				MORE_LOOT_AND_HEALTH: does both of the above functions for each boss.""";

}
