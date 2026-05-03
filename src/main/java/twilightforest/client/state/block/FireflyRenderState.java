package twilightforest.client.state.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class FireflyRenderState extends BlockEntityRenderState {

	public Direction facing = Direction.NORTH;
	public int yaw;
	public float rotation;
	public float glowIntensity;
}
