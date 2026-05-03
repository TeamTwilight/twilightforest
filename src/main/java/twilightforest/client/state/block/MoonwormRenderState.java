package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class MoonwormRenderState extends BlockEntityRenderState {

	public Direction facing = Direction.NORTH;
	public float yaw;
	public float rotation;
	public float wiggleRotation;
	public int delay;
}
