package twilightforest.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.world.TFWorld;

import java.util.Random;

public class ComponentTFYetiCave extends ComponentTFHollowHill {

	public ComponentTFYetiCave() {
		super();
	}

	public ComponentTFYetiCave(TFFeature feature, World world, Random rand, int i, int x, int y, int z) {
		super(feature, world, rand, i, 2, x, y + 2, z);
	}

	/**
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	@Override
	boolean isInHill(int cx, int cz) {
		// yeti cave is square
		return cx < this.radius * 2 && cx > 0 && cz < this.radius * 2 && cz > 0;
	}

	/**
	 * @return true if the coordinates are inside the hill in 3D
	 */
	@Override
	boolean isInHill(int mapX, int mapY, int mapZ) {
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
		for (int i = 0; i < sn; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// ice stalactites!
		for (int i = 0; i < sn; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.ICE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// packed ice stalactites!
		for (int i = 0; i < sn; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.PACKED_ICE, 0.9F, true, dest[0], 1, dest[1], sbb);
		}

		// spawn alpha yeti
		final IBlockState yetiSpawner = TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.ALPHA_YETI);
		setBlockStateRotated(world, yetiSpawner, radius, 1, radius, Rotation.NONE, sbb);

		return true;
	}

}
