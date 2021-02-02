package twilightforest.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.function.Supplier;

public class ChunkGeneratorTwilightVoid extends ChunkGeneratorTFBase {

	public static final Codec<ChunkGeneratorTwilightVoid> codecVoidChunk = null; /* FIXME RecordCodecBuilder.create((instance) ->
			instance.group(
					BiomeProvider.PROVIDER_CODEC.fieldOf("biome_source").forGetter((obj) -> obj.biomeProvider),
					Codec.LONG.fieldOf("seed").stable().forGetter((obj) -> obj.seed),
					DimensionSettings.field_236098_b_.fieldOf("settings").forGetter((obj) -> obj.dimensionSettings))
					.apply(instance, instance.stable(ChunkGeneratorTwilightVoid::new)));*/

	//private final boolean generateHollowTrees = TFConfig.COMMON_CONFIG.DIMENSION.skylightOaks.get();
	private final long seed;
	protected final DimensionSettings dimensionSettings;

	public ChunkGeneratorTwilightVoid(BiomeProvider provider, long seed, Supplier<DimensionSettings> settings) {
		super(provider, seed, settings, false);
		this.seed = seed;
		this.dimensionSettings = settings.get();
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return null;
	}

	@Override
	public ChunkGenerator func_230349_a_(long l) {
		return null;
	}

	@Override
	public void generateSurface(WorldGenRegion worldGenRegion, IChunk iChunk) {

	}

	@Override
	public void func_230354_a_(WorldGenRegion region) { //decorate?
		int x = region.getMainChunkX();
		int z = region.getMainChunkZ();

		//TODO: Is there a point to this?
//		rand.setSeed(getSeed(x, z));

		ChunkBitArray data = new ChunkBitArray();
		//func_222529_a(x, z, data);
		squishTerrain(data);

		// now we reload the biome array so that it's scaled 1:1 with blocks on the ground
		//this.biomesForGeneration = getBiomeProvider().getBiomesInArea(biomesForGeneration, x * 16, z * 16, 16, 16);

		ChunkPrimer primer = new DirectChunkPrimer(new ChunkPos(x, z));
		//initPrimer(primer, data); TODO : Should be do SurfaceBuilder

		//deformTerrainForFeature(x, z, region);
		//replaceBiomeBlocks(x, z, primer, biomesForGeneration);

//		generateFeatures(x, z, primer); TODO: Should move to biome decorator
//		if (generateHollowTrees) { TODO: Should be done via biome decorator
//			hollowTreeGenerator.generate(world, x, z, primer);
//		}

//		makeChunk(x, z, primer);
	}

	@Override
	public void func_230352_b_(IWorld iWorld, StructureManager structureManager, IChunk iChunk) {

	}

	@Override
	public int getHeight(int i, int i1, Heightmap.Type type) {
		return 0;
	}

	@Override
	public IBlockReader func_230348_a_(int i, int i1) {
		return null;
	}

	/*@Override
	protected void initPrimer(ChunkPrimer primer, ChunkBitArray data) {

		BlockState stone = Blocks.STONE.getDefaultState();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				Biome biome = biomesForGeneration[x & 15 | (z & 15) << 4];
				if (biome != TFBiomes.highlandsCenter.get()) continue;

				for (int y = 0; y < 256; y++) {
					if (data.get(getIndex(x, y, z))) {
						primer.setBlockState(x, y, z, stone);
					}
				}
			}
		}
	}*/

//	@Override
//	public void decorate(int region) {
//
////		BlockFalling.fallInstantly = true;
//
////		int i = x * 16;
////		int j = z * 16;
////		BlockPos blockpos = new BlockPos(i, 0, j);
//		this.rand.setSeed(this.world.getSeed());
//		long k = this.rand.nextLong() / 2L * 2L + 1L;
//		long l = this.rand.nextLong() / 2L * 2L + 1L;
//		this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
////		boolean flag = false;
//
////		ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);
//
//		//TODO: The following code block should be generated via Biome Decorator
////		for (MapGenTFMajorFeature generator : featureGenerators.values()) {
////			generator.place(world, world.getRandom(), chunkpos);
////		}
////
////		if (generateHollowTrees) {
////			hollowTreeGenerator.generateStructure(world, rand, chunkpos);
////		}
//
////		blockpos = blockpos.add(8, 0, 8);
//
////		if (TerrainGen.populate(this, this.world, this.rand, x, z, flag, PopulateChunkEvent.Populate.EventType.ICE)) {
////			for (int k2 = 0; k2 < 16; ++k2) {
////				for (int j3 = 0; j3 < 16; ++j3) {
////
//////					BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
//////					BlockPos blockpos2 = blockpos1.down();
////
////					//TODO: Handled via SurfaceBuilder
//////					if (this.world.canBlockFreezeWater(blockpos2)) {
//////						this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 16 | 2);
//////					}
////
////					//TODO: This is done via Feature now
//////					if (this.world.canSnowAt(blockpos1, true)) {
//////						this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 16 | 2);
//////					}
////				}
////			}
////		}//Forge: End ICE
//
////		ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);
//
////		BlockFalling.fallInstantly = false;
//	}

	//TODO : Should be move to biome decorator, or WorldCarver?
	/*@Override
	protected void deformTerrainForTrollCaves(ChunkPrimer primer, TFFeature nearFeature, int x, int z, int dx, int dz) {

		int radius = (nearFeature.size * 2 + 1) * 8;
		int dist = (int) Math.sqrt(dx * dx + dz * dz);
		if (dist > radius) return;

		Biome biome = biomesForGeneration[x & 15 | (z & 15) << 4];
		if (biome != TFBiomes.highlands.get()) return;

		for (int y = 0; y < 60; y++) {
			if (primer.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.STONE) {
				primer.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState(), false);
			}
		}
	}*/

	//TODO: See super
//	@Override
//	public void recreateStructures(Chunk chunk, int x, int z) {
//		super.recreateStructures(chunk, x, z);
//		if (generateHollowTrees) {
//			hollowTreeGenerator.generate(world, x, z, null);
//		}
//	}
}
