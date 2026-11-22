package twilightforest.world.components.feature.templates;

import com.google.common.math.StatsAccumulator;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.event.EventHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.Wraith;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureProcessors;
import twilightforest.loot.TFLootTables;
import twilightforest.util.features.FeatureLogic;

import java.util.ArrayList;
import java.util.List;

public record GraveyardFeature() implements Feature {

	private static final Identifier GRAVEYARD = TwilightForestMod.prefix("feature/graveyard/graveyard");
	private static final Identifier TRAP = TwilightForestMod.prefix("feature/graveyard/grave_trap");

	public static final MapCodec<GraveyardFeature> MAP_CODEC = MapCodec.unit(GraveyardFeature::new);

	@Override
	public MapCodec<? extends Feature> codec() {
		return MAP_CODEC;
	}

	private static boolean offsetToAverageGroundLevel(WorldGenLevel world, BlockPos.MutableBlockPos startPos, Vec3i size) {
		StatsAccumulator heights = new StatsAccumulator();

		for (int dx = 0; dx < size.getX(); dx++) {
			for (int dz = 0; dz < size.getZ(); dz++) {

				int x = startPos.getX() + dx;
				int z = startPos.getZ() + dz;

				int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

				while (y >= 0) {
					BlockState state = world.getBlockState(new BlockPos(x, y, z));
					if (FeatureLogic.isBlockNotOk(state))
						return false;
					if (FeatureLogic.isBlockOk(state))
						break;
					y--;
				}

				if (y < 0)
					return false;

				heights.add(y);
			}
		}

		if (heights.populationStandardDeviation() > 2.0) {
			return false;
		}

		int baseY = (int) Math.round(heights.mean());
		int maxY = (int) heights.max();

		startPos.setY(baseY);

		return FeatureLogic.isAreaClear(world, startPos.above(maxY - baseY + 1), startPos.offset(size));
	}

