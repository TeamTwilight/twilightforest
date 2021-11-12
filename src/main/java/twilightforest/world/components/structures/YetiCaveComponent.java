package twilightforest.world.components.structures;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.components.feature.config.SpikeConfig;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class YetiCaveComponent extends HollowHillComponent {
	private static final SpikeConfig BLUE_ICE_SPIKE = new SpikeConfig(new SimpleStateProvider(Blocks.BLUE_ICE.defaultBlockState()), UniformInt.of(8, 8), ConstantInt.of(4), true);
	private static final SpikeConfig PACKED_ICE_SPIKE = new SpikeConfig(new SimpleStateProvider(Blocks.PACKED_ICE.defaultBlockState()), UniformInt.of(5, 9), ConstantInt.of(4), true);
	private static final SpikeConfig ICE_SPIKE = new SpikeConfig(new SimpleStateProvider(Blocks.ICE.defaultBlockState()), UniformInt.of(6, 10), ConstantInt.of(4), true);

	public YetiCaveComponent(ServerLevel level, CompoundTag nbt) {
		super(TFFeature.TFYeti, nbt);
	}

	public YetiCaveComponent(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFeature.TFYeti, feature, i, 2, x, y, z);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int sn = 64;

		// fill in features

//		// ore or glowing stalactites! (smaller, less plentiful)
//		for (int i = 0; i < sn; i++)
//		{
//			int[] dest = getCoordsInHill2D(rand);
//			generateOreStalactite(world, dest[0], 1, dest[1], sbb);
//		}
		// blue ice stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, BLUE_ICE_SPIKE, dest.getX(), dest.getY(), dest.getZ(), sbb);
		}
		// packed ice stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, PACKED_ICE_SPIKE, dest.getX(), dest.getY(), dest.getZ(), sbb);
		}
		// ice stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, ICE_SPIKE, dest.getX(), dest.getY(), dest.getZ(), sbb);
		}
		// stone stalactites!
		for (int i = 0; i < sn; i++) {
			BlockPos.MutableBlockPos dest = this.randomCeilingCoordinates(rand, 24);
			this.generateBlockSpike(world, STONE_STALACTITE, dest.getX(), dest.getY(), dest.getZ(), sbb);
		}

		// spawn alpha yeti
		final BlockState yetiSpawner = TFBlocks.ALPHA_YETI_BOSS_SPAWNER.get().defaultBlockState();
		this.setBlockStateRotated(world, yetiSpawner, this.radius, 1, this.radius, Rotation.NONE, sbb);

		return true;
	}

	/**
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	@Override
	boolean isInHill(int cx, int cz) {
		// yeti cave is square
		return cx < this.radius * 2 && cx > 0 && cz < this.radius * 2 && cz > 0;
	}

	@Override
	BlockPos.MutableBlockPos randomCeilingCoordinates(Random rand, float maximumRadius) {
		int rad = (int) maximumRadius;
		int x = rand.nextInt(rad * 2) - rad;
		int z = rand.nextInt(rad * 2) - rad;

		int dist = Math.min(Math.abs(x), Math.abs(z));

		return new BlockPos.MutableBlockPos(this.radius + x, 17 - dist / 6, this.radius + z);
	}
}
