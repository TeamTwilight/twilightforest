package twilightforest.loot;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import twilightforest.TwilightForestMod;

import java.util.Collections;
import java.util.Set;

//TODO fix other chest loot table directories next breaking version (1.21.4)
public class TFLootTables {
	// For easy testing:
	// /give @p chest{BlockEntityTag:{LootTable:"twilightforest:all_bosses",CustomName:'{"text":"Master Loot Crate"}'}} 1
	private static final Set<ResourceKey<LootTable>> TF_LOOT_TABLES = Sets.newHashSet();
	private static final Set<ResourceKey<LootTable>> TF_IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(TF_LOOT_TABLES);
	public static final int DEFAULT_PLACE_FLAG = Block.UPDATE_CLIENTS;

	// Chest loot
	public static final ResourceKey<LootTable> SUSPICIOUS_STEW = register("chests/suspicious_stew");
	public static final ResourceKey<LootTable> SMALL_HOLLOW_HILL = register("hill_1");
	public static final ResourceKey<LootTable> MEDIUM_HOLLOW_HILL = register("hill_2");
	public static final ResourceKey<LootTable> LARGE_HOLLOW_HILL = register("hill_3");
	public static final ResourceKey<LootTable> HEDGE_MAZE = register("hedge_maze");
	public static final ResourceKey<LootTable> HEDGE_CLOTH = register("hedge_cloth");
	public static final ResourceKey<LootTable> FANCY_WELL = register("fancy_well");
	public static final ResourceKey<LootTable> WELL = register("well");
	public static final ResourceKey<LootTable> LABYRINTH_ROOM = register("labyrinth_room");
	public static final ResourceKey<LootTable> LABYRINTH_DEAD_END = register("labyrinth_dead_end");
	public static final ResourceKey<LootTable> TOWER_ROOM = register("chests/tower_room");
	public static final ResourceKey<LootTable> TOWER_LIBRARY = register("chests/tower_library");
	public static final ResourceKey<LootTable> TOWER_POTION = register("chests/tower_potion");
	public static final ResourceKey<LootTable> TOWER_GRAVE = register("chests/tower_grave");
	public static final ResourceKey<LootTable> TOWER_ENCHANTING = register("chests/tower_enchanting");
	public static final ResourceKey<LootTable> TOWER_JARS = register("chests/tower_jars");
	public static final ResourceKey<LootTable> TOWER_FOYER = register("chests/tower_foyer");
	public static final ResourceKey<LootTable> CASKET_LOOT = register("chests/casket_loot");
	public static final ResourceKey<LootTable> BASEMENT = register("chests/basement");
	public static final ResourceKey<LootTable> HUT_JUNK = register("chests/hut_junk");
	public static final ResourceKey<LootTable> FOUNDATION_BASEMENT = register("foundation_basement");
	public static final ResourceKey<LootTable> LABYRINTH_VAULT = register("labyrinth_vault");
	public static final ResourceKey<LootTable> LABYRINTH_VAULT_JACKPOT = register("labyrinth_vault_jackpot");
	public static final ResourceKey<LootTable> DARKTOWER_CACHE = register("darktower_cache");
	public static final ResourceKey<LootTable> DARKTOWER_KEY = register("darktower_key");
	public static final ResourceKey<LootTable> DARKTOWER_BOSS = register("darktower_boss");
	public static final ResourceKey<LootTable> TREE_CACHE = register("tree_cache");
	public static final ResourceKey<LootTable> STRONGHOLD_CACHE = register("stronghold_cache");
	public static final ResourceKey<LootTable> STRONGHOLD_ROOM = register("stronghold_room");
	public static final ResourceKey<LootTable> AURORA_CACHE = register("aurora_cache");
	public static final ResourceKey<LootTable> AURORA_ROOM = register("aurora_room");
	public static final ResourceKey<LootTable> TROLL_GARDEN = register("troll_garden");
	public static final ResourceKey<LootTable> TROLL_VAULT = register("troll_vault");
	public static final ResourceKey<LootTable> TROLL_VAULT_WITH_LAMP = register("troll_vault_with_lamp");
	public static final ResourceKey<LootTable> GRAVEYARD = register("graveyard");
	public static final ResourceKey<LootTable> QUEST_GROVE = register("quest_grove_dropper");
	public static final ResourceKey<LootTable> USELESS_LOOT = register("useless");

