package twilightforest.world.components.structures.trollcave;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.Features;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.BlockConstants;
import twilightforest.world.registration.TFBiomeFeatures;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class TrollCaveGardenComponent extends TrollCaveMainComponent {
	// FIXME These should probably reference stuff from the datapack
	private final ConfiguredFeature<?,?> myceliumBlobGen = TFBiomeFeatures.MYCELIUM_BLOB.get().configured(new DiskConfiguration(BlockConstants.MYCELIUM, UniformInt.of(3, 5), 0, ImmutableList.of(BlockConstants.STONE, BlockConstants.DEADROCK)));
	private final ConfiguredFeature<?,?> dirtGen = TFBiomeFeatures.MYCELIUM_BLOB.get().configured(new DiskConfiguration(BlockConstants.DIRT, UniformInt.of(2, 5), 0, ImmutableList.of(BlockConstants.STONE, BlockConstants.DEADROCK)));
	private final ConfiguredFeature<?,?> smallUberousGen = TFBiomeFeatures.MYCELIUM_BLOB.get().configured(new DiskConfiguration(BlockConstants.UBEROUS_SOIL, UniformInt.of(2, 3), 0, ImmutableList.of(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.DIRT)));
	private final ConfiguredFeature<?,?> bigRedMushroomGen = Features.HUGE_RED_MUSHROOM;
	private final ConfiguredFeature<?,?> bigBrownMushroomGen = Features.HUGE_BROWN_MUSHROOM;
	private final ConfiguredFeature<?,?> bigMushgloomGen = TFBiomeFeatures.BIG_MUSHGLOOM.get().configured(FeatureConfiguration.NONE);

	public TrollCaveGardenComponent(ServerLevel level, CompoundTag nbt) {
		super(TrollCavePieces.TFTCGard, nbt);
	}

	public TrollCaveGardenComponent(TFFeature feature, int index, int x, int y, int z, int caveSize, int caveHeight, Direction direction) {
		super(TrollCavePieces.TFTCGard, feature, index, x, y, z);
		this.size = caveSize;
		this.height = caveHeight;
		this.setOrientation(direction);
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		// add a cloud
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
//		if (this.isBoundingBoxOutsideBiomes(world, sbb, highlands)) {
//			return false;
//		}

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// treasure!
		makeTreasureCrate(world, sbb);

		// dirt!
		for (int i = 0; i < 24; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, dirtGen, decoRNG, dest.getX(), 0, dest.getZ(), sbb);
		}

		// mycelium!
		for (int i = 0; i < 16; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, myceliumBlobGen, decoRNG, dest.getX(), 0, dest.getZ(), sbb);
		}

		// uberous!
		for (int i = 0; i < 16; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, smallUberousGen, decoRNG, dest.getX(), 0, dest.getZ(), sbb);

			generateAtSurface(world, generator, smallUberousGen, decoRNG, dest.getX(), dest.getZ(), sbb);
		}

		// mushglooms first
		for (int i = 0; i < 16; i++) {
			BlockPos.MutableBlockPos dest = getCoordsInCave(decoRNG);
			setBlockStateRotated(world, Blocks.MYCELIUM.defaultBlockState(), dest.getX(), dest.setY(0).getY(), dest.getZ(), this.rotation, sbb);
			generate(world, generator, bigMushgloomGen, decoRNG, dest.getX(), dest.setY(1).getY(), dest.getZ(), sbb);
		}

		// mushrooms!
		for (int i = 0; i < 32; i++) {
			BlockPos.MutableBlockPos dest = getCoordsInCave(decoRNG);
			setBlockStateRotated(world, Blocks.MYCELIUM.defaultBlockState(), dest.getX(), dest.setY(0).getY(), dest.getZ(), this.rotation, sbb);
			generate(world, generator, rand.nextBoolean() ? bigBrownMushroomGen : bigRedMushroomGen, decoRNG, dest.getX(), dest.setY(1).getY(), dest.getZ(), sbb);
		}

		// stone stalactites!
		for (int i = 0; i < 128; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockSpike(world, STONE_STALACTITE, dest.atY(this.height), sbb);
		}

		return true;
	}

	protected void generate(WorldGenLevel world, ChunkGenerator generator, ConfiguredFeature<?,?> feature, Random rand, int x, int y, int z, BoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getWorldX(x, z);
		int dy = getWorldY(y);
		int dz = getWorldZ(x, z);

		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isInside(pos)) {
			feature.place(world, generator, rand, pos);
		}
	}
}
