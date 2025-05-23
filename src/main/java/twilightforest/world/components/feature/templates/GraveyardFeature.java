package twilightforest.world.components.feature.templates;

import com.google.common.math.StatsAccumulator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.event.EventHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.monster.Wraith;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructureProcessors;
import twilightforest.loot.TFLootTables;
import twilightforest.util.features.FeatureLogic;

import java.util.ArrayList;
import java.util.List;

public class GraveyardFeature extends Feature<NoneFeatureConfiguration> {
	private static final ResourceLocation GRAVEYARD = TwilightForestMod.prefix("feature/graveyard/graveyard");
	private static final ResourceLocation TRAP = TwilightForestMod.prefix("feature/graveyard/grave_trap");

	public GraveyardFeature(Codec<NoneFeatureConfiguration> config) {
		super(config);
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
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();

		int flags = 16 | 2 | 1;

		StructureTemplateManager templatemanager = world.getLevel().getServer().getStructureManager();
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
		Rotation rotation = rotations[rand.nextInt(rotations.length)];

		Mirror[] mirrors = Mirror.values();
		Mirror mirror = mirrors[rand.nextInt(mirrors.length + 1) % mirrors.length];

		Vec3i transformedSize = base.getSize(rotation);
		Vec3i transformedGraveSize = graves.get(0).getValue().getSize(rotation);

		ChunkPos chunkpos = new ChunkPos(pos.offset(-8, 0, -8));
		ChunkPos chunkendpos = new ChunkPos(pos.offset(-8, 0, -8).offset(transformedSize));
		BoundingBox structureboundingbox = new BoundingBox(chunkpos.getMinBlockX() + 8, 0, chunkpos.getMinBlockZ() + 8, chunkendpos.getMaxBlockX() + 8, 255, chunkendpos.getMaxBlockZ() + 8);
		StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(rand);

		BlockPos posSnap = chunkpos.getWorldPosition().offset(8, pos.getY() - 1, 8); // Verify this is correct. Originally chunkpos.getBlock(8, pos.getY() - 1, 8);
		BlockPos.MutableBlockPos startPos = new BlockPos.MutableBlockPos(posSnap.getX(), posSnap.getY(), posSnap.getZ());

		if (!offsetToAverageGroundLevel(world, startPos, transformedSize)) {
			return false;
		}

		BlockPos placementPos = base.getZeroPositionWithTransform(startPos, mirror, rotation).offset(1, -1, 0);
		Vec3i size = transformedSize.offset(-1, 0, -1);
		Vec3i graveSize = transformedGraveSize.offset(-1, 0, -1);

		base.placeInWorld(world, placementPos, placementPos, placementsettings.addProcessor(new WebTemplateProcessor()), rand, flags);
		List<StructureTemplate.StructureBlockInfo> data = new ArrayList<>(base.filterBlocks(placementPos, placementsettings, Blocks.STRUCTURE_BLOCK));

		BlockPos start = startPos.offset(1, 1, 0);
		BlockPos end = start.offset(size.getX(), 0, size.getZ());

		for (int x = 1; x <= size.getX() - 1; x++)
			for (int z = 1; z <= size.getZ() - 1; z++)
				if (world.isEmptyBlock(start.offset(x, 0, z)) && rand.nextInt(12) == 0)
					world.setBlock(start.offset(x, 0, z), Blocks.COBWEB.defaultBlockState(), flags);

		BlockPos inner = start.offset(2, 0, 2);
		BlockPos bound = end.offset(-2, 0, -2);
		BlockPos innerSize = new BlockPos(bound.getX() - inner.getX(), bound.getY() - inner.getY(), bound.getZ() - inner.getZ());
		BlockPos fixed = inner.offset(

			(rotation == Rotation.CLOCKWISE_180 ? graveSize.getX() : 0) + (mirror == Mirror.FRONT_BACK ? transformedGraveSize.getX() - 1 : 0) * (rotation == Rotation.CLOCKWISE_180 ? -1 : 1),

			0,

			(rotation == Rotation.COUNTERCLOCKWISE_90 ? graveSize.getZ() : 0) + (mirror == Mirror.FRONT_BACK ? transformedGraveSize.getZ() - 1 : 0) * (rotation == Rotation.COUNTERCLOCKWISE_90 ? -1 : 1)

		);
		BlockPos fixedSize = innerSize.offset(-graveSize.getX(), 0, -graveSize.getZ());
		BlockPos chestloc = new BlockPos(rand.nextInt(2) - (mirror == Mirror.FRONT_BACK ? 1 : 0), 1, 0).rotate(rotation);

		for (int x = 0; x <= fixedSize.getX(); x += (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90 ? 2 : 5)) {
			for (int z = 0; z <= fixedSize.getZ(); z += (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180 ? 2 : 5)) {
				if (x == innerSize.getX() / 2 || z == innerSize.getZ() / 2)
					continue;
				BlockPos placement = fixed.offset(x, -2, z);
				Pair<GraveType, StructureTemplate> grave = graves.get(rand.nextInt(graves.size()));
				grave.getValue().placeInWorld(world, placement, placement, placementsettings, rand, flags);
				data.addAll(grave.getValue().filterBlocks(placement, placementsettings, Blocks.STRUCTURE_BLOCK));
				if (grave.getKey() == GraveType.Full) {
					if (rand.nextBoolean()) {
						if (rand.nextInt(3) == 0) {
							placement = placement.offset(new BlockPos(mirror == Mirror.FRONT_BACK ? 1 : -1, 0, mirror == Mirror.LEFT_RIGHT ? 1 : -1).rotate(rotation));
							trap.placeInWorld(world, placement, placement, placementsettings, rand, flags);
						}
						data.addAll(trap.filterBlocks(placementPos, placementsettings, Blocks.STRUCTURE_BLOCK));
						if (world.setBlock(placement.offset(chestloc), Blocks.TRAPPED_CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.WEST).rotate(rotation).mirror(mirror), flags)) {
							TFLootTables.generateChestContents(world, placement.offset(chestloc), TFLootTables.GRAVEYARD);
							world.setBlock(placement.offset(chestloc).below(), Blocks.MOSSY_COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL);
						}
						Wraith wraith = new Wraith(TFEntities.WRAITH.get(), world.getLevel());
						wraith.setPos(placement.getX(), placement.getY(), placement.getZ());
						EventHooks.finalizeMobSpawn(wraith, world, world.getCurrentDifficultyAt(placement), EntitySpawnReason.STRUCTURE, null);
						world.addFreshEntity(wraith);
					}
				}
			}
		}

