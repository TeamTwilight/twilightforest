package twilightforest.biomes;

import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.enums.FireJetVariant;
import twilightforest.world.TFGenFireJet;
import twilightforest.world.TFGenVines;
import twilightforest.world.TFWorld;

import java.util.Random;

public class TFBiomeFireSwamp extends TFBiomeBase {

	public TFBiomeFireSwamp(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setDeadBushPerChunk(2);
		getTFBiomeDecorator().setMushroomsPerChunk(8);
		getTFBiomeDecorator().setReedsPerChunk(4);
		getTFBiomeDecorator().setClayPerChunk(1);
		getTFBiomeDecorator().setTreesPerChunk(3);
		getTFBiomeDecorator().setWaterlilyPerChunk(6);

		((TFBiomeDecorator) decorator).hasCanopy = false;
		getTFBiomeDecorator().lavaPoolChance = 0.125F;
		getTFBiomeDecorator().mangrovesPerChunk = 3;
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(3) == 0) {
			new WorldGenShrub(
					Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
					Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK));
		}

		return SWAMP_FEATURE;
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		super.decorate(world, rand, pos);

		TFFeature nearFeature = TFFeature.getNearestFeature(pos.getX() >> 4, pos.getZ() >> 4, world);
		if (nearFeature.areChunkDecorationsEnabled) {
			TFGenVines vines = new TFGenVines();
			BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos(0, 0, 0);
			for (int i = 0; i < 20; i++) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int ry = TFWorld.SEALEVEL + 128;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				mutPos.setPos(rx, ry, rz);
				vines.generate(world, rand, mutPos);
			}

			TFGenFireJet genSmoker = new TFGenFireJet(FireJetVariant.SMOKER);

			if (rand.nextInt(4) == 0) {
				int j = pos.getX() + rand.nextInt(14) + 8;
				byte byte0 = (byte) TFWorld.SEALEVEL;
				int k = pos.getZ() + rand.nextInt(14) + 8;
				genSmoker.generate(world, rand, new BlockPos(j, byte0, k));
			}

			TFGenFireJet genFireJet = new TFGenFireJet(FireJetVariant.JET_IDLE);

			for (int i = 0; i < 1; i++) {
				int j = pos.getX() + rand.nextInt(14) + 8;
				byte byte0 = (byte) TFWorld.SEALEVEL;
				int k = pos.getZ() + rand.nextInt(14) + 8;
				genFireJet.generate(world, rand, new BlockPos(j, byte0, k));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) {
		return 0x572e23;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) {
		return 0x64260f;
	}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ new ResourceLocation(TwilightForestMod.ID, "progress_labyrinth") };
	}

	@Override
	public void enforceProgession(EntityPlayer player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.setFire(8);
		}
		// hint monster?
		if (world.rand.nextInt(4) == 0) {
			TFFeature.hydraLair.trySpawnHintMonster(world, player);
		}
	}
}
