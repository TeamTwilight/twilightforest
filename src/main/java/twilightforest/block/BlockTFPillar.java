package twilightforest.block;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;

public class BlockTFPillar extends RotatedPillarBlock {

	protected BlockTFPillar(Properties props) {
		super(props);
		this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y));
	}

}
