package twilightforest.world.components.structures.lichtowerrevamp;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Autowired;
import tamaized.beanification.Component;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.world.components.processors.*;
import twilightforest.world.components.structures.util.StructureTemplateDefinitions;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Component
public class LichTowerUtil {
	@Autowired
	private LichTowerPieces lichRoomPieces;

	private final Supplier<StructureProcessor> roomSpawners = Suppliers.memoize(() -> SpawnerProcessor.compile(2, 0.8f, Object2IntMaps.unmodifiable(Util.make(new Object2IntArrayMap<>(), map -> {
		// 1/3 chance for any spider variant, 1/3 chance for skeleton, 1/3 chance for zombie
		map.put(EntityType.SPIDER, 1);
		map.put(EntityType.CAVE_SPIDER, 1);
		map.put(TFEntities.SWARM_SPIDER.get(), 1);
		map.put(TFEntities.HEDGE_SPIDER.get(), 1);
		map.put(EntityType.SKELETON, 4);
		map.put(EntityType.ZOMBIE, 4);
	}))));
	private final Supplier<StructureProcessor> centralSpawners = Suppliers.memoize(() -> SpawnerProcessor.compile(4, Object2IntMaps.unmodifiable(Util.make(new Object2IntArrayMap<>(), map -> {
		map.put(EntityType.SKELETON, 2);
		map.put(EntityType.ZOMBIE, 1);
		map.put(TFEntities.SWARM_SPIDER.get(), 1);
	}))));
	private final Supplier<List<Block>> STAIR_DECAY_BLOCKS = Suppliers.memoize(() -> List.of(
		TFBlocks.TWILIGHT_OAK_SLAB.value(),
		TFBlocks.CANOPY_SLAB.value(),
		TFBlocks.TWILIGHT_OAK_BANISTER.value(),
		TFBlocks.CANOPY_BANISTER.value()
	));
	private final Supplier<StructureProcessor[]> stairDecayProcessors = Suppliers.memoize(() -> {
		List<Block> filter = this.STAIR_DECAY_BLOCKS.get();
		return new StructureProcessor[]{
			new VerticalDecayProcessor(filter, 0.025f),
			new VerticalDecayProcessor(filter, 0.05f),
			new VerticalDecayProcessor(filter, 0.075f),
			new VerticalDecayProcessor(filter, 0.1f),
			new VerticalDecayProcessor(filter, 0.125f),
			new VerticalDecayProcessor(filter, 0.15f),
			new VerticalDecayProcessor(filter, 0.175f),
			new VerticalDecayProcessor(filter, 0.2f)
		};
	});
	private static final Supplier<StructureProcessor> UPDATE_MARKER = Suppliers.memoize(() -> UpdateMarkingProcessor.forBlocks(
		Blocks.BIRCH_FENCE,
		Blocks.POLISHED_ANDESITE_STAIRS,
		Blocks.STONE_BRICK_WALL,
		Blocks.MOSSY_STONE_BRICK_WALL,
		Blocks.COBBLESTONE_WALL,
		Blocks.MOSSY_COBBLESTONE_WALL,
		TFBlocks.WROUGHT_IRON_FENCE.value(),
		TFBlocks.CANOPY_FENCE.value(),
		TFBlocks.TWISTED_STONE_PILLAR.value()
	));

	public StructureProcessor getRoomSpawnerProcessor() {
		return this.roomSpawners.get();
	}

	public StructureProcessor getCentralBridgeSpawnerProcessor() {
		return this.centralSpawners.get();
	}

	public StructureProcessor[] getStairDecayProcessors() {
		return this.stairDecayProcessors.get();
	}

