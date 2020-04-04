package twilightforest.loot;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import twilightforest.TwilightForestMod;
import twilightforest.entity.*;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFIceCrystal;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.entity.boss.EntityTFMinoshroom;
import twilightforest.entity.boss.EntityTFNaga;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFYetiAlpha;
import twilightforest.entity.passive.*;

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

	public static void init() {
		// Preload all entity tables
		//TODO: We just handle loot tables by adding a json file now
//		LootTables.register(EntityTFArmoredGiant.LOOT_TABLE);
//		LootTables.register(EntityTFBird.LOOT_TABLE);
//		LootTables.register(EntityTFBighorn.SHEARED_LOOT_TABLE);
//		EntityTFBighorn.COLORED_LOOT_TABLES.values().forEach(LootTables::register);
//		LootTables.register(EntityTFBlockGoblin.LOOT_TABLE);
//		LootTables.register(EntityTFBoar.LOOT_TABLE);
//		LootTables.register(EntityTFBunny.LOOT_TABLE);
//		LootTables.register(EntityTFDeathTome.LOOT_TABLE);
//		LootTables.register(EntityTFDeathTome.HURT_LOOT_TABLE);
//		LootTables.register(EntityTFDeer.LOOT_TABLE);
//		LootTables.register(EntityTFFireBeetle.LOOT_TABLE);
//		LootTables.register(EntityTFGiantMiner.LOOT_TABLE);
//		LootTables.register(EntityTFGoblinKnightUpper.LOOT_TABLE);
//		LootTables.register(EntityTFHelmetCrab.LOOT_TABLE);
//		LootTables.register(EntityTFHydra.LOOT_TABLE);
//		LootTables.register(EntityTFIceCrystal.LOOT_TABLE);
//		LootTables.register(EntityTFIceExploder.LOOT_TABLE);
//		LootTables.register(EntityTFIceShooter.LOOT_TABLE);
//		LootTables.register(EntityTFKobold.LOOT_TABLE);
//		LootTables.register(EntityTFLich.LOOT_TABLE);
//		LootTables.register(EntityTFMazeSlime.LOOT_TABLE);
//		LootTables.register(EntityTFMiniGhast.LOOT_TABLE);
//		LootTables.register(EntityTFMinoshroom.LOOT_TABLE);
//		LootTables.register(EntityTFMinotaur.LOOT_TABLE);
//		LootTables.register(EntityTFNaga.LOOT_TABLE);
//		LootTables.register(EntityTFPenguin.LOOT_TABLE);
//		LootTables.register(EntityTFQuestRam.LOOT_TABLE);
//		LootTables.register(EntityTFQuestRam.REWARD_LOOT_TABLE);
//		LootTables.register(EntityTFRaven.LOOT_TABLE);
//		LootTables.register(EntityTFRedcap.LOOT_TABLE);
//		LootTables.register(EntityTFSkeletonDruid.LOOT_TABLE);
//		LootTables.register(EntityTFSlimeBeetle.LOOT_TABLE);
//		LootTables.register(EntityTFSnowGuardian.LOOT_TABLE);
//		LootTables.register(EntityTFSnowQueen.LOOT_TABLE);
//		LootTables.register(EntityTFSquirrel.LOOT_TABLE);
//		LootTables.register(EntityTFTinyBird.LOOT_TABLE);
//		LootTables.register(EntityTFTowerGolem.LOOT_TABLE);
//		LootTables.register(EntityTFTowerTermite.LOOT_TABLE);
//		LootTables.register(EntityTFTroll.LOOT_TABLE);
//		LootTables.register(EntityTFWinterWolf.LOOT_TABLE);
//		LootTables.register(EntityTFWraith.LOOT_TABLE);
//		LootTables.register(EntityTFYeti.LOOT_TABLE);
//		LootTables.register(EntityTFYetiAlpha.LOOT_TABLE);

		LootFunctionManager.registerFunction(new LootFunctionEnchant.Serializer());
		LootFunctionManager.registerFunction(new LootFunctionModItemSwap.Serializer());

		LootConditionManager.registerCondition(new LootConditionIsMinion.Serializer());
		LootConditionManager.registerCondition(new LootConditionModExists.Serializer());
	}

	private final ResourceLocation lootTable;

	private TFTreasure(String path) {
		lootTable = TwilightForestMod.prefix(String.format("structures/%s/%s", path, path));

		// only preload the primary table, the subtables will be loaded on-demand the first time the primary table is used
		LootTables.register(lootTable);
	}

	public void generateChest(World world, BlockPos pos, boolean trapped) {
		world.setBlockState(pos, trapped ? Blocks.TRAPPED_CHEST.getDefaultState() : Blocks.CHEST.getDefaultState(), 2);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ChestTileEntity) {
			((ChestTileEntity) te).setLootTable(lootTable, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
		}
	}

	public void generateChestContents(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof ChestTileEntity)
			((ChestTileEntity) te).setLootTable(lootTable, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
	}
}
