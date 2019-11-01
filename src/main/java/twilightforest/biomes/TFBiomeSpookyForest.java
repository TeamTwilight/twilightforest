package twilightforest.biomes;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.TFBlocks;
import twilightforest.entity.EntityTFSkeletonDruid;
import twilightforest.enums.PlantVariant;
import twilightforest.features.TFGenGraveyard;
import twilightforest.world.TFWorld;
import twilightforest.world.feature.TFGenCanopyTree;
import twilightforest.world.feature.TFGenFallenLeaves;
import twilightforest.world.feature.TFGenLampposts;
import twilightforest.world.feature.TFGenTallGrass;
import twilightforest.world.feature.TFGenWebs;

import java.util.Random;

public class TFBiomeSpookyForest extends TFBiomeBase {

	private final WorldGenerator tfGenWebs = new TFGenWebs();
	private final WorldGenerator tfGenLeaf = new TFGenFallenLeaves();
	private final WorldGenerator tfGenLampposts = new TFGenLampposts(Blocks.LIT_PUMPKIN.getDefaultState());
	private final WorldGenerator worldGenMushgloom = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.MUSHGLOOM));
	private final WorldGenerator worldGenDeadBush = new TFGenTallGrass(TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, PlantVariant.DEADBUSH), 8);
	private final WorldGenerator graveyardGen = new TFGenGraveyard();
	private final WorldGenerator worldGenPumpkin = new WorldGenPumpkin();

	public TFBiomeSpookyForest(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setFlowersPerChunk(1);
		getTFBiomeDecorator().setGrassPerChunk(4);
		getTFBiomeDecorator().setTreesPerChunk(2);
		getTFBiomeDecorator().hasCanopy = false;

		spawnableCreatureList.add(new SpawnListEntry(EntityBat.class, 20, 8, 8));

		spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 50, 1, 4));
		spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 20, 1, 4));
		spawnableMonsterList.add(new SpawnListEntry(EntityTFSkeletonDruid.class, 5, 1, 1));
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		super.decorate(world, rand, pos);

		float canopyPerChunk = 1.7F;

		// add canopy trees
		int nc = (int) canopyPerChunk + ((rand.nextFloat() < (canopyPerChunk - (int) canopyPerChunk)) ? 1 : 0);
		for (int i = 0; i < nc; i++) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			BlockPos genPos = new BlockPos(rx, world.getHeight(rx, rz), rz);
			((TFGenCanopyTree) getTFBiomeDecorator().canopyTreeGen).generate(world, rand, genPos, false);
		}

		// shroom
		if (rand.nextInt(24) == 0) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = TFWorld.getGroundLevel(world, rx, rz);
			// mushglooms
			this.worldGenMushgloom.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// webs
		for (int i = 0; i < 36; i++) {
			int rx = pos.getX() + rand.nextInt(30) + 2;
			int rz = pos.getZ() + rand.nextInt(30) + 2;
			int ry = TFWorld.SEALEVEL + rand.nextInt(TFWorld.CHUNKHEIGHT - TFWorld.SEALEVEL);

			this.tfGenWebs.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// lamps with PUMPKINSSSSSS
		if (rand.nextInt(2) == 0) {
			int rx = pos.getX() + rand.nextInt(30) + 2;
			int rz = pos.getZ() + rand.nextInt(30) + 2;
			int ry = TFWorld.getGroundLevel(world, rx, rz);

			this.tfGenLampposts.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// extra pumpkins
		if (rand.nextInt(16) == 0) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			int ry = TFWorld.getGroundLevel(world, rx, rz);

			this.worldGenPumpkin.generate(world, rand, new BlockPos(rx, ry, rz));
		}

		// Dried Bush
		for (int i = 0; i < 6; i++) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			worldGenDeadBush.generate(world, rand, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}

		// Leaf Piles
		for (int i = 0; i < 6; i++) {
			int rx = pos.getX() + rand.nextInt(25) + 2;
			int rz = pos.getZ() + rand.nextInt(25) + 2;
			tfGenLeaf.generate(world, rand, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}

		// graveyards for spooky forests
		if (rand.nextFloat() < 0.05F) {
			int rx = pos.getX() + rand.nextInt(16) + 8;
			int rz = pos.getZ() + rand.nextInt(16) + 8;
			graveyardGen.generate(world, rand, new BlockPos(rx, world.getHeight(rx, rz), rz));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return 0xC45123;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		return 0xFF8501;
	}
}