	// Sheep wool drops
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_WHITE = register("entities/bighorn_sheep/white");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_ORANGE = register("entities/bighorn_sheep/orange");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_MAGENTA = register("entities/bighorn_sheep/magenta");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_LIGHT_BLUE = register("entities/bighorn_sheep/light_blue");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_YELLOW = register("entities/bighorn_sheep/yellow");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_LIME = register("entities/bighorn_sheep/lime");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_PINK = register("entities/bighorn_sheep/pink");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_GRAY = register("entities/bighorn_sheep/gray");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_LIGHT_GRAY = register("entities/bighorn_sheep/light_gray");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_CYAN = register("entities/bighorn_sheep/cyan");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_PURPLE = register("entities/bighorn_sheep/purple");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_BLUE = register("entities/bighorn_sheep/blue");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_BROWN = register("entities/bighorn_sheep/brown");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_GREEN = register("entities/bighorn_sheep/green");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_RED = register("entities/bighorn_sheep/red");
	public static final ResourceKey<LootTable> BIGHORN_SHEEP_BLACK = register("entities/bighorn_sheep/black");

	// Special loot
	public static final ResourceKey<LootTable> QUESTING_RAM_REWARDS = register("entities/questing_ram_rewards");
	public static final ResourceKey<LootTable> QUESTING_RAM_REWARD_BLOCKS = register("entities/questing_ram_reward_blocks");
	public static final ResourceKey<LootTable> DEATH_TOME_HURT = register("entities/death_tome_hurt");
	public static final ResourceKey<LootTable> DEATH_TOME_BOOKS = register("entities/death_tome_books");
	public static final ResourceKey<LootTable> LIFEDRAIN_SCEPTER_KILL_BONUS = register("items/lifedrain_scepter_kill_bonus");
	public static final ResourceKey<LootTable> KNIGHT_PHANTOM_DEFEATED = register("entities/knight_phantom_defeated");

	public static final ResourceKey<LootTable> OMINOUS_SPAWNER_DROPS = register("blocks/ominous_spawner_drops");

	// Big bug squish loot
	public static final ResourceKey<LootTable> CICADA_SQUISH_DROPS = register("blocks/cicada_squish");
	public static final ResourceKey<LootTable> FIREFLY_SQUISH_DROPS = register("blocks/firefly_squish");
	public static final ResourceKey<LootTable> MOONWORM_SQUISH_DROPS = register("blocks/moonworm_squish");

	//public static final ResourceLocation ALL_BOSSES = register("entities/all_bosses");

	public static void generateChest(WorldGenLevel world, BlockPos pos, Direction dir, boolean trapped, ResourceKey<LootTable> lootTable) {
		generateLootContainer(world, pos, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.FACING, dir), DEFAULT_PLACE_FLAG, lootTable);
	}

	public static void generateLootContainer(WorldGenLevel world, BlockPos pos, BlockState state, int flags, ResourceKey<LootTable> lootTable) {
		world.setBlock(pos, state, flags);
		generateChestContents(world, pos, lootTable);
	}

	public static void generateLootContainer(LevelAccessor world, BlockPos pos, BlockState state, int flags, long seed, ResourceKey<LootTable> lootTable) {
		world.setBlock(pos, state, flags);
		generateChestContents(world, pos, seed, lootTable);
	}

	public static void generateChestContents(WorldGenLevel level, BlockPos pos, ResourceKey<LootTable> lootTable) {
		generateChestContents(level, pos, level.getSeed() * pos.getX() + pos.getY() ^ pos.getZ(), lootTable);
	}

	public static void generateChestContents(LevelAccessor level, BlockPos pos, long seed, ResourceKey<LootTable> lootTable) {
		if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity lootContainer) lootContainer.setLootTable(lootTable, seed);
	}

	private static ResourceKey<LootTable> register(String id) {
		return register(ResourceKey.create(Registries.LOOT_TABLE, TwilightForestMod.prefix(id)));
	}

	private static ResourceKey<LootTable> register(ResourceKey<LootTable> id) {
		if (TF_LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
	}

	public static LootParams.Builder createLootParams(LivingEntity entity, boolean checkPlayerKill, DamageSource source) {
		LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) entity.level())).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
		if (checkPlayerKill && entity.getKillCredit() instanceof Player player) {
			lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
		}

		return lootcontext$builder;
	}

	public static Set<ResourceKey<LootTable>> allBuiltin() {
		return TF_IMMUTABLE_LOCATIONS;
	}
}