		data.forEach(info -> {
			if (info.nbt() != null && StructureMode.valueOf(info.nbt().getString("mode")) == StructureMode.DATA) {
				String s = info.nbt().getString("metadata");
				BlockPos p = info.pos();
				if ("spawner".equals(s)) {
					world.removeBlock(p, false);
					if (rand.nextInt(4) == 0) {
						if (world.setBlock(p, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_ALL)) {
							SpawnerBlockEntity ms = (SpawnerBlockEntity) world.getBlockEntity(p);
							if (ms != null)
								ms.setEntityId(TFEntities.RISING_ZOMBIE.get(), rand);
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
		private final ResourceLocation RL;

		GraveType(ResourceLocation rl) {
			this.RL = rl;
		}
	}

	public static class WebTemplateProcessor extends StructureProcessor {
		public static final WebTemplateProcessor INSTANCE = new WebTemplateProcessor();
		public static final MapCodec<WebTemplateProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

		private WebTemplateProcessor() {
		}

		@Override
		protected StructureProcessorType<?> getType() {
			return TFStructureProcessors.WEB.get();
		}

		@Nullable
		@Override
		public StructureTemplate.StructureBlockInfo process(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_process_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
			return blockInfo.state().getBlock() == Blocks.GRASS_BLOCK ? blockInfo : settings.getRandom(pos).nextInt(5) == 0 ? new StructureTemplate.StructureBlockInfo(pos, Blocks.COBWEB.defaultBlockState(), null) : blockInfo;
		}
	}
}
