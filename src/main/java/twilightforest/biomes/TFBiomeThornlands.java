package twilightforest.biomes;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import java.util.function.Predicate;

public class TFBiomeThornlands extends TFBiomeBase {

	public TFBiomeThornlands(Builder props) {
		super(props);
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addThorns(this);
		TFBiomeDecorator.addGrassWithFern(this, 2);
		TFBiomeDecorator.addFlowers(this, 2);
		TFBiomeDecorator.addDeadBushes(this, 4);
		TFBiomeDecorator.addMushrooms(this);
	}

	@Override
	public void addSpawns() {
		super.addSpawns();

		getSpawns(EntityClassification.CREATURE).clear();
	}

	private final Predicate<Block> otherGround = block -> block == Blocks.SANDSTONE || block == Blocks.SAND || block == Blocks.CLAY || block == TFBlocks.deadrock.get() || block == TFBlocks.deadrock_cracked.get() || block == TFBlocks.deadrock_weathered.get();

	@Override
	protected ResourceLocation[] getRequiredAdvancements() {
		return new ResourceLocation[]{ TwilightForestMod.prefix("progress_troll") };
	}

	@Override
	public void enforceProgression(PlayerEntity player, World world) {
		if (!world.isRemote && player.ticksExisted % 5 == 0) {
			player.attackEntityFrom(DamageSource.MAGIC, 1.0F);
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0F, 1.0F);

			// hint monster?
			if (world.rand.nextInt(4) == 0) TFFeature.TROLL_CAVE.trySpawnHintMonster(world, player);
		}
	}
}
