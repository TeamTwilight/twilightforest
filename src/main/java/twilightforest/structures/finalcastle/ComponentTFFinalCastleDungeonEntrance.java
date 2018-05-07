package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFCastleDoor;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.BlockTFForceField;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFDecoratorCastle;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleDungeonEntrance extends ComponentTFFinalCastleDungeonRoom31 {
	public boolean hasExit = false;

	public ComponentTFFinalCastleDungeonEntrance() {
	}

	public ComponentTFFinalCastleDungeonEntrance(TFFeature feature, Random rand, int i, int x, int y, int z, EnumFacing direction, int level) {
		super(feature, rand, i, x, y, z, direction, level);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		this.deco = new StructureTFDecoratorCastle();
		this.deco.blockState = TFBlocks.castle_rune_brick
				.getDefaultState()
				.withProperty(BlockTFCastleMagic.COLOR, BlockTFCastleMagic.VALID_COLORS.get(2));

		this.deco.fenceState = TFBlocks.force_field
				.getDefaultState()
				.withProperty(BlockTFForceField.COLOR, BlockTFForceField.VALID_COLORS.get(1));

		// this is going to be the parent for all rooms on this level
		super.buildComponent(this, list, rand);
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		final IBlockState stairs = deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH);
		final IBlockState deadRock = TFBlocks.deadrock.getDefaultState();
		// stairs
		for (int y = 0; y <= this.height; y++) {
			int x = (this.size / 2) - 2;
			int z = (this.size / 2) - y + 2;

			this.fillWithBlocks(world, sbb, x, y, z, x + 4, y, z, stairs, stairs, false);
			this.fillWithBlocks(world, sbb, x, 0, z, x + 4, y - 1, z, deadRock, deadRock, false);
		}

		// door
		final IBlockState castleDoor = TFBlocks.castle_door.getDefaultState()
				.withProperty(BlockTFCastleDoor.LOCK_INDEX, 2);
		this.fillWithBlocks(world, sbb, 23, 0, 12, 23, 3, 14, castleDoor, AIR, false);
		this.fillWithBlocks(world, sbb, 23, 4, 12, 23, 4, 14, deco.blockState, deco.blockState, false);


		return true;
	}

	@Override
	protected EnumDyeColor getForceFieldMeta(Random decoRNG) {
		return BlockTFForceField.VALID_COLORS.get(1);
	}

	@Override
	protected EnumDyeColor getRuneMeta(EnumDyeColor fieldMeta) {
		return BlockTFCastleMagic.VALID_COLORS.get(0);
	}
}
