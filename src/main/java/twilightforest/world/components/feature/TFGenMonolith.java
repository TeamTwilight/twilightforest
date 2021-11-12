package twilightforest.world.components.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import twilightforest.entity.TFEntities;
import twilightforest.entity.passive.Raven;
import twilightforest.util.FeatureUtil;

import java.util.Random;

/**
 * A 2x2 monolith of obsidian
 *
 * @author Ben
 */
public class TFGenMonolith extends Feature<NoneFeatureConfiguration> {

	public TFGenMonolith(Codec<NoneFeatureConfiguration> configIn) {
		super(configIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		Random rand = ctx.random();

		int ht = rand.nextInt(10) + 10;
		int dir = rand.nextInt(4);
		int h0, h1, h2, h3;

		if (!FeatureUtil.isAreaSuitable(world, pos, 2, ht, 2)) {
			return false;
		}

		switch (dir) {
			case 0:
				h0 = ht;
				h1 = (int) (ht * .75);
				h2 = (int) (ht * .75);
				h3 = (int) (ht * .5);
				break;
			case 1:
				h0 = (int) (ht * .5);
				h1 = ht;
				h2 = (int) (ht * .75);
				h3 = (int) (ht * .75);
				break;
			case 2:
				h0 = (int) (ht * .75);
				h1 = (int) (ht * .5);
				h2 = ht;
				h3 = (int) (ht * .75);
				break;
			case 3:
			default:
				h0 = (int) (ht * .75);
				h1 = (int) (ht * .75);
				h2 = (int) (ht * .5);
				h3 = ht;
				break;
		}

		for (int cy = 0; cy <= h0; cy++) {
			world.setBlock(pos.offset(0, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), 3);
		}
		for (int cy = 0; cy <= h1; cy++) {
			world.setBlock(pos.offset(1, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), 3);
		}
		for (int cy = 0; cy <= h2; cy++) {
			world.setBlock(pos.offset(0, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), 3);
		}
		for (int cy = 0; cy <= h3; cy++) {
			world.setBlock(pos.offset(1, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.defaultBlockState() : Blocks.OBSIDIAN.defaultBlockState(), 3);
		}

		// spawn a few ravens nearby
		for (int i = 0; i < 2; i++) {
			BlockPos dPos = pos.offset(
					rand.nextInt(8) - rand.nextInt(8),
					0,
					rand.nextInt(8) - rand.nextInt(8)
			);
			dPos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dPos);

			if (dPos.getY() > 0) {
				Raven raven = new Raven(TFEntities.RAVEN, world.getLevel());
				raven.moveTo(dPos, rand.nextFloat() * 360.0F, 0.0F);

				world.addFreshEntity(raven);
			}
		}

		return true;
	}
}
