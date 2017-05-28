package twilightforest.structures;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.entity.boss.EntityTFYetiAlpha;
import twilightforest.world.TFWorld;

public class ComponentTFYetiCave extends ComponentTFHollowHill {

	private boolean yetiPlaced;

	public ComponentTFYetiCave(World world, Random rand, int i, int x, int y, int z) {
		super(world, rand, i, 2, x, y + 2, z);
	}
	
	/**
	 * 
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	@Override
	boolean isInHill(int cx, int cz)
	{
		// yeti cave is square
		return cx < this.radius * 2 && cx > 0 && cz < this.radius * 2 && cz > 0;
	}
	
	/**
	 * @return true if the coordinates are inside the hill in 3D
	 */
	@Override
	boolean isInHill(int mapX, int mapY, int mapZ)
	{
		// yeti cave is square and 16 blocks tall
		return mapX < this.radius * 2 && mapX > 0 && mapZ < this.radius * 2 && mapZ > 0 && mapY > TFWorld.SEALEVEL && mapY < TFWorld.SEALEVEL + 20;
	}
	
	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		int sn = 128;

		// fill in features
		
//		// ore or glowing stalactites! (smaller, less plentiful)
//		for (int i = 0; i < sn; i++)
//		{
//			int[] dest = getCoordsInHill2D(rand);
//			generateOreStalactite(world, dest[0], 1, dest[1], sbb);
//		}
		// stone stalactites!
		for (int i = 0; i < sn; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}		
		// ice stalactites!
		for (int i = 0; i < sn; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.ICE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// packed ice stalactites!
		for (int i = 0; i < sn; i++)
		{
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.PACKED_ICE, 0.9F, true, dest[0], 1, dest[1], sbb);
		}
		
		// spawn alpha yeti
		if (!yetiPlaced) {
			int bx = this.getXWithOffset(this.radius, this.radius);
			int by = this.getYWithOffset(0);
			int bz = this.getZWithOffset(this.radius, this.radius);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isVecInside(pos)) {
				yetiPlaced = true;
				
				EntityTFYetiAlpha yeti = new EntityTFYetiAlpha(world);
				yeti.setPosition(bx, by, bz);
				yeti.setHomePosAndDistance(pos, 30);
				
				world.spawnEntity(yeti);
			}
		}

		
		return true;
	}

}
