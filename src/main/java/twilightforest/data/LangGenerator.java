package twilightforest.data;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import twilightforest.config.ConfigComments;
import twilightforest.data.helpers.TFLangProvider;
import twilightforest.data.tags.FluidTagGenerator;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.*;

import java.util.HashMap;
import java.util.Map;

public class LangGenerator extends TFLangProvider {
	public static final Map<ResourceLocation, Pair<String, String>> MAGIC_PAINTING_HELPER = new HashMap<>();
	public static final Map<String, String> SUBTITLE_GENERATOR = new HashMap<>();

	public LangGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void addTranslations() {

		this.add("itemGroup.twilightforest.blocks", "Twilight Forest: Blocks");
		this.add("itemGroup.twilightforest.items", "Twilight Forest: Items");
		this.add("itemGroup.twilightforest.equipment", "Twilight Forest: Equipment");

		this.addBiome(TFBiomes.FOREST, "Twilight Forest");
		this.addBiome(TFBiomes.DENSE_FOREST, "Dense Forest");
		this.addBiome(TFBiomes.FIREFLY_FOREST, "Firefly Forest");
		this.addBiome(TFBiomes.CLEARING, "Twilight Clearing");
		this.addBiome(TFBiomes.OAK_SAVANNAH, "Oak Savanna");
		this.addBiome(TFBiomes.STREAM, "Twilight Stream");
		this.addBiome(TFBiomes.LAKE, "Twilight Lake");
		this.addBiome(TFBiomes.MUSHROOM_FOREST, "Mushroom Forest");
		this.addBiome(TFBiomes.DENSE_MUSHROOM_FOREST, "Dense Mushroom Forest");
		this.addBiome(TFBiomes.ENCHANTED_FOREST, "Enchanted Forest");
		this.addBiome(TFBiomes.SPOOKY_FOREST, "Spooky Forest");
		this.addBiome(TFBiomes.SWAMP, "Twilight Swamp");
		this.addBiome(TFBiomes.FIRE_SWAMP, "Fire Swamp");
		this.addBiome(TFBiomes.DARK_FOREST, "Dark Forest");
		this.addBiome(TFBiomes.DARK_FOREST_CENTER, "Dark Forest Center");
		this.addBiome(TFBiomes.SNOWY_FOREST, "Snowy Forest");
		this.addBiome(TFBiomes.GLACIER, "Twilight Glacier");
		this.addBiome(TFBiomes.HIGHLANDS, "Twilight Highlands");
		this.addBiome(TFBiomes.THORNLANDS, "Thornlands");
		this.addBiome(TFBiomes.FINAL_PLATEAU, "Final Plateau");
		this.addBiome(TFBiomes.UNDERGROUND, "Underground");

		this.add("dimension.twilightforest.twilight_forest", "Twilight Forest");

		this.addStructure(TFStructures.HEDGE_MAZE, "Hedge Maze");
		this.addStructure(TFStructures.HOLLOW_HILL_SMALL, "Small Hollow Hill");
		this.addStructure(TFStructures.HOLLOW_HILL_MEDIUM, "Medium Hollow Hill");
		this.addStructure(TFStructures.HOLLOW_HILL_LARGE, "Large Hollow Hill");
		this.addStructure(TFStructures.QUEST_GROVE, "Quest Grove");
		this.addStructure(TFStructures.MUSHROOM_TOWER, "Mushroom Castle");
		this.addStructure(TFStructures.NAGA_COURTYARD, "Naga Courtyard");
		this.addStructure(TFStructures.LICH_TOWER, "Lich Tower");
		this.addStructure(TFStructures.LABYRINTH, "Minotaur Labyrinth");
		this.addStructure(TFStructures.HYDRA_LAIR, "Hydra's Lair");
		this.addStructure(TFStructures.KNIGHT_STRONGHOLD, "Knight Stronghold");
		this.addStructure(TFStructures.DARK_TOWER, "Dark Tower");
		this.addStructure(TFStructures.YETI_CAVE, "Yeti Cave");
		this.addStructure(TFStructures.AURORA_PALACE, "Aurora Palace");
		this.addStructure(TFStructures.TROLL_CAVE, "Troll Cave");
		this.addStructure(TFStructures.FINAL_CASTLE, "Final Plateau Castle");

		this.addCommand("usage", "/%s <info | reactivate | conquer | center>");
		this.addCommand("not_player", "This command must be run by a valid player!");
		this.addCommand("none_nearby", "Couldn't find a feature nearby!");
		this.addCommand("nearest", "The nearest feature is: %s");
		this.addCommand("center", "Center of feature: %s");
		this.addCommand("chunk", "Located in feature chunk: %s");
		this.addCommand("structure.inside", "You are in a Landmark structure");
		this.addCommand("structure.outside", "You are not in a Landmark structure");
		this.addCommand("structure.required", "You are not in a structure");
		this.addCommand("structure.conquer.status", "Structure conquer flag: %s");
		this.addCommand("structure.conquer.update", "Structure conquer flag was %s, changing to %s");
		this.addCommand("structure.spawn_list", "Spawn list for this area is:");
		this.addCommand("structure.spawn_info", "%s, Weight %s");
		this.addCommand("structure.boundaries", "Structure boundaries: %s");

		this.add("gamerule.tfEnforcedProgression", "Twilight Forest: Enforced Progression");
		this.add("gamerule.tfEnforcedProgression.description", "Some Twilight Forest biomes will be locked until you defeat certain bosses in the dimension. (You can check the progression order in your advancements)");

		this.add("effect.twilightforest.frosted", "Frosted");

		this.addEnchantment("chill_aura", "Chill Aura", "Adds a chance for the attacker to gain the frosted effect after hitting the wearer.");
		this.addEnchantment("fire_react", "Fire React", "Adds a chance for the attacker to be set on fire after hitting the wearer.");
		this.addEnchantment("destruction", "Destruction", "Allows the Block and Chain to break blocks with a higher mining tier.");
		this.addEnchantment("renewal", "Renewal", "Automatically recharges scepters when they run out if the holder has the recharge items in their inventory.");

		this.addAdvancement("root", "Twilight Forest", "Enter the mysterious, magical woodlands: the Twilight Forest");
		this.addAdvancement("kill_cicada", "Shut", "Kill a Cicada");
		this.addAdvancement("uncraft_uncrafting_table", "A Step too Far", "Uncraft an Uncrafting Table");
		this.addAdvancement("hedge", "Bug Stomper", "Defeat a spider in a Hedge Maze");
		this.addAdvancement("hill1", "The Boots Are Mine!", "Defeat a %s in a Small Hollow Hill");
		this.addAdvancement("hill2", "What Was That Noise?", "Defeat a %s in a Medium Hollow Hill");
		this.addAdvancement("hill3", "I See Right Through You", "Defeat a %s in a Large Hollow Hill");
		this.addAdvancement("quest_ram", "Consummate Baaahs", "Give the %s what it is missing");
		this.addAdvancement("magic_map_focus", "With Fire It Writes", "Craft the %s with a %s, %s, and %s");
		this.addAdvancement("magic_map", "I Can See Forever", "Craft the %s");
		this.addAdvancement("maze_map", "And Now, to Find the Exit", "Craft the %s after obtaining the focus from the labyrinth");
		this.addAdvancement("ore_map", "How Can That Be Worth It?", "Craft the %s");
		this.addAdvancement("twilight_hunter", "The Silence of the Forest", "Hunt some of the local wildlife");
		this.addAdvancement("kill_naga", "Time To Even The Scales", "Slay the %s in its forest courtyard and obtain a %s to overcome the barrier magic surrounding the Lich's tower");
		this.addAdvancement("naga_armors", "Naga Armorer", "Craft both %s chest and leg armor");
		this.addAdvancement("kill_lich", "Bring Out Your Dead", "Slay the %s at top of his tower and retrieve a scepter to clear poisonous mosquitoes from the Swamp, see through blinding darkness of the Dark Forest's curse, and resist the Snowy Forest's chill");
		this.addAdvancement("lich_scepters", "By Our Powers Combined!", "Acquire all four scepters of power");
		this.addAdvancement("full_mettle_alchemist", "Full Mettle Alchemist", "Drink three doses of Harming II from a potion flask in under 6 seconds and survive");
		this.addAdvancement("progress_labyrinth", "Mighty Stroganoff", "Eat the Meef Stroganoff to acclimatize your body to the dangerous heat of the Fire Swamp");
		this.addAdvancement("mazebreaker", "Breaking the Maze", "Find the %s pickaxe in the secret labyrinth vault");
		this.addAdvancement("kill_hydra", "Hydra Slayer", "Defeat the mighty %s and empower yourself");
		this.addAdvancement("hydra_chop", "Hydra Chop, Baby!", "Chow down on a %s when your hunger bar is depleted");
		this.addAdvancement("progress_trophy_pedestal", "Trophied Champion", "Claim your title by placing a trophy on the pedestal in the Dark Forest ruins");
		this.addAdvancement("progress_knights", "Carminite Acclimation", "Settle the restless phantoms in the knight's tomb and the carminite tower's devices will obey you");
		this.addAdvancement("ghast_trap", "Something Strange in Towerwood", "Slay the %ss around a %s and activate to wrench the %s from the sky!");
		this.addAdvancement("progress_ur_ghast", "Tears of Fire", "Touch the fiery red tears of the %s");
		this.addAdvancement("experiment_115", "Mystery Meat?", "It looks like cake, though...");
		this.addAdvancement("experiment_115_2", "Making a note: Huge Success!", "It's so delicious and moist!");
		this.addAdvancement("experiment_115_3", "Eating 115 Everyday, 115 Years, Forever", "eating experiment one-hundred-fifteen all day, forever, one-hundred-fifteen times experiment one-hundred-fifteen, experiment one-hundred-fifteen dot com, double-u double-u double-u dot experiment one-hundred-fifteen dot com, one-hundred-fifteen years, every minute experiment one-hundred-fifteen dot com, double-u double-u double-u dot one-hundred-fifteen times experiment one-hundred-fifteen dot com");
		this.addAdvancement("progress_yeti", "Alpha Fur", "Line your garments with the soft fur from the %s, keeping you safe from the Glacier's cold");
		this.addAdvancement("arctic_dyed", "Getting in Fashion", "Dye four unique pieces of Arctic armor");
		this.addAdvancement("progress_glacier", "Clear Skies", "Defeat the %s atop the Aurora Palace");
		this.addAdvancement("glass_sword", "One Hit Wonder", "Hope you made good use of that.");
		this.addAdvancement("fiery_set", "Gallons of Blood and Tears", "Wield a fiery tool or weapon while having at least one piece of fiery armor in your inventory");
		this.addAdvancement("progress_merge", "Ultimate Showdown", "Slay the %s, %s, and %s to clear the acid rain and embolden yourself for the Highlands");
		this.addAdvancement("troll", "We Do a Little Trolling", "Find and kill a %s");
		this.addAdvancement("beanstalk", "Jack and the Beanstalk", "Obtain some %s in the troll caves and use them on the glowing soil beneath the clouds");
		this.addAdvancement("giants", "I'm on Cloud Nine", "Kill a %s in the clouds, retrieve a %s, and return back to the troll caves");
		this.addAdvancement("progress_troll", "I Wish For More Burning", "Find the %s in the troll caves, and you can burn away the thorn barriers");
		this.addAdvancement("progression_end", "End of Progression", "Anything past this point in the Highlands is a work in progress. It will be finished in the future");
		//this.addAdvancement("progress_thorns", "Past the Thorns [NYI]", "Make it past the Thornlands, and unlock the door of the castle");
		//this.addAdvancement("progress_castle", "So Castle Very Wow [NYI]", "What could even be in that castle?!?");
		this.addAdvancement("twilight_dining", "We Dine At Eternal Sundown", "Eat every edible item exclusive to Twilight Forest");
		this.addAdvancement("arborist", "Maniacal Dendrologist", "Get your axes and shears ready. Search every nook and cranny and get anything and everything that comes from trees! Craft, loot, obtain every slab... sapling... leaves... more... everything. ALL OF IT!");

		this.addMessage("advancement_hidden", "<Hidden Advancement>");
		this.addMessage("advancement_required", "Advancement Required:");
		this.addMessage("biome_locked", "Biome Locked!");
		this.addMessage("biome_locked_2", "Check your advancements");
		this.addMessage("core_disabled", "%s is disabled via config");
		this.addMessage("firefly_spawner_radius", "Firefly Particle Radius: %s Blocks");
		this.addMessage("magic_map_fail", "The Magic faltered. Perhaps it doesn't work here?");
		this.addMessage("ore_meter_separator", "-");
		this.addMessage("ore_meter_ratio", "(%s%%)");
		this.addMessage("ore_meter_header_block", "Block");
		this.addMessage("ore_meter_header_count", "Count");
		this.addMessage("ore_meter_header_ratio", "Ratio");
		this.addMessage("ore_meter_loading", "Loading");
		this.addMessage("ore_meter_new_range", "Range set to %s chunks");
		this.addMessage("ore_meter_no_blocks", "No blocks found nearby");
		this.addMessage("ore_meter_range", "Radius: %s, Origin: [%s, %s]");
		this.addMessage("ore_meter_set_block", "Targeted block set to %s");
		this.addMessage("ore_meter_targeted_block", "Targeted block: %s");
		this.addMessage("ore_meter_total", "Total blocks scanned: %s");
		this.addMessage("pedestal_ineligible", "You are unworthy.");
		this.addMessage("portal_unsafe", "It doesn't seem safe here...");
		this.addMessage("portal_unworthy", "The Portal pool is unresponsive. Perhaps something was neglected?");
		this.addMessage("wip", "This feature is a work in progress and may have bugs or unintended effects that may damage your world");

		this.addBlock(TFBlocks.MASON_JAR, "Mason Jar");
		this.addBlock(TFBlocks.CICADA, "Cicada");
		this.addBlock(TFBlocks.CICADA_JAR, "Cicada Jar");
		this.addBlock(TFBlocks.FIREFLY, "Firefly");
		this.addBlock(TFBlocks.FIREFLY_JAR, "Firefly Jar");
		this.addBlock(TFBlocks.MOONWORM, "Moonworm");
		this.addBlock(TFBlocks.FIREFLY_SPAWNER, "Firefly Particle Spawner");

		this.addBlock(TFBlocks.TWILIGHT_PORTAL, "Twilight Forest Portal");
		this.addBlock(TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE, "Miniature Twilight Forest Portal");

		this.addBlock(TFBlocks.NAGASTONE, "Nagastone");
		this.addBlock(TFBlocks.NAGASTONE_HEAD, "Nagastone Head");
		this.addStoneVariants("etched_nagastone", "Etched Nagastone");
		this.addStoneVariants("nagastone_pillar", "Nagastone Pillar");
		this.addBlock(TFBlocks.NAGASTONE_STAIRS_LEFT, "Nagastone Stairs (Left)");
		this.addBlock(TFBlocks.NAGASTONE_STAIRS_RIGHT, "Nagastone Stairs (Right)");
		this.addBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT, "Cracked Nagastone Stairs (Left)");
		this.addBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT, "Cracked Nagastone Stairs (Right)");
		this.addBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT, "Mossy Nagastone Stairs (Left)");
		this.addBlock(TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT, "Mossy Nagastone Stairs (Right)");
		this.addBlock(TFBlocks.SPIRAL_BRICKS, "Spiral Bricks");
		this.addBlock(TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE, "Miniature Naga Courtyard");

		this.addBlock(TFBlocks.TWISTED_STONE, "Twisted Stone");
		this.addBlock(TFBlocks.TWISTED_STONE_PILLAR, "Twisted Stone Pillar");
		this.addBlock(TFBlocks.BOLD_STONE_PILLAR, "Bold Stone Pillar");
		this.addBlock(TFBlocks.CANDELABRA, "Candelabra");
		this.addBlock(TFBlocks.WROUGHT_IRON_FENCE, "Wrought Iron Fence");
		this.add("block.twilightforest.wrought_iron_fence.cap", "Shift + right-click the same block to place finial");
		this.addBlock(TFBlocks.TERRORCOTTA_ARCS, "Terrorcotta Arcs");
		this.addBlock(TFBlocks.TERRORCOTTA_CURVES, "Terrorcotta Curves");
		this.addBlock(TFBlocks.TERRORCOTTA_LINES, "Terrorcotta Lines");
		this.addBlock(TFBlocks.CORONATION_CARPET, "Coronation Carpet");

		this.addBlock(TFBlocks.SKULL_CHEST, "Skull Chest");
		this.addBlock(TFBlocks.KEEPSAKE_CASKET, "Keepsake Casket");
		this.add("block.twilightforest.casket.damage", "Damage: %s");
		this.add("block.twilightforest.casket.broken", "Your Keepsake Casket was too damaged, it degraded into a Skull Chest while saving your items.");
		this.add("block.twilightforest.casket.locked", "This Casket can only be opened by %s!");
		this.addBlock(TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE, "Miniature Lich Tower");

		this.addBlock(TFBlocks.HUGE_LILY_PAD, "Huge Lily Pad");
		this.addBlock(TFBlocks.HUGE_WATER_LILY, "Huge Water Lily");
		this.addBlock(TFBlocks.MAZESTONE, "Mazestone");
		this.addBlock(TFBlocks.MAZESTONE_BRICK, "Mazestone Brick");
		this.addBlock(TFBlocks.CRACKED_MAZESTONE, "Cracked Mazestone Brick");
		this.addBlock(TFBlocks.MOSSY_MAZESTONE, "Mossy Mazestone Brick");
		this.addBlock(TFBlocks.CUT_MAZESTONE, "Cut Mazestone");
		this.addBlock(TFBlocks.DECORATIVE_MAZESTONE, "Decorative Mazestone Brick");
		this.addBlock(TFBlocks.MAZESTONE_MOSAIC, "Mazestone Mosaic");
		this.addBlock(TFBlocks.MAZESTONE_BORDER, "Mazestone Border");
		this.addBlock(TFBlocks.RED_THREAD, "Red Thread");

		this.addBlock(TFBlocks.SMOKER, "Smoker");
		this.addBlock(TFBlocks.FIRE_JET, "Fire Jet");
		this.addBlock(TFBlocks.ENCASED_SMOKER, "Encased Smoker");
		this.addBlock(TFBlocks.ENCASED_FIRE_JET, "Encased Fire Jet");

		this.addBlock(TFBlocks.TROPHY_PEDESTAL, "Trophy Pedestal");
		this.addBlock(TFBlocks.STRONGHOLD_SHIELD, "Stronghold Shield");
		this.addBlock(TFBlocks.UNDERBRICK, "Underbrick");
		this.addBlock(TFBlocks.CRACKED_UNDERBRICK, "Cracked Underbrick");
		this.addBlock(TFBlocks.MOSSY_UNDERBRICK, "Mossy Underbrick");
		this.addBlock(TFBlocks.UNDERBRICK_FLOOR, "Underbrick Floor");

		this.addBlock(TFBlocks.TOWERWOOD, "Towerwood Planks");
		this.addBlock(TFBlocks.CRACKED_TOWERWOOD, "Cracked Towerwood Planks");
		this.addBlock(TFBlocks.MOSSY_TOWERWOOD, "Mossy Towerwood Planks");
		this.addBlock(TFBlocks.INFESTED_TOWERWOOD, "Infested Towerwood Planks");
		this.addBlock(TFBlocks.ENCASED_TOWERWOOD, "Encased Towerwood Planks");
		this.addBlock(TFBlocks.UNBREAKABLE_VANISHING_BLOCK, "Vanishing Block");
		this.addBlock(TFBlocks.VANISHING_BLOCK, "Vanishing Block");
		this.addBlock(TFBlocks.REAPPEARING_BLOCK, "Reappearing Block");
		this.addBlock(TFBlocks.LOCKED_VANISHING_BLOCK, "Locked Vanishing Block");
		this.addBlock(TFBlocks.CARMINITE_BUILDER, "Carminite Builder");
		this.addBlock(TFBlocks.BUILT_BLOCK, "Built Block");
		this.addBlock(TFBlocks.ANTIBUILDER, "Antibuilder");
		this.addBlock(TFBlocks.ANTIBUILT_BLOCK, "Antibuilt Block");
		this.addBlock(TFBlocks.GHAST_TRAP, "Ghast Trap");
		this.addBlock(TFBlocks.CARMINITE_REACTOR, "Carminite Reactor");
		this.addBlock(TFBlocks.REACTOR_DEBRIS, "Reactor Debris");
		this.addBlock(TFBlocks.FAKE_DIAMOND, "Diamond Block");
		this.addBlock(TFBlocks.FAKE_GOLD, "Gold Block");
		this.addBlock(TFBlocks.EXPERIMENT_115, "Experiment 115");

		this.addBlock(TFBlocks.AURORA_BLOCK, "Aurora Block");
		this.addBlock(TFBlocks.AURORA_SLAB, "Aurora Slab");
		this.addBlock(TFBlocks.AURORA_PILLAR, "Aurora Pillar");
		this.addBlock(TFBlocks.AURORALIZED_GLASS, "Auroralized Glass");

		this.addBlock(TFBlocks.HUGE_MUSHGLOOM, "Huge Mushgloom Block");
		this.addBlock(TFBlocks.HUGE_MUSHGLOOM_STEM, "Huge Mushgloom Stem");
		this.addBlock(TFBlocks.HUGE_STALK, "Huge Stalk");
		this.addBlock(TFBlocks.BEANSTALK_LEAVES, "Beanstalk Leaves");
		this.addBlock(TFBlocks.UBEROUS_SOIL, "Uberous Soil");
		this.addBlock(TFBlocks.TROLLVIDR, "Trollvidr");
		this.addBlock(TFBlocks.UNRIPE_TROLLBER, "Unripe Trollber");
		this.addBlock(TFBlocks.TROLLBER, "Trollber");
		this.addBlock(TFBlocks.TROLLSTEINN, "Trollsteinn");
		this.addBlock(TFBlocks.FLUFFY_CLOUD, "Fluffy Cloud");
		this.addBlock(TFBlocks.RAINY_CLOUD, "Rainy Cloud");
		this.addBlock(TFBlocks.SNOWY_CLOUD, "Snowy Cloud");
		this.addBlock(TFBlocks.WISPY_CLOUD, "Wispy Cloud");
		this.addBlock(TFBlocks.GIANT_LEAVES, "Giant Leaves");
		this.addBlock(TFBlocks.GIANT_LOG, "Giant Log");
		this.addBlock(TFBlocks.GIANT_COBBLESTONE, "Giant Cobblestone");
		this.addBlock(TFBlocks.GIANT_OBSIDIAN, "Giant Obsidian");

		this.addBlock(TFBlocks.BROWN_THORNS, "Thorns");
		this.addBlock(TFBlocks.GREEN_THORNS, "Green Thorns");
		this.addBlock(TFBlocks.BURNT_THORNS, "Burnt Thorns");
		this.addBlock(TFBlocks.THORN_ROSE, "Thorn Rose");
		this.addBlock(TFBlocks.THORN_LEAVES, "Thorn Leaves");
		this.addBlock(TFBlocks.DEADROCK, "Deadrock");
		this.addBlock(TFBlocks.CRACKED_DEADROCK, "Cracked Deadrock");
		this.addBlock(TFBlocks.WEATHERED_DEADROCK, "Weathered Deadrock");

		this.addStoneVariants("castle_brick", "Castle Brick");
		this.addStoneVariants("castle_brick_stairs", "Castle Brick Stairs");
		this.add(TFBlocks.WORN_CASTLE_BRICK.get(), "Worn Castle Brick");
		this.add(TFBlocks.THICK_CASTLE_BRICK.get(), "Thick Castle Brick");
		this.add(TFBlocks.CASTLE_ROOF_TILE.get(), "Castle Roof Tile");
		this.add(TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(), "Encased Castle Brick Pillar");
		this.add(TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(), "Encased Castle Brick Tile");
		this.add(TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(), "Bold Castle Brick Pillar");
		this.add(TFBlocks.BOLD_CASTLE_BRICK_TILE.get(), "Bold Castle Brick Tile");
		this.add(TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(), "Worn Castle Brick Stairs");
		this.add(TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), "Encased Castle Brick Stairs");
		this.add(TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(), "Bold Castle Brick Stairs");

		this.add(TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), "Magenta Castle Rune Brick");
		this.add(TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), "Yellow Castle Rune Brick");
		this.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), "Blue Castle Rune Brick");
		this.add(TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), "Violet Castle Rune Brick");

		this.add(TFBlocks.PINK_CASTLE_DOOR.get(), "Magenta Castle Door");
		this.add(TFBlocks.YELLOW_CASTLE_DOOR.get(), "Yellow Castle Door");
		this.add(TFBlocks.BLUE_CASTLE_DOOR.get(), "Blue Castle Door");
		this.add(TFBlocks.VIOLET_CASTLE_DOOR.get(), "Violet Castle Door");

		this.add(TFBlocks.PINK_FORCE_FIELD.get(), "Magenta Force Field");
		this.add(TFBlocks.ORANGE_FORCE_FIELD.get(), "Orange Force Field");
		this.add(TFBlocks.GREEN_FORCE_FIELD.get(), "Green Force Field");
		this.add(TFBlocks.BLUE_FORCE_FIELD.get(), "Blue Force Field");
		this.add(TFBlocks.VIOLET_FORCE_FIELD.get(), "Violet Force Field");

		this.addBlock(TFBlocks.IRONWOOD_BLOCK, "Block of Ironwood");
		this.addBlock(TFBlocks.STEELEAF_BLOCK, "Block of Steeleaf");
		this.addBlock(TFBlocks.KNIGHTMETAL_BLOCK, "Block of Knightmetal");
		this.add("block.twilightforest.knightmetal_block.desc", "Works as a stronger cactus");
		this.addBlock(TFBlocks.ARCTIC_FUR_BLOCK, "Block of Arctic Fur");
		this.add("block.twilightforest.arctic_fur_block.desc", "Reduces fall damage by 90%");
		this.addBlock(TFBlocks.FIERY_BLOCK, "Block of Fiery Metal");
		this.addBlock(TFBlocks.CARMINITE_BLOCK, "Block of Carminite");

		this.addBlock(TFBlocks.CINDER_FURNACE, "Cinder Furnace");
		this.addBlock(TFBlocks.CINDER_LOG, "Cinder Log");
		this.addBlock(TFBlocks.CINDER_WOOD, "Cinder Wood");

		this.addBlock(TFBlocks.IRON_LADDER, "Iron Ladder");
		this.addBlock(TFBlocks.ROPE, "Rope");
		this.addBlock(TFBlocks.CANOPY_WINDOW, "Canopy Window");
		this.addBlock(TFBlocks.CANOPY_WINDOW_PANE, "Canopy Window Pane");
		this.addBlock(TFBlocks.SLIDER, "Slide Trap");
		this.addBlock(TFBlocks.BRAZIER, "Brazier");

		this.addBlock(TFBlocks.TWILIGHT_OAK_LEAVES, "Twilight Oak Leaves");
		this.addSapling("twilight_oak", "Sickly Twilight Oak Sapling");
		this.createLogs("twilight_oak", "Twilight Oak");
		this.createWoodSet("twilight_oak", "Twilight Oak");

		this.addBlock(TFBlocks.CANOPY_LEAVES, "Canopy Tree Leaves");
		this.addSapling("canopy", "Canopy Tree Sapling");
		this.createLogs("canopy", "Canopy Tree");
		this.createWoodSet("canopy", "Canopy");
		this.addBlock(TFBlocks.CANOPY_BOOKSHELF, "Canopy Bookshelf");
		this.addBlock(TFBlocks.CHISELED_CANOPY_BOOKSHELF, "Chiseled Canopy Bookshelf");

		this.addBlock(TFBlocks.MANGROVE_LEAVES, "Mangrove Leaves");
		this.addSapling("mangrove", "Mangrove Sapling");
		this.createLogs("mangrove", "Mangrove");
		this.createWoodSet("mangrove", "Mangrove");

		this.addBlock(TFBlocks.DARK_LEAVES, "Darkwood Leaves");
		this.addBlock(TFBlocks.HARDENED_DARK_LEAVES, "Thick Darkwood Leaves");
		this.addSapling("darkwood", "Darkwood Sapling");
		this.createLogs("dark", "Darkwood");
		this.createWoodSet("dark", "Darkwood");

		this.addBlock(TFBlocks.TIME_LOG_CORE, "Timewood Clock");
		this.addBlock(TFBlocks.TIME_LEAVES, "Timewood Leaves");
		this.addSapling("time", "Tree of Time Sapling");
		this.createLogs("time", "Timewood");
		this.createWoodSet("time", "Timewood");

		this.addBlock(TFBlocks.TRANSFORMATION_LOG_CORE, "Heart of Transformation");
		this.addBlock(TFBlocks.TRANSFORMATION_LEAVES, "Leaves of Transformation");
		this.addSapling("transformation", "Tree of Transformation Sapling");
		this.createLogs("transformation", "Transwood");
		this.createWoodSet("transformation", "Transwood");

		this.addBlock(TFBlocks.MINING_LOG_CORE, "Minewood Core");
		this.addBlock(TFBlocks.MINING_LEAVES, "Miner's Leaves");
		this.addSapling("mining", "Miner's Tree Sapling");
		this.createLogs("mining", "Minewood");
		this.createWoodSet("mining", "Minewood");

		this.addBlock(TFBlocks.SORTING_LOG_CORE, "Sortingwood Engine");
		this.addBlock(TFBlocks.SORTING_LEAVES, "Sorting Leaves");
		this.addSapling("sorting", "Sorting Tree Sapling");
		this.createLogs("sorting", "Sortingwood");
		this.createWoodSet("sorting", "Sortingwood");

		this.addBlock(TFBlocks.RAINBOW_OAK_LEAVES, "Rainbow Oak Leaves");
		this.addSapling("rainbow_oak", "Rainbow Oak Sapling");
		this.addSapling("hollow_oak", "Robust Twilight Oak Sapling");

		this.addBlock(TFBlocks.OAK_BANISTER, "Oak Banister");
		this.addBlock(TFBlocks.SPRUCE_BANISTER, "Spruce Banister");
		this.addBlock(TFBlocks.BIRCH_BANISTER, "Birch Banister");
		this.addBlock(TFBlocks.JUNGLE_BANISTER, "Jungle Banister");
		this.addBlock(TFBlocks.ACACIA_BANISTER, "Acacia Banister");
		this.addBlock(TFBlocks.DARK_OAK_BANISTER, "Dark Oak Banister");
		this.addBlock(TFBlocks.CRIMSON_BANISTER, "Crimson Banister");
		this.addBlock(TFBlocks.WARPED_BANISTER, "Warped Banister");
		this.addBlock(TFBlocks.VANGROVE_BANISTER, "Mangrove Banister");
		this.addBlock(TFBlocks.BAMBOO_BANISTER, "Bamboo Banister");
		this.addBlock(TFBlocks.CHERRY_BANISTER, "Cherry Banister");

		this.createHollowLogs("oak", "Oak", false);
		this.createHollowLogs("spruce", "Spruce", false);
		this.createHollowLogs("birch", "Birch", false);
		this.createHollowLogs("jungle", "Jungle", false);
		this.createHollowLogs("acacia", "Acacia", false);
		this.createHollowLogs("dark_oak", "Dark Oak", false);
		this.createHollowLogs("crimson", "Crimson", true);
		this.createHollowLogs("warped", "Warped", true);
		this.createHollowLogs("vangrove", "Mangrove", false);
		this.createHollowLogs("cherry", "Cherry", false);
		this.add("block.twilightforest.banister.cycle", "Right-click with axe to cycle");

		this.addBlock(TFBlocks.MOSS_PATCH, "Moss Patch");
		this.addBlock(TFBlocks.CLOVER_PATCH, "Clover Patch");
		this.addBlock(TFBlocks.TORCHBERRY_PLANT, "Torchberry Plant");
		this.addBlock(TFBlocks.ROOT_STRAND, "Root Strands");
		this.addBlock(TFBlocks.FALLEN_LEAVES, "Fallen Leaves");
		this.addBlock(TFBlocks.MAYAPPLE, "Mayapple");
		this.addBlock(TFBlocks.POTTED_MAYAPPLE, "Potted Mayapple");
		this.addBlock(TFBlocks.FIDDLEHEAD, "Fiddlehead Fern");
		this.addBlock(TFBlocks.POTTED_FIDDLEHEAD, "Potted Fiddlehead");
		this.addBlock(TFBlocks.MUSHGLOOM, "Mushgloom");
		this.addBlock(TFBlocks.POTTED_MUSHGLOOM, "Potted Mushgloom");
		this.addBlock(TFBlocks.POTTED_THORN, "Potted Thorn");
		this.addBlock(TFBlocks.POTTED_GREEN_THORN, "Potted Green Thorn");
		this.addBlock(TFBlocks.POTTED_DEAD_THORN, "Potted Burnt Thorn");

		this.addBlock(TFBlocks.HEDGE, "Hedge");
		this.addBlock(TFBlocks.ROOT_BLOCK, "Roots");
		this.addBlock(TFBlocks.LIVEROOT_BLOCK, "Liveroots");
		this.addBlock(TFBlocks.MANGROVE_ROOT, "Mangrove Roots");

		this.addBlock(TFBlocks.NAGA_TROPHY, "Naga Trophy");
		this.addBlock(TFBlocks.LICH_TROPHY, "Lich Trophy");
		this.addBlock(TFBlocks.MINOSHROOM_TROPHY, "Minoshroom Trophy");
		this.addBlock(TFBlocks.HYDRA_TROPHY, "Hydra Trophy");
		this.addBlock(TFBlocks.KNIGHT_PHANTOM_TROPHY, "Knight Phantom Trophy");
		this.addBlock(TFBlocks.UR_GHAST_TROPHY, "Ur-Ghast Trophy");
		this.addBlock(TFBlocks.ALPHA_YETI_TROPHY, "Alpha Yeti Trophy");
		this.addBlock(TFBlocks.SNOW_QUEEN_TROPHY, "Snow Queen Trophy");
		this.addBlock(TFBlocks.QUEST_RAM_TROPHY, "Questing Ram Trophy");

		this.addBlock(TFBlocks.NAGA_BOSS_SPAWNER, "Naga Boss Spawner");
		this.addBlock(TFBlocks.LICH_BOSS_SPAWNER, "Lich Boss Spawner");
		this.addBlock(TFBlocks.MINOSHROOM_BOSS_SPAWNER, "Minoshroom Boss Spawner");
		this.addBlock(TFBlocks.HYDRA_BOSS_SPAWNER, "Hydra Boss Spawner");
		this.addBlock(TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER, "Knight Phantoms Boss Spawner");
		this.addBlock(TFBlocks.UR_GHAST_BOSS_SPAWNER, "Ur-Ghast Boss Spawner");
		this.addBlock(TFBlocks.ALPHA_YETI_BOSS_SPAWNER, "Alpha Yeti Boss Spawner");
		this.addBlock(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER, "Snow Queen Boss Spawner");
		this.addBlock(TFBlocks.FINAL_BOSS_BOSS_SPAWNER, "Final Boss Boss Spawner");

		this.addBannerPattern("naga", "Naga Scales");
		this.addBannerPattern("lich", "Lich Crown");
		this.addBannerPattern("minoshroom", "Minoshroom Axes");
		this.addBannerPattern("hydra", "Hydra Flame");
		this.addBannerPattern("knight_phantom", "Knight Helmet");
		this.addBannerPattern("ur_ghast", "Carminite Border");
		this.addBannerPattern("alpha_yeti", "Alpha Yeti Face");
		this.addBannerPattern("snow_queen", "Snowflake");
		this.addBannerPattern("quest_ram", "Questing Ram Swirls");

		this.addBlock(TFBlocks.ZOMBIE_SKULL_CANDLE, "Zombie Skull Candle");
		this.addBlock(TFBlocks.ZOMBIE_WALL_SKULL_CANDLE, "Zombie Wall Skull Candle");
		this.addBlock(TFBlocks.SKELETON_SKULL_CANDLE, "Skeleton Skull Candle");
		this.addBlock(TFBlocks.SKELETON_WALL_SKULL_CANDLE, "Skeleton Wall Skull Candle");
		this.addBlock(TFBlocks.WITHER_SKELE_SKULL_CANDLE, "Wither Skeleton Skull Candle");
		this.addBlock(TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE, "Wither Skeleton Wall Skull Candle");
		this.addBlock(TFBlocks.CREEPER_SKULL_CANDLE, "Creeper Skull Candle");
		this.addBlock(TFBlocks.CREEPER_WALL_SKULL_CANDLE, "Creeper Wall Skull Candle");
		this.addBlock(TFBlocks.PLAYER_SKULL_CANDLE, "Player Skull Candle");
		this.add("block.twilightforest.player_skull_candle.named", "%s's Head with Candles");
		this.addBlock(TFBlocks.PLAYER_WALL_SKULL_CANDLE, "Player Wall Skull Candle");
		this.addBlock(TFBlocks.PIGLIN_SKULL_CANDLE, "Piglin Skull Candle");
		this.addBlock(TFBlocks.PIGLIN_WALL_SKULL_CANDLE, "Piglin Wall Skull Candle");

		this.addBlock(TFBlocks.UNCRAFTING_TABLE, "Uncrafting Table");
		this.add("block.twilightforest.uncrafting_table.disabled", "This block has been disabled.");
		this.add("container.twilightforest.uncrafting_table", "Uncrafting Table");
		this.add("container.twilightforest.uncrafting_table.disabled_item", "Uncrafting this item is disabled.");
		this.add("container.twilightforest.uncrafting_table.uncrafting_disabled", "Uncrafting is disabled via config");
		this.add("container.twilightforest.uncrafting_table.cycle_next_recipe", "Next Crafting Recipe");
		this.add("container.twilightforest.uncrafting_table.cycle_back_recipe", "Previous Crafting Recipe");
		this.add("container.twilightforest.uncrafting_table.cycle_next_ingredient", "Next Uncrafting Ingredients");
		this.add("container.twilightforest.uncrafting_table.cycle_back_ingredient", "Previous Uncrafting Ingredients");
		this.add("container.twilightforest.uncrafting_table.cycle_next_uncraft", "Next Uncrafting Recipe");
		this.add("container.twilightforest.uncrafting_table.cycle_back_uncraft", "Previous Uncrafting Recipe");

		this.addBlock(TFBlocks.SINISTER_SPAWNER, "Sinister Spawner");

		this.add("item.twilightforest.boarkchop", "Raw Boarkchop");
		this.addItem(TFItems.NAGA_SCALE, "Naga Scale");
		this.addItem(TFItems.NAGA_CHESTPLATE, "Naga Scale Tunic");
		this.addItem(TFItems.NAGA_LEGGINGS, "Naga Scale Leggings");
		this.addItem(TFItems.FORTIFICATION_SCEPTER, "Scepter of Fortification");
		this.addItem(TFItems.LIFEDRAIN_SCEPTER, "Scepter of Life Draining");
		this.addItem(TFItems.TWILIGHT_SCEPTER, "Scepter of Twilight");
		this.addItem(TFItems.ZOMBIE_SCEPTER, "Zombie Scepter");
		this.add("item.twilightforest.scepter.desc", "%s charges left");
		this.addItem(TFItems.EXANIMATE_ESSENCE, "Exanimate Essence");
		this.addItem(TFItems.WROUGHT_IRON_BAR, "Wrought Iron Bar");
		this.addItem(TFItems.BRITTLE_FLASK, "Brittle Potion Flask");
		this.addItem(TFItems.GREATER_FLASK, "Greater Potion Flask");
		this.add("item.twilightforest.flask.empty", "Empty");
		this.add("item.twilightforest.flask.empty_description", "Can hold multiple potion doses");
		this.add("item.twilightforest.flask.no_refill", "Cannot be refilled");

		this.addItem(TFItems.MAGIC_PAINTING, "Magic Painting");
		this.addItem(TFItems.ORE_METER, "Ore Meter");
		this.addItem(TFItems.FILLED_MAGIC_MAP, "Magic Map");
		this.addItem(TFItems.FILLED_MAZE_MAP, "Maze Map");
		this.add("item.twilightforest.maze_map.y_level", "Y level %s");
		this.addItem(TFItems.FILLED_ORE_MAP, "Maze/Ore Map");
		this.addItem(TFItems.MAGIC_MAP, "Blank Magic Map");
		this.addItem(TFItems.MAZE_MAP, "Blank Maze Map");
		this.addItem(TFItems.ORE_MAP, "Blank Maze/Ore Map");
		this.addItem(TFItems.RAVEN_FEATHER, "Raven Feather");
		this.addItem(TFItems.MAGIC_MAP_FOCUS, "Magic Map Focus");
		this.addItem(TFItems.MAZE_MAP_FOCUS, "Maze Map Focus");

		this.addItem(TFItems.LIVEROOT, "Liveroot");
		this.addItem(TFItems.RAW_IRONWOOD, "Raw Ironwood");
		this.addItem(TFItems.IRONWOOD_INGOT, "Ironwood Ingot");
		this.addArmor("ironwood", "Ironwood");
		this.addTools("ironwood", "Ironwood");

		this.addItem(TFItems.STEELEAF_INGOT, "Steeleaf");
		this.addArmor("steeleaf", "Steeleaf");
		this.addTools("steeleaf", "Steeleaf");

		this.addItem(TFItems.ARMOR_SHARD, "Armor Shard");
		this.addItem(TFItems.ARMOR_SHARD_CLUSTER, "Armor Shard Cluster");
		this.addItem(TFItems.KNIGHTMETAL_INGOT, "Knightmetal Ingot");
		this.addItem(TFItems.KNIGHTMETAL_HELMET, "Knightmetal Helmet");
		this.addItem(TFItems.KNIGHTMETAL_CHESTPLATE, "Knightmetal Chestplate");
		this.addItem(TFItems.KNIGHTMETAL_LEGGINGS, "Knightmetal Greaves");
		this.addItem(TFItems.KNIGHTMETAL_BOOTS, "Knightmetal Boots");
		this.addItem(TFItems.KNIGHTMETAL_SWORD, "Knightmetal Sword");
		this.add("item.twilightforest.knightmetal_sword.desc", "Extra damage to armored targets");
		this.addItem(TFItems.KNIGHTMETAL_PICKAXE, "Knightmetal Pickaxe");
		this.add("item.twilightforest.knightmetal_pickaxe.desc", "Extra damage to armored targets");
		this.addItem(TFItems.KNIGHTMETAL_AXE, "Knightmetal Axe");
		this.add("item.twilightforest.knightmetal_axe.desc", "Extra damage to unarmored targets");
		this.addItem(TFItems.KNIGHTMETAL_RING, "Knightmetal Loop");
		this.addItem(TFItems.KNIGHTMETAL_SHIELD, "Knightmetal Shield");
		this.addItem(TFItems.BLOCK_AND_CHAIN, "Block and Chain");
		this.addItem(TFItems.PHANTOM_HELMET, "Phantom Helmet");
		this.addItem(TFItems.PHANTOM_CHESTPLATE, "Phantom Chestplate");
		this.add("item.twilightforest.phantom_armor.desc", "Is never lost on death");

		this.addItem(TFItems.FIERY_BLOOD, "Fiery Blood");
		this.addItem(TFItems.FIERY_TEARS, "Fiery Tears");
		this.addItem(TFItems.FIERY_INGOT, "Fiery Ingot");
		this.addArmor("fiery", "Fiery");
		this.add("item.twilightforest.fiery_armor.desc", "Burns attackers");
		this.addItem(TFItems.FIERY_SWORD, "Fiery Sword");
		this.add("item.twilightforest.fiery_sword.desc", "Burns targets");
		this.addItem(TFItems.FIERY_PICKAXE, "Fiery Pickaxe");
		this.add("item.twilightforest.fiery_pickaxe.desc", "Auto-smelting");

		this.addItem(TFItems.ARCTIC_FUR, "Arctic Fur");
		this.addItem(TFItems.ARCTIC_HELMET, "Arctic Hood");
		this.addItem(TFItems.ARCTIC_CHESTPLATE, "Arctic Jacket");
		this.addItem(TFItems.ARCTIC_LEGGINGS, "Arctic Leggings");
		this.addItem(TFItems.ARCTIC_BOOTS, "Arctic Boots");
		this.add("item.twilightforest.arctic_armor.desc", "Dyeable");

		this.addItem(TFItems.ALPHA_YETI_FUR, "Alpha Yeti Fur");
		this.addItem(TFItems.YETI_HELMET, "Yeti Horned Helm");
		this.addItem(TFItems.YETI_CHESTPLATE, "Yeti Jacket");
		this.addItem(TFItems.YETI_LEGGINGS, "Yeti Leggings");
		this.addItem(TFItems.YETI_BOOTS, "Yeti Boots");
		this.add("item.twilightforest.yeti_armor.desc", "Chills attackers");


		this.addItem(TFItems.DIAMOND_MINOTAUR_AXE, "Diamond Minotaur Axe");
		this.addItem(TFItems.GOLDEN_MINOTAUR_AXE, "Golden Minotaur Axe");
		this.add("item.twilightforest.minotaur_axe.desc", "Extra damage while charging");
		this.addItem(TFItems.MAZEBREAKER_PICKAXE, "Mazebreaker");
		this.addItem(TFItems.ICE_SWORD, "Ice Sword");
		this.addItem(TFItems.ICE_BOMB, "Ice Bomb");
		this.addItem(TFItems.GLASS_SWORD, "Glass Sword");
		this.add("item.twilightforest.glass_sword.desc", "Creative Mode only");
		this.addItem(TFItems.TRIPLE_BOW, "Tri-Bow");
		this.addItem(TFItems.SEEKER_BOW, "Seeker Bow");
		this.addItem(TFItems.ICE_BOW, "Ice Bow");
		this.addItem(TFItems.ENDER_BOW, "Ender Bow");
		this.addItem(TFItems.GIANT_SWORD, "Giant's Sword");
		this.addItem(TFItems.GIANT_PICKAXE, "Giant's Pickaxe");
		this.add("item.twilightforest.giant_pickaxe.desc", "Breaks giant blocks");
		this.addItem(TFItems.LAMP_OF_CINDERS, "Lamp of Cinders");
		this.addItem(TFItems.CUBE_OF_ANNIHILATION, "Cube of Annihilation");

		this.addItem(TFItems.MOON_DIAL, "Moon Dial");
		this.add("item.twilightforest.moon_dial.phase_0", "Full Moon");
		this.add("item.twilightforest.moon_dial.phase_1", "Waning Gibbous");
		this.add("item.twilightforest.moon_dial.phase_2", "Third Quarter");
		this.add("item.twilightforest.moon_dial.phase_3", "Waning Crescent");
		this.add("item.twilightforest.moon_dial.phase_4", "New Moon");
		this.add("item.twilightforest.moon_dial.phase_5", "Waxing Crescent");
		this.add("item.twilightforest.moon_dial.phase_6", "First Quarter");
		this.add("item.twilightforest.moon_dial.phase_7", "Waxing Gibbous");
		this.add("item.twilightforest.moon_dial.phase_unknown", "Moon phase indeterminate");
		this.add("item.twilightforest.moon_dial.phase_unknown_fools", "404 moon not found");

		this.addItem(TFItems.POCKET_WATCH, "Rabbit's Pocket Watch");
		this.add("item.twilightforest.pocket_watch.desc", "It seems to always be running late");
		this.addItem(TFItems.EMPERORS_CLOTH, "Emperor's Cloth");
		this.add("item.twilightforest.emperors_cloth.desc", "Shrouded");
		this.addItem(TFItems.FOUR_LEAF_CLOVER, "Four-Leaf Clover");
		this.addItem(TFItems.CROWN_SPLINTER, "Crown Splinter");
		this.addItem(TFItems.MYSTIC_CROWN, "Mystic Crown");
		this.addItem(TFItems.ORE_MAGNET, "Ore Magnet");
		this.addItem(TFItems.CRUMBLE_HORN, "Crumble Horn");
		this.addItem(TFItems.MOONWORM_QUEEN, "Moonworm Queen");
		this.addItem(TFItems.PEACOCK_FEATHER_FAN, "Peacock Feather Fan");
		this.addItem(TFItems.TRANSFORMATION_POWDER, "Transformation Powder");
		this.addItem(TFItems.TOWER_KEY, "Tower Key");
		this.addItem(TFItems.BORER_ESSENCE, "Borer Essence");
		this.addItem(TFItems.CARMINITE, "Carminite");
		this.addItem(TFItems.MAGIC_BEANS, "Magic Beans");
		this.addItem(TFItems.CUBE_TALISMAN, "Talisman of the Cube");
		this.add("item.twilightforest.skull_candle.desc", "Has: %s %s Candle");
		this.add("item.twilightforest.skull_candle.desc.multiple", "Has: %s %s Candles");

		this.addItem(TFItems.TORCHBERRIES, "Torchberries");
		this.addItem(TFItems.RAW_VENISON, "Raw Venison");
		this.addItem(TFItems.COOKED_VENISON, "Venison Steak");
		this.addItem(TFItems.MAZE_WAFER, "Maze Wafer");
		this.addItem(TFItems.RAW_MEEF, "Raw Meef");
		this.addItem(TFItems.COOKED_MEEF, "Meef Steak");
		this.addItem(TFItems.MEEF_STROGANOFF, "Meef Stroganoff");
		this.addItem(TFItems.HYDRA_CHOP, "Hydra Chop");

		this.addItem(TFItems.CHARM_OF_LIFE_1, "Charm of Life I");
		this.addItem(TFItems.CHARM_OF_LIFE_2, "Charm of Life II");
		this.addItem(TFItems.CHARM_OF_KEEPING_1, "Charm of Keeping I");
		this.addItem(TFItems.CHARM_OF_KEEPING_2, "Charm of Keeping II");
		this.addItem(TFItems.CHARM_OF_KEEPING_3, "Charm of Keeping III");

		this.addMusicDisc(TFItems.MUSIC_DISC_RADIANCE, "Rotch Gwylt - Radiance");
		this.addMusicDisc(TFItems.MUSIC_DISC_STEPS, "Rotch Gwylt - Steps");
		this.addMusicDisc(TFItems.MUSIC_DISC_SUPERSTITIOUS, "Rotch Gwylt - Superstitious");
		this.addMusicDisc(TFItems.MUSIC_DISC_HOME, "MrCompost - Home");
		this.addMusicDisc(TFItems.MUSIC_DISC_WAYFARER, "MrCompost - Wayfarer");
		this.addMusicDisc(TFItems.MUSIC_DISC_FINDINGS, "MrCompost - Findings");
		this.addMusicDisc(TFItems.MUSIC_DISC_MAKER, "MrCompost - Maker");
		this.addMusicDisc(TFItems.MUSIC_DISC_THREAD, "MrCompost - Thread");
		this.addMusicDisc(TFItems.MUSIC_DISC_MOTION, "MrCompost - Motion");

		this.add("item.twilightforest.lower_goblin_knight_spawn_egg", "Goblin Knight Spawn Egg");

		this.addEntityAndEgg(TFEntities.ADHERENT, "Adherent");
		this.addEntityAndEgg(TFEntities.ALPHA_YETI, "Alpha Yeti");
		this.addEntityAndEgg(TFEntities.ARMORED_GIANT, "Armored Giant");
		this.addEntityAndEgg(TFEntities.BIGHORN_SHEEP, "Bighorn Sheep");
		this.addEntityAndEgg(TFEntities.BLOCKCHAIN_GOBLIN, "Block and Chain Goblin");
		this.addEntityAndEgg(TFEntities.BOAR, "Boar");
		this.addEntityAndEgg(TFEntities.CARMINITE_BROODLING, "Carminite Broodling");
		this.addEntityAndEgg(TFEntities.CARMINITE_GHASTGUARD, "Carminite Ghastguard");
		this.addEntityAndEgg(TFEntities.CARMINITE_GHASTLING, "Carminite Ghastling");
		this.addEntityAndEgg(TFEntities.CARMINITE_GOLEM, "Carminite Golem");
		this.addEntityAndEgg(TFEntities.DEATH_TOME, "Death Tome");
		this.addEntityAndEgg(TFEntities.DEER, "Deer");
		this.addEntityAndEgg(TFEntities.DWARF_RABBIT, "Dwarf Rabbit");
		this.addEntityAndEgg(TFEntities.FIRE_BEETLE, "Fire Beetle");
		this.addEntityAndEgg(TFEntities.GIANT_MINER, "Giant Miner");
		this.addEntityAndEgg(TFEntities.HARBINGER_CUBE, "Harbinger Cube");
		this.addEntityAndEgg(TFEntities.HEDGE_SPIDER, "Hedge Spider");
		this.addEntityAndEgg(TFEntities.HELMET_CRAB, "Helmet Crab");
		this.addEntityAndEgg(TFEntities.HOSTILE_WOLF, "Hostile Wolf");
		this.addEntityAndEgg(TFEntities.HYDRA, "Hydra");
		this.addEntityAndEgg(TFEntities.ICE_CRYSTAL, "Ice Crystal");
		this.addEntityAndEgg(TFEntities.KING_SPIDER, "King Spider");
		this.addEntityAndEgg(TFEntities.KNIGHT_PHANTOM, "Knight Phantom");
		this.add("entity.twilightforest.knight_phantom.plural", "Knight Phantoms");
		this.addEntityAndEgg(TFEntities.KOBOLD, "Kobold");
		this.addEntityAndEgg(TFEntities.LICH, "Lich");
		this.addEntityAndEgg(TFEntities.MAZE_SLIME, "Maze Slime");
		this.addEntityAndEgg(TFEntities.MINOSHROOM, "Minoshroom");
		this.addEntityAndEgg(TFEntities.MINOTAUR, "Minotaur");
		this.addEntityAndEgg(TFEntities.MIST_WOLF, "Mist Wolf");
		this.addEntityAndEgg(TFEntities.MOSQUITO_SWARM, "Mosquito Swarm");
		this.addEntityAndEgg(TFEntities.NAGA, "Naga");
		this.addEntityAndEgg(TFEntities.PENGUIN, "Penguin");
		this.addEntityAndEgg(TFEntities.PINCH_BEETLE, "Pinch Beetle");
		this.addEntityAndEgg(TFEntities.QUEST_RAM, "Questing Ram");
		this.addEntityAndEgg(TFEntities.RAVEN, "Raven");
		this.addEntityAndEgg(TFEntities.REDCAP, "Redcap");
		this.addEntityAndEgg(TFEntities.REDCAP_SAPPER, "Redcap Sapper");
		this.addEntityAndEgg(TFEntities.SKELETON_DRUID, "Skeleton Druid");
		this.addEntityAndEgg(TFEntities.SLIME_BEETLE, "Slime Beetle");
		this.addEntityAndEgg(TFEntities.SNOW_GUARDIAN, "Snow Guardian");
		this.addEntityAndEgg(TFEntities.SNOW_QUEEN, "Snow Queen");
		this.addEntityAndEgg(TFEntities.SQUIRREL, "Squirrel");
		this.addEntityAndEgg(TFEntities.STABLE_ICE_CORE, "Stable Ice Core");
		this.addEntityAndEgg(TFEntities.SWARM_SPIDER, "Swarm Spider");
		this.addEntityAndEgg(TFEntities.TINY_BIRD, "Tiny Bird");
		this.addEntityAndEgg(TFEntities.TOWERWOOD_BORER, "Towerwood Borer");
		this.addEntityAndEgg(TFEntities.TROLL, "Troll");
		this.addEntityAndEgg(TFEntities.UNSTABLE_ICE_CORE, "Unstable Ice Core");
		this.addEntityAndEgg(TFEntities.UR_GHAST, "Ur-Ghast");
		this.addEntityAndEgg(TFEntities.WINTER_WOLF, "Winter Wolf");
		this.addEntityAndEgg(TFEntities.WRAITH, "Wraith");
		this.addEntityAndEgg(TFEntities.YETI, "Yeti");

		this.addEntityType(TFEntities.LICH_MINION, "Lich Minion");
		this.addEntityType(TFEntities.LOYAL_ZOMBIE, "Loyal Zombie");
		this.addEntityType(TFEntities.LOWER_GOBLIN_KNIGHT, "Lower Goblin Knight");
		this.addEntityType(TFEntities.RISING_ZOMBIE, "Zombie");
		this.addEntityType(TFEntities.ROVING_CUBE, "Roving Cube");
		this.addEntityType(TFEntities.UPPER_GOBLIN_KNIGHT, "Upper Goblin Knight");

		this.addEntityType(TFEntities.ICE_SNOWBALL, "Ice Snowball");
		this.addEntityType(TFEntities.ICE_ARROW, "Ice Arrow");
		this.addEntityType(TFEntities.THROWN_ICE, "Ice Bomb");
		this.addEntityType(TFEntities.SEEKER_ARROW, "Seeker Arrow");
		this.addEntityType(TFEntities.MOONWORM_SHOT, "Moonworm");
		this.addEntityType(TFEntities.NATURE_BOLT, "Nature Bolt");
		this.addEntityType(TFEntities.SLIME_BLOB, "Slime Blob");
		this.addEntityType(TFEntities.TOME_BOLT, "Death Tome Bolt");
		this.addEntityType(TFEntities.WAND_BOLT, "Twilight Scepter Bolt");
		this.addEntityType(TFEntities.LICH_BOLT, "Lich Bolt");
		this.addEntityType(TFEntities.LICH_BOMB, "Explosive Lich Bolt");
		this.addEntityType(TFEntities.HYDRA_MORTAR, "Hydra Mortar");
		this.addEntityType(TFEntities.FALLING_ICE, "Falling Ice");
		this.addEntityType(TFEntities.THROWN_WEP, "Thrown Weapon");
		this.addEntityType(TFEntities.CHARM_EFFECT, "Charm Effect");
		this.addEntityType(TFEntities.CHAIN_BLOCK, "Block and Chain");
		this.addEntityType(TFEntities.CUBE_OF_ANNIHILATION, "Cube of Annihilation");
		this.addEntityType(TFEntities.THROWN_BLOCK, "Thrown Block");
		this.addEntityType(TFEntities.SLIDER, "Moving Slide Trap");
		this.addEntityType(TFEntities.PROTECTION_BOX, "Progression Protection Box");
		this.addEntityType(TFEntities.MAGIC_PAINTING, "Magic Painting");

		SUBTITLE_GENERATOR.forEach(this::add);

		this.addDeathMessage("ghastTear", "%1$s was scalded by fiery tears");
		this.addDeathMessage("ghastTear.player", "%1$s was scalded by fiery tears while escaping %2$s");
		this.addDeathMessage("hydraFire", "%1$s was roasted alive by the Hydra");
		this.addDeathMessage("hydraFire.player", "%1$s was roasted alive by the Hydra while escaping %2$s");
		this.addDeathMessage("hydraBite", "%1$s's skin was ripped off by the Hydra");
		this.addDeathMessage("hydraBite.player", "%1$s's skin was ripped off by the Hydra while escaping %2$s");
		this.addDeathMessage("lichBolt", "The Lich's aim was better than %1$s");
		this.addDeathMessage("lichBolt.player", "The Lich and %2$s had better aim than %1$s");
		this.addDeathMessage("lichBomb", "%1$s succumbed to the Lich's explosive magic");
		this.addDeathMessage("lichBomb.player", "%1$s succumbed to the Lich's explosive magic while escaping %2$s");
		this.addDeathMessage("chillingBreath", "%1$s was frozen to death by the Snow Queen");
		this.addDeathMessage("chillingBreath.player", "%1$s was frozen to death by the Snow Queen while escaping %2$s");
		this.addDeathMessage("squish", "%1$s was squashed by the Snow Queen");
		this.addDeathMessage("squish.player", "%1$s was squashed by the Snow Queen while escaping %2$s");
		this.addDeathMessage("thrownAxe", "%1$s was decapitated by a thrown axe");
		this.addDeathMessage("thrownAxe.player", "%1$s was decapitated by a thrown axe while escaping %2$s");
		this.addDeathMessage("thrownPickaxe", "%1$s was decapitated by a thrown pickaxe");
		this.addDeathMessage("thrownPickaxe.player", "%1$s was decapitated by a thrown pickaxe while escaping %2$s");
		this.addDeathMessage("fireJet", "%1$s accidentally walked into a Fire Jet");
		this.addDeathMessage("fireJet.player", "%1$s accidentally walked into a Fire Jet while escaping %2$s");
		this.addDeathMessage("reactor", "%1$s stood too close to a Carminite Reactor");
		this.addDeathMessage("reactor.player", "%1$s stood too close to a Carminite Reactor while escaping %2$s");
		this.addDeathMessage("slider", "%1$s was sliced up by a Sliding Trap");
		this.addDeathMessage("slider.player", "%1$s was sliced up by a Sliding Trap while escaping %2$s");
		this.addDeathMessage("thorns", "%1$s walked into some thorns");
		this.addDeathMessage("thorns.player", "%1$s walked into some thorns while escaping %2$s");
		this.addDeathMessage("knightmetal", "%1$s was skewered by a Knightmetal block");
		this.addDeathMessage("knightmetal.player", "%1$s was skewered by a Knightmetal block while escaping %2$s");
		this.addDeathMessage("fiery", "%1$s walked onto a Fiery block");
		this.addDeathMessage("fiery.player", "%1$s walked onto a Fiery block while escaping %2$s");
		this.addDeathMessage("thrownBlock", "%1$s was squashed by a thrown block");
		this.addDeathMessage("thrownBlock.player", "%1$s was squashed by a thrown block while escaping %2$s");
		this.addDeathMessage("expired", "%1$s's life expired");
		this.addDeathMessage("expired.player", "%1$s's life expired");

		this.addDeathMessage("axing", "%1$s was chopped up by %2$s");
		this.addDeathMessage("axing.item", "%1$s was chopped up by %2$s using %3$s");
		this.addDeathMessage("moonworm", "%1$s was shot by Moonworm");
		this.addDeathMessage("ant", "%1$s was squashed like an ant by %2$s");
		this.addDeathMessage("ant.item", "%1$s was squashed like an ant by %2$s holding %3$s");
		this.addDeathMessage("haunt", "%1$s joined the %2$s's haunt");
		this.addDeathMessage("haunt.item", "%1$s joined the %2$s's haunt after being killed by %3$s");
		this.addDeathMessage("clamped", "%1$s was squeezed to death by %2$s");
		this.addDeathMessage("clamped.item", "%1$s was squeezed to death by %2$s using %3$s");
		this.addDeathMessage("scorched", "%1$s was scorched by %2$s");
		this.addDeathMessage("scorched.item", "%1$s was scorched by %2$s using %3$s");
		this.addDeathMessage("frozen", "%1$s was frozen by %2$s using an Ice Bomb");
		this.addDeathMessage("frozen.item", "%1$s was frozen by %2$s using %3$s");
		this.addDeathMessage("spiked", "%1$s was skewered by %2$s");
		this.addDeathMessage("spiked.item", "%1$s was skewered by %2$s using %3$s");
		this.addDeathMessage("leafBrain", "%1$s's brain was turned into leaves by %2$s");
		this.addDeathMessage("leafBrain.item", "%1$s's brain was turned into leaves by %2$s using %3$s");
		this.addDeathMessage("lostWords", "%1$s was at a loss for words after being killed by %2$s");
		this.addDeathMessage("lostWords.item", "%1$s was at a loss for words after being killed by %2$s using %3$s");
		this.addDeathMessage("schooled", "%1$s was schooled by %2$s");
		this.addDeathMessage("schooled.item", "%1$s was schooled by %2$s using %3$s");
		this.addDeathMessage("snowballFight", "%1$s lost a snowball fight to %2$s");
		this.addDeathMessage("snowballFight.item", "%1$s lost a snowball fight to %2$s using %3$s");
		this.addDeathMessage("lifedrain", "%1$s's life was drained by %2$s");
		this.addDeathMessage("lifedrain.item", "%1$s's life was drained by %2$s using %3$s");
		this.addDeathMessage("yeeted", "%1$s was yeeted for the last time");
		this.addDeathMessage("yeeted.entity", "%1$s was yeeted for the last time by %2$s");
		this.addDeathMessage("yeeted.item", "%1$s was yeeted for the last time by %2$s while somehow holding %3$s");
		this.addDeathMessage("acid_rain", "%1$s went dancing in the acid rain");

		this.addStat("blocks_crumbled", "Blocks Crumbled");
		this.addStat("bugs_squished", "Bugs Squashed");
		this.addStat("e115_slices_eaten", "Experiment 115 Slices Eaten");
		this.addStat("keeping_charms_activated", "Charms of Keeping Used");
		this.addStat("life_charms_activated", "Charms of Life Used");
		this.addStat("skull_candles_made", "Skull Candles Created");
		this.addStat("tf_shields_broken", "Fortification Shields Broken");
		this.addStat("torchberries_harvested", "Torchberries Harvested");
		this.addStat("trophy_pedestals_activated", "Trophy Pedestals Activated");
		this.addStat("uncrafting_table_interactions", "Interactions with Uncrafting Table");

		this.add("config.jade.plugin_twilightforest.quest_ram_wool", "Questing Ram Wool");
		this.add("config.jade.plugin_twilightforest.chiseled_bookshelf_spawner", "Chiseled Canopy Bookshelf Spawns");

		this.add("twilightforest.book.author", "a forgotten explorer");

		this.addBookAndContents("lichtower", "Notes on a Pointy Tower",
			"§8[An explorer's notebook, gnawed on by monsters]§0\n\nI have begun examining the strange aura surrounding this tower. The bricks of the tower are protected by a curse, stronger than any I've seen before. The magic from the curse is boiling off into the",
			"surrounding area.\n\nIn my homeland I would have many options for dealing with this magic, but here my supplies are limited. I shall have to research...",
			"§8[[Many entries later]]§0\n\nA breakthrough! In my journeys I sighted a huge snake-like monster in a decorated courtyard. Nearby, I picked up a worn down, discarded green scale.\n\nThe magic in the scale seems to have the curse-breaking",
			"properties I need, but the magic is too dim. I may need to acquire a fresher specimen, directly from the creature.");

		this.addBookAndContents("labyrinth", "Notes on a Swampy Labyrinth",
			"§8[[An explorer's notebook, written on waterproof paper]]§0\n\nThe mosquitoes in this swamp are vexing, but strange. The vast majority of them seem to have no natural source, nor do they seem to have a role in the local ecology. I have begun to suspect that they are",
			"some kind of magical curse.\n\n§8[[Next entry]]§0\n\nNow that I have encountered a protection spell on the ruined labyrinth here, I consider my suspicions confirmed. Both the protection spell and the mosquitoes are a",
			"curse. This curse seems to have a different source from the others I have encountered. I will have to research further...\n\n§8[[Next entry]]§0\n\nThe curse seems to be of a type too powerful for one being alone to",
			"produce. Several wizards working in combination would be necessary.\n\nIf one of the wizards stopped contributing, the whole of the curse over the entire swamp would fall. Strangely, my divinations do not show signs of any nearby living wizards.",
			"I did see something interesting in one of the nearby pointy-roofed towers though...");

		this.addBookAndContents("hydralair", "Notes on the Fire Swamp",
			"§8[[An explorer's notebook, written on fireproof paper]]§0\n\nFire is a trivial obstacle for a master explorer such as myself. I have traversed seas of fire, and swam through oceans of lava. The burning air here is an interesting variation, but",
			"ultimately no hindrance.\n\nWhat does stop me though is that I have encountered another protection spell, this time surrounding a mighty creature that must be king of this fire swamp. This is not the first protection spell I have encountered, and I am",
			"beginning to unravel the mysteries of how they work.\n\nIf this spell is like the others, it will be sustained by a powerful creature nearby. Surrounding the fire swamp are several wet swamps, and under those swamps are labyrinths full of minotaurs.",
			"The logical choice to bind such a spell to would be some sort of powerful minotaur, different in some way from the others that surround it...");

		this.addBookAndContents("tfstronghold", "Notes on a Stronghold",
			"§8[[An explorer's notebook, written on faintly glowing paper]]§0\n\nThe tendrils of darkness surrounding this area are just a manifestation of a protective spell over the entire dark forest. The spell causes blindness, which is quite vexing. I have seen several",
			"interesting things in the area and would like to keep exploring.\n\n§8[[Next entry]]§0\n\nI have found ruins in the dark forest. They belong to a stronghold, of a type usually inhabited by knights. Rather than knights though, this stronghold is full of",
			"goblins. They wear knightly armor, but their behavior is most un-knightly.\n\n§8[[Next entry]]§0\n\nDeep in the ruins, I have found a pedestal. The pedestal seems to be of a type that knights would place trophies on to prove their strength.",
			"Obtaining a powerful scepter would seem to weaken the curse on the dark forest, and placing a trophy associated with a powerful creature on the pedestal would likely grant access into the main part of the stronghold.");

		this.addBookAndContents("darktower", "Notes on a Wooden Tower",
			"§8[[An explorer's notebook that seems to have survived an explosion]]§0\n\nThis tower clearly has mechanisms that are not responding to me. Their magic almost yearns to acknowledge my touch, but it cannot. It is if the devices of the tower are being",
			"suppressed by a powerful group of beings nearby.\n\n§8[[Next entry]]§0\n\nThe magic seems to emanate from deep within the strongholds nearby. It can't come from the goblins, as their magic is charming, but unfocused. There",
			"must still be some force still active in the strongholds.\n\n§8[[Next entry]]§0\n\nMy analysis indicates that it comes from several sources, operating as a group. I will head back to the stronghold after I resupply...");

		this.addBookAndContents("yeticave", "Notes on an Icy Cave",
			"§8[[An explorer's notebook, covered in frost]]§0\n\nThe blizzard surrounding these snowy lands is unceasing. This is no ordinary snowfall--this is a magical phenomenon. I will have to conduct experiments to find what is capable of",
			"causing such an effect.\n\n§8[[Next entry]]§0\n\nThe curse seems to be of a type too powerful for one being alone to produce. Several wizards working in combination would be necessary. If one of the wizards stopped",
			"contributing, the blizzard would calm. Strangely, my divinations do not show signs of any nearby living wizards. I did see something interesting in one of the nearby pointy-roofed towers though...");

		this.addBookAndContents("icetower", "Notes on Auroral Fortification",
			"§8[[An explorer's notebook, caked in ice]]§0\n\nI overcame one blizzard, only to run into this terrible ice storm atop the glacier. My explorations have shown me the splendor of an ice palace, shining with the colors of the",
			"polar aurora. It all seems protected by some sort of curse.\n\n§8[[Next entry]]§0\n\nI am no novice. This curse is fed by the power of a creature nearby. The cause of the curse surrounding the fire swamp was built off the power of the",
			"leader of the minotaurs nearby.\n\nSurrounding this glacier, there are masses of yetis. Perhaps the yetis have some sort of leader...");

		this.addBookAndContents("trollcave", "Notes on the Highlands",
			"§8[[An explorer's notebook, damaged by acid]]§0\n\nThere seems to be no way to protect myself from the toxic rainstorm surrounding this area. In my brief excursions, I have also encountered another protection spell, similar to the others I have",
			"witnessed. The spell must be connected to the toxic storm in some way. Further research to follow...\n\n§8[[Next entry]]§0\n\nSuch supreme weather magic must be the result of multiple undefeated great evils in this world. My studies contain",
			"several clues pointing at a searing swamp, a forest coated in deep darkness, and a realm coated in snow.");

		this.addBookAndContents("unknown", "Notes on the Unexplained",
			"§8[[This book shows signs of having been copied many times]]§0\n\nI cannot explain the field surrounding this structure, but the magic is powerful. If this curse is like the others, then the answer to unlocking it lies elsewhere. Perhaps there is something I have left",
			"undone, or some monster I have yet to defeat. I will have to turn back. I will return to this place later, to see if anything has changed.");

		this.addScreenMessage("optifine.title", "WARNING: OPTIFINE DETECTED");
		this.addScreenMessage("optifine.message", "Before proceeding, please note that Optifine is known to cause crashes, multipart entity visual bugs and many other issues.\n\nBefore reporting a bug, please remove Optifine first and check again to see if the bug is still present.\n\nOptifine-related issues are not solvable on Twilight Forest's end!\n\nThis screen may be disabled in the Client Config.");
		this.addScreenMessage("optifine.suggestions", "Here's a selection of mods that we recommend using instead.");

		this.addScreenMessage("progression_end.message", "This is the end of progression for now. The Final Castle that awaits on the plateau is still unfinished and a work in progress. If you would like to keep up with the mod's development you can join our %s.");
		this.addScreenMessage("progression_end.discord", "Discord Server");

		this.addScreenMessage("crumble_horn_jei", "Crumble Horn");
		this.addScreenMessage("transformation_jei", "Transformation Powder");
		this.addScreenMessage("uncrafting_jei", "Uncrafting");
		this.addScreenMessage("moonworm_queen_jei", "Moonworm Queen Repairing");
		this.add("item.twilightforest.moonworm_queen.jei_info_message", "Torchberries restore 64 durability each");

		this.addTrim("carminite", "Carminite");
		this.addTrim("fiery", "Fiery");
		this.addTrim("ironwood", "Ironwood");
		this.addTrim("knightmetal", "Knightmetal");
		this.addTrim("naga_scale", "Naga Scale");
		this.addTrim("steeleaf", "Steeleaf");

		this.add("museumcurator.animalhusbandry.twilightforest.bugs", "Bugs");
		this.add("museumcurator.architecture.twilightforest.aurorablocks", "Aurora Blocks");
		this.add("museumcurator.architecture.twilightforest.banisters", "Banisters");
		this.add("museumcurator.architecture.twilightforest.castlebrick", "Castle Brick");
		this.add("museumcurator.architecture.twilightforest.giantblocks", "Giant Blocks");
		this.add("museumcurator.botany.twilightforest.beanstalk", "Beanstalk");
		this.add("museumcurator.botany.twilightforest.canopytree", "Canopy Tree");
		this.add("museumcurator.botany.twilightforest.darkwoodtree", "Darkwood Tree");
		this.add("museumcurator.botany.twilightforest.miningtree", "Minewood Tree");
		this.add("museumcurator.botany.twilightforest.sortingtree", "Sortingwood Tree");
		this.add("museumcurator.botany.twilightforest.mangrovetree", "Mangrove Tree");
		this.add("museumcurator.botany.twilightforest.timetree", "Timewood Tree");
		this.add("museumcurator.botany.twilightforest.transformationtree", "Transwood Tree");
		this.add("museumcurator.botany.twilightforest.twilightoaktree", "Twilight Oak Tree");
		this.add("museumcurator.equipment.twilightforest.scepters", "Scepters of Power");
		this.add("museumcurator.lithology.twilightforest.deadrock", "Deadrock");
		this.add("museumcurator.lithology.twilightforest.mazestone", "Mazestone");
		this.add("museumcurator.lithology.twilightforest.nagastone", "Nagastone");
		this.add("museumcurator.machinery.twilightforest.carminitemachines", "Carminite Mechanisms");
		this.add("museumcurator.metallurgy.twilightforest.fiery", "Fiery Metal");
		this.add("museumcurator.metallurgy.twilightforest.ironwood", "Ironwood");
		this.add("museumcurator.metallurgy.twilightforest.knightmetal", "Knightmetal");

		MAGIC_PAINTING_HELPER.forEach((location, stringStringPair) -> {
			this.add(location.toLanguageKey("magic_painting", "title"), stringStringPair.getFirst());
			this.add(location.toLanguageKey("magic_painting", "author"), stringStringPair.getSecond());
		});

		this.createTip("anvil_squashing", "Bugs can be squashed by Anvils.");
		this.createTip("arctic_armor", "Arctic Armor can be dyed any color.");
		this.createTip("banister_shape", "Banisters can be right-clicked with an axe to change their height.");
		this.createTip("block_and_chain", "Enchanting a Block and Chain with Destruction allows it to break blocks.");
		this.createTip("boggard", "What the heck is a Boggard?");
		this.createTip("bugs_on_head", "Bugs will happily sit on your head.");
		this.createTip("burnt_thorns", "Burnt Thorns will disintegrate when stepped on.");
		this.createTip("carminite_builder", "Carminite Builders place temporary blocks in the direction you're looking.");
		this.createTip("charm_of_keeping", "A Charm of Keeping will return parts of your inventory after death.");
		this.createTip("charm_of_life", "A Charm of Life can save you from a fatal blow.");
		this.createTip("crumble_horn", "The Crumble Horn will deteriorate nearby blocks when used.");
		this.createTip("druid_hut", "Druid huts sometimes have hidden basements.");
		this.createTip("e115_pickup", "Sneak + right-click placed Experiment 115 to pick it back up.");
		this.createTip("e115_sprinkle", "Redstone can be sprinkled on top of Experiment 115.");
		this.createTip("experiment_115", "Does anyone know what Experiment 115 really is?");
		this.createTip("fiery_pickaxe", "A Fiery Pickaxe will smelt any blocks it breaks.");
		this.createTip("ghast_trap", "Killing Carminite Ghastlings near a Ghast Trap will charge it.");
		this.createTip("glass_sword", "Glass Swords break after a single hit.");
		this.createTip("hollow_log", "Various things can be placed inside Hollow Logs such as snow, moss, or ladders.");
		this.createTip("hollow_oak_sapling", "Saplings that grow into giant hollow trees can be found in Druid Huts.");
		this.createTip("hollow_oak_tree", "Hollow Oak trees sometimes have dungeon-like rooms with unique loot in their leaves.");
		this.createTip("hydra_chop", "Hydra Chops fill up your hunger bar completely when eaten.");
		this.createTip("hydra_heads", "For each head slain, the Hydra will regrow two in its place!");
		this.createTip("hydra_mortars", "You can deflect the Hydra's mortar attack.");
		this.createTip("ice_core", "Ice Cores and Snow Guardians melt in hot biomes.");
		this.createTip("jars", "Fireflies and Cicadas can be put into jars.");
		this.createTip("kobold", "Kobold");
		this.createTip("labyrinth_vault", "The Labyrinth contains a secret room.");
		this.createTip("lich_scepters", "The Lich drops a variety of magic-based scepters.");
		this.createTip("liveroot", "Liveroot is found under most trees.");
		this.createTip("magic_beans", "Planting Magic Beans on Uberous Soil sprouts a mighty beanstalk.");
		this.createTip("magic_leaves", "Magic Tree Leaves won't drop saplings when broken.");
		this.createTip("magic_map", "Magic Maps are used to easily locate structures.");
		this.createTip("magic_saplings", "Special magic saplings can be found inside Hollow Oak trees.");
		this.createTip("mazebreaker", "The Mazebreaker can break Mazestone blocks 16 times faster and doesn't take extra durability damage.");
		this.createTip("mining_tree", "The Miner's Tree will pull ores up to the surface.");
		this.createTip("moon_dial", "The Moon Dial shows the current phase of the moon.");
		this.createTip("moonworm_queen", "The Moonworm Queen can be fed Torchberries.");
		this.createTip("mushglooms", "Mushglooms cannot be bonemealed into giant mushrooms. However, placing them on Uberous Soil will make them grow.");
		this.createTip("music_disc", "Music Discs are found outside of dungeons.");
		this.createTip("naga", "The Naga can be stunned by making it ram something hard!");
		this.createTip("netherite_axe", "There will never be a Netherite Minotaur Axe.");
		this.createTip("ore_magnet", "The Ore Magnet can pull ore veins up to the surface.");
		this.createTip("peacock_feather_fan", "The Peacock Feather Fan can be used to extinguish Candles.");
		this.createTip("phantom_armor", "Phantom Armor is automatically kept on death.");
		this.createTip("quest_ram", "The Questing Ram will reward anyone who gives it what it's missing.");
		this.createTip("red_thread", "Red Thread can be seen through walls.");
		this.createTip("redcap", "Redcaps can place and light TNT.");
		this.createTip("skull_candle", "Candles can be placed on top of mob heads to create a fancy light source.");
		this.createTip("sorting_tree", "The Sorting Tree will sort chests next to it into other chests nearby.");
		this.createTip("spooky_forest", "The Spooky Forest is not Halloween themed.");
		this.createTip("structure_conquering", "Killing a boss will make mobs stop spawning in that structure.");
		this.createTip("structure_spawning", "Structures spawn in a grid-like pattern.");
		this.createTip("time_tree", "The Tree of Time will accelerate the growth of nearby crops.");
		this.createTip("torchberries", "We did glow berries first!");
		this.createTip("towerwood", "Towerwood Planks are very resistant, but not immune, to fire.");
		this.createTip("transformation_tree", "The Tree of Transformation will convert the area around it into an Enchanted Forest.");
		this.createTip("trollber_ripening", "Killing a Troll will ripen nearby Trollber.");
		this.createTip("twilight_portal", "Throw a diamond into a pool of water surrounded by flowers.");
		this.createTip("ur_ghast", "The Ur-Ghast can be pulled down from the sky using Ghast Traps.");
		this.createTip("vanishing_block", "Vanishing Blocks will disappear forever when activated.");
		this.createTip("worldgen_features", "The forest is filled with many ruins. Some may even contain unique items.");
		this.createTip("yeti", "Yetis love throwing things.");
		this.createTip("zombie_healing", "Zombies summoned with a Zombie Scepter can be healed with Rotten Flesh.");

		this.translateTag(ItemTagGenerator.CARMINITE_GEMS, "Carminite Gems");
		this.translateTag(ItemTagGenerator.FIERY_INGOTS, "Fiery Ingots");
		this.translateTag(ItemTagGenerator.IRONWOOD_INGOTS, "Ironwood Ingots");
		this.translateTag(ItemTagGenerator.KNIGHTMETAL_INGOTS, "Knightmetal Ingots");
		this.translateTag(ItemTagGenerator.STEELEAF_INGOTS, "Steeleaf Ingots");
		this.translateTag(ItemTagGenerator.PAPER, "Papers");
		this.translateTag(ItemTagGenerator.RAW_MATERIALS_IRONWOOD, "Raw Ironwood");
		this.translateTag(ItemTagGenerator.RAW_MATERIALS_KNIGHTMETAL, "Raw Knightmetal");
		this.translateTag(ItemTagGenerator.STORAGE_BLOCKS_ARCTIC_FUR, "Arctic Fur Storage Blocks");
		this.translateTag(ItemTagGenerator.STORAGE_BLOCKS_CARMINITE, "Carminite Storage Blocks");
		this.translateTag(ItemTagGenerator.STORAGE_BLOCKS_FIERY, "Fiery Storage Blocks");
		this.translateTag(ItemTagGenerator.STORAGE_BLOCKS_IRONWOOD, "Ironwood Storage Blocks");
		this.translateTag(ItemTagGenerator.STORAGE_BLOCKS_KNIGHTMETAL, "Knightmetal Storage Blocks");
		this.translateTag(ItemTagGenerator.STORAGE_BLOCKS_STEELEAF, "Steeleaf Storage Blocks");
		this.translateTag(ItemTagGenerator.ARCTIC_FUR, "Arctic Fur");
		this.translateTag(ItemTagGenerator.BANNED_UNCRAFTABLES, "Can't be Uncrafted");
		this.translateTag(ItemTagGenerator.BANNED_UNCRAFTING_INGREDIENTS, "Banned Uncrafting Ingredients");
		this.translateTag(ItemTagGenerator.BANISTERS, "Banisters");
		this.translateTag(ItemTagGenerator.CANOPY_LOGS, "Canopy Logs");
		this.translateTag(ItemTagGenerator.DARKWOOD_LOGS, "Darkwood Logs");
		this.translateTag(ItemTagGenerator.FIERY_VIAL, "Fiery Vials");
		this.translateTag(ItemTagGenerator.KEPT_ON_DEATH, "Kept on Death");
		this.translateTag(ItemTagGenerator.BLOCK_AND_CHAIN_ENCHANTABLE, "Block and Chain Enchantable");
		this.translateTag(ItemTagGenerator.KOBOLD_PACIFICATION_BREADS, "Kobold Pacification Items");
		this.translateTag(ItemTagGenerator.BOAR_TEMPT_ITEMS, "Boar Temptables");
		this.translateTag(ItemTagGenerator.DEER_TEMPT_ITEMS, "Deer Temptables");
		this.translateTag(ItemTagGenerator.DWARF_RABBIT_TEMPT_ITEMS, "Dwarf Rabbit Temptables");
		this.translateTag(ItemTagGenerator.PENGUIN_TEMPT_ITEMS, "Penguin Temptables");
		this.translateTag(ItemTagGenerator.RAVEN_TEMPT_ITEMS, "Raven Temptables");
		this.translateTag(ItemTagGenerator.SQUIRREL_TEMPT_ITEMS, "Squirrel Temptables");
		this.translateTag(ItemTagGenerator.TINY_BIRD_TEMPT_ITEMS, "Tiny Bird Temptables");
		this.translateTag(ItemTagGenerator.TWILIGHT_LOGS, "Twilight Forest Logs");
		this.translateTag(ItemTagGenerator.MANGROVE_LOGS, "Mangrove Logs");
		this.translateTag(ItemTagGenerator.MINING_LOGS, "Miningwood Logs");
		this.translateTag(ItemTagGenerator.PORTAL_ACTIVATOR, "Twilight Forest Portal Activators");
		this.translateTag(ItemTagGenerator.REPAIRS_FIERY_TOOLS, "Repairs Fiery Tools");
		this.translateTag(ItemTagGenerator.REPAIRS_GIANT_TOOLS, "Repairs Giant Tools");
		this.translateTag(ItemTagGenerator.REPAIRS_ICE_TOOLS, "Repairs Ice Tools");
		this.translateTag(ItemTagGenerator.REPAIRS_IRONWOOD_TOOLS, "Repairs Ironwood Tools");
		this.translateTag(ItemTagGenerator.REPAIRS_KNIGHTMETAL_TOOLS, "Repairs Knightmetal Tools");
		this.translateTag(ItemTagGenerator.REPAIRS_STEELEAF_TOOLS, "Repairs Steeleaf Tools");
		this.translateTag(ItemTagGenerator.SORTING_LOGS, "Sortingwood Logs");
		this.translateTag(ItemTagGenerator.TIME_LOGS, "Timewood Logs");
		this.translateTag(ItemTagGenerator.TOWERWOOD, "Towerwood Blocks");
		this.translateTag(ItemTagGenerator.TRANSFORMATION_LOGS, "Transformation Logs");
		this.translateTag(ItemTagGenerator.TWILIGHT_OAK_LOGS, "Twilight Oak Logs");
		this.translateTag(ItemTagGenerator.UNCRAFTING_IGNORES_COST, "Uncrafting Table Ignores Cost");
		this.translateTag(ItemTagGenerator.WIP, "Work In Progress Items");
		this.translateTag(FluidTagGenerator.FIRE_JET_FUEL, "Fire Jet Fuel");

		//config
		this.add("twilightforest.configuration.title", "Twilight Forest Config");
		this.add("twilightforest.configuration.section.twilightforest.client.toml", "Client Settings");
		this.add("twilightforest.configuration.section.twilightforest.client.toml.title", "Client Settings");
		this.add("twilightforest.configuration.section.twilightforest.common.toml", "Common Settings");
		this.add("twilightforest.configuration.section.twilightforest.common.toml.title", "Common Settings");

		//client config
		this.configEntry("silent_cicadas", "Silent Cicadas", ConfigComments.SILENT_CICADAS);
		this.configEntry("silent_cicadas_on_head", "Silent Cicadas on Head", ConfigComments.SILENT_CICADAS_ON_HEAD);
		this.configEntry("screen_shake", "Beanstalk Screen Shake", ConfigComments.SCREEN_SHAKE);
		this.configEntry("animate_trophies", "Animate Trophies", ConfigComments.ANIMATE_TROPHIES);
		this.configEntry("optifine", "Optifine Screen", ConfigComments.OPTIFINE);
		this.configEntry("locked_toasts", "Disable Locked Biome Toasts", ConfigComments.LOCKED_TOASTS);
		this.configEntry("ram_indicator", "Questing Ram Wool Indicator", ConfigComments.QUESTING_RAM_WOOL);
		this.configEntry("shield_indicator", "Fortification Shield Indicator", ConfigComments.FORTIFICATION);
		this.configEntry("shield_indicator_creative", "Fortification Shield Indicator (creative)", ConfigComments.FORTIFICATION_CREATIVE);
		this.configEntry("giant_skin_uuid_list", "Giant Skins", ConfigComments.GIANT_SKINS);
		this.add("giantSkinUUIDs.button", "Edit Skins");
		this.configEntry("aurora_biomes", "Aurora Shader Biomes", ConfigComments.AURORA_SHADER);
		this.add("auroraBiomes.button", "Edit Biomes");
		this.configEntry("prettify_ore_meter_gui", "Prettify Ore Meter GUI", ConfigComments.PRETTIFY_ORE_METER);
		this.configEntry("totem_charm_animation", "Totem of Undying Charm Animation", ConfigComments.CHARMS_AS_TOTEMS);

		//common config
		this.configCategory("dim_settings", "Dimension Settings", ConfigComments.DIMENSION);
		this.configEntry("spawn_in_tf", "Spawn in Twilight Forest", ConfigComments.SPAWN_IN_TF);
		this.configEntry("portal_for_new_player", "Create Return Portal for Players", ConfigComments.NEW_PORTAL);

		this.configCategory("portal_settings", "Portal Settings", ConfigComments.PORTAL);
		this.configEntry("origin_dimension", "Origin Dimension", ConfigComments.ORIGIN_DIMENSION);
		this.configEntry("portals_in_other_dimensions", "Create Portals in Other Dimensions", ConfigComments.OTHER_DIMENSION_PORTALS);
		this.configEntry("portal_permission", "Portal Creation Permission", ConfigComments.PORTAL_PERMISSION);
		this.configEntry("disable_portal", "Disable Portal Creation", ConfigComments.DISABLE_PORTAL);
		this.configEntry("check_portal_placement", "Check Portal Placement", ConfigComments.CHECK_PORTAL);
		this.configEntry("destructive_portal_lighting", "Destructive Lightning", ConfigComments.PORTAL_LIGHTNING);
		this.configEntry("portal_return", "Lock Return Portal", ConfigComments.RETURN_PORTAL);
		this.configEntry("portal_unlocked_by_advancement", "Portal Unlock Advancement", ConfigComments.PORTAL_ADVANCEMENT);
		this.configEntry("max_portal_size", "Max Portal Size", ConfigComments.PORTAL_SIZE);

		this.configCategory("uncrafting", "Uncrafting Table", ConfigComments.UNCRAFTING_TABLE);
		this.configEntry("uncrafting_xp_cost", "Uncrafting Cost Multiplier", ConfigComments.UNCRAFTING_MULTIPLIER);
		this.configEntry("repairing_xp_cost", "Repairing Cost Multiplier", ConfigComments.REPAIR_MULTIPLIER);
		this.configEntry("uncrafting_recipes", "Disabled Recipes", ConfigComments.RECIPE_WHITELIST);
		this.configEntry("uncrafting_recipes_flip", "Recipe Blacklist", ConfigComments.RECIPE_BLACKLIST);
		this.configEntry("uncrafting_mod_ids", "Disabled Mod IDs", ConfigComments.MOD_ID_WHITELIST);
		this.configEntry("uncrafting_mod_id_flip", "Mod ID Blacklist", ConfigComments.MOD_ID_BLACKLIST);
		this.configEntry("shapeless_uncrafting", "Shapeless Uncrafting", ConfigComments.SHAPELESS_UNCRAFTING);
		this.configEntry("ingredient_switching", "Disable Ingredient Switching", ConfigComments.INGREDIENT_SWITCHING);
		this.configEntry("disable_uncrafting", "Disable Uncrafting", ConfigComments.DISABLE_UNCRAFTING);
		this.configEntry("disable_uncrafting_table", "Disable Uncrafting Table", ConfigComments.DISABLE_TABLE);

		this.configCategory("magic_trees", "Magic Trees", ConfigComments.MAGIC_TREES);
		this.configEntry("time_range", "Tree of Time Range", ConfigComments.TIME_CORE);
		this.configEntry("transformation_range", "Tree of Transformation Range", ConfigComments.TRANSFORMATION_CORE);
		this.configEntry("mining_range", "Miner's Tree Range", ConfigComments.MINING_CORE);
		this.configEntry("sorting_range", "Sorting Tree Range", ConfigComments.SORTING_CORE);

		this.configEntry("shield", "Shield Interactions", ConfigComments.SHIELD_PARRYING);
		this.add("Shield Parrying.button", "Edit");
		this.configEntry("parry_non_twilight", "Parry Non-TF Projectiles", ConfigComments.PARRY_NON_TF);
		this.configEntry("parry_window", "Parry Window", ConfigComments.PARRY_WINDOW);

		this.configEntry("casket_uuid_locking", "Casket UUID Locking", ConfigComments.CASKET_UUID_LOCKING);
		this.configEntry("disable_skull_candles", "Disable Skull Candles", ConfigComments.DISABLE_SKULL_CANDLES);
		this.configEntry("default_item_enchantments", "Default Item Enchantments", ConfigComments.DEFAULT_ENCHANTS);
		this.configEntry("boss_drop_chests", "Boss Drop Chests", ConfigComments.BOSS_CHESTS);
		this.configEntry("cloud_precipitation", "Cloud Precipitation Distance", ConfigComments.CLOUD_PRECIP_SERVER);
		this.configEntry("multiplayer_fight_adjuster", "Multiplayer Fight Adjuster", ConfigComments.MULTIPLAYER_ADJUSTER);
		this.add("config.twilightforest.multiplayer_fight_adjuster.none", "None");
		this.add("config.twilightforest.multiplayer_fight_adjuster.more_loot", "More Loot");
		this.add("config.twilightforest.multiplayer_fight_adjuster.more_health", "More Health");
		this.add("config.twilightforest.multiplayer_fight_adjuster.more_loot_and_health", "More Loot & Health");
	}
}
