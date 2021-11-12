package twilightforest.loot;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.TwilightForestMod;
import twilightforest.loot.conditions.IsMinion;
import twilightforest.loot.conditions.ModExists;
import twilightforest.loot.functions.Enchant;
import twilightforest.loot.functions.ModItemSwap;

import java.util.Set;

public class TFTreasure {
	// For easy testing:
	// /give @p chest{BlockEntityTag:{LootTable:"twilightforest:all_bosses",CustomName:'{"text":"Master Loot Crate"}'}} 1
	private static final Set<ResourceLocation> TF_LOOT_TABLES = Sets.newHashSet();
	
	public static final TFTreasure SMALL_HOLLOW_HILL = new TFTreasure("hill_1");
	public static final TFTreasure MEDIUM_HOLLOW_HILL = new TFTreasure("hill_2");
	public static final TFTreasure LARGE_HOLLOW_HILL = new TFTreasure("hill_3");
	public static final TFTreasure HEDGE_MAZE = new TFTreasure("hedge_maze");
	public static final TFTreasure FANCY_WELL = new TFTreasure("fancy_well");
	public static final TFTreasure WELL = new TFTreasure("well");
	public static final TFTreasure LABYRINTH_ROOM = new TFTreasure("labyrinth_room");
	public static final TFTreasure LABYRINTH_DEAD_END = new TFTreasure("labyrinth_dead_end");
	public static final TFTreasure TOWER_ROOM = new TFTreasure("tower_room");
	public static final TFTreasure TOWER_LIBRARY = new TFTreasure("tower_library");
	public static final TFTreasure BASEMENT = new TFTreasure("basement");
	public static final TFTreasure FOUNDATION_BASEMENT = new TFTreasure("foundation_basement");
	public static final TFTreasure LABYRINTH_VAULT = new TFTreasure("labyrinth_vault");
	public static final TFTreasure DARKTOWER_CACHE = new TFTreasure("darktower_cache");
	public static final TFTreasure DARKTOWER_KEY = new TFTreasure("darktower_key");
	public static final TFTreasure DARKTOWER_BOSS = new TFTreasure("darktower_boss");
	public static final TFTreasure TREE_CACHE = new TFTreasure("tree_cache");
	public static final TFTreasure STRONGHOLD_CACHE = new TFTreasure("stronghold_cache");
	public static final TFTreasure STRONGHOLD_ROOM = new TFTreasure("stronghold_room");
	public static final TFTreasure STRONGHOLD_BOSS = new TFTreasure("stronghold_boss");
	public static final TFTreasure AURORA_CACHE = new TFTreasure("aurora_cache");
	public static final TFTreasure AURORA_ROOM = new TFTreasure("aurora_room");
//	public static final TFTreasure AURORA_BOSS = new TFTreasure("aurora_boss"); //unused
	public static final TFTreasure TROLL_GARDEN = new TFTreasure("troll_garden");
	public static final TFTreasure TROLL_VAULT = new TFTreasure("troll_vault");
	public static final TFTreasure GRAVEYARD = new TFTreasure("graveyard");
	public static final TFTreasure QUEST_GROVE = new TFTreasure("quest_grove_dropper");
	public static final TFTreasure USELESS_LOOT = new TFTreasure("useless");

