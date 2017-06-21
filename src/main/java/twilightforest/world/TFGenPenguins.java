package twilightforest.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.entity.passive.EntityTFPenguin;

import java.util.Random;


public class TFGenPenguins extends TFGenerator {

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {

		for (int i = 0; i < 10; i++) {
			int dx = (pos.getX() + rand.nextInt(8)) - rand.nextInt(8);
			int dz = (pos.getZ() + rand.nextInt(8)) - rand.nextInt(8);
			int dy = world.getTopSolidOrLiquidBlock(new BlockPos(dx, 0, dz)).getY();

			EntityTFPenguin penguin = new EntityTFPenguin(world);
			penguin.setLocationAndAngles(dx, dy, dz, rand.nextFloat() * 360.0F, 0.0F);

			world.spawnEntity(penguin);

		}

		return true;

	}

}
