package twilightforest.world.components.structures;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class HydraLairComponent extends HollowHillComponent {

	public HydraLairComponent(ServerLevel level, CompoundTag nbt) {
		super(TFFeature.TFHydra, nbt);
	}

	public HydraLairComponent(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFeature.TFHydra, feature, i, 2, x, y + 2, z);
	}

	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor accessor, Random random) {
		// NO-OP
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int stalacts = 64;
		int stalags = 8;

		// fill in features
		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < stalacts; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, this.radius);
			this.generateOreStalactite(world, dest.move(0, 1, 0), sbb);
		}
		// stone stalactites!
		for (int i = 0; i < stalacts; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, this.radius);
			this.generateBlockSpike(world, STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < stalags; i++) {
			BlockPos.MutableBlockPos dest = this.randomFloorCoordinates(rand, this.radius);
			this.generateBlockSpike(world, STONE_STALAGMITE, dest.getX(), dest.getY(), dest.getZ(), sbb);
		}

		// boss spawner seems important
		placeBlock(world, TFBlocks.HYDRA_BOSS_SPAWNER.get().defaultBlockState(), 27, 3, 27, sbb);

		return true;
	}

	@Override
	BlockPos.MutableBlockPos randomFloorCoordinates(Random rand, float maximumRadius) {
		float degree = rand.nextFloat() * 4.537856f + 1.7453294f; // rand * (2pi - 5pi/9) + 5pi/9
		// The full radius isn't actually hollow. Not feeling like doing the math to find the intersections of the curves involved
		float radius = maximumRadius * 0.9f * (rand.nextFloat() * 0.35f + 0.65f); // 0.9 (max width) * 0.65 minimum radius
		// Nonetheless the floor-carving curve is one-third the top-level terrain curve
		float dist = Mth.sqrt(radius * radius);
		float height = 4 - Mth.cos(dist / this.hdiam * Mth.PI) * (this.hdiam / 20f);

		return new BlockPos.MutableBlockPos(maximumRadius - Mth.cos(degree) * radius, height, maximumRadius - Mth.sin(degree) * radius);
	}

	@Override
	BlockPos.MutableBlockPos randomCeilingCoordinates(Random rand, float maximumRadius) {
		float degree = rand.nextFloat() * 4.537856f + 1.7453294f; // rand * (2pi - 5pi/9) + 5pi/9
		// The full radius isn't actually hollow. Not feeling like doing the math to find the intersections of the curves involved
		float radius = rand.nextFloat() * 0.9f * maximumRadius;
		// Nonetheless the floor-carving curve is one-third the top-level terrain curve
		float dist = Mth.sqrt(radius * radius);
		float height = Mth.cos(dist / this.hdiam * Mth.PI) * (this.hdiam / 4f);

		return new BlockPos.MutableBlockPos(maximumRadius - Mth.cos(degree) * radius, height, maximumRadius - Mth.sin(degree) * radius);
	}
}
