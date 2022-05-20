package twilightforest.data.tags;

import com.google.common.collect.ImmutableSet;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import java.util.Set;
import java.util.function.Predicate;

import static twilightforest.TFConstants.MOD_ID;

public class BlockTagGenerator extends BlockTagsProvider {
	public static final TagKey<Block> TOWERWOOD = BlockTags.create(TwilightForestMod.prefix("towerwood"));

	public static final TagKey<Block> TWILIGHT_OAK_LOGS = BlockTags.create(TwilightForestMod.prefix("twilight_oak_logs"));
	public static final TagKey<Block> CANOPY_LOGS = BlockTags.create(TwilightForestMod.prefix("canopy_logs"));
	public static final TagKey<Block> MANGROVE_LOGS = BlockTags.create(TwilightForestMod.prefix("mangrove_logs"));
	public static final TagKey<Block> DARKWOOD_LOGS = BlockTags.create(TwilightForestMod.prefix("darkwood_logs"));
	public static final TagKey<Block> TIME_LOGS = BlockTags.create(TwilightForestMod.prefix("timewood_logs"));
	public static final TagKey<Block> TRANSFORMATION_LOGS = BlockTags.create(TwilightForestMod.prefix("transwood_logs"));
	public static final TagKey<Block> MINING_LOGS = BlockTags.create(TwilightForestMod.prefix("mining_logs"));
	public static final TagKey<Block> SORTING_LOGS = BlockTags.create(TwilightForestMod.prefix("sortwood_logs"));

	public static final TagKey<Block> TF_LOGS = BlockTags.create(TwilightForestMod.prefix("logs"));
	public static final TagKey<Block> TF_FENCES = BlockTags.create(TwilightForestMod.prefix("fences"));
	public static final TagKey<Block> TF_FENCE_GATES = BlockTags.create(TwilightForestMod.prefix("fence_gates"));
	public static final TagKey<Block> BANISTERS = BlockTags.create(TwilightForestMod.prefix("banisters"));
	public static final TagKey<Block> HOLLOW_LOGS_HORIZONTAL = BlockTags.create(TwilightForestMod.prefix("hollow_logs_horizontal"));
	public static final TagKey<Block> HOLLOW_LOGS_VERTICAL = BlockTags.create(TwilightForestMod.prefix("hollow_logs_vertical"));
	public static final TagKey<Block> HOLLOW_LOGS_CLIMBABLE = BlockTags.create(TwilightForestMod.prefix("hollow_logs_climbable"));
	public static final TagKey<Block> HOLLOW_LOGS = BlockTags.create(TwilightForestMod.prefix("hollow_logs"));

	public static final TagKey<Block> STORAGE_BLOCKS_ARCTIC_FUR = BlockTags.create(TwilightForestMod.prefix("storage_blocks/arctic_fur"));
	public static final TagKey<Block> STORAGE_BLOCKS_CARMINITE = BlockTags.create(TwilightForestMod.prefix("storage_blocks/carminite"));
	public static final TagKey<Block> STORAGE_BLOCKS_FIERY = BlockTags.create(TwilightForestMod.prefix("storage_blocks/fiery"));
	public static final TagKey<Block> STORAGE_BLOCKS_IRONWOOD = BlockTags.create(TwilightForestMod.prefix("storage_blocks/ironwood"));
	public static final TagKey<Block> STORAGE_BLOCKS_KNIGHTMETAL = BlockTags.create(TwilightForestMod.prefix("storage_blocks/knightmetal"));
	public static final TagKey<Block> STORAGE_BLOCKS_STEELEAF = BlockTags.create(TwilightForestMod.prefix("storage_blocks/steeleaf"));

	public static final TagKey<Block> ORES_IRONWOOD = BlockTags.create(TwilightForestMod.prefix("ores/ironwood"));
	public static final TagKey<Block> ORES_KNIGHTMETAL = BlockTags.create(TwilightForestMod.prefix("ores/knightmetal"));

	public static final TagKey<Block> PORTAL_EDGE = BlockTags.create(TwilightForestMod.prefix("portal/edge"));
	public static final TagKey<Block> PORTAL_POOL = BlockTags.create(TwilightForestMod.prefix("portal/fluid"));
	public static final TagKey<Block> PORTAL_DECO = BlockTags.create(TwilightForestMod.prefix("portal/decoration"));

	public static final TagKey<Block> DARK_TOWER_ALLOWED_POTS = BlockTags.create(TwilightForestMod.prefix("dark_tower_allowed_pots"));
	public static final TagKey<Block> TROPHIES = BlockTags.create(TwilightForestMod.prefix("trophies"));
	public static final TagKey<Block> FIRE_JET_FUEL = BlockTags.create(TwilightForestMod.prefix("fire_jet_fuel"));
	public static final TagKey<Block> ICE_BOMB_REPLACEABLES = BlockTags.create(TwilightForestMod.prefix("ice_bomb_replaceables"));

	public static final TagKey<Block> COMMON_PROTECTIONS = BlockTags.create(TwilightForestMod.prefix("common_protections"));
	public static final TagKey<Block> ANNIHILATION_INCLUSIONS = BlockTags.create(TwilightForestMod.prefix("annihilation_inclusions"));
	public static final TagKey<Block> ANTIBUILDER_IGNORES = BlockTags.create(TwilightForestMod.prefix("antibuilder_ignores"));
	public static final TagKey<Block> CARMINITE_REACTOR_IMMUNE = BlockTags.create(TwilightForestMod.prefix("carminite_reactor_immune"));
	public static final TagKey<Block> CARMINITE_REACTOR_ORES = BlockTags.create(TwilightForestMod.prefix("carminite_reactor_ores"));
	public static final TagKey<Block> STRUCTURE_BANNED_INTERACTIONS = BlockTags.create(TwilightForestMod.prefix("structure_banned_interactions"));

