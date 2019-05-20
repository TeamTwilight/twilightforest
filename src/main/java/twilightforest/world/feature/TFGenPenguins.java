package twilightforest.world.feature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.entity.passive.EntityTFPenguin;

import java.util.Random;

public class TFGenPenguins extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		for (int i = 0; i < 10; i++) {
			BlockPos dPos = pos.add(
					rand.nextInt(8) - rand.nextInt(8),
					0,
					rand.nextInt(8) - rand.nextInt(8)
			);
			dPos = world.getTopSolidOrLiquidBlock(dPos);

			if (dPos.getY() > 0) {
				EntityTFPenguin penguin = new EntityTFPenguin(world);
				penguin.moveToBlockPosAndAngles(dPos, rand.nextFloat() * 360.0F, 0.0F);

				world.spawnEntity(penguin);
			}
		}

		return true;
	}
}
