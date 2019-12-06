package twilightforest.biomes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFDeadrock;
import twilightforest.block.TFBlocks;
import twilightforest.entity.passive.EntityTFRaven;
import twilightforest.enums.DeadrockVariant;

import java.util.Random;

public class TFBiomeFinalPlateau extends TFBiomeBase {

	public TFBiomeFinalPlateau(Builder props) {
		super(props);

		//TODO: Move to SurfaceBuilderConfig
		this.topBlock = TFBlocks.deadrock.getDefaultState().withProperty(BlockTFDeadrock.VARIANT, DeadrockVariant.SURFACE);
		this.fillerBlock = TFBlocks.deadrock.getDefaultState().withProperty(BlockTFDeadrock.VARIANT, DeadrockVariant.CRACKED);

		getTFBiomeDecorator().hasCanopy = false;
		getTFBiomeDecorator().setTreesPerChunk(-999);

		this.decorator.generateFalls = false;

		// custom creature list.
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityTFRaven.class, 10, 4, 4));
	}

	//TODO: Move to SurfaceBuilder
	@Override
	public IBlockState getStoneReplacementState() {
		return TFBlocks.deadrock.getDefaultState().withProperty(BlockTFDeadrock.VARIANT, DeadrockVariant.SOLID);
	}

	@Override
	public void decorate(World world, Random rand, BlockPos pos) {}

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_troll") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 5 == 0) {
			player.attackEntityFrom(DamageSource.MAGIC, 1.5F);
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			// TODO: change this when there's a book for the castle
			if (world.rand.nextInt(4) == 0) TFFeature.TROLL_CAVE.trySpawnHintMonster(world, player);
		}
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.FINAL_CASTLE;
	}
}
