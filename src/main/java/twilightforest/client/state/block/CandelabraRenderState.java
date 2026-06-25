package twilightforest.client.state.block;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class CandelabraRenderState extends BlockEntityRenderState {

	public Direction facing = Direction.NORTH;
	public boolean onWall;
	public List<BlockModelRenderState> candleStates = new ArrayList<>();
}
