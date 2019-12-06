package twilightforest.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import twilightforest.TFFeature;
import twilightforest.entity.EntityTFKobold;
import twilightforest.entity.passive.*;
import twilightforest.util.PlayerHelper;
import twilightforest.world.ChunkGeneratorTFBase;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFBiomeBase extends Biome {

    //TODO: Move to feature decoration
	protected final WorldGenAbstractTree birchGen = new WorldGenBirchTree(false, false);

	protected final List<SpawnListEntry> undergroundMonsterList = new ArrayList<>();

	protected final ResourceLocation[] requiredAdvancements = getRequiredAdvancements();

	public final TFFeature containedFeature = getContainedFeature();

	public TFBiomeBase(Builder props) {
		super(props);

		// remove normal monster spawns
		spawnableMonsterList.clear();
		// remove squids
		spawnableWaterCreatureList.clear();
		// custom creature list.
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityTFBighorn.class, 12, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityTFBoar.class, 10, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityType.CHICKEN, 10, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityTFDeer.class, 15, 4, 5));
		spawnableCreatureList.add(new SpawnListEntry(EntityType.WOLF, 5, 4, 4));

		spawnableCreatureList.add(new SpawnListEntry(EntityTFTinyBird.class, 15, 4, 8));
		spawnableCreatureList.add(new SpawnListEntry(EntityTFSquirrel.class, 10, 2, 4));
		spawnableCreatureList.add(new SpawnListEntry(EntityTFBunny.class, 10, 4, 5));
		spawnableCreatureList.add(new SpawnListEntry(EntityTFRaven.class, 10, 1, 2));

		undergroundMonsterList.add(new SpawnListEntry(EntityType.SPIDER, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.ZOMBIE, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SKELETON, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.CREEPER, 1, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.SLIME, 10, 4, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityType.ENDERMAN, 1, 1, 4));
		undergroundMonsterList.add(new SpawnListEntry(EntityTFKobold.class, 10, 4, 8));

		spawnableCaveCreatureList.clear();
		spawnableCaveCreatureList.add(new SpawnListEntry(EntityType.BAT, 10, 8, 8));
		spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFMobileFirefly.class, 10, 8, 8));

		getTFBiomeDecorator().setTreesPerChunk(10);
		getTFBiomeDecorator().setGrassPerChunk(2);
	}

	@Override
	public float getSpawningChance() {
		// okay, 20% more animals
		return 0.12F;
	}

	//TODO: YEET
	@Override
	public TFBiomeDecorator createBiomeDecorator() {
		return new TFBiomeDecorator();
	}

	//TODO: YEET
	protected TFBiomeDecorator getTFBiomeDecorator() {
		return (TFBiomeDecorator) this.decorator;
	}

	//TODO: Move to decoration
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(5) == 0) {
			return birchGen;
		} else if (random.nextInt(10) == 0) {
			return new WorldGenBigTree(false);
		} else {
			return TREE_FEATURE;
		}
	}

	//TODO: Move to decoration
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random random) {
		if (random.nextInt(4) == 0) {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.FERN);
		} else {
			return new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
		}
	}

	//TODO: Not needed after SurfaceBuilder
	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
		this.genTwilightBiomeTerrain(world, rand, primer, x, z, noiseVal);
	}

	//TODO: Move to SurfaceBuilder
	// Copy of super's generateBiomeTerrain, relevant edits noted.
	protected void genTwilightBiomeTerrain(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
		int i = TFWorld.SEALEVEL; // TF - set sea level to 31
		IBlockState iblockstate = this.topBlock;
		IBlockState iblockstate1 = this.fillerBlock;
		IBlockState stoneReplacement = getStoneReplacementState(); // TF - Replace stone
		int j = -1;
		int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int l = x & 15;
		int i1 = z & 15;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		boolean generateBedrock = shouldGenerateBedrock(world); // TF - conditional bedrock gen

		for (int j1 = 255; j1 >= 0; --j1) {
			// TF - conditional bedrock gen
			if (generateBedrock && j1 <= rand.nextInt(5)) {
				primer.setBlockState(i1, j1, l, BEDROCK);
			} else {
				IBlockState iblockstate2 = primer.getBlockState(i1, j1, l);

				// TF - use block check for air
				if (iblockstate2.getBlock() == Blocks.AIR) {
					// j = -1; TF - commented out? todo 1.9
				} else if (iblockstate2.getBlock() == Blocks.STONE) {
					if (j == -1) {
						if (k <= 0) {
							iblockstate = AIR;
							iblockstate1 = STONE;
						} else if (j1 >= i - 4 && j1 <= i + 1) {
							iblockstate = this.topBlock;
							iblockstate1 = this.fillerBlock;
						}

						// TF - use block check for air
						if (j1 < i && (iblockstate == null || iblockstate.getBlock() == Blocks.AIR)) {
							if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
								iblockstate = ICE;
							} else {
								iblockstate = WATER;
							}
						}

						j = k;

						if (j1 >= i - 1) {
							primer.setBlockState(i1, j1, l, iblockstate);
						} else if (j1 < i - 7 - k) {
							iblockstate = AIR;
							iblockstate1 = STONE;
							primer.setBlockState(i1, j1, l, GRAVEL);
						} else {
							primer.setBlockState(i1, j1, l, iblockstate1);
						}
					} else if (j > 0) {
						--j;
						primer.setBlockState(i1, j1, l, iblockstate1);

						if (j == 0 && iblockstate1.getBlock() == Blocks.SAND) {
							j = rand.nextInt(4) + Math.max(0, j1 - 63);
							iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
						}
					} else if (stoneReplacement != null) {
						// TF - Replace stone
						primer.setBlockState(i1, j1, l, stoneReplacement);
					}
				}
			}
		}
	}

	//TODO: Move to SurfaceBuilder
	private static boolean shouldGenerateBedrock(World world) {
		ChunkGeneratorTFBase generator = TFWorld.getChunkGenerator(world);
		return generator == null || generator.shouldGenerateBedrock();
	}

	/**
	 * Return a block if you want it to replace stone in the terrain generation
	 */
	//TODO: Move to SurfaceBuilder
	@Nullable
	public IBlockState getStoneReplacementState() {
		return null;
	}

	/**
	 * Does the player have the advancements needed to be in this biome?
	 */
	public boolean doesPlayerHaveRequiredAdvancements(PlayerEntity player) {
		return PlayerHelper.doesPlayerHaveRequiredAdvancements(player, requiredAdvancements);
	}

	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[0];
	}

	/**
	 * Do something bad to a player in the wrong biome.
	 */
	public void enforceProgression(PlayerEntity player, World world) {}

	protected void trySpawnHintMonster(PlayerEntity player, World world) {
		if (world.rand.nextInt(4) == 0) {
			containedFeature.trySpawnHintMonster(world, player);
		}
	}

	protected TFFeature getContainedFeature() {
		return TFFeature.NOTHING;
	}

	/**
	 * Returns the list of underground creatures.
	 */
	public List<SpawnListEntry> getUndergroundSpawnableList(EntityClassification type) {
		return type == EntityClassification.MONSTER ? this.undergroundMonsterList : getSpawns(type);
	}
}
