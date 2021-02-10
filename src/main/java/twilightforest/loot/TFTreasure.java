package twilightforest.loot;

import net.minecraft.block.Blocks;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import twilightforest.TwilightForestMod;

public class TFTreasure {
	// For easy testing:
	// /give @p chest 1 0 {"display":{"Name":"Master Loot Crate"},"BlockEntityTag":{"LootTable":"twilightforest:entities/all_bosses"}}

	public static final TFTreasure hill1 = new TFTreasure("hill_1");
	public static final TFTreasure hill2 = new TFTreasure("hill_2");
	public static final TFTreasure hill3 = new TFTreasure("hill_3");
	public static final TFTreasure hedgemaze = new TFTreasure("hedge_maze");
	public static final TFTreasure labyrinth_room = new TFTreasure("labyrinth_room");
	public static final TFTreasure labyrinth_deadend = new TFTreasure("labyrinth_dead_end");
	public static final TFTreasure tower_room = new TFTreasure("tower_room");
	public static final TFTreasure tower_library = new TFTreasure("tower_library");
	public static final TFTreasure basement = new TFTreasure("basement");
	public static final TFTreasure labyrinth_vault = new TFTreasure("labyrinth_vault");
	public static final TFTreasure darktower_cache = new TFTreasure("darktower_cache");
	public static final TFTreasure darktower_key = new TFTreasure("darktower_key");
	public static final TFTreasure darktower_boss = new TFTreasure("darktower_boss");
	public static final TFTreasure tree_cache = new TFTreasure("tree_cache");
	public static final TFTreasure stronghold_cache = new TFTreasure("stronghold_cache");
	public static final TFTreasure stronghold_room = new TFTreasure("stronghold_room");
	public static final TFTreasure stronghold_boss = new TFTreasure("stronghold_boss");
	public static final TFTreasure aurora_cache = new TFTreasure("aurora_cache");
	public static final TFTreasure aurora_room = new TFTreasure("aurora_room");
	public static final TFTreasure aurora_boss = new TFTreasure("aurora_boss");
	public static final TFTreasure troll_garden = new TFTreasure("troll_garden");
	public static final TFTreasure troll_vault = new TFTreasure("troll_vault");
	public static final TFTreasure graveyard = new TFTreasure("graveyard");

	public static LootFunctionType ENCHANT;
	public static LootFunctionType ITEM_OR_DEFAULT;

	public static LootConditionType IS_MINION;
	public static LootConditionType MOD_EXISTS;

	private final ResourceLocation lootTable;

	private TFTreasure(String path) {
		lootTable = TwilightForestMod.prefix(String.format("structures/%s/%s", path, path));
	}

	public static void init() {
		ENCHANT = registerFunction("enchant", new LootFunctionType(new LootFunctionEnchant.Serializer()));
		ITEM_OR_DEFAULT = registerFunction("item_or_default", new LootFunctionType(new LootFunctionModItemSwap.Serializer()));

		IS_MINION = registerCondition("is_minion", new LootConditionType(new LootConditionIsMinion.Serializer()));
		MOD_EXISTS = registerCondition("mod_exists", new LootConditionType(new LootConditionModExists.Serializer()));
	}

	public void generateChest(IWorld world, BlockPos pos, boolean trapped) {
		world.setBlockState(pos, trapped ? Blocks.TRAPPED_CHEST.getDefaultState() : Blocks.CHEST.getDefaultState(), 2);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ChestTileEntity) {
			((ChestTileEntity) te).setLootTable(lootTable, ((ISeedReader)world).getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
		}
	}

	public void generateChestContents(ISeedReader world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ChestTileEntity)
			((ChestTileEntity) te).setLootTable(lootTable, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
	}

	private static LootFunctionType registerFunction(String name, LootFunctionType function) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, TwilightForestMod.prefix(name), function); //ILootFunction registry
	}

	private static LootConditionType registerCondition(String name, LootConditionType condition) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, TwilightForestMod.prefix(name), condition); //ILootCondition registry
	}
}
