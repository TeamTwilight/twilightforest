package twilightforest.world.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.entity.passive.EntityTFRaven;

import java.util.Random;

/**
 * A 2x2 monolith of obsidian
 *
 * @author Ben
 */
public class TFGenMonolith extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		int ht = rand.nextInt(10) + 10;
		int dir = rand.nextInt(4);
		int h0, h1, h2, h3;

		if (!isAreaSuitable(world, rand, pos, 2, ht, 2)) {
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
			setBlockAndNotifyAdequately(world, pos.add(0, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.getDefaultState() : Blocks.OBSIDIAN.getDefaultState());
		}
		for (int cy = 0; cy <= h1; cy++) {
			setBlockAndNotifyAdequately(world, pos.add(1, cy - 1, 0), cy == ht ? Blocks.LAPIS_BLOCK.getDefaultState() : Blocks.OBSIDIAN.getDefaultState());
		}
		for (int cy = 0; cy <= h2; cy++) {
			setBlockAndNotifyAdequately(world, pos.add(0, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.getDefaultState() : Blocks.OBSIDIAN.getDefaultState());
		}
		for (int cy = 0; cy <= h3; cy++) {
			setBlockAndNotifyAdequately(world, pos.add(1, cy - 1, 1), cy == ht ? Blocks.LAPIS_BLOCK.getDefaultState() : Blocks.OBSIDIAN.getDefaultState());
		}

		// spawn a few ravens nearby
		for (int i = 0; i < 2; i++) {
			BlockPos dPos = pos.add(
					rand.nextInt(8) - rand.nextInt(8),
					0,
					rand.nextInt(8) - rand.nextInt(8)
			);
			dPos = world.getTopSolidOrLiquidBlock(dPos);

			if (dPos.getY() > 0) {
				EntityTFRaven raven = new EntityTFRaven(world);
				raven.moveToBlockPosAndAngles(dPos, rand.nextFloat() * 360.0F, 0.0F);

				world.spawnEntity(raven);
			}
		}

		return true;
	}
}