	public static final ResourceLocation BIGHORN_SHEEP_WHITE = register("entities/bighorn_sheep/white");
	public static final ResourceLocation BIGHORN_SHEEP_ORANGE = register("entities/bighorn_sheep/orange");
	public static final ResourceLocation BIGHORN_SHEEP_MAGENTA = register("entities/bighorn_sheep/magenta");
	public static final ResourceLocation BIGHORN_SHEEP_LIGHT_BLUE = register("entities/bighorn_sheep/light_blue");
	public static final ResourceLocation BIGHORN_SHEEP_YELLOW = register("entities/bighorn_sheep/yellow");
	public static final ResourceLocation BIGHORN_SHEEP_LIME = register("entities/bighorn_sheep/lime");
	public static final ResourceLocation BIGHORN_SHEEP_PINK = register("entities/bighorn_sheep/pink");
	public static final ResourceLocation BIGHORN_SHEEP_GRAY = register("entities/bighorn_sheep/gray");
	public static final ResourceLocation BIGHORN_SHEEP_LIGHT_GRAY = register("entities/bighorn_sheep/light_gray");
	public static final ResourceLocation BIGHORN_SHEEP_CYAN = register("entities/bighorn_sheep/cyan");
	public static final ResourceLocation BIGHORN_SHEEP_PURPLE = register("entities/bighorn_sheep/purple");
	public static final ResourceLocation BIGHORN_SHEEP_BLUE = register("entities/bighorn_sheep/blue");
	public static final ResourceLocation BIGHORN_SHEEP_BROWN = register("entities/bighorn_sheep/brown");
	public static final ResourceLocation BIGHORN_SHEEP_GREEN = register("entities/bighorn_sheep/green");
	public static final ResourceLocation BIGHORN_SHEEP_RED = register("entities/bighorn_sheep/red");
	public static final ResourceLocation BIGHORN_SHEEP_BLACK = register("entities/bighorn_sheep/black");

	public static final ResourceLocation QUESTING_RAM_REWARDS = register("entities/questing_ram_rewards");
	public static final ResourceLocation DEATH_TOME_HURT = register("entities/death_tome_hurt");
	public static final ResourceLocation DEATH_TOME_BOOKS = register("entities/death_tome_books");

	public static final ResourceLocation ALL_BOSSES = register("entities/all_bosses");

	public static LootItemFunctionType ENCHANT;
	public static LootItemFunctionType ITEM_OR_DEFAULT;

	public static LootItemConditionType IS_MINION;
	public static LootItemConditionType MOD_EXISTS;

	public final ResourceLocation lootTable;

	private TFTreasure(String path) {
		lootTable = TwilightForestMod.prefix(String.format("structures/%s", path));
	}

	public static void init() {
		ENCHANT = registerFunction("enchant", new LootItemFunctionType(new Enchant.Serializer()));
		ITEM_OR_DEFAULT = registerFunction("item_or_default", new LootItemFunctionType(new ModItemSwap.Serializer()));

		IS_MINION = registerCondition("is_minion", new LootItemConditionType(new IsMinion.ConditionSerializer()));
		MOD_EXISTS = registerCondition("mod_exists", new LootItemConditionType(new ModExists.ConditionSerializer()));
	}

	public void generateChest(WorldGenLevel world, BlockPos pos, Direction dir, boolean trapped) {
		this.generateLootContainer(world, pos, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.FACING, dir), 2);
	}

	public void generateLootContainer(WorldGenLevel world, BlockPos pos, BlockState state, int flags) {
		world.setBlock(pos, state, flags);

		this.generateChestContents(world, pos);
	}

	public void generateLootContainer(LevelAccessor world, BlockPos pos, BlockState state, int flags, long seed) {
		world.setBlock(pos, state, flags);

		this.generateChestContents(world, pos, seed);
	}

	public void generateChestContents(WorldGenLevel world, BlockPos pos) {
		this.generateChestContents(world, pos, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
	}

	public void generateChestContents(LevelAccessor world, BlockPos pos, long seed) {
		if (world.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity lootContainer)
			lootContainer.setLootTable(lootTable, seed);
	}

	private static LootItemFunctionType registerFunction(String name, LootItemFunctionType function) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, TwilightForestMod.prefix(name), function); //ILootFunction registry
	}

	private static LootItemConditionType registerCondition(String name, LootItemConditionType condition) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, TwilightForestMod.prefix(name), condition); //ILootCondition registry
	}

	private static ResourceLocation register(String id) {
		return register(TwilightForestMod.prefix(id));
	}

	private static ResourceLocation register(ResourceLocation id) {
		if (TF_LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
	}
}
