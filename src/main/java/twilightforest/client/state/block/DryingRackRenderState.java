package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class DryingRackRenderState extends BlockEntityRenderState {

	public Direction facing = Direction.NORTH;
	public ItemStackRenderState item = new ItemStackRenderState();
	public float renderOffset;
}