	public static final TagKey<Block> WORLDGEN_REPLACEABLES = BlockTags.create(TwilightForestMod.prefix("worldgen_replaceables"));

	public static final TagKey<Block> ORE_MAGNET_SAFE_REPLACE_BLOCK = BlockTags.create(TwilightForestMod.prefix("ore_magnet/ore_safe_replace_block"));
	public static final TagKey<Block> ORE_MAGNET_IGNORE = BlockTags.create(TwilightForestMod.prefix("ore_magnet/ignored_ores"));
	public static final TagKey<Block> ROOT_GROUND = BlockTags.create(new ResourceLocation("forge", "ore_bearing_ground/root"));
	public static final TagKey<Block> ROOT_ORES = BlockTags.create(new ResourceLocation("forge", "ores_in_ground/root"));

	public BlockTagGenerator(DataGenerator generator, ExistingFileHelper exFileHelper) {
		super(generator, MOD_ID, exFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {
		tag(TWILIGHT_OAK_LOGS)
				.add(TFBlocks.TWILIGHT_OAK_LOG.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_LOG.get(), TFBlocks.TWILIGHT_OAK_WOOD.get(), TFBlocks.STRIPPED_TWILIGHT_OAK_WOOD.get());
		tag(CANOPY_LOGS)
				.add(TFBlocks.CANOPY_LOG.get(), TFBlocks.STRIPPED_CANOPY_LOG.get(), TFBlocks.CANOPY_WOOD.get(), TFBlocks.STRIPPED_CANOPY_WOOD.get());
		tag(MANGROVE_LOGS)
				.add(TFBlocks.MANGROVE_LOG.get(), TFBlocks.STRIPPED_MANGROVE_LOG.get(), TFBlocks.MANGROVE_WOOD.get(), TFBlocks.STRIPPED_MANGROVE_WOOD.get());
		tag(DARKWOOD_LOGS)
				.add(TFBlocks.DARK_LOG.get(), TFBlocks.STRIPPED_DARK_LOG.get(), TFBlocks.DARK_WOOD.get(), TFBlocks.STRIPPED_DARK_WOOD.get());
		tag(TIME_LOGS)
				.add(TFBlocks.TIME_LOG.get(), TFBlocks.STRIPPED_TIME_LOG.get(), TFBlocks.TIME_WOOD.get(), TFBlocks.STRIPPED_TIME_WOOD.get());
		tag(TRANSFORMATION_LOGS)
				.add(TFBlocks.TRANSFORMATION_LOG.get(), TFBlocks.STRIPPED_TRANSFORMATION_LOG.get(), TFBlocks.TRANSFORMATION_WOOD.get(), TFBlocks.STRIPPED_TRANSFORMATION_WOOD.get());
		tag(MINING_LOGS)
				.add(TFBlocks.MINING_LOG.get(), TFBlocks.STRIPPED_MINING_LOG.get(), TFBlocks.MINING_WOOD.get(), TFBlocks.STRIPPED_MINING_WOOD.get());
		tag(SORTING_LOGS)
				.add(TFBlocks.SORTING_LOG.get(), TFBlocks.STRIPPED_SORTING_LOG.get(), TFBlocks.SORTING_WOOD.get(), TFBlocks.STRIPPED_SORTING_WOOD.get());

		tag(TF_LOGS)
				.addTags(TWILIGHT_OAK_LOGS, CANOPY_LOGS, MANGROVE_LOGS, DARKWOOD_LOGS, TIME_LOGS, TRANSFORMATION_LOGS, MINING_LOGS, SORTING_LOGS);
		tag(BlockTags.LOGS)
				.addTag(TF_LOGS);

		tag(BlockTags.LOGS_THAT_BURN)
				.addTags(TWILIGHT_OAK_LOGS, CANOPY_LOGS, MANGROVE_LOGS, TIME_LOGS, TRANSFORMATION_LOGS, MINING_LOGS, SORTING_LOGS);

		tag(BlockTags.SAPLINGS)
				.add(TFBlocks.TWILIGHT_OAK_SAPLING.get(), TFBlocks.CANOPY_SAPLING.get(), TFBlocks.MANGROVE_SAPLING.get(), TFBlocks.DARKWOOD_SAPLING.get())
				.add(TFBlocks.TIME_SAPLING.get(), TFBlocks.TRANSFORMATION_SAPLING.get(), TFBlocks.MINING_SAPLING.get(), TFBlocks.SORTING_SAPLING.get())
				.add(TFBlocks.HOLLOW_OAK_SAPLING.get(), TFBlocks.RAINBOW_OAK_SAPLING.get());
		tag(BlockTags.LEAVES)
				.add(TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.DARK_LEAVES.get())
				.add(TFBlocks.TIME_LEAVES.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.SORTING_LEAVES.get())
				.add(TFBlocks.THORN_LEAVES.get(), TFBlocks.BEANSTALK_LEAVES.get());

		tag(BlockTags.PLANKS)
				.add(TFBlocks.TWILIGHT_OAK_PLANKS.get(), TFBlocks.CANOPY_PLANKS.get(), TFBlocks.MANGROVE_PLANKS.get(), TFBlocks.DARK_PLANKS.get())
				.add(TFBlocks.TIME_PLANKS.get(), TFBlocks.TRANSFORMATION_PLANKS.get(), TFBlocks.MINING_PLANKS.get(), TFBlocks.SORTING_PLANKS.get())
				.add(TFBlocks.TOWERWOOD.get(), TFBlocks.ENCASED_TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get());

		tag(TF_FENCES)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.DARK_FENCE.get())
				.add(TFBlocks.TIME_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.SORTING_FENCE.get());
		tag(TF_FENCE_GATES)
				.add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get())
				.add(TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		tag(BlockTags.WOODEN_FENCES)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.DARK_FENCE.get())
				.add(TFBlocks.TIME_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.SORTING_FENCE.get());
		tag(BlockTags.FENCE_GATES)
				.add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get())
				.add(TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		tag(Tags.Blocks.FENCES)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.DARK_FENCE.get())
				.add(TFBlocks.TIME_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.SORTING_FENCE.get());
		tag(Tags.Blocks.FENCE_GATES)
				.add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get())
				.add(TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());
		tag(Tags.Blocks.FENCES_WOODEN)
				.add(TFBlocks.TWILIGHT_OAK_FENCE.get(), TFBlocks.CANOPY_FENCE.get(), TFBlocks.MANGROVE_FENCE.get(), TFBlocks.DARK_FENCE.get())
				.add(TFBlocks.TIME_FENCE.get(), TFBlocks.TRANSFORMATION_FENCE.get(), TFBlocks.MINING_FENCE.get(), TFBlocks.SORTING_FENCE.get());
		tag(Tags.Blocks.FENCE_GATES_WOODEN)
				.add(TFBlocks.TWILIGHT_OAK_GATE.get(), TFBlocks.CANOPY_GATE.get(), TFBlocks.MANGROVE_GATE.get(), TFBlocks.DARK_GATE.get())
				.add(TFBlocks.TIME_GATE.get(), TFBlocks.TRANSFORMATION_GATE.get(), TFBlocks.MINING_GATE.get(), TFBlocks.SORTING_GATE.get());

		tag(BlockTags.WOODEN_SLABS)
				.add(TFBlocks.TWILIGHT_OAK_SLAB.get(), TFBlocks.CANOPY_SLAB.get(), TFBlocks.MANGROVE_SLAB.get(), TFBlocks.DARK_SLAB.get())
				.add(TFBlocks.TIME_SLAB.get(), TFBlocks.TRANSFORMATION_SLAB.get(), TFBlocks.MINING_SLAB.get(), TFBlocks.SORTING_SLAB.get());
		tag(BlockTags.SLABS)
				.add(TFBlocks.AURORA_SLAB.get());
		tag(BlockTags.WOODEN_STAIRS)
				.add(TFBlocks.TWILIGHT_OAK_STAIRS.get(), TFBlocks.CANOPY_STAIRS.get(), TFBlocks.MANGROVE_STAIRS.get(), TFBlocks.DARK_STAIRS.get())
				.add(TFBlocks.TIME_STAIRS.get(), TFBlocks.TRANSFORMATION_STAIRS.get(), TFBlocks.MINING_STAIRS.get(), TFBlocks.SORTING_STAIRS.get());
		tag(BlockTags.STAIRS)
				.add(TFBlocks.CASTLE_BRICK_STAIRS.get(), TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(), TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(), TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(), TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get())
				.add(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get());

		tag(BlockTags.WOODEN_BUTTONS)
				.add(TFBlocks.TWILIGHT_OAK_BUTTON.get(), TFBlocks.CANOPY_BUTTON.get(), TFBlocks.MANGROVE_BUTTON.get(), TFBlocks.DARK_BUTTON.get())
				.add(TFBlocks.TIME_BUTTON.get(), TFBlocks.TRANSFORMATION_BUTTON.get(), TFBlocks.MINING_BUTTON.get(), TFBlocks.SORTING_BUTTON.get());
		tag(BlockTags.WOODEN_PRESSURE_PLATES)
				.add(TFBlocks.TWILIGHT_OAK_PLATE.get(), TFBlocks.CANOPY_PLATE.get(), TFBlocks.MANGROVE_PLATE.get(), TFBlocks.DARK_PLATE.get())
				.add(TFBlocks.TIME_PLATE.get(), TFBlocks.TRANSFORMATION_PLATE.get(), TFBlocks.MINING_PLATE.get(), TFBlocks.SORTING_PLATE.get());

		tag(BlockTags.WOODEN_TRAPDOORS)
				.add(TFBlocks.TWILIGHT_OAK_TRAPDOOR.get(), TFBlocks.CANOPY_TRAPDOOR.get(), TFBlocks.MANGROVE_TRAPDOOR.get(), TFBlocks.DARK_TRAPDOOR.get())
				.add(TFBlocks.TIME_TRAPDOOR.get(), TFBlocks.TRANSFORMATION_TRAPDOOR.get(), TFBlocks.MINING_TRAPDOOR.get(), TFBlocks.SORTING_TRAPDOOR.get());
		tag(BlockTags.WOODEN_DOORS)
				.add(TFBlocks.TWILIGHT_OAK_DOOR.get(), TFBlocks.CANOPY_DOOR.get(), TFBlocks.MANGROVE_DOOR.get(), TFBlocks.DARK_DOOR.get())
				.add(TFBlocks.TIME_DOOR.get(), TFBlocks.TRANSFORMATION_DOOR.get(), TFBlocks.MINING_DOOR.get(), TFBlocks.SORTING_DOOR.get());

		tag(Tags.Blocks.CHESTS_WOODEN)
				.add(TFBlocks.TWILIGHT_OAK_CHEST.get(), TFBlocks.CANOPY_CHEST.get(), TFBlocks.MANGROVE_CHEST.get(), TFBlocks.DARKWOOD_CHEST.get())
				.add(TFBlocks.TIME_CHEST.get(), TFBlocks.TRANSFORMATION_CHEST.get(), TFBlocks.MINING_CHEST.get(), TFBlocks.SORTING_CHEST.get());

		tag(BlockTags.FLOWER_POTS)
				.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(), TFBlocks.POTTED_DARKWOOD_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get())
				.add(TFBlocks.POTTED_HOLLOW_OAK_SAPLING.get(), TFBlocks.POTTED_TIME_SAPLING.get(), TFBlocks.POTTED_TRANSFORMATION_SAPLING.get(), TFBlocks.POTTED_MINING_SAPLING.get(), TFBlocks.POTTED_SORTING_SAPLING.get())
				.add(TFBlocks.POTTED_MAYAPPLE.get(), TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.POTTED_MUSHGLOOM.get(), TFBlocks.POTTED_THORN.get(), TFBlocks.POTTED_GREEN_THORN.get(), TFBlocks.POTTED_DEAD_THORN.get());

		tag(BANISTERS).add(
				TFBlocks.OAK_BANISTER.get(),
				TFBlocks.SPRUCE_BANISTER.get(),
				TFBlocks.BIRCH_BANISTER.get(),
				TFBlocks.JUNGLE_BANISTER.get(),
				TFBlocks.ACACIA_BANISTER.get(),
				TFBlocks.DARK_OAK_BANISTER.get(),
				TFBlocks.CRIMSON_BANISTER.get(),
				TFBlocks.WARPED_BANISTER.get(),

				TFBlocks.TWILIGHT_OAK_BANISTER.get(),
				TFBlocks.CANOPY_BANISTER.get(),
				TFBlocks.MANGROVE_BANISTER.get(),
				TFBlocks.DARKWOOD_BANISTER.get(),
				TFBlocks.TIME_BANISTER.get(),
				TFBlocks.TRANSFORMATION_BANISTER.get(),
				TFBlocks.MINING_BANISTER.get(),
				TFBlocks.SORTING_BANISTER.get()
		);

		tag(HOLLOW_LOGS_HORIZONTAL).add(
				TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(),
				TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get()
		);

		tag(HOLLOW_LOGS_VERTICAL).add(
				TFBlocks.HOLLOW_OAK_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_SPRUCE_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_BIRCH_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_JUNGLE_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_DARK_OAK_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_CRIMSON_STEM_VERTICAL.get(),
				TFBlocks.HOLLOW_WARPED_STEM_VERTICAL.get(),

				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_CANOPY_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_MANGROVE_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_DARK_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_TIME_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_TRANSFORMATION_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_MINING_LOG_VERTICAL.get(),
				TFBlocks.HOLLOW_SORTING_LOG_VERTICAL.get()
		);

		tag(HOLLOW_LOGS_CLIMBABLE).add(
				TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(),
				TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(),

				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get()
		);

		tag(HOLLOW_LOGS).addTags(HOLLOW_LOGS_HORIZONTAL, HOLLOW_LOGS_VERTICAL, HOLLOW_LOGS_CLIMBABLE);

		tag(BlockTags.STRIDER_WARM_BLOCKS).add(TFBlocks.FIERY_BLOCK.get());
		tag(BlockTags.PORTALS).add(TFBlocks.TWILIGHT_PORTAL.get());

		tag(BlockTags.CLIMBABLE).add(TFBlocks.IRON_LADDER.get(), TFBlocks.ROOT_STRAND.get()).addTag(HOLLOW_LOGS_CLIMBABLE);

		tag(BlockTags.STANDING_SIGNS).add(TFBlocks.TWILIGHT_OAK_SIGN.get(), TFBlocks.CANOPY_SIGN.get(),
				TFBlocks.MANGROVE_SIGN.get(), TFBlocks.DARKWOOD_SIGN.get(),
				TFBlocks.TIME_SIGN.get(), TFBlocks.TRANSFORMATION_SIGN.get(),
				TFBlocks.MINING_SIGN.get(), TFBlocks.SORTING_SIGN.get());

		tag(BlockTags.WALL_SIGNS).add(TFBlocks.TWILIGHT_WALL_SIGN.get(), TFBlocks.CANOPY_WALL_SIGN.get(),
				TFBlocks.MANGROVE_WALL_SIGN.get(), TFBlocks.DARKWOOD_WALL_SIGN.get(),
				TFBlocks.TIME_WALL_SIGN.get(), TFBlocks.TRANSFORMATION_WALL_SIGN.get(),
				TFBlocks.MINING_WALL_SIGN.get(), TFBlocks.SORTING_WALL_SIGN.get());

		tag(TOWERWOOD).add(TFBlocks.TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get());

		tag(STORAGE_BLOCKS_ARCTIC_FUR).add(TFBlocks.ARCTIC_FUR_BLOCK.get());
		tag(STORAGE_BLOCKS_CARMINITE).add(TFBlocks.CARMINITE_BLOCK.get());
		tag(STORAGE_BLOCKS_FIERY).add(TFBlocks.FIERY_BLOCK.get());
		tag(STORAGE_BLOCKS_IRONWOOD).add(TFBlocks.IRONWOOD_BLOCK.get());
		tag(STORAGE_BLOCKS_KNIGHTMETAL).add(TFBlocks.KNIGHTMETAL_BLOCK.get());
		tag(STORAGE_BLOCKS_STEELEAF).add(TFBlocks.STEELEAF_BLOCK.get());

		tag(BlockTags.BEACON_BASE_BLOCKS).addTags(
				STORAGE_BLOCKS_ARCTIC_FUR,
				STORAGE_BLOCKS_CARMINITE,
				STORAGE_BLOCKS_FIERY,
				STORAGE_BLOCKS_IRONWOOD,
				STORAGE_BLOCKS_KNIGHTMETAL,
				STORAGE_BLOCKS_STEELEAF
		);

		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(STORAGE_BLOCKS_ARCTIC_FUR, STORAGE_BLOCKS_CARMINITE, STORAGE_BLOCKS_FIERY, STORAGE_BLOCKS_IRONWOOD, STORAGE_BLOCKS_KNIGHTMETAL, STORAGE_BLOCKS_STEELEAF);

		tag(Tags.Blocks.ORES).addTags(ORES_IRONWOOD, ORES_KNIGHTMETAL);
		tag(ORES_IRONWOOD); // Intentionally blank
		tag(ORES_KNIGHTMETAL); // Intentionally blank

		tag(BlockTags.DIRT).add(TFBlocks.UBEROUS_SOIL.get());
		tag(PORTAL_EDGE).addTags(BlockTags.DIRT);
		// So yes, we could do fluid tags for the portal pool but the problem is that we're -replacing- the block, effectively replacing what would be waterlogged, with the portal block
		// In the future if we can "portal log" blocks then we can re-explore doing it as a fluid
		tag(PORTAL_POOL).add(Blocks.WATER);
		tag(PORTAL_DECO)
				.addTags(BlockTags.FLOWERS, BlockTags.LEAVES, BlockTags.SAPLINGS, BlockTags.CROPS)
				.add(Blocks.BAMBOO)
				.add(getAllMinecraftOrTwilightBlocks(b -> (b.material == Material.PLANT || b.material == Material.REPLACEABLE_PLANT || b.material == Material.LEAVES) && !plants.contains(b)));

		tag(DARK_TOWER_ALLOWED_POTS)
				.add(TFBlocks.POTTED_TWILIGHT_OAK_SAPLING.get(), TFBlocks.POTTED_CANOPY_SAPLING.get(), TFBlocks.POTTED_MANGROVE_SAPLING.get(),
						TFBlocks.POTTED_DARKWOOD_SAPLING.get(), TFBlocks.POTTED_RAINBOW_OAK_SAPLING.get(), TFBlocks.POTTED_MAYAPPLE.get(),
						TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.POTTED_MUSHGLOOM.get())
				.add(Blocks.FLOWER_POT, Blocks.POTTED_POPPY, Blocks.POTTED_BLUE_ORCHID, Blocks.POTTED_ALLIUM, Blocks.POTTED_AZURE_BLUET,
						Blocks.POTTED_RED_TULIP, Blocks.POTTED_ORANGE_TULIP, Blocks.POTTED_WHITE_TULIP, Blocks.POTTED_PINK_TULIP,
						Blocks.POTTED_OXEYE_DAISY, Blocks.POTTED_DANDELION, Blocks.POTTED_OAK_SAPLING, Blocks.POTTED_SPRUCE_SAPLING,
						Blocks.POTTED_BIRCH_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, Blocks.POTTED_ACACIA_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING,
						Blocks.POTTED_RED_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, Blocks.POTTED_DEAD_BUSH, Blocks.POTTED_FERN,
						Blocks.POTTED_CACTUS, Blocks.POTTED_CORNFLOWER, Blocks.POTTED_LILY_OF_THE_VALLEY, Blocks.POTTED_WITHER_ROSE,
						Blocks.POTTED_BAMBOO, Blocks.POTTED_CRIMSON_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, Blocks.POTTED_CRIMSON_ROOTS,
						Blocks.POTTED_WARPED_ROOTS, Blocks.POTTED_AZALEA, Blocks.POTTED_FLOWERING_AZALEA);

		tag(TROPHIES)
				.add(TFBlocks.NAGA_TROPHY.get(), TFBlocks.NAGA_WALL_TROPHY.get())
				.add(TFBlocks.LICH_TROPHY.get(), TFBlocks.LICH_WALL_TROPHY.get())
				.add(TFBlocks.MINOSHROOM_TROPHY.get(), TFBlocks.MINOSHROOM_WALL_TROPHY.get())
				.add(TFBlocks.HYDRA_TROPHY.get(), TFBlocks.HYDRA_WALL_TROPHY.get())
				.add(TFBlocks.KNIGHT_PHANTOM_TROPHY.get(), TFBlocks.KNIGHT_PHANTOM_WALL_TROPHY.get())
				.add(TFBlocks.UR_GHAST_TROPHY.get(), TFBlocks.UR_GHAST_WALL_TROPHY.get())
				.add(TFBlocks.ALPHA_YETI_TROPHY.get(), TFBlocks.ALPHA_YETI_WALL_TROPHY.get())
				.add(TFBlocks.SNOW_QUEEN_TROPHY.get(), TFBlocks.SNOW_QUEEN_WALL_TROPHY.get())
				.add(TFBlocks.QUEST_RAM_TROPHY.get(), TFBlocks.QUEST_RAM_WALL_TROPHY.get());

		tag(FIRE_JET_FUEL).add(Blocks.LAVA);

		tag(ICE_BOMB_REPLACEABLES)
				.add(TFBlocks.MAYAPPLE.get(), TFBlocks.FIDDLEHEAD.get(), Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN)
				.addTag(BlockTags.FLOWERS);

		tag(COMMON_PROTECTIONS).add( // For any blocks that absolutely should not be meddled with
				TFBlocks.NAGA_BOSS_SPAWNER.get(),
				TFBlocks.LICH_BOSS_SPAWNER.get(),
				TFBlocks.MINOSHROOM_BOSS_SPAWNER.get(),
				TFBlocks.HYDRA_BOSS_SPAWNER.get(),
				TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER.get(),
				TFBlocks.UR_GHAST_BOSS_SPAWNER.get(),
				TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get(),
				TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get(),
				TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get(),
				TFBlocks.STRONGHOLD_SHIELD.get(),
				TFBlocks.VANISHING_BLOCK.get(),
				TFBlocks.LOCKED_VANISHING_BLOCK.get(),
				TFBlocks.PINK_FORCE_FIELD.get(),
				TFBlocks.ORANGE_FORCE_FIELD.get(),
				TFBlocks.GREEN_FORCE_FIELD.get(),
				TFBlocks.BLUE_FORCE_FIELD.get(),
				TFBlocks.VIOLET_FORCE_FIELD.get(),
				TFBlocks.KEEPSAKE_CASKET.get(),
				TFBlocks.TROPHY_PEDESTAL.get()
		).add( // [VanillaCopy] WITHER_IMMUNE - Do NOT include that tag in this tag
				Blocks.BARRIER,
				Blocks.BEDROCK,
				Blocks.END_PORTAL,
				Blocks.END_PORTAL_FRAME,
				Blocks.END_GATEWAY,
				Blocks.COMMAND_BLOCK,
				Blocks.REPEATING_COMMAND_BLOCK,
				Blocks.CHAIN_COMMAND_BLOCK,
				Blocks.STRUCTURE_BLOCK,
				Blocks.JIGSAW,
				Blocks.MOVING_PISTON
		);

		// TODO Test behavior with giant blocks for immunity stuffs
		tag(BlockTags.DRAGON_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.GIANT_OBSIDIAN.get(), TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get());

		tag(BlockTags.WITHER_IMMUNE).addTag(COMMON_PROTECTIONS).add(TFBlocks.FAKE_DIAMOND.get(), TFBlocks.FAKE_GOLD.get());

		tag(CARMINITE_REACTOR_IMMUNE).addTag(COMMON_PROTECTIONS);

		tag(CARMINITE_REACTOR_ORES).add(Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE);

		tag(ANNIHILATION_INCLUSIONS) // This is NOT a blacklist! This is a whitelist
				.add(Blocks.NETHER_PORTAL)
				.add(TFBlocks.DEADROCK.get(), TFBlocks.CRACKED_DEADROCK.get(), TFBlocks.WEATHERED_DEADROCK.get())
				.add(TFBlocks.CASTLE_BRICK.get(), TFBlocks.CRACKED_DEADROCK.get(), TFBlocks.THICK_CASTLE_BRICK.get(), TFBlocks.MOSSY_CASTLE_BRICK.get(), TFBlocks.CASTLE_ROOF_TILE.get(), TFBlocks.WORN_CASTLE_BRICK.get())
				.add(TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.PINK_CASTLE_RUNE_BRICK.get())
				.add(TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(), TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(), TFBlocks.VIOLET_FORCE_FIELD.get())
				.add(TFBlocks.BROWN_THORNS.get(), TFBlocks.GREEN_THORNS.get());

		tag(ANTIBUILDER_IGNORES).addTag(COMMON_PROTECTIONS).addOptional(new ResourceLocation("gravestone:gravestone")).add(
				Blocks.REDSTONE_LAMP,
				Blocks.TNT,
				Blocks.WATER,
				TFBlocks.ANTIBUILDER.get(),
				TFBlocks.CARMINITE_BUILDER.get(),
				TFBlocks.BUILT_BLOCK.get(),
				TFBlocks.REACTOR_DEBRIS.get(),
				TFBlocks.CARMINITE_REACTOR.get(),
				TFBlocks.REAPPEARING_BLOCK.get(),
				TFBlocks.GHAST_TRAP.get(),
				TFBlocks.FAKE_DIAMOND.get(),
				TFBlocks.FAKE_GOLD.get()
		);

		tag(STRUCTURE_BANNED_INTERACTIONS)
				.addTags(BlockTags.BUTTONS, Tags.Blocks.CHESTS).add(Blocks.LEVER)
				.add(TFBlocks.ANTIBUILDER.get());

		tag(ORE_MAGNET_SAFE_REPLACE_BLOCK).addTags(
				BlockTags.DIRT,
				Tags.Blocks.GRAVEL,
				Tags.Blocks.SAND,
				BlockTags.NYLIUM,
				BlockTags.BASE_STONE_OVERWORLD,
				BlockTags.BASE_STONE_NETHER,
				Tags.Blocks.END_STONES,
				BlockTags.DEEPSLATE_ORE_REPLACEABLES,
				BlockTags.STONE_ORE_REPLACEABLES,
				ROOT_GROUND
		);

		tag(ORE_MAGNET_IGNORE).addTag(BlockTags.COAL_ORES);

		tag(ROOT_GROUND).add(TFBlocks.ROOT_BLOCK.get());
		tag(ROOT_ORES).add(TFBlocks.LIVEROOT_BLOCK.get());

		tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).add(TFBlocks.ARCTIC_FUR_BLOCK.get());

		tag(BlockTags.SMALL_DRIPLEAF_PLACEABLE).add(TFBlocks.UBEROUS_SOIL.get());

		tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(COMMON_PROTECTIONS).add(TFBlocks.LIVEROOT_BLOCK.get(), TFBlocks.MANGROVE_ROOT.get());
		// For anything that permits replacement during Worldgen
		tag(WORLDGEN_REPLACEABLES).addTags(BlockTags.LUSH_GROUND_REPLACEABLE, BlockTags.REPLACEABLE_PLANTS);

		tag(BlockTags.REPLACEABLE_PLANTS).add(
				TFBlocks.MAYAPPLE.get(),
				TFBlocks.FIDDLEHEAD.get(),
				TFBlocks.MUSHGLOOM.get(),
				TFBlocks.TORCHBERRY_PLANT.get(),
				TFBlocks.ROOT_STRAND.get(),
				TFBlocks.MOSS_PATCH.get(),
				TFBlocks.CLOVER_PATCH.get(),
				TFBlocks.FALLEN_LEAVES.get());

		tag(BlockTags.MINEABLE_WITH_AXE).addTags(BANISTERS, HOLLOW_LOGS).add(
				TFBlocks.HEDGE.get(),
				TFBlocks.ROOT_BLOCK.get(),
				TFBlocks.LIVEROOT_BLOCK.get(),
				TFBlocks.MANGROVE_ROOT.get(),
				TFBlocks.UNCRAFTING_TABLE.get(),
				TFBlocks.ENCASED_SMOKER.get(),
				TFBlocks.ENCASED_FIRE_JET.get(),
				TFBlocks.TIME_LOG_CORE.get(),
				TFBlocks.TRANSFORMATION_LOG_CORE.get(),
				TFBlocks.MINING_LOG_CORE.get(),
				TFBlocks.SORTING_LOG_CORE.get(),
				TFBlocks.TOWERWOOD.get(),
				TFBlocks.MOSSY_TOWERWOOD.get(),
				TFBlocks.CRACKED_TOWERWOOD.get(),
				TFBlocks.INFESTED_TOWERWOOD.get(),
				TFBlocks.ENCASED_TOWERWOOD.get(),
				TFBlocks.REAPPEARING_BLOCK.get(),
				TFBlocks.ANTIBUILDER.get(),
				TFBlocks.CARMINITE_REACTOR.get(),
				TFBlocks.CARMINITE_BUILDER.get(),
				TFBlocks.GHAST_TRAP.get(),
				TFBlocks.HUGE_STALK.get(),
				TFBlocks.HUGE_MUSHGLOOM.get(),
				TFBlocks.HUGE_MUSHGLOOM_STEM.get(),
				TFBlocks.CINDER_LOG.get(),
				TFBlocks.CINDER_WOOD.get(),
				TFBlocks.IRONWOOD_BLOCK.get(),
				TFBlocks.DEATH_TOME_SPAWNER.get(),
				TFBlocks.EMPTY_CANOPY_BOOKSHELF.get(),
				TFBlocks.CANOPY_BOOKSHELF.get(),
				TFBlocks.TWILIGHT_OAK_CHEST.get(),
				TFBlocks.CANOPY_CHEST.get(),
				TFBlocks.MANGROVE_CHEST.get(),
				TFBlocks.DARKWOOD_CHEST.get(),
				TFBlocks.TIME_CHEST.get(),
				TFBlocks.TRANSFORMATION_CHEST.get(),
				TFBlocks.MINING_CHEST.get(),
				TFBlocks.SORTING_CHEST.get()
		);

		tag(BlockTags.MINEABLE_WITH_HOE).add(
				//vanilla doesnt use the leaves tag
				TFBlocks.TWILIGHT_OAK_LEAVES.get(),
				TFBlocks.CANOPY_LEAVES.get(),
				TFBlocks.MANGROVE_LEAVES.get(),
				TFBlocks.DARK_LEAVES.get(),
				TFBlocks.RAINBOW_OAK_LEAVES.get(),
				TFBlocks.TIME_LEAVES.get(),
				TFBlocks.TRANSFORMATION_LEAVES.get(),
				TFBlocks.MINING_LEAVES.get(),
				TFBlocks.SORTING_LEAVES.get(),
				TFBlocks.THORN_LEAVES.get(),
				TFBlocks.THORN_ROSE.get(),
				TFBlocks.BEANSTALK_LEAVES.get(),
				TFBlocks.STEELEAF_BLOCK.get(),
				TFBlocks.ARCTIC_FUR_BLOCK.get()
		);

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				TFBlocks.MAZESTONE.get(),
				TFBlocks.MAZESTONE_BORDER.get(),
				TFBlocks.MAZESTONE_BRICK.get(),
				TFBlocks.CUT_MAZESTONE.get(),
				TFBlocks.CRACKED_MAZESTONE.get(),
				TFBlocks.DECORATIVE_MAZESTONE.get(),
				TFBlocks.MAZESTONE_MOSAIC.get(),
				TFBlocks.MOSSY_MAZESTONE.get(),
				TFBlocks.NAGASTONE.get(),
				TFBlocks.NAGASTONE_HEAD.get(),
				TFBlocks.STRONGHOLD_SHIELD.get(),
				TFBlocks.TROPHY_PEDESTAL.get(),
				TFBlocks.AURORA_PILLAR.get(),
				TFBlocks.AURORA_SLAB.get(),
				TFBlocks.UNDERBRICK.get(),
				TFBlocks.MOSSY_UNDERBRICK.get(),
				TFBlocks.CRACKED_UNDERBRICK.get(),
				TFBlocks.UNDERBRICK_FLOOR.get(),
				TFBlocks.DEADROCK.get(),
				TFBlocks.CRACKED_DEADROCK.get(),
				TFBlocks.WEATHERED_DEADROCK.get(),
				TFBlocks.TROLLSTEINN.get(),
				TFBlocks.GIANT_LEAVES.get(),
				TFBlocks.GIANT_OBSIDIAN.get(),
				TFBlocks.GIANT_COBBLESTONE.get(),
				TFBlocks.GIANT_LOG.get(),
				TFBlocks.CASTLE_BRICK.get(),
				TFBlocks.WORN_CASTLE_BRICK.get(),
				TFBlocks.CRACKED_CASTLE_BRICK.get(),
				TFBlocks.CASTLE_ROOF_TILE.get(),
				TFBlocks.MOSSY_CASTLE_BRICK.get(),
				TFBlocks.THICK_CASTLE_BRICK.get(),
				TFBlocks.ENCASED_CASTLE_BRICK_PILLAR.get(),
				TFBlocks.ENCASED_CASTLE_BRICK_TILE.get(),
				TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get(),
				TFBlocks.BOLD_CASTLE_BRICK_TILE.get(),
				TFBlocks.CASTLE_BRICK_STAIRS.get(),
				TFBlocks.WORN_CASTLE_BRICK_STAIRS.get(),
				TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(),
				TFBlocks.MOSSY_CASTLE_BRICK_STAIRS.get(),
				TFBlocks.ENCASED_CASTLE_BRICK_STAIRS.get(),
				TFBlocks.BOLD_CASTLE_BRICK_STAIRS.get(),
				TFBlocks.PINK_CASTLE_RUNE_BRICK.get(),
				TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(),
				TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(),
				TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
				TFBlocks.PINK_CASTLE_DOOR.get(),
				TFBlocks.YELLOW_CASTLE_DOOR.get(),
				TFBlocks.BLUE_CASTLE_DOOR.get(),
				TFBlocks.VIOLET_CASTLE_DOOR.get(),
				TFBlocks.CINDER_FURNACE.get(),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(),
				//TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.get(),
				//TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.get(),
				//TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.get(),
				//TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.get(),
				TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(),
				TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get(),
				//TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(),
				//TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.get(),
				//TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.get(),
				//TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(),
				//TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.get(),
				//TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.get(),
				//TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.get(),
				//TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.get(),
				TFBlocks.KNIGHTMETAL_BLOCK.get(),
				TFBlocks.IRONWOOD_BLOCK.get(),
				TFBlocks.FIERY_BLOCK.get(),
				TFBlocks.SPIRAL_BRICKS.get(),
				TFBlocks.ETCHED_NAGASTONE.get(),
				TFBlocks.NAGASTONE_PILLAR.get(),
				TFBlocks.NAGASTONE_STAIRS_LEFT.get(),
				TFBlocks.NAGASTONE_STAIRS_RIGHT.get(),
				TFBlocks.MOSSY_ETCHED_NAGASTONE.get(),
				TFBlocks.MOSSY_NAGASTONE_PILLAR.get(),
				TFBlocks.MOSSY_NAGASTONE_STAIRS_LEFT.get(),
				TFBlocks.MOSSY_NAGASTONE_STAIRS_RIGHT.get(),
				TFBlocks.CRACKED_ETCHED_NAGASTONE.get(),
				TFBlocks.CRACKED_NAGASTONE_PILLAR.get(),
				TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(),
				TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(),
				TFBlocks.IRON_LADDER.get(),
				//TFBlocks.TERRORCOTTA_CIRCLE.get(),
				//TFBlocks.TERRORCOTTA_DIAGONAL.get(),
				TFBlocks.TWISTED_STONE.get(),
				TFBlocks.TWISTED_STONE_PILLAR.get(),
				//TFBlocks.LAPIS_BLOCK.get(),
				TFBlocks.KEEPSAKE_CASKET.get(),
				TFBlocks.BOLD_STONE_PILLAR.get()
		);

		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				TFBlocks.SMOKER.get(),
				TFBlocks.FIRE_JET.get(),
				TFBlocks.UBEROUS_SOIL.get()
		);
	}

	private static Block[] getAllMinecraftOrTwilightBlocks(Predicate<Block> predicate) {
		return ForgeRegistries.BLOCKS.getValues().stream()
				.filter(b -> b.getRegistryName() != null && (b.getRegistryName().getNamespace().equals(MOD_ID) || b.getRegistryName().getNamespace().equals("minecraft")) && predicate.test(b))
				.toArray(Block[]::new);
	}

	private static final Set<Block> plants;

	static {
		plants = ImmutableSet.<Block>builder().add(
				Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE, // BlockTags.SMALL_FLOWERS
				Blocks.SUNFLOWER, Blocks.LILAC, Blocks.PEONY, Blocks.ROSE_BUSH, // BlockTags.TALL_FLOWERS
				Blocks.FLOWERING_AZALEA_LEAVES, Blocks.FLOWERING_AZALEA, // BlockTags.FLOWERS
				Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, // BlockTags.LEAVES
				Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.AZALEA, Blocks.FLOWERING_AZALEA, // BlockTags.SAPLINGS
				Blocks.BEETROOTS, Blocks.CARROTS, Blocks.POTATOES, Blocks.WHEAT, Blocks.MELON_STEM, Blocks.PUMPKIN_STEM // BlockTags.CROPS
		).add( // TF addons of above taglists
				TFBlocks.TWILIGHT_OAK_SAPLING.get(),
				TFBlocks.CANOPY_SAPLING.get(),
				TFBlocks.MANGROVE_SAPLING.get(),
				TFBlocks.DARKWOOD_SAPLING.get(),
				TFBlocks.TIME_SAPLING.get(),
				TFBlocks.TRANSFORMATION_SAPLING.get(),
				TFBlocks.MINING_SAPLING.get(),
				TFBlocks.SORTING_SAPLING.get(),
				TFBlocks.HOLLOW_OAK_SAPLING.get(),
				TFBlocks.RAINBOW_OAK_SAPLING.get(),
				TFBlocks.RAINBOW_OAK_LEAVES.get(),
				TFBlocks.TWILIGHT_OAK_LEAVES.get(),
				TFBlocks.CANOPY_LEAVES.get(),
				TFBlocks.MANGROVE_LEAVES.get(),
				TFBlocks.DARK_LEAVES.get(),
				TFBlocks.TIME_LEAVES.get(),
				TFBlocks.TRANSFORMATION_LEAVES.get(),
				TFBlocks.MINING_LEAVES.get(),
				TFBlocks.SORTING_LEAVES.get(),
				TFBlocks.THORN_LEAVES.get(),
				TFBlocks.BEANSTALK_LEAVES.get()
		).build();
	}
}
