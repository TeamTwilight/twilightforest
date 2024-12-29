package twilightforest.world.components.structures.lichtowerrevamp;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import org.jetbrains.annotations.Nullable;
import twilightforest.beans.Autowired;
import twilightforest.beans.Component;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.util.ArrayUtil;
import twilightforest.world.components.processors.*;

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
			new VerticalDecayProcessor(filter, 0.05f),
			new VerticalDecayProcessor(filter, 0.1f),
			new VerticalDecayProcessor(filter, 0.15f),
			new VerticalDecayProcessor(filter, 0.2f),
			new VerticalDecayProcessor(filter, 0.25f),
			new VerticalDecayProcessor(filter, 0.3f),
			new VerticalDecayProcessor(filter, 0.35f)
		};
	});
	private static final Supplier<StructureProcessor> UPDATE_MARKER = Suppliers.memoize(() -> UpdateMarkingProcessor.forBlocks(
		Blocks.BIRCH_FENCE,
		Blocks.MOSSY_STONE_BRICK_WALL,
		Blocks.POLISHED_ANDESITE_STAIRS,
		Blocks.STONE_BRICK_WALL,
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
	public ResourceLocation rollRandomRoom(RandomSource randomSource, int size) {
		return ArrayUtil.randomOrNull(ArrayUtil.orNull(this.lichRoomPieces.rooms, size), randomSource);
	}

	@Nullable
	public ResourceLocation rollTowerGallery(RandomSource randomSource) {
		return ArrayUtil.randomOrNull(this.lichRoomPieces.galleryRooms, randomSource);
	}

	@Nullable
	public ResourceLocation rollGalleryRoof(RandomSource randomSource, BoundingBox box) {
		boolean odd = (Math.min(box.getXSpan(), box.getZSpan()) & 1) == 1;
		return ArrayUtil.randomOrNull(odd ? this.lichRoomPieces.galleryRoofsOdd : this.lichRoomPieces.galleryRoofsEven, randomSource);
	}

	public ResourceLocation rollRandomMobBridge(RandomSource randomSource) {
		return Util.getRandom(this.lichRoomPieces.mobBridges, randomSource);
	}

	public ResourceLocation rollRandomCover(RandomSource randomSource) {
		return Util.getRandom(this.lichRoomPieces.bridgeCovers, randomSource);
	}

	public ResourceLocation rollRandomDecor(RandomSource randomSource, boolean inCentralTower) {
		return Util.getRandom(inCentralTower ? this.lichRoomPieces.centerDecors : this.lichRoomPieces.roomDecors, randomSource);
	}

	public Iterable<ResourceLocation> shuffledCenterBridges(RandomSource randomSource) {
		return Util.shuffledCopy(this.lichRoomPieces.centerBridges, randomSource);
	}

	public Iterable<ResourceLocation> shuffledRoomBridges(RandomSource randomSource) {
		return Util.shuffledCopy(this.lichRoomPieces.roomBridges, randomSource);
	}

	public Iterable<ResourceLocation> shuffledEndBridges(RandomSource randomSource) {
		return Util.shuffledCopy(this.lichRoomPieces.endBridges, randomSource);
	}

	public Iterable<ResourceLocation> shuffledRoofs(RandomSource randomSource, int size, boolean doSideRoofOnly) {
		return ArrayUtil.safeShuffledCopy(ArrayUtil.orNull(doSideRoofOnly ? this.lichRoomPieces.sideRoofs : this.lichRoomPieces.roofs, size), randomSource);
	}

	public Iterable<ResourceLocation> shuffledBeards(RandomSource randomSource, int size) {
		return ArrayUtil.safeShuffledCopy(ArrayUtil.orNull(this.lichRoomPieces.wingBeards, size - 1), randomSource);
	}

	@Nullable
	public ResourceLocation getTrim(int size) {
		return ArrayUtil.orNull(this.lichRoomPieces.wingTrims, size - 1);
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
	public ResourceLocation getRoomUpwards(RandomSource random, int size, int ladderOffset) {
		if (size > 0 && size <= 3) {
			Int2ObjectMap<List<ResourceLocation>> roomsForSize = this.lichRoomPieces.ladderRooms.get(size - 1);
			List<ResourceLocation> roomsForLadderPlacement = roomsForSize.getOrDefault(ladderOffset, Collections.emptyList());
			return roomsForLadderPlacement.isEmpty() ? null : roomsForLadderPlacement.get(random.nextInt(roomsForLadderPlacement.size()));
		}

		return null;
	}

	@Nullable
	public ResourceLocation getFallbackRoof(int size, boolean sideAttachment) {
		return ArrayUtil.orNull(sideAttachment ? this.lichRoomPieces.flatSideRoofs : this.lichRoomPieces.flatRoofs, size);
	}

	@Nullable
	public ResourceLocation getFallbackBeard(int size) {
		return ArrayUtil.orNull(this.lichRoomPieces.flatBeards, size - 1);
	}

	public static void addDefaultProcessors(StructurePlaceSettings settings) {
		settings.addProcessor(MetaBlockProcessor.INSTANCE)
			.addProcessor(StoneBricksVariants.INSTANCE)
			.addProcessor(CobbleVariants.INSTANCE)
			.addProcessor(InfestBlocksProcessor.INSTANCE)
			.addProcessor(UPDATE_MARKER.get());
	}

	public ResourceLocation getKeepsakeCasketRoom() {
		return this.lichRoomPieces.keepsakeCasketRoom;
	}

	public ResourceLocation getEnclosedCentralBridge() {
		return this.lichRoomPieces.enclosedBridgeCentral;
	}

	public ResourceLocation getDirectRoomAttachment() {
		return this.lichRoomPieces.directAttachment;
	}

	public ResourceLocation getDefaultBridgeStopper() {
		return this.lichRoomPieces.cobblestoneWall;
	}

	public ResourceLocation rollGrave(RandomSource randomSource) {
		// TODO Random graves?
		return this.lichRoomPieces.yardGrave;
	}
}
