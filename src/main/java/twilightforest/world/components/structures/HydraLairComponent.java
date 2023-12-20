package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.data.custom.stalactites.entry.HillConfig;
import twilightforest.data.custom.stalactites.entry.StalactiteReloadListener;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RectangleLatticeIterator;
import twilightforest.world.components.feature.BlockSpikeFeature;


public class HydraLairComponent extends HollowHillComponent {

	public HydraLairComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFHydra.get(), nbt);
	}

	public HydraLairComponent(int i, int x, int y, int z, RectangleLatticeIterator.TriangularLatticeConfig spikePlacement) {
		super(TFStructurePieceTypes.TFHydra.get(), i, 2, x, y + 2, z, spikePlacement);
	}

	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor accessor, RandomSource random) {
		// NO-OP
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox writeableBounds, ChunkPos chunkPosIn, BlockPos blockPos) {
		float radiusSq = 0.9f * this.radius * this.radius;
		BlockPos locator = this.getLocatorPosition();

		BlockPos exclusionCenter = locator.offset(-16, 0, -16);

        HillConfig config = StalactiteReloadListener.HILL_CONFIGS.get(HillConfig.HillType.HYDRA_LAIR);

		float exclusionRadiusSq = 23 * 23;
		for (BlockPos.MutableBlockPos dest : this.spikePlacement.boundedGrid(writeableBounds, 0)) {
			// xz -9 -9 from spawner, center of exclude circle with radius 24
			int dX = dest.getX() - exclusionCenter.getX();
			int dZ = dest.getZ() - exclusionCenter.getZ();

			if (dX * dX + dZ * dZ < exclusionRadiusSq) continue;

			int distSq = getDistSqFromCenter(locator, dest);

			if (distSq > rand.nextFloat() * 0.9f * radiusSq) continue;

			dest.setY(Mth.floor(Mth.cos(Mth.sqrt(distSq) / this.hdiam * Mth.PI) * (this.hdiam / 4f)));

			if (config.shouldDoAStalactite(rand)) {
				if (rand.nextBoolean()) {
					this.generateOreStalactite(world, dest, writeableBounds, HillConfig.HillType.HYDRA_LAIR, true);
				} else {
					this.generateBlockSpike(world, BlockSpikeFeature.STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), writeableBounds, true);
				}
			}

			if (config.shouldDoAStalagmite(rand)) {
				if ((rand.nextFloat() * 0.667f + 0.333f) * distSq / radiusSq < 0.333f) continue;

				dest.setY(1);

				this.generateOreStalactite(world, dest, writeableBounds, HillConfig.HillType.HYDRA_LAIR, false);
				//this.generateBlockSpike(world, BlockSpikeFeature.STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), writeableBounds, false);
			}
		}

		// boss spawner seems important
		placeBlock(world, TFBlocks.HYDRA_BOSS_SPAWNER.get().defaultBlockState(), 27, 3, 27, writeableBounds);
	}
}