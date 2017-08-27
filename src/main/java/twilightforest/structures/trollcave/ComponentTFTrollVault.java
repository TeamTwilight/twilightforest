package twilightforest.structures.trollcave;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFTreasure;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponent;

import java.util.Random;

public class ComponentTFTrollVault extends StructureTFComponent {

	public ComponentTFTrollVault() {
	}

	public ComponentTFTrollVault(int index, int x, int y, int z) {
		super(index);
		this.setCoordBaseMode(EnumFacing.SOUTH);


		// adjust x, y, z
		x = (x >> 2) << 2;
		y = (y / 4) * 4;
		z = (z >> 2) << 2;

		// spawn list!
		this.spawnListIndex = -1;

		this.boundingBox = StructureTFComponent.getComponentToAddBoundingBox(x, y, z, -8, 0, -8, 12, 12, 12, EnumFacing.SOUTH);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// make walls
		this.fillWithBlocks(world, sbb, 0, 0, 0, 11, 11, 11, TFBlocks.giantObsidian.getDefaultState(), TFBlocks.giantObsidian.getDefaultState(), false);

		// clear inside
		this.fillWithAir(world, sbb, 4, 4, 4, 7, 7, 7);

		// cobblestone platform
		this.fillWithBlocks(world, sbb, 5, 5, 5, 6, 5, 6, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);

		// chests
		this.setBlockState(world, Blocks.CHEST.getDefaultState(), 5, 6, 5, sbb);
		this.placeTreasureAtCurrentPosition(world, rand, 5, 6, 6, TFTreasure.troll_vault, false, sbb);

		this.placeTreasureAtCurrentPosition(world, rand, 6, 6, 5, TFTreasure.troll_garden, true, sbb);
		this.setBlockState(world, Blocks.TRAPPED_CHEST.getDefaultState(), 6, 6, 6, sbb);

		return true;
	}

}
