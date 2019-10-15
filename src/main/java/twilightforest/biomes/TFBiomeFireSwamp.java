package twilightforest.biomes;

import net.minecraft.block.BlockLeaves;
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
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.enums.FireJetVariant;
import twilightforest.world.feature.TFGenFireJet;
import twilightforest.world.feature.TFGenVines;
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

		getTFBiomeDecorator().hasCanopy = false;
		getTFBiomeDecorator().lavaPoolChance = 0.125F;
		getTFBiomeDecorator().mangrovesPerChunk = 3;
	}

	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random random) {
		if (random.nextInt(3) == 0) {
			return new WorldGenShrub(
					Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE),
					Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false)
			);
		} else {
			return SWAMP_FEATURE;
		}
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {
		super.decorate(world, rand, pos);

		TFFeature nearFeature = TFFeature.getNearestFeature(pos.getX() >> 4, pos.getZ() >> 4, world);
		if (nearFeature.areChunkDecorationsEnabled) {
			BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();

			WorldGenerator vines = new TFGenVines();
			for (int i = 0; i < 20; i++) {
				int rx = pos.getX() + rand.nextInt(16) + 8;
				int ry = TFWorld.SEALEVEL + 128;
				int rz = pos.getZ() + rand.nextInt(16) + 8;
				vines.generate(world, rand, mutPos.setPos(rx, ry, rz));
			}

			WorldGenerator genSmoker = new TFGenFireJet(FireJetVariant.SMOKER);
			if (rand.nextInt(4) == 0) {
				int rx = pos.getX() + rand.nextInt(14) + 8;
				int ry = TFWorld.SEALEVEL;
				int rz = pos.getZ() + rand.nextInt(14) + 8;
				genSmoker.generate(world, rand, mutPos.setPos(rx, ry, rz));
			}

			WorldGenerator genFireJet = new TFGenFireJet(FireJetVariant.JET_IDLE);
			for (int i = 0; i < 1; i++) {
				int rx = pos.getX() + rand.nextInt(14) + 8;
				int ry = TFWorld.SEALEVEL;
				int rz = pos.getZ() + rand.nextInt(14) + 8;
				genFireJet.generate(world, rand, mutPos.setPos(rx, ry, rz));
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
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_labyrinth") };
	}

	@Override
	public void enforceProgression(EntityPlayer player, World world) {
		if (!world.isRemote && player.ticksExisted % 60 == 0) {
			player.setFire(8);
		}
		trySpawnHintMonster(player, world);
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.HYDRA_LAIR;
	}
}