	@Nullable
	public Identifier rollRandomRoom(RandomSource randomSource, int size) {
		Identifier pool = switch (size) {
			case 0 -> LichTowerPieces.ROOM_3;
			case 1 -> LichTowerPieces.ROOM_5;
			case 2 -> LichTowerPieces.ROOM_7;
			case 3 -> LichTowerPieces.ROOM_9;
			default -> null;
		};

		if (pool == null) return null;

		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, pool);
	}

	@Nullable
	public Identifier rollTowerGallery(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, LichTowerPieces.GALLERY);
	}

	@Nullable
	public Identifier rollGalleryRoof(RandomSource randomSource, BoundingBox box) {
		boolean odd = (Math.min(box.getXSpan(), box.getZSpan()) & 1) == 1;
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, odd ? LichTowerPieces.GALLERY_ROOF_ODD : LichTowerPieces.GALLERY_ROOF_EVEN);
	}

	@Nullable
	public Identifier rollRandomMobBridge(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, LichTowerPieces.MOB_BRIDGE);
	}

	@Nullable
	public Identifier rollRandomCover(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, LichTowerPieces.DOOR_STOPPER);
	}

	@Nullable
	public Identifier rollRandomDecor(RandomSource randomSource, boolean inCentralTower) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, inCentralTower ? LichTowerPieces.CENTER_DECOR : LichTowerPieces.ROOM_DECOR);
	}

	public Iterable<Identifier> shuffledCenterBridges(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.getShuffledSequence(randomSource, LichTowerPieces.BRIDGE_FROM_CENTRAL);
	}

	public Iterable<Identifier> shuffledRoomBridges(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.getShuffledSequence(randomSource, LichTowerPieces.ROOM_BRIDGE);
	}

	public Iterable<Identifier> shuffledEndBridges(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.getShuffledSequence(randomSource, LichTowerPieces.END_BRIDGE);
	}

	public Iterable<Identifier> shuffledRoofs(RandomSource randomSource, int size, boolean sideRoof) {
		Identifier pool = switch (size) {
			case 0 -> sideRoof ? LichTowerPieces.ROOM_3_SIDE_ROOF : LichTowerPieces.ROOM_3_ROOF;
			case 1 -> sideRoof ? LichTowerPieces.ROOM_5_SIDE_ROOF : LichTowerPieces.ROOM_5_ROOF;
			case 2 -> sideRoof ? LichTowerPieces.ROOM_7_SIDE_ROOF : LichTowerPieces.ROOM_7_ROOF;
			case 3 -> sideRoof ? LichTowerPieces.ROOM_9_SIDE_ROOF : LichTowerPieces.ROOM_9_ROOF;
			default -> null;
		};

		if (pool == null) return List.of();

		return StructureTemplateDefinitions.INSTANCE.getShuffledSequence(randomSource, pool);
	}

	public Iterable<Identifier> shuffledBeards(RandomSource randomSource, int size) {
		Identifier pool = switch (size) {
			case 1 -> LichTowerPieces.ROOM_5_BEARD;
			case 2 -> LichTowerPieces.ROOM_7_BEARD;
			case 3 -> LichTowerPieces.ROOM_9_BEARD;
			default -> null;
		};

		if (pool == null) return List.of();

		return StructureTemplateDefinitions.INSTANCE.getShuffledSequence(randomSource, pool);
	}

	@Nullable
	public Identifier getTrim(RandomSource randomSource, int size) {
		Identifier pool = switch (size) {
			case 1 -> LichTowerPieces.ROOM_5_TRIM;
			case 2 -> LichTowerPieces.ROOM_7_TRIM;
			case 3 -> LichTowerPieces.ROOM_9_TRIM;
			default -> null;
		};

		if (pool == null) return null;

		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, pool);
	}

	public Set<String> getLadderPlacementsForSize(int size) {
		return switch (size) {
			case 1 -> this.lichRoomPieces.ladderPlacements1;
			case 2 -> this.lichRoomPieces.ladderPlacements2;
			case 3 -> this.lichRoomPieces.ladderPlacements3;
			default -> Collections.emptySet();
		};
	}

	@Nullable
	public Identifier getRoomUpwards(RandomSource random, int size, int ladderOffset) {
		if (size > 0 && size <= 3) {
			Int2ObjectMap<Identifier> roomsForSize = this.lichRoomPieces.ladderRooms.get(size - 1);
			Identifier roomsForLadderPlacement = roomsForSize.get(ladderOffset);
			return roomsForLadderPlacement == null ? null : StructureTemplateDefinitions.INSTANCE.rollTemplatePool(random, roomsForLadderPlacement);
		}

		return null;
	}

	@Nullable
	public Identifier getFallbackRoof(RandomSource random, int size, boolean sideAttachment) {
		Identifier pool = switch (size) {
			case 0 -> sideAttachment ? LichTowerPieces.ROOM_3_SIDE_ROOF_FALLBACK : LichTowerPieces.ROOM_3_ROOF_FALLBACK;
			case 1 -> sideAttachment ? LichTowerPieces.ROOM_5_SIDE_ROOF_FALLBACK : LichTowerPieces.ROOM_5_ROOF_FALLBACK;
			case 2 -> sideAttachment ? LichTowerPieces.ROOM_7_SIDE_ROOF_FALLBACK : LichTowerPieces.ROOM_7_ROOF_FALLBACK;
			case 3 -> sideAttachment ? LichTowerPieces.ROOM_9_SIDE_ROOF_FALLBACK : LichTowerPieces.ROOM_9_ROOF_FALLBACK;
			default -> null;
		};

		if (pool == null) return null;

		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(random, pool);
	}

	@Nullable
	public Identifier getFallbackBeard(RandomSource random, int size) {
		Identifier pool = switch (size) {
			case 1 -> LichTowerPieces.ROOM_5_BEARD_FALLBACK;
			case 2 -> LichTowerPieces.ROOM_7_BEARD_FALLBACK;
			case 3 -> LichTowerPieces.ROOM_9_BEARD_FALLBACK;
			default -> null;
		};

		if (pool == null) return null;

		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(random, pool);
	}

	public static void addDefaultProcessors(StructurePlaceSettings settings) {
		settings.addProcessor(JigsawReplacementProcessor.INSTANCE)
			.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
			.addProcessor(StoneBricksVariants.INSTANCE)
			.addProcessor(CobbleVariants.INSTANCE)
			.addProcessor(InfestBlocksProcessor.INSTANCE)
			.addProcessor(UPDATE_MARKER.get());
	}

	@Nullable
	public Identifier getKeepsakeCasketRoom(RandomSource random) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(random, LichTowerPieces.ROOM_9_SPECIAL);
	}

	@Nullable
	public Identifier getEnclosedCentralBridge(RandomSource random) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(random, LichTowerPieces.BRIDGE_FROM_CENTRAL_FALLBACK);
	}

	@Nullable
	public Identifier getDirectRoomAttachment(RandomSource random) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(random, LichTowerPieces.ROOM_BRIDGE_FALLBACK);
	}

	@Nullable
	public Identifier getDefaultBridgeStopper(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, LichTowerPieces.DOOR_STOPPER_FALLBACK);
	}

	@Nullable
	public Identifier rollGrave(RandomSource randomSource) {
		return StructureTemplateDefinitions.INSTANCE.rollTemplatePool(randomSource, LichTowerPieces.YARD_GRAVE);
	}
}
