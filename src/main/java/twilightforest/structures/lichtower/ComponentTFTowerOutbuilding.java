package twilightforest.structures.lichtower;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;

import java.util.List;
import java.util.Random;


public class ComponentTFTowerOutbuilding extends ComponentTFTowerWing {

	public ComponentTFTowerOutbuilding() {
		super();
	}

	protected ComponentTFTowerOutbuilding(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * NO BEARDS!
	 */
	@Override
	public void makeABeard(StructureComponent parent, List<StructureComponent> list, Random rand) {
		return;
	}

	/**
	 * Outbuildings should not make new wings close to the ground.
	 */
	@Override
	public boolean makeTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation direction) {
		if (y > 7) {
			return super.makeTowerWing(list, rand, index, x, y, z, wingSize, wingHeight, direction);
		} else {
			return false;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		final IBlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				this.replaceAirAndLiquidDownwards(world, cobblestone, x, -1, z, sbb);
			}
		}
		return super.addComponentParts(world, rand, sbb);
	}


}