	@Override
	public boolean place(WorldGenLevel worldGenLevel, ChunkGenerator chunkGenerator, RandomSource randomSource, BlockPos blockPos) {
		int flags = 16 | 2 | 1;

		StructureTemplateManager templatemanager = worldGenLevel.getLevel().getServer().getStructureTemplateManager();
		StructureTemplate base = templatemanager.getOrCreate(GRAVEYARD);
		if (base == null)
			return false;
		List<Pair<GraveType, StructureTemplate>> graves = new ArrayList<>();
		StructureTemplate trap = templatemanager.getOrCreate(TRAP);
		if (trap == null)
			return false;
		for (GraveType type : GraveType.VALUES) {
			StructureTemplate grave = templatemanager.getOrCreate(type.RL);
			if (grave == null)
				return false;
			graves.add(Pair.of(type, grave));
		}

		Rotation[] rotations = Rotation.values();
		Rotation rotation = rotations[randomSource.nextInt(rotations.length)];

		Mirror[] mirrors = Mirror.values();
		Mirror mirror = mirrors[randomSource.nextInt(mirrors.length + 1) % mirrors.length];

		Vec3i transformedSize = base.getSize(rotation);
		Vec3i transformedGraveSize = graves.get(0).getValue().getSize(rotation);

		ChunkPos chunkpos = ChunkPos.containing(blockPos.offset(-8, 0, -8));
		ChunkPos chunkendpos =ChunkPos.containing(blockPos.offset(-8, 0, -8).offset(transformedSize));
		BoundingBox structureboundingbox = new BoundingBox(chunkpos.getMinBlockX() + 8, 0, chunkpos.getMinBlockZ() + 8, chunkendpos.getMaxBlockX() + 8, 255, chunkendpos.getMaxBlockZ() + 8);
		StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(randomSource);

		BlockPos posSnap = chunkpos.getWorldPosition().offset(8, blockPos.getY() - 1, 8); // Verify this is correct. Originally chunkpos.getBlock(8, pos.getY() - 1, 8);
		BlockPos.MutableBlockPos startPos = new BlockPos.MutableBlockPos(posSnap.getX(), posSnap.getY(), posSnap.getZ());

		if (!offsetToAverageGroundLevel(worldGenLevel, startPos, transformedSize)) {
			return false;
		}

		BlockPos placementPos = base.getZeroPositionWithTransform(startPos, mirror, rotation).offset(1, -1, 0);
		Vec3i size = transformedSize.offset(-1, 0, -1);
		Vec3i graveSize = transformedGraveSize.offset(-1, 0, -1);

		base.placeInWorld(worldGenLevel, placementPos, placementPos, placementsettings.addProcessor(new WebTemplateProcessor()), randomSource, flags);
		List<StructureTemplate.StructureBlockInfo> data = new ArrayList<>(base.filterBlocks(placementPos, placementsettings, Blocks.STRUCTURE_BLOCK));

		BlockPos start = startPos.offset(1, 1, 0);
		BlockPos end = start.offset(size.getX(), 0, size.getZ());

		for (int x = 1; x <= size.getX() - 1; x++)
			for (int z = 1; z <= size.getZ() - 1; z++)
				if (worldGenLevel.isEmptyBlock(start.offset(x, 0, z)) && randomSource.nextInt(12) == 0)
					worldGenLevel.setBlock(start.offset(x, 0, z), Blocks.COBWEB.defaultBlockState(), flags);

		BlockPos inner = start.offset(2, 0, 2);
		BlockPos bound = end.offset(-2, 0, -2);
		BlockPos innerSize = new BlockPos(bound.getX() - inner.getX(), bound.getY() - inner.getY(), bound.getZ() - inner.getZ());
		BlockPos fixed = inner.offset(

			(rotation == Rotation.CLOCKWISE_180 ? graveSize.getX() : 0) + (mirror == Mirror.FRONT_BACK ? transformedGraveSize.getX() - 1 : 0) * (rotation == Rotation.CLOCKWISE_180 ? -1 : 1),

			0,

			(rotation == Rotation.COUNTERCLOCKWISE_90 ? graveSize.getZ() : 0) + (mirror == Mirror.FRONT_BACK ? transformedGraveSize.getZ() - 1 : 0) * (rotation == Rotation.COUNTERCLOCKWISE_90 ? -1 : 1)

		);
		BlockPos fixedSize = innerSize.offset(-graveSize.getX(), 0, -graveSize.getZ());
		BlockPos chestloc = new BlockPos(randomSource.nextInt(2) - (mirror == Mirror.FRONT_BACK ? 1 : 0), 1, 0).rotate(rotation);

		for (int x = 0; x <= fixedSize.getX(); x += (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90 ? 2 : 5)) {
			for (int z = 0; z <= fixedSize.getZ(); z += (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180 ? 2 : 5)) {
				if (x == innerSize.getX() / 2 || z == innerSize.getZ() / 2)
					continue;
				BlockPos placement = fixed.offset(x, -2, z);
				Pair<GraveType, StructureTemplate> grave = graves.get(randomSource.nextInt(graves.size()));
				grave.getValue().placeInWorld(worldGenLevel, placement, placement, placementsettings, randomSource, flags);
				data.addAll(grave.getValue().filterBlocks(placement, placementsettings, Blocks.STRUCTURE_BLOCK));
				if (grave.getKey() == GraveType.Full) {
					if (randomSource.nextBoolean()) {
						if (randomSource.nextInt(3) == 0) {
							placement = placement.offset(new BlockPos(mirror == Mirror.FRONT_BACK ? 1 : -1, 0, mirror == Mirror.LEFT_RIGHT ? 1 : -1).rotate(rotation));
							trap.placeInWorld(worldGenLevel, placement, placement, placementsettings, randomSource, flags);
						}
						data.addAll(trap.filterBlocks(placementPos, placementsettings, Blocks.STRUCTURE_BLOCK));
						if (worldGenLevel.setBlock(placement.offset(chestloc), Blocks.TRAPPED_CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST).rotate(rotation).mirror(mirror), flags)) {
							TFLootTables.generateChestContents(worldGenLevel, placement.offset(chestloc), TFLootTables.GRAVEYARD);
							worldGenLevel.setBlock(placement.offset(chestloc).below(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
						}
						Wraith wraith = new Wraith(TFEntities.WRAITH.get(), worldGenLevel.getLevel());
						wraith.setPos(placement.getX(), placement.getY(), placement.getZ());
						EventHooks.finalizeMobSpawn(wraith, worldGenLevel, worldGenLevel.getCurrentDifficultyAt(placement), EntitySpawnReason.STRUCTURE, null);
						worldGenLevel.addFreshEntity(wraith);
					}
				}
			}
		}

		data.forEach(info -> {
			StructureMode mode = StructureMode.valueOf(info.nbt().getString("mode").orElseThrow());
			if (info.nbt() != null && mode == StructureMode.DATA) {
				String s = info.nbt().getString("metadata").orElseThrow();
				BlockPos p = info.pos();
				if ("spawner".equals(s)) {
					worldGenLevel.removeBlock(p, false);
					if (randomSource.nextInt(4) == 0) {
						if (worldGenLevel.setBlock(p, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_ALL)) {
							SpawnerBlockEntity ms = (SpawnerBlockEntity) worldGenLevel.getBlockEntity(p);
							if (ms != null)
								ms.setEntityId(TFEntities.RISING_ZOMBIE.get(), randomSource);
						}
					}
				}
			}
		});

		return true;
	}

	private enum GraveType {

		Full(TwilightForestMod.prefix("feature/graveyard/grave_full")),

		Upper(TwilightForestMod.prefix("feature/graveyard/grave_upper")),

		Lower(TwilightForestMod.prefix("feature/graveyard/grave_lower"));

		private static final GraveType[] VALUES = values();
		private final Identifier RL;

		GraveType(Identifier rl) {
			this.RL = rl;
		}
	}

	public static class WebTemplateProcessor implements StructureProcessor {
		public static final WebTemplateProcessor INSTANCE = new WebTemplateProcessor();
		public static final MapCodec<WebTemplateProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

		private WebTemplateProcessor() {
		}

		@Override
		public MapCodec<? extends StructureProcessor> codec() {
			return TFStructureProcessors.WEB.get();
		}

		@Override
		public StructureTemplate.@Nullable StructureBlockInfo process(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_process_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
			return blockInfo.state().getBlock() == Blocks.GRASS_BLOCK ? blockInfo : settings.getRandom(pos).nextInt(5) == 0 ? new StructureTemplate.StructureBlockInfo(pos, Blocks.COBWEB.defaultBlockState(), null) : blockInfo;
		}
	}
}
