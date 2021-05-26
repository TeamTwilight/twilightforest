package twilightforest.structures.trollcave;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import twilightforest.TFFeature;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.worldgen.BlockConstants;

import java.util.List;
import java.util.Random;

public class ComponentTFTrollCaveGarden extends ComponentTFTrollCaveMain {

	private final ConfiguredFeature<?,?> myceliumBlobGen = TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(BlockConstants.MYCELIUM, FeatureSpread.func_242253_a(5, 2), 0, ImmutableList.of(BlockConstants.STONE, BlockConstants.DEADROCK))).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 10)));
	private final ConfiguredFeature<?,?> dirtGen = TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(BlockConstants.DIRT, FeatureSpread.func_242253_a(5, 2), 0, ImmutableList.of(BlockConstants.STONE, BlockConstants.DEADROCK))).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 10)));
	private final ConfiguredFeature<?,?> smallUberousGen = TFBiomeFeatures.MYCELIUM_BLOB.get().withConfiguration(new SphereReplaceConfig(BlockConstants.UBEROUS_SOIL, FeatureSpread.func_242253_a(4, 3), 0, ImmutableList.of(BlockConstants.PODZOL, BlockConstants.COARSE_DIRT, BlockConstants.DIRT))).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(60, 10)));
	private final ConfiguredFeature<?,?> bigRedMushroomGen = Features.HUGE_RED_MUSHROOM.withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 10)));
	private final ConfiguredFeature<?,?> bigBrownMushroomGen = Features.HUGE_BROWN_MUSHROOM.withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 10)));
	private final ConfiguredFeature<?,?> bigMushgloomGen = TFBiomeFeatures.BIG_MUSHGLOOM.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(15, 10)));

	public ComponentTFTrollCaveGarden(TemplateManager manager, CompoundNBT nbt) {
		super(TFTrollCavePieces.TFTCGard, nbt);
	}

	public ComponentTFTrollCaveGarden(TFFeature feature, int index, int x, int y, int z, int caveSize, int caveHeight, Direction direction) {
		super(TFTrollCavePieces.TFTCGard, feature, index);
		this.size = caveSize;
		this.height = caveHeight;
		this.setCoordBaseMode(direction);
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, size - 1, direction);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		// add a cloud
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
//		if (this.isBoundingBoxOutsideBiomes(world, sbb, highlands)) {
//			return false;
//		}

		// clear inside
		hollowCaveMiddle(world, sbb, rand, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// treasure!
		makeTreasureCrate(world, sbb);

		// dirt!
		for (int i = 0; i < 24; i++) {
			BlockPos dest = getCenterBiasedCaveCoords(decoRNG);
			generate(world, generator, dirtGen, decoRNG, dest.getX() * 4, 1, dest.getZ() * 4, sbb);
		}

		// mycelium!
		for (int i = 0; i < 16; i++) {
			BlockPos dest = getCenterBiasedCaveCoords(decoRNG);
			generate(world, generator, myceliumBlobGen, decoRNG, dest.getX() * 4, 1, dest.getZ() * 4, sbb);
		}

		// uberous!
		for (int i = 0; i < 16; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generate(world, generator, smallUberousGen, decoRNG, dest.getX() * 4, 1, dest.getZ() * 4, sbb);

			generateAtSurface(world, generator, smallUberousGen, decoRNG, dest.getX(), 60, dest.getZ(), sbb);
		}

		// mushglooms first
		for (int i = 0; i < 32; i++) {
			BlockPos dest = getCenterBiasedCaveCoords(decoRNG);
			generate(world, generator, bigMushgloomGen, decoRNG, dest.getX() * 4, 1, dest.getZ() * 4, sbb);
		}

		// mushrooms!
		for (int i = 0; i < 64; i++) {
			BlockPos dest = getCenterBiasedCaveCoords(decoRNG);
			generate(world, generator, rand.nextBoolean() ? bigBrownMushroomGen : bigRedMushroomGen, decoRNG, dest.getX() * 4, 1, dest.getZ() * 4, sbb);
		}

		// stone stalactites!
		for (int i = 0; i < 128; i++) {
			BlockPos dest = getCoordsInCave(decoRNG);
			generateBlockStalactite(world, generator, decoRNG, Blocks.STONE, 0.7F, true, dest.getX(), 3, dest.getZ(), sbb);
		}

		return true;
	}

	protected void generate(ISeedReader world, ChunkGenerator generator, ConfiguredFeature<?,?> feature, Random rand, int x, int y, int z, MutableBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos)) {
			feature.generate(world, generator, rand, pos);
		}
	}
}
