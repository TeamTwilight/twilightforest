package twilightforest.structures.finalcastle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.BlockTFForceField;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleBellTower21 extends ComponentTFFinalCastleMazeTower13 {

	private static final int FLOORS = 8;

	public ComponentTFFinalCastleBellTower21() {
	}

	public ComponentTFFinalCastleBellTower21(TFFeature feature, Random rand, int i, int x, int y, int z, EnumFacing direction) {
		super(feature, rand, i, x, y, z, FLOORS, 1, BlockTFCastleMagic.VALID_COLORS.get(1), direction);
		this.size = 21;
		int floors = FLOORS;
		this.height = floors * 8 + 1;
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox2(x, y, z, -6, -8, -this.size / 2, this.size - 1, this.height, this.size - 1, direction);
		this.openings.clear();
		addOpening(0, 9, size / 2, Rotation.CLOCKWISE_180);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// add foundation
		ComponentTFFinalCastleBellFoundation21 foundation = new ComponentTFFinalCastleBellFoundation21(getFeatureType(), rand, 4, this);
		list.add(foundation);
		foundation.buildComponent(this, list, rand);

		// add roof
		StructureTFComponentOld roof = new ComponentTFFinalCastleRoof13Crenellated(getFeatureType(), rand, 4, this);
		list.add(roof);
		roof.buildComponent(this, list, rand);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		// openings!
		IBlockState fieldBlock = TFBlocks.force_field
				.getDefaultState()
				.withProperty(BlockTFForceField.COLOR, BlockTFForceField.VALID_COLORS.get(4));
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			int y = 48;
			for (int x = 5; x < this.size - 4; x += 2) {
//	        	for (int wy = 0; wy < 15; wy++) {
//		       		fieldMeta = rand.nextInt(4) + 1;
//	        		this.setBlockStateRotated(world, fieldBlock, fieldMeta, x, y + wy, 0, rotation, sbb);
//	        	}
//	        	fieldMeta = rand.nextInt(5);
				this.fillBlocksRotated(world, sbb, x, y, 0, x, y + 14, 0, fieldBlock, rotation);
			}
			y = 24;
			for (int x = 1; x < this.size - 1; x += 8) {
//	        		for (int wy = 0; wy < 15; wy++) {
//		        		fieldMeta = rand.nextInt(4) + 1;
//	        			this.setBlockStateRotated(world, fieldBlock, fieldMeta, x, y + wy, 0, rotation, sbb);
//		        		fieldMeta = rand.nextInt(4) + 1;
//	        			this.setBlockStateRotated(world, fieldBlock, fieldMeta, x + 2, y + wy, 0, rotation, sbb);
//	        		}
//	        		fieldMeta = rand.nextInt(5);
				this.fillBlocksRotated(world, sbb, x, y, 0, x, y + 14, 0, fieldBlock, rotation);
//	        		fieldMeta = rand.nextInt(5);
				this.fillBlocksRotated(world, sbb, x + 2, y, 0, x + 2, y + 14, 0, fieldBlock, rotation);
			}
		}

		// sign
		this.placeSignAtCurrentPosition(world, 7, 9, 8, "Parkour area 2", "mini-boss 1", sbb);

		return true;
	}
}
