package twilightforest.structures;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;

import java.util.List;
import java.util.Random;

public class ComponentTFHydraLair extends ComponentTFHollowHill {

	public ComponentTFHydraLair() {
		super();
	}

	public ComponentTFHydraLair(TFFeature feature, World world, Random rand, int i, int x, int y, int z) {
		super(feature, world, rand, i, 2, x, y + 2, z);
	}

	@Override
	public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> list, Random random) {
		;
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		int stalacts = 64;
		int stalags = 8;

		// fill in features
		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < stalacts; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateOreStalactite(world, dest[0], 1, dest[1], sbb);
		}
		// stone stalactites!
		for (int i = 0; i < stalacts; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < stalags; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, Blocks.STONE, 0.9F, false, dest[0], 1, dest[1], sbb);
		}

		// boss spawner seems important
		setBlockState(world, TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.HYDRA), 27, 3, 27, sbb);

		return true;
	}

}
